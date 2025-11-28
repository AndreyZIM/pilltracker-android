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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PillTracker"
include(":app")
include(":core")
include(":core:ui")
include(":core:common")
include(":feature:reminders:api")
include(":feature:reminders:impl")
include(":feature:aids:api")
include(":feature:aids:impl")
