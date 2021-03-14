package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Enemy;

public class GameMap extends Map {
    MapScreen screen;

    public GameMap(Assets assets, String path, MapScreen screen){
        super(path, assets);
        this.screen = screen;
        generateItems();
        generateDots(150);
    }

    public void generateItems(){
        for(int x = 0; x < MAP_WIDTH; x++){
            for(int y = 0; y < MAP_HEIGHT; y++){
                if(layerCollect.getCell(x,y) != null) {
                    matrix[x][y].placeItem();
                }
            }
        }
    }

    public void generateDots(int total_Dots){
        while(total_Dots > 0){
            for(int x = 0; x < MAP_WIDTH; x++){
                for(int y = 0; y < MAP_HEIGHT; y++){
                    if(layerWall.getCell(x, y) == null && total_Dots>0 && layerCollect.getCell(x,y) == null){
                        if (layerPath.getCell(x, y) != null & !matrix[x][y].isDot && x > 0 && x < (MAP_WIDTH - 2)) { //X-Abfrage: Dots sollen nicht im Teleportgang spawnen
                            int max = 1;
                            int min = 0;
                            int random = (int) (Math.random() * (max - min + 1) + min); // random ist entweder 0 oder 1
                            if (random > 0) {
                                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                                TiledMapTile t = new Tile(createTextureRegion(Tile.Type.DOT));
                                cell.setTile(t);
                                layerCollect.setCell(x, y, cell);
                                matrix[x][y].isDot = true;
                                total_Dots--;
                            }
                        }
                    }
                }
            }
        }
    }

    public void collect(Tile tile){
        if(tile.isItem()){
            layerCollect.setCell(
                    tile.getX()/ TILE_SIZE,
                    tile.getY()/ TILE_SIZE,
                    null
            );
            tile.takeItem();
            ASSETS.manager.get(ASSETS.POWER_UP).play(0.1f);
            screen.evolvePacMan();
            for(Enemy ghost : screen.getGhosts()){
                ghost.setDifficulty(Enemy.Difficulty.RUNAWAY);
            }
        }
        if(tile.isDot){
            PacManGame.increaseScore(1);
            screen.hud.levelScore++;
            screen.hud.update();
        }
        super.collect(tile);
    }

}
