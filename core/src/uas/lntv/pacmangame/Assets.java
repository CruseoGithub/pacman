package uas.lntv.pacmangame;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Assets {

    public final AssetManager manager = new AssetManager();

    public final AssetDescriptor<Music> GAME_MUSIC = new AssetDescriptor<>("GameMusic.mp3", Music.class);
    public final AssetDescriptor<Music> GAME_MUSIC_2 = new AssetDescriptor<>("GameMusic2.mp3", Music.class);
    public final AssetDescriptor<Music> GAME_MUSIC_3 = new AssetDescriptor<>("GameMusic3.mp3", Music.class);
    public final AssetDescriptor<Music> GAME_MUSIC_4 = new AssetDescriptor<>("GameMusic4.mp3", Music.class);
    public final AssetDescriptor<Music> SCORE_MUSIC = new AssetDescriptor<>("HeartOfCourage.mp3", Music.class);
    public final AssetDescriptor<Music> HUNTING_MUSIC = new AssetDescriptor<>("hunting.mp3", Music.class);
    public final AssetDescriptor<Music> MENU_MUSIC = new AssetDescriptor<>("MenuMusic.MP3", Music.class);

    public final AssetDescriptor<Sound> ALARM = new AssetDescriptor<>("ALARM.mp3", Sound.class);
    public final AssetDescriptor<Sound> DIE = new AssetDescriptor<>("die.wav", Sound.class);
    public final AssetDescriptor<Sound> DOT = new AssetDescriptor<>("dot.wav", Sound.class);
    public final AssetDescriptor<Sound> KILL = new AssetDescriptor<>("kill.wav", Sound.class);
    public final AssetDescriptor<Sound> POWER_UP = new AssetDescriptor<>("PowerUp.wav", Sound.class);

/*
    public final AssetDescriptor<TiledMap> MAP_1 = new AssetDescriptor<>("map.tmx", TiledMap.class);
    public final AssetDescriptor<TiledMap> MAP_2 = new AssetDescriptor<>("map2.tmx", TiledMap.class);
    public final AssetDescriptor<TiledMap> MAP_3 = new AssetDescriptor<>("map3.tmx", TiledMap.class);
    public final AssetDescriptor<TiledMap> MAP_4 = new AssetDescriptor<>("map4.tmx", TiledMap.class);
    public final AssetDescriptor<TiledMap> MAP_5 = new AssetDescriptor<>("map5.tmx", TiledMap.class);
    public final AssetDescriptor<TiledMap> MENU_MAP = new AssetDescriptor<>("MainMenu.tmx", TiledMap.class);
    public final AssetDescriptor<TiledMap> SCORE_MAP = new AssetDescriptor<>("HighScoreList.tmx", TiledMap.class);
    public final AssetDescriptor<TiledMap> CONTROL = new AssetDescriptor<>("controller.tmx", TiledMap.class);
*/

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

/*
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(MAP_1);
        manager.load(MAP_2);
        manager.load(MAP_3);
        manager.load(MAP_4);
        manager.load(MAP_5);
        manager.load(MENU_MAP);
        manager.load(SCORE_MAP);
        manager.load(CONTROL);
*/
    }

    public void dispose(){ manager.dispose(); }
}
