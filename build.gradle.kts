plugins {
    id("java")
    id("application")
}

group = "wtf.jishe.tictacbot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://m2.dv8tion.net/releases")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.dv8tion:JDA:5.5.1")
    implementation("ch.qos.logback:logback-classic:1.5.13")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("wtf.jishe.tictacbot.Main")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}