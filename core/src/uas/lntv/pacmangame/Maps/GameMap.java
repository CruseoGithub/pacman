package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.GameScreen;
import uas.lntv.pacmangame.Screens.MapScreen;

public class GameMap extends Map {
    MapScreen screen;
    private PacManGame game;

    private Sound powerUp;

    public GameMap(PacManGame game, String path, MapScreen screen){
        super(path);
        this.game = game;
        this.screen = screen;
        generateItems();
        generateDots(150);
        powerUp = Gdx.audio.newSound(Gdx.files.internal("PowerUp.wav"));
    }

    public void generateItems(){
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                if(layerCollect.getCell(x,y) != null) {
                    matrix[x][y].placeItem();
                }
            }
        }
    }

    public void generateDots(int total_Dots){
        while(total_Dots > 0){
            for(int x = 0; x < mapWidth; x++){
                for(int y = 0; y < mapHeight; y++){
                    if(layerWall.getCell(x, y) == null && total_Dots>0 && layerCollect.getCell(x,y) == null){
                        if (layerPath.getCell(x, y) != null & !matrix[x][y].isDot && x > 0 && x < (mapWidth - 2)) { //X-Abfrage: Dots sollen nicht im Teleportgang spawnen
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
                    tile.getX()/tileSize,
                    tile.getY()/tileSize,
                    null
            );
            tile.takeItem();
            powerUp.play(0.4f);
        }
        if(tile.isDot){
            game.increaseScore(1);
            screen.hud.levelScore++;
            screen.hud.update();
        }
        super.collect(tile);
    }

}
