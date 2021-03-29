package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Sprites.Actor;

/**
 * This class is generates a Tiled Map which can be loaded onto a Screen.
 * It provides a matrix which holds information about map dimensions and layout (Path/Wall/Collectable)
 * This class is abstract and should be implemented as a GameMap or MenuMap
 */
public abstract class Map {

    /* Fields */

    private final boolean firstMap;

    protected final Assets ASSETS;
    protected final OrthogonalTiledMapRenderer RENDERER;
    protected final TiledMap TMX_MAP;
    protected static int mapWidth;
    protected static int mapHeight;
    protected static int tileSize;
    protected Tile[][] matrix;
    protected TiledMapTileLayer layerCollect;
    protected TiledMapTileLayer layerControlButton;
    protected TiledMapTileLayer layerControlZone;
    protected TiledMapTileLayer layerPath;
    protected TiledMapTileLayer layerWall;



    /* Constructor */

    /**
     * The constructor loads the graphic layers of the tmx-map-file and sets them up into a map-renderer
     * Additionally it loads all graphic layers for the on screen controller (initially they are invisible).
     * It generates a matrix of the Map which contains a Tile for each cell.
     * It provides methods for generating and collecting Collectables which should be implemented as needed by the child class.
     * @param path String value which contains the path to a tmx-map-file.
     * @param assets provide the assets manager instance of the game.
     */
    public Map(String path, Assets assets){
        this.ASSETS = assets;
        firstMap = path.equals("maps/map_1.tmx");
        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        TMX_MAP = tmxMapLoader.load(path);
        TiledMap TMX_CONTROL = assets.manager.get(assets.CONTROL);
        RENDERER = new OrthogonalTiledMapRenderer(TMX_MAP);

        mapWidth = Integer.parseInt(TMX_MAP.getProperties().get("width").toString());
        mapHeight = Integer.parseInt(TMX_MAP.getProperties().get("height").toString());
        tileSize = Integer.parseInt(TMX_MAP.getProperties().get("tilewidth").toString());

        layerWall = (TiledMapTileLayer) TMX_MAP.getLayers().get("Walls");
        layerPath = (TiledMapTileLayer) TMX_MAP.getLayers().get("Path");
        layerCollect = (TiledMapTileLayer) TMX_MAP.getLayers().get("Collectables");
        layerControlButton = (TiledMapTileLayer) TMX_CONTROL.getLayers().get("ControllerButtons");
        layerControlZone = (TiledMapTileLayer) TMX_CONTROL.getLayers().get("ControllerZone");
        TMX_MAP.getLayers().add(layerControlZone);
        TMX_MAP.getLayers().add(layerControlButton);


        matrix = new Tile[mapWidth][mapHeight];
        generateScreenMap();
    }

    /* Accessors */

    public OrthogonalTiledMapRenderer getRenderer() { return RENDERER; }

    public TiledMapTileLayer getLayerControlButton() { return layerControlButton; }

    public TiledMapTileLayer getLayerControlZone() { return layerControlZone; }

    public static int getMapHeight(){ return mapHeight; }

    public static int getMapWidth(){ return mapWidth; }

    public static int getTileSize(){ return tileSize; }

    public Tile[][] getMatrix(){ return matrix; }

    /**
     * Get a tile by position.
     * @param xPosition x-position of the tile
     * @param yPosition y-position of the tile
     * @return returns the tile
     */
    public Tile getTile(int xPosition, int yPosition){
        return matrix[xPosition / tileSize][yPosition / tileSize];
    }

    /**
     * Get the neighbouring tile in a certain direction.
     * @param xPosition x-position of the current tile
     * @param yPosition y-position of the current tile
     * @param dir direction to the neighbouring tile
     * @return returns the neighbouring tile
     */
    public Tile getTile(int xPosition, int yPosition, Actor.Direction dir){
        int nextCellX = ((xPosition/ tileSize));
        int nextCellY = ((yPosition/ tileSize));
        switch (dir) {
            case RIGHT:
                nextCellX = ((xPosition+ tileSize) / tileSize);
                break;
            case LEFT:
                nextCellX = ((xPosition- tileSize) / tileSize);
                break;
            case UP:
                nextCellY = ((yPosition+ tileSize) / tileSize);
                break;
            case DOWN:
                nextCellY = ((yPosition- tileSize) / tileSize);
                break;
        }
        return matrix[nextCellX][nextCellY];
    }

    /* Methods */

    /**
     * This Method will generate the matrix which holds the information about every Tile in the Map
     * It iterates through the matrix and adds specific tiles to it depending on the tile type.
     * It gets this information from the tmx-file layers.
     */
    private void generateScreenMap(){
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                if(layerWall.getCell(x, y) == null){
                    matrix[x][y] = new Tile(Tile.Type.EMPTY, ((x* tileSize)), ((y* tileSize)));
                    if(layerPath.getCell(x,y) != null){
                        Tile tile = new Tile(layerPath.getCell(x,y).getTile().getTextureRegion(), Tile.Type.PATH, (x* tileSize), (y* tileSize));
                        matrix[x][y] = tile;
                    }
                }
                else {
                    TextureRegion wallRegion = layerWall.getCell(x,y).getTile().getTextureRegion();
                    matrix[x][y] = new Tile(wallRegion, Tile.Type.WALL, (x* tileSize), (y* tileSize));
                }

            }
        }
    }

    /**
     * Generates all simple dots/score-points which can be collected by Pac-Man.
     * Should be implemented in child classes.
     * @param amount the total amount of Dots/Points generated on the map
     */
    protected abstract void generateCollectables(Tile.Item item, int amount);

    public int countItems() {
        return 0;
    }

    /**
     * This is a helper-method. it generates a cell with a texture-region depending on type specified.
     * it is mostly used to generate the correct texture for Collectables.
     * @param type specify type of the tile (exp. DOT)
     * @return returns the cell for the type specified
     */
    public TiledMapTileLayer.Cell createItem(Tile.Item type){
        TextureRegion region = null;
        switch (type){
            case DOT:
                if(firstMap) region = layerPath.getCell(1,17).getTile().getTextureRegion();
                else{
                    Texture tex = ASSETS.manager.get(ASSETS.COIN_GOLD);
                    region = new TextureRegion(tex);
                    region.setRegionX(128);
                    region.setRegionWidth(32);
                    region.setRegionY(0);
                    region.setRegionHeight(32);
                }
                break;
            case HUNTER:
                Texture tex = ASSETS.manager.get(ASSETS.ITEM_HUNTER);
                region = new TextureRegion(tex);
                region.setRegionX(0);
                region.setRegionWidth(32);
                region.setRegionY(0);
                region.setRegionHeight(32);
                break;
            case SLO_MO:
                Texture tex2 = ASSETS.manager.get(ASSETS.ITEM_SLO_MO);
                region = new TextureRegion(tex2);
                region.setRegionX(0);
                region.setRegionWidth(32);
                region.setRegionY(0);
                region.setRegionHeight(32);
                break;
            case TIME:
                Texture tex3 = ASSETS.manager.get(ASSETS.ITEM_TIME);
                region = new TextureRegion(tex3);
                region.setRegionX(0);
                region.setRegionWidth(32);
                region.setRegionY(0);
                region.setRegionHeight(32);
                break;
            case LIFE:
                Texture tex4 = ASSETS.manager.get(ASSETS.ITEM_LIFE);
                region = new TextureRegion(tex4);
                region.setRegionX(0);
                region.setRegionWidth(32);
                region.setRegionY(0);
                region.setRegionHeight(32);
                break;
        }
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        TiledMapTile t = new Tile(region);
        cell.setTile(t);
        return cell;
    }

    public void generateSpecialItem(){ }

    /**
     * This will delete a collectable from the map and plays a sound.
     * @param tile specify the tile from which you want to collect an item
     */
    public void collect(Tile tile){
        layerCollect.setCell(
                tile.getX()/ tileSize,
                tile.getY()/ tileSize,
                null
        );
    }

}

