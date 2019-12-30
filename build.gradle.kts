plugins {
    java
    kotlin("jvm") version "1.3.61"
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}


val kotlinVersion = "1.3.61"
val springBootVesrion = "2.2.2.RELEASE"


buildscript {
    val kotlinVersion = "1.3.61"
    val springBootVesrion = "2.2.2.RELEASE"

    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVesrion")
    }
}

repositories {
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
}


apply(plugin = "java")
apply(plugin = "kotlin")
apply(plugin = "io.spring.dependency-management")


subprojects {
    buildscript {
        repositories {
            mavenCentral()
            maven("https://plugins.gradle.org/m2/")
        }

        dependencies {
            classpath(kotlin("gradle-plugin", version = "1.3.61"))
            classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVesrion")
        }
    }

    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")


    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
            mavenBom("com.fasterxml.jackson:jackson-bom:2.10.1")
            mavenBom("com.linecorp.armeria:armeria-bom:0.96.0")
            mavenBom("com.linecorp.armeria:armeria-line-bom:0.96.0.1")
            mavenBom("io.micrometer:micrometer-bom:1.3.2")
        }
    }


    dependencies {
        // Used compile/coding time.
        implementation("org.springframework.boot:spring-boot-starter-logging:2.2.2.RELEASE") // 実際は starter-web の依存とかに入ってることが多い
        testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVesrion}")
    }

    configurations.all {
        exclude(group = "log4j") // = Old only log4j impl.
        exclude(group = "org.apache.logging.log4j", module = "log4j-slf4j-impl")// = SLF4J > Log4J 2.x. (Apache Side)
        exclude(group = "org.slf4j", module = "slf4j-log4j12") // = SLF4J > Log4J 1.x. (SLF4J Side)
        exclude(group = "org.slf4j", module = "slf4j-jdk14") // = SLF4J > JDK14.
        exclude(group = "commons-logging", module = "commons-logging-api") // Replaced by jcl-over-slf4j
        exclude(group = "commons-logging", module = "commons-logging") // Implementation is not needed.
    }

    // config JVM target to 1.8 for kotlin compilation tasks
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }
}
