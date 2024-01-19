public class MovableSpriteMain {

    public static void main(String[] args) {
        MovableSpriteAnimation animation = new MovableSpriteAnimation();
        AnimationFrame frame = new AnimationFrame((Animation) animation);
        frame.start();
    }

}
