package jade;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Particle {
    public Vector2f position;  // world position
    public Vector2f velocity;  // current velocity
    public float radius;       // size
    public Vector4f color;     // for rendering
    public GameObject gameObject;
    public boolean resting = false;

    public Particle(Vector2f position, float radius, Vector4f color, GameObject gO) {
        this.position = new Vector2f(position);
        this.velocity = new Vector2f(0, 0); // start still
        this.radius = radius;
        this.color = color;
        this.gameObject = gO;
    }




}

