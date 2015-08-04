package nl.robojan.real_pipboy.FalloutFont;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;


/**
 * Created by s120330 on 4-7-2015.
 */
public class FalloutGlyphLayout {
    public float height;
    public float width;
    Array<GlyphRun> runs;

    public FalloutGlyphLayout (FalloutFontData data, java.lang.CharSequence str) {
        createLayout(data, str, 0, str.length(), 0, Align.left, false, 1, 1);
    }

    public FalloutGlyphLayout (FalloutFontData data, java.lang.CharSequence str,
                               float scaleX, float scaleY) {
        createLayout(data, str, 0, str.length(), 0, Align.left, false, scaleX, scaleY);
    }

    public FalloutGlyphLayout (FalloutFontData data, java.lang.CharSequence str,
                               float targetWidth, int halign, boolean wrap,
                               float scaleX, float scaleY){
        createLayout(data, str, 0, str.length(), targetWidth, halign, wrap, scaleX, scaleY);
    }

    public FalloutGlyphLayout (FalloutFontData data, java.lang.CharSequence str, int start,
                               int end, float targetWidth, int halign, boolean wrap,
                               float scaleX, float scaleY) {
        createLayout(data, str, start, end, targetWidth, halign, wrap, scaleX, scaleY);
    }

    public void createLayout (FalloutFontData data, java.lang.CharSequence str, int start,
        int end, float targetWidth, int halign, boolean wrap,
        float scaleX, float scaleY) {

        float width = 0;
        int lines = 0;
        float x = 0;
        float y = 0;
        int runStart = start;
        runs = new Array<GlyphRun>();

        while (true) {
            int runEnd = -1;
            boolean newline = false;
            // Find the length of the run
            if (start == end) {
                if (runStart == end) // End of string, drawing is finished
                    break;
                runEnd = end; // The last drawing needs to be done
            } else {
                if (str.charAt(start++) == '\n') {
                    // End of Line
                    runEnd = start - 1;
                    newline = true;
                }
            }

            // Find the size of the run
            if (runEnd != -1) {
                if (runEnd != runStart) {
                    GlyphRun run = new GlyphRun();
                    runs.add(run);
                    getGlyphIndices(data, run, str, runStart, runEnd, scaleX);
                    run.x = x;
                    run.y = y;

                    for (int i = 0, n = run.indices.size; i < n; i++) {
                        int index = run.indices.get(i);
                        FalloutFontChar glyph = data.getGlyphInfo(index);
                        float xAdvance = run.xAdvances.get(i);
                        x += xAdvance;

                        // Do wrapping if necessary
                        if (wrap && x > targetWidth && i > 1 && x - xAdvance +
                                (glyph.leftSpace + glyph.charViewWidth) * scaleX > targetWidth) {
                            // Find the wrap index
                            int wrapIndex = getWrapIndex(run.indices, i - 1);
                            if ((run.x == 0 && wrapIndex == 0) || // Require at least one glyph per Line
                                    wrapIndex >= run.indices.size) {
                                wrapIndex = i - 1;
                            }
                            GlyphRun next = wrap(run, wrapIndex, i);
                            if (run.indices.size == 0)
                                runs.pop();
                            runs.add(next);

                            width = Math.max(width, run.x + run.width);
                            x = 0;
                            y += data.getLineHeight()*scaleY;
                            lines++;
                            next.x = 0;
                            next.y = y;
                            i = -1;
                            n = next.indices.size;
                            run = next;
                        } else {
                            run.width += xAdvance;
                        }
                    }
                }

                if (newline) {
                    width = Math.max(width, x);
                    x = 0;
                    y += data.getLineHeight()*scaleY;
                    lines++;
                }

                runStart = start;
            }
        }
        width = Math.max(width, x);

        if ((halign & Align.left) == 0) {
            boolean center = (halign & Align.center) != 0;
            float lineWidth = 0;
            float lineY = Integer.MIN_VALUE;
            int lineStart = 0;
            int n = runs.size;
            for (int i = 0; i < n; i++) {
                GlyphRun run = runs.get(i);
                if (run.y != lineY) {
                    lineY = run.y;
                    float shift = targetWidth - lineWidth;
                    if (center)
                        shift /= 2;
                    while (lineStart < i) {
                        runs.get(lineStart++).x += shift;
                    }
                    lineWidth = 0;
                }
                lineWidth += run.width;
            }
            float shift = targetWidth - lineWidth;
            if (center)
                shift /= 2;
            while (lineStart < n) {
                runs.get(lineStart++).x += shift;
            }
        }

        this.width = width;
        this.height = (data.getCapHeight() + data.getLineHeight()*lines)*scaleY;
    }

    private int getWrapIndex(IntArray glyphs, int start) {
        for(int i = start; i >= 1; i--) {
            int ch = glyphs.get(i);
            if(isWhitespace((char)ch))
                return i;
        }
        return 0;
    }

    public boolean isWhitespace(char c)
    {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }


    public GlyphRun wrap(GlyphRun first, int wrapIndex, int widthIndex)
    {
        GlyphRun second = new GlyphRun();
        int glyphCount = first.indices.size;

        // Find end of first run
        int endIndex = wrapIndex;
        for(; endIndex > 0; endIndex--)
        {
            if(!isWhitespace((char)first.indices.get(endIndex -1)))
                break;
        }

        // Determine start of second run
        int startIndex = wrapIndex;
        for(;startIndex < glyphCount; startIndex++)
        {
            if(!isWhitespace((char)first.indices.get(startIndex)))
                break;
        }

        // copy wrapped glyphs
        if(startIndex < glyphCount)
        {
            second.indices.addAll(first.indices, startIndex, glyphCount - startIndex);
            second.xAdvances.addAll(first.xAdvances, startIndex, glyphCount - startIndex);
        }

        while(widthIndex < endIndex)
        {
            first.width += first.xAdvances.get(widthIndex++);
        }

        while(widthIndex > endIndex)
        {
            first.width -= first.xAdvances.get(widthIndex--);
        }

        first.indices.truncate(endIndex);
        first.xAdvances.truncate(endIndex);

        return second;
    }

    public void getGlyphIndices(FalloutFontData data, GlyphRun run, java.lang.CharSequence str,
                                int start, int end, float scaleX)
    {
        int incr = 1;
        if(end < start)
            incr = -1;

        run.indices = new IntArray(Math.abs(end-start));
        for(int i = start; i != end; i+= incr)
        {
            int val = (int)str.charAt(i);
            run.indices.add(val);

            FalloutFontChar glyph = data.getGlyphInfo(val);
            run.xAdvances.add((glyph.leftSpace + glyph.rightSpace +
                    glyph.charViewWidth) * scaleX);
        }
    }


    public class GlyphRun
    {
        public IntArray indices = new IntArray();
        public FloatArray xAdvances = new FloatArray();
        public float x, y, width;

        public String toString() {
            StringBuilder buffer = new StringBuilder(indices.size);
            buffer.append("\"");
            for(int i = 0; i < indices.size; i++)
            {
                int index = indices.get(i);
                buffer.append((char)index);
            }
            buffer.append("\", ");
            buffer.append(x);
            buffer.append(", ");
            buffer.append(y);
            buffer.append(", ");
            buffer.append(width);
            return buffer.toString();
        }
    }

}
