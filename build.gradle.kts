buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
