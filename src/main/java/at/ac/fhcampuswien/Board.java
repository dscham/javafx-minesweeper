package at.ac.fhcampuswien;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class Board {
    @Getter
    private static final int CELL_SIZE = 15;
    @Getter
    private static final int COLS = 25;
    @Getter
    private static final int ROWS = 25;
    @Getter
    private static final int NUM_MINES = 50;
    private int cellsUncovered = 0;
    private int minesMarked = 0;
    private boolean gameOver = false;
    private Map<Vec2, Cell> cells = new HashMap<>();

    @Getter(AccessLevel.PRIVATE)
    private final Set<Vec2> neighbourHelper = Set.of(
            new Vec2(-1, -1),
            new Vec2(-1, 0),
            new Vec2(-1, 1),
            new Vec2(0, -1),
            new Vec2(0, 1),
            new Vec2(1, -1),
            new Vec2(1, 0),
            new Vec2(1, 1)
    );

    public Board() {
        for (int i = 0; i < ROWS * COLS; i++) {
            Vec2 pos = new Vec2(i % COLS, i / COLS);
            cells.put(pos, new Cell(pos, CellState.NEW));
        }

        for (int i = 0; i < NUM_MINES; i++) {
            Vec2 pos = Vec2.getRandom(0, COLS - 1, 0, ROWS - 1);
            System.out.println("Platziere Bombe auf " + pos.getCol() + " | " + pos.getRow());
            cells.get(pos).setBomb(true);
        }

        cells.forEach((vec, cell) -> cell.setNeighbours(computeNeighbours(vec.getCol(), vec.getRow())));
    }

    public boolean uncover(int row, int col) {
        Cell cell = cells.get(new Vec2(col, row));

        if (cell.getState() == CellState.FLAG) {
            if (gameOver && !cell.isBomb()) {
                cell.update(CellState.WRONG_FLAG);
            }
            return false;
        }

        if (cell.isBomb()) {
            cell.update(CellState.BOMB);
            gameOver = true;
        }

        if (cell.getState() == CellState.NEW) {
            cell.update(CellState.values()[
                    (int) cell.getNeighbours().stream()
                            .filter(neighbour -> neighbour.isBomb())
                            .count()
                    ]
            );
        }

        if (cell.getState() == CellState.ZERO_NEIGHBOURS) {
            uncoverEmptyCells(cell);
        }

        cellsUncovered = (int) cells.entrySet().stream()
                .filter(entry -> entry.getValue().getState() != CellState.NEW )
                .count();

        return true;
    }

    public boolean markCell(int row, int col) {
        Cell cell = cells.get(new Vec2(col, row));
        if (cell.getState() == CellState.NEW) {
            cell.update(CellState.FLAG);
        } else if (cell.getState() == CellState.FLAG) {
            cell.update(CellState.NEW);
        }

        minesMarked = (int) cells.entrySet().stream()
                .filter(entry -> entry.getValue().getState() == CellState.FLAG)
                .count();

        return true;
    }

    public void uncoverEmptyCells(Cell cell) {
        cell.getNeighbours().stream()
                .filter(neighbour -> !neighbour.isBomb() && neighbour.getState() == CellState.NEW)
                .forEach(neigbour -> uncover(neigbour.getPosition().getRow(), neigbour.getPosition().getCol()));
    }

    public void uncoverAllCells() {
        cells.forEach((vec, cell) -> uncover(vec.getRow(), vec.getCol()));
    }

    public Set<Cell> computeNeighbours(int col, int row) {
        return neighbourHelper.stream()
                .map(entry -> cells.get(new Vec2(col + entry.getCol(), row + entry.getRow())))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
