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

        //Iterator<String> properties = tmxMap.getProperties().getKeys(); //Zeigt alle möglichen Eigenschaften der Map
        mapWidth = Integer.parseInt(tmxMap.getProperties().get("width").toString());
        mapHeight = Integer.parseInt(tmxMap.getProperties().get("height").toString());
        tileSize = Integer.parseInt(tmxMap.getProperties().get("tilewidth").toString());

        layerWall = (TiledMapTileLayer)tmxMap.getLayers().get("Walls");
        layerPath = (TiledMapTileLayer)tmxMap.getLayers().get("Path");
        layerCollect = (TiledMapTileLayer)tmxMap.getLayers().get("Collectables");

        layerPath.setOpacity(0.5f);
        matrix = new Tile[mapWidth][mapHeight];
        this.path = path;
        generateWalls();
        //moveMapTo(0,5);
        TextureRegion t = layerWall.getCell(1,16).getTile().getTextureRegion();
        int rx = t.getRegionX();
        int ry = t.getRegionY();
        generateDots(150);
    }

    public TextureRegion createTextureRegion(Tile.Type type){
        TextureRegion region = null;
        if(path.equals("map.tmx")){
            Texture tex = new Texture("maptileset.png");
            region = new TextureRegion(tex);
            switch (type) {
                case DOT:
                    region.setRegionX(24); // Dot
                    region.setRegionWidth(8);
                    region.setRegionY(8);
                    region.setRegionHeight(8);
                    region.setU(0.27272728f);
                    region.setU2(0.36363637f);
                    region.setV(0.2f);
                    region.setV2(0.4f);
                    break;
            /*case BUFF:
                    Weitere Typen...
                break;*/
                default:
                    region.setRegionX(8); // Leer
                    region.setRegionWidth(8);
                    region.setRegionY(24);
                    region.setRegionHeight(8);
                    region.setU2(0.45454547f);
                    region.setU(0.36363637f);
                    region.setV(0.2f);
                    region.setV2(0.4f);
                    break;
            }
        }
        else if( path.equals("map2.tmx")){
            Texture tex = new Texture("tiles.png");
            region = new TextureRegion(tex);

            switch (type) {
                case DOT:
                    tex = new Texture("coin_gold.png");
                    region = new TextureRegion(tex);

                    region.setRegionX(128); // Dot
                    region.setRegionWidth(32);
                    region.setRegionY(0);
                    region.setRegionHeight(32);
                    /*region.setU(0.27272728f);
                    region.setU2(0.36363637f);
                    region.setV(0.2f);
                    region.setV2(0.4f);*/
                    break;
                case PATH:
                    /*region.setRegionX(416); // Path Grau
                    region.setRegionWidth(tileSize);
                    region.setRegionY(64);
                    region.setRegionHeight(tileSize);
                    region.setU(0.92857146f);
                    region.setU2(1.0f);
                    region.setV(0.2f);
                    region.setV2(0.3f);*/
                    region.setRegionX(0); // Dot
                    region.setRegionWidth(tileSize);
                    region.setRegionY(64);
                    region.setRegionHeight(tileSize);
                    region.setU(0.0f);
                    region.setU2(0.071428575f);
                    region.setV(0.2f);
                    region.setV2(0.3f);
                break;
                case PATHDOT:
                    region.setRegionX(96); // PATHDOT Blau
                    region.setRegionWidth(tileSize);
                    region.setRegionY(64);
                    region.setRegionHeight(tileSize);
                    region.setU(0.21428573f);
                    region.setU2(0.2857143f);
                    region.setV(0.2f);
                    region.setV2(0.3f);
                    break;
                case WALL:
                    /*region.setRegionX(160); // Wand Grau
                    region.setRegionWidth(tileSize);
                    region.setRegionY(0);
                    region.setRegionHeight(tileSize);
                    region.setU2(0.42857146f);
                    region.setU(0.35714287f);
                    region.setV(0.0f);
                    region.setV2(0.1f);*/
                    region.setRegionX(160); // Wand Blau
                    region.setRegionWidth(tileSize);
                    region.setRegionY(64);
                    region.setRegionHeight(tileSize);
                    region.setU2(0.42857146f);
                    region.setU(0.35714287f);
                    region.setV(0.2f);
                    region.setV2(0.3f);
                    break;
                default:

                    break;
            }
        }
        return region;
    }

    private void generateWalls(){
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                if(layerWall.getCell(x, y) == null){
                    matrix[x][y] = new Tile(Tile.Type.EMPTY, ((x*tileSize)), ((y*tileSize)));
                    if(layerPath.getCell(x,y) != null){

                        /*if(x==1 && y==18){
                            TextureRegion tp = layerCollect.getCell(x,y).getTile().getTextureRegion();
                            int xR = tp.getRegionX();
                            int yR = tp.getRegionY();
                            x=x;
                        }*/
                        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                        Tile tile = new Tile(createTextureRegion(Tile.Type.PATH), Tile.Type.PATH, (x*tileSize), (y*tileSize));
                        cell.setTile((TiledMapTile) tile); // Cast to original TiledMapTile da Cell meine Klasse nicht will
                        layerPath.setCell(x,y, cell);
                        matrix[x][y] = tile;
                    }
                }
                else {
                    /*TextureRegion wallRegion = layerWall.getCell(x,y).getTile().getTextureRegion();
                    int xR = wallRegion.getRegionX();
                    int yR = wallRegion.getRegionY();
                    matrix[x][y] = new Tile(wallRegion, Tile.Type.WALL, (x*tileSize), (y*tileSize));*/

                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    Tile tile = new Tile(createTextureRegion(Tile.Type.WALL), Tile.Type.WALL, (x*tileSize), (y*tileSize));
                    cell.setTile((TiledMapTile) tile); // Cast to original TiledMapTile da Cell meine Klasse nicht will
                    layerWall.setCell(x,y, cell);
                    matrix[x][y] = tile;
                    if(x==1 && y==16){
                        x=x;
                    }
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
                                cell = new TiledMapTileLayer.Cell();
                                t = new Tile(createTextureRegion(Tile.Type.PATHDOT));
                                cell.setTile(t);
                                layerPath.setCell(x,y, cell);
                                total_Dots--;
                            }
                        }
                    }

                }
            }

        }

    }
    public void tileSwitch(int diffX, int diffY, int x, int y){
        if(x-diffX >= 0 && x-diffX<=mapWidth && y-diffY >= 0 && y-diffY<=mapHeight){
            matrix[x-diffX][y-diffY] = matrix[x][y];
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile((StaticTiledMapTile)matrix[x][y]);
            switch (matrix[x][y].type){
                case WALL:
                    layerWall.setCell(x, y, null);
                    layerWall.setCell(x-diffX,y-diffY, cell);
                    break;
                case DOT:
                    layerCollect.setCell(x, y, null);
                    layerCollect.setCell(x-diffX,y-diffY, cell);
                    break;
                case PATH:
                    layerPath.setCell(x, y, null);
                    layerPath.setCell(x-diffX,y-diffY, cell);
                    break;
            }
        }
    }
    public void moveMapTo(int xPos, int yPos){
        Tile leftBottumCornerTile = null;
        Tile rightUpperCornerTile = null;
        for(int x = 0; x < mapWidth && leftBottumCornerTile == null; x++){
            for(int y = 0; y < mapHeight && leftBottumCornerTile == null; y++){
                if(matrix[x][y].type == Tile.Type.WALL) leftBottumCornerTile = matrix[x][y];
            }
        }
        for(int x = mapWidth-1; x >= 0 && rightUpperCornerTile == null; x--){
            for(int y = mapHeight-1; y >= 0 && rightUpperCornerTile == null; y--){
                if(matrix[x][y].type == Tile.Type.WALL) rightUpperCornerTile = matrix[x][y];
            }
        }
        int diffX = (leftBottumCornerTile.x/tileSize) - xPos; // positiv = links; negativ = rechts
        int diffY = (leftBottumCornerTile.y/tileSize) - yPos; // positiv = unten; negativ = oben

        if(diffX >= 0){ //links
            if(diffY >= 0){ //links unten
                for(int x = (leftBottumCornerTile.x/tileSize); x < mapWidth; x++){
                    for(int y = (leftBottumCornerTile.y/tileSize); y < mapHeight; y++){
                        tileSwitch(diffX, diffY, x, y);
                    }
                }
            }
            else if(diffY <= 0){ //links oben
                for(int x = (leftBottumCornerTile.x/tileSize); x < mapWidth; x++){
                    for(int y = (rightUpperCornerTile.y/tileSize); y >= 0; y--){
                        tileSwitch(diffX, diffY, x, y);
                    }
                }
            }
        }else if(diffX >= 0){ // rechts
            if(diffY >= 0){ //rechts unten
                for(int x = (rightUpperCornerTile.x/tileSize); x >= 0; x--){
                    for(int y = (leftBottumCornerTile.y/tileSize); y < mapHeight; y++){
                        tileSwitch(diffX, diffY, x, y);
                    }
                }
            }
            else if(diffY < 0){ //rechts oben
                for(int x = (rightUpperCornerTile.x/tileSize); x >= 0; x--){
                    for(int y = (rightUpperCornerTile.y/tileSize); y >= 0; y--){
                        tileSwitch(diffX, diffY, x, y);
                    }
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
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TiledMapTile t = new Tile(createTextureRegion(Tile.Type.PATH));
            cell.setTile(t);
            layerPath.setCell(
                    tile.x/tileSize,
                    tile.y/tileSize,
                    cell
            );
            tile.isDot = false;
        }
    }
}
