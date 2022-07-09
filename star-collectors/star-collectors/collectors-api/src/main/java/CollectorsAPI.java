import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class CollectorsAPI {

    private Reflections reflections;

    public CollectorsAPI() {

    }

    /**
     *
     * @param clazz The main class that will be binded to the collections class.
     *
     * @return returns the {@link CollectorsAPI} class.
     */
    public CollectorsAPI bind(Class<? extends JavaPlugin> clazz) {
        this.reflections = new Reflections(
                new ConfigurationBuilder().addUrls(ClasspathHelper.forPackage(clazz.getName())));
        return this;
    }
}
