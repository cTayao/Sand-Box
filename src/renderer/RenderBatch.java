package renderer;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.*;

import components.SpriteRenderer;
import jade.Window;

public class RenderBatch {

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID, eboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int f_maxBatchSize){
        shader = new Shader("assets\\shaders\\default.glsl");
        shader.compile();
        this.sprites = new SpriteRenderer[f_maxBatchSize];
        this.maxBatchSize = f_maxBatchSize;

        //to hold 1 quad * maxBatch
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;


    }


    public void start(){
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

    
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }

    public void addSprite(SpriteRenderer spr){
        int index = this.numSprites;
        // System.out.println("This is the INDEX: "+ index);
        this.sprites[index] = spr;
        this.numSprites++;

        //ad properties to local vertices array
        localVertexProperties(index);

        if(numSprites >= maxBatchSize){
            this.hasRoom = false;
        }
    }

    private void localVertexProperties(int f_index){
        SpriteRenderer spr = this.sprites[f_index];

        int offset = f_index * 4 * VERTEX_SIZE;

        Vector4f color = spr.getColor(); 

        float xAdd = 1f;
        float yAdd = 1f;
        for(int i = 0; i < 4; i++){
            if (i == 1){
                yAdd = 0f;
            }
            else if (i == 2){
                xAdd = 0f;
            }
            else if (i == 3){
                yAdd = 1f;
            }

            //load position
            vertices[offset] = spr.gameObject.transform.position.x + (xAdd * spr.gameObject.transform.scale.x);
            vertices[offset + 1] = spr.gameObject.transform.position.y + (yAdd * spr.gameObject.transform.scale.y);
        
            //load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            //after loading move on to the next vertex
            offset += VERTEX_SIZE;
        
        }


    }


    public void render(){
        // for now rebuffer every frame?
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        //use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());
    
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6,  GL_UNSIGNED_INT, 0);

        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(vaoID);

        shader.detach();

    }

    private int[] generateIndices(){
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++){
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] f_elements, int f_index){
        int offsetArrayIndex = 6 * f_index;
        int offset = 4 * f_index;

        //Triangle 1 (bottom right)
        f_elements[offsetArrayIndex] = offset + 3;
        f_elements[offsetArrayIndex + 1] = offset + 2;
        f_elements[offsetArrayIndex + 2] = offset + 0;
        
        //Triangle 2 (top left)
        f_elements[offsetArrayIndex + 3] = offset + 0;
        f_elements[offsetArrayIndex + 4] = offset + 2;
        f_elements[offsetArrayIndex + 5] = offset + 1;

        
    }

}


