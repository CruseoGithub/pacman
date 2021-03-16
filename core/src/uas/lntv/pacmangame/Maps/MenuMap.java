package uas.lntv.pacmangame.Maps;

import uas.lntv.pacmangame.Managers.Assets;

/**
 * This class provides a simple map for the purpose of creating menus.
 * It will not create collectables besides the ones already provided by the tmx-mapfile
 */
public class MenuMap extends Map {

    /**
     * Does the same as the parent constructor, but creates no additional collectables.
     * @param path String value which contains the path to a tmx-Mapfile.
     * @param assets provide the Assetsmanager instance of the game.
     */
    public MenuMap(String path, Assets assets){
        super(path, assets);
        generateDots(0);
    }

    /**
     * searches for collectables in the tmx-mapfile layer and adds them to the tile matrix
     * @param total_Dots the total amount of Dots/Points generated on the map
     */
    @Override
    public void generateDots(int total_Dots) {
        for(int x = 0; x < MAP_WIDTH; x++){
            for(int y = 0; y < MAP_HEIGHT; y++){
                if(layerCollect.getCell(x, y) != null){
                    matrix[x][y].isDot = true;
                }
            }
        }
    }

    /**
     * Does the same as the parent constructor.
     * @param tile specify the tile from which you want to collect an item
     */
    @Override
    public void collect(Tile tile) {
        super.collect(tile);
    }

}
