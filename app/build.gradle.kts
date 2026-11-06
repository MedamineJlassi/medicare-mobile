plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.medicaremobile"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.medicaremobile"
        minSdk = 27
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ⚡ C'est cette ligne qui manquait pour satisfaire AppAuth dans le Manifest
        manifestPlaceholders["appAuthRedirectScheme"] = "com.example.medicaremobile"
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
    // AndroidX & UI
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.androidx.activity.ktx)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // AppAuth (Pour se connecter avec Keycloak via OIDC/PKCE)
    implementation("net.openid:appauth:0.11.1")

    // Retrofit (Pour les requêtes HTTP vers le Backend Spring Boot)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.auth0.android:jwtdecode:2.0.2")
}