package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Enemy;

/**
 * this class sets up a map for a game level.
 * It implements some additional code for generating and collecting collectables
 */
public class GameMap extends Map {
    MapScreen screen;
    ArrayList<Vector3> hunterItems = new ArrayList<>();
	
    /**
     * does the same as the parent constructor.
     * Additionaly it generates collectables and provides a method to collect them.
     * @param assets instance of the Assetmanager
     * @param path  string value which contains the path to a tmx-Mapfile.
     * @param screen instance of a Screen which contains this map
     */
    public GameMap(Assets assets, String path, MapScreen screen){
        super(path, assets);
        this.screen = screen;

        //Hunter Item Positions
        hunterItems.add(new Vector3(1, 21, 0));
        hunterItems.add(new Vector3(1, 36, 0));
        hunterItems.add(new Vector3(26, 21, 0));
        hunterItems.add(new Vector3(26, 36, 0));

        generateItems();
        generateDots(150);
    }

    /**
     * generates Collectables (Not Dots!)
     */
    public void generateItems(){
        for(int x = 0; x < MAP_WIDTH; x++){
            for(int y = 0; y < MAP_HEIGHT; y++){
                //Generate hunter items on the map
                for (Vector3 pos: hunterItems) {
                    if(x == pos.x && y == pos.y){
                        layerCollect.setCell(x, y, createItem(Tile.Item.HUNTER));
                        matrix[x][y].placeItem(Tile.Item.HUNTER);
                    }
                }
            }
        }
        generateRandomItem();
    }
    @Override
    public void generateRandomItem(){
            Tile.Item random = randomItem();
            layerCollect.setCell((int)randomItemPos.x, (int)randomItemPos.y, createItem(random));
            matrix[(int)randomItemPos.x][(int)randomItemPos.y].placeItem(random);
    }
    public Tile.Item randomItem(){
        int max = 5;
        int min = 2;
        int random = (int) (Math.random() * (max - min + 1) + min); // random ist zwischen 1 und 4
        Tile.Item itemList[] = Tile.Item.values();
        return itemList[random];
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
                        if (layerPath.getCell(x, y) != null & !matrix[x][y].isItem() && x > 0 && x < (MAP_WIDTH - 2)) { //X-Abfrage: Dots sollen nicht im Teleportgang spawnen
                            int max = 1;
                            int min = 0;
                            int random = (int) (Math.random() * (max - min + 1) + min); // random ist entweder 0 oder 1
                            if (random > 0) {
                                layerCollect.setCell(x, y, createItem(Tile.Item.DOT));
                                matrix[x][y].placeItem(Tile.Item.DOT);
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
     * additionally it will in increase Pac-Mans score value.
     * If the collectable is a hunter item it will evolve Pac-Man to SuperPacMan and set the ghosts to a frightened state.
     * @param tile specify the tile from which you want to collect an item
     */
    public void collect(Tile tile){
        switch (tile.getItem()){
            case DOT:
                tile.takeItem();
                if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.DOT).play(0.25f);
                PacManGame.increaseScore(1);
                screen.hud.levelScore++;
                screen.hud.update();
                break;
            case HUNTER:
                tile.takeItem();
                if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.POWER_UP).play(0.1f);
                screen.activateBuff(Tile.Item.HUNTER);
                for(Enemy ghost : screen.getGhosts()){
                    ghost.setDifficulty(Enemy.Difficulty.RUNAWAY);
                }
                break;
            case SLOWMO:
                tile.takeItem();
                if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.POWER_UP).play(0.1f);
                screen.activateBuff(Tile.Item.SLOWMO);
                break;
            case TIME:
                tile.takeItem();
                if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.POWER_UP).play(0.1f);
                screen.activateBuff(Tile.Item.TIME);
                break;
            case LIFE:
                tile.takeItem();
                if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.POWER_UP).play(0.1f);
                screen.activateBuff(Tile.Item.LIFE);
                break;
        }
        super.collect(tile);
    }

}
