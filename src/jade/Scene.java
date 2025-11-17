package jade;

import java.util.ArrayList;
import java.util.List;

import renderer.Renderer;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected List<Particle> particles = new ArrayList<>();

    private boolean isRunning = false;


    public Scene(){

    }

    public void init(){
        
    }

    public void start(){
        for (GameObject gO : gameObjects){
            gO.start();
            this.renderer.add(gO);
        }
        isRunning = true;

    }

    public void addGameObjectToScene(GameObject gO){

        gameObjects.add(gO);

        if(isRunning){
            gO.start();
            this.renderer.add(gO);
        }

    }

    public Camera camera(){
        return this.camera;
    }


    public void update(float dt){
        
    }

}
