package components;

import java.sql.Array;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import jade.Component;
import renderer.Texture;

public class SpriteRenderer extends Component {

    Vector4f color;
    private Vector2f[] texCoords = new Vector2f[4];
    private Texture texture;

    public SpriteRenderer(Vector4f f_color){
        this.color = f_color; 
        this.texture = null;
    }

    public SpriteRenderer(Texture f_texture){
        this.color = new Vector4f(1,1,1,1);
        this.texture = f_texture;
    }

    public void start(){

    }
    
    public void update(float dt){

    }

    public Vector4f getColor(){
        return this.color;
    }

    public Texture getTexture(){
        return this.texture;
    }

    public Vector2f[] getTexCoords(){

        this.texCoords[0] = new Vector2f(1,1);
        this.texCoords[1] = new Vector2f(1,0);
        this.texCoords[2] = new Vector2f(0,0);
        this.texCoords[3] = new Vector2f(0,1);


        return this.texCoords;
    }
}
