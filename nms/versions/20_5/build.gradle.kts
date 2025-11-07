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
    paperweight.paperDevBundle("1.20.5-R0.1-SNAPSHOT") {
        exclude(group = "net.kyori")
    }
    
    // 手动添加稳定版本的 adventure 依赖
    compileOnly("net.kyori:adventure-api:4.17.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.17.0")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.17.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.17.0")
    compileOnly("net.kyori:adventure-text-serializer-plain:4.17.0")
}