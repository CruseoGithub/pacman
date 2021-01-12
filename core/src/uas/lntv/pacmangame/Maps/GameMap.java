package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public class GameMap extends Map {
    MapScreen screen;


    public GameMap(String path, MapScreen screen){
        super(path);
        this.screen = screen;
        generateDots(150);
    }



    public void generateDots(int total_Dots){
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
