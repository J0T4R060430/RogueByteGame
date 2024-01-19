import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BulletSprite implements DisplayableSprite, MovableSprite {
    private double centerX = 0;
    private double centerY = 0;
    private double velocityX = 0;
    private double velocityY = 0;
    private double height;
    private double width;
    private boolean dispose = false;
    private Image image;
    private double deltaX, deltaY;
    private int lifetime, dmg;

    public BulletSprite(double startX, double startY, double angleInDegrees,
                        int speed, String imageURL, int tiltangle, int lifetime, int dmg, int size) {
        this.dmg = dmg;
        this.centerX = startX;
        this.centerY = startY;
        this.deltaX = Math.sin(angleInDegrees) * speed;
        this.deltaY = Math.cos(angleInDegrees) * speed;

        try {
            this.image = ImageIO.read(new File(imageURL));
            this.image = ImageRotator.rotate(this.image, tiltangle);
            this.height = this.image.getHeight(null) * size;
            this.width = this.image.getWidth(null) * size;
        } catch (IOException e) {
        }
        ;
        this.lifetime = lifetime;
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
        return this.image;
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
    public void setDirection(boolean right) {
    }

    @Override
    public void setDistanceToTarget(double d) {
    }

    @Override
    public void updateHealth(int i) {
    }

    @Override
    public DisplayableSprite getWeapon() {
        return null;
    }

    @Override
    public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
        this.centerX += this.deltaX;
        this.centerY -= this.deltaY;

        this.lifetime -= actual_delta_time;
        if (this.lifetime < 0) {
            this.dispose = true;
        }


    }


    @Override
    public DisplayableSprite getHealthBar() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return this.dmg + "";
    }


}
