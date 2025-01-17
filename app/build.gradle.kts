plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.smilz_pdm_pa"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.smilz_pdm_pa"
        minSdk = 26
        //noinspection EditedTargetSdkVersion
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth:22.1.1")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    // Kotlin coroutines e lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1") // ViewModel com corrotinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4") // Suporte para corrotinas
    implementation ("org.apache.poi:poi-ooxml:5.2.3")
    implementation ("org.apache.poi:poi:5.2.3")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // Material Design (necessário para NavigationView e FloatingActionButton)
    implementation ("com.google.android.material:material:1.9.0")

    // RecyclerView (se ainda não tiver)
    implementation ("androidx.recyclerview:recyclerview:1.3.1")

    // Core UI components (AppCompat para suporte a ActionBar e DrawerToggle)
    implementation ("androidx.appcompat:appcompat:1.6.1'")
    implementation ("androidx.drawerlayout:drawerlayout:1.2.0")

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


}