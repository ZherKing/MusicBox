plugins {
    id("io.papermc.paperweight.userdev")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.5-R0.1-SNAPSHOT")
}

