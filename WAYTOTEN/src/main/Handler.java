package main;

import entity.Entity;
import entity.mob.Enemie;
import entity.mob.Player;
import tile.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Handler {

    public LinkedList<Entity> entity = new LinkedList<>();
    public LinkedList<Tile> tile = new LinkedList<>();

    public void render(Graphics g){
        for(Entity en:entity){
            en.render(g);
        }
        for(Tile ti:tile){
            ti.render(g);
        }
    }

    public void tick() {
        for(int i=0;i<entity.size();i++){
            entity.get(i).tick();
        }
        for(Tile ti:tile){
            ti.tick();
        }
    }

    public void addEntity(Entity en){
        entity.add(en);
    }

    public void removeEntity(Entity en){
        entity.remove(en);
    }

    public void addTile(Tile ti){
        tile.add(ti);
    }

    public void removeTile(Tile ti){
        tile.remove(ti);
    }

    public void createLevel(BufferedImage level){
        int width = level.getWidth();
        int height = level.getHeight();

        for(int y=0;y<height;y++){
            for(int x = 0;x<width;x++){
                int pixel = level.getRGB(x,y);


                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if(red==0 && green==0 && blue==0) addTile(new Wall(x*64,y*64,64,64,true,Id.wall,this));

                if(red==0 && green==0 && blue==255) addEntity(new Player(x*64,y*64,64,64,false,Id.player,this));

                if(red==255 && green==250 && blue==0) addTile(new Pluses(x*64,y*64,64,64,true,Id.pluses,this));

                if(red==180 && green==250 && blue==180) addEntity(new Enemie(x*64,y*64,64,64,true,Id.enemie,this));

                if (red ==0 && green == 255 && blue ==0) addTile(new Flag(x*64,y*64,64,64*5,true,Id.flag,this));

            }
        }
    }

    public void clearLevel(){
        entity.clear();
        tile.clear();
    }
}
