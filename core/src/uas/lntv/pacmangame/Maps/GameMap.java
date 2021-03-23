package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import uas.lntv.pacmangame.Managers.Assets;
import uas.lntv.pacmangame.PacManGame;
import uas.lntv.pacmangame.Managers.PrefManager;
import uas.lntv.pacmangame.Screens.MapScreen;
import uas.lntv.pacmangame.Sprites.Enemy;

/**
 * This class sets up a map for a game level.
 * It implements some additional code for generating and collecting collectables
 */
public class GameMap extends Map {
    MapScreen screen;
    ArrayList<Vector3> collectablesPos = new ArrayList<>();
	
    /**
     * does the same as the parent constructor.
     * Additionally it generates collectables and provides a method to collect them.
     * @param assets instance of the Assetmanager
     * @param path  string value which contains the path to a tmx-Mapfile.
     * @param screen instance of a Screen which contains this map
     */
    public GameMap(Assets assets, String path, MapScreen screen){
        super(path, assets);
        this.screen = screen;

        //Collectables Positions on the map
        collectablesPos.add(new Vector3(1, 21, 0));
        collectablesPos.add(new Vector3(1, 36, 0));
        collectablesPos.add(new Vector3(26, 21, 0));
        collectablesPos.add(new Vector3(26, 36, 0));

        generateSpecialItem();
        generateCollectables(Tile.Item.DOT, 150);
    }

    /**
     * generates Collectables (Not Dots!)
     */
    public void generateSpecialItem(){
        ArrayList<Tile.Item> exsitingItems = new ArrayList<Tile.Item>();
        for (Vector3 pos: collectablesPos) {
            if(matrix[(int) pos.x][(int) pos.y].isItem()){
                exsitingItems.add(matrix[(int) pos.x][(int) pos.y].getItem());
            }else{
                exsitingItems.add(Tile.Item.EMPTY);
            }
        }


        /*for(int x = 0; x < mapWidth; x++){
            for(int y = 0; y < mapHeight; y++){
                //Generate item on the map
                for (Vector3 pos: collectablesPos) {
                    if(x == pos.x && y == pos.y){
                        layerCollect.setCell(x, y, createItem(Tile.Item.HUNTER));
                        matrix[x][y].placeItem(Tile.Item.HUNTER);
                    }
                }
            }
        }*/
        //generateRandomItem();
    }

    /**
     * Generates an item based on priority/percentage on the map.
     */
    @Override
    public void generateRandomItem(){
            /*
            Tile.Item random = randomItem(new ArrayList<Tile.Item>);
            layerCollect.setCell((int)randomItemPos.x, (int)randomItemPos.y, createItem(random));
            matrix[(int)randomItemPos.x][(int)randomItemPos.y].placeItem(random);
            */
    }

    /**
     * It will generate an array that represents a percentage cake that includes every item based on its specific priority
     * From this array it will choose a random item.
     * @return returns a random item based on percentage ( percentage is based on the golden ratio )
     */
    public Tile.Item randomItem(ArrayList<Tile.Item> exsitingItems){
        Tile.Item[] itemList = Tile.Item.values();

        int[] percentList = getPercentage(Tile.Item.values().length-2);

        int threshold = 10;
        for(int i = 0; i < percentList.length; i++){
            if(exsitingItems.get(i) != Tile.Item.EMPTY){
                int rest = 0;
                if(percentList[i] > threshold){
                    if(percentList[i] % 2 == 0){
                        percentList[i] /= 2;
                        rest = percentList[i];
                    }else{
                        percentList[i] =(int)(percentList[i] / 2);
                        rest = percentList[i]+1;
                    }

                    while (rest > 0){
                        if(i < percentList.length-1){
                            int perItem = (int) rest / percentList.length-i;
                            if(perItem > 1){
                                for(int j = i+1; j < percentList.length; j++){
                                    percentList[j] += perItem;
                                    rest -= perItem;
                                }
                            } else {
                                //In case rest is uneven it will give 1% to a Life Item
                                percentList[5] += rest; // 5 = LIFE
                                rest = 0;
                            }
                        }else{
                            percentList[percentList.length-1]
                        }

                    }
                }
            }
        }


        //destributes the percentages to a array[100]
        Tile.Item[] percentCake = new Tile.Item[100];
        int j = 2;
        for(int i = 0; i < 100; i++){
            if(percentList[j-2] > 0) {
                percentCake[i] = itemList[j];
                percentList[j-2]--;
            }
            else j++;
        }





        //Picks a random item from the array
        int min = 0;
        int max = percentCake.length-1; //length is 100
        int random = (int) (Math.random() * (max - min + 1) + min);
        return percentCake[random];
    }

    /**
     * This calculates the golden ratio based on the number of items
     * @param items total number of items
     * @return returns the golden ratio
     */
    public static double goldenRatio(int items) {
        if (items == 0) return 1;
        return 1.0 + 1.0 / goldenRatio(items-1);
    }

    /**
     * it calculates a percentage portion for each item based on the golden ration. (it wont amount to exactly 100% though)
     * Any left over percentages will be destributed equaly to every item
     * @param items total number of items
     * @return returns a list of percentages for each item
     */
    public static int[] getPercentage(int items) {
        double ratio = goldenRatio(items);

        //Calculating every percentage portion fore each item
        int[] percentList = new int[items];
        double max_old, max_new, percent;
        max_old = 100.0;
        for (int i = 0; i < items; i++){
            max_new = max_old / ratio;
            percent = max_old - max_new;

            //fractions bigger than one half shall be rounded up to the nearest whole number;
            //fractions smaller than one half shall be rounded down to the nearest whole number.
            if((percent-(int)percent) < 0.5) percentList[i] = (int)percent;
            else percentList[i] = (int)percent + 1;
            max_old = max_new;
        }
        int total = 0;
        for(double p : percentList){
            total += p;
        }

        //if the calculation did not add up to 100% the remaining percentage will be distributed to every item
        if(total < 100){
            total = 100 - total;
            while (total > 0){
                int perItem = (int)(total/items);
                if(perItem > 1){
                    for(int i = 0; i < percentList.length; i++){
                        percentList[i] += perItem;
                        total -= perItem;
                    }
                } else {
                    percentList[0] += total;
                    total = 0;
                }
            }
        }
        return percentList;
    }

    /**
     * generates all simple dots/scorepoints which can be collected by Pac-Man.
     * It does this by iterating through the tile matrix and placing items by chance (default: 50% chance) until it reaches a total amount of items.
     * @param amount the total amount of Dots/Points generated on the map
     */
    public void generateCollectables(Tile.Item item, int amount){
        while(amount > 0){
            for(int x = 0; x < mapWidth; x++){
                for(int y = 0; y < mapHeight; y++){
                    if(layerWall.getCell(x, y) == null && amount >0 && layerCollect.getCell(x,y) == null){
                        if (layerPath.getCell(x, y) != null & !matrix[x][y].isItem() && x > 0 && x < (mapWidth - 2)) { //X-Abfrage: Dots sollen nicht im Teleportgang spawnen
                            int max = 1;
                            int min = 0;
                            int random = (int) (Math.random() * (max - min + 1) + min); // random ist entweder 0 oder 1
                            if (random > 0) {
                                layerCollect.setCell(x, y, createItem(item));
                                matrix[x][y].placeItem(item);
                                amount--;
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
