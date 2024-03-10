plugins {
    conventions.java
    conventions.`maven-publish`
}

version = "1.0"

dependencies {
    compileOnly(project(":bundlelib-adapter"))
    implementation(project(":bundlelib-common"))
    compileOnly(files("lib/paper-1.16.5.jar"))
}