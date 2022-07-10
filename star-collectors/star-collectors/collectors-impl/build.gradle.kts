setupShadowJar()

dependencies {
    compileOnly(Dependencies.LOMBOK)
    annotationProcessor(Dependencies.LOMBOK)
    compileOnly(Dependencies.SPIGOT)
    implementation(Dependencies.LUCKO_HELPER)
    implementation(Dependencies.LUCKO_SQL)
    implementation(project(":collectors-api"))

}