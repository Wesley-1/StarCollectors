import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class CollectorsAPI {

    private Reflections reflections;
    private static JavaPlugin plugin;

    /**
     *
     * @return returns the main class.
     *
     */
    public static JavaPlugin get() {
        if (plugin == null) {
            throw new CollectorCreationException("CollectorsAPI not binded.");
        }
        return plugin;
    }

    /**
     *
     * @param clazz The main class that will be binded to the collections class.
     *
     * @return returns the {@link CollectorsAPI} class.
     */
    public CollectorsAPI bind(Class<? extends JavaPlugin> clazz) {

        this.reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(clazz.getName())));
        this.plugin = JavaPlugin.getProvidingPlugin(clazz);

        return this;
    }
}
