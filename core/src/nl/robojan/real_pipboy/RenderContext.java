package nl.robojan.real_pipboy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;

import java.util.Stack;

/**
 * Created by s120330 on 6-7-2015.
 */
public class RenderContext {
    public float screenWidth;
    public float screenHeight;
    public SpriteBatch batch;
    public ShaderProgram pipboyShaderProgram;
    public ShaderProgram postShaderProgram;
    public FrameBuffer fbo;
    public Context context;
    public Stack<Rectangle> clippingRectangles;

}
