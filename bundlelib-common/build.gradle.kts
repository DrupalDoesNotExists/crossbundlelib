plugins {
    conventions.java
    conventions.`maven-publish`
}

version = "1.0"

dependencies {
    compileOnly("org.jetbrains:annotations:+")
    compileOnly(project(":bundlelib-adapter"))
}