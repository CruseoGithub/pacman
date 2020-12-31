package com.vieth.pacman.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class Tile extends StaticTiledMapTile {
    public enum Type { EMPTY,PATH, PATHDOT, WALL, DOT}
    public Tile.Type type;

    public int x,y;
    public boolean isDot;

    /**
     * Creates a static tile with the given region
     *
     * @param textureRegion the {@link TextureRegion} to use.
     */
    public Tile(TextureRegion textureRegion) {
        super(textureRegion);
    }

    public Tile(TextureRegion textureRegion, Tile.Type type, int x, int y) {

        super(textureRegion);
        this.type = type;
        this.isDot = false;
        this.x = x;
        this.y = y;
    }
    public Tile(TextureRegion textureRegion, Tile.Type type, int x, int y, boolean isDot){
        super(textureRegion);
        this.type = type;
        this.isDot = false;
        this.x = x;
        this.y = y;
        if(type == Tile.Type.PATH && isDot == true){
            this.isDot = isDot;
        }
    }
    public Tile(Type type, int x, int y) {
        super(new TextureRegion());
        this.type = type;
        this.isDot = false;
        this.x = x;
        this.y = y;
    }
    public Tile(Type type, int x, int y, boolean isDot){
        super(new TextureRegion());
        this.type = type;
        this.isDot = false;
        this.x = x;
        this.y = y;
        if(type == Type.PATH && isDot == true){
            this.isDot = isDot;
        }
    }

}
