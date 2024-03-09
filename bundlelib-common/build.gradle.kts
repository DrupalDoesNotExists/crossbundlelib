plugins {
    conventions.java
}

version = "1.0"

dependencies {
    compileOnly("org.jetbrains:annotations:+")
    compileOnly(project(":bundlelib-adapter"))
}