# Asclepius - Cancer Detector App

Asclepius adalah aplikasi Android untuk **deteksi kanker berbasis AI on-device** menggunakan TensorFlow Lite. Pengguna cukup memilih foto dari galeri, aplikasi langsung menganalisis gambar secara lokal di perangkat (tanpa upload ke server), menampilkan hasil klasifikasi beserta persentase probabilitasnya, dan menyimpan seluruh riwayat scan ke database lokal. Selain itu, aplikasi menyediakan berita kesehatan terbaru seputar kanker dari NewsAPI.org.

> ⚠️ **Disclaimer:** Hasil deteksi dari aplikasi ini bersifat edukatif/informatif dan **bukan pengganti diagnosis medis**. Selalu konsultasikan dengan dokter untuk diagnosis yang akurat.

## 📌 Project Preview

<div style="text-align: center">
    <img src="https://raw.githubusercontent.com/aldnazr/android-tensorflow/refs/heads/main/preview/1.jpg" width="250"/>
    <img src="https://raw.githubusercontent.com/aldnazr/android-tensorflow/refs/heads/main/preview/2.jpg" width="250"/>
    <img src="https://raw.githubusercontent.com/aldnazr/android-tensorflow/refs/heads/main/preview/3.jpg" width="250"/>
    <img src="https://raw.githubusercontent.com/aldnazr/android-tensorflow/refs/heads/main/preview/4.jpg" width="250"/>
    <img src="https://raw.githubusercontent.com/aldnazr/android-tensorflow/refs/heads/main/preview/5.jpg" width="250"/>
    <img src="https://raw.githubusercontent.com/aldnazr/android-tensorflow/refs/heads/main/preview/6.jpg" width="250"/>
</div>

## ✨ Fitur

- 🖼️ **Pilih gambar dari galeri** dengan fitur *crop* gambar (Android Image Cropper).
- 🤖 **Deteksi kanker on-device** menggunakan model TensorFlow Lite (`cancer_classification.tflite`) — analisis berjalan sepenuhnya di perangkat, lebih cepat & privat.
- 📊 **Hasil klasifikasi + probabilitas** — menampilkan label deteksi beserta persentase keyakinannya.
- 🗂️ **Riwayat scan** — setiap hasil scan otomatis tersimpan ke Room Database dan dapat dilihat kembali, termasuk hapus riwayat dengan *swipe* kiri/kanan.
- 📰 **Berita kesehatan kanker** — mengambil top headlines kategori *health* (kata kunci "cancer") dari NewsAPI.org, lengkap dengan halaman detail artikel dan tautan ke situs sumber.
- 🌙 **Dark mode** & tampilan **edge-to-edge**.

## 🛠️ Tech Stack

| Komponen | Teknologi |
| --- | --- |
| Bahasa | Kotlin (JVM Target 17) |
| Minimum SDK | Android 8.0 (API 26) |
| Target/Compile SDK | Android 14 (API 34) |
| UI | View Binding, Material Components, RecyclerView, ViewModels + LiveData |
| ML | TensorFlow Lite Task Vision (`tensorflow-lite-task-vision:0.4.4`) |
| Database | Room (`room-runtime:2.6.1`) + KSP |
| Network | Retrofit 2 + OkHttp Interceptor + Gson |
| Image Loading & Cropping | Glide, Android Image Cropper |
| Build | Gradle Kotlin DSL, AGP 8.6.0, Kotlin 1.9.23 |

## 📚 Alur Kerja (How it Works)

```
SplashActivity → MainActivity (berita)
                    │
                    └─ FAB → ScanActivity
                              │  (pilih gambar dari galeri + crop)
                              ▼
                 TensorFlow Lite (TensorImage → CancerClassification)
                              │
                              ├─ Hasil: "Terdeteksi: <label> <persentase>"
                              ├─ Simpan ke Room (ScanHistory)
                              ▼
                         ResultActivity
```

1. **Splash** (`SplashActivity`) — layar pembuka 2 detik dengan system UI tersembunyi.
2. **Home** (`MainActivity`) — menampilkan daftar berita kanker dari NewsAPI.org via Retrofit; artikel `[Removed]` atau tanpa gambar difilter.
3. **Scan** (`ScanActivity`) — pengguna memilih gambar dari galeri, mencropp-nya, lalu `TensorImage.fromBitmap()` diproses oleh `CancerClassification` (binding yang dibuat otomatis dari file model). Probabilitas tertinggi diambil sebagai hasil akhir.
4. **Result** (`ResultActivity`) — menampilkan gambar dan hasil analisis.
5. **Riwayat** (`RiwayatActivity`) — daftar semua scan dari Room Database; item bisa dihapus dengan swipe.
6. **Detail Berita** (`NewsDetail`) — menampilkan gambar, judul, deskripsi artikel, dan tombol *Read Article* yang membuka browser.

## 📁 Struktur Proyek

```
android-tensorflow/
├── app/
│   ├── src/main/
│   │   ├── ml/
│   │   │   └── cancer_classification.tflite   # Model TFLite
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/dicoding/asclepius/
│   │   │   ├── adapter/                 # Adapter RecyclerView
│   │   │   │   ├── HistoryAdapter.kt
│   │   │   │   └── NewsAdapter.kt
│   │   │   ├── custom/                  # Custom view
│   │   │   │   └── EdgeSafeBottomNavigationView.kt
│   │   │   ├── data/                    # Data class respons API
│   │   │   │   └── NewsResponse.kt
│   │   │   ├── database/                # Room
│   │   │   │   ├── ScanHistoryRepository.kt
│   │   │   │   ├── dao/ScanHistoryDao.kt
│   │   │   │   ├── entity/ScanHistory.kt
│   │   │   │   └── local/ScanHistoryDatabase.kt
│   │   │   ├── model/                   # ViewModel
│   │   │   │   ├── HistoryModel.kt
│   │   │   │   └── NewsModel.kt
│   │   │   ├── retrofit/                # Konfigurasi & service API
│   │   │   │   ├── ApiConfig.kt
│   │   │   │   └── ApiService.kt
│   │   │   ├── ui/                      # Activity
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── NewsDetail.kt
│   │   │   │   ├── ResultActivity.kt
│   │   │   │   ├── RiwayatActivity.kt
│   │   │   │   ├── ScanActivity.kt
│   │   │   │   └── SplashActivity.kt
│   │   │   └── util/Time.kt
│   │   └── res/                         # Resources (layout, drawable, values)
│   ├── src/androidTest/                 # Instrumen test
│   └── src/test/                        # Unit test
├── build.gradle.kts                     # Root project
├── settings.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat
└── preview/                             # Screenshot preview
```

## 📦 Cara Menjalankan

1. **Clone** repository ini:
   ```bash
   git clone <url-repository>
   cd android-tensorflow
   ```
2. **Buka proyek** di Android Studio (disarankan versi Ladybug atau lebih baru agar mendukung AGP 8.6.0).
3. **Tunggu Gradle sync** selesai (perlu unduh dependensi).
4. **Siapkan API Key NewsAPI** — lihat bagian [Konfigurasi](#konfigurasi-api-key).
5. **Run** modul `app` ke emulator atau perangkat fisik (minimal Android 8.0 / API 26).

> Catatan: File model `cancer_classification.tflite` sudah termasuk di folder `app/src/main/ml/`, jadi tidak perlu langkah tambahan untuk menjalankan deteksi.

## 🔑 Konfigurasi API Key

Fitur berita mengambil data dari [NewsAPI.org](https://newsapi.org/). API key **tidak di-commit ke repo** dan dibaca saat build dengan urutan fallback:

1. `NEWS_API_KEY` di `local.properties` (root proyek) — untuk pengembangan lokal
2. Environment variable `NEWS_API_KEY` — untuk CI (GitHub Actions meng-inject dari GitHub Secrets)

Langkah:
1. Buat akun dan dapatkan API key (free tier) di [newsapi.org](https://newsapi.org/).
2. Tambahkan baris berikut ke `local.properties` (file ini sudah masuk `.gitignore`, tidak akan terkirim ke repo):
   ```properties
   NEWS_API_KEY=api_key_kamu
   ```
3. Untuk CI: tambahkan secret `NEWS_API_KEY` di GitHub (`Settings > Secrets and variables > Actions`), workflow otomatis menggunakannya saat build.
4. Sync ulang Gradle.

## 📜 Izin (Permissions)

| Izin | Tujuan |
| --- | --- |
| `INTERNET` | Mengambil berita dari NewsAPI.org |
| `READ_MEDIA_IMAGES` / `READ_MEDIA_VIDEO` | Membaca gambar dari galeri (Android 13+) |
| `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE` | Akses galeri pada Android 12 ke bawah |
| `READ_MEDIA_VISUAL_USER_SELECTED` | Pilihan parsial media (Android 14+) |

Deteksi kanker berjalan **on-device**, sehingga tidak ada izin tambahan untuk pengiriman data gambar.

## 🧪 Pengujian

- **Unit test**: `app/src/test/` (JUnit).
- **Instrumented test**: `app/src/androidTest/` (Espresso).

Jalankan dari Android Studio atau lewat Gradle:
```bash
./gradlew test          # unit test
./gradlew connectedAndroidTest   # instrumented test (butuh perangkat/emulator)
```

## 📄 Lisensi

Proyek ini untuk keperluan belajar/edukasi.

