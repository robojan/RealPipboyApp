package nl.robojan.real_pipboy.FalloutFont;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.StreamUtils;

//import nl.robojan.real_pipboy.util.EndianInputStream;

/**
 * Created by s120330 on 5-7-2015.
 */
public class FalloutFontData implements Disposable{

    private FalloutFontHeader mFileHeader;
    private FalloutFontChar[] mChars;

    private float mAscent = 0;
    private float mCapHeight = 0;
    private float mDescent = 0;
    private float mXHeight = 0;

    private Texture mTexture;
    private TextureRegion[] mRegions;

    public char[] xChars = {'x', 'e', 'a', 'o', 'n', 's', 'r', 'c', 'u', 'm', 'v', 'w', 'z'};
    public char[] capChars = {'M', 'N', 'B', 'D', 'C', 'E', 'F', 'K', 'A', 'G', 'H', 'I', 'J',
            'L', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    @Override
    public void dispose() {
        if(mTexture != null)
            mTexture.dispose();

    }

    public FalloutFontData(FileHandle fontFile)
    {
        readFontFile(fontFile);
    }

    private void readFontFile(FileHandle fontFile)
    {
        LittleEndianInputStream is = new LittleEndianInputStream(fontFile.read());
        try {
            // Read the font definition file
            mFileHeader = new FalloutFontHeader(is);
            // Load the corresponding texture
            FileHandle imageFile = fontFile.parent().child(mFileHeader.imageFileName + ".png");
            mTexture = new Texture(imageFile);

            int numChars = 256;
            // Load all the characters
            mChars = new FalloutFontChar[numChars];
            mRegions = new TextureRegion[numChars];

            float maxHeight = 0;

            for(int i = 0; i < numChars; i++)
            {
                FalloutFontChar info = new FalloutFontChar(is);
                mChars[i] = info;
                mRegions[i] = new TextureRegion(mTexture, info.x1, info.y1, info.x4, info.y4);
                maxHeight = Math.max(maxHeight, info.bottomAlign);
                mDescent = Math.max(mDescent, info.charViewHeight - info.bottomAlign);
            }

            // Calculate The cap height
            FalloutFontChar capChar = null;
            for(int i = 0; i < capChars.length; i++)
            {
                capChar = mChars[capChars[i]];
                if(capChar != null)
                    break;
            }
            if(capChar == null)
            {
                for(int i = 0; i < 256; i++)
                {
                    if(mChars[i] == null || mChars[i].charViewHeight == 0 ||
                            mChars[i].charViewWidth == 0)
                        continue;
                    mCapHeight = Math.max(mCapHeight, mChars[i].bottomAlign);
                }
            } else {
                mCapHeight = capChar.bottomAlign;
            }

            // Calculate The x char height
            FalloutFontChar xChar = null;
            for(int i = 0; i < xChars.length; i++)
            {
                xChar = mChars[xChars[i]];
                if(xChar != null)
                    break;
            }
            if(xChar == null)
            {
                for(int i = 0; i < 256; i++)
                {
                    if(mChars[i] != null && mChars[i].charViewHeight != 0 &&
                            mChars[i].charViewWidth != 0) {
                        mXHeight = mChars[i].bottomAlign;
                    }
                }
            } else {
                mXHeight = xChar.bottomAlign;
            }

            mAscent = maxHeight - mCapHeight;

        } catch (Exception ex) {
            throw new GdxRuntimeException("Error loading font file: " + fontFile, ex);
        } finally {
            StreamUtils.closeQuietly(is);
        }
    }


    public float getAscent()
    {
        return mAscent;
    }

    public float getCapHeight()
    {
        return mCapHeight;
    }


    public float getDescent()
    {
        return mDescent;
    }

    public float getLineHeight()
    {
        return mFileHeader.lineHeight;
    }

    public TextureRegion getRegion(int c)
    {
        return mRegions[c];
    }

    public Array<TextureRegion> getRegions()
    {
        return new Array<TextureRegion>(mRegions);
    }

    public float getSpaceWidth() {
        FalloutFontChar space = mChars[' '];
        return (space.charViewWidth + space.leftSpace + space.rightSpace);
    }

    public float getXHeight()
    {
        return mXHeight;
    }

    public FalloutFontChar getGlyphInfo(int index)
    {
        if(index > mChars.length) throw new ArrayIndexOutOfBoundsException();
        return mChars[index];
    }

    public Texture getTexture()
    {
        return mTexture;
    }
}
