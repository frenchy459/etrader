package Layer3.Repos;
import Layer1.Entities.Item;
import Layer1.Entities.User;
import Layer1.Enums.Picture;
import Layer2.Interfaces.ImageEntityRepo;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageRepo implements ImageEntityRepo<String, Image> {

    private final HashMap<String, Image> imageHashMap = new HashMap<>();
    private Item item;
    private Picture repoState;
    private User user;

    /**
     * Instantiates a new Image user repo.
     */
    public ImageRepo() {
        reload();
    }

    @Override
    public Image get(String str) {
        return imageHashMap.get(str);
    }

    @Override
    public void add(Image image) {

        if (this.repoState.equals(Picture.ITEM)) {
            imageHashMap.put(item.getUUID().toString(), image);
        }
        else if (this.repoState.equals(Picture.USER_PROFILE)) {
            imageHashMap.put(user.getUsername(), image);
        }
    }

    @Override
    public void remove(String str) {
        imageHashMap.remove(str);
    }

    @Override
    public List<Image> getAll() {
        return new ArrayList<>(imageHashMap.values());
    }

    @Override
    public List<Image> getFilteredList(List<String> keys) {
        ArrayList<Image> images = new ArrayList<>();
        for (String str : keys)
            images.add(imageHashMap.get(str));
        return images;
    }

    @Override
    public void reload() {
        File folder = new File("phase2/repoFiles/images/");
        File[] fileList = folder.listFiles();

        if (fileList != null) {
            for (File file: fileList) {
                String name = file.getName();
                String fileName = name.replaceFirst("[.][^.]+$", "");
                imageHashMap.put(fileName, new Image(file.toURI().toString()));
            }
        }
    }

    @Override
    public void onExit() {
        try {
            Path path = Paths.get("phase2/repoFiles/images");
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            for (String str: imageHashMap.keySet()) {
                File outputFile = new File("phase2/repoFiles/images/" + str + ".png");
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(imageHashMap.get(str), null), "png", outputFile);
                }
                catch (IllegalArgumentException e) {
                    ImageIO.write(SwingFXUtils.fromFXImage(
                            new Image(new FileInputStream("phase2/resources/error-image.png")),
                            null), "png", outputFile);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the Current item of this image repo
     * @param item an item object
     */
    public void setCurrentItem(Item item) {
        this.item = item;
        this.repoState = Picture.ITEM;
    }

    /**
     * Sets the current user of this image repo
     * @param user an user object
     */
    public void setCurrentUser(User user) {
        this.user = user;
        this.repoState = Picture.USER_PROFILE;
    }

    /**
     * Return true if the image exists in the image repo
     * @param key a string representing the key of an image
     * @return a boolean, true if the key is contained in the image repo
     */
    public boolean imageExists(String key) {
        return imageHashMap.containsKey(key);
    }
}
