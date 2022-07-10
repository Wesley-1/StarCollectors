package collectors.models;

import collectors.exceptions.CollectorCreationException;
import collectors.CollectorsAPI;
import collectors.interfaces.CollectorUpgrade;
import collectors.managers.CollectorManager;
import collectors.records.Collector;
import collectors.services.DataService;
import lombok.Getter;
import me.lucko.helper.hologram.Hologram;
import me.lucko.helper.serialize.Position;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the instance class, when making a collector you must make an instance of it.
 */
public class Instance {
    @Getter
    private final Player owner;
    @Getter
    private final JavaPlugin main;
    @Getter
    private Location bukkitLocation;
    @Getter
    private final ConcurrentHashMap<Location, Collector> service;
    @Getter
    private List<CollectorUpgrade> upgrades;
    @Getter
    private final Collector collector;
    @Getter
    private Hologram collectorHologram;
    @Getter
    private Chunk chunk;
    @Getter
    private final LinkedHashMap<CollectorUpgrade, Integer> upgradeLevels;

    public Instance(Collector collector) {
        this.collector = collector;
        this.owner = Bukkit.getPlayer(collector.name());
        this.main = CollectorsAPI.get();
        this.upgrades = new ArrayList<>();
        this.upgradeLevels = new LinkedHashMap<>();
        this.service = CollectorsAPI.getService();
        this.collectorHologram = Hologram.create(Position.of(bukkitLocation.add(0, 1, 0)), List.of("test"));
    }

    /**
     * @param owner Checks if the owner is equal to the player.
     * @return returns a boolean true/false based on ownership.
     */
    public boolean isOwner(Player owner) {
        return this.owner.equals(owner);
    }

    /**
     * @param chunk This allows us to set the chunk the collector is in.
     * @return returns the {@link Instance} class.
     * <p>
     * Builder Pattern.
     */
    public Instance setChunk(Chunk chunk) {
        this.chunk = chunk;
        return this;
    }

    /**
     * Resets the chunk the object was in.
     *
     * @return returns the {@link Instance} class.
     * <p>
     * Builder Pattern.
     */
    public Instance removeChunk() {
        this.chunk = null;
        return this;
    }

    /**
     * @param bukkitLocation The location that the collector will be at.
     * @return returns the {@link Instance} class.
     * <p>
     * Builder pattern.
     */
    public Instance setLocation(Location bukkitLocation) {
        this.bukkitLocation = bukkitLocation;
        return this;
    }

    /**
     * Creates the {@link Hologram} for the collector.
     */
    public void createHologram() {
        this.collectorHologram.spawn();

    }

    /**
     * Removes the {@link Hologram} for the collector.
     */
    public void removeHologram() {
        this.collectorHologram.despawn();
        this.collectorHologram = null;
    }

    /**
     * @param event {@link EntityDeathEvent} class.
     */
    public void handleEntityDeath(EntityDeathEvent event) {
        if (collector == null) return;
        if (collector.inventory().isFull()) return;
        collector.inventory().handleAddingItems(event.getDrops());
        event.getDrops().clear();
    }

    /**
     * @param upgrades The array of upgrades that the collector will have.
     * @return returns the {@link Instance} class.
     * <p>
     * Builder pattern.
     */
    public Instance registerUpgrades(CollectorUpgrade... upgrades) {
        if (this.upgrades == null) return this;
        this.upgrades.addAll(Arrays.asList(upgrades));
        this.upgrades.forEach($ -> {
            this.upgradeLevels.put($, 1);
        });

        return this;
    }

    /**
     * @return returns the {@link CollectorManager} class.
     * <p>
     * Builds the {@link CollectorManager} class.
     */
    public CollectorManager build() {
        try {
            return new CollectorManager(this);
        } catch (CollectorCreationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
