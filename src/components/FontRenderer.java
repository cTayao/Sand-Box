package components;

import jade.Component;

public class FontRenderer extends Component{
    boolean running = false;

    public void start(){
        System.out.println("Font Renderer Starting");

    }

    public void update(float dt){
        if(!running){
            System.out.println("Updating Sprite Renderer");
            running = true;
        }
    }
}
