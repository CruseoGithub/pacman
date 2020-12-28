package com.vieth.pacman.Scenes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.vieth.pacman.PacMan;
import com.vieth.pacman.Screens.GameScreen;
import com.vieth.pacman.Sprites.Player;
import com.vieth.pacman.Sprites.Tile;

import java.util.Iterator;

public class Map {
    GameScreen screen;
    private TmxMapLoader maploader;
    private TiledMap tmxMap;
    public OrthogonalTiledMapRenderer renderer;

    public TiledMapTileLayer layerWall;
    public TiledMapTileLayer layerCollect;

    public int mapWidth;
    public int mapHeight;
    public int tileSize;

    public Tile matrix[][];

    public Map(String path, GameScreen screen){
        this.screen = screen;
        maploader = new TmxMapLoader();
        tmxMap = maploader.load(path);
        renderer = new OrthogonalTiledMapRenderer(tmxMap);

        //Iterator<String> properties = tmxMap.getProperties().getKeys(); //Zeigt alle möglichen Eigenschaften der Map
        mapWidth = Integer.parseInt(tmxMap.getProperties().get("width").toString());
        mapHeight = Integer.parseInt(tmxMap.getProperties().get("height").toString());
        tileSize = Integer.parseInt(tmxMap.getProperties().get("tilewidth").toString());


        layerWall = (TiledMapTileLayer)tmxMap.getLayers().get("Walls");
        layerCollect = (TiledMapTileLayer)tmxMap.getLayers().get("Collectables");

        matrix = new Tile[mapWidth][mapHeight];
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                if(layerWall.getCell(x, y) == null){
                    matrix[x][y] = new Tile(Tile.Type.PATH, ((x*tileSize)), ((y*tileSize)));
                    if(layerCollect.getCell(x,y) != null){
                        TextureRegion region = layerCollect.getCell(x,y).getTile().getTextureRegion();
                        int regionX = region.getRegionX();
                        int regionY = region.getRegionY();

                        if(regionX == 24 && regionY == 8) matrix[x][y].isDot = true;        // Dot at x=24, y= 8
                        else if(regionX == 24 && regionY == 16) matrix[x][y].isDot = true;  // Big Dot at x=24, y=16
                        //else if();                                                        // Other Buffs
                    }
                }
                else {
                    matrix[x][y] = new Tile(Tile.Type.WALL, (x*tileSize), (y*tileSize));
                }

            }
        }
    }
    public Tile getTile(int xPosition, int yPosition){
        Tile tile = matrix[(int) xPosition/tileSize][(int)yPosition/tileSize];
        return tile;
    }

    public Tile getTile(int xPosition, int yPosition, Player.Direction dir){
        int nextCellX = (int) ((xPosition/tileSize));
        int nextCellY = (int) ((yPosition/tileSize));
        Tile tile;
        switch (dir) {
            case RIGHT:
                nextCellX = (int) ((xPosition+tileSize) / tileSize);
                break;
            case LEFT:
                nextCellX = (int) ((xPosition-tileSize) / tileSize);
                break;
            case UP:
                nextCellY = (int) ((yPosition+tileSize) / tileSize);
                break;
            case DOWN:
                nextCellY = (int) ((yPosition-tileSize) / tileSize);
                break;

        }
        return matrix[nextCellX][nextCellY];
    }
    public void setTile(Tile tile, Tile.Type type){
        tile.type = type;
    }
    public void collect(Tile tile){
        if(tile.isDot){
            screen.hud.score++;
            screen.hud.update();
            layerCollect.setCell(
                    tile.x/tileSize,
                    tile.y/tileSize,
                    null
            );
            tile.isDot = false;
        }
    }
}
