package uas.lntv.pacmangame.Maps;

public class MenuMap extends Map {

    public MenuMap(String path){
        super(path);
        generateDots(0);
    }

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

    @Override
    public void collect(Tile tile) {
        super.collect(tile);
    }

}
