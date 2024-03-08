package conventions

plugins {
    publishing
    `maven-publish`
}

publishing {
    repositories {
        maven(rootProject.layout.buildDirectory.dir("maven-internal")) {
            name = "MavenInternal"
        }
    }

    publications {
        create<MavenPublication>("default") {
            from(components["java"])
        }
    }
}
