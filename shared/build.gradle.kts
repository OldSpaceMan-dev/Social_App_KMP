plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)

    //Kotlinx Serialization
    kotlin("plugin.serialization") version "1.9.20"
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


    //Dependencies versions
    val coroutinesVersion = "1.6.4"
    val ktorVersion = "2.3.1"
    val koinVersion = "3.3.2"
    val datastoreVersion = "1.1.1"

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
