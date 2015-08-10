package nl.robojan.real_pipboy.PipBoy.Data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.Connection.ConnectionManager;
import nl.robojan.real_pipboy.Connection.Packets.DoActionPacket;
import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.MapMarker;
import nl.robojan.real_pipboy.FalloutData.Note;
import nl.robojan.real_pipboy.FalloutData.NoteList;
import nl.robojan.real_pipboy.FalloutData.Quest;
import nl.robojan.real_pipboy.FalloutData.QuestList;
import nl.robojan.real_pipboy.Constants;
import nl.robojan.real_pipboy.FalloutData.Radio;
import nl.robojan.real_pipboy.FalloutData.RadioList;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.ListBox;
import nl.robojan.real_pipboy.PipBoy.Controls.ListBoxItem;
import nl.robojan.real_pipboy.PipBoy.Controls.TabLine;
import nl.robojan.real_pipboy.PipBoy.Controls.TextBox;

/**
 * Created by s120330 on 13-7-2015.
 */
public class DataMenu extends Control {

    private final static String[] TABLINE_BUTTONS = new String[] { "Local Map", "World Map",
            "Quests", "Misc", "Radio"};

    private float mWidth, mHeight;

    private MainRect MM_MainRect;
    private TabLine MM_Tabline;

    public DataMenu() {
        super(0, 0);
        mWidth = Constants.PIPBOY_WIDTH;
        mHeight = Constants.PIPBOY_HEIGHT;

        MM_MainRect = new MainRect(50, 50, 855, 650);
        addChild(MM_MainRect);

        MM_Tabline = new TabLine(MM_MainRect.getX(), MM_MainRect.getY() + MM_MainRect.getHeight()
                - 50, MM_MainRect.getWidth(), TABLINE_BUTTONS);
        addChild(MM_Tabline);

    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    private class MainRect extends Control {
        private float mWidth, mHeight;

        private HeadlineRect mHeadlineRect;

        private WorldMapMenu mWorldMap;
        private TextBox mLocalMap;

        private ListBox mQuestsList;
        private ListBox mNotesList;
        private ListBox mRadioStationList;


        private DataRect mDataRect;

        private TextBox mButtonA;
        private TextBox mButtonX;
        private TextBox mButtonY;

        private int mCurrentTab = -1;

        private RadioList mStations = new RadioList();
        private Radio mSelectedRadio = null;

        private NoteList mNotes = new NoteList();
        private Note mSelectedNote = null;

        private QuestList mQuests = new QuestList();
        private Quest mSelectedQuest = null;

        private WaveformRect mWaveformRect;

        private final String BUTTONA_STRINGS[] = {"sTravel", "sMakeActiveQuest",
            "sPlayAudio", "sTune", "sStopAudio"};
        private final String BUTTONX_STRINGS[] = {"sPlaceMarker", "sShowLocation",
                "sShowAllNotes", "sShowActiveNotes", "sNextChallenge"};

        public MainRect(float x, float y, float width, float height) {
            super(x, y);
            mWidth = width;
            mHeight = height;

            mHeadlineRect = new HeadlineRect(0,0,mWidth, 20);
            addChild(mHeadlineRect);

            mQuestsList = new ListBox(-15, 75, 400, 468);

            mNotesList = new ListBox(-15, 75, 400, 468);
            mRadioStationList = new ListBox(-15, 75, 400, 468);

            mWaveformRect = new WaveformRect(500, 125, 350, 350);

            mLocalMap = new TextBox(width/2,height/2, "Local map not implemented. \n" +
                    "Does someone have ideas how to \ngenerate the local map or fetch\n" +
                    "the local map images\nfrom the game.", Align.center);

            mWorldMap = new WorldMapMenu(0, 50, 855, 500);
            mWorldMap.setVisible(false);
            mWorldMap.setEnabled(false);
            addChild(mWorldMap);

            float buttonY = mQuestsList.getY();

            Color white = new Color(1,1,1,1);

            mButtonA = new TextBox(mWidth, buttonY, GameString.getString(BUTTONA_STRINGS[0]), 20,
                    15, Align.right, white);
            mButtonA.addClickableListener(mButtonAListener);
            addChild(mButtonA);
            mButtonX = new TextBox(mWidth, buttonY + mButtonA.getHeight(),
                    GameString.getString(BUTTONX_STRINGS[0]), 20, 15, Align.right, white);
            mButtonX.addClickableListener(mButtonXListener);
            addChild(mButtonX);
            mButtonY = new TextBox(mWidth, mButtonX.getBottom(),
                    GameString.getString("sChallengeToggle"), 20, 15, Align.right, white);
            mButtonY.addClickableListener(mButtonYListener);
            addChild(mButtonY);

            mDataRect = new DataRect(width - 400, mQuestsList.getY(), 400, 400);
            addChild(mDataRect);
        }

        private ClickableListener mButtonAListener = new ClickableListener() {
            @Override
            public void onClickableEvent(Control source, Object user) {
                MapMarker marker = mWorldMap.getSelectedMarker();
                switch(mCurrentTab) {
                    case 1:
                        //Travel
                        if(marker != null) {
                            ConnectionManager.getInstance().send(new DoActionPacket(
                                    DoActionPacket.ACTION_FASTTRAVEL, marker.getId()));
                        }
                        break;
                    case 2:
                        // SetActiveQuest
                        if(mSelectedQuest != null) {
                            ConnectionManager.getInstance().send(new DoActionPacket(
                                    DoActionPacket.ACTION_SETACTIVEQUEST,
                                    mSelectedQuest.getQuestID()));
                        }
                        break;
                    default:

                }
            }
        };

        private ClickableListener mButtonXListener = new ClickableListener() {
            @Override
            public void onClickableEvent(Control source, Object user) {

            }
        };

        private ClickableListener mButtonYListener = new ClickableListener() {
            @Override
            public void onClickableEvent(Control source, Object user) {

            }
        };

        private void setLocalMapActive(boolean active) {
            if(active) {
                mButtonA.setVisible(false);
                mButtonX.setVisible(false);
                mButtonX.setY(mQuestsList.getY());
                mButtonX.setText(GameString.getString(BUTTONX_STRINGS[0]));
                mButtonY.setVisible(false);
                if(!containsChild(mLocalMap))
                    addChild(mLocalMap, true);
            } else {
                removeChild(mLocalMap);
            }
        }

        private void setWorldMapActive(boolean active) {
            if(active) {
                mWorldMap.setVisible(true);
                mWorldMap.setEnabled(true);
                mButtonA.setVisible(true);
                mButtonA.setText(GameString.getString(BUTTONA_STRINGS[0]));
                mButtonX.setVisible(false);
                mButtonX.setY(mButtonA.getBottom());
                mButtonX.setText(GameString.getString(BUTTONX_STRINGS[0]));
                mButtonY.setVisible(false);
            } else {
                mWorldMap.setVisible(false);
                mWorldMap.setEnabled(false);
            }
        }

        private void setQuestsActive(boolean active) {
            if(active) {
                if(!containsChild(mQuestsList))
                    addChild(mQuestsList, true);
                mButtonA.setVisible(true);
                mButtonA.setText(GameString.getString(BUTTONA_STRINGS[1]));
                mButtonX.setVisible(true);
                mButtonX.setY(mButtonA.getBottom());
                mButtonX.setText(GameString.getString(BUTTONX_STRINGS[1]));
                mDataRect.setY(mButtonX.getBottom());
                mButtonY.setVisible(false);
            } else {
                removeChild(mQuestsList);
            }
        }

        private void setMiscActive(boolean active) {
            if(active) {
                if(!containsChild(mNotesList))
                    addChild(mNotesList, true);
                mButtonA.setVisible(true);
                mButtonA.setText(GameString.getString(BUTTONA_STRINGS[2]));
                mButtonX.setVisible(true);
                mButtonX.setY(mButtonA.getBottom());
                mButtonX.setText(GameString.getString(BUTTONX_STRINGS[2]));
                mDataRect.setY(mButtonX.getBottom());
                mButtonY.setVisible(false);
            } else {
                removeChild(mNotesList);
                mNotesList.highLightItem(null, true);
                mSelectedNote = null;
            }

        }

        private void setRadioActive(boolean active) {
            if(active) {
                if(!containsChild(mRadioStationList))
                    addChild(mRadioStationList, true);
                if(!containsChild(mWaveformRect))
                    addChild(mWaveformRect, true);
                mButtonA.setVisible(false);
                mButtonA.setText(GameString.getString(BUTTONA_STRINGS[3]));
                mButtonX.setVisible(false);
                mDataRect.setY(mButtonA.getBottom());
                mButtonY.setVisible(false);
            } else {
                removeChild(mRadioStationList);
                removeChild(mWaveformRect);
            }

        }

        @Override
        public Rectangle getSize() {
            return new Rectangle(mX, mY, mWidth, mHeight);
        }

        @Override
        public void update(Context context) {
            IFalloutData data = context.foData;
            if(MM_Tabline.getCurrentTab() != mCurrentTab) {
                mCurrentTab = MM_Tabline.getCurrentTab();
                setLocalMapActive(mCurrentTab == 0);
                setWorldMapActive(mCurrentTab == 1);
                setQuestsActive(mCurrentTab == 2);
                setMiscActive(mCurrentTab == 3);
                setRadioActive(mCurrentTab == 4);
                mDataRect.hideAll();
            }
            switch(mCurrentTab) {
                case 1:
                    if(mWorldMap.getSelectedMarker() != null) {
                        mButtonA.setEnabled(true);
                        mButtonA.setColor(new Color(1, 1, 1, 1));
                    } else {
                        mButtonA.setEnabled(false);
                        mButtonA.setColor(new Color(1, 1, 1, 0.5f));
                    }
                    break;
                case 2:
                    if(mSelectedQuest != null) {
                        mButtonA.setEnabled(true);
                        mButtonA.setColor(new Color(1, 1, 1, 1));
                        mButtonX.setEnabled(true);
                        mButtonX.setColor(new Color(1, 1, 1, 1));
                    } else {
                        mButtonA.setEnabled(false);
                        mButtonA.setColor(new Color(1, 1, 1, 0.5f));
                        mButtonX.setEnabled(false);
                        mButtonX.setColor(new Color(1, 1, 1, 0.5f));
                    }
                    break;
                default:
                    mButtonA.setEnabled(false);
                    mButtonA.setColor(new Color(1, 1, 1, 0.5f));
                    mButtonX.setEnabled(false);
                    mButtonX.setColor(new Color(1, 1, 1, 0.5f));

            }

            NoteList notes = data.getNotes();
            if(!mNotes.equivalent(notes)) {
                if(notes == null){
                    notes = new NoteList();
                }
                mNotes = notes;
                ListBoxItem selectedItem = mNotesList.getSelectedItem();
                mNotesList.clearItems();
                for(Note note : notes.list) {
                    DataItemMarker item = new DataItemMarker(0, 0, note.getTitle(),
                            note.equivalent(mSelectedNote));
                    item.setClickSound("sound/fx/ui/pipboy/ui_pipboy_scroll.wav");
                    item.addClickableListener(mNoteClickableListener, note);
                    mNotesList.addItem(item);
                }
                mNotesList.highLightItem(selectedItem, false);
            }

            QuestList quests = data.getQuests();
            if(!mQuests.equivalent(quests)) {
                if(quests == null) {
                    quests = new QuestList();
                }
                mQuests = quests;
                mQuestsList.clearItems();
                for(Quest quest : quests.list) {
                    DataItemMarker item = new DataItemMarker(0,0,quest.getName(),
                            quest.isActive(), !quest.isCompleted(), false);
                    item.setClickSound("sound/fx/ui/pipboy/ui_pipboy_scroll.wav");
                    item.addClickableListener(mQuestClickableListener, quest);
                    mQuestsList.addItem(item);
                    if(mSelectedQuest != null && mCurrentTab == 2 &&
                            quest.getQuestID() == mSelectedQuest.getQuestID()) {
                        mDataRect.displayQuest(quest);
                        mQuestsList.highLightItem(item, true);
                        mSelectedQuest = quest;
                    }
                }
            }

            RadioList stations = data.getRadioStations();
            if(!mStations.equivalent(stations)) {
                if(stations == null) {
                    stations = new RadioList();
                }
                mStations = stations;
                mRadioStationList.clearItems();
                for(Radio radio : mStations.list) {
                    DataItemMarker item = new DataItemMarker(0, 0, radio.getName(),
                            radio.isPlaying(), radio.isActive(), false);
                    item.setClickSound("sound/fx/ui/pipboy/ui_pipboy_tuner.wav");
                    item.addClickableListener(mRadioClickableListener, radio);
                    mRadioStationList.addItem(item);
                    if(mSelectedRadio != null && radio.getId() == mSelectedRadio.getId()) {
                        mRadioStationList.highLightItem(item, true);
                        mSelectedRadio = radio;
                    }
                }
                mWaveformRect.setPlaying(stations.isPlaying());
            }

            super.update(context);
        }

        private ClickableListener mQuestClickableListener = new ClickableListener() {
            @Override
            public void onClickableEvent(Control source, Object user) {
                if(!Quest.class.isAssignableFrom(user.getClass()) ||
                        !DataItemMarker.class.isAssignableFrom(source.getClass())) {
                    return;
                }
                Quest quest = (Quest) user;
                DataItemMarker element = (DataItemMarker) source;
                mQuestsList.highLightItem(element, false);
                mDataRect.displayQuest(quest);
                mSelectedQuest = quest;
            }
        };

        private ClickableListener mNoteClickableListener = new ClickableListener() {
            @Override
            public void onClickableEvent(Control source, Object user) {
                if(!Note.class.isAssignableFrom(user.getClass()) ||
                        !DataItemMarker.class.isAssignableFrom(source.getClass())) {
                    return;
                }
                Note note = (Note) user;
                DataItemMarker element = (DataItemMarker) source;
                DataItemMarker lastElement = (DataItemMarker) mNotesList.getSelectedItem();
                if(lastElement!= null) {
                    lastElement.setSelected(false);
                }
                element.setSelected(true);
                mNotesList.highLightItem(element, false);
                mDataRect.displayNote(note);
            }
        };

        private ClickableListener mRadioClickableListener = new ClickableListener() {
            @Override
            public void onClickableEvent(Control source, Object user) {
                if(!Radio.class.isAssignableFrom(user.getClass()) ||
                        !DataItemMarker.class.isAssignableFrom(source.getClass())) {
                    return;
                }
                Radio radio = (Radio) user;
                DataItemMarker element = (DataItemMarker) source;
                if(!radio.isActive())
                    return;
                if(mSelectedRadio == radio) {
                    ConnectionManager.getInstance().send(new DoActionPacket(
                            DoActionPacket.ACTION_TUNERADIO, radio.isPlaying() ? 0 :radio.getId()));
                } else {
                    mRadioStationList.highLightItem(element, false);
                    mSelectedRadio = radio;
                }
            }
        };
    }
}
