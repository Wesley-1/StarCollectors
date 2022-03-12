setupShadowJar()

dependencies {
    compileOnly(Dependencies.LOMBOK)
    annotationProcessor(Dependencies.LOMBOK)
    compileOnly(Dependencies.SPIGOT)
    implementation(files("libs/AtlasUniversal.jar"))
    implementation(project(":enchants-api"))
    implementation(project(":mines-api"))
    //
}