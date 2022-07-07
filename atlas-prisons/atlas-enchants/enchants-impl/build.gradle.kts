setupShadowJar()

dependencies {
    compileOnly(Dependencies.LOMBOK)
    annotationProcessor(Dependencies.LOMBOK)
    compileOnly(Dependencies.SPIGOT)
    implementation(project(":enchants-api"))
    implementation(project(":mines-api"))

}