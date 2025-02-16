plugins {
    id("java")
    id("application")
    kotlin("jvm") version "1.9.22"
}

group = "org.goandgo"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("org.goandgo.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    //Jersey 3.1.3
    implementation("org.glassfish.jersey.core:jersey-server:3.1.3")
    implementation("org.glassfish.jersey.inject:jersey-hk2:3.1.3")
    implementation("org.glassfish.jersey.containers:jersey-container-grizzly2-http:3.1.3")
    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:3.0.0")

    //API Jakarta WS-RS
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")

    //Jackson para JSON
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:3.1.3")

    // Logging
    implementation("org.slf4j:slf4j-simple:1.7.36")

    // Flyway para migração de banco de dados
    implementation("org.flywaydb:flyway-core:9.22.3")

    // Driver do PostgreSQL
    implementation("org.postgresql:postgresql:42.5.0")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    //JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    //Criptografia
    implementation(kotlin("stdlib"))
}

tasks.test {
    useJUnitPlatform()
}