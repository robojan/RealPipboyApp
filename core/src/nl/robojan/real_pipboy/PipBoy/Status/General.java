package nl.robojan.real_pipboy.PipBoy.Status;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.Karma;
import nl.robojan.real_pipboy.FalloutData.Reputation;
import nl.robojan.real_pipboy.FalloutData.Reputations;
import nl.robojan.real_pipboy.FalloutData.StatusItem;
import nl.robojan.real_pipboy.FalloutData.StatusList;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.ListBox;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;
import nl.robojan.real_pipboy.PipBoy.Controls.TextBox;

/**
 * Created by s120330 on 11-7-2015.
 */
public class General extends Control {

    public enum Mode {
        GENERAL,
        REPUTATION,
    }

    private float mWidth, mHeight;
    private Mode mMode = null;

    private TextBox stats_genrep_button;
    private ListBox stats_general_container;
    private ListBox stats_reputation_container;

    private RepRect stats_reptitle_rect;
    private KarmaRect stats_karma_rect;

    private StatusList mGeneralStats = new StatusList();
    private Reputations mReputations = new Reputations();

    public General(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;
        create();
    }

    private void create() {
        float xBuffer = 40;

        stats_genrep_button = new TextBox(850,50, "", Align.right);
        stats_genrep_button.addClickableListener(mGenRepHandler);
        addChild(stats_genrep_button);

        stats_general_container = new ListBox(mX, mY, 500, mHeight);

        stats_reputation_container = new ListBox(mX, mY, 500, mHeight);
        stats_reptitle_rect = new RepRect(stats_reputation_container.getRight() + xBuffer,0,
                905 - stats_reputation_container.getRight() -xBuffer);

        stats_karma_rect = new KarmaRect(stats_general_container.getRight() + xBuffer, 0,
                stats_reptitle_rect.getWidth());
        stats_karma_rect.setY(stats_general_container.getY() +
                (mHeight-stats_karma_rect.getHeight())/2);


        setMode(Mode.REPUTATION);
    }

    @Override
    public void update(Context context) {
        IFalloutData data = context.foData;
        super.update(context);
        switch(mMode){

            case GENERAL:
                updateGeneral(data);
                break;
            case REPUTATION:
                updateReputation(data);
                break;
        }

    }

    private void updateGeneral(IFalloutData data){
        StatusList stats = data.getStatistics();
        if(!stats.equivalent(mGeneralStats)){
            stats_general_container.clearItems();
            mGeneralStats = stats;
            for(int i = 0; i < stats.items.size; i++) {
                StatusItem info = stats.items.get(i);
                StatsItem item = new StatsItem(0,0, stats_general_container.getWidth(),
                        info.name, info.level);
                stats_general_container.addItem(item);
            }
        }
        Karma.KarmaLevel karma = data.getKarmaLevel();
        int playerLevel = data.getLevel();
        stats_karma_rect.setAlignment(Karma.getKarmaName(karma));
        stats_karma_rect.setIcon(Karma.getKarmaIcon(karma));
        stats_karma_rect.setTitle(Karma.getKarmaTitle(karma, playerLevel));
    }

    private void updateReputation(IFalloutData data){
        Reputations reps = data.getReputations();
        if(!reps.equivalent(mReputations)){
            stats_reputation_container.clearItems();
            mReputations = reps;
            for(int i = 0; i < reps.reputations.size; i++) {
                Reputation info = reps.reputations.get(i);
                StatsItem item = new StatsItem(0,0, stats_reputation_container.getWidth(),
                        info.faction);
                item.addClickableListener(mReputationHandler, info);
                stats_reputation_container.addItem(item);
            }
        }
    }

    private void setMode(Mode mode) {
        if(mode == mMode)
            return;
        mMode = mode;
        switch(mMode) {
            case GENERAL:
                stats_genrep_button.setText("Reputation");
                if(!containsChild(stats_general_container))
                    addChild(stats_general_container, true);
                if(!containsChild(stats_karma_rect))
                    addChild(stats_karma_rect, true);
                removeChild(stats_reputation_container);
                removeChild(stats_reptitle_rect);
                break;
            case REPUTATION:
                stats_genrep_button.setText("General");
                if(!containsChild(stats_reputation_container))
                    addChild(stats_reputation_container, true);
                if(!containsChild(stats_reptitle_rect))
                    addChild(stats_reptitle_rect);
                removeChild(stats_general_container);
                removeChild(stats_karma_rect);
                break;
        }
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    private ClickableListener mGenRepHandler = new ClickableListener() {
        @Override
        public void onClickableEvent(Control source, Object user) {
            if(mMode == Mode.GENERAL) {
                setMode(Mode.REPUTATION);
            } else {
                setMode(Mode.GENERAL);
            }
        }
    };

    private ClickableListener mReputationHandler = new ClickableListener() {
        @Override
        public void onClickableEvent(Control source, Object user) {
            if(user.getClass() != Reputation.class)
                return;
            Reputation rep = (Reputation)user;
            stats_reptitle_rect.setReputation(rep);
        }
    };

    private class RepRect extends Control {
        private final static String SOLID_BLACK = "textures/interface/icons/pipboyimages/reputations/reputations_goodsprings.dds";

        private float mWidth, mHeight;

        private Image stats_reputation_image;
        private Text stats_reputation_title;
        private Text stats_reputation_rep_title;

        public RepRect(float x, float y, float width) {
            super(x, y);
            mWidth = width;
            create();
        }

        public void setReputation(Reputation reputation) {
            stats_reputation_title.setText(reputation.reputation);
            stats_reputation_image.setFile(reputation.icon);
            stats_reputation_rep_title.setText(reputation.faction);
        }

        private void create(){
            stats_reputation_image = new Image(0, 80, SOLID_BLACK, 256*1.5f, 256*1.5f);
            addChild(stats_reputation_image);

            stats_reputation_title = new Text(0, stats_reputation_image.getBottom() + 0,
                    "Vilified", 2, mWidth, Align.center);
            addChild(stats_reputation_title);

            stats_reputation_rep_title = new Text(0, stats_reputation_image.getBottom()+50,
                    "Goodsprings", 2, mWidth, Align.center);
            addChild(stats_reputation_rep_title);

            mHeight = stats_reputation_title.getY() + stats_reputation_title.getHeight();
        }

        @Override
        public Rectangle getSize() {
            return new Rectangle(mX, mY, mWidth, mHeight);
        }
    }

    private class KarmaRect extends Control {
        private static final String DEFAULT_IMAGE =
                "textures/interface/icons/pipboyimages/karma icons/karma_neutral.dds";
        private float mWidth, mHeight;

        private Text stats_karma_alignment;
        private Image stats_karma_image;
        private Text stats_karma_title;

        public KarmaRect(float x, float y, float width) {
            super(x, y);
            mWidth = width;
            create();
        }

        public void setAlignment(String alignment) {
            stats_karma_alignment.setText(alignment);
        }

        public void setIcon(String icon) {
            stats_karma_image.setFile(icon);
        }

        public void setTitle(String title) {
            stats_karma_title.setText(title);
        }

        private void create() {
            stats_karma_alignment = new Text(0, 0, "Neutral", 2, mWidth,  Align.center);
            addChild(stats_karma_alignment);

            stats_karma_image = new Image(0, -50, DEFAULT_IMAGE, 256*1.5f, 256*1.5f);
            stats_karma_image.setX((mWidth - stats_karma_image.getWidth())/2);
            addChild(stats_karma_image);

            stats_karma_title = new Text(0,stats_karma_image.getY() + 256 + 80, "Drifter", 2,
                    mWidth, Align.center);
            addChild(stats_karma_title);
            mHeight = stats_karma_title.getBottom();

            //addChild(new Image(0,0,"textures/interface/shared/solid.png",mWidth,mHeight,
            //        new Color(1,0,0,0.2f)));
        }

        @Override
        public Rectangle getSize() {
            return new Rectangle(mX, mY, mWidth, mHeight);
        }
    }

}
