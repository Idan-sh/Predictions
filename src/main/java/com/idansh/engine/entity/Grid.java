package com.idansh.engine.entity;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * a Two dimensional grid that contains the world's entity instances.
 * Each point in the grid can contain one entity instance at most.
 * The entity instances in the grid can move on each tick in one of four directions:
 * LEFT, RIGHT, TOP, BOTTOM.
 * On each tick every entity in the grid will move in a random direction, while moving outside the grid
 * will move the entity to the other side of it.
 * Also, an entity cannot move into an occupied spot, so another direction will be rolled. If every side is occupied,
 * the entity will stay at its place.
 */
public class Grid {
    private enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    private final Entity[][] grid;
    private final int nofRows;
    private final int nofColumns;

    public Grid(int nofRows, int nofColumns) {
        validateGridSize(nofRows, nofColumns);

        this.grid = new Entity[nofRows][nofColumns];
        this.nofRows = nofRows;
        this.nofColumns = nofColumns;
    }

    public Grid(Grid grid) {
        this.grid = new Entity[grid.nofRows][grid.nofColumns];  // Create the new grid with the same size, without any entities inside of it
        this.nofRows = grid.nofRows;
        this.nofColumns = grid.nofColumns;
    }

    public int getNofRows() {
        return nofRows;
    }

    public int getNofColumns() {
        return nofColumns;
    }

    /**
     * Validates that a grid can be created with the received size.
     * @param rows      the number of rows to set to the new grid.
     * @param columns   the number of columns to set to the new grid.
     */
    private void validateGridSize(int rows, int columns) {
        if(rows < 10 || rows > 100) {
            throw new IllegalArgumentException("Cannot create grid with number of rows "
                    + rows + ". \nCan only create grid with number of rows that is between 10 and 100 (included).");
        }

        if(columns < 10 || columns > 100) {
            throw new IllegalArgumentException("Cannot create grid with number of columns "
                    + columns + ". \nCan only create grid with number of columns that is between 10 and 100 (included).");
        }
    }


    /**
     * Validates that a grid location is not out of bounds.
     * @param point the location of the grid to check if out of bounds.
     *              x is the number of row, y is the number of column.
     */
    private void validateGridLocation(Point point) {
        if (point.x >= nofRows || point.x < 0) {
            throw new IllegalArgumentException("Received out of bounds location of ("
                    + point.x + ", " + point.y + "). \nRows must be between " + 0 + " and " + (nofRows - 1) + " (included).");
        }

        if (point.y >= nofColumns || point.y < 0) {
            throw new IllegalArgumentException("Received out of bounds location of ("
                    + point.x + ", " + point.y + "). \nColumns must be between " + 0 + " and " + (nofColumns - 1) + " (included).");
        }
    }


    /**
     * Add an entity to the grid, at a specified location.
     * @param location  the location in which to add the entity instance.
     * @param entity    an entity instance to add at the location.
     */
    public void addEntityToGrid(Point location, Entity entity) {
        validateGridLocation(location);
        grid[location.x][location.y] = entity;
    }


    /**
     * Try to move each entity in the grid.
     * Call for this method on each simulation tick.
     */
    public void moveEntities() {
        Entity currEntity;

        for (int i = 0; i < nofRows; i++) {
            for (int j = 0; j < nofColumns; j++) {
                currEntity = grid[i][j];

                if (currEntity != null) {
                    tryToMoveEntity(currEntity);
                }
            }
        }
    }


    /**
     * Tries to move an entity in the grid in a random direction.
     * If the randomized location is occupied, tries to move in another random direction.
     * If all directions are occupied, keeps the entity at its location without moving it.
     * @param entity entity instance to move.
     */
    private void tryToMoveEntity(Entity entity) {
        List<Object> possibleDirectionsList = Arrays.asList(EnumSet.allOf(Direction.class).toArray());
        Random random = new Random();

        // Try to move in each direction in the list, if all fails keep unchanged
        while (!possibleDirectionsList.isEmpty()) {
            int rndDirectionInd = random.nextInt(possibleDirectionsList.size()); // Get a random direction index from the possible directions set
            Direction direction = (Direction) possibleDirectionsList.get(rndDirectionInd);

            // Try to move in the randomized direction received, if successful then finish working
            if(tryToMoveEntityInDirection(entity, direction)) {
                return;
            }

            possibleDirectionsList.remove(direction);   // Move was unsuccessful, remove the direction from the possible directions list
        }
    }


    /**
     * Tries to move an entity in a given direction if possible
     * (no other entity is occupying the new location).
     * @param entity      Entity to try to move.
     * @param direction   Direction to try to move to.
     * @return true if the move occurred, false otherwise.
     */
    private boolean tryToMoveEntityInDirection(Entity entity, Direction direction) {
        Point currLocation = entity.getGridLocation();
        int x, y;

        switch (direction) {
            case UP:
                x = getAxisLocation(nofRows, currLocation.x - 1);
                y =  currLocation.y;
                break;

            case DOWN:
                x = getAxisLocation(nofRows, currLocation.x + 1);
                y =  currLocation.y;
                break;

            case LEFT:
                x = currLocation.x;
                y = getAxisLocation(nofColumns, currLocation.y - 1);
                break;

            case RIGHT:
                x = currLocation.x;
                y = getAxisLocation(nofColumns, currLocation.y + 1);
                break;

            default:
                throw new RuntimeException("Unhandled direction received " + direction.name() + " in Grid.");
        }

        // Check if the location is not occupied
        if (grid[x][y] == null) {
            grid[currLocation.x][currLocation.y] = null;    // Empty the previous location
            grid[x][y] = entity;                            // Add to the new location
            entity.setGridLocation(x, y);                   // Update the entity's location
            return true;
        }

        return false;
    }


    /**
     * Returns a correct axis location of a given new location and a maximum size of the axis.
     * @param axisSize      The size of the axis in which the new location is on.
     * @param newLocation   The new location in the axis.
     */
    private int getAxisLocation(int axisSize, int newLocation) {
        if (newLocation < 0)
            return axisSize + newLocation;
        else
            return newLocation % axisSize;
    }


    /**
     * Returns an entity from a specified grid location.
     * If there is no entity in that location, returns null.
     * @param location The location of the entity in the grid.
     */
    public Entity getEntityFromGrid(Point location) {
        validateGridLocation(location);

        return grid[location.x][location.y];
    }


    /**
     * Scatters the population of a world across the grid,
     * in randomized locations.
     * @param population The population of the world to add into random locations in the grid.
     */
    public void populateGrid(List<Entity> population) {
        List<Point> possibleLocationsList = getPossibleLocationsList();
        Random random = new Random();

        for (Entity entity : population) {
            int rndLocationInd = random.nextInt(possibleLocationsList.size()); // Get a random point index from the possible locations set
            Point randomLocation = possibleLocationsList.get(rndLocationInd);

            grid[randomLocation.x][randomLocation.y] = entity;
            entity.setGridLocation(randomLocation.x, randomLocation.y);

            possibleLocationsList.remove(randomLocation);   // Remove the location from the possible locations
        }
    }


    /**
     * Get all possible locations in the grid in a List of Points.
     * Each point has X value for the row in the grid, and Y value for the column in the grid.
     */
    private List<Point> getPossibleLocationsList() {
        List<Point> locationsSet = new ArrayList<>();

        for (int i = 0; i < nofRows; i++) {
            for (int j = 0; j < nofColumns; j++) {
                locationsSet.add(new Point(i, j));
            }
        }

        return locationsSet;
    }
}
