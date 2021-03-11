package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.Sprites.Actor;

public abstract class Map {
        private final boolean firstMap;
        public OrthogonalTiledMapRenderer renderer;
        protected final Assets ASSETS;

        public TiledMapTileLayer layerWall;
        public TiledMapTileLayer layerPath;
        public TiledMapTileLayer layerCollect;

        private TiledMap tmxControl;
        public TiledMapTileLayer layerControlButton;
        //public TiledMapObjectL layerControlTouch;

        protected final int MAP_WIDTH;
        protected final int MAP_HEIGHT;
        protected final int TILE_SIZE;

        public Tile matrix[][];

        public final int getMapWidth(){ return this.MAP_WIDTH; }
        public final int getMapHeight(){ return this.MAP_HEIGHT; }
        public final int getTileSize(){ return this.TILE_SIZE; }

        public Map(String path, Assets assets){
            this.ASSETS = assets;
            firstMap = path.equals("maps/map.tmx");
            TmxMapLoader tmxMapLoader = new TmxMapLoader();
            TiledMap tmxMap = tmxMapLoader.load(path);
            tmxControl = assets.manager.get(assets.CONTROL);
            renderer = new OrthogonalTiledMapRenderer(tmxMap);

            MAP_WIDTH = Integer.parseInt(tmxMap.getProperties().get("width").toString());
            MAP_HEIGHT = Integer.parseInt(tmxMap.getProperties().get("height").toString());
            TILE_SIZE = Integer.parseInt(tmxMap.getProperties().get("tilewidth").toString());

            layerWall = (TiledMapTileLayer)tmxMap.getLayers().get("Walls");
            layerPath = (TiledMapTileLayer)tmxMap.getLayers().get("Path");
            layerCollect = (TiledMapTileLayer)tmxMap.getLayers().get("Collectables");
            layerControlButton = (TiledMapTileLayer)tmxControl.getLayers().get("ControllerButtons");
            //layerControlTouch = (TiledMapTileLayer)tmxControl.getLayers().get("ControllerTouch");
            tmxMap.getLayers().add(layerControlButton);
            //tmxMap.getLayers().add(layerControlTouch);

            //layerPath.setOpacity(0.5f);
            matrix = new Tile[MAP_WIDTH][MAP_HEIGHT];
            generateScreenMap();
        }

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

        public abstract void generateDots(int total_Dots);

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

        public Tile getTile(int xPosition, int yPosition){
            return matrix[xPosition / TILE_SIZE][yPosition / TILE_SIZE];
        }

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

        public void setTile(Tile tile, Tile.Type type){
            tile.type = type;
        }

        public void collect(Tile tile){
            if(tile.isDot){
                ASSETS.manager.get(ASSETS.DOT).play(0.25f);
                layerCollect.setCell(
                        tile.getX()/ TILE_SIZE,
                        tile.getY()/ TILE_SIZE,
                        null
                );
                tile.isDot = false;
            }
        }

}

