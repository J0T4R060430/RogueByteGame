import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SimpleSprite implements DisplayableSprite {

    private static Image image;
    private final double VELOCITY = 200;
    private double centerX = 0;
    private double centerY = 0;
    private double width = 50;
    private double height = 50;
    private boolean dispose = false;

    public SimpleSprite(double centerX, double centerY, double height, double width) {
        this(centerX, centerY);

        this.height = height;
        this.width = width;
    }


    public SimpleSprite(double centerX, double centerY) {

        this.centerX = centerX;
        this.centerY = centerY;

        if (image == null) {
            try {
                image = ImageIO.read(new File("res/simple-sprite.png"));
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    public Image getImage() {
        return image;
    }

    //DISPLAYABLE

    public boolean getVisible() {
        return true;
    }

    public double getMinX() {
        return centerX - (width / 2);
    }

    public double getMaxX() {
        return centerX + (width / 2);
    }

    public double getMinY() {
        return centerY - (height / 2);
    }

    public double getMaxY() {
        return centerY + (height / 2);
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getCenterX() {
        return centerX;
    }

    ;

    public double getCenterY() {
        return centerY;
    }

    ;


    public boolean getDispose() {
        return dispose;
    }

    @Override
    public void setDispose(boolean dispose) {
        this.dispose = true;
    }

    public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {

    }

    @Override
    public boolean getCloak() {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void setDirection(boolean right) {
        // TODO Auto-generated method stub

    }


    @Override
    public void setDistanceToTarget(double d) {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateHealth(int i) {
        // TODO Auto-generated method stub

    }


    @Override
    public DisplayableSprite getWeapon() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DisplayableSprite getHealthBar() {
        return null;
    }

}
