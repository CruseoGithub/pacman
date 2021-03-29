package uas.lntv.pacmangame.Maps;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import uas.lntv.pacmangame.Sprites.Actor;
import uas.lntv.pacmangame.Sprites.Enemy;
import uas.lntv.pacmangame.Sprites.PacMan;

/**
 * The whole map is divided in several tiles, these tiles are defined here.
 * They contain coordinates, the information of which item is placed on it and also all the
 * information that is needed by the Pathfinder to calculate the shortest path via A*.
 * @see uas.lntv.pacmangame.AI.Pathfinder
 * @see Map
 */
public class Tile extends StaticTiledMapTile {

    /* Map-Specific Fields */

    public enum Item { EMPTY, DOT, TIME, SLO_MO, HUNTER, LIFE}
    public enum Type { EMPTY, PATH, WALL, DOT}

    private boolean occupiedByPacMan = false;
    private boolean occupiedByGhost = false;
    private final int x,y;
    private final Tile.Type TYPE;
    private Tile.Item item;

    /* A*-Specific Fields */

    private double heuristics;  // Length of direct connection between this tile and the aimed tile
    private double total;       // = heuristics + cost
    private int cost;           // No. of steps from the starting point
    private Tile prev;          // Needed to find the shortest connection between start and goal


    /* Constructors */

    /**
     * Creates a tile without a texture or special type.
     * This would certainly be a raw tile, that will be changed soon.
     */
    public Tile(){
        super(new TextureRegion());
        this.TYPE = null;
        this.prev = null;
        this.item = Item.EMPTY;
        this.x = 0;
        this.y = 0;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

    /**
     * Creates a tile with a texture region, that still needs to be put in place and needs
     * to be characterized.
     * @param textureRegion TILE_SIZE x TILE_SIZE pixels region of a texture
     */
    public Tile(TextureRegion textureRegion) {
        super(textureRegion);
        this.TYPE = null;
        this.prev = null;
        this.item = Item.EMPTY;
        this.x = 0;
        this.y = 0;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

    /**
     * Creates a tile with a texture region and type and also puts it on the right place of the map.
     * @param textureRegion TILE_SIZE x TILE_SIZE pixels region of a texture
     * @param type the kind of tile
     * @param x x-position in pixels
     * @param y y-position in pixels
     */
    public Tile(TextureRegion textureRegion, Tile.Type type, int x, int y) {
        super(textureRegion);
        this.TYPE = type;
        this.item = Item.EMPTY;
        this.x = x;
        this.y = y;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

    /**
     * Puts a tile with it's attribute in the right place on the map without giving it a
     * texture.
     * @param type the kind of tile
     * @param x x-position in pixels
     * @param y y-position in pixels
     */
    public Tile(Type type, int x, int y) {
        super(new TextureRegion());
        this.TYPE = type;
        this.prev = null;
        this.item = Item.EMPTY;
        this.x = x;
        this.y = y;
        this.cost = 1000000;
        this.heuristics = 0;
        this.total = 1000000;
    }

    /* Accessors */

    /**
     * Kind of an accessor, that checks if there is an item on the tile.
     * @return true if there is an item
     */
    public boolean isItem(){
        return item != Item.EMPTY;
    }

    public boolean isOccupiedByGhost(){ return occupiedByGhost; }

    public boolean isOccupiedByPacMan(){ return occupiedByPacMan; }

    public double getHeuristics() { return heuristics; }

    public double getTotal() { return total; }

    public int getCost() { return cost; }

    public int getX() { return x; }

    public int getY() { return y; }

    public Item getItem(){ return this.item;}

    public Tile getPrev() { return prev; }

    public Type getType() { return TYPE; }

    /* Mutators */

    public Tile setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public Tile setTotal(double total) {
        this.total = total;
        return this;
    }

    public Tile setPrev(Tile prev) {
        this.prev = prev;
        return this;
    }

    /**
     * Determines which kind of Actor occupies this tile.
     * @param actor PacMan or Enemy
     */
    public void enter(Actor actor){
        if(actor instanceof PacMan) occupiedByPacMan = true;
        if(actor instanceof Enemy) occupiedByGhost = true;
    }

    /**
     * Determines which kind of Actor doesn't occupy this tile anymore.
     * @param actor PacMan or Enemy
     */
    public void leave(Actor actor){
        if(actor instanceof PacMan) occupiedByPacMan = false;
        if(actor instanceof Enemy) occupiedByGhost = false;
    }

    /**
     * Tell the tile which kind of item will be put on it.
     * @param item The kind of item that will be placed.
     */
    public void placeItem(Tile.Item item){ this.item = item; }

    public void setHeuristics(double heuristics) { this.heuristics = heuristics; }

    /**
     * Takes the item that was placed on this tile. The tile will now be empty.
     */
    public void takeItem(){ this.item = Item.EMPTY; }

}