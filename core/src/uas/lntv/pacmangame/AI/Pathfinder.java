package uas.lntv.pacmangame.AI;

import uas.lntv.pacmangame.Maps.Map;
import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;

/**
 * This class is generated to find the shortest Path from a starting point to a certain destination.
 * In the PacMan game it is used by the ghosts to either find the shortest path to their prey
 * PacMan, or to find the fastest way home or away from PacMan if he is in hunting mode,
 * using the A*-Algorithm.
 */
public class Pathfinder {
    private final Tile[][] MATRIX;
    private Tile[] open;
    private final Enemy HUNTER;
    private final int TARGET_X;
    private final int TARGET_Y;
    private final int MAP_WIDTH;
    private final int MAP_HEIGHT;
    private final int TILE_SIZE;
    private final boolean FIND_HOME;
    private boolean noWay = false;

    /**
     * This constructor is used to find the shortest path to an actor, which will be PacMan in this
     * game.
     * It generates a matrix of the whole map and also calculates the heuristics to the target.
     * It also prepares to search by creating a 'open' list with all tiles of the map, setting the
     * cost of the starting tile to 0 and creating a yet empty 'closed' list.
     * @param hunter actor that wants to find a shortest path
     * @param prey target of the actor
     */
    public Pathfinder(Enemy hunter, Actor prey){
        this.MAP_WIDTH = Map.getMapWidth();
        this.MAP_HEIGHT = Map.getMapHeight();
        this.TILE_SIZE = Map.getTileSize();
        this.MATRIX = Map.getMatrix();
        this.open = new Tile[(MAP_WIDTH * MAP_HEIGHT)];
        this.HUNTER = hunter;
        this.TARGET_X = prey.getXPosition();
        this.TARGET_Y = prey.getYPosition();
        this.FIND_HOME = false;
        int i = 0;
        for(int x = 0; x < MAP_WIDTH; x++){
            for(int y = 0; y < MAP_HEIGHT; y++){
                open[i] = this.MATRIX[x][y];
                open[i++]
                        .setCost(1000000)
                        .setTotal(1000000)
                        .setPrev(null)
                        .setHeuristics(calcHeuristics(
                                x,
                                y,
                                this.TARGET_X /this.TILE_SIZE,
                                this.TARGET_Y /this.TILE_SIZE
                                )
                        );
            }
        }
        open[searchHunter()]
                .setCost(0)
                .setTotal(open[searchHunter()].getHeuristics());
    }

    /**
     * Does the same as the first constructor, but uses x-/y-coordinates instead of an actor.
     * @see Pathfinder
     */
    public Pathfinder(Enemy hunter, int targetX, int targetY, boolean findHome){
        this.MAP_WIDTH = Map.getMapWidth();
        this.MAP_HEIGHT = Map.getMapHeight();
        this.TILE_SIZE = Map.getTileSize();
        this.MATRIX = Map.getMatrix();
        this.open = new Tile[(MAP_WIDTH * MAP_HEIGHT)];
        this.HUNTER = hunter;
        this.TARGET_X = targetX;
        this.TARGET_Y = targetY;
        this.FIND_HOME = findHome;
        int i = 0;
        for(int x = 0; x < MAP_WIDTH; x++){
            for(int y = 0; y < MAP_HEIGHT; y++){
                open[i] = this.MATRIX[x][y];
                open[i++]
                        .setCost(1000000)
                        .setTotal(1000000)
                        .setPrev(null)
                        .setHeuristics(calcHeuristics(
                                x,
                                y,
                                this.TARGET_X /this.TILE_SIZE,
                                this.TARGET_Y /this.TILE_SIZE
                                )
                        );
            }
        }
        open[searchHunter()]
                .setCost(0)
                .setTotal(open[searchHunter()].getHeuristics());
    }

    private int searchHunter(){
        int i = 0;
        while(open[i] != Map.getTile(HUNTER.getXPosition(), HUNTER.getYPosition())){
            i++;
        }
        return i;
    }

    private double calcHeuristics(int xHunt, int yHunt, int xPrey, int yPrey){
        return Math.sqrt( Math.pow(xHunt - xPrey, 2) + Math.pow(yHunt - yPrey, 2));
    }

    /**
     * Runs through the whole 'open'-list and searches for the tile in the map with the lowest
     * total-value, which is the sum of the costs and the heuristics.
     * This tile is needed for the next step of the A*-algorithm and will be removed from the
     * 'open'-list.
     * @return Tile with lowest total-value
     */
    private Tile extractMinimum(){
        Tile temp = new Tile();
        int len = open.length;
        if(len == 0) return null;                      //If the 'open'-list is empty, there is no possible path to the target.
        for(int i = 0; i < len - 1; i++) {
            if (temp.getTotal() > open[i].getTotal()) {
                temp = open[i];
            }
        }
        open = removeElement(open, temp);
        return temp;
    }

    /**
     * This method is used to remove a special element of a given list.
     * @param list the list to be edited
     * @param element the tile that is supposed to be removed
     * @return The new list without the tile.
     */
    private Tile[] removeElement(Tile[] list, Tile element){
        Tile[] copy = new Tile[list.length - 1];
        int len = list.length;
        int i = 0;
        int c = 0;
        while(i < len - 1){
            if(list[i] != element){
                copy[c++] = list[i];
            }
            i++;
        }
        return copy;
    }

    /**
     * In this method the magic happens.
     * It creates connections between tiles and tells every edited tile, which is the best
     * tile on the way back to the starting point.
     * Once it reaches the target, the shortest path is found.
     * @return 'false' if it only did a calculating step or 'true' if it found either the shortest path or no path
     */
    private boolean aStarAlg(){
        Tile min = extractMinimum();
        if(min == null) {
            noWay = true;
            return true;
        }
        if(min == Map.getTile(TARGET_X, TARGET_Y)) return true;
        int x = min.getX() / TILE_SIZE;
        int y = min.getY() / TILE_SIZE;
        if(y < (MAP_HEIGHT - 1)) {
            Tile up = MATRIX[x][y + 1];
            if(up.getType() != Tile.Type.WALL
                    && up.getCost() > min.getCost() + 1
                    && ((((HUNTER.getDifficulty() == Enemy.Difficulty.HARD || HUNTER.getDifficulty() == Enemy.Difficulty.MEDIUM)
                        && !(up.isOccupiedByGhost())
                        )
                        || ((HUNTER.getDifficulty() == Enemy.Difficulty.RUNAWAY) && !(up.isOccupiedByPacMan()))
                    ) || FIND_HOME
                    )
            ){
                up.setCost(min.getCost() + 1);
                up.setTotal(up.getCost() + up.getHeuristics());
                up.setPrev(min);
            }
        }
        if(y > 0) {
            Tile down = MATRIX[x][y - 1];
            if(down.getType() != Tile.Type.WALL
                    && down.getCost() > min.getCost() + 1
                    && ((((HUNTER.getDifficulty() == Enemy.Difficulty.HARD || HUNTER.getDifficulty() == Enemy.Difficulty.MEDIUM)
                        && !(down.isOccupiedByGhost())
                        )
                        || ((HUNTER.getDifficulty() == Enemy.Difficulty.RUNAWAY) && !(down.isOccupiedByPacMan()))
                    ) || FIND_HOME
                    )
            ){
                down.setCost(min.getCost() + 1);
                down.setTotal(down.getCost() + down.getHeuristics());
                down.setPrev(min);
            }
        }
        if(x > 0) {
            Tile left = MATRIX[x - 1][y];
            if(left.getType() != Tile.Type.WALL
                    && left.getCost() > min.getCost() + 1
                    && ((((HUNTER.getDifficulty() == Enemy.Difficulty.HARD || HUNTER.getDifficulty() == Enemy.Difficulty.MEDIUM)
                        && !(left.isOccupiedByGhost())
                        )
                        || ((HUNTER.getDifficulty() == Enemy.Difficulty.RUNAWAY) && !(left.isOccupiedByPacMan()))
                    ) || FIND_HOME
                    )
            ){
                left.setCost(min.getCost() + 1);
                left.setTotal(left.getCost() + left.getHeuristics());
                left.setPrev(min);
            }
        }
        if(x < (MAP_WIDTH - 1)) {
            Tile right = MATRIX[x + 1][y];
            if(right.getType() != Tile.Type.WALL
                    && right.getCost() > min.getCost() + 1
                    && ((((HUNTER.getDifficulty() == Enemy.Difficulty.HARD || HUNTER.getDifficulty() == Enemy.Difficulty.MEDIUM)
                        && !(right.isOccupiedByGhost())
                        )
                        || ((HUNTER.getDifficulty() == Enemy.Difficulty.RUNAWAY) && !(right.isOccupiedByPacMan()))
                    ) || FIND_HOME
                    )
            ){
                right.setCost(min.getCost() + 1);
                right.setTotal(right.getCost() + right.getHeuristics());
                right.setPrev(min);
            }
        }
        return false;
    }

    /**
     * Calculates the shortest path to the target.
     * @return next tile from the actor on the way to its target.
     */
    public Tile aStarResult(){
        Tile temp = Map.getTile(TARGET_X, TARGET_Y);
        while(true){ if(aStarAlg()) break; }
        if(noWay) return null;
        if(temp == Map.getTile(HUNTER.getXPosition(), HUNTER.getYPosition())) return temp;
        while(temp.getPrev() != Map.getTile(HUNTER.getXPosition(), HUNTER.getYPosition())){
            temp = temp.getPrev();
        }
        return temp;
    }

}
