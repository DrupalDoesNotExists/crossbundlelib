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
        create<MavenPublication>("library") {
            groupId = project.group.toString()
            artifactId = project.name

            from(components["java"])
        }
    }
}
