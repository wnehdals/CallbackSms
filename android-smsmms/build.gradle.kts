plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.klinker.android.send_message"
    compileSdk = 34

    defaultConfig {
        minSdk = 28
        targetSdk = 34
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
    lint {
        abortOnError = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    useLibrary("org.apache.http.legacy")
}

dependencies {
    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("com.jakewharton.timber:timber:5.0.1")
    implementation("com.squareup.okhttp:okhttp:2.5.0")
    implementation ("com.squareup.okhttp:okhttp-urlconnection:2.5.0")
    //implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
}