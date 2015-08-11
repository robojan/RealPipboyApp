package nl.robojan.real_pipboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;


/**
 * Created by s120330 on 2-8-2015.
 */
public class Settings {
    private static final String SETTINGSFILE = "nl.robojan.real_pipboy.settings";
    private static Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    private Color mPipboyColor = new Color(0.1f, 1.0f, 0.5f, 1.0f);
    private String mHostName = "";
    private int mPort = 28115;
    private boolean mScreenStretched = false;

    private Settings() {

    }

    public void loadSettings() {
        Preferences prefs = Gdx.app.getPreferences(SETTINGSFILE);
        int pipboyColor = prefs.getInteger("PipboyColor", 0);
        if(pipboyColor != 0) {
            mPipboyColor = new Color(pipboyColor);
        }
        mHostName = prefs.getString("HostName");
        mPort = prefs.getInteger("Port", 28115);
    }

    public void saveSettings() {
        Preferences prefs = Gdx.app.getPreferences(SETTINGSFILE);
        prefs.putInteger("PipboyColor", Color.rgba8888(mPipboyColor));
        prefs.putString("HostName", mHostName);
        prefs.putInteger("Port", mPort);
        prefs.flush();
    }

    public Color getPipboyColor() {
        return mPipboyColor;
    }

    public void setPipboyColor(Color color) {
        mPipboyColor = color;
        saveSettings();
    }

    public String getHostName() {
        return mHostName;
    }

    public void setHostName(String hostName) {
        mHostName = hostName;
        saveSettings();
    }

    public int getPort(){
        return mPort;
    }

    public void setPort(int port) {
        mPort = port;
        saveSettings();
    }

    public boolean isScreenStretched() {
        return mScreenStretched;
    }

    public void setScreenStretched(boolean screenStretched) {
        mScreenStretched = screenStretched;
        saveSettings();
    }
}
