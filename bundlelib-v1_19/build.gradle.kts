plugins {
    conventions.java
    conventions.`maven-publish`
    conventions.`paperweight-adapter`
}

version = "1.0"

dependencies {
    compileOnly(project(":bundlelib-adapter"))
    implementation(project(":bundlelib-paperweight"))
    paperweight.paperDevBundle("1.19-R0.1-SNAPSHOT")
}