package nl.robojan.real_pipboy.PipBoy.Status;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.Line;
import nl.robojan.real_pipboy.PipBoy.Controls.ScrollbarVert;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;
import nl.robojan.real_pipboy.PipBoy.Controls.VerticalFadeLine;

/**
 * Created by s120330 on 11-7-2015.
 */
public class StatisticsDisplay extends Control {
    private final static String DEFAULT_ICON = null;
    private final static String DEFAULT_BADGE = null;

    private float mWidth;
    private float mHeight;
    private float mEndX;

    private Image stats_icon;
    private Image stats_icon_badge;
    private Line stats_icon_separator;
    private VerticalFadeLine stats_icon_separator_vert;
    private DescriptionRect stats_description_rect;

    public StatisticsDisplay(float x, float y, float endX, float height) {
        super(x, y);
        mHeight = height;
        mEndX = endX;
        mWidth = endX - x;
        create();
    }

    public void setIcon(String icon){
        if(icon == null){
            stats_icon.setVisible(false);
        }else {
            stats_icon.setFile(icon);
            stats_icon.setVisible(true);
        }
    }

    public void setBadge(String badge) {
        if(badge == null){
            stats_icon_badge.setVisible(false);
        }else {
            stats_icon_badge.setFile(badge);
            stats_icon_badge.setVisible(true);
        }
    }

    public void setDescription(String description) {
        if(description == null){
            stats_description_rect.setDescription("");
        }else {
            stats_description_rect.setDescription(description);
        }
    }

    private void create(){
        stats_icon = new Image(0, -60, DEFAULT_ICON, 340, 384);
        stats_icon.setX((mWidth - stats_icon.getWidth())/2);
        addChild(stats_icon);
        stats_icon_badge = new Image(30, 255, DEFAULT_BADGE, 64, 64);
        addChild(stats_icon_badge);
        stats_icon_separator = new Line(22, 319, mWidth - 22);
        addChild(stats_icon_separator);
        stats_icon_separator_vert = new VerticalFadeLine(stats_icon_separator.getWidth(), 0);
        stats_icon_separator.addChild(stats_icon_separator_vert);

        stats_description_rect = new DescriptionRect(stats_icon_separator.getX(),333,
                stats_icon_separator.getLength(), 175, "");
        addChild(stats_description_rect);
    }

    @Override
    public void setX(float x) {
        mWidth = mEndX-x;
        stats_icon.setX((mWidth - stats_icon.getWidth()) / 2);
        stats_icon_separator.setLength(mWidth - stats_icon_separator.getX());
        stats_icon_separator_vert.setX(stats_icon_separator.getWidth() -
                stats_icon_separator_vert.getWidth());
        stats_description_rect.setWidth(stats_icon_separator.getLength());
        super.setX(x);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    public class DescriptionRect extends Control {
        private float mWidth, mHeight;
        private String mDescription;

        private ScrollbarVert stats_description_scrollbar;
        private Text stats_description;

        public DescriptionRect(float x, float y, float width, float height, String description) {
            super(x, y);
            mWidth = width;
            mHeight = height;
            mDescription = description;
            create();
        }

        private void create() {
            clearChilds();

            stats_description_scrollbar = new ScrollbarVert(mWidth, 35, 130, 1, 1);
            addChild(stats_description_scrollbar);

            stats_description = new Text(0,5, mDescription, 2, mWidth, Align.left);
            stats_description.setWrap(true);
            addChild(stats_description);
        }

        public void setWidth(float width) {
            mWidth = width;
            stats_description_scrollbar.setX(mWidth);
            stats_description.setTargetWidth(mWidth);
        }

        public void setDescription(String description) {
            mDescription = description;
            stats_description.setText(description);
        }

        public String getDescription() {
            return mDescription;
        }

        @Override
        public Rectangle getSize() {
            return new Rectangle(mX, mY, mWidth, mHeight);
        }

    }
}
