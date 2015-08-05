package nl.robojan.real_pipboy.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by s120330 on 20-7-2015.
 */
public class TextFileLoader extends AsynchronousAssetLoader<String, TextFileLoader.TextFileParameter> {

    private String mContent;

    public TextFileLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
                                                  TextFileParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file,
                          TextFileParameter parameter) {
        mContent = null;
        Charset encoding = Charset.defaultCharset();
        if(parameter != null && parameter.encoding != null) {
            encoding = parameter.encoding;
        }
        try {
            readContent(fileName, file, encoding);
        } catch(Exception e) {
            throw new GdxRuntimeException("Couldn't load file: " + file, e);
        }
    }

    @Override
    public String loadSync(AssetManager manager, String fileName, FileHandle file,
                           TextFileParameter parameter) {
        String content = this.mContent;
        this.mContent = null;
        return content;
    }

    private void readContent(String fileName, FileHandle file, Charset encoding) throws IOException{
        byte[] encoded = file.readBytes();
        mContent = new String(encoded, encoding);
    }

    static public class TextFileParameter extends AssetLoaderParameters<String> {
        Charset encoding;
    }
}
