plugins {
    id("java")
    id("application")
}

application {
    mainClass.set("talk.icsiscet.ConstraintPropagationConferenceScheduler")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.choco-solver:choco-solver:4.10.7")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to "talk.icsiscet.ConstraintPropagationConferenceScheduler"))
    }
}

tasks.test {
    useJUnitPlatform()
}

