package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class Tile extends StaticTiledMapTile {
    public enum Type { EMPTY,PATH, PATHDOT, WALL, DOT}
    public Tile.Type type;

    private int x,y;
    public boolean isDot;

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

    public Tile(){
        super(new TextureRegion());
        this.type = null;
        this.prev = null;
        this.isDot = false;
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
        this.isDot = false;
        this.x = 0;
        this.y = 0;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

    public Tile(TextureRegion textureRegion, Tile.Type type, int x, int y) {

        super(textureRegion);
        this.type = type;
        this.isDot = false;
        this.x = x;
        this.y = y;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

    public Tile(TextureRegion textureRegion, Tile.Type type, int x, int y, boolean isDot){
        super(textureRegion);
        this.type = type;
        this.prev = null;
        this.isDot = false;
        this.x = x;
        this.y = y;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
        if(type == Tile.Type.PATH && isDot == true){
            this.isDot = isDot;
        }
    }

    public Tile(Type type, int x, int y) {
        super(new TextureRegion());
        this.type = type;
        this.prev = null;
        this.isDot = false;
        this.x = x;
        this.y = y;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

    public Tile(Type type, int x, int y, boolean isDot){
        super(new TextureRegion());
        this.type = type;
        this.prev = null;
        this.isDot = false;
        this.x = x;
        this.y = y;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
        if(type == Type.PATH && isDot == true){
            this.isDot = isDot;
        }
    }

}