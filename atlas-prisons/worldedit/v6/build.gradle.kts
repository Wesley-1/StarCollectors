setupShadowJar()

dependencies {
    compileOnly(Dependencies.LOMBOK)
    compileOnly(Dependencies.SPIGOT)
    annotationProcessor(Dependencies.LOMBOK)
    compileOnly(Dependencies.REFLECTIONS)
    compileOnly(Dependencies.WORLDGUARDWRAPPER)
 //   implementation(files("libs/worldedit-bukkit-7.2.9.jar"))
    implementation(files("libs/worldedit-6.1.9.jar"))

}