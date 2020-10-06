package at.ac.fhcampuswien;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Controller {
    private Board board;

    private boolean isActive;

    @FXML
    private Label message;
    @FXML
    private GridPane grid;
    @FXML
    private Button restart;

    @FXML
    public void initialize() {
        System.out.println("-----New Game-----");
        message.setText("laden...");
        isActive = true;
        this.board = new Board();
        this.board.getCells().forEach((vec, cell) -> grid.add(cell, vec.getCol(), vec.getRow()));
        message.setText("Finde die " + Board.getNUM_MINES() + " Minen!");
    }

    @FXML
    public void update(MouseEvent event) {
        if (isActive) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                int col = (int) event.getX() / Board.getCELL_SIZE();
                int row = (int) event.getY() / Board.getCELL_SIZE();

                setMarkerText();

                if (event.getButton() == MouseButton.PRIMARY) {
                    if (board.uncover(row, col)) {
                        if (board.isGameOver()) {
                            System.out.println("----Game Over!----");
                            message.setText("Sorry. Leider verloren.");
                            board.uncoverAllCells();
                            isActive = false;
                        }
                    } else {
                        message.setText("Da ist vielleicht eine Mine ;o");
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    board.markCell(row, col);
                    setMarkerText();
                }

                if (board.getCellsUncovered() == (Board.getROWS() * Board.getCOLS() - Board.getNUM_MINES())
                        && board.getMinesMarked() == Board.getNUM_MINES()) {
                    System.out.println("-----Gewonnen!----");
                    message.setText("Glückwunsch! Du hast gewonnen.");
                    isActive = false;
                }
            }
        }
    }

    private void setMarkerText() {
        message.setText(" Marker: " + board.getMinesMarked() + " / " + Board.getNUM_MINES()
                + "Geklickt: " + board.getCellsUncovered()
                + " / " + (Board.getROWS() * Board.getCOLS() - Board.getNUM_MINES()));
    }

    @FXML
    public void restart(ActionEvent actionEvent) {
        grid.getChildren().clear();
        initialize();
    }
}
