plugins {
    kotlin("jvm")
    id("com.apollographql.apollo3") version "3.5.0"
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}

dependencies {
    // implementation(project(mapOf("path" to ":model")))
    // implementation(project(mapOf("path" to ":database")))
    // implementation(project(mapOf("path" to ":datastore")))

    implementation("javax.inject:javax.inject:1")
    implementation("org.jsoup:jsoup:1.15.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    api("com.kttdevelopment:mal4j:2.9.0")
    api("com.apollographql.apollo3:apollo-runtime:3.5.0")

    implementation("dev.datlag.jsunpacker:jsunpacker:1.0.1")

    api("dev.datlag.flower-ktorfit:flower-ktorfit:3.0.0-beta02")
    api("io.ktor:ktor-client-android:2.0.3")
    api("io.ktor:ktor-client-cio:2.0.3")
    api("io.ktor:ktor-client-content-negotiation:2.0.3")
    api("io.ktor:ktor-serialization-kotlinx-json:2.0.3")
    ksp("de.jensklingenberg.ktorfit:ktorfit-ksp:1.0.0-beta09")
}

apollo {
    service("anilist") {
        srcDir("src/main/graphql/anilist")
        packageName.set("de.datlag.network.anilist")

        introspection {
            endpointUrl.set("https://graphql.anilist.co")
            schemaFile.set(file("src/main/graphql/anilist/schema.graphqls"))
        }
    }
    service("github") {
        srcDir("src/main/graphql/github")
        packageName.set("de.datlag.network.github")

        introspection {
            headers.set(mapOf("Authorization" to "Bearer token"))
            endpointUrl.set("https://api.github.com/graphql")
            schemaFile.set(file("src/main/graphql/github/schema.graphqls"))
        }
    }
}