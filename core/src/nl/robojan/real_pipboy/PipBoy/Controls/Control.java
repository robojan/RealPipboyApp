package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import nl.robojan.real_pipboy.Assets;
import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.RenderContext;

/**
 * Created by s120330 on 7-7-2015.
 */
public abstract class Control implements Disposable, InputProcessor, GestureListener {
    protected Array<Control> mChilds;
    protected float mX, mY;
    protected boolean mLoaded = false;
    protected boolean mEnabled = true;
    protected Array<ClickableListenerItem> mClickableListeners =
            new Array<ClickableListenerItem>(2);
    protected Sound mClickSound = null;
    protected String mClickSoundFile = null;
    protected Vector2 mOrigin = new Vector2();
    protected float mAngle = 0;

    protected boolean mClips = false;

    public Control(float x, float y) {
        mChilds = new Array<Control>(4);
        mX = x;
        mY = y;
        mOrigin = new Vector2(x, y);
    }

    @Override
    public void dispose() {
        for(Control child : mChilds)
        {
            child.dispose();
        }
    }

    public void load() {
        for(Control child : mChilds)
        {
            child.load();
        }
        mLoaded = true;
    }

    public boolean isLoaded() {
        return mLoaded;
    }

    public void update(Context context){
        for(Control child : mChilds)
        {
            child.update(context);
        }
    }

    private void setClippingRectangle(RenderContext context, Rectangle rect) {
        context.batch.flush();
        context.pipboyShaderProgram.begin();
        Vector3 TL = new Vector3(0, Constants.PIPBOY_HEIGHT, 0);
        Vector3 BR = new Vector3(rect.getWidth(), TL.y - rect.getHeight(), 0);
        Matrix4 projTran = new Matrix4(context.batch.getProjectionMatrix());
        projTran.mul(context.batch.getTransformMatrix());
        TL.mul(projTran).add(1, 1, 0).scl(0.5f * context.screenWidth, 0.5f * context.screenHeight, 0);
        BR.mul(projTran).add(1, 1, 0).scl(0.5f * context.screenWidth, 0.5f * context.screenHeight, 0);
        context.clippingRectangles.push(new Rectangle(TL.x, TL.y, BR.x, BR.y));
        context.pipboyShaderProgram.setUniformf("u_clippingTL", TL.x, TL.y);
        context.pipboyShaderProgram.setUniformf("u_clippingBR", BR.x, BR.y);
    }

    private void removeClippingRectangle(RenderContext context) {
        context.batch.flush();
        context.pipboyShaderProgram.begin();
        context.clippingRectangles.pop();
        if(context.clippingRectangles.size() > 0) {
            Rectangle rect = context.clippingRectangles.peek();
            Vector2 TL = new Vector2(rect.x, rect.y);
            Vector2 BR = new Vector2(rect.width, rect.height);
            context.pipboyShaderProgram.setUniformf("u_clippingTL", TL);
            context.pipboyShaderProgram.setUniformf("u_clippingBR", BR);
        } else {
            context.pipboyShaderProgram.setUniformf("u_clippingTL", 0, context.screenHeight);
            context.pipboyShaderProgram.setUniformf("u_clippingBR", context.screenWidth, 0);
        }
    }

    public void render(RenderContext context){
        for(Control child : mChilds)
        {
            Matrix4 t = context.batch.getTransformMatrix();
            Matrix4 t_saved = new Matrix4(t);
            t.translate(child.getOrigin().x, -child.getOrigin().y, 0);
            t.rotateRad(0, 0, 1, child.getAngle());
            t.translate(child.getX(),
                    -(child.getY()), 0);
            context.batch.setTransformMatrix(t);
            if(child.isClipping()) {
                setClippingRectangle(context, child.getSize());
            }
            child.render(context);
            if(child.isClipping()) {
                removeClippingRectangle(context);
            }
            context.batch.setTransformMatrix(t_saved);
        }
    }

    public void setClipping(boolean clips) {
        mClips = clips;
    }

    public boolean isClipping() {
        return mClips;
    }

    public boolean containsChild(Control child) {
        return mChilds.contains(child, true);
    }

    public void addChild(Control child, boolean doLoad) {
        addChild(child);
        if(!child.isLoaded() && doLoad)
            child.load();
    }

    public void addChild(Control child)
    {
        mChilds.add(child);
    }

    public void removeChild(Control child) {
        mChilds.removeValue(child, true);
    }

    public void clearChilds() {
        mChilds.clear();
    }

    public abstract Rectangle getSize();

    public float getHeight() {
        return getSize().getHeight();
    }

    public float getWidth() {
        return getSize().getWidth();
    }

    public float getTop() {
        return mY;
    }

    public float getBottom() {
        return mY + getHeight();
    }

    public float getLeft() {
        return mX;
    }

    public float getRight() {
        return mX + getWidth();
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        this.mY = y;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public Vector2 getPos()
    {
        return new Vector2(mX, mY);
    }

    public void setPos(Vector2 pos) {
        mX = pos.x;
        mY = pos.y;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.mEnabled = enabled;
    }

    public void setClickSound(String soundFile){
        if(mClickSoundFile != null && Assets.manager.isLoaded(mClickSoundFile)){
            Assets.manager.unload(mClickSoundFile);
        }
        mClickSoundFile = soundFile;
        if(soundFile == null){
            if(mClickSound != null)
                mClickSound.dispose();
            mClickSound = null;
            return;
        }
        Assets.manager.load(soundFile, Sound.class);
        getClickSoundFromAsset();
    }

    private boolean getClickSoundFromAsset() {
        if(mClickSound != null){
            mClickSound.dispose();
        }
        if(Assets.manager.isLoaded(mClickSoundFile)){
            mClickSound = Assets.manager.get(mClickSoundFile, Sound.class);
            return true;
        }
        return false;
    }

    public void playClickSound() {
        if(mClickSoundFile != null) {
            if(mClickSound == null){
                if(getClickSoundFromAsset()){
                    mClickSound.play();
                }
            } else {
                mClickSound.play();
            }
        }
    }

    public void addClickableListener(ClickableListener listener) {
        addClickableListener(listener, null);
    }

    public void addClickableListener(ClickableListener listener, Object user) {
        mClickableListeners.add(new ClickableListenerItem(listener, user));
    }

    public void removeClickableListener(ClickableListener listener) {
        for(int i = 0; i < mClickableListeners.size; i++) {
            if(mClickableListeners.get(i).listener == listener) {
                mClickableListeners.removeIndex(i);
            }
        }
    }

    public boolean fireClickableEvent() {
        for(ClickableListenerItem i : mClickableListeners)
        {
            i.listener.onClickableEvent(this, i.user);
        }
        return mClickableListeners.size != 0;
    }

    protected class ClickableListenerItem {
        public ClickableListener listener;
        public Object user;

        public ClickableListenerItem(ClickableListener listener, Object user) {
            this.listener = listener;
            this.user = user;
        }
    }

    public interface ClickableListener {
        void onClickableEvent(Control source, Object user);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        x -= mX;
        y -= mY;
        for(Control child : mChilds)
        {
            if(!child.getSize().contains(x, y))
                continue;
            if(child.touchDown(x, y, pointer, button))
                return true;
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        boolean fired = false;

        if(isEnabled() && getSize().contains(x, y)){
            fired = fireClickableEvent();
            playClickSound();
        }
        x -= mX;
        y -= mY;
        for(Control child : mChilds)
        {
            //if(!child.getSize().contains(x, y))
            //    continue;
            if(child.tap(x, y, count, button))
                return true;
        }
        return fired;
    }

    @Override
    public boolean longPress(float x, float y) {
        x -= mX;
        y -= mY;
        for(Control child : mChilds)
        {
            if(!child.getSize().contains(x, y))
                continue;
            if(child.longPress(x, y))
                return true;
        }
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        for(Control child : mChilds)
        {
            if(child.fling(velocityX, velocityY, button))
                return true;
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        x -= mX;
        y -= mY;
        for(Control child : mChilds)
        {
            //if(!child.getSize().contains(x, y))
            //    continue;
            if(child.pan(x, y, deltaX, deltaY))
                return true;
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        x -= mX;
        y -= mY;
        for(Control child : mChilds)
        {
            //if(!child.getSize().contains(x, y))
            //    continue;
            if(child.panStop(x, y, pointer, button))
                return true;
        }
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        for(Control child : mChilds)
        {
            if(child.zoom(initialDistance, distance))
                return true;
        }
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1,
                         Vector2 pointer2) {
        for(Control child : mChilds)
        {
            if(child.pinch(initialPointer1, initialPointer2, pointer1, pointer2))
                return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        for(Control child : mChilds)
        {
            if(child.keyDown(keycode))
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for(Control child : mChilds)
        {
            if(child.keyUp(keycode))
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        for(Control child : mChilds)
        {
            if(child.keyTyped(character))
                return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX -= mX;
        screenY -= mY;
        for(Control child : mChilds)
        {
            if(!child.getSize().contains(screenX, screenY))
                continue;
            if(child.touchUp(screenX, screenY, pointer, button))
                return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        screenX -= mX;
        screenY -= mY;

        for(Control child : mChilds)
        {
            //if(!child.getSize().contains(screenX, screenY))
            //    continue;
            if(child.touchDown(screenX, screenY, pointer, button))
                return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        screenX -= mX;
        screenY -= mY;
        for(Control child : mChilds)
        {
            if(!child.getSize().contains(screenX, screenY))
                continue;
            if(child.touchDragged(screenX, screenY, pointer))
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        screenX -= mX;
        screenY -= mY;
        for(Control child : mChilds)
        {
            if(!child.getSize().contains(screenX, screenY))
                continue;
            if(child.mouseMoved(screenX, screenY))
                return true;
        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        for(Control child : mChilds)
        {
            if(child.scrolled(amount))
                return true;
        }
        return false;
    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    public Vector2 getOrigin() {
        return mOrigin;
    }

    public void setOrigin(Vector2 origin) {
        mOrigin = origin;
    }
}
