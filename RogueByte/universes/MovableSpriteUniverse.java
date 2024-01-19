import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MovableSpriteUniverse implements Universe {

    public static int wave = 0;
    private final int DETECTIONDISTANCE = 1000000;
    private final double VELOCITY = 200;
    private boolean complete = false;
    private ArrayList<Background> backgrounds = null;
    private Background background = null;
    private DisplayableSprite Byte = null;
    private DisplayableSprite StationaryEnemy = null;
    private DisplayableSprite MovableEnemy = null;
    private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
    private ArrayList<DisplayableSprite> enemies = new ArrayList<DisplayableSprite>();
    private long elapsedTime = 0;
    private String status = "";
    private Random rand = new Random();
    private int stationaryEnemyCount = 0;
    private int ByteHealth = 19;
    private int dashCD = 0;
    private int spawnTime = 5000;
    private int enemyNumber = 6;
    private int spawnTimer = this.spawnTime;
    private DashSprite dash = null;
    //	//require a separate list for sprites to be removed to avoid a concurence exception
    private ArrayList<DisplayableSprite> disposalList = new ArrayList<DisplayableSprite>();
    private Potion[] potions = null;
    private int potionPointer = 2;
    private int dashCDReset = 0;


    public MovableSpriteUniverse() {
        this.setXCenter(0);
        this.setYCenter(0);
        //backgraounds
        background = new MappedBackground();
        ArrayList<DisplayableSprite> barriers = ((MappedBackground) background).getBarriers();
        backgrounds = new ArrayList<Background>();
        backgrounds.add(background);

        Scanner sc=new Scanner(System.in);
        System.out.println("Hello! Welcome to RogueByte, a game developed By Yihe and Elliot." + "\n"+
                "Here is a short starter guide to the Game:" +"\n"+
                "WASD  -> Move" +"\n"+
                "E     -> Use Potion to Restore All Health" +"\n"+
                "SPACE -> Fire" +"\n"+
                "Shift -> Dash" +"\n"+
                "K     -> Cloak" +"\n"+
                "At the beginning of each wave, enemies spawn in larger number. Your health is fully restored." +"\n"+
                "Good Luck and Have Fun!");
        System.out.println("\n"+"\n"+"\n"+"\n");
        System.out.println("Please Choose from the following Weapons." +"\n"+
                "Type 1 for Rifle   - 17 Dmg <> 030 Reload <> 100 Speed" +"\n"+
                "Type 2 for Shotgun - 50 Dmg <> 012 Reload <> 200 Speed" +"\n"+
                "Type 3 for Bazooka - 80 Dmg <> 005 Reload <> 120 Speed" +"\n"+
                "Type 4 for BoltGun - 03 Dmg <> 200 Reload <> 060 Speed" +"\n");
        int weaponChoise = sc.nextInt();
        String weapon = new String[] {"", "Rifle", "Shotgun", "RocketLauncher", "BoltPistol"} [weaponChoise];
        this.dashCDReset = new int[] {0, 800, 400, 600, 1250} [weaponChoise];


//create all of the sprites here
        Byte = new ByteSprite(this.ByteHealth, this, weapon, this.dashCDReset);
        sprites.add(Byte);
        sprites.add(Byte.getWeapon());
        sprites.add(Byte.getHealthBar());
        dash = new DashSprite();
        sprites.add(dash);

        Potion p1 = new Potion(-900), p2 = new Potion(-820), p3 = new Potion(-740);
        sprites.add(p1);
        sprites.add(p2);
        sprites.add(p3);
        this.potions = new Potion[]{p1, p2, p3};

        MovableSprite movable = (MovableSprite) Byte;
        movable.setCenterX(1600);
        movable.setCenterY(1300);
        sprites.addAll(barriers);





//	StationaryEnemy = new StationaryEnemySprite();
//	sprites.add(StationaryEnemy);


        //menus
        MenuSprite CDBar = new CDBar();
        sprites.add(CDBar);
        MenuSprite HPBar = new HPBar();
        sprites.add(HPBar);


        //

    }

    public  int getWave() {
        return this.wave;
    }

    public double getScale() {
        return 1;
    }

    public double getXCenter() {
        return this.Byte.getCenterX();
    }

    public void setXCenter(double xCenter) {
    }

    public double getYCenter() {
        return this.Byte.getCenterY();
    }

    public void setYCenter(double yCenter) {
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
    }

    public ArrayList<Background> getBackgrounds() {
        return backgrounds;
    }

    public DisplayableSprite getPlayer1() {
        return Byte;
    }

//	public boolean centerOnPlayer() {
//		return true;
//	}		

    public ArrayList<DisplayableSprite> getSprites() {
        return sprites;
    }

    public void update(KeyboardInput keyboard, long actual_delta_time) {
        if (keyboard.keyDown(16)) {
            if (this.dashCD <= 0) {
                this.dashCD = this.dashCDReset;
            }
        }


        for (int i = 0; i < sprites.size(); i++) {
            DisplayableSprite sprite = sprites.get(i);
            if (sprite.getDispose()) {
                sprites.remove(i);
            }
            sprite.update(this, keyboard, actual_delta_time);
        }


        //directions

        double d = 0;
        double min = Integer.MAX_VALUE;
        double cX = this.getPlayer1().getCenterX(), cY = this.getPlayer1().getCenterY();
        double maxX = 0, maxY = 0;
        for (DisplayableSprite s : this.getSprites()) {
            if (s instanceof StationaryEnemySprite || s instanceof MovableEnemySprite) {
                d = Math.pow(cX - s.getCenterX(), 2) +
                        Math.pow(cY - s.getCenterY(), 2);
                s.setDistanceToTarget(d);
                if (d < this.DETECTIONDISTANCE) {
                    s.setDirection(cX > s.getCenterX());
                }
                if (d < min) {
                    min = d;
                    maxX = s.getCenterX();
                    maxY = s.getCenterY();
                }
            }
        }
        this.getPlayer1().setDistanceToTarget(min);


        if (min < this.DETECTIONDISTANCE) {
            this.getPlayer1().setDirection(maxX > cX);
            this.getPlayer1().getWeapon().setDirection(true);

            String fp = String.format("%03d%03d", (int) Math.abs(maxX - cX), (int) Math.abs(maxY - cY - 10));

            double encoded = 0;
            if (maxX > cX && maxY > cY) {
                encoded = 11;
            } else if (maxX > cX && maxY < cY) {
                encoded = 10;
            } else if (maxX < cX && maxY < cY) {
                encoded = 0;
            } else {
                encoded = 1;
            }
            this.getPlayer1().getWeapon().setDistanceToTarget(Integer.parseInt(fp) * 100 + encoded);
        } else {
            this.getPlayer1().getWeapon().setDirection(false);
        }

        //menu items
//		this.Byte.updateHealth(this.ByteHealth);


        //do all of the bullet calculation in here, and the cooldowns

        for (DisplayableSprite s : this.sprites) {
            if (s instanceof BulletSprite) {
                for (DisplayableSprite e : this.sprites) {
                    if (CollisionDetection.overlaps(s, e)) {
                        if(e instanceof StationaryEnemySprite){
                            e.updateHealth(-Integer.parseInt(s.toString()));
                        }else if (e instanceof EnemyBulletSprite){
                            e.setDispose(true);
                        }
                    }
                }
            } else if (s instanceof EnemyBulletSprite) {
                if (CollisionDetection.overlaps(s, this.getPlayer1())) {
                    this.getPlayer1().updateHealth(-1);
                    s.setDispose(true);
                }
            }
        }


        if (keyboard.keyDownOnce(69) && this.potionPointer >= 0) {
            this.potions[this.potionPointer].setDispose(true);
            this.getPlayer1().updateHealth(19);
            this.potionPointer--;
        }


        this.spawnTimer -= actual_delta_time;
        if (this.spawnTimer <= 0) {
            this.spawnTime = this.getNextSpawnTime(this.spawnTime);
            this.spawnTimer = this.spawnTime;
            this.enemyNumber *= 1.8;
            this.spawn(this.enemyNumber, this.enemyNumber / 2);
            this.getPlayer1().updateHealth(19);
        }

        this.dash.setDistanceToTarget(910 + (this.dashCD) / 3);

        if (this.dashCD > 0) {
            this.dashCD -= Math.min(this.dashCD, actual_delta_time);
        }

    }

    public String toString() {
        return this.status;
    }

    public int getNextSpawnTime(int i) {
        return (int) (i * 1.5);
    }

    public void spawn(int max, int min) {
        this.stationaryEnemyCount = rand.nextInt(max - min) + min;
        for (int i = 0; i < this.stationaryEnemyCount; i++) {
            StationaryEnemy = new StationaryEnemySprite();
            sprites.add(StationaryEnemy);
            enemies.add(StationaryEnemy);
        }
        this.wave ++;

    }


}
