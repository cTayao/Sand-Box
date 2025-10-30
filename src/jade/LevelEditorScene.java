package jade;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import renderer.Shader;
import util.Time;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;



public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float changeDuration = 2.0f;

    

    //VBO data?
    private float[] vertexArray = {
        //pos xyz                //color rgb
        100f,  100f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f,   //Top Right  
        100f, 0f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f,   //Bottom Right  
        0f, 0f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f,   //Bottom Left  
        0f,  100f, 0.0f,      1.0f, 0.0f, 1.0f, 1.0f,   //Top Left  
    };

    //EBO data?
    private int[] elementArray = {
        3, 1, 0, //Top right triangle
        1, 3, 2  //Botton left triangle

    };                
    
    private int vaoID, vboID, eboID;

    private Shader defaultShader;

    

    @Override
    public void init(){
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets\\shaders\\default.glsl");
        defaultShader.compile();
        
        //GENERATE VAO 
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create a float buffer for vertices (for vbo??)
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //GENERATE VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create the buffer for indices???
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        //GENERSTE EBO
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // add the vertex attribute pointers (to the VAO??)
        int positionSize = 3;
        int colorSize = 4;
        int sizeOfFloat = 4;
        int vertexSizeBytes = (positionSize + colorSize) * sizeOfFloat;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * sizeOfFloat);
        glEnableVertexAttribArray(1);
    }


    public LevelEditorScene(){
        System.out.println("Inside Level Editor Scene");
        changingScene = false;
    }

    
    @Override
    public void update(float dt) {
        float incX = dt * 100f;
        float incY = dt * 100f;
        int key = 0;

        if(KeyListener.isKeyPressed(KeyEvent.VK_W)){
            camera.position.y += incY;
        }
        if(KeyListener.isKeyPressed(KeyEvent.VK_A)){
            camera.position.x -= incX;
        }
        if(KeyListener.isKeyPressed(KeyEvent.VK_S)){
            camera.position.y -= incY;
        }
        if(KeyListener.isKeyPressed(KeyEvent.VK_D)){
            camera.position.x += incX;
        }

        // camera.position.x -= incX;
        // camera.position.y -= incY;

        
        System.out.println("FPS: " + (1f / dt));

        //bind shader program
        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        //bind vao were using
        glBindVertexArray(vaoID);

        //enable vertex attrib pointers (of vao??)
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);


        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        defaultShader.detach();

        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            changingScene = true;
        }

        if(changingScene && changeDuration > 0){
            changeDuration -= dt;
            Window.get().r -= dt * 5f;
            Window.get().g -= dt * 5f;
            Window.get().b -= dt * 5f;
        }
        else if(changingScene){
            Window.changeScene(1);
        }  
        
    }

    
}
