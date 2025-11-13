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


    @Override
    public void init(){
        this.camera = new Camera(new Vector2f());

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(200, 200)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets\\images\\testImage.png")));
        this.addGameObjectToScene(obj1);
    
        loadResources();

    }      

    private void loadResources(){
        AssetPool.getShader("assets\\shaders\\default.glsl");
    }

    
    @Override
    public void update(float dt) {
        float incX = dt * 100f;
        float incY = dt * 100f;

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

        
        // System.out.println("FPS: " + (1f / dt));


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

        

        for (GameObject gO : this.gameObjects){
            gO.update(dt);
        }

        this.renderer.render();
        
    }

    
}
