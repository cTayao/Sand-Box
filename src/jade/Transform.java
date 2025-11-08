package jade;

import org.joml.Vector2f;

public class Transform {
    public Vector2f position;
    public Vector2f scale;


    public Transform(){
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f f_position){
        init(f_position, new Vector2f());

    }
    public Transform(Vector2f f_position, Vector2f f_scale){
        init(f_position, f_scale);
    }

    public void init(Vector2f f_position, Vector2f f_scale){
        this.position = f_position;
        this.scale = f_scale;
    }
}
