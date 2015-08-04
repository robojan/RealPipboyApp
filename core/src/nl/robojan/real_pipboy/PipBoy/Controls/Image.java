package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import java.util.Objects;

import nl.robojan.real_pipboy.Assets;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.RenderContext;

/**
 * Created by s120330 on 8-7-2015.
 */
public class Image extends Control {

    private float mWidth = -1, mHeight =-1;
    private String mFile;
    private Color mColor = new Color(1,1,1,1);
    private boolean mTileH = false, mTileV = false;
    private boolean mVisible = true;

    private Texture mTexture = null;

    public Image(float x, float y, String file) {
        super(x, y);
        mFile = file;
        load();
    }

    public Image(float x, float y, String file, Color color) {
        super(x, y);
        mFile = file;
        mColor = color;
        load();
    }

    public Image(float x, float y, String file, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mFile = file;
    }

    public Image(float x, float y, String file, float width, float height, Color color) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        mFile = file;
        mColor = color;
    }

    @Override
    public void load() {
        super.load();
        if(mFile == null)
            return;
        Assets.manager.load(mFile, Texture.class);
        if(mWidth == -1 && mHeight == -1)
        {
            Assets.manager.finishLoadingAsset(mFile);
            mTexture = Assets.manager.get(mFile);
            mWidth = mTexture.getWidth();
            mHeight = mTexture.getHeight();
        }
    }

    @Override
    public void render(RenderContext context) {
        if(mTexture == null)
        {
            if(mFile == null || !Assets.manager.isLoaded(mFile)){
                super.render(context);
                return;
            }
            mTexture = Assets.manager.get(mFile);
        }

        if(mVisible) {
            context.batch.setColor(mColor);
            //mTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            context.batch.draw(mTexture, 0, Constants.PIPBOY_HEIGHT - mHeight, mWidth, mHeight,
                    0, 1, mTileH ? mWidth/mTexture.getWidth() : 1,
                     1 - (mTileV ? mHeight/mTexture.getHeight() : 1));
        }
/*
        if(mVisible) {
            context.batch.setColor(mColor);
            if (!mTileH && !mTileV) {
                context.batch.draw(mTexture, mX, Constants.PIPBOY_HEIGHT - mY - mHeight,
                        mWidth, mHeight);
            } else if (mTileH && !mTileV) {
                for (float x = mX; x < mX + mWidth; x += mTexture.getWidth()) {
                    if (x + mTexture.getWidth() > mX + mWidth) {
                        float width = mWidth - x - mX;
                        context.batch.draw(mTexture, x, Constants.PIPBOY_HEIGHT - mY - mHeight,
                                width, mHeight, 0, 0, (int) width, mTexture.getHeight(), false, false);
                    } else {
                        context.batch.draw(mTexture, x, Constants.PIPBOY_HEIGHT - mY - mHeight,
                                mTexture.getWidth(), mHeight);
                    }
                }
            } else if (!mTileH && mTileV) {
                for (float y = 0; y < mHeight; y += mTexture.getHeight()) {
                    if (y + mTexture.getHeight() > mHeight) {
                        float height = mHeight - y;
                        context.batch.draw(mTexture, mX, Constants.PIPBOY_HEIGHT - mY - y,
                                mTexture.getWidth(), height, 0, 0, (int) mTexture.getWidth(),
                                (int) height, false, false);
                    } else {
                        context.batch.draw(mTexture, mX, Constants.PIPBOY_HEIGHT - mY - y,
                                mWidth, mTexture.getHeight());
                    }
                }
            } else if (mTileH && mTileV) {
                for (float y = 0; y < mHeight; y += mTexture.getHeight()) {
                    for (float x = 0; x < mWidth; x += mTexture.getWidth()) {
                        int width = mTexture.getWidth();
                        int height = mTexture.getHeight();
                        if (x + mTexture.getWidth() > mWidth) {
                            width = (int) (mWidth - x);
                        }
                        if (y + mTexture.getHeight() > mHeight) {
                            height = (int) (mHeight - y);
                        }
                        context.batch.draw(mTexture, mX + x, Constants.PIPBOY_HEIGHT - mY - y,
                                width, height, 0, 0, (int) width, height, false, false);
                    }
                }
            }
        }*/

        super.render(context);

    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight) ;
    }

    @Override
    public void dispose() {
        super.dispose();
        if(mFile != null && isLoaded() && Assets.manager.isLoaded(mFile))
            Assets.manager.unload(mFile);
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color color) {
        this.mColor = color;
    }

    public float getWidth() {
        return mWidth;
    }

    public void setWidth(float width) {
        this.mWidth = width;
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(float height) {
        this.mHeight = height;
    }

    public String getFile() {
        return mFile;
    }

    public void setFile(String file) {
        if(Objects.equals(mFile, file))
            return;
        if(mFile != null && isLoaded() && Assets.manager.isLoaded(mFile) ){
            Assets.manager.unload(mFile);
        }
        if(mTexture != null) {
            mTexture = null;
        }
        this.mFile = file;
        if(file != null) {
            Assets.manager.load(file, Texture.class);
        }
    }

    public void setTile(boolean tile) {
        mTileH = tile;
        mTileV = tile;
    }

    public void setTile(boolean tileH, boolean tileV) {
        mTileH = tileH;
        mTileV = tileV;
    }

    public boolean getTileH(){
        return mTileH;
    }

    public boolean getTileV(){
        return mTileV;
    }

    public void setVisible(boolean visible){
        mVisible = visible;
    }

    public boolean getVisible() {
        return mVisible;
    }
}
