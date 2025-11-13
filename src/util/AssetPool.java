package util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import renderer.Shader;
import renderer.Texture;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();

    public static Shader getShader(String resourceName){
        File file = new File(resourceName);

        if(shaders.containsKey((file.getAbsolutePath()))){
            return shaders.get(file.getAbsolutePath());
        } else{
            Shader newShader = new Shader(resourceName);
            newShader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), newShader);
            return newShader;
        }
        
    }

    public static Texture getTexture(String resourceName){
        
        File file = new File(resourceName);

        if(textures.containsKey(file.getAbsolutePath())){
            return textures.get(file.getAbsolutePath());
        } else {
            Texture newTexture = new Texture(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), newTexture);
            return newTexture;
        }

    }
    
}
