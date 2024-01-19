import java.util.ArrayList;

public class ShellUniverse implements Universe {

    private boolean complete = false;
    private DisplayableSprite player1 = null;
    private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
    private ArrayList<Background> backgrounds = new ArrayList<Background>();
    private ArrayList<DisplayableSprite> disposalList = new ArrayList<DisplayableSprite>();

    public ShellUniverse() {

        this.setXCenter(0);
        this.setYCenter(0);
        player1 = new SimpleSprite(0, 0);
        sprites.add(player1);

    }

    public double getScale() {
        return 1;
    }

    public double getXCenter() {
        return 0;
    }

    public void setXCenter(double xCenter) {
    }

    public double getYCenter() {
        return 0;
    }

    public void setYCenter(double yCenter) {
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        complete = true;
    }

    public ArrayList<Background> getBackgrounds() {
        return backgrounds;
    }

    public DisplayableSprite getPlayer1() {
        return player1;
    }

    public ArrayList<DisplayableSprite> getSprites() {
        return sprites;
    }

    public boolean centerOnPlayer() {
        return false;
    }

    public void update(KeyboardInput keyboard, long actual_delta_time) {

//		if (keyboard.keyDownOnce(27)) {
//			complete = true;
//			this.player1.setDispose(true);
//		}

        for (int i = 0; i < sprites.size(); i++) {
            DisplayableSprite sprite = sprites.get(i);
            sprite.update(this, keyboard, actual_delta_time);
        }

        disposeSprites();

    }

    @Override
    public int getWave() {
        return 0;
    }

    protected void disposeSprites() {

        //collect a list of sprites to dispose
        //this is done in a temporary list to avoid a concurrent modification exception
        for (int i = 0; i < sprites.size(); i++) {
            DisplayableSprite sprite = sprites.get(i);
            if (sprite.getDispose() == true) {
                disposalList.add(sprite);
            }
        }

        //go through the list of sprites to dispose
        //note that the sprites are being removed from the original list
        for (int i = 0; i < disposalList.size(); i++) {
            DisplayableSprite sprite = disposalList.get(i);
            sprites.remove(sprite);
        }

        //clear disposal list if necessary
        if (disposalList.size() > 0) {
            disposalList.clear();
        }
    }


    public String toString() {
        return "ShellUniverse";
    }

}
