package nl.robojan.real_pipboy.FalloutData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by s120330 on 12-7-2015.
 */
public class GameString {
    private static final String DEFAULTSTRINGS = "text/strings.txt";
    private static final String CUSTOMSTRINGS = "text/custom.txt";

    private static final String GAMESTRINGS = "strings.txt";

    private static Map<String, String> mStringMap =
            new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);


    public static void loadStrings() {
        FileHandle handle = Gdx.files.internal(DEFAULTSTRINGS);
        readStringsFile(handle);
        handle = Gdx.files.internal(CUSTOMSTRINGS);
        readStringsFile(handle);
        if(Gdx.files.isLocalStorageAvailable()) {
            handle = Gdx.files.local(GAMESTRINGS);
            if(handle.exists())
                readStringsFile(handle);
        }
    }

    public static void readStringsFile(FileHandle handle) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(handle.read(),
                    "UTF-8"));
            String line;
            while((line = reader.readLine()) != null)
            {
                line = line.trim();
                if(line.length() == 0 || line.startsWith("#"))
                    continue;
                int index = line.indexOf('=');
                if(index == -1){
                    Gdx.app.log("IO", "Line does not contain a valid declaration");
                    continue;
                }
                String value = line.substring(index + 1).trim();
                String key = line.substring(0, index).trim();
                if(key.indexOf(' ') != -1 || key.indexOf('\t') != -1) {
                    Gdx.app.log("IO", "Invalid key in declaration");
                    continue;
                }
                value = parseValue(value);
                mStringMap.put(key, value);
            }
            reader.close();
        } catch (UnsupportedEncodingException e) {
            Gdx.app.error("IO", "Could not read file \"" + handle.path() +
                    "\": " + e.getMessage());
        } catch (IOException e) {
            Gdx.app.error("IO", "Error while reading file \"" + handle.path() + "\": " +
                    e.getMessage());

        }

    }

    public static String parseValue(String value) {
        return value;
    }

    public static String getString(String id) {
        return getString(id, false);
    }

    public static String getString(String id, boolean force) {
        String result = mStringMap.get(id);
        if(result == null) {
            result = "Not found";
        }
        return result;
    }
}
