package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class MenuMap extends Map {

    public MenuMap(String path){
        super(path);
        generateDots(0);
    }

    @Override
    public void generateDots(int total_Dots) {
        for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                if(layerCollect.getCell(x, y) != null){
                    matrix[x][y].isDot = true;
                }
            }
        }
    }

    @Override
    public void collect(Tile tile) {
        if(tile.isDot){
            layerCollect.setCell(
                    tile.getX()/tileSize,
                    tile.getY()/tileSize,
                    null
            );
            tile.isDot = false;
        }
    }
}
