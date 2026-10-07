buildscript {
    val agp_version by extra("8.3.0")
    val agp_version1 by extra("9.4.1")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "9.4.1" apply false
    id("com.google.devtools.ksp") version "2.3.12" apply false
}