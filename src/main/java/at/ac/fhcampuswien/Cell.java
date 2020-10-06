package at.ac.fhcampuswien;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class Cell extends Pane {
    @Getter
    @Setter
    private Set<Cell> neighbours = new HashSet<>();
    @Getter
    @Setter
    private boolean bomb;
    @Getter
    private CellState state;
    @Getter
    private Vec2 position;

    private ImageView view;
    private ImagesHolder imagesHolder = ImagesHolder.getInstance();

    public Cell(Vec2 position, CellState state) {
        this.position = position;
        this.state = state;
        view = new ImageView(imagesHolder.getImage(state));
        getChildren().add(view);
    }

    public void update(CellState state) {
        this.state = state;
        this.view.setImage(imagesHolder.getImage(state));
    }
}
