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
        private TmxMapLoader maploader;
        private TiledMap tmxMap;
        public OrthogonalTiledMapRenderer renderer;

        public TiledMapTileLayer layerWall;
        public TiledMapTileLayer layerPath;
        public TiledMapTileLayer layerCollect;

        private TiledMap tmxControl;
        public TiledMapTileLayer layerControlButton;
        //public TiledMapObjectL layerControlTouch;

        public int mapWidth;
        public int mapHeight;
        public int tileSize;
        public String path;

        public Tile matrix[][];

        private Sound sound;


        public Map(String path){
            maploader = new TmxMapLoader();
            tmxMap = maploader.load(path);
            tmxControl = maploader.load("controller.tmx");
            renderer = new OrthogonalTiledMapRenderer(tmxMap);

            mapWidth = Integer.parseInt(tmxMap.getProperties().get("width").toString());
            mapHeight = Integer.parseInt(tmxMap.getProperties().get("height").toString());
            tileSize = Integer.parseInt(tmxMap.getProperties().get("tilewidth").toString());

            layerWall = (TiledMapTileLayer)tmxMap.getLayers().get("Walls");
            layerPath = (TiledMapTileLayer)tmxMap.getLayers().get("Path");
            layerCollect = (TiledMapTileLayer)tmxMap.getLayers().get("Collectables");
            layerControlButton = (TiledMapTileLayer)tmxControl.getLayers().get("ControllerButtons");
            //layerControlTouch = (TiledMapTileLayer)tmxControl.getLayers().get("ControllerTouch");
            tmxMap.getLayers().add(layerControlButton);
            //tmxMap.getLayers().add(layerControlTouch);

            //layerPath.setOpacity(0.5f);
            matrix = new Tile[mapWidth][mapHeight];
            this.path = path;
            generateScreenMap();
            this.sound = Gdx.audio.newSound(Gdx.files.internal("dot.wav"));
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
            Tile tile = matrix[(int) xPosition/tileSize][(int)yPosition/tileSize];
            return tile;
        }

        public Tile getTile(int xPosition, int yPosition, Actor.Direction dir){
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
                sound.play(0.3f);
                layerCollect.setCell(
                        tile.getX()/tileSize,
                        tile.getY()/tileSize,
                        null
                );
                tile.isDot = false;
            }
        }

        public void dispose(){
            tmxMap.dispose();
        }
}

