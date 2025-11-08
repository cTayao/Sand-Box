package jade;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();
    private boolean isRunning = false;


    public Scene(){

    }

    public void init(){
        
    }

    public void start(){
        for (GameObject gO : gameObjects){
            gO.start();
        }
        isRunning = true;

    }

    public void addGameObjectToScene(GameObject gO){

        gameObjects.add(gO);

        if(isRunning){
            gO.start();
        }

    }


    public void update(float dt){
        
    }

}
