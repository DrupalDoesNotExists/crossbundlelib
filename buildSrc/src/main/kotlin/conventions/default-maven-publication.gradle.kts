package conventions

import org.gradle.kotlin.dsl.publishing

plugins {
    publishing
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("default") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}
