setupShadowJar()

dependencies {
    compileOnly(Dependencies.LOMBOK)
    compileOnly(Dependencies.SPIGOT)
    annotationProcessor(Dependencies.LOMBOK)
    compileOnly(Dependencies.REFLECTIONS)
    implementation(files("libs/AtlasUniversal.jar"))

}