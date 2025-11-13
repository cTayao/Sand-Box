package renderer;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.*;

import components.SpriteRenderer;
import jade.Window;
import util.AssetPool;

public class RenderBatch {
    // Vertex Pos        Color                           Tex Coords          Tex Id
    // float, float,     float, float, float, float,     float, float,       float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    
    
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;

    //vertex stride size
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0,1,2,3,4,5,6,7};
    
    private List<Texture> textures;
    private int vaoID, vboID, eboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int f_maxBatchSize){
        //DEBUG: System.out.println("Creating new batch here");
        shader = AssetPool.getShader("assets\\shaders\\default.glsl");
        shader.compile();

        //allocate room for sprites to render per batch?
        this.sprites = new SpriteRenderer[f_maxBatchSize];
        this.maxBatchSize = f_maxBatchSize;

        //to hold 1 quad * maxBatch
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();


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
        
        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);
        
        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    
    
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }

    public void addSprite(SpriteRenderer spr){
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        if(spr.getTexture() != null){
            if(!textures.contains(spr.getTexture())){
                textures.add(spr.getTexture());
            }
        }

        //add properties to local vertices array
        localVertexProperties(index);

        if(numSprites >= maxBatchSize){
            this.hasRoom = false;
        }
    }

    private void localVertexProperties(int f_index){
        SpriteRenderer spr = this.sprites[f_index];

        int offset = f_index * 4 * VERTEX_SIZE;

        Vector4f color = spr.getColor(); 
        Vector2f[]  texCoords = spr.getTexCoords();

        int texID = 0;

        if(spr.getTexture() != null){
            for(int i = 0; i < textures.size(); i++){
                if(textures.get(i) == spr.getTexture()){
                    texID = i + 1;
                    break;
                }
            }
        }

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
            
            //load texture coordinates
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            //load texture ID
            vertices[offset + 8] = texID;


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
    
        for(int i = 0; i < textures.size(); i++){
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }

        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6,  GL_UNSIGNED_INT, 0);


        for(int i = 0; i < textures.size(); i++){
            textures.get(i).unbind();
        }
        
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

    public boolean hasTextureRoom(){
        return this.textures.size() < 8;
    }

    public boolean hasTexture(Texture tex){
        return this.textures.contains(tex);
    }

}


