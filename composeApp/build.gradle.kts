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
      implementation(libs.compose.ui.backhandler)
      implementation(compose.components.resources)
      implementation(libs.androidx.lifecycle.viewmodel)
      implementation(libs.androidx.lifecycle.viewmodel.compose)
      implementation(libs.androidx.lifecycle.runtime.compose)
      implementation(libs.androidx.room.runtime)
      implementation(libs.androidx.sqlite.bundled)
      implementation(libs.haze)
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.kotlinx.serialization.json)
      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.cio)
    }

    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.core.ktx)
      implementation(libs.androidx.biometric)
      implementation(libs.androidx.appcompat)
      implementation(libs.kotlinx.coroutines.android)
      implementation(libs.zxing.android.embedded)
      implementation(libs.zxing.core)
    }

    val desktopMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.kotlinx.coroutines.swing)
        implementation(libs.ktor.server.core)
        implementation(libs.ktor.server.cio)
        implementation(libs.zxing.core)
      }
    }

    commonTest.dependencies {
      implementation(libs.kotlinx.coroutines.core)
      implementation(kotlin("test"))
    }

    val desktopTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
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

val releaseKeystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
val releaseStorePassword = System.getenv("STORE_PASSWORD")
val releaseKeyPassword = System.getenv("KEY_PASSWORD")
val releaseKeystore = file(releaseKeystorePath)
val hasReleaseSigning = releaseKeystore.isFile &&
  !releaseStorePassword.isNullOrBlank() &&
  !releaseKeyPassword.isNullOrBlank()

android {
  namespace = "com.example"
  compileSdk = 37

  defaultConfig {
    applicationId = "io.github.balius32.passilius"
    minSdk = 24
    targetSdk = 37
    versionCode = 1
    versionName = "1.0"
  }

  signingConfigs {
    create("release") {
      if (hasReleaseSigning) {
        storeFile = releaseKeystore
        storePassword = requireNotNull(releaseStorePassword)
        keyAlias = "upload"
        keyPassword = requireNotNull(releaseKeyPassword)
      }
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
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      if (hasReleaseSigning) {
        signingConfig = signingConfigs.getByName("release")
      }
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

tasks.configureEach {
  if (name == "assembleRelease" || name == "bundleRelease") {
    doFirst {
      if (!hasReleaseSigning) {
        error(
          "Release signing needs an existing keystore plus STORE_PASSWORD and KEY_PASSWORD. " +
            "KEYSTORE_PATH defaults to my-upload-key.jks in the project root. The alias is upload."
        )
      }
    }
  }
}

compose.desktop {
  application {
    mainClass = "com.example.MainKt"
    val packagingJdk = System.getenv("COMPOSE_PACKAGE_JDK")
    if (!packagingJdk.isNullOrBlank()) {
      javaHome = packagingJdk
    }
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "Passilius"
      packageVersion = "1.0.0"
    }
  }
}
