package com.rollonit.edugame;

/**
 * Represents the state the grid.
 * It stores the location of the cell that is currently active.
 */
public class CellState {
    final int totalCellsX = 3;
    final int totalCellsY = 3;
    int activeCellX;
    int activeCellY;

    /**
     * Initialises the state and sets the active cell to a random cell in the grid.
     */
    public CellState() {
        activeCellX = (int) (Math.random() * totalCellsX);
        activeCellY = (int) (Math.random() * totalCellsY);
    }

    /**
     * Initialises the state and sets the active cell to the cell at the given coordinates.
     *
     * @param activeCellX the x coordinate of the cell
     * @param activeCellY the y coordinate of the cell
     */
    public CellState(int activeCellX, int activeCellY) {
        this.activeCellX = activeCellX;
        this.activeCellY = activeCellY;
    }

    /**
     * Sets the active cell to the cell at the given coordinates.
     *
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     */
    public void setActiveCell(int x, int y) {
        activeCellX = x;
        activeCellY = y;
    }
}
