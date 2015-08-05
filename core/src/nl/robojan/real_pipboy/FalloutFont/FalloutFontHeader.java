package nl.robojan.real_pipboy.FalloutFont;

import com.badlogic.gdx.utils.LittleEndianInputStream;

import java.io.IOException;

//import nl.robojan.real_pipboy.util.EndianInputStream;

/**
 * Created by s120330 on 4-7-2015.
 */
public class FalloutFontHeader {

    private static final int IMAGEFILENAME_LENGTH = 0x11c;

    public float lineHeight;
    public int constant1;
    public int constant2;
    public String imageFileName;

    public FalloutFontHeader(LittleEndianInputStream is) throws IOException {
        lineHeight = is.readFloat();
        constant1 = is.readInt();
        constant2 = is.readInt();
        byte[] imageFileNameBytes = new byte[IMAGEFILENAME_LENGTH];
        is.readFully(imageFileNameBytes, 0, IMAGEFILENAME_LENGTH );
        int len;
        //noinspection StatementWithEmptyBody
        for(len = 0; len < imageFileNameBytes.length && imageFileNameBytes[len] != 0; len++);
        imageFileName = new String(imageFileNameBytes, 0, len);
    }

    public int getSize()
    {
        return 4*4+IMAGEFILENAME_LENGTH;
    }
}
