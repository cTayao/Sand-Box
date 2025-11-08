package components;

import jade.Component;

public class SpriteRenderer extends Component {
    boolean running = false;

    public void start(){
        System.out.println("Sprite Renderer Starting");
    }
    
    public void update(float dt){
        if(!running){
            System.out.println("Updating Sprite Renderer");
            running = true;
        }
    }
}
