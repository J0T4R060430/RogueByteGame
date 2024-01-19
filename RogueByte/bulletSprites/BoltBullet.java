public class BoltBullet extends BulletSprite{
    public BoltBullet(double startX, double startY, int angleInDegrees){
        super(startX, startY, Math.toRadians(angleInDegrees), 70, "res/Bullets/BoltBullet.png",angleInDegrees, 500, 3, 4);
    }
}
