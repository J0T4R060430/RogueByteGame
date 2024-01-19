import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BarrierSprite implements DisplayableSprite {

    private static Image image;
    private boolean visible = true;
    private double centerX = 0;
    private double centerY = 0;
    private double width = 50;
    private double height = 50;
    private boolean dispose = false;

    public BarrierSprite() {

    }

    public BarrierSprite(double minX, double minY, double maxX, double maxY, boolean visible) {

        if (image == null && visible) {
            try {
                image = ImageIO.read(new File("res/blue-barrier.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.centerX = (minX + maxX) / 2;
        this.centerY = (minY + maxY) / 2;
        this.width = maxX - minX;
        this.height = maxY - minY;
        this.visible = visible;

    }


    public Image getImage() {
        return image;
    }

    public boolean getVisible() {
        return this.visible;
    }

    //DISPLAYABLE

    public void setVisible(boolean visible) {
        this.visible = visible;
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

    public void setDispose(boolean dispose) {
        this.dispose = dispose;
    }


    public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {

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
    public DisplayableSprite getHealthBar() {
        return null;
    }

}
