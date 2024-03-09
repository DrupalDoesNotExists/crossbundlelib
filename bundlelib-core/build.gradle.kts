import distribution.asConsumer
import distribution.mainSourcesAttributes

plugins {
    conventions.java
    conventions.`maven-publish`
}

version = "1.0"

val mainSources by configurations.creating<Configuration> {
    asConsumer()
    mainSourcesAttributes(objects)
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    implementation(project(":bundlelib-adapter"))
    mainSources(project(":bundlelib-adapter"))
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
