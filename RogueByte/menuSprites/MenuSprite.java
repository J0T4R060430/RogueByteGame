import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuSprite implements DisplayableSprite {

    private Image image;
    private double centerX = 0;
    private double centerY = 0;
    private double width = 0;
    private double height = 0;
    private boolean dispose = false;
    private boolean visible = true;
    private int Xoff = 0, Yoff = 0;
    //    private String prefix = "C:\\Users\\____\\Downloads\\RogueByteGame\\RogueByteGame\\RogueByte\\";
    private String prefix = "";

    public MenuSprite(String imageURL, int Xoffset, int Yoffset, int multiplier) {
        try {
            this.image = ImageIO.read(new File(this.prefix+ imageURL));
            this.height = this.image.getHeight(null) * multiplier;
            this.width = this.image.getWidth(null) * multiplier;
        } catch (IOException e) {
        }
        this.Xoff = Xoffset;
        this.Yoff = Yoffset;
    }

    public void move(int deltaX, int deltaY) {
        this.centerX += deltaX;
        this.centerY += deltaY;
    }


    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public boolean getVisible() {
        return this.visible;
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
    public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
        this.centerX = universe.getPlayer1().getCenterX() + this.Xoff;
        this.centerY = universe.getPlayer1().getCenterY() + this.Yoff;

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
        this.Xoff = (int) d;
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
        // TODO Auto-generated method stub
        return null;
    }

}
