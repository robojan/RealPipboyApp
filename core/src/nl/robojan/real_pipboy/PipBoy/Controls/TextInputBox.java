package nl.robojan.real_pipboy.PipBoy.Controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

import nl.robojan.real_pipboy.util.StringUtil;

/**
 * Created by s120330 on 16-7-2015.
 */
public class TextInputBox extends TextBox {
    private String mTitle, mText, mHint;
    private String mValidCharacters = null;

    public TextInputBox(float x, float y, String title, String text, String hint) {
        super(x, y, text);
        mTitle = title;
        mText = text;
        mHint = hint;
        create();
    }

    public TextInputBox(float x, float y, String title, String text, String hint, int align) {
        super(x, y, text, align);
        mTitle = title;
        mText = text;
        mHint = hint;
        create();
    }

    public TextInputBox(float x, float y, String title, String text, String hint, float horbuf,
                        float verbuf) {
        super(x, y, text, horbuf, verbuf);
        mTitle = title;
        mText = text;
        mHint = hint;
        create();
    }

    public TextInputBox(float x, float y, String title, String text, String hint, float fixedWidth,
                        float horbuf, float verbuf) {
        super(x, y, text, fixedWidth, horbuf, verbuf);
        mTitle = title;
        mText = text;
        mHint = hint;
        create();
    }

    public TextInputBox(float x, float y, String title, String text, String hint, float horbuf,
                        float verbuf, int align, Color color) {
        super(x, y, text, horbuf, verbuf, align, color);
        mTitle = title;
        mText = text;
        mHint = hint;
        create();
    }

    public TextInputBox(float x, float y, String title, String text, String hint, float fixedWidth,
                        float horbuf, float verbuf, int align, Color color) {
        super(x, y, text, fixedWidth, horbuf, verbuf, align, color);
        mTitle = title;
        mText = text;
        mHint = hint;
        create();
    }

    public TextInputBox(float x, float y, String title, String text, String hint, float horbuf,
                        float verbuf, int align, boolean glow, Color color) {
        super(x, y, text, horbuf, verbuf, align, glow, color);
        mTitle = title;
        mText = text;
        mHint = hint;
        create();
    }

    public TextInputBox(float x, float y, String title, String text, String hint, float fixedWidth,
                        float horbuf, float verbuf, int align, boolean glow, Color color) {
        super(x, y, text, fixedWidth, horbuf, verbuf, align, glow, color);
        mTitle = title;
        mText = text;
        mHint = hint;
        create();
    }

    private void create() {
        super.setBoxVisible(true);
        addClickableListener(mBoxClickListener);

    }

    public void setValidCharacters(String chars) {
        mValidCharacters = chars;
    }

    public String getValidCharacters() {
        return mValidCharacters;
    }

    private String removeInvalidChars(String str) {
        if(mValidCharacters == null)
            return str;
        String result = "";
        for(int i = 0; i<str.length(); i++){
            char c = str.charAt(i);
            if(mValidCharacters.contains(Character.toString(c)))
                result += c;
        }
        return result;
    }

    private ClickableListener mBoxClickListener = new ClickableListener() {
        @Override
        public void onClickableEvent(Control source, Object user) {
            Input.TextInputListener listener = new Input.TextInputListener() {
                @Override
                public void input(String text) {
                    text = text.trim();
                    text = removeInvalidChars(text);
                    setText(text);
                }

                @Override
                public void canceled() {

                }
            };

            Gdx.input.getTextInput(listener, mTitle, getText(), mHint);
        }
    };


}
