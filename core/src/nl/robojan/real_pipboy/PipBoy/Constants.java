package nl.robojan.real_pipboy.PipBoy;

/**
 * Created by s120330 on 7-7-2015.
 */
public class Constants {
    public static final float LINE_THICKNESS = 4;
    public static final float PIPBOY_WIDTH = 1024;
    public static final float PIPBOY_HEIGHT = 768;
    public static final float GAP_SIZE = 15;

    public static final float LINE_BRIGHTNESS = 0.78f;
    public static final float LINE_BRIGHTNESS_DISABLED = 0.5f;
    public static final float TEXT_BRIGHTNESS = 0.1f;
    public static final float TEXT_BRIGHTNESS_DISABLED = 0.5f;
    public static final float BACKGROUND_FILL_ALPHA = 0.125f;
    public static final float BACKGROUND_FILL_BRIGHTNESS = 0.5f;

    public static final String FNF_IMAGE = "textures/interface/shared/missing_image.dds.png";

    private static final float[] BRIGHTNESSES = new float[]{
            LINE_BRIGHTNESS,
            TEXT_BRIGHTNESS,
            0.686f,
            0.686f,
            0.412f,
            0.686f,
            0.608f,
            1.0f
        };

    public static float getBrightness(int index)
    {
        return BRIGHTNESSES[index - 1];
    }

    public static float getBrightness()
    {
        return getBrightness(1);
    }
}
