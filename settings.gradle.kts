@file:Suppress("UnstableApiUsage")

rootProject.name = "crossbundlelib"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

include("bundlelib-adapter")
include("bundlelib-common")
include("bundlelib-core")
include("bundlelib-v1_17")
include("bundlelib-paperweight")
