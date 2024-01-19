public class ShotgunBullet extends BulletSprite {
    private static int offset = 80;

    public ShotgunBullet(double startX, double startY, int angleInDegrees) {
        super(startX + Math.sin(Math.toRadians(angleInDegrees)) * offset,
                startY - Math.cos(Math.toRadians(angleInDegrees)) * offset,
                Math.toRadians(angleInDegrees), 5, "res/Bullets/Blast.png", angleInDegrees, 100, 50, 8);
    }

}
