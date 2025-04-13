plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)

    //Kotlinx Serialization - было 1.9.20
    kotlin("plugin.serialization") version "1.9.20"
    id("co.touchlab.skie") version "0.10.1" // https://github.com/touchlab/SKIE

    //id("org.jetbrains.kotlin.native.cocoapods") version "2.0.21" // Добавьте эту строку
}




kotlin {


    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true


        }
    }



    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            isStatic = false
            baseName = "shared"
        }
    }

    //Dependencies versions
    val coroutinesVersion = "1.6.4"
    val ktorVersion = "2.3.1"
    val koinVersion = "3.3.2"
    val datastoreVersion = "1.1.1"
    val cinteropVersion = "1.9.20" // Добавляем версию cinterop




    sourceSets {


        //commonMain sourceSet
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

            //implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

            //implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")

            //Use api so that the android app can use it as well
            api("io.insert-koin:koin-core:$koinVersion")

            //либа хранилища данных андройд
            implementation("androidx.datastore:datastore-preferences-core:$datastoreVersion")
            //data time lib
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
            implementation(libs.multiplatformSettings)




        }

        //TEST
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        //AndroidMain sourceSet
        androidMain.dependencies {
            api("io.insert-koin:koin-android:$koinVersion")

            implementation("io.ktor:ktor-client-android:$ktorVersion")
            api("androidx.datastore:datastore-preferences:$datastoreVersion")
        }


        //iOSMain sourceSet
        iosMain.dependencies{
            implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            //для @ObjCName(name = "KoinHelperKt")
            //implementation("org.jetbrains.kotlinx:kotlinx-cinterop-runtime:$cinteropVersion")
        }



    }



}

android {
    namespace = "com.example.socialapp"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
