plugins {
    id ("com.android.library")
}
android {
    namespace = "com.klinker.android.send_message"
    compileSdk = 25

    defaultConfig {
        minSdk = 25
        targetSdk = 25
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
    useLibrary("org.apache.http.legacy")
}

dependencies {
    implementation("com.squareup.okhttp:okhttp:2.5.0")
    implementation ("com.squareup.okhttp:okhttp-urlconnection:2.5.0")
    //implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
}