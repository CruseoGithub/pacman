package com.vieth.pacman;

import com.vieth.pacman.Sprites.Enemy;
import com.vieth.pacman.Sprites.Player;
import com.vieth.pacman.Sprites.Tile;

public class Pathfinder {
    private Tile[][] matrix;
    private Tile[] open;
    private Tile[] closed;
    private Enemy hunter;
    private Player prey;
    private int closedElements;


    //A* Constructor
    public Pathfinder(Tile matrix[][], Enemy hunter, Player prey){
        this.matrix = matrix;
        this.open = new Tile[(PacMan.V_WIDTH / 8) * (PacMan.V_HEIGHT / 8)];
        this.hunter = hunter;
        this.prey = prey;
        int i = 0;
        for(int x = 0; x < PacMan.V_WIDTH / 8; x++){
            for(int y = 0; y < PacMan.V_HEIGHT / 8; y++){
                open[i] = matrix[x][y];
                open[i++]
                        .setCost(1000000)
                        .setTotal(1000000)
                        .setPrev(null)
                        .setHeuristics(calcHeuristics(x, y, (int) this.prey.getXPosition() / 8, (int) (this.prey.getYPosition() - 128) / 8));
            }
        }
        open[searchHunter()]
                .setCost(0)
                .setTotal(open[(((hunter.getYPosition() - 128) / 8) * PacMan.V_WIDTH / 8) + hunter.getXPosition()].getHeuristics());
        this.closed = new Tile[(PacMan.V_WIDTH / 8) * (PacMan.V_HEIGHT / 8)];
        this.closedElements = 0;
    }

    private int searchHunter(){
        int i = 0;
        while(open[i] != hunter.getCell()){
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

    public Tile aStarResult(){
        Tile temp = prey.getCell();
        while(aStarAlg() == 0);
        while(temp.getPrev() != hunter.getCell()){
            temp = temp.getPrev();
        }
        return temp;
    }

    private int aStarAlg(){
        Tile min = extractMinimum();
        closed[closedElements++] = min;
        if(min == prey.getCell()) return 1;
        int x = min.getX() / 8;
        int y = min.getY() / 8;
        if(y < (PacMan.V_HEIGHT / 8) - 1) {
            Tile up = matrix[x][y + 1];
            if(up.getType() != Tile.Type.WALL && up.getCost() > min.getCost() + 1){
                up.setCost(min.getCost() + 1);
                up.setTotal(up.getCost() + up.getHeuristics());
                up.setPrev(min);
            }
        }
        if(y > 16) {
            Tile down = matrix[x][y - 1];
            if(down.getType() != Tile.Type.WALL && down.getCost() > min.getCost() + 1) {
                down.setCost(min.getCost() + 1);
                down.setTotal(down.getCost() + down.getHeuristics());
                down.setPrev(min);
            }
        }
        if(x > 0) {
            Tile left = matrix[x - 1][y];
            if(left.getType() != Tile.Type.WALL && left.getCost() > min.getCost() + 1) {
                left.setCost(min.getCost() + 1);
                left.setTotal(left.getCost() + left.getHeuristics());
                left.setPrev(min);
            }
        }
        if(x < (PacMan.V_WIDTH / 8) - 1) {
            Tile right = matrix[x + 1][y];
            if(right.getType() != Tile.Type.WALL && right.getCost() > min.getCost() + 1) {
                right.setCost(min.getCost() + 1);
                right.setTotal(right.getCost() + right.getHeuristics());
                right.setPrev(min);
            }
        }
        return 0;
    }

    //Debugging Testmethode
    public Enemy.Direction bestDirection(){
        if( (Math.abs(hunter.getXPosition() - prey.getXPosition())) < 8 && (Math.abs(hunter.getYPosition() - prey.getYPosition())) < 8 ){
            prey.die(hunter);
            return Enemy.Direction.RIGHT;
        }
        double up = calcHeuristics(hunter.getXPosition() / 8, ((hunter.getYPosition() - 128) / 8) + 8, prey.getXPosition() / 8, (prey.getYPosition() -128) / 8);
        double down = calcHeuristics(hunter.getXPosition() / 8, ((hunter.getYPosition() - 128) /8) - 8, prey.getXPosition() / 8, (prey.getYPosition() - 128) / 8);
        double left = calcHeuristics(hunter.getXPosition() / 8 - 8, (hunter.getYPosition() - 128) /8, prey.getXPosition() / 8, (prey.getYPosition() - 128) / 8);
        double right = calcHeuristics(hunter.getXPosition() / 8 + 8, (hunter.getYPosition() -128) / 8, prey.getXPosition() / 8, (prey.getYPosition() - 128) / 8);
        if(up <= down && up <= left && up <= right && hunter.getNextCell(Enemy.Direction.UP).type != Tile.Type.WALL) return Enemy.Direction.UP;
        if(down <= up && down <= left && down <= right && hunter.getNextCell(Enemy.Direction.DOWN).type != Tile.Type.WALL) return Enemy.Direction.DOWN;
        if(left <= up && left <= down && left <= right && hunter.getNextCell(Enemy.Direction.LEFT).type != Tile.Type.WALL) return Enemy.Direction.LEFT;
        else return Enemy.Direction.RIGHT;
    }
}
