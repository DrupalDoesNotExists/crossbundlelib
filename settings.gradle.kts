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
include("bundlelib-v1_18")
include("bundlelib-v1_19")
include("bundlelib-v1_20")
include("bundlelib-v1_16")
