package nl.robojan.real_pipboy.PipBoy.Options;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.Connection.ConnectionManager;
import nl.robojan.real_pipboy.Connection.FileTransferHandler;
import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.TextBox;
import nl.robojan.real_pipboy.PipBoy.Controls.TextInputBox;
import nl.robojan.real_pipboy.Settings;

/**
 * Created by s120330 on 16-7-2015.
 */
public class ConnectionOptions extends Control {
    private float mWidth, mHeight;

    private TextInputBox mHostNameInput;
    private TextInputBox mPortInput;
    private TextBox mConnectButton;

    private boolean mTCPConnected = false;

    public ConnectionOptions(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;

        Settings settings = Settings.getInstance();

        mHostNameInput = new TextInputBox(10, 50, GameString.getString("sRJHostAddr"),
                settings.getHostName(), GameString.getString("sRJHostAddrHint"), 230, 40, 40);
        addChild(mHostNameInput);
        mPortInput = new TextInputBox(250, 50, GameString.getString("sRJHostPort"),
                Integer.toString(settings.getPort()), GameString.getString("sRJHostPortHint"),
                100, 40, 40);
        mPortInput.setValidCharacters("0123456789");
        addChild(mPortInput);
        mConnectButton = new TextBox(360 + 90, 50, GameString.getString("sRJConnect"), 180, 40, 40,
                Align.center, new Color(1,1,1,1));
        mConnectButton.setBoxVisible(true);
        mConnectButton.addClickableListener(mOnConnectButtonClick);
        mConnectButton.setClickSound("sound/fx/ui/pipboy/ui_pipboy_select.wav");
        addChild(mConnectButton);
    }

    private ClickableListener mOnConnectButtonClick = new ClickableListener() {
        @Override
        public void onClickableEvent(Control source, Object user) {
            if(mTCPConnected) {
                ConnectionManager.getInstance().disconnect();
            } else {
                String hostname = mHostNameInput.getText();
                String portStr = mPortInput.getText();
                int port = Integer.parseInt(portStr);
                Settings.getInstance().setHostName(hostname);
                Settings.getInstance().setPort(port);
                ConnectionManager.getInstance().connect(hostname, port);
            }
        }
    };

    @Override
    public void update(Context context) {
        ConnectionManager manager = ConnectionManager.getInstance();
        ConnectionManager.TCPStatus status = manager.getTCPStatus();
        boolean connected = status == ConnectionManager.TCPStatus.Connected ||
                status == ConnectionManager.TCPStatus.Connecting;
        if(mTCPConnected != connected) {
            mTCPConnected = connected;
            if(connected) {
                mHostNameInput.setEnabled(false);
                mPortInput.setEnabled(false);
                mHostNameInput.setColor(new Color(1, 1, 1, 0.5f));
                mPortInput.setColor(new Color(1, 1, 1, 0.5f));
                mConnectButton.setText(GameString.getString("SRJDisconnect"));
            } else {
                mHostNameInput.setEnabled(true);
                mPortInput.setEnabled(true);
                mHostNameInput.setColor(new Color(1, 1, 1, 1.0f));
                mPortInput.setColor(new Color(1, 1, 1, 1.0f));
                mConnectButton.setText(GameString.getString("sRJConnect"));
            }
        }

        super.update(context);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }
}
