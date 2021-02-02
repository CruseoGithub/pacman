package uas.lntv.pacmangame.AI;

import uas.lntv.pacmangame.Maps.Tile;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;

public class Pathfinder {
    private Tile[][] matrix;
    private Tile[] open;
    private Tile[] closed;
    private Enemy hunter;
    private int targetX;
    private int targetY;
    private MapScreen screen;
    private int closedElements;
    private int tileSize;
    private boolean findHome;

    //A* Constructor
    public Pathfinder(MapScreen screen, Enemy hunter, Actor prey, int tileSize){
        this.screen = screen;
        this.tileSize = tileSize;
        this.matrix = screen.map.matrix;
        this.open = new Tile[(screen.map.mapWidth * screen.map.mapHeight )];
        this.hunter = hunter;
        this.targetX = prey.getXPosition();
        this.targetY = prey.getYPosition();
        int i = 0;
        for(int x = 0; x < screen.map.mapWidth; x++){
            for(int y = 0; y < screen.map.mapHeight; y++){
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
        this.closed = new Tile[(screen.map.mapWidth * screen.map.mapHeight)];
        this.closedElements = 0;
    }

    public Pathfinder(MapScreen screen, Enemy hunter, int targetX, int targetY, int tileSize, boolean findHome){
        this.screen = screen;
        this.tileSize = tileSize;
        this.matrix = screen.map.matrix;
        this.open = new Tile[(screen.map.mapWidth * screen.map.mapHeight )];
        this.hunter = hunter;
        this.targetX = targetX;
        this.targetY = targetY;
        this.findHome = findHome;
        int i = 0;
        for(int x = 0; x < screen.map.mapWidth; x++){
            for(int y = 0; y < screen.map.mapHeight; y++){
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
        this.closed = new Tile[(screen.map.mapWidth * screen.map.mapHeight)];
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

    private Tile extractMinimum(){
        Tile temp = new Tile();
        int len = open.length;
        for(int i = 0; i < len - 1; i++) {
            if (temp.getTotal() > open[i].getTotal()) {
                temp = open[i];
            }
        }
        open = removeElement(open, temp);
        return temp;
    }

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

    private boolean aStarAlg(){
        Tile min = extractMinimum();
        closed[closedElements++] = min;
        if(min == screen.map.getTile(targetX, targetY)) return true;
        int x = min.getX() / tileSize;
        int y = min.getY() / tileSize;
        if(y < (screen.map.mapHeight) - 1) {
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
        if(x < (screen.map.mapWidth - 1)) {
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

    public Tile aStarResult(){
        Tile temp = screen.map.getTile(targetX, targetY);
        while(!aStarAlg());
        if(temp == screen.map.getTile(hunter.getXPosition(), hunter.getYPosition())) return temp;
        while(temp.getPrev() != screen.map.getTile(hunter.getXPosition(), hunter.getYPosition())){
            temp = temp.getPrev();
        }
        return temp;
    }

}
