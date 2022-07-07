setupShadowJar()

dependencies {
    compileOnly(Dependencies.LOMBOK)
    annotationProcessor(Dependencies.LOMBOK)
    compileOnly(Dependencies.SPIGOT)
    compileOnly(Dependencies.WORLDGUARDWRAPPER)
    implementation(project(":mines-api"))
    implementation(project(":enchants-api"))
}