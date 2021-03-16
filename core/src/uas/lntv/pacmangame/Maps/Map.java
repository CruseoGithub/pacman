package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Sprites.Actor;

/**
 * This class is generates a Tiled Map which can be loaded onto a Screen.
 * It provides a matrix which holds information about map dimensions and layout (Path/Wall/Collectable)
 * This class is abtract and should be implemented as a GameMap or MenuMap
 */
public abstract class Map {
        private final boolean firstMap;
        public OrthogonalTiledMapRenderer renderer;
        protected final Assets ASSETS;

        public TiledMapTileLayer layerWall;
        public TiledMapTileLayer layerPath;
        public TiledMapTileLayer layerCollect;

        private final TiledMap TMX_CONTROL;
        public TiledMapTileLayer layerControlButton;
        public TiledMapTileLayer layerControlZone;

        protected final int MAP_WIDTH;
        protected final int MAP_HEIGHT;
        protected final int TILE_SIZE;

        public Tile[][] matrix;

        public final int getMapWidth(){ return this.MAP_WIDTH; }
        public final int getMapHeight(){ return this.MAP_HEIGHT; }
        public final int getTileSize(){ return this.TILE_SIZE; }

        /**
         * The constructor loads the graphic layers of the tmx-Mapfile and sets them up into a Maprenderer
         * It generates a matrix of the Map which contains a Tile for each cell.
         * It provides methods for generating and collecting Collectables which should be implemented as needed by the child class.
         * @param path String value which contains the path to a tmx-Mapfile.
         * @param assets provide the Assetsmanager instance of the game.
         */
        public Map(String path, Assets assets){
            this.ASSETS = assets;
            firstMap = path.equals("maps/map.tmx");
            TmxMapLoader tmxMapLoader = new TmxMapLoader();
            TiledMap tmxMap = tmxMapLoader.load(path);
            TMX_CONTROL = assets.manager.get(assets.CONTROL);
            renderer = new OrthogonalTiledMapRenderer(tmxMap);

            MAP_WIDTH = Integer.parseInt(tmxMap.getProperties().get("width").toString());
            MAP_HEIGHT = Integer.parseInt(tmxMap.getProperties().get("height").toString());
            TILE_SIZE = Integer.parseInt(tmxMap.getProperties().get("tilewidth").toString());

            layerWall = (TiledMapTileLayer)tmxMap.getLayers().get("Walls");
            layerPath = (TiledMapTileLayer)tmxMap.getLayers().get("Path");
            layerCollect = (TiledMapTileLayer)tmxMap.getLayers().get("Collectables");
            layerControlButton = (TiledMapTileLayer) TMX_CONTROL.getLayers().get("ControllerButtons");
            layerControlZone = (TiledMapTileLayer)TMX_CONTROL.getLayers().get("ControllerZone");
            tmxMap.getLayers().add(layerControlZone);
            tmxMap.getLayers().add(layerControlButton);
            tmxMap.getLayers().get("ControllerZone").

            //layerPath.setOpacity(0.5f);
            matrix = new Tile[MAP_WIDTH][MAP_HEIGHT];
            generateScreenMap();
        }

        /**
         * This Method will generade the matrix which holds the information about every Tile in the Map
         * It iterates through the matrix and adds specific tiles to it depending on the tile type.
         * it gets this information from the tmx-file layers
         */
        private void generateScreenMap(){
            for(int x = 0; x < MAP_WIDTH; x++){
                for(int y = 0; y < MAP_HEIGHT; y++){
                    if(layerWall.getCell(x, y) == null){
                        matrix[x][y] = new Tile(Tile.Type.EMPTY, ((x* TILE_SIZE)), ((y* TILE_SIZE)));
                        if(layerPath.getCell(x,y) != null){
                            Tile tile = new Tile(layerPath.getCell(x,y).getTile().getTextureRegion(), Tile.Type.PATH, (x* TILE_SIZE), (y* TILE_SIZE));
                            matrix[x][y] = tile;
                        }
                    }
                    else {
                        TextureRegion wallRegion = layerWall.getCell(x,y).getTile().getTextureRegion();
                        matrix[x][y] = new Tile(wallRegion, Tile.Type.WALL, (x* TILE_SIZE), (y* TILE_SIZE));
                    }

                }
            }
        }

        /**
         * generates all simple dots/scorepoints which can be collected by Pac-Man.
         * Should be implemented in child classes.
         * @param total_Dots the total amount of Dots/Points generated on the map
         */
        public abstract void generateDots(int total_Dots);

        /**
         * this is a helper-method. it generates a textureregion depending on type specified.
         * it is mostly used to generate the correct texture for Collectables.
         * @param type specify type of the tile (exp. DOT)
         * @return returns the textureregion for the type specified
         */
        public TextureRegion createTextureRegion(Tile.Type type){
            TextureRegion region = null;
            if(firstMap){
                if(type == Tile.Type.DOT) {
                        region = layerPath.getCell(1,17).getTile().getTextureRegion();
                }
            }
            else {
                Texture tex = ASSETS.manager.get(ASSETS.TILES);
                region = new TextureRegion(tex);
                if(type == Tile.Type.DOT) {
                        tex = ASSETS.manager.get(ASSETS.COIN_GOLD);
                        region = new TextureRegion(tex);
                        region.setRegionX(128);
                        region.setRegionWidth(32);
                        region.setRegionY(0);
                        region.setRegionHeight(32);
                }
            }
            return region;
        }

        /**
         * get a tile by position
         * @param xPosition x-position of the tile
         * @param yPosition y-position of the tile
         * @return returns the tile
         */
        public Tile getTile(int xPosition, int yPosition){
            return matrix[xPosition / TILE_SIZE][yPosition / TILE_SIZE];
        }

    /**
     * get the neighbouring tile in a certain direction
     * @param xPosition x-position of the current tile
     * @param yPosition y-position of the current tile
     * @param dir direction to the neighbouring tile
     * @return returns the neighbouring tile
     */
        public Tile getTile(int xPosition, int yPosition, Actor.Direction dir){
            int nextCellX = ((xPosition/ TILE_SIZE));
            int nextCellY = ((yPosition/ TILE_SIZE));
            switch (dir) {
                case RIGHT:
                    nextCellX = ((xPosition+ TILE_SIZE) / TILE_SIZE);
                    break;
                case LEFT:
                    nextCellX = ((xPosition- TILE_SIZE) / TILE_SIZE);
                    break;
                case UP:
                    nextCellY = ((yPosition+ TILE_SIZE) / TILE_SIZE);
                    break;
                case DOWN:
                    nextCellY = ((yPosition- TILE_SIZE) / TILE_SIZE);
                    break;

            }
            return matrix[nextCellX][nextCellY];
        }

        /**
         * Not implemented !!!!!!!!!!!
         * @param tile
         * @param type
         */
        public void setTile(Tile tile, Tile.Type type){
            tile.type = type;
        }

        /**
         * this will delete a collectable from the map and plays a sound
         * @param tile specify the tile from which you want to collect an item
         */
        public void collect(Tile tile){
            if(tile.isDot){
                if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.DOT).play(0.25f);
                layerCollect.setCell(
                        tile.getX()/ TILE_SIZE,
                        tile.getY()/ TILE_SIZE,
                        null
                );
                tile.isDot = false;
            }
        }

}

