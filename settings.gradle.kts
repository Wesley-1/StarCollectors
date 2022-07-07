rootProject.name = "AtlasPrisons"

setupPrisonsModule(
            "atlas-prisons",
        listOf(
            Pair("atlas-mines", listOf("mines-api", "mines-impl")),
            Pair("atlas-enchants", listOf("enchants-api", "enchants-impl")),
            Pair("atlas-robots", listOf("robots-api", "robots-impl")),
            Pair("atlas-gangs", listOf("gangs-api", "gangs-impl")),
            Pair("worldedit", listOf("v6", "v7")),
            Pair("core", listOf("config", "module", "item", "database", "commands", "menus", "utils", "packets" +
                    ""))))


fun setupPrisonsModule(base: String, setup: List<Pair<String, List<String>>>) =
    setup.forEach { pair
        -> pair.second.forEach { name
            -> setupSubproject(name, file("$base/${pair.first}/$name"))
        }
    }


fun setupSubproject(name: String, projectDirectory: File) = setupSubproject(name) {
    projectDir = projectDirectory
}

inline fun setupSubproject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name)
    project(":$name").apply(block)
}