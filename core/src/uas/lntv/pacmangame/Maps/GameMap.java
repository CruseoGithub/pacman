package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.math.Vector2;

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

    /* Fields */

    private final ArrayList<Vector2> ITEM_POSITIONS = new ArrayList<>();
    private final MapScreen SCREEN;
    private static int collectedDots;

    public final static int TOTAL_DOTS = 150;

	/* Constructor */

    /**
     * Does the same as the parent constructor.
     * Additionally it generates collectables and provides a method to collect them.
     * @param assets instance of the assets manager
     * @param path  string value which contains the path to a tmx-map-file.
     * @param screen instance of a Screen which contains this map
     */
    public GameMap(Assets assets, String path, MapScreen screen){
        super(path, assets);
        this.SCREEN = screen;

        //Collectables Positions on the map
        ITEM_POSITIONS.add(new Vector2(1, 21));
        ITEM_POSITIONS.add(new Vector2(1, 36));
        ITEM_POSITIONS.add(new Vector2(26, 21));
        ITEM_POSITIONS.add(new Vector2(26, 36));

        for (Vector2 pos: ITEM_POSITIONS) {
            matrix[(int) pos.x][(int) pos.y].placeItem(Tile.Item.EMPTY);
            layerCollect.setCell((int) pos.x, (int) pos.y, null);
        }
        for(int i = 0 ; i< 4; i++){
            generateSpecialItem();
        }
        generateCollectables(Tile.Item.DOT, TOTAL_DOTS);
        collectedDots = 0;
    }

    /* Accessor */

    public static int getCollectedDots(){ return collectedDots; }

    /* Methods */

    /**
     * This calculates the golden ratio based on the number of items
     * @param items total number of items
     * @return returns the golden ratio
     */
    private static double goldenRatio(int items) {
        if (items == 0) return 1;
        return 1.0 + 1.0 / goldenRatio(items-1);
    }

    /**
     * It calculates a percentage portion for each item based on the golden ration. (it wont amount to exactly 100% though)
     * Any left over percentages will be distributed equally to every item
     * @param items total number of items
     * @return returns a list of percentages for each item
     */
    private static int[] getPercentage(int items) {
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
                int perItem = total/items;
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
     * It will generate an array that represents a percentage cake that includes every item based on its specific priority
     * From this array it will choose a random item.
     * @param existingItems the ArrayList that contains which kinds of items are already on the map
     * @return returns a random item based on percentage ( percentage is based on the golden ratio )
     */
    private Tile.Item newItem(ArrayList<Tile.Item> existingItems){
        Tile.Item[] itemList = Tile.Item.values();

        int[] percentList = getPercentage(Tile.Item.values().length-2);

        int threshold = 10;
        for(int i = 0; i < percentList.length; i++){

            //If the item already exists
            if(existingItems.get(i) != Tile.Item.EMPTY){
                int rest;
                //if the percentage of an item is above the minimum threshold
                if(percentList[i] > threshold){
                    if(percentList[i] % 2 == 0){
                        percentList[i] /= 2;
                        rest = percentList[i];
                    }else{
                        percentList[i] = percentList[i] / 2;
                        rest = percentList[i]+1;
                    }
                    // redistributes the rest to the rarer items
                    while (rest > 0){
                        if(i < percentList.length-1){
                            int perItem = rest / percentList.length-i-1;
                            if(perItem >= 1){
                                for(int j = i+1; j < percentList.length; j++){
                                    percentList[j] += perItem;
                                    rest -= perItem;
                                }
                            } else {
                                //In case the rest can not be distributed to all the rarest item will get the rest ( rest < sum(all items) )
                                percentList[0] += rest; // 5 = LIFE
                                rest = 0;
                            }
                        }else{
                            //if the current item is the last item it gives the rest to itself
                            percentList[percentList.length-1] = rest;
                            rest = 0;
                        }

                    }
                }
            }
        }

        //distributes the percentages to an array[100]
        Tile.Item[] percentCake = new Tile.Item[100];
        int j = 2;
        for(int i = 0; i < 100; i++){
            if(percentList[j-2] > 0) {
                percentCake[i] = itemList[j];
                percentList[j-2]--;
            }
            else{
                if(j != itemList.length-1) j++;
                percentCake[i] = itemList[j];
            }
        }

        //Picks a random item from the array
        int min = 0;
        int max = percentCake.length-1; //length is 100
        int random = (int) (Math.random() * (max - min + 1) + min);
        return percentCake[random];
    }

    /**
     * Generates all simple dots/score-points which can be collected by Pac-Man. (would also work for special items)
     * It does this by iterating through the tile matrix and placing items by chance (default: 50% chance) until it reaches a total amount of items.
     * @param amount the total amount of Dots/Points generated on the map
     */
    protected void generateCollectables(Tile.Item item, int amount) {
        while (amount > 0) {
            for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                    if (layerWall.getCell(x, y) == null && amount > 0 && layerCollect.getCell(x, y) == null) {
                        if (layerPath.getCell(x, y) != null & !matrix[x][y].isItem() && x > 0 && x < (mapWidth - 2)) { //X-Abfrage: Dots sollen nicht im Teleportgang spawnen
                            int max = 1;
                            int min = 0;
                            int random = (int) (Math.random() * (max - min + 1) + min); // random is either 0 or 1
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
     * This will delete a collectable from the map and plays a sound
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
                collectedDots++;
                SCREEN.getHud().update();
                break;
            case HUNTER:
                tile.takeItem();
                if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.POWER_UP).play(0.1f);
                SCREEN.activateBuff(Tile.Item.HUNTER);
                for(Enemy ghost : SCREEN.getGhosts()){
                    ghost.setDifficulty(Enemy.Difficulty.RUNAWAY);
                }
                break;
            case SLO_MO:
                tile.takeItem();
                if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.SLO_MO).play(0.1f);
                SCREEN.activateBuff(Tile.Item.SLO_MO);
                break;
            case TIME:
                tile.takeItem();
                if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.TIME).play(0.1f);
                SCREEN.activateBuff(Tile.Item.TIME);
                break;
            case LIFE:
                tile.takeItem();
                if(PrefManager.isSfxOn()) ASSETS.manager.get(ASSETS.LIFE_UP).play(0.1f);
                SCREEN.activateBuff(Tile.Item.LIFE);
                break;
        }
        super.collect(tile);
    }

    /**
     * Counts the number of items that are placed on the map already. (no dots)
     * @return number of buff items (0-4)
     */
    @Override
    public int countItems(){
        int count = 0;
        for(Vector2 position : ITEM_POSITIONS){
            if(matrix[(int)position.x][(int)position.y].isItem()) count++;
        }
        return count;
    }

    /**
     * This method looks for special Items on the map an will generate a new special item in a free slot.
     * The selection of which item to generate will be decided via an algorithm which is loosely based on the golden ratio.
     * if a certain item already exists the likelihood of it spawning will decrease further.
     */
    public void generateSpecialItem(){
        ArrayList<Tile.Item> existingItems = new ArrayList<>();

        //initializes two lists: itemList = all possible Items; existingItems= all existing items on the map
        Tile.Item[] tmpList = Tile.Item.values();
        ArrayList<Tile.Item> itemList = new ArrayList<>();
        for(int i = 2; i<Tile.Item.values().length; i++){
            itemList.add(tmpList[i]);
            existingItems.add(Tile.Item.EMPTY);
        }

        //Looks for Items which are already on the map and adds them to the list
        for (Vector2 pos: ITEM_POSITIONS) {
            if(matrix[(int) pos.x][(int) pos.y].isItem()){
                Tile.Item exists = matrix[(int) pos.x][(int) pos.y].getItem();
                int index = itemList.indexOf(exists);
                existingItems.add(index, exists);
            }
        }

        // Creates a new special Item on the Map
        boolean created = false;
        for (Vector2 pos: ITEM_POSITIONS) {
            if(!matrix[(int) pos.x][(int) pos.y].isItem() && !created){
                Tile.Item newItem = newItem(existingItems);
                layerCollect.setCell((int) pos.x, (int) pos.y, createItem(newItem));
                matrix[(int) pos.x][(int) pos.y].placeItem(newItem);
                created = true;
            }
        }
    }

}
