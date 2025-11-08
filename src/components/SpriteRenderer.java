package components;

import org.joml.Vector4f;

import jade.Component;

public class SpriteRenderer extends Component {

    Vector4f color;

    public SpriteRenderer(Vector4f f_color){
        this.color = f_color; 
    }

    public void start(){

    }
    
    public void update(float dt){

    }

    public Vector4f getColor(){
        return this.color;
    }
}
