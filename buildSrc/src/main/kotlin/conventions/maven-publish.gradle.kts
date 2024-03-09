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
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = project.name

            from(components["java"])
        }
    }
}
