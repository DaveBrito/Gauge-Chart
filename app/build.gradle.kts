plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.gauge_chart"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gauge_chart"
        minSdk = 29
        targetSdk = 34
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
}

dependencies {
    //Implementado dependências do repositório
    implementation ("com.github.Gruzer:simple-gauge-android:0.3.1")
    //Implementação do MQTT para o projeto.
    implementation ("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")



    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}