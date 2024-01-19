import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class WeaponSprite implements DisplayableSprite, MovableSprite {
    private final int A = 65, W = 87, S = 83, D = 68, SHOOT = 32;
    private double centerX = 0;
    private double centerY = 0;
    private double velocityX = 0;
    private double velocityY = 0;
    private double height;
    private double width;
    private boolean dispose = false;
    private Image image, imageR;
    private Image[] images = new Image[8], fullImages = new Image[360];
    private double angle = 0, fullAngle = 0, angleInRadians = 0;
    private boolean r = false, u = false, inRange = false;
    private double x = 0, y = 0;
    private int RELOADTIME = 330;
    private int reload = 0;
    private String weapon;
    private int multiplier = 3;
    //    private String prefix = "C:\\Users\\yihed\\Downloads\\RogueByteGame\\RogueByteGame\\RogueByte\\";
    private String prefix = "";

    public WeaponSprite(String weapon) {
        this.weapon = weapon;
        if (this.weapon.equals("Rifle")) {
            this.RELOADTIME = 330;
        } else if (this.weapon.equals("RocketLauncher")) {
            this.RELOADTIME = 2000;
            this.multiplier = 6;
        } else if (this.weapon.equals("Bow")) {
            this.RELOADTIME = 500;
        } else if (this.weapon.equals("Shotgun")) {
            this.RELOADTIME = 800;
            this.multiplier = 4;
        } else if (this.weapon.equals("BoltPistol")) {
            this.RELOADTIME = 50;
        } else {
            this.RELOADTIME = 0;
        }


        try {
            this.image = ImageIO.read(new File(this.prefix + "res/Weapons/"+ weapon + ".png"));
            this.imageR = ImageIO.read(new File(this.prefix + "res/Weapons/" + weapon + "R.png"));
            this.height = this.image.getHeight(null) * this.multiplier;
            this.width = this.height;
        } catch (IOException e) {
        }
        for (int i = 0; i < 8; i++) {
            if (i >= 4) {
                images[i] = ImageRotator.rotate(imageR, (i - 4) * 45);
            } else {
                images[i] = ImageRotator.rotate(image, i * 45);
            }
        }
        for (int i = 0; i < 360; i++) {
            if (i >= 180) {
                fullImages[i] = ImageRotator.rotate(imageR, (i - 180));
            } else {
                fullImages[i] = ImageRotator.rotate(image, i);
            }
        }
    }

    @Override
    public void setVelocityX(double pixelsPerSecond) {
        this.velocityX = pixelsPerSecond;

    }

    @Override
    public void setVelocityY(double pixelsPerSecond) {
        this.velocityY = pixelsPerSecond;

    }

    @Override
    public Image getImage() {
        return this.fullImages[(int) fullAngle];

    }

    @Override
    public boolean getVisible() {
        return true;
    }

    @Override
    public double getMinX() {
        return this.getCenterX() - this.getWidth() / 2;
    }

    @Override
    public double getMaxX() {
        return this.getCenterX() + this.getWidth() / 2;

    }

    @Override
    public double getMinY() {
        return this.getCenterY() - this.getHeight() / 2;

    }

    @Override
    public double getMaxY() {
        return this.getCenterY() + this.getHeight() / 2;

    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getCenterX() {
        return this.centerX;
    }

    @Override
    public void setCenterX(double centerX) {
        this.centerX = centerX;

    }

    @Override
    public double getCenterY() {

        return this.centerY;
    }

    @Override
    public void setCenterY(double centerY) {
        this.centerY = centerY;

    }

    @Override
    public boolean getDispose() {

        return this.dispose;
    }

    @Override
    public void setDispose(boolean dispose) {
        this.dispose = dispose;

    }

    @Override
    public boolean getCloak() {
        return false;
    }

    @Override
    public void setDirection(boolean inrange) {
        this.inRange = inrange;

    }

    @Override
    public void setDistanceToTarget(double d) {
        int dd = (int) d;
        this.u = (dd % 10 == 0);
        dd /= 10;
        this.r = (dd % 10 == 1);
        dd /= 10;
        this.y = dd % 1000;
        dd /= 1000;
        this.x = dd % 1000;

        this.angleInRadians = Math.atan(x / y);
        this.fullAngle = Math.toDegrees(this.angleInRadians);

        if (!u) this.fullAngle = 180 - this.fullAngle;
        if (!r) this.fullAngle = 360 - this.fullAngle;

        if (this.fullAngle < 0) this.angle += 360;
        this.fullAngle %= 360;


    }

    @Override
    public DisplayableSprite getWeapon() {
        return null;
    }

    @Override
    public void updateHealth(int i) {
    }

    @Override
    public void update(Universe universe, KeyboardInput k, long actual_delta_time) {
        this.setCenterX(universe.getPlayer1().getCenterX());
        this.setCenterY(universe.getPlayer1().getCenterY() + 10);
        if (!this.inRange) {
            if (k.keyDown(W) && k.keyDown(D)) {
                this.fullAngle = 1 * 45;
            } else if (k.keyDown(D) && k.keyDown(S)) {
                this.fullAngle = 3 * 45;
            } else if (k.keyDown(S) && k.keyDown(A)) {
                this.fullAngle = 5 * 45;
            } else if (k.keyDown(A) && k.keyDown(W)) {
                this.fullAngle = 7 * 45;
            } else if (k.keyDown(W)) {
                this.fullAngle = 0 * 45;
            } else if (k.keyDown(D)) {
                this.fullAngle = 2 * 45;
            } else if (k.keyDown(S)) {
                this.fullAngle = 4 * 45;
            } else if (k.keyDown(A)) {
                this.fullAngle = 6 * 45;
            }
        }


        if (k.keyDown(SHOOT) && this.reload <= 0 && universe.getPlayer1().getCloak() == false) {
            this.reload = this.RELOADTIME;
            shoot(universe);
        }

        this.reload -= actual_delta_time;


    }


    public void shoot(Universe universe) {
        if (this.weapon.equals("Rifle")) {
            RifleBullet Bullet = new RifleBullet(this.centerX, this.centerY, (int) this.fullAngle);
            universe.getSprites().add(Bullet);
        } else if (this.weapon.equals("RocketLauncher")) {
            RocketBullet Bullet = new RocketBullet(this.centerX, this.centerY, (int) this.fullAngle);
            universe.getSprites().add(Bullet);
        } else if (this.weapon.equals("Shotgun")) {
            ShotgunBullet Bullet = new ShotgunBullet(this.centerX, this.centerY, (int) this.fullAngle);
            universe.getSprites().add(Bullet);
        } else if(this.weapon.equals("BoltPistol")){
            BoltBullet Bullet = new BoltBullet(this.centerX, this.centerY, (int) this.fullAngle);
            universe.getSprites().add(Bullet);
        }
    }

    @Override
    public DisplayableSprite getHealthBar() {
        // TODO Auto-generated method stub
        return null;
    }

}
