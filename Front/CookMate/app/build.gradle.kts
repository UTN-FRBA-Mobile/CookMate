plugins {
/*    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android-extensions")*/
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

}

android {
    namespace = "com.utn.cookmate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.utn.cookmate"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    // Jetpack Compose dependencies
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material:material:1.5.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
    implementation("androidx.compose.runtime:runtime:1.5.0")
    implementation("androidx.compose.foundation:foundation:1.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-beta01")

    implementation("androidx.compose.material3:material3:1.0.1")
    // Jetpack Compose Navigation dependencies
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

    /*implementation("androidx.navigation:navigation-compose:2.6.1")*/
/*    implementation("androidx.navigation:navigation-fragment-ktx:2.5.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.1")*/

    // Gson library
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("io.socket:socket.io-client:1.0.0")
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.messaging.ktx)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.0.5")

    implementation("androidx.compose.material:material-icons-extended:1.4.0")




}
