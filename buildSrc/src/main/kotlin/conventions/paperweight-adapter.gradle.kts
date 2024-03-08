package conventions

plugins {
    java
    publishing
    id("com.github.johnrengelman.shadow")
    id("io.papermc.paperweight.userdev")
}

val shadowPublication = publishing.publications.create<MavenPublication>("shadow") {
    from(components["java"])
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}

shadow {
    component(shadowPublication)
}
