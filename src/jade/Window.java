package jade;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import util.Time;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
private int width, height;
    private String title;

    private long glfwWindow;

    public float r, g, b, a;

    private static Scene currentScene;

    private static Window window = null;

    private Window() {
        this.width = 960;
        this.height = 540;
        this.title = "Sand Box";
        r = 1;
        g = 1;
        b = 1;
        a = 1;

    }

    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;

            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;

            default:
            assert false : "Unkown Scene '" + newScene + "'";

        }

    }


    public static Window get(){
        if (Window.window == null){
            Window.window = new Window();
        }

        return Window.window;

    }

    public void run(){
        System.out.println("Hello LWJGL" + Version.getVersion());

        init();
        loop();

		//Free the window callbacks and destroy the window        
        glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);

		//Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();

    }

    public void init(){
        //Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Init GLFW Failed!");        
        }

        //Configure GLFW Hints
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Faild to create GLFW Window");

        }

        //event callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::cursor_position_callback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouse_button_callback);
        glfwSetScrollCallback(glfwWindow, MouseListener::scroll_callback);
         
        glfwSetKeyCallback(glfwWindow, KeyListener::key_callback);

        //Make openGL context current
        glfwMakeContextCurrent(glfwWindow);

        //enable vsync?
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(glfwWindow);


        GL.createCapabilities();

        changeScene(0);
    }
    
    public void loop(){
        float startTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = -1f;

        

        while (!glfwWindowShouldClose(glfwWindow)){
            //System.out.println("raw time: " + Time.getTime());

            //poll events; key, mouse events etc.
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);
            

            if (dt >= 0){
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);
            
            endTime = Time.getTime();
            dt = endTime - startTime;
            startTime = endTime;
            
        }

    }


    
}
