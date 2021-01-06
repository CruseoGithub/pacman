package com.vieth.pacman.Scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.vieth.pacman.PacMan;
import com.vieth.pacman.Screens.GameScreen;
import com.vieth.pacman.Sprites.Player;
import com.vieth.pacman.Sprites.Tile;

public class Map {
    GameScreen screen;
    private TmxMapLoader maploader;
    private TiledMap tmxMap;
    public OrthogonalTiledMapRenderer renderer;

    public TiledMapTileLayer layerWall;
    public TiledMapTileLayer layerPath;
    public TiledMapTileLayer layerCollect;

    public int mapWidth;
    public int mapHeight;
    public int tileSize;
    public String path;

    public Tile matrix[][];


    public Map(String path, GameScreen screen){
        this.screen = screen;
        maploader = new TmxMapLoader();
        tmxMap = maploader.load(path);
        renderer = new OrthogonalTiledMapRenderer(tmxMap);

        mapWidth = Integer.parseInt(tmxMap.getProperties().get("width").toString());
        mapHeight = Integer.parseInt(tmxMap.getProperties().get("height").toString());
        tileSize = Integer.parseInt(tmxMap.getProperties().get("tilewidth").toString());
        PacMan.V_WIDTH = mapWidth * tileSize;
        PacMan.V_HEIGHT = mapHeight * tileSize;

        layerWall = (TiledMapTileLayer)tmxMap.getLayers().get("Walls");
        layerPath = (TiledMapTileLayer)tmxMap.getLayers().get("Path");
        layerCollect = (TiledMapTileLayer)tmxMap.getLayers().get("Collectables");

        //layerPath.setOpacity(0.5f);
        matrix = new Tile[mapWidth][mapHeight];
        this.path = path;
        generateScreenMap();
        generateDots(150);
    }



    private void generateScreenMap(){
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                if(layerWall.getCell(x, y) == null){
                    matrix[x][y] = new Tile(Tile.Type.EMPTY, ((x*tileSize)), ((y*tileSize)));
                    if(layerPath.getCell(x,y) != null){
                        Tile tile = new Tile(layerPath.getCell(x,y).getTile().getTextureRegion(), Tile.Type.PATH, (x*tileSize), (y*tileSize));
                        matrix[x][y] = tile;
                    }
                }
                else {
                    TextureRegion wallRegion = layerWall.getCell(x,y).getTile().getTextureRegion();
                    matrix[x][y] = new Tile(wallRegion, Tile.Type.WALL, (x*tileSize), (y*tileSize));
                }

            }
        }
    }

    private void generateDots(int total_Dots){
        while(total_Dots > 0){
            for(int x = 0; x < mapWidth; x++){
                for(int y = 0; y < mapHeight; y++){
                    if(layerWall.getCell(x, y) == null && total_Dots>0){
                        if(layerPath.getCell(x,y) != null & !matrix[x][y].isDot && x>0 && x<(mapWidth-2)){ //X-Abfrage: Dots sollen nicht im Teleportgang spawnen
                            int max = 1;
                            int min = 0;
                            int random = (int) (Math.random() * (max - min + 1) + min); // random ist entweder 0 oder 1
                            if(random > 0){
                                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                                TiledMapTile t = new Tile(createTextureRegion(Tile.Type.DOT));
                                cell.setTile(t);
                                layerCollect.setCell(x,y, cell);
                                matrix[x][y].isDot = true;
                                total_Dots--;
                            }
                        }
                    }
                }
            }
        }
    }

    public TextureRegion createTextureRegion(Tile.Type type){
        TextureRegion region = null;
        if(path.equals("map.tmx")){
            switch (type) {
                case DOT:
                    region = layerPath.getCell(1,17).getTile().getTextureRegion();
                    break;
                default:
                    break;
            }
        }
        else if( !path.equals("map.tmx")){
            Texture tex = new Texture("tiles.png");
            region = new TextureRegion(tex);
            switch (type) {
                case DOT:
                    tex = new Texture("coin_gold.png");
                    region = new TextureRegion(tex);
                    region.setRegionX(128);
                    region.setRegionWidth(32);
                    region.setRegionY(0);
                    region.setRegionHeight(32);
                    break;
                default:
                    break;
            }
        }
        return region;
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
                    tile.getX()/tileSize,
                    tile.getY()/tileSize,
                    null
            );
            tile.isDot = false;
        }
    }
}
