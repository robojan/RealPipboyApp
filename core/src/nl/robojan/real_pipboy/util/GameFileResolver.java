package nl.robojan.real_pipboy.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;

import nl.robojan.real_pipboy.Connection.ConnectionManager;
import nl.robojan.real_pipboy.Connection.FileTransferHandler;

/**
 * Created by s120330 on 19-7-2015.
 */
public class GameFileResolver implements FileHandleResolver {
    private boolean mDownloadFromPC = false;
    private HashMap<String, String> mExtensionReplacements = new HashMap<String,String>(1);
    public static final char SEPERATOR_CHAR = '\007'; // Will not be used in file names

    public GameFileResolver(boolean downloadFromPC) {
        mDownloadFromPC = downloadFromPC;
        addExtensionReplacement("dds", "png");
    }

    public void addExtensionReplacement(String from, String to) {
        mExtensionReplacements.put(from, to);
    }

    public String modifyExtension(String fileName) {
        // Try find file with modified extension
        String replacement = getExtensionModification(fileName);
        if(replacement != null) {
            return fileName + "." +
                    replacement;
        }
        return fileName;
    }

    public String getExtensionModification(String fileName) {
        String extension = StringUtil.getFileExtension(fileName);
        return mExtensionReplacements.get(extension);
    }

    public static String getFallbackFile(String fileName) {
        if(fileName == null) {
            return null;
        }
        int i = fileName.lastIndexOf(SEPERATOR_CHAR);
        if(i > 0) {
            return fileName.substring(i+1);
        }
        return null;
    }

    public static String getNormalFile(String fileName) {
        if(fileName == null) {
            return null;
        }
        int i = fileName.lastIndexOf(SEPERATOR_CHAR);
        if(i > 0) {
            return fileName.substring(0,i);
        }
        return fileName;
    }

    public static String combineWithFallback(String file, String fallback) {
        return combineWithFallback(file, fallback, false);
    }

    public static String combineWithFallback(String file, String fallback,
                                             boolean passThroughNull) {
        if(file == null) {
            if(passThroughNull){
                return null;
            }
            return fallback;
        }
        return file + SEPERATOR_CHAR + fallback;
    }

    public boolean exists(String fileName) {
        return Gdx.files.internal(fileName).exists() ||
                Gdx.files.local(fileName).exists();
    }

    public FileHandle resolveFromLocation(String filename) {
        if(Gdx.files.internal(filename).exists()) {
            return Gdx.files.internal(filename);
        } else if (Gdx.files.isLocalStorageAvailable() && Gdx.files.local(filename).exists()) {
            return Gdx.files.local(filename);
        }
        return null;
    }

    @Override
    public FileHandle resolve(String fileName) {
        String correctedFile = fileName.toLowerCase();
        correctedFile = correctedFile.replace('\\', '/');
        String normalName = getNormalFile(correctedFile);
        String fallback = getFallbackFile(correctedFile);

        // Normal fileName
        FileHandle result = resolveFromLocation(normalName);
        if(result != null) {
            return result;
        }

        // Extension replacement
        String modifiedFilename = modifyExtension(normalName);
        result = resolveFromLocation(modifiedFilename);
        if(result != null) {
            return result;
        }

        // If possible download from server
        if(mDownloadFromPC && ConnectionManager.getInstance().isConnected()) {
            FileTransferHandler fth = FileTransferHandler.getInstance();
            String modification = getExtensionModification(normalName);
            result = fth.getFileSync(normalName, modification == null);
            if(result != null) {
                return result;
            }
        }

        // Return fallback when available
        if(fallback != null) {
            return resolve(fallback);
        }

        return Gdx.files.internal(normalName);
    }
}
