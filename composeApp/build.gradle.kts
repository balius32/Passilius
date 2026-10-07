import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.androidx.room)
}

kotlin {
  androidTarget {
    compilerOptions {
      jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
  }
  jvm("desktop")

  jvmToolchain(17)

  sourceSets {
    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.materialIconsExtended)
      implementation(compose.ui)
      implementation(compose.components.resources)
      implementation(libs.androidx.lifecycle.viewmodel)
      implementation(libs.androidx.lifecycle.viewmodel.compose)
      implementation(libs.androidx.lifecycle.runtime.compose)
      implementation(libs.androidx.room.runtime)
      implementation(libs.androidx.sqlite.bundled)
      implementation(libs.haze)
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.kotlinx.serialization.json)
    }

    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.core.ktx)
      implementation(libs.androidx.biometric)
      implementation(libs.kotlinx.coroutines.android)
    }

    val desktopMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.kotlinx.coroutines.swing)
      }
    }

    commonTest.dependencies {
      implementation(libs.kotlinx.coroutines.core)
    }
  }
}

dependencies {
  add("kspAndroid", libs.androidx.room.compiler)
  add("kspDesktop", libs.androidx.room.compiler)
}

room {
  schemaDirectory("$projectDir/schemas")
}

android {
  namespace = "com.example"
  compileSdk = 37

  defaultConfig {
    applicationId = "com.aistudio.vault.qmxzld"
    minSdk = 24
    targetSdk = 37
    versionCode = 1
    versionName = "1.0"
  }

  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
    val debugKeystore = file("${rootDir}/debug.keystore")
    if (debugKeystore.exists()) {
      create("debugConfig") {
        storeFile = debugKeystore
        storePassword = "android"
        keyAlias = "androiddebugkey"
        keyPassword = "android"
      }
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug {
      if (file("${rootDir}/debug.keystore").exists()) {
        signingConfig = signingConfigs.getByName("debugConfig")
      }
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildFeatures {
    buildConfig = true
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

compose.desktop {
  application {
    mainClass = "com.example.MainKt"
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "Passilius"
      packageVersion = "1.0.0"
    }
  }
}
