import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * This class represents the 'graphical user interface' or 'presentation' layer or 'frame'. Its job is to continuously
 * read input from the user (i.e. keyboard, mouse) and to render (draw) a universe or 'logical' layer. Also, it
 * continuously prompts the logical layer to update itself based on the number of milliseconds that have elapsed.
 *
 * The presentation layer generally does not try to affect the logical layer; most information
 * passes "upwards" from the logical layer to the presentation layer.
 */

public class AnimationFrame extends JFrame {

    final public static int FRAMES_PER_SECOND = 60;
    //	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//    public static double width = screenSize.getWidth();
//    public static double height = screenSize.getHeight();
    public static double width = 1360;
    final public static int SCREEN_WIDTH = (int) width;
    public static double height = 765;
    final public static int SCREEN_HEIGHT = (int) height;
    private static boolean stop = false;
    final long REFRESH_TIME = 1000 / FRAMES_PER_SECOND;    //MILLISECONDS
    //basic controls on interface... these are protected so that subclasses can access
    protected JPanel panel = null;
    protected JButton btnPauseRun;
    protected JLabel lblTop;
    protected JLabel lblBottom;
    protected long total_elapsed_time = 0;
    protected long lastRefreshTime = 0;
    protected long deltaTime = 0;
    protected boolean isPaused = false;
    protected KeyboardInput keyboard = new KeyboardInput();
    protected Universe universe = null;
    int universeLevel = 0;
    //These variables control where the screen is centered in relation to the logical center of universe.
    //Generally it makes sense to have these start at half screen width and height, so that the logical
    //center is rendered in the center of the screen. Changing them will 'pan' the screen.
    private int screenOffsetX = SCREEN_WIDTH / 2;
    private int screenOffsetY = SCREEN_HEIGHT / 2;
    private boolean SHOW_GRID = false;
    private boolean DISPLAY_TIMING = false;
    //scale at which to render the universe. When 1, each logical unit represents 1 pixel in both x and y dimension
    private double scale = 5;
    //point in universe on which the screen will center
    private double logicalCenterX = 0;
    private double logicalCenterY = 0;
    //local (and direct references to various objects in universe ... should reduce lag by avoiding dynamic lookup
    private Animation animation = null;
    private DisplayableSprite player1 = null;
    private ArrayList<DisplayableSprite> sprites = null;
    private ArrayList<Background> backgrounds = null;
    private Background background = null;

    /*
     * Much of the following constructor uses a library called Swing to create various graphical controls. You do not need
     * to modify this code to create an animation, but certainly many custom controls could be added.
     */
    public AnimationFrame(Animation animation) {

        super("");
        getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                thisContentPane_mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                thisContentPane_mouseReleased(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                contentPane_mouseExited(e);
            }
        });

        this.animation = animation;
        this.setVisible(true);
        this.setFocusable(true);
        this.setSize(SCREEN_WIDTH + 20, SCREEN_HEIGHT + 36);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                keyboard.keyPressed(arg0);
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                keyboard.keyReleased(arg0);
            }
        });
        getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                contentPane_mouseMoved(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                contentPane_mouseMoved(e);
            }
        });

        Container cp = getContentPane();
        cp.setBackground(Color.BLACK);
        cp.setLayout(null);

        panel = new DrawPanel();
        panel.setLayout(null);
        panel.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        getContentPane().add(panel, BorderLayout.CENTER);

//		btnPauseRun = new JButton("||");
//		btnPauseRun.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				btnPauseRun_mouseClicked(arg0);
//			}
//		});

//		btnPauseRun.setFont(new Font("Tahoma", Font.BOLD, 12));
//		btnPauseRun.setBounds(SCREEN_WIDTH - 64, 20, 48, 32);
//		btnPauseRun.setFocusable(false);
//		getContentPane().add(btnPauseRun);
//		getContentPane().setComponentZOrder(btnPauseRun, 0);

		lblTop = new JLabel("Wave # ");
		lblTop.setForeground(Color.MAGENTA);
		lblTop.setFont(new Font("Consolas", Font.BOLD, 36));
		lblTop.setBounds(600, 22, 500, 50);
		getContentPane().add(lblTop);
		getContentPane().setComponentZOrder(lblTop, 0);
//
//		lblBottom = new JLabel("Status");
//		lblBottom.setForeground(Color.WHITE);
//		lblBottom.setFont(new Font("Consolas", Font.BOLD, 30));
//		lblBottom.setBounds(16, SCREEN_HEIGHT - 30 - 16, SCREEN_WIDTH - 16, 36);
//		lblBottom.setHorizontalAlignment(SwingConstants.CENTER);
//		getContentPane().add(lblBottom);
//		getContentPane().setComponentZOrder(lblBottom, 0);

    }

    /*
     * The entry point into an Animation. The presentation (gui) and the logical layers should run on separate
     * threads. This allows the presentation layer to remain responsive to user input while the logical is updating
     * its state. The universe (a.k.a. logical) thread is created below.
     */
    public void start() {
        Thread thread = new Thread() {
            public void run() {
                animationLoop();
                System.out.println("run() complete");
            }
        };







        thread.start();
        //start the animation loop so that it can initialize at the same time as a title screen being visible
        //as it runs on a separate thread, it will execute asynchronously
        displayTitleScreen();

//        System.out.println("main() complete");

    }

    /*
     * You can add a title screen here using a JDialog or similar
     */
    protected void displayTitleScreen() {

    }

    /*
     * The animationLoop runs on the logical thread, and is only active when the universe needs to be
     * updated. There are actually two loops here. The outer loop cycles through all universes as provided
     * by the animation. Whenever a universe is 'complete', the animation is asked for the next universe;
     * if there is none, then the loop exits and this method terminates
     *
     * The inner loop attempts to update the universe regularly, whenever enough milliseconds have
     * elapsed to move to the next 'frame' (i.e. the refresh rate). Once the universe has updated itself,
     * the code then moves to a rendering phase where the universe is rendered to the gui and the
     * controls updated. These two steps may take several milliseconds, but hopefully no more than the refresh rate.
     * When the refresh has finished, the loop (and thus the thread) goes to sleep until the next
     * refresh time.
     */
    private void animationLoop() {

        lastRefreshTime = System.currentTimeMillis();

        universe = animation.getNextUniverse();
        universeLevel++;

        while (stop == false && universe != null) {

            sprites = universe.getSprites();
            player1 = universe.getPlayer1();
            backgrounds = universe.getBackgrounds();
            this.scale = 0.65;

            // main game loop
            while (stop == false && universe.isComplete() == false) {

                if (DISPLAY_TIMING == true)
                    System.out.println(String.format("animation loop: %10s @ %6d", "sleep", System.currentTimeMillis() % 1000000));

                //adapted from http://www.java-gaming.org/index.php?topic=24220.0
                long target_wake_time = System.currentTimeMillis() + REFRESH_TIME;
                //sleep until the next refresh time
                while (System.currentTimeMillis() < target_wake_time) {
                    //allow other threads (i.e. the Swing thread) to do its work
                    Thread.yield();

                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                    }

                }

                if (DISPLAY_TIMING == true)
                    System.out.println(String.format("animation loop: %10s @ %6d  (+%4d ms)", "wake", System.currentTimeMillis() % 1000000, System.currentTimeMillis() - lastRefreshTime));

                //track time that has elapsed since the last update, and note the refresh time
                deltaTime = (isPaused ? 0 : System.currentTimeMillis() - lastRefreshTime);
                lastRefreshTime = System.currentTimeMillis();
                total_elapsed_time += deltaTime;

                //read input
                keyboard.poll();
                handleKeyboardInput();

                //update logical
                universe.update(keyboard, deltaTime);
                if (DISPLAY_TIMING == true)
                    System.out.println(String.format("animation loop: %10s @ %6d  (+%4d ms)", "logic", System.currentTimeMillis() % 1000000, System.currentTimeMillis() - lastRefreshTime));

                //update interface
                updateControls(universe.getWave());
                this.logicalCenterX = universe.getXCenter();
                this.logicalCenterY = universe.getYCenter();

                this.repaint();

            }

            handleUniverseComplete();
            keyboard.poll();

        }

//        System.out.println("animation complete");
        System.out.println("THANK YOU FOR PLAYING OUR GAME!!!!!");
        AudioPlayer.setStopAll(true);
        dispose();

    }

    private void handleUniverseComplete() {
        universe = animation.getNextUniverse();
    }

    protected void updateControls(int wave) {

		this.lblTop.setText("Wave #" + wave);
//		this.lblBottom.setText(Integer.toString(universeLevel));
//		if (universe != null) {
//			this.lblBottom.setText(universe.toString());
//		}

    }

    protected void btnPauseRun_mouseClicked(MouseEvent arg0) {
        if (isPaused) {
            isPaused = false;
            this.btnPauseRun.setText("||");
        } else {
            isPaused = true;
            this.btnPauseRun.setText(">");
        }
    }

    private void handleKeyboardInput() {

        if (keyboard.keyDown(KeyboardInput.KEY_P) && !isPaused) {
            btnPauseRun_mouseClicked(null);
        }
        if (keyboard.keyDown(KeyboardInput.KEY_O) && isPaused) {
            btnPauseRun_mouseClicked(null);
        }
        if (keyboard.keyDown(KeyboardInput.KEY_F1)) {
            scale *= 1.01;
            contentPane_mouseMoved(null);
        }
        if (keyboard.keyDown(KeyboardInput.KEY_F2)) {
            scale /= 1.01;
            contentPane_mouseMoved(null);
        }

//		if (keyboard.keyDown(KeyboardInput.KEY_A)) {
//			screenOffsetX += 1;
//		}
//		if (keyboard.keyDown(KeyboardInput.KEY_D)) {
//			screenOffsetX -= 1;
//		}
//		if (keyboard.keyDown(KeyboardInput.KEY_S)) {
//			screenOffsetY += 1;
//		}
//		if (keyboard.keyDown(KeyboardInput.KEY_X)) {
//			screenOffsetY -= 1;
//		}
        if (keyboard.keyDownOnce(KeyboardInput.KEY_G)) {
            this.SHOW_GRID = !this.SHOW_GRID;
        }
        if (keyboard.keyDownOnce(KeyboardInput.KEY_T)) {
            this.DISPLAY_TIMING = !this.DISPLAY_TIMING;
        }
    }

    private int translateToScreenX(double logicalX) {
        return screenOffsetX + scaleLogicalX(logicalX - logicalCenterX);
    }

    private int scaleLogicalX(double logicalX) {
        return (int) Math.round(scale * logicalX);
    }

    private int translateToScreenY(double logicalY) {
        return screenOffsetY + scaleLogicalY(logicalY - logicalCenterY);
    }

    private int scaleLogicalY(double logicalY) {
        return (int) Math.round(scale * logicalY);
    }

    private double translateToLogicalX(int screenX) {
        int offset = screenX - screenOffsetX;
        return offset / scale;
    }

    private double translateToLogicalY(int screenY) {
        int offset = screenY - screenOffsetY;
        return offset / scale;
    }

    protected void contentPane_mouseMoved(MouseEvent e) {
        Point point = this.getContentPane().getMousePosition();
        if (point != null) {
            MouseInput.screenX = point.x;
            MouseInput.screenY = point.y;
            MouseInput.logicalX = translateToLogicalX(MouseInput.screenX);
            MouseInput.logicalY = translateToLogicalY(MouseInput.screenY);
        } else {
            MouseInput.screenX = -1;
            MouseInput.screenY = -1;
            MouseInput.logicalX = Double.NaN;
            MouseInput.logicalY = Double.NaN;
        }
    }

    protected void thisContentPane_mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            MouseInput.leftButtonDown = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            MouseInput.rightButtonDown = true;
        } else {
            //DO NOTHING
        }
    }

    protected void thisContentPane_mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            MouseInput.leftButtonDown = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            MouseInput.rightButtonDown = false;
        } else {
            //DO NOTHING
        }
    }

    protected void this_windowClosing(WindowEvent e) {
//        System.out.println("windowClosing()");
        stop = true;
        dispose();
    }

    protected void contentPane_mouseExited(MouseEvent e) {
        contentPane_mouseMoved(e);
    }

    /*
     * This method will run whenever the universe needs to be rendered. The animation loop calls it
     * by invoking the repaint() method.
     *
     * The work is reasonably simple. First, all backgrounds are rendered from "furthest" to "closest"
     * Then, all sprites are rendered in order. Observe that the logical coordinates are continuously
     * being translated to screen coordinates. Thus, how the universe is rendered is determined by
     * the gui, but what is being rendered is determined by the universe. In other words, a sprite may
     * be in a given logical location, but where it is rendered also depends on scale and camera placement
     */
    class DrawPanel extends JPanel {

        public void paintComponent(Graphics g) {
            if (universe == null) {
                return;
            }

            if (backgrounds != null) {
                for (Background background : backgrounds) {
                    paintBackground(g, background);
                }
            }

            if (sprites != null) {
                for (DisplayableSprite activeSprite : sprites) {
                    DisplayableSprite sprite = activeSprite;
                    if (sprite.getVisible()) {
                        if (sprite.getImage() != null) {
                            g.drawImage(sprite.getImage(), translateToScreenX(sprite.getMinX()), translateToScreenY(sprite.getMinY()), scaleLogicalX(sprite.getWidth()), scaleLogicalY(sprite.getHeight()), null);
                        } else {
                            g.setColor(Color.BLUE);
                            g.fillRect(translateToScreenX(sprite.getMinX()), translateToScreenY(sprite.getMinY()), scaleLogicalX(sprite.getWidth()), scaleLogicalY(sprite.getHeight()));
                        }
                    }
                }
            }

            if (SHOW_GRID) {
                for (int x = 0; x <= SCREEN_WIDTH; x += 50) {
                    if (x % 100 == 0) {
                        g.setColor(Color.GRAY);
                    } else {
                        g.setColor(Color.DARK_GRAY);
                    }
                    g.drawLine(x, 0, x, SCREEN_HEIGHT);
                }
                for (int y = 0; y <= SCREEN_HEIGHT; y += 50) {
                    if (y % 100 == 0) {
                        g.setColor(Color.GRAY);
                    } else {
                        g.setColor(Color.DARK_GRAY);
                    }
                    g.drawLine(0, y, SCREEN_WIDTH, y);
                }
            }

            if (DISPLAY_TIMING == true)
                System.out.println(String.format("animation loop: %10s @ %6d  (+%4d ms)", "interface", System.currentTimeMillis() % 1000000, System.currentTimeMillis() - lastRefreshTime));

        }

        /*
         * The algorithm for rendering a background may appear complex, but you can think of it as
         * 'tiling' the screen from top left to bottom right. Each time, the gui determines a screen coordinate
         * that has not yet been covered. It then asks the background (which is part of the universe) for the tile
         * that would cover the equivalent logical coordinate. This tile has height and width, which allows
         * the gui to draw the tile and to then move to the screen coordinate at the same minY and to the right of this tile.
         * Again, the background is asked for the tile that would cover this coordinate.
         * When eventually this coordinate is off the right hand edge of the screen, then move to the left of the screen
         * but below the previously drawn tile. Repeat until the entire panel is covered.
         */
        private void paintBackground(Graphics g, Background background) {

            if ((g == null) || (background == null)) {
                return;
            }

            //what tile covers the top-left corner?
            double logicalLeft = (logicalCenterX - (screenOffsetX / scale) - background.getShiftX());
            double logicalTop = (logicalCenterY - (screenOffsetY / scale) - background.getShiftY());

            int row = background.getRow((int) (logicalTop - background.getShiftY()));
            int col = background.getCol((int) (logicalLeft - background.getShiftX()));
            Tile tile = background.getTile(col, row);

            boolean rowDrawn = false;
            boolean screenDrawn = false;
            while (screenDrawn == false) {
                while (rowDrawn == false) {
                    tile = background.getTile(col, row);
                    if (tile.getWidth() <= 0 || tile.getHeight() <= 0) {
                        //no increase in width; will cause an infinite loop, so consider this screen to be done
                        g.setColor(Color.GRAY);
                        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                        rowDrawn = true;
                        screenDrawn = true;
                    } else {
                        Tile nextTile = background.getTile(col + 1, row + 1);
                        int width = translateToScreenX(nextTile.getMinX()) - translateToScreenX(tile.getMinX());
                        int height = translateToScreenY(nextTile.getMinY()) - translateToScreenY(tile.getMinY());
                        g.drawImage(tile.getImage(), translateToScreenX(tile.getMinX() + background.getShiftX()), translateToScreenY(tile.getMinY() + background.getShiftY()), width, height, null);
                    }
                    //does the RHE of this tile extend past the RHE of the visible area?
                    if (translateToScreenX(tile.getMinX() + background.getShiftX() + tile.getWidth()) > SCREEN_WIDTH || tile.isOutOfBounds()) {
                        rowDrawn = true;
                    } else {
                        col++;
                    }
                }
                //does the bottom edge of this tile extend past the bottom edge of the visible area?
                if (translateToScreenY(tile.getMinY() + background.getShiftY() + tile.getHeight()) > SCREEN_HEIGHT || tile.isOutOfBounds()) {
                    screenDrawn = true;
                } else {
                    col = background.getCol(logicalLeft);
                    row++;
                    rowDrawn = false;
                }
            }
        }
    }
}