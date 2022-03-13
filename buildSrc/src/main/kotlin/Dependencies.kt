// Contains all version information for out dependencies.
object Versions {
    const val SPIGOT_VERSION = "1.12.2-R0.1-SNAPSHOT"
    const val LOMBOK_VERSION = "1.18.20"
    const val REFLECTIONS_VERSION = "0.10.2";
    const val WORLDGUARDWRAPPER = "1.2.0-SNAPSHOT"

}

object Dependencies {
    const val SPIGOT = "org.spigotmc:spigot-api:${Versions.SPIGOT_VERSION}"
    const val LOMBOK = "org.projectlombok:lombok:${Versions.LOMBOK_VERSION}"
    const val REFLECTIONS = "org.reflections:reflections:${Versions.REFLECTIONS_VERSION}"
    const val WORLDGUARDWRAPPER = "org.codemc.worldguardwrapper:worldguardwrapper:${Versions.WORLDGUARDWRAPPER}"

}