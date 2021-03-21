package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

public class Tile extends StaticTiledMapTile {
    public enum Type { EMPTY,PATH, WALL, DOT}
    public enum Item { EMPTY, DOT, HUNTER, SLOWMO, TIME}
    public Tile.Type type;
    protected Tile.Item item;

    private final int x,y;
    private boolean occupiedByPacMan = false;
    private boolean occupiedByGhost = false;

    private int cost;
    private double heuristics;
    private double total;

    private Tile prev;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Type getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public Tile setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public double getHeuristics() {
        return heuristics;
    }

    public Tile setHeuristics(double heuristics) {
        this.heuristics = heuristics;
        return this;
    }

    public double getTotal() {
        return total;
    }

    public Tile setTotal(double total) {
        this.total = total;
        return this;
    }

    public Tile getPrev() {
        return prev;
    }

    public Tile setPrev(Tile prev) {
        this.prev = prev;
        return this;
    }

    public boolean isOccupiedByGhost(){ return occupiedByGhost; }

    public boolean isOccupiedByPacMan(){ return occupiedByPacMan; }

    public void enter(Actor actor){
        if(actor instanceof PacMan) occupiedByPacMan = true;
        if(actor instanceof Enemy) occupiedByGhost = true;
    }

    public void leave(Actor actor){
        if(actor instanceof PacMan) occupiedByPacMan = false;
        if(actor instanceof Enemy) occupiedByGhost = false;
    }

    public boolean isItem(){
        return item != Item.EMPTY;
    }
    public Item getItem(){ return this.item;}

    public void placeItem(Tile.Item item){ this.item = item; }

    public void takeItem(){ this.item = Item.EMPTY; }

    public Tile(){
        super(new TextureRegion());
        this.type = null;
        this.prev = null;
        this.item = Item.EMPTY;
        this.x = 0;
        this.y = 0;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

    public Tile(TextureRegion textureRegion) {
        super(textureRegion);
        this.type = null;
        this.prev = null;
        this.item = Item.EMPTY;
        this.x = 0;
        this.y = 0;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

    public Tile(TextureRegion textureRegion, Tile.Type type, int x, int y) {
        super(textureRegion);
        this.type = type;
        this.item = Item.EMPTY;
        this.x = x;
        this.y = y;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

    public Tile(Type type, int x, int y) {
        super(new TextureRegion());
        this.type = type;
        this.prev = null;
        this.item = Item.EMPTY;
        this.x = x;
        this.y = y;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

}