plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ntutee.team3.JavaFinalProject"
    compileSdk = 34

    defaultConfig {
        applicationId = "ntutee.team3.JavaFinalProject"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("armeabi", "armeabi-v7a", "x86", "mips","x86_64"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(project(":unityLibrary"))
    implementation(fileTree(mapOf("dir" to "../unityLibrary/libs", "include" to listOf("*.jar"))))
}