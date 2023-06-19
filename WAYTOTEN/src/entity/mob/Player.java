package entity.mob;

import entity.Entity;
import states.PlayerState;
import main.Handler;
import main.Id;
import main.Main;
import tile.Tile;

import java.awt.*;
import java.util.Random;

public class Player extends Entity {

    private PlayerState state;

    private Random random;

    private boolean animate = false;

    public Player(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
        super(x, y, width, height, solid, id, handler);

        state = PlayerState.SMALL;

        random = new Random();
    }

    public void render(Graphics g) {

        if (!jumping) {
            if (facing == 0) {//влево
                g.drawImage(Main.player[frame + 4].getBufferedImage(), x, y, width, height, null);
            } else if (facing == 1) {//вправо
                g.drawImage(Main.player[frame].getBufferedImage(), x, y, width, height, null);
            }
        } else if (jumping) {//влево
            if (facing == 0) {
                g.drawImage(Main.playerjump[1].getBufferedImage(), x, y, width, height, null);
            } else if (facing == 1) {//вправо
                g.drawImage(Main.playerjump[0].getBufferedImage(), x, y, width, height, null);
            }
        }
    }

    public void tick() {
        x += velX;
        y += velY;

        if (x <= 0) x = 0;
        if (velX != 0) animate = true;
        else animate = false;
        for (int a = 0; a < handler.tile.size(); a++) {
            Tile t = handler.tile.get(a);
            if (t.isSolid() && !goingDownPipe) {
                if (getBoundsTop().intersects(t.getBounds()) && t.getId() != Id.pluses) {
                    setVelY(0);
                    if (jumping && !goingDownPipe) {
                        jumping = false;
                        if (state == PlayerState.SMALL) gravity -= 0.2;
                        if (state == PlayerState.BIG) gravity -= 0.4;
                        falling = true;
                    }
                }
                if (getBoundsBottom().intersects(t.getBounds()) && t.getId() != Id.pluses) {
                    setVelY(0);
                    if (falling) falling = false;

                } else {
                    if (!falling && !jumping) {
                        gravity = 0.8;
                        falling = true;
                    }

                }
                if (getBoundsLeft().intersects(t.getBounds()) && t.getId() != Id.pluses) {
                    setVelX(0);
                    x = t.getX() + t.width;
                }
                if (getBoundsRight().intersects(t.getBounds()) && t.getId() != Id.pluses) {
                    setVelX(0);
                    x = t.getX() - t.width;
                }

                if (getBounds().intersects(t.getBounds())) {
                    if (t.getId() == Id.flag) {
                        Main.switchLevel();
                    }
                }
                //}
            }
            for (int i = 0; i < handler.entity.size(); i++) {
                Entity e = handler.entity.get(i);
                if (getBounds().intersects(t.getBounds()) && t.getId() == Id.pluses) {
                    Main.plusessound.play();
                    Main.plus = Main.plus + 1;
                    t.die();
                } else if (e.getId() == Id.enemie) {
                    if (getBoundsBottom().intersects(e.getBoundsTop())) {
                        e.die(0);
                        Main.stomp.play();
                    } else if (getBounds().intersects(e.getBounds())) {
                        if (state == PlayerState.BIG) {
                            state = PlayerState.SMALL;
                            width /= 2;
                            height /= 2;
                            x += width;
                            y += height;
                        } else if (state == PlayerState.SMALL) {
                            die(1);
                        }
                    }
                }
            }
        }

        if (jumping && !goingDownPipe) {
            if (state == PlayerState.SMALL) gravity -= 0.2;
            if (state == PlayerState.BIG) gravity -= 0.1;
            setVelY((int) -gravity);
            if (gravity <= 0.0) {
                jumping = false;
                falling = true;
            }
        }
        if (falling && !goingDownPipe) {
            gravity += 0.1;
            setVelY((int) gravity);
        }
        if (animate) {
            frameDelay++;
            if (frameDelay >= 3) {
                frame++;
                if (frame >= 4) {
                    frame = 0;
                }
                frameDelay = 0;
            }
        }
    }
}