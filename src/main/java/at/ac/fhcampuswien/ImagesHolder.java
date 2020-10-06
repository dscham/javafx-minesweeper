package at.ac.fhcampuswien;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImagesHolder {
    public static final int NUM_IMAGES = 13;
    private static ImagesHolder instance;
    private final Image[] images;

    private ImagesHolder() {
        images = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = getClass().getResource(i + ".png").getPath().replaceAll("%20", " ");
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(path);
                this.images[i] = new Image(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static ImagesHolder getInstance() {
        if (instance == null) {
            instance = new ImagesHolder();
        }
        return instance;
    }

    public Image getImage(CellState state) {
        return images[state.ordinal()];
    }
}
