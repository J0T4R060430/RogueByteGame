import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class StationaryEnemySprite implements DisplayableSprite {

    private final int DETECTIONDISTANCE = 1000000;
    private double centerX = 0;
    private double centerY = 0;
    private double height;
    private double width;
    private boolean dispose = false;
    private boolean right = false;
    private Image standingLeft, standingRight;
    private Random rand = new Random();
    private double distanceToByte;
    private int health = 0;
    private int fireTimer = rand.nextInt(1000) + 500;


    public StationaryEnemySprite() {
        this.updateHealth(50);
        this.centerX = rand.nextInt(850);
        if (rand.nextBoolean()) this.centerX *= -1;
        this.centerY = rand.nextInt(850);
        if (rand.nextBoolean()) this.centerY *= -1;
        this.centerX += 1600;
        this.centerY += 1300;
        try {
            this.standingLeft = ImageIO.read(new File("C:\\Users\\yihed\\Downloads\\RogueByteGame\\RogueByteGame\\RogueByte\\res/StationaryEnemy/Left.png"));
            this.standingRight = ImageIO.read(new File("C:\\Users\\yihed\\Downloads\\RogueByteGame\\RogueByteGame\\RogueByte\\res/StationaryEnemy/Right.png"));
        } catch (IOException e) {
        }
        this.height = this.standingLeft.getHeight(null) * 3;
        this.width = this.standingLeft.getWidth(null) * 3;
    }

    @Override
    public Image getImage() {
        return this.right ? this.standingRight : this.standingLeft;
    }

    @Override
    public boolean getVisible() {
        return true;
    }

    @Override
    public double getMinX() {
        return this.centerX - this.width / 2;
    }

    @Override
    public double getMaxX() {
        return this.centerX + this.width / 2;
    }

    @Override
    public double getMinY() {
        return this.centerY - this.height / 2;
    }

    @Override
    public double getMaxY() {
        return this.centerY + this.height / 2;
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
    public double getCenterY() {
        return this.centerY;
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
    public void setDirection(boolean right) {
        this.right = right;
    }

    @Override
    public void setDistanceToTarget(double d) {
        this.distanceToByte = d;
    }


    @Override
    public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
        if (this.distanceToByte > this.DETECTIONDISTANCE) {
            if (this.rand.nextInt(200) == 19) {
                this.right = this.right ? false : true;
            }
        }
        if (this.health <= 0) {
            this.setDispose(true);
        }

        this.fireTimer -= actual_delta_time;
        if (this.fireTimer <= 0) {
            this.fireTimer = rand.nextInt(1000) + 1000;
            this.shoot(universe);
        }


    }

    @Override
    public WeaponSprite getWeapon() {
        return null;
    }

    @Override
    public void updateHealth(int i) {
        this.health += i;
    }

    @Override
    public DisplayableSprite getHealthBar() {
        return null;
    }

    public void shoot(Universe universe) {
        EnemyBulletSprite Bullet = new EnemyBulletSprite(this.centerX, this.centerY,
                this.getAngle(universe) + rand.nextInt(5) - rand.nextInt(10), 14, "res/Bullets/EnemyBullet.png", 0, 15000, 1);
        universe.getSprites().add(Bullet);
    }

    public int getAngle(Universe universe) {
        if (universe.getPlayer1().getCloak()) {
            return rand.nextInt(360);
        }
        double x = universe.getPlayer1().getCenterX() - this.centerX;
        double y = -(universe.getPlayer1().getCenterY() - this.centerY);

        double fullAngle = Math.atan(Math.abs(y) / Math.abs(x));
        fullAngle = Math.toDegrees(fullAngle);

        if (x > 0 && y > 0) {
        } else if (x > 0 && y < 0) {
            fullAngle = 360 - fullAngle;
        } else if (x < 0 && y > 0) {
            fullAngle = 180 - fullAngle;
        } else if (x < 0 && y < 0) {
            fullAngle += 180;
        }


        return (int) fullAngle;
    }


}
