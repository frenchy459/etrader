package Layer3.Repos;

import Layer1.Entities.Item;
import Layer2.Interfaces.EntityRepo;

import java.io.*;
import java.util.*;

/**
 * The gateway class ItemsRepo is responsible for reading and writing Item objects to a file..
 */
public class ItemsRepo implements EntityRepo<UUID, Item> {

    private final HashMap<UUID, Item> itemHashMap = new HashMap<>();

    /**
     * Instantiates a new Items repo.
     */
    public ItemsRepo() {
        reload();
    }

    @Override
    public void reload() {
        try (FileInputStream fi = new FileInputStream("phase2/repoFiles/items.bin")) {

            ObjectInputStream os = new ObjectInputStream(fi);

            Object obj;
            Item item;

            try {
                while ((obj = os.readObject()) != null) {
                    if (obj instanceof Item) {
                        item = (Item) obj;
                        itemHashMap.put(item.getUUID(), item);
                    }
                }
            } catch (Exception ignored) { }

            os.close();

        } catch (IOException e) {
            System.out.println("[phase2/repoFiles/items.bin] not found");
        }
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(itemHashMap.values());
    }

    @Override
    public List<Item> getFilteredList(List<UUID> keys) {
        ArrayList<Item> items = new ArrayList<>();
        for (UUID uuid : keys)
            items.add(itemHashMap.get(uuid));
        return items;
    }

    @Override
    public void add(Item item) {
        itemHashMap.put(item.getUUID(), item);
    }

    @Override
    public Item get(UUID uuid) {
        return itemHashMap.get(uuid);
    }

    @Override
    public void remove(UUID uuid) {
        itemHashMap.remove(uuid);
    }

    @Override
    public void onExit() {
        try (FileOutputStream fs = new FileOutputStream("phase2/repoFiles/items.bin")) {

            ObjectOutputStream os = new ObjectOutputStream(fs);

            for (Item item : itemHashMap.values())
                os.writeObject(item);
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
