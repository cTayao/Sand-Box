package jade;

import java.awt.event.KeyEvent;

public class LevelScene extends Scene {
    private boolean changingScene = false;
    private float changeDuration = 2.0f;

    @Override
    public void init(){

    }


    public LevelScene(){
        System.out.println("Inside Level Scene");
        changingScene = false;
    }

    @Override
    public void update(float dt) {
        
        System.out.println("FPS: " + (1f / dt));

        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            changingScene = true;
        }

        if(changingScene && changeDuration > 0){
            changeDuration -= dt;
            Window.get().r += dt * 5f;
            Window.get().g += dt * 5f;
            Window.get().b += dt * 5f;
        }
        else if(changingScene){
            Window.changeScene(0);
        } 
        
    }
}
