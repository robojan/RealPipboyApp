package nl.robojan.real_pipboy.PipBoy.Options;

import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.Connection.ConnectionManager;
import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.PipBoy.Controls.CardInfo;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Line;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;
import nl.robojan.real_pipboy.PipBoy.Controls.VerticalFadeLine;

/**
 * Created by s120330 on 13-7-2015.
 */
class HeadlineRect extends Control {
    private float mWidth, mHeight;

    private VerticalFadeLine mHeadline_v1;
    private Line mHeadline_h1;
    private Text mHeadline_Title;
    private Line mHeadline_h2;
    private CardInfo mHeadline_ConnInfo;
    private CardInfo mHeadline_UnkInfo;

    private ConnectionManager.TCPStatus mTCPStatus;

    public HeadlineRect(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;


        mHeadline_v1 = new VerticalFadeLine(0, 0);
        addChild(mHeadline_v1);
        mHeadline_h1 = new Line(0, 0, 50);
        addChild(mHeadline_h1);
        mHeadline_Title = new Text(70, 0, GameString.getString("sRJOptions"), 4);
        addChild(mHeadline_Title);
        mHeadline_h2 = new Line(mHeadline_Title.getRight()+20, 0, 30);
        addChild(mHeadline_h2);

        mHeadline_ConnInfo = new CardInfo(240, 0, 300, GameString.getString("sRJCONNAbbrev"),
                "None");
        addChild(mHeadline_ConnInfo);

        mHeadline_UnkInfo = new CardInfo(mHeadline_ConnInfo.getRight() + 10, 0, 20, "", "");
        mHeadline_UnkInfo.setWidth(mWidth - mHeadline_UnkInfo.getX());
        addChild(mHeadline_UnkInfo);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    @Override
    public void update(Context context) {

        ConnectionManager.TCPStatus connStatus = ConnectionManager.getInstance().getTCPStatus();
        if(connStatus != mTCPStatus) {
            mTCPStatus = connStatus;
            switch(connStatus) {
                case Disconnected:
                    mHeadline_ConnInfo.setValue(GameString.getString("sRJDisconnected"));
                    break;
                case Connecting:
                    mHeadline_ConnInfo.setValue(GameString.getString("sRJConnecting"));
                    break;
                case ConnectionFailed:
                    mHeadline_ConnInfo.setValue(GameString.getString("sRJConnFailure"));
                    break;
                case Connected:
                    mHeadline_ConnInfo.setValue(GameString.getString("sRJConnected"));
                    break;
            }
        }

        super.update(context);
    }
}
