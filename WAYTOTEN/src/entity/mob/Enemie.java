package entity.mob;

import entity.Entity;
import main.Handler;
import main.Id;
import main.Main;
import tile.Tile;

import java.awt.*;
import java.util.Random;

public class Enemie extends Entity {

    private Random random = new Random();

    public Enemie(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);

        int dir = random.nextInt(2);

        switch(dir){
            case 0:
                setVelX(-2);
                facing = 1;
                break;
            case 1:
                setVelX(2);
                facing = 0;
                break;
        }
    }

    public void render(Graphics g) {
        //влево
        if(facing==0){
            g.drawImage(Main.enemie[frame+5].getBufferedImage(),x,y,width,height,null);
        }
        //вправо
        else if(facing==1){
            g.drawImage(Main.enemie[frame].getBufferedImage(),x,y,width,height,null);
        }
    }

    public void tick(){
        x+=velX;
        y+=velY;
        for (Tile t : handler.tile) {
            if (!t.solid) break;
            if (t.isSolid()) {
                if (getBoundsBottom().intersects(t.getBounds())) {
                    setVelY(0);
                    if (falling) falling = false;

                }
                else {
                    if (!falling) {
                        gravity = 0.8;
                        falling = true;
                    }

                }
                if (getBoundsLeft().intersects(t.getBounds())) {
                    setVelX(5);
                    facing = 1;
                }
                if (getBoundsRight().intersects(t.getBounds())) {
                    setVelX(-5);
                    facing = 0;
                }
            }
        }
        if (falling) {
            gravity += 0.1;
            setVelY((int) gravity);
        }

        if(velX!=0){
            frameDelay++;
            if(frameDelay>=10){
                frame++;
                if(frame>=3){
                    frame = 0;
                }
                frameDelay = 0;
            }
        }
    }
}