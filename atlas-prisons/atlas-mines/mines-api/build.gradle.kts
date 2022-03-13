setupShadowJar()

dependencies {
    compileOnly(Dependencies.LOMBOK)
    compileOnly(Dependencies.SPIGOT)
    annotationProcessor(Dependencies.LOMBOK)
    compileOnly(Dependencies.REFLECTIONS)
    compileOnly(Dependencies.WORLDGUARDWRAPPER)
    implementation(project(":v6"))
    implementation(project(":v7"))

}