package main;

import entity.Entity;
import graphics.GUI.Launcher;
import graphics.Sprite;
import graphics.SpriteSheet;
import input.KeyInput;
import input.MouseInput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;



public class Main extends Canvas implements Runnable{

    public static final int WIDTH = 270;
    public static final int HEIGHT = WIDTH/14*10;
    public static final int SCALE = 4;
    public static final String TITLE = "Way To Ten";
    private Thread thread;
    private boolean running = false;
    public static BufferedImage[] levels;

    private BufferedImage darksoulsyoudied;
    private BufferedImage thankyouforplaying;

    public int secondscount;

    public static int level = 0;

    public static boolean presentationmode = true;

    public static int plus = 0;
    public static int lives = 2500;
    public static int deathScreenTime = 0;

    public static boolean showDeathScreen = true;
    public static boolean gameOver = false;
    public static boolean playing = false;

    private static BufferedImage background;
    private static BufferedImage background2;
    private static BufferedImage background3;

    public static Handler handler;
    public static SpriteSheet sheet;
    public static Camera cam;
    public static Launcher launcher;
    public static MouseInput mouse;

    public static Sprite groundblock;
    public static Sprite darkgroundblock;
    public static Sprite[] player;
    public static Sprite[] playerjump;
    public static Sprite pluses;
    public static Sprite[] enemie;
    public static Sprite[] enemieshell;
    public static Sprite[] flag;

    public static Sounds[] backgroundmusic;

    public static Sounds plusessound;
    public static Sounds gameover;
    public static Sounds jump;
    public static Sounds mariodies;
    public static Sounds oneup;
    public static Sounds stomp;
    public static Sounds mysteryblockbreak;

    public static void showDeathScreen() {
    }

    private synchronized void start() {
        if(running) return;
        running = true;
        thread = new Thread(this,"Thread");
        thread.start();
    }

    private synchronized void stop() {
        if(!running) return;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Main() {
        Dimension size = new Dimension(WIDTH*SCALE,HEIGHT*SCALE);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    private void init(){

        handler = new Handler();
        sheet = new SpriteSheet("/spritesheet.png");
        cam = new Camera();
        launcher = new Launcher();
        mouse = new MouseInput();

        addKeyListener(new KeyInput());
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        groundblock = new Sprite(sheet,1,1);
        darkgroundblock = new Sprite(sheet,1,2);
        pluses = new Sprite(sheet,5,1);

        player = new Sprite[8];
        playerjump = new Sprite[2];
        enemie = new Sprite[8];
        enemieshell = new Sprite[2];
        flag = new Sprite[2];

        levels = new BufferedImage[4];
        backgroundmusic = new Sounds[4];


        for(int i=0; i<player.length; i++){
            player[i] = new Sprite(sheet,i+2,16);
        }

        playerjump[0] = new Sprite(sheet,1,16); //влево
        playerjump[1] = new Sprite(sheet,10,16); //вправо


        for(int i=0; i<enemie.length; i++){
            enemie[i] = new Sprite(sheet,i+1,14);
        }
        //FLAG SPRITES
        for(int i=0; i<flag.length; i++){
            flag[i] = new Sprite(sheet,i+7,1);
        }

        try {
            levels[0] = ImageIO.read(getClass().getResource("/level1.png"));
            levels[1] = ImageIO.read(getClass().getResource("/level3.png"));
            levels[2] = ImageIO.read(getClass().getResource("/level2.png"));
            levels[3] = ImageIO.read(getClass().getResource("/levelthanks.png"));

            background = ImageIO.read(getClass().getResource("/background11.png"));
            background2 = ImageIO.read(getClass().getResource("/background12.png"));
            background3 = ImageIO.read(getClass().getResource("/background13.png"));
            darksoulsyoudied = ImageIO.read(getClass().getResource("/darksoulsyoudied.png"));
            thankyouforplaying = ImageIO.read(getClass().getResource("/thankyouscreen.png"));
        } catch (IOException e){
            e.printStackTrace();
        }

        backgroundmusic[0] = new Sounds("/audio/background.wav");
        backgroundmusic[1] = new Sounds("/audio/marioworldsubcastle.wav");
        backgroundmusic[2] = new Sounds("/audio/marioworldend.wav");
        backgroundmusic[3] = new Sounds("/audio/jump.wav");
        plusessound = new Sounds("/audio/pluses.wav");
        gameover = new Sounds("/audio/gameover.wav");
        jump = new Sounds("/audio/jump.wav");
        mariodies = new Sounds("/audio/marioDies.wav");
        oneup = new Sounds("/audio/oneUp.wav");
        stomp = new Sounds("/audio/stomp.wav");
        mysteryblockbreak = new Sounds("/audio/mysteryblockbreak.wav");

    }

    public void run(){

        init();
        requestFocus();
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0.0;
        double ns = 1000000000.0/60.0;
        int frames = 0;
        int ticks = 0;

        while(running) {
            long now = System.nanoTime();
            delta+=(now-lastTime)/ns;
            lastTime = now;
            while(delta>=1){
                tick();
                ticks++;
                delta--;
            }
            render();
            frames++;
            if(System.currentTimeMillis()-timer>1000){
                timer+=1000;
                System.out.println(frames + " fps " + ticks + "ticks ");
                frames = 0;
                ticks = 0;
                secondscount++;
            }
        }
        stop();
    }

    public void render(){

        BufferStrategy bs = getBufferStrategy();
            if(bs==null){
                createBufferStrategy(3);
                return;
            }
            Graphics g = bs.getDrawGraphics();

        if (showDeathScreen && playing) {
            if(!gameOver){
                g.setColor(new Color(0,0,0));
                g.fillRect(0,0,getWidth()-0,getHeight()-0);
                g.setColor(Color.WHITE);
                //show lives
                g.drawImage(Main.playerjump[0].getBufferedImage(),WIDTH*4/2-115,HEIGHT*4/2-30,60,60,null);
                g.setFont(new Font("Pixel NES",Font.PLAIN,45));
                g.drawString("x" + (lives/500), WIDTH*4/2-25, HEIGHT*4/2+20);
                g.drawString("LEVEL " + (level+1), WIDTH*4/2-150, HEIGHT*4/2-80);
                if(playing) g.translate(cam.getX(),cam.getY());


                if(level == 0) backgroundmusic[0].play();
                if(level == 1){
                    backgroundmusic[1].play();
                    backgroundmusic[0].stop();
                }
                if(level == 2){
                    backgroundmusic[2].play();
                    backgroundmusic[1].stop();
                }

            }
            else{
                g.drawImage(darksoulsyoudied, 0, 0, getWidth(), getHeight(), null);
                secondscount = 0;
                plus = 0;
                lives = 2500;
                Main.gameover.play();
                Main.backgroundmusic[level].stop();
                Timer timer = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) { ////////////////////я добавил вчера выход в лобби после смерти
                        playing = false;
                    }
                });
                timer.setRepeats(false);
                timer.start();
        }
        }
        else if(!playing) launcher.render(g);


            if(!showDeathScreen && playing){

                if(level == 0) g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
                if(level == 1) g.drawImage(background2, 0, 0, getWidth(), getHeight(), null);
                if(level == 2) g.drawImage(background3, 0, 0, getWidth(), getHeight(), null);
                if(level == 3) g.drawImage(background3, 0, 0, getWidth(), getHeight(), null);

                g.drawImage(Main.pluses.getBufferedImage(),25,25,60,60,null);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Pixel NES",Font.PLAIN,45));
                g.drawString(":" + plus/11, 80, 80);
                g.setFont(new Font("Pixel NES",Font.PLAIN,40));
                g.drawString("Time: " + secondscount, getWidth()/2-300, 70);
                g.setFont(new Font("Pixel NES",Font.PLAIN,40));
                g.drawString("Score: " + score(), getWidth()/2+20, 70);
                g.drawImage(Main.player[0].getBufferedImage(),getWidth()-172,23,60,60,null);
                g.setFont(new Font("Pixel NES",Font.PLAIN,45));
                g.drawString("x" + (lives/500), getWidth()-100, 80);
                if(playing) g.translate(cam.getX(),cam.getY());
                if(playing) handler.render(g);

            }
            g.dispose();
            bs.show();
        }

    public void tick(){
        if(playing) handler.tick();

        for(int i=0; i<handler.entity.size(); i++){
            Entity e = handler.entity.get(i);
            if(e.getId()==Id.player){
                if(!e.goingDownPipe) cam.tick(e);
            }
        }

        for(Entity e:handler.entity){
            if(e.getId()==Id.player){
                cam.tick(e);
            }
        }
        if(showDeathScreen) deathScreenTime++;
        if(deathScreenTime>=180) {
            deathScreenTime = 0;
            handler.clearLevel();
            handler.createLevel(levels[level]);
            showDeathScreen = false;
        }
    }

    public static int getFrameWidth(){
        return WIDTH*SCALE;
    }

    public static int getFrameHeight(){
        return HEIGHT*SCALE;
    }

    public static void switchLevel() {
        Main.level++;
        handler.clearLevel();
        handler.createLevel(levels[level]);
    }

    public static void main(String [] args){
        Main game = new Main();
        JFrame frame = new JFrame(TITLE);
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.start();
    }

    public int score(){
        int scorecalctest1 = plus/11*10-secondscount/10;
        return scorecalctest1;
    }

    }



