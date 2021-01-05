package com.vieth.pacman.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Tile extends TiledMapTileLayer.Cell {
    public enum Type { PATH, WALL}
    public Type type;

    public int x,y;
    public boolean isDot; //noch keine Wirkung

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
        this.type = null;
        this.prev = null;
        this.isDot = false;
        this.x = 0;
        this.y = 0;
        this.cost = 0;
        this.heuristics = 0;
        this.total = 1000000;
    }

    public Tile(Type type, int x, int y){
        this.type = type;
        this.prev = null;
        this.isDot = false;
        this.x = x;
        this.y = y;
        this.cost = 0;
        this.heuristics = 0;
        this.total = 1000000;
    }
    public Tile(Type type, int x, int y, boolean isDot){
        this.type = type;
        this.prev = null;
        this.isDot = false;
        this.x = x;
        this.y = y;
        if(type == Type.PATH && isDot == true){
            this.isDot = isDot;
        }
    }

}
