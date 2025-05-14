plugins {
    id("com.android.application")
    id("com.google.gms.google-services")

}

    android {
    namespace = "com.example.fooddeliveryapp"
        compileSdk = 35

        defaultConfig {
        applicationId = "com.example.fooddeliveryapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
        buildToolsVersion = "35.0.0"
    }

dependencies {
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.google.android.gms:play-services-maps:17.0.1")

        implementation ("com.google.android.gms:play-services-maps:18.0.0")
        implementation ("com.google.android.gms:play-services-location:18.0.0")


    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.google.firebase:firebase-auth:23.2.0")
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation ("com.google.firebase:firebase-database:20.3.0")

}