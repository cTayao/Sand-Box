package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;

    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean pressed[] = new boolean[3];
    private boolean isDragging;


    private MouseListener(){
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    } 

    public static MouseListener get(){
        if(instance == null){
            instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void cursor_position_callback(long window, double xpos, double ypos)
    {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;

        //if this cursor position moved is called while a button is pressed
        //then; isDragging = true
        get().isDragging = get().pressed[0] || get().pressed[1] || get().pressed[2];
    }

    public static void mouse_button_callback(long window, int button, int action, int mods)
    {  
        if(action == GLFW_PRESS){
            if(button < get().pressed.length){
                get().pressed[button] = true;
            }

        }
        else if (action == GLFW_RELEASE){
            if(button < get().pressed.length){
                get().pressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void scroll_callback(long window, double xoffset, double yoffset)
    {
        get().scrollX = xoffset;
        get().scrollY = yoffset;

    }


    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;

    }

    public static float getX(){
        return (float)get().xPos;
    }
    
    public static float getY(){
        return (float)get().yPos;
    }
    
    public static float getScrollX(){
        return (float)get().scrollX;
    }
    
    public static float getScrollY(){
        return (float)get().scrollY;
    }
    
    public static boolean isDragging(){
        return get().isDragging;
    }
    
    public static boolean mouseButtonDown(int button){
        if(button < get().pressed.length){
            return get().pressed[button];
        }
        else{
            return false;
        }
    }
}
