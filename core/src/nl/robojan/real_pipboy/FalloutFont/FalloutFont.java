package nl.robojan.real_pipboy.FalloutFont;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;


/**
 * Created by s120330 on 4-7-2015.
 */
public class FalloutFont implements Disposable {

    private FalloutFontData mData;

    private float mScaleX = 1;
    private float mScaleY = 1;

    private Color mColor = new Color(1,1,1,1);

    @Override
    public void dispose() {
        if(mData != null) mData.dispose();
    }

    public FalloutFont(FileHandle fontFile)
    {
        mData = new FalloutFontData(fontFile);
    }

    public FalloutGlyphLayout draw(Batch batch, java.lang.CharSequence str, float x, float y)
    {
        return draw(batch, str, x, y, 0, str.length(), 0, Align.left, false);
    }


    public FalloutGlyphLayout draw(Batch batch, java.lang.CharSequence str, float x, float y,
                            float targetWidth, int halign, boolean wrap)
    {
        return draw(batch, str, x, y, 0, str.length(), targetWidth, halign, wrap);
    }

    public FalloutGlyphLayout draw(Batch batch, java.lang.CharSequence str, float x, float y, int start,
                            int end, float targetWidth, int halign, boolean wrap)
    {
        FalloutGlyphLayout layout = new FalloutGlyphLayout(mData, str, start, end, targetWidth,
                halign, wrap, mScaleX, mScaleY);

        draw(batch, layout, x, y);
        return layout;
    }

    public void draw(Batch batch, FalloutGlyphLayout layout, float x, float y)
    {
        float startX = x;

        y -= getCapHeight();
        for(FalloutGlyphLayout.GlyphRun run : layout.runs)
        {
            int n = run.indices.size;
            x += run.x;

            for(int i = 0; i < n; i++)
            {
                int index = run.indices.get(i);
                float xAdvance = run.xAdvances.get(i);
                FalloutFontChar info = mData.getGlyphInfo(index);
                if(info.charViewWidth != 0 && info.charViewHeight != 0) {
                    TextureRegion region = mData.getRegion(index);

                    float posX = x + info.leftSpace*mScaleX;
                    float posY = y - run.y + (info.bottomAlign - info.charViewHeight)*mScaleY;

                    batch.draw(region, posX, posY, info.charViewWidth * mScaleX,
                            info.charViewHeight * mScaleY);
                }
                x += xAdvance;
            }
            x = startX;
        }
    }

    public float getAscent()
    {
        return mData.getAscent() * mScaleY;
    }

    public float getCapHeight()
    {
        return mData.getCapHeight() * mScaleY;
    }

    public Color getColor()
    {
        return mColor;
    }

    public float getDescent()
    {
        return mData.getDescent() * mScaleY;
    }

    public float getLineHeight()
    {
        return mData.getLineHeight() *mScaleY;
    }

    public TextureRegion getRegion(char c)
    {
        return mData.getRegion(c);
    }

    public Array<TextureRegion> getRegions()
    {
        return mData.getRegions();
    }

    public float getScaleX()
    {
        return mScaleX;
    }

    public float getScaleY()
    {
        return mScaleY;
    }

    public float getSpaceWidth() {
        return mData.getSpaceWidth() * mScaleX;
    }

    public float getXHeight()
    {
        return mData.getXHeight() * mScaleY;
    }

    public boolean isFlipped()
    {
        return false;
    }

    public void setColor(Color color)
    {
        mColor = color;
    }

    public void setColor(float r, float g, float b, float a)
    {
        setColor(new Color(r, g, b,a));
    }

    public void setScaleX(float scale) {
        mScaleX = scale;
    }

    public void setScaleY(float scale) {
        mScaleY = scale;
    }

    public void setScale(float scale) {
        mScaleX = scale;
        mScaleY = scale;
    }

    public void setScale(float scaleX, float scaleY) {
        mScaleX = scaleX;
        mScaleY = scaleY;
    }

    public FalloutFontData getData() {
        return mData;
    }
}

