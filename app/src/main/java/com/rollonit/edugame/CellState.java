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

    /**
     * Checks if the given coordinates are the coordinates of the active cell.
     *
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return true if the given coordinates are the coordinates of the active cell
     */
    public boolean isActiveCell(int x, int y) {
        return activeCellX == x && activeCellY == y;
    }

    /**
     * Checks the equality of the given state with this state.
     *
     * @param other the state to compare with
     * @return true if the given state is equal to this state
     */
    public boolean equals(CellState other) {
        if (other == null) return false;
        return activeCellX == other.activeCellX && activeCellY == other.activeCellY;
    }

    public int getActiveCellX() {
        return activeCellX;
    }

    public int getActiveCellY() {
        return activeCellY;
    }
}
