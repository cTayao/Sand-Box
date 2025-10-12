package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.security.Key;

public class KeyListener {
    private static KeyListener instance;

    private boolean pressed[] = new boolean[350];

    private KeyListener(){

    }

    public static KeyListener get(){
        if (KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void key_callback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS){
            get().pressed[key] = true;
        
        }
        else if (action == GLFW_RELEASE){
            get().pressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode){
        return get().pressed[keyCode];
    }

}
