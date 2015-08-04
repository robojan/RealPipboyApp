package nl.robojan.real_pipboy.PipBoy.Data;

import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.FalloutData.Note;
import nl.robojan.real_pipboy.FalloutData.Quest;
import nl.robojan.real_pipboy.FalloutData.QuestObjective;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.ListBox;
import nl.robojan.real_pipboy.util.GameFileResolver;

/**
 * Created by s120330 on 28-7-2015.
 */
public class DataRect extends Control {

    private float mWidth;
    private float mHeight;

    private DataTextRect mDataText;
    private Image mDataImage;
    private DataAudioInfo mDataAudioInfo;
    private ListBox mDataQuestList;

    private Note mNote = null;

    public DataRect(float x, float y, float width, float height) {
        super(x, y);
        mWidth = width;
        mHeight = height;

        mDataText = new DataTextRect(-17, 20, width, 360);
        mDataImage = new Image(width - 360, 20, null, 360, 360);
        mDataAudioInfo = new DataAudioInfo(0, 20, width, 200);
        mDataQuestList = new ListBox(width - 400, 0, 400, 360,true);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    public void displayNote(Note note) {
        mNote = note;
        hideAll();
        if(note == null) {
            return;
        }

        switch(note.getType()) {
        case Note.TYPE_VOICE:
        case Note.TYPE_SOUND:
            addChild(mDataAudioInfo, true);
            break;
        case Note.TYPE_TEXT:
            mDataText.setText(note.getContent());
            addChild(mDataText, true);
            break;
        case Note.TYPE_IMAGE:
            mDataImage.setFile(GameFileResolver.combineWithFallback(note.getContent(),
                    Constants.FNF_IMAGE));
            addChild(mDataImage, true);
            break;
        }
    }

    public void displayQuest(Quest quest) {
        hideAll();
        if(quest == null) {
            return;
        }

        mDataQuestList.clearItems();
        for(QuestObjective objective : quest.getObjectives())
        {
            if(!objective.isDisplayed())
                continue;
            mDataQuestList.addItem(new DataItemMarker(0,0, objective.getText(),
                    objective.isCompleted(), !objective.isCompleted(), true));
        }
        addChild(mDataQuestList);
    }

    public void hideAll() {
        if(containsChild(mDataText))
            removeChild(mDataText);
        if(containsChild(mDataImage))
            removeChild(mDataImage);
        if(containsChild(mDataAudioInfo))
            removeChild(mDataAudioInfo);
        if(containsChild(mDataQuestList))
            removeChild(mDataQuestList);
    }
}
