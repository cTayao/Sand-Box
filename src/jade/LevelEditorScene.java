package jade;

import java.awt.event.KeyEvent; 

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;



public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float changeDuration = 2.0f;

    @Override
    public void init(){
        this.camera = new Camera(new Vector2f());

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(300 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100f;
        float sizeY = totalHeight / 100f;

        for(int i = 0; i < 100; i++){
            for (int j = 0; j < 100; j++){
                float xPos = xOffset + (i * sizeX);
                float yPos = yOffset + (j * sizeY);

                GameObject gO = new GameObject("Obj" + i + " " + j, new Transform(new Vector2f(xPos,yPos), new Vector2f(sizeX / 1.5f, sizeY / 1.5f) ));
                gO.addComponent(new SpriteRenderer(new Vector4f(i / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(gO);
            }
        }

    }   


    public LevelEditorScene(){
        System.out.println("Inside Level Editor Scene");
        changingScene = false;
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

        
        System.out.println("FPS: " + (1f / dt));


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
