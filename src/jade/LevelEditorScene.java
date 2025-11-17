package jade;

import java.awt.event.KeyEvent; 

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;
import util.AssetPool;



public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float changeDuration = 2.0f;
    
    public LevelEditorScene(){
        System.out.println("Inside Level Editor Scene");
        changingScene = false;
    }

    GameObject bottom, top, left, right;
    
    float thickness = 20f;
    
    @Override
    public void init(){
        this.camera = new Camera(new Vector2f());

        // GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(200, 200)));
        // obj1.addComponent(new SpriteRenderer(new Vector4f(0,0,0,0)));
        // this.addGameObjectToScene(obj1);


        // Bottom border
        bottom = new GameObject("bottomBorder",
            new Transform(new Vector2f(0, 0), new Vector2f(1280, thickness)));
        bottom.addComponent(new SpriteRenderer(new Vector4f(0, 0, 0, 1))); // black
        this.addGameObjectToScene(bottom);

        // Top border
        top = new GameObject("topBorder",
            new Transform(new Vector2f(0, 672 - thickness), new Vector2f(1280, thickness)));
        top.addComponent(new SpriteRenderer(new Vector4f(0, 0, 0, 1))); // black
        this.addGameObjectToScene(top);

        // Left border
        left = new GameObject("leftBorder",
            new Transform(new Vector2f(0, 0), new Vector2f(thickness, 672)));
        left.addComponent(new SpriteRenderer(new Vector4f(0, 0, 0, 1))); // black
        this.addGameObjectToScene(left);

        // Right border
        right = new GameObject("rightBorder",
            new Transform(new Vector2f(1280 - thickness, 0), new Vector2f(thickness, 672)));
        right.addComponent(new SpriteRenderer(new Vector4f(0, 0, 0, 1))); // black
        this.addGameObjectToScene(right);
    
        for(Particle p : particles) {
            GameObject particleObj = new GameObject("particle",
            new Transform(new Vector2f(p.position.x - p.radius, p.position.y - p.radius), new Vector2f(p.radius * 2, p.radius * 2)));
            particleObj.addComponent(new SpriteRenderer(p.color));
            this.addGameObjectToScene(particleObj);
        }


        loadResources();

    }      

    private void loadResources(){
        AssetPool.getShader("assets\\shaders\\default.glsl");
    }

    public void spawnParticle(Vector2f position, float radius, Vector4f color) {
        
        // Create the visual GameObject for this particle
        GameObject particleObj = new GameObject("particle",
        new Transform(new Vector2f(position.x - radius, position.y - radius),
        new Vector2f(radius * 2, radius * 2)));
        particleObj.addComponent(new SpriteRenderer(color));
        
        Particle p = new Particle(position, radius, color, particleObj);
        
        this.addGameObjectToScene(particleObj); // add to renderer
        particles.add(p); // store in particle list
    }
    
    boolean ePressedLastFrame = false;
    @Override
    public void update(float dt) {
        // PRINT FPS
        // System.out.println("FPS: " + (1f / dt));

        float incX = dt * 100f;
        float incY = dt * 100f;

        //Camera Movement
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
        
        //Spawn Particle
        boolean ePressed = KeyListener.isKeyPressed(KeyEvent.VK_E);
        if(ePressed && !ePressedLastFrame){
            spawnParticle(new Vector2f(640, 600), 1f, new Vector4f(1, 1, 0, 1)); // yellow sand   
            System.out.println("Particles: " + particles.size());
        }
        ePressedLastFrame = ePressed;

        //Add Gracvity to Particles
        Vector2f gravity = new Vector2f(0, -1000f); // units/sec²

        for(Particle p : particles) {
            // Apply gravity
            p.velocity.add(new Vector2f(gravity).mul(dt));

            // Update position
            p.position.add(new Vector2f(p.velocity).mul(dt));

            // Boundary collision (use your borders)
            float Left   = left.transform.position.x + left.transform.scale.x + p.radius;
            float Right  = right.transform.position.x - p.radius;
            float Bottom = bottom.transform.position.y + bottom.transform.scale.y + p.radius;
            float Top    = top.transform.position.y - p.radius;

            if(p.position.x < Left)   { p.position.x = Left; p.velocity.x *= -0.5f; }
            if(p.position.x > Right)  { p.position.x = Right; p.velocity.x *= -0.5f; }
            if(p.position.y < Bottom) { p.position.y = Bottom; p.velocity.y *= -0.5f; }
            if(p.position.y > Top)    { p.position.y = Top; p.velocity.y *= -0.5f; }

            // Update the GameObject transform to match particle
            p.gameObject.transform.position.set(
                p.position.x - p.radius,
                p.position.y - p.radius
            );

            //System.out.println("Particle pos: " + p.gameObject.transform.position + " vel: " + p.velocity);

        }
        
        

        for (GameObject gO : this.gameObjects){
            gO.update(dt);
        }

        this.renderer.render();
        
    }

    
}
