package nl.robojan.real_pipboy.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

/**
 * Created by s120330 on 20-7-2015.
 */
public class ShaderProgramLoader extends AsynchronousAssetLoader<ShaderProgram,
        ShaderProgramLoader.ShaderProgramParameter> {
    public ShaderProgramLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file,
                          ShaderProgramParameter parameter) {
    }

    @Override
    public ShaderProgram loadSync(AssetManager manager, String fileName, FileHandle file,
                                  ShaderProgramParameter parameter) {
        String vertex, fragment;
        if(parameter == null) {
            vertex = manager.get(fileName + ".vert");
            fragment = manager.get(fileName + ".frag");
        } else {
            vertex = manager.get(parameter.vertexShader);
            fragment = manager.get(parameter.fragmentShader);
        }
        return new ShaderProgram(vertex, fragment);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
                                                  ShaderProgramParameter parameter) {
        Array<AssetDescriptor> deps = new Array();
        if(parameter == null) {
            deps.add(new AssetDescriptor(fileName + ".vert", String.class));
            deps.add(new AssetDescriptor(fileName + ".frag", String.class));
        } else {
            deps.add(new AssetDescriptor(parameter.vertexShader, String.class));
            deps.add(new AssetDescriptor(parameter.fragmentShader, String.class));
        }
        return deps;
    }

    static public class ShaderProgramParameter extends AssetLoaderParameters<ShaderProgram> {
        public String vertexShader = null;
        public String fragmentShader = null;

        public ShaderProgramParameter(String vertexShader, String fragmentShader) {
            this.vertexShader = vertexShader;
            this.fragmentShader = fragmentShader;
        }
    }
}
