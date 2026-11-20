import java.io.File
import java.util.Properties
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

val keystoreProperties = Properties().apply {
  val local = rootProject.file("keystore.properties")
  if (local.isFile) {
    local.inputStream().use { load(it) }
  }
}

fun releaseSigningValue(propertyKey: String, envKey: String): String? =
  keystoreProperties.getProperty(propertyKey)?.takeIf { it.isNotBlank() }
    ?: System.getenv(envKey)?.takeIf { it.isNotBlank() }

val releaseKeystorePath = releaseSigningValue("storeFile", "KEYSTORE_PATH")
  ?: "${rootDir}/my-upload-key.jks"
val releaseStorePassword = releaseSigningValue("storePassword", "STORE_PASSWORD")
val releaseKeyPassword = releaseSigningValue("keyPassword", "KEY_PASSWORD")
val releaseKeyAlias = releaseSigningValue("keyAlias", "KEY_ALIAS") ?: "upload"
val releaseKeystore = rootProject.file(releaseKeystorePath)
val hasReleaseSigning = releaseKeystore.isFile &&
  !releaseStorePassword.isNullOrBlank() &&
  !releaseKeyPassword.isNullOrBlank()

android {
  namespace = "com.example"
  compileSdk = 37

  defaultConfig {
    applicationId = "com.balius.passilius"
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
        keyAlias = releaseKeyAlias
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

base {
  archivesName.set("passilius")
}

fun renameReleaseArtifact(dir: File, extension: String, newFileName: String) {
  if (!dir.isDirectory) return
  dir.listFiles()
    ?.filter { it.isFile && it.extension.equals(extension, ignoreCase = true) && it.name != newFileName }
    ?.forEach { source ->
      val target = File(dir, newFileName)
      if (target.exists()) target.delete()
      if (!source.renameTo(target)) {
        source.copyTo(target, overwrite = true)
        source.delete()
      }
    }
  if (extension == "apk") {
    val metadata = File(dir, "output-metadata.json")
    if (metadata.isFile) {
      metadata.writeText(
        metadata.readText().replace(
          Regex("\"outputFile\"\\s*:\\s*\"[^\"]+\""),
          "\"outputFile\": \"$newFileName\"",
        ),
      )
    }
  }
}

val releaseSigningOk = hasReleaseSigning
val releaseSigningError =
  "Release signing needs keystore.properties (or STORE_PASSWORD/KEY_PASSWORD env vars) " +
    "and an existing keystore. See keystore.properties.example."

tasks.configureEach {
  if (name != "assembleRelease" && name != "bundleRelease") return@configureEach
  val ok = releaseSigningOk
  val msg = releaseSigningError
  doFirst {
    if (!ok) {
      throw org.gradle.api.GradleException(msg)
    }
  }
  doLast {
    when (name) {
      "assembleRelease" -> {
        listOf(
          layout.buildDirectory.dir("outputs/apk/release").get().asFile,
          layout.projectDirectory.dir("release").asFile,
        ).forEach { renameReleaseArtifact(it, "apk", "passilius.apk") }
      }
      "bundleRelease" -> {
        listOf(
          layout.buildDirectory.dir("outputs/bundle/release").get().asFile,
          layout.projectDirectory.dir("release").asFile,
        ).forEach { renameReleaseArtifact(it, "aab", "passilius.aab") }
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
