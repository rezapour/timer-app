pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Interval timer"
include(":app")
include(":core:db")
include(":core:di")
include(":core:common")
include(":feature:workout:impl")
include(":feature:di")
include(":core:designsystem")
include(":core:timer-core")
include(":core:ui")
include(":feature:timer:impl")
include(":core:resources")
include(":feature:workout:api")
include(":feature:timer:api")
