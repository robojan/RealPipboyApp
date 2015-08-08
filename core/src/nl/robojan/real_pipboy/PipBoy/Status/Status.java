package nl.robojan.real_pipboy.PipBoy.Status;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.Connection.ConnectionManager;
import nl.robojan.real_pipboy.Connection.Packets.DoActionPacket;
import nl.robojan.real_pipboy.Constants;
import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.SuperTextBox;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;
import nl.robojan.real_pipboy.PipBoy.Controls.TextBox;
import nl.robojan.real_pipboy.RenderContext;

/**
 * Created by s120330 on 8-7-2015.
 */
public class Status extends Control {

    public enum StatusMode {
        INVALLID,
        CND,
        RAD,
        EFF,
        H2O,
        FOD,
        SLP
    }


    private float mWidth, mHeight;
    private StatusMode mMode = StatusMode.INVALLID;
    private boolean mVisible = false;


    // Mode buttons
    private SuperTextBox stats_CND_button;
    private SuperTextBox stats_RAD_button;
    private SuperTextBox stats_EFF_button;
    private SuperTextBox stats_H2O_button;
    private SuperTextBox stats_FOD_button;
    private SuperTextBox stats_SLP_button;

    // Action buttons
    private TextBox stats_stimpak_button;
    private TextBox stats_healing_mode;
    private TextBox stats_radaway_button;
    private TextBox stats_radx_button;
    private TextBox stats_drbag_button;

    // sub pages
    private ConditionStatus stats_vaultboy;
    private Text stats_player_name;
    private RadiationStatus stats_radiation_container;
    private DehydrationStatus stats_dehydration_container;
    private HungerStatus stats_hunger_container;
    private SleepStatus stats_sleep_container;
    private EffectsStatus stats_effects_container;


    public Status(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        create();
    }

    public void create()
    {
        createModeButtons();
        createActionButtons();

        // Condition
        stats_vaultboy = new ConditionStatus(mWidth/2, 40, mWidth, mHeight, 1.45f);
        stats_player_name = new Text(mWidth/2, 0, "Player name", Align.center, new Color(1,1,1,1));
        stats_player_name.setY(mHeight - stats_player_name.getHeight()/2);
        // Radiation
        stats_radiation_container = new RadiationStatus(0, 0, mWidth, mHeight, 1.3f);
        // Effects
        stats_effects_container = new EffectsStatus(0,0,mWidth, mHeight);
        // Water
        stats_dehydration_container = new nl.robojan.real_pipboy.PipBoy.Status.DehydrationStatus(0,0,mWidth, mHeight, 1.3f);
        // Food
        stats_hunger_container = new nl.robojan.real_pipboy.PipBoy.Status.HungerStatus(0,0,mWidth, mHeight,1.3f);
        // Sleep
        stats_sleep_container = new SleepStatus(0,0,mWidth,mHeight, 1.3f);

        setMode(StatusMode.CND);
    }

    public void createModeButtons()
    {
        stats_CND_button = new SuperTextBox(0,0, GameString.getString("sStatsCNDAbbrev"), 84);
        stats_RAD_button = new SuperTextBox(stats_CND_button.getX(), stats_CND_button.getBottom(),
                GameString.getString("sStatsRADAbbrev"), 84);
        stats_EFF_button = new SuperTextBox(stats_CND_button.getX(), stats_RAD_button.getBottom(),
                GameString.getString("sStatsEFFAbbrev"), 84);
        stats_H2O_button = new SuperTextBox(stats_CND_button.getX(), stats_EFF_button.getBottom(),
                GameString.getString("sStatsH20Abbrev"), 84);
        stats_FOD_button = new SuperTextBox(stats_CND_button.getX(), stats_H2O_button.getBottom(),
                GameString.getString("sStatsFODAbbrev"), 84);
        stats_SLP_button = new SuperTextBox(stats_CND_button.getX(), stats_FOD_button.getBottom(),
                GameString.getString("sStatsSLPAbbrev"), 84);
        addChild(stats_CND_button);
        addChild(stats_RAD_button);
        addChild(stats_EFF_button);

        // make buttons exclusive
        ClickableListener modeButtonsListener = new ClickableListener() {
            @Override
            public void onClickableEvent(Control source, Object user) {
                if(source == stats_CND_button){
                    setMode(StatusMode.CND);
                } else if(source == stats_RAD_button){
                    setMode(StatusMode.RAD);
                } else if(source == stats_EFF_button){
                    setMode(StatusMode.EFF);
                } else if(source == stats_H2O_button){
                    setMode(StatusMode.H2O);
                } else if(source == stats_FOD_button){
                    setMode(StatusMode.FOD);
                } else if(source == stats_SLP_button){
                    setMode(StatusMode.SLP);
                }
            }
        };

        stats_CND_button.addClickableListener(modeButtonsListener);
        stats_RAD_button.addClickableListener(modeButtonsListener);
        stats_EFF_button.addClickableListener(modeButtonsListener);
        stats_H2O_button.addClickableListener(modeButtonsListener);
        stats_FOD_button.addClickableListener(modeButtonsListener);
        stats_SLP_button.addClickableListener(modeButtonsListener);
        stats_CND_button.setClickSound("sound/fx/ui/pipboy/ui_pipboy_select.wav");
        stats_RAD_button.setClickSound("sound/fx/ui/pipboy/ui_pipboy_select.wav");
        stats_EFF_button.setClickSound("sound/fx/ui/pipboy/ui_pipboy_select.wav");
        stats_H2O_button.setClickSound("sound/fx/ui/pipboy/ui_pipboy_select.wav");
        stats_FOD_button.setClickSound("sound/fx/ui/pipboy/ui_pipboy_select.wav");
        stats_SLP_button.setClickSound("sound/fx/ui/pipboy/ui_pipboy_select.wav");
    }

    public void createActionButtons() {
        stats_stimpak_button = new TextBox(850, 10, "Stimpak", Align.right);
        stats_stimpak_button.addClickableListener(mConsumeableListener);
        stats_healing_mode = new TextBox(stats_stimpak_button.getX(),
                stats_stimpak_button.getBottom(), "Limbs", Align.right);
        stats_healing_mode.addClickableListener(mConsumeableListener);
        stats_radaway_button = new TextBox(stats_stimpak_button.getX(),
                stats_stimpak_button.getY(), "RadAway", Align.right);
        stats_radaway_button.addClickableListener(mConsumeableListener);
        stats_radx_button = new TextBox(stats_healing_mode.getX(),
                stats_healing_mode.getY(), "Rad-X", Align.right);
        stats_radx_button.addClickableListener(mConsumeableListener);
        stats_drbag_button = new TextBox(stats_healing_mode.getX(),
                stats_healing_mode.getBottom(), "Docter's Bag", Align.right);
        stats_drbag_button.addClickableListener(mConsumeableListener);
    }

    private ClickableListener mConsumeableListener = new ClickableListener() {
        @Override
        public void onClickableEvent(Control source, Object user) {
            ConnectionManager conn = ConnectionManager.getInstance();
            if(source == stats_stimpak_button) {
                conn.send(new DoActionPacket(DoActionPacket.ACTION_USESTIMPACK));
            } else if(source == stats_drbag_button) {
                conn.send(new DoActionPacket(DoActionPacket.ACTION_USEDRBAG));
            } else if(source == stats_radaway_button) {
                conn.send(new DoActionPacket(DoActionPacket.ACTION_USERADAWAY));
            } else if(source == stats_radx_button) {
                conn.send(new DoActionPacket(DoActionPacket.ACTION_USERADX));
            }
        }
    };

    public void setCNDActive(boolean active) {
        if (active) {
            addChild(stats_stimpak_button, true);
            addChild(stats_drbag_button, true);
            addChild(stats_vaultboy, true);
            addChild(stats_player_name, true);
        } else {
            removeChild(stats_stimpak_button);
            removeChild(stats_drbag_button);
            removeChild(stats_vaultboy);
            removeChild(stats_player_name);
        }
    }

    public void setRADActive(boolean active) {
        if (active) {
            addChild(stats_radaway_button, true);
            addChild(stats_radx_button, true);
            addChild(stats_radiation_container, true);
        } else {
            removeChild(stats_radaway_button);
            removeChild(stats_radx_button);
            removeChild(stats_radiation_container);
        }
    }

    public void setEFFActive(boolean active) {
        if (active) {
            addChild(stats_effects_container, true);
        } else {
            removeChild(stats_effects_container);
        }
    }

    public void setH2OActive(boolean active) {
        if (active) {
            addChild(stats_dehydration_container, true);
        } else {
            removeChild(stats_dehydration_container);
        }
    }

    public void setFODActive(boolean active) {
        if (active) {
            addChild(stats_hunger_container, true);
        } else {
            removeChild(stats_hunger_container);
        }
    }

    public void setSLPActive(boolean active) {
        if (active) {
            addChild(stats_sleep_container, true);
        } else {
            removeChild(stats_sleep_container);
        }
    }

    public void setMode(StatusMode mode) {
        if(mode == mMode)
            return;
        mMode = mode;
        stats_CND_button.setSelected(mMode == StatusMode.CND);
        stats_RAD_button.setSelected(mMode == StatusMode.RAD);
        stats_EFF_button.setSelected(mMode == StatusMode.EFF);
        stats_H2O_button.setSelected(mMode == StatusMode.H2O);
        stats_FOD_button.setSelected(mMode == StatusMode.FOD);
        stats_SLP_button.setSelected(mMode == StatusMode.SLP);
        setCNDActive(mMode == StatusMode.CND);
        setRADActive(mMode == StatusMode.RAD);
        setEFFActive(mMode == StatusMode.EFF);
        setH2OActive(mMode == StatusMode.H2O);
        setFODActive(mMode == StatusMode.FOD);
        setSLPActive(mMode == StatusMode.SLP);
    }

    public StatusMode getMode(){
        return mMode;
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    public boolean isVisible() {
        return mVisible;
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void update(Context context) {
        IFalloutData data = context.foData;
        if(mVisible) {
            super.update(context);

            stats_player_name.setText(String.format("%s - Level %d", data.getPlayerName(),
                    data.getLevel()));
            int numStimpacks = data.getNumStimpacks();
            stats_stimpak_button.setText((numStimpacks > 0 ? "("+ numStimpacks + ") " : "" ) +
                    "Stimpak");
            if(numStimpacks > 0 && (data.getHP() < data.getMaxHP())) {
                stats_stimpak_button.setEnabled(true);
                stats_stimpak_button.setColor(new Color(1,1,1,1));
            } else {
                stats_stimpak_button.setEnabled(false);
                stats_stimpak_button.setColor(new Color(1, 1, 1, 0.5f));
            }
            int numDrBags = data.getNumDrBags();
            stats_drbag_button.setText((numDrBags > 0 ? "("+ numDrBags + ") " : "" ) +
                    "Docter's Bag");
            if(numDrBags > 0 && (data.isLimbDamaged())) {
                stats_drbag_button.setEnabled(true);
                stats_drbag_button.setColor(new Color(1,1,1,1));
            } else {
                stats_drbag_button.setEnabled(false);
                stats_drbag_button.setColor(new Color(1, 1, 1, 0.5f));
            }
            int numRadAway = data.getNumRadAway();
            stats_radaway_button.setText((numRadAway > 0 ? "("+ numRadAway + ") " : "" ) +
                    "RadAway");
            if(numRadAway > 0 && data.getRads() > 0) {
                stats_radaway_button.setEnabled(true);
                stats_radaway_button.setColor(new Color(1,1,1,1));
            } else {
                stats_radaway_button.setEnabled(false);
                stats_radaway_button.setColor(new Color(1, 1, 1, 0.5f));
            }
            int numRadx = data.getNumRadX();
            stats_radx_button.setText((numRadx > 0 ? "("+ numRadx + ") " : "" ) +
                    "Rad-X");
            if(numRadx > 0) {
                stats_radx_button.setEnabled(true);
                stats_radx_button.setColor(new Color(1,1,1,1));
            } else {
                stats_radx_button.setEnabled(false);
                stats_radx_button.setColor(new Color(1, 1, 1, 0.5f));
            }

            if(data.isHardcore()) {
                if(!containsChild(stats_H2O_button))
                    addChild(stats_H2O_button, true);
                if(!containsChild(stats_FOD_button))
                    addChild(stats_FOD_button, true);
                if(!containsChild(stats_SLP_button))
                    addChild(stats_SLP_button, true);
            } else {
                removeChild(stats_H2O_button);
                removeChild(stats_FOD_button);
                removeChild(stats_SLP_button);
            }
        }
    }

    @Override
    public void render(RenderContext context) {
        if(mVisible) {
            super.render(context);
        }
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    @Override
    public boolean scrolled(int amount) {
        if(mVisible) {
            return super.scrolled(amount);
        }
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if(mVisible){
            return super.touchDown(x, y, pointer, button);
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(mVisible) {
            return super.tap(x, y, count, button);
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        if(mVisible)
            return super.longPress(x, y);
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if(mVisible)
            return super.fling(velocityX, velocityY, button);
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if(mVisible)
            return super.pan(x, y, deltaX, deltaY);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if(mVisible)
            return super.panStop(x, y, pointer, button);
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(mVisible)
            return super.zoom(initialDistance, distance);
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1,
                         Vector2 pointer2) {
        if(mVisible)
            return super.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(mVisible)
            return super.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(mVisible)
            return super.keyUp(keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if(mVisible)
            return super.keyTyped(character);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(mVisible)
            return super.touchUp(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(mVisible)
            return super.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(mVisible)
            return super.touchDragged(screenX, screenY, pointer);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if(mVisible)
            return super.mouseMoved(screenX, screenY);
        return false;
    }
}
