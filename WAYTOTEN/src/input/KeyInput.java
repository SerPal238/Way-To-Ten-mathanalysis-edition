package input;

import entity.Entity;
import main.Id;
import main.Main;
import states.LauncherState;
import tile.Tile;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static graphics.GUI.Launcher.launcherState;
import static main.Main.*;

public class KeyInput implements KeyListener  {

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        for(Entity en: Main.handler.entity){
            if(en.getId()== Id.player){
                if(en.goingDownPipe) return;

                switch(key){
                    case KeyEvent.VK_W:
                        for(int q=0; q < Main.handler.tile.size(); q++) {
                            Tile t = Main.handler.tile.get(q);
                            if (t.isSolid()) {
                                if (en.getBoundsBottom().intersects(t.getBounds())) {
                                    if (!en.jumping) {
                                        en.jumping = true;
                                        en.gravity = 10.0;

                                        Main.jump.play();
                                    }
                                }
                            }
                        }
                        break;

                    case KeyEvent.VK_A:
                        en.setVelX(-5);
                        en.facing = 0;
                        break;

                    case KeyEvent.VK_D:
                        en.setVelX(5);
                        en.facing = 1;
                        break;

                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;

                    case KeyEvent.VK_BACK_SPACE:
                        if(launcherState == LauncherState.CREDITS){
                            launcherState = LauncherState.BASE;
                        }
                        if(launcherState == LauncherState.THANKS){
                            launcherState = LauncherState.BASE;
                        }
                        break;


                    case KeyEvent.VK_U:
                        en.die(1);
                        break;

                    ///*
                    case KeyEvent.VK_P:

                        if(Main.presentationmode == true){
                            Main.presentationmode = false;
                            Main.oneup.play();
                        }
                        else if(Main.presentationmode == false){
                            Main.presentationmode = true;
                            plusessound.play();
                        }
                        break;
                    //*/
                    case KeyEvent.VK_I:
                        if (playing == true){
                            Main.backgroundmusic[Main.level].stop();
                            playing = false;
                            launcherState = LauncherState.BASE;
                            level = 0;
                        }
                        break;
                    case KeyEvent.VK_L:
                        switchLevel();
                        Main.backgroundmusic[Main.level].stop();
                        Main.level++;
                        handler.clearLevel();
                        handler.createLevel(Main.levels[level]);
                        Main.backgroundmusic[Main.level].play();
                        //Main.level=Main.level+1;
                        break;
                        //*/
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        for(Entity en: Main.handler.entity){
            if(en.getId()== Id.player){
                switch(key){
                    case KeyEvent.VK_W:
                        en.setVelY(0);
                        break;
                    case KeyEvent.VK_A:
                        en.setVelX(0);
                        break;
                    case KeyEvent.VK_D:
                        en.setVelX(0);
                        break;
                }
            }

        }
    }

    public void keyTyped(KeyEvent e) {
    }

}
