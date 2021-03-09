package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import uas.lntv.pacmangame.Sprites.Actor;

public abstract class Map {
        private TiledMap tmxMap;
        public OrthogonalTiledMapRenderer renderer;

        public TiledMapTileLayer layerWall;
        public TiledMapTileLayer layerPath;
        public TiledMapTileLayer layerCollect;

        private TiledMap tmxControl;
        public TiledMapTileLayer layerControlButton;
        //public TiledMapObjectL layerControlTouch;

        protected final int MAP_WIDTH;
        protected final int MAP_HEIGHT;
        protected final int TILE_SIZE;
        private String path;

        public Tile matrix[][];

        private Sound sound;

        public final int getMapWidth(){ return this.MAP_WIDTH; }
        public final int getMapHeight(){ return this.MAP_HEIGHT; }
        public final int getTileSize(){ return this.TILE_SIZE; }

        public Map(String path){
            TmxMapLoader mapLoader = new TmxMapLoader();
            tmxMap = mapLoader.load(path);
            tmxControl = mapLoader.load("controller.tmx");
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
            this.path = path;
            generateScreenMap();
            this.sound = Gdx.audio.newSound(Gdx.files.internal("dot.wav"));
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
            Tile tile = matrix[(int) xPosition/ TILE_SIZE][(int)yPosition/ TILE_SIZE];
            return tile;
        }

        public Tile getTile(int xPosition, int yPosition, Actor.Direction dir){
            int nextCellX = (int) ((xPosition/ TILE_SIZE));
            int nextCellY = (int) ((yPosition/ TILE_SIZE));
            Tile tile;
            switch (dir) {
                case RIGHT:
                    nextCellX = (int) ((xPosition+ TILE_SIZE) / TILE_SIZE);
                    break;
                case LEFT:
                    nextCellX = (int) ((xPosition- TILE_SIZE) / TILE_SIZE);
                    break;
                case UP:
                    nextCellY = (int) ((yPosition+ TILE_SIZE) / TILE_SIZE);
                    break;
                case DOWN:
                    nextCellY = (int) ((yPosition- TILE_SIZE) / TILE_SIZE);
                    break;

            }
            return matrix[nextCellX][nextCellY];
        }

        public void setTile(Tile tile, Tile.Type type){
            tile.type = type;
        }

        public void collect(Tile tile){
            if(tile.isDot){
                sound.play(0.25f);
                layerCollect.setCell(
                        tile.getX()/ TILE_SIZE,
                        tile.getY()/ TILE_SIZE,
                        null
                );
                tile.isDot = false;
            }
        }

        public void dispose(){
            sound.dispose();
            tmxMap.dispose();
        }
}

