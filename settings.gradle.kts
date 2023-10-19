pluginManagement {
    repositories {
        google()
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

rootProject.name = "Anymaster Chats"
include(":app")
include(":core-remote-api")
include(":core-remote-impl")
include(":feauture-chat")
include(":core-repository-api")
include(":core-repository-impl")
include(":core")
include(":core-ui")
include(":core-local-api")
include(":core-local-impl")
