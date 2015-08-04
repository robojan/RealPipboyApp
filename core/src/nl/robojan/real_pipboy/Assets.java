package nl.robojan.real_pipboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.HashMap;

import nl.robojan.real_pipboy.FalloutFont.FalloutFont;
import nl.robojan.real_pipboy.util.GameFileResolver;
import nl.robojan.real_pipboy.util.ShaderProgramLoader;
import nl.robojan.real_pipboy.util.StringUtil;
import nl.robojan.real_pipboy.util.TextFileLoader;

/**
 * Created by s120330 on 7-7-2015.
 */
public class Assets {
    private static GameFileResolver mResolver = new GameFileResolver(true);
    public static AssetManager manager = new AssetManager(mResolver);
    private static FalloutFont[] mFonts;

    public static void init() {
        manager.setLoader(String.class, new TextFileLoader(mResolver));
        manager.setLoader(ShaderProgram.class, new ShaderProgramLoader(mResolver));
        loadFonts();
    }

    public static void loadFonts()
    {
        mFonts = new FalloutFont[8];
        mFonts[0] = new FalloutFont(Gdx.files.internal("textures/fonts/glow_monofonto_large.fnt"));
        mFonts[1] = new FalloutFont(Gdx.files.internal("textures/fonts/monofonto_large.fnt"));
        mFonts[2] = new FalloutFont(Gdx.files.internal("textures/fonts/glow_monofonto_medium.fnt"));
        mFonts[3] = new FalloutFont(Gdx.files.internal("textures/fonts/monofonto_verylarge02_dialogs2.fnt"));
        mFonts[4] = new FalloutFont(Gdx.files.internal("textures/fonts/fixedsys_comp_uniform_width.fnt"));
        mFonts[5] = new FalloutFont(Gdx.files.internal("textures/fonts/glow_monofonto_vl_dialogs.fnt"));
        mFonts[6] = new FalloutFont(Gdx.files.internal("textures/fonts/baked_in_monofonto_large.fnt"));
        mFonts[7] = new FalloutFont(Gdx.files.internal("textures/fonts/glow_futura_caps_large.fnt"));
    }

    public static FalloutFont getFont(int index)
    {
        if(index < 1 || index >= mFonts.length)
            throw new IndexOutOfBoundsException("Font index is not valid");
        FalloutFont font = mFonts[index -1 ];
        return font;
    }

    public static boolean update(){
        return manager.update();
    }

}
