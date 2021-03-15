package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import uas.lntv.pacmangame.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Enemy;

/**
 * this class sets up a map for a game level.
 * It implements some additional code for generating and collecting collectables
 */
public class GameMap extends Map {
    MapScreen screen;
    private final PacManGame GAME;

    /**
     * does the same as the parent constructor.
     * Additionaly it generates collectables and provides a method to collect them.
     * @param game instance of PacManGame
     * @param assets instance of the Assetmanager
     * @param path  string value which contains the path to a tmx-Mapfile.
     * @param screen instance of a Screen which contains this map
     */
    public GameMap(PacManGame game, Assets assets, String path, MapScreen screen){
        super(path, assets);
        this.GAME = game;
        this.screen = screen;
        generateItems();
        generateDots(150);
    }

    /**
     * generates hunter items
     */
    public void generateItems(){
        for(int x = 0; x < MAP_WIDTH; x++){
            for(int y = 0; y < MAP_HEIGHT; y++){
                if(layerCollect.getCell(x,y) != null) {
                    matrix[x][y].placeItem();
                }
            }
        }
    }

    /**
     * generates all simple dots/scorepoints which can be collected by Pac-Man.
     * It does this by iterating through the tile matrix and placing items by chance (default: 50% chance) until it reaches a total amount of items.
     * @param total_Dots the total amount of Dots/Points generated on the map
     */
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

    /**
     * this will delete a collectable from the map and plays a sound
     * additionaly it will in increase Pac-Mans score value.
     * If the collectable is a hunter item it will evolve Pac-Man to SuperPacMan and set the ghosts to a freightened state.
     * @param tile specify the tile from which you want to collect an item
     */
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
            GAME.increaseScore(1);
            screen.hud.levelScore++;
            screen.hud.update();
        }
        super.collect(tile);
    }

}
