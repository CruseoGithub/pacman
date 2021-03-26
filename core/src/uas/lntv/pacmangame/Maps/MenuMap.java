package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.Managers.PrefManager;

/**
 * This class provides a simple map for the purpose of creating menus.
 * It will not create collectables besides the ones already provided by the tmx-mapfile
 */
public class MenuMap extends Map {

    /* Fields */

    private final ArrayList<TiledMapTileLayer> HIGHLIGHT_LAYERS = new ArrayList<>();

    /* Constructor */

    /**
     * Does the same as the parent constructor, but creates no additional collectables.
     * @param path String value which contains the path to a tmx-Mapfile.
     * @param assets provide the Assetsmanager instance of the game.
     */
    public MenuMap(String path, Assets assets){
        super(path, assets);
        HIGHLIGHT_LAYERS.add((TiledMapTileLayer) TMX_MAP.getLayers().get("MusicOn"));
        HIGHLIGHT_LAYERS.add((TiledMapTileLayer) TMX_MAP.getLayers().get("MusicOff"));
        HIGHLIGHT_LAYERS.add((TiledMapTileLayer) TMX_MAP.getLayers().get("SFXOn"));
        HIGHLIGHT_LAYERS.add((TiledMapTileLayer) TMX_MAP.getLayers().get("SFXOff"));
        HIGHLIGHT_LAYERS.add((TiledMapTileLayer) TMX_MAP.getLayers().get("Joystick"));
        HIGHLIGHT_LAYERS.add((TiledMapTileLayer) TMX_MAP.getLayers().get("Buttons"));
        generateCollectables(Tile.Item.DOT,0);
    }

    /* Accessor */

    public ArrayList<TiledMapTileLayer> getHighlightLayers(){ return HIGHLIGHT_LAYERS; }

    /* Methods */

    /**
     * searches for collectables in the tmx-mapfile layer and adds them to the tile matrix
     * @param amount the total amount of Dots/Points generated on the map
     */
    @Override
    protected void generateCollectables(Tile.Item item, int amount) {
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                if(layerCollect.getCell(x, y) != null){
                    matrix[x][y].placeItem(item);
                }
            }
        }
    }

    /**
     * Does the same as the parent method and plays a sound.
     * @param tile specify the tile from which you want to collect an item
     */
    @Override
    public void collect(Tile tile) {
        if(tile.getItem() == Tile.Item.DOT){
            tile.takeItem();
            if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.DOT).play(0.25f);
            super.collect(tile);
        }
    }

}
