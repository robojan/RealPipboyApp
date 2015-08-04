package nl.robojan.real_pipboy.FalloutFont;

import com.badlogic.gdx.utils.LittleEndianInputStream;

import java.io.IOException;

//import nl.robojan.real_pipboy.util.EndianInputStream;

/**
 * Created by s120330 on 4-7-2015.
 */
public class FalloutFontChar {

    public float constant0;
    public float x1, y1, x2, y2, x3, y3, x4, y4;
    public float charViewWidth;
    public float charViewHeight;
    public float leftSpace;
    public float rightSpace;
    public float bottomAlign;

    public FalloutFontChar()
    {
        constant0 = 0;
        x1 = x2 = x3 = x4 = y1 = y2 = y3 = y4 = 0;
        charViewHeight = 0;
        charViewWidth = 0;
        leftSpace = 0;
        rightSpace = 0;
        bottomAlign = 0;
    }

    public FalloutFontChar(LittleEndianInputStream is) throws IOException
    {
        load(is);
    }

    public void load(LittleEndianInputStream is) throws IOException
    {
        constant0 = is.readFloat();
        x1 = is.readFloat();
        y1 = is.readFloat();
        x2 = is.readFloat();
        y2 = is.readFloat();
        x3 = is.readFloat();
        y3 = is.readFloat();
        x4 = is.readFloat();
        y4 = is.readFloat();
        charViewWidth = is.readFloat();
        charViewHeight = is.readFloat();
        leftSpace = is.readFloat();
        rightSpace = is.readFloat();
        bottomAlign = is.readFloat();
    }

    public int getSize()
    {
        return 4*14;
    }
}
