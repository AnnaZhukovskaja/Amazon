plugins {
    id("java")
}

group = "by.zhukovskaya"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.codeborne:selenide:7.0.2")
}

tasks.test {
    useJUnitPlatform()
}