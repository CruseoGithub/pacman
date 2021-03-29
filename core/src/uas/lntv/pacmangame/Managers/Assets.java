package uas.lntv.pacmangame.Managers;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * !WARNING!
 * Assets are NOT supposed to run static! This can cause problems on Android devices!
 * This class is used to manage the assets. It loads all needed assets once in the beginning and
 * disposes them when the application is closed.
 *
 * File-names are supposed to be written in lowercase letters and separated with underscores to work
 * properly on most android devices.
 */
public class Assets {

    /* Fields */

    public final AssetManager manager = new AssetManager();

    //SETUP
    public final AssetDescriptor<Sound> DIAL_UP = new AssetDescriptor<>("setup/dial_up.mp3", Sound.class);
    public final AssetDescriptor<Texture> LOADING = new AssetDescriptor<>("setup/loading_pac_man_256.png", Texture.class);

    //MUSIC
    public final AssetDescriptor<Music> GAME_MUSIC = new AssetDescriptor<>("music/game_music_1.mp3", Music.class);
    public final AssetDescriptor<Music> GAME_MUSIC_2 = new AssetDescriptor<>("music/game_music_2.mp3", Music.class);
    public final AssetDescriptor<Music> GAME_MUSIC_3 = new AssetDescriptor<>("music/game_music_3.mp3", Music.class);
    public final AssetDescriptor<Music> GAME_MUSIC_4 = new AssetDescriptor<>("music/game_music_4.mp3", Music.class);
    public final AssetDescriptor<Music> SCORE_MUSIC = new AssetDescriptor<>("music/heart_of_courage.mp3", Music.class);
    public final AssetDescriptor<Music> HUNTING_MUSIC = new AssetDescriptor<>("music/hunting_music.mp3", Music.class);
    public final AssetDescriptor<Music> MENU_MUSIC = new AssetDescriptor<>("music/menu_music.mp3", Music.class);
    public final AssetDescriptor<Music> SETTINGS_MUSIC = new AssetDescriptor<>("music/settings_music.mp3", Music.class);

    //SOUNDS
    public final AssetDescriptor<Sound> ALARM = new AssetDescriptor<>("sounds/alarm.mp3", Sound.class);
    public final AssetDescriptor<Sound> DIE = new AssetDescriptor<>("sounds/die.wav", Sound.class);
    public final AssetDescriptor<Sound> DOT = new AssetDescriptor<>("sounds/dot.wav", Sound.class);
    public final AssetDescriptor<Sound> KILL = new AssetDescriptor<>("sounds/kill.wav", Sound.class);
    public final AssetDescriptor<Sound> LIFE_UP = new AssetDescriptor<>("sounds/life_up.wav", Sound.class);
    public final AssetDescriptor<Sound> POWER_UP = new AssetDescriptor<>("sounds/power_up.wav", Sound.class);
    public final AssetDescriptor<Sound> SLO_MO = new AssetDescriptor<>("sounds/slo_mo.wav", Sound.class);
    public final AssetDescriptor<Sound> TIME = new AssetDescriptor<>("sounds/time.wav", Sound.class);

    //TEXTURES
    public final AssetDescriptor<Texture> BLUE_DEAD = new AssetDescriptor<>("textures/blue.png", Texture.class);
    public final AssetDescriptor<Texture> COIN_GOLD = new AssetDescriptor<>("textures/coin_gold.png", Texture.class);
    public final AssetDescriptor<Texture> DEATH_PAC = new AssetDescriptor<>("textures/pac_man_death.png", Texture.class);
    public final AssetDescriptor<Texture> GHOST_1 = new AssetDescriptor<>("textures/red.png", Texture.class);
    public final AssetDescriptor<Texture> GHOST_2 = new AssetDescriptor<>("textures/orange.png", Texture.class);
    public final AssetDescriptor<Texture> GHOST_3 = new AssetDescriptor<>("textures/pink.png", Texture.class);
    public final AssetDescriptor<Texture> ITEM_HUNTER = new AssetDescriptor<>("maps/cherry.png", Texture.class);
    public final AssetDescriptor<Texture> ITEM_LIFE = new AssetDescriptor<>("textures/item_life.png", Texture.class);
    public final AssetDescriptor<Texture> ITEM_TIME = new AssetDescriptor<>("textures/item_time.png", Texture.class);
    public final AssetDescriptor<Texture> ITEM_SLO_MO = new AssetDescriptor<>("textures/item_running.png", Texture.class);
    public final AssetDescriptor<Texture> JOYSTICK_KNOB = new AssetDescriptor<>("textures/joystick_knob.png", Texture.class);
    public final AssetDescriptor<Texture> JOYSTICK_ZONE = new AssetDescriptor<>("textures/joystick_zone.png", Texture.class);
    public final AssetDescriptor<Texture> PAC_MAN = new AssetDescriptor<>("textures/pac_man_32.png", Texture.class);
    public final AssetDescriptor<Texture> SUPER_PAC = new AssetDescriptor<>("textures/super_pac_man.png", Texture.class);
    public final AssetDescriptor<Texture> TILES = new AssetDescriptor<>("maps/tiles.png", Texture.class);
    public final AssetDescriptor<Texture> WHITE_DEAD = new AssetDescriptor<>("textures/white.png", Texture.class);

    //PATHS OF THE MAPS
    public final String CONTROL = "maps/controller.tmx";
    public final String MAP_1 = "maps/map_1.tmx";
    public final String MAP_2 = "maps/map_2.tmx";
    public final String MAP_3 = "maps/map_3.tmx";
    public final String MAP_4 = "maps/map_4.tmx";
    public final String MAP_5 = "maps/map_5.tmx";
    public final String MENU_MAP = "maps/main_menu.tmx";
    public final String PAUSE = "maps/pause_map.tmx";
    public final String SCORE_MAP = "maps/high_score_list.tmx";
    public final String SETTINGS_MAP = "maps/settings_map.tmx";
    public final String SPLASH = "maps/splash.tmx";

    /* Methods */

    /**
     * Disposes everything, that has been loaded by this manager before.
     */
    public void dispose(){ manager.dispose(); }

    /**
     * This method puts everything in the loading queue, that is needed in the application.
     */
    public void load(){
        manager.load(ALARM);
        manager.load(DIE);
        manager.load(DOT);
        manager.load(KILL);
        manager.load(LIFE_UP);
        manager.load(POWER_UP);
        manager.load(SLO_MO);
        manager.load(TIME);

        manager.load(GAME_MUSIC);
        manager.load(GAME_MUSIC_2);
        manager.load(GAME_MUSIC_3);
        manager.load(GAME_MUSIC_4);
        manager.load(SCORE_MUSIC);
        manager.load(HUNTING_MUSIC);
        manager.load(MENU_MUSIC);
        manager.load(SETTINGS_MUSIC);

        manager.load(DEATH_PAC);
        manager.load(BLUE_DEAD);
        manager.load(COIN_GOLD);
        manager.load(GHOST_1);
        manager.load(GHOST_2);
        manager.load(GHOST_3);
        manager.load(ITEM_HUNTER);
        manager.load(ITEM_LIFE);
        manager.load(ITEM_SLO_MO);
        manager.load(ITEM_TIME);
        manager.load(JOYSTICK_KNOB);
        manager.load(JOYSTICK_ZONE);
        manager.load(PAC_MAN);
        manager.load(SUPER_PAC);
        manager.load(TILES);
        manager.load(WHITE_DEAD);

        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(CONTROL, TiledMap.class);
    }

    /**
     * Small method, that puts everything in the loading queue, that is needed for the splash- and
     * loading screen. The method will also block everything until loading is finished.
     */
    public void loadSetup(){
        manager.load(DIAL_UP);
        manager.load(LOADING);
        manager.finishLoading();
    }

}
