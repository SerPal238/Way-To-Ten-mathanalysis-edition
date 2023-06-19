package graphics.GUI;

import main.Main;
import states.LauncherState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static states.LauncherState.*;

public class Launcher {
    public graphics.GUI.Button[] buttons;
    public BufferedImage mainmenu1;
    public BufferedImage menubackgroundblock1;
    public BufferedImage thankyouforplaying;
    public static LauncherState launcherState;{
        try {
            mainmenu1 = ImageIO.read(getClass().getResource("/wwtmenu.png"));
            menubackgroundblock1 = ImageIO.read(getClass().getResource("/menubackgroundblock1.png"));
            thankyouforplaying = ImageIO.read(getClass().getResource("/thankyouscreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Launcher() {

        launcherState = LauncherState.BASE;

        buttons = new graphics.GUI.Button[3];

        buttons[0] = new graphics.GUI.Button(game.getFrameWidth() / 2 - 150, 430, 300, 40, "Play game");
        buttons[1] = new graphics.GUI.Button(game.getFrameWidth() / 2 - 150, 560, 300, 40, "Credits");
        buttons[2] = new Button(game.getFrameWidth() / 2 - 150, 480, 300, 40, "Exit game");

    }

    Main game = new Main();

    public void render(Graphics g) {
        g.drawImage(mainmenu1, 0, 0, game.getFrameWidth(), game.getFrameHeight(), null);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].render(g);
        }

        if (launcherState == CREDITS) {
            g.drawImage(menubackgroundblock1, game.getFrameWidth() / 6 - 20, game.getFrameHeight() / 6 - 90, game.getFrameWidth() - 3 * game.getFrameWidth() / 10, game.getFrameHeight() - 3 * game.getFrameHeight() / 10, null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Pixel NES", Font.BOLD, 45));
            g.drawString("Created by:", game.getFrameWidth() / 6 + 75, 120);
            g.setFont(new Font("Pixel NES", Font.PLAIN, 43));
            g.drawString("Sultan", game.getFrameWidth() / 6 + 75, 190);
            g.setFont(new Font("Pixel NES", Font.PLAIN, 40));
            g.drawString("Nikita", game.getFrameWidth() / 6 + 75, 260);
            g.drawString("SerPal", game.getFrameWidth() / 6 + 75, 330);
            g.setFont(new Font("Pixel NES", Font.PLAIN, 35));
            g.drawString("Press backspace to return", game.getFrameWidth() / 6 + 29, 520);
        }
    }
}

