package uas.lntv.pacmangame;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Assets {

    public final AssetManager manager = new AssetManager();

    public final AssetDescriptor<Music> GAME_MUSIC = new AssetDescriptor<>("music/GameMusic.mp3", Music.class);
    public final AssetDescriptor<Music> GAME_MUSIC_2 = new AssetDescriptor<>("music/GameMusic2.mp3", Music.class);
    public final AssetDescriptor<Music> GAME_MUSIC_3 = new AssetDescriptor<>("music/GameMusic3.mp3", Music.class);
    public final AssetDescriptor<Music> GAME_MUSIC_4 = new AssetDescriptor<>("music/GameMusic4.mp3", Music.class);
    public final AssetDescriptor<Music> SCORE_MUSIC = new AssetDescriptor<>("music/HeartOfCourage.mp3", Music.class);
    public final AssetDescriptor<Music> HUNTING_MUSIC = new AssetDescriptor<>("music/hunting.mp3", Music.class);
    public final AssetDescriptor<Music> MENU_MUSIC = new AssetDescriptor<>("music/MenuMusic.MP3", Music.class);

    public final AssetDescriptor<Sound> ALARM = new AssetDescriptor<>("sounds/ALARM.mp3", Sound.class);
    public final AssetDescriptor<Sound> DIE = new AssetDescriptor<>("sounds/die.wav", Sound.class);
    public final AssetDescriptor<Sound> DOT = new AssetDescriptor<>("sounds/dot.wav", Sound.class);
    public final AssetDescriptor<Sound> KILL = new AssetDescriptor<>("sounds/kill.wav", Sound.class);
    public final AssetDescriptor<Sound> POWER_UP = new AssetDescriptor<>("sounds/PowerUp.wav", Sound.class);

    public final AssetDescriptor<Texture> PAC_MAN = new AssetDescriptor<>("textures/PacMan32.png", Texture.class);
    public final AssetDescriptor<Texture> SUPER_PAC = new AssetDescriptor<>("textures/SuperPacMan.png", Texture.class);
    public final AssetDescriptor<Texture> DEATH_PAC = new AssetDescriptor<>("textures/PacManDeath.png", Texture.class);
    public final AssetDescriptor<Texture> GHOST_1 = new AssetDescriptor<>("textures/red.png", Texture.class);
    public final AssetDescriptor<Texture> GHOST_2 = new AssetDescriptor<>("textures/orange.png", Texture.class);
    public final AssetDescriptor<Texture> GHOST_3 = new AssetDescriptor<>("textures/pink.png", Texture.class);
    public final AssetDescriptor<Texture> WHITE_DEAD = new AssetDescriptor<>("textures/white.png", Texture.class);
    public final AssetDescriptor<Texture> BLUE_DEAD = new AssetDescriptor<>("textures/blue.png", Texture.class);
    public final AssetDescriptor<Texture> TILES = new AssetDescriptor<>("maps/tiles.png", Texture.class);
    public final AssetDescriptor<Texture> COIN_GOLD = new AssetDescriptor<>("textures/CoinGold.png", Texture.class);


    public final String MAP_1 = "maps/map.tmx";
    public final String MAP_2 = "maps/map2.tmx";
    public final String MAP_3 = "maps/map3.tmx";
    public final String MAP_4 = "maps/map4.tmx";
    public final String MAP_5 = "maps/map5.tmx";
    public final String MENU_MAP = "maps/MainMenu.tmx";
    public final String SCORE_MAP = "maps/HighScoreList.tmx";
    public final String CONTROL = "maps/controller.tmx";

    public void load(){
        manager.load(GAME_MUSIC);
        manager.load(GAME_MUSIC_2);
        manager.load(GAME_MUSIC_3);
        manager.load(GAME_MUSIC_4);
        manager.load(SCORE_MUSIC);
        manager.load(HUNTING_MUSIC);
        manager.load(MENU_MUSIC);

        manager.load(ALARM);
        manager.load(DIE);
        manager.load(DOT);
        manager.load(KILL);
        manager.load(POWER_UP);

        manager.load(PAC_MAN);
        manager.load(SUPER_PAC);
        manager.load(DEATH_PAC);
        manager.load(GHOST_1);
        manager.load(GHOST_2);
        manager.load(GHOST_3);
        manager.load(WHITE_DEAD);
        manager.load(BLUE_DEAD);
        manager.load(TILES);
        manager.load(COIN_GOLD);

        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(CONTROL, TiledMap.class);
    }

    public void dispose(){ manager.dispose(); }
}
