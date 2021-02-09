package uas.lntv.pacmangame.AI;

import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;

/**
 * This class is generated to find the shortest Path from a starting point to a certain destination.
 * In the PacMan game it is used by the ghosts to either find the shortest path to their prey
 * PacMan, or to find the fastest way home or away from PacMan if he is in hunting mode,
 * using the A*-Algorithm.
 */
public class Pathfinder {
    private Tile[][] matrix;
    private Tile[] open;
    private Tile[] closed;
    private Enemy hunter;
    private int targetX;
    private int targetY;
    private MapScreen screen;
    private int closedElements;
    private final int mapWidth;
    private final int mapHeight;
    private final int tileSize;
    private boolean findHome;
    private boolean noWay = false;

    /**
     * This constructor is used to find the shortest path to an actor, which will be PacMan in this
     * game.
     * It generates a matrix of the whole map and also calculates the heuristics to the target.
     * It also prepares to search by creating a 'open' list with all tiles of the map, setting the
     * cost of the starting tile to 0 and creating a yet empty 'closed' list.
     * @param screen the screen in which the actor acts
     * @param hunter actor that wants to find a shortest path
     * @param prey target of the actor
     */
    public Pathfinder(MapScreen screen, Enemy hunter, Actor prey){
        this.screen = screen;
        this.mapWidth = screen.map.getMapWidth();
        this.mapHeight = screen.map.getMapHeight();
        this.tileSize = screen.map.getTileSize();
        this.matrix = screen.map.matrix;
        this.open = new Tile[(mapWidth * mapHeight )];
        this.hunter = hunter;
        this.targetX = prey.getXPosition();
        this.targetY = prey.getYPosition();
        int i = 0;
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                open[i] = this.matrix[x][y];
                open[i++]
                        .setCost(1000000)
                        .setTotal(1000000)
                        .setPrev(null)
                        .setHeuristics(calcHeuristics(
                                x,
                                y,
                                (int) this.targetX/this.tileSize,
                                (int) this.targetY/this.tileSize
                                )
                        );
            }
        }
        open[searchHunter()]
                .setCost(0)
                .setTotal(open[searchHunter()].getHeuristics());
        this.closed = new Tile[(mapWidth * mapHeight)];
        this.closedElements = 0;
    }

    /**
     *Does the same as the first constructor, but uses x-/y-coordinates instead of an actor.
     * @see Pathfinder
     */
    public Pathfinder(MapScreen screen, Enemy hunter, int targetX, int targetY, boolean findHome){
        this.screen = screen;
        this.mapWidth = screen.map.getMapWidth();
        this.mapHeight = screen.map.getMapHeight();
        this.tileSize = screen.map.getTileSize();
        this.matrix = screen.map.matrix;
        this.open = new Tile[(mapWidth * mapHeight)];
        this.hunter = hunter;
        this.targetX = targetX;
        this.targetY = targetY;
        this.findHome = findHome;
        int i = 0;
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                open[i] = this.matrix[x][y];
                open[i++]
                        .setCost(1000000)
                        .setTotal(1000000)
                        .setPrev(null)
                        .setHeuristics(calcHeuristics(
                                x,
                                y,
                                (int) this.targetX/this.tileSize,
                                (int) this.targetY/this.tileSize
                                )
                        );
            }
        }
        open[searchHunter()]
                .setCost(0)
                .setTotal(open[searchHunter()].getHeuristics());
        this.closed = new Tile[(mapWidth * mapHeight)];
        this.closedElements = 0;
    }

    private int searchHunter(){
        int i = 0;
        while(open[i] != screen.map.getTile(hunter.getXPosition(), hunter.getYPosition())){
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
        closed[closedElements++] = min;
        if(min == screen.map.getTile(targetX, targetY)) return true;
        int x = min.getX() / tileSize;
        int y = min.getY() / tileSize;
        if(y < (mapHeight - 1)) {
            Tile up = matrix[x][y + 1];
            if(up.getType() != Tile.Type.WALL
                    && up.getCost() > min.getCost() + 1
                    && ((((hunter.getDifficulty() == Enemy.Difficulty.HARD || hunter.getDifficulty() == Enemy.Difficulty.MEDIUM)
                        && !(up.isOccupiedByGhost())
                        )
                        || ((hunter.getDifficulty() == Enemy.Difficulty.RUNAWAY) && !(up.isOccupiedByPacMan()))
                    ) || findHome
                    )
            ){
                up.setCost(min.getCost() + 1);
                up.setTotal(up.getCost() + up.getHeuristics());
                up.setPrev(min);
            }
        }
        if(y > 0) {
            Tile down = matrix[x][y - 1];
            if(down.getType() != Tile.Type.WALL
                    && down.getCost() > min.getCost() + 1
                    && ((((hunter.getDifficulty() == Enemy.Difficulty.HARD || hunter.getDifficulty() == Enemy.Difficulty.MEDIUM)
                        && !(down.isOccupiedByGhost())
                        )
                        || ((hunter.getDifficulty() == Enemy.Difficulty.RUNAWAY) && !(down.isOccupiedByPacMan()))
                    ) || findHome
                    )
            ){
                down.setCost(min.getCost() + 1);
                down.setTotal(down.getCost() + down.getHeuristics());
                down.setPrev(min);
            }
        }
        if(x > 0) {
            Tile left = matrix[x - 1][y];
            if(left.getType() != Tile.Type.WALL
                    && left.getCost() > min.getCost() + 1
                    && ((((hunter.getDifficulty() == Enemy.Difficulty.HARD || hunter.getDifficulty() == Enemy.Difficulty.MEDIUM)
                        && !(left.isOccupiedByGhost())
                        )
                        || ((hunter.getDifficulty() == Enemy.Difficulty.RUNAWAY) && !(left.isOccupiedByPacMan()))
                    ) || findHome
                    )
            ){
                left.setCost(min.getCost() + 1);
                left.setTotal(left.getCost() + left.getHeuristics());
                left.setPrev(min);
            }
        }
        if(x < (mapWidth - 1)) {
            Tile right = matrix[x + 1][y];
            if(right.getType() != Tile.Type.WALL
                    && right.getCost() > min.getCost() + 1
                    && ((((hunter.getDifficulty() == Enemy.Difficulty.HARD || hunter.getDifficulty() == Enemy.Difficulty.MEDIUM)
                        && !(right.isOccupiedByGhost())
                        )
                        || ((hunter.getDifficulty() == Enemy.Difficulty.RUNAWAY) && !(right.isOccupiedByPacMan()))
                    ) || findHome
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
        Tile temp = screen.map.getTile(targetX, targetY);
        while(!aStarAlg());
        if(noWay) return null;
        if(temp == screen.map.getTile(hunter.getXPosition(), hunter.getYPosition())) return temp;
        while(temp.getPrev() != screen.map.getTile(hunter.getXPosition(), hunter.getYPosition())){
            temp = temp.getPrev();
        }
        return temp;
    }

}
