package com.vieth.pacman.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Tile extends TiledMapTileLayer.Cell {
    public enum Type { PATH, WALL}
    public Type type;

    public int x,y;
    public boolean isDot; //noch keine Wirkung

    public Tile(Type type, int x, int y){
        this.type = type;
        this.isDot = false;
        this.x = x;
        this.y = y;
    }
    public Tile(Type type, int x, int y, boolean isDot){
        this.type = type;
        this.isDot = false;
        this.x = x;
        this.y = y;
        if(type == Type.PATH && isDot == true){
            this.isDot = isDot;
        }
    }

}
