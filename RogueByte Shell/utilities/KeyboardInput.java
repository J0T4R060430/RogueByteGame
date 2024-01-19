//retrieved from https://www.gamedev.net/articles/programming/general-and-gameplay-programming/java-games-keyboard-and-mouse-r2439/

//codes used are javascript char codes. See https://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

    public static final int KEY_BACKSPACE = 8;
    public static final int KEY_TAB = 9;
    public static final int KEY_ENTER = 13;
    public static final int KEY_SHIFT = 16;
    public static final int KEY_CTRL = 17;
    public static final int KEY_ALT = 18;
    public static final int KEY_PAUSE_BREAK = 19;
    public static final int KEY_CAPS_LOCK = 20;
    public static final int KEY_ESCAPE = 27;
    public static final int KEY_PAGE_UP = 33;
    public static final int KEY_PAGE_DOWN = 34;
    public static final int KEY_END = 35;
    public static final int KEY_HOME = 36;
    public static final int KEY_LEFT_ARROW = 37;
    public static final int KEY_UP_ARROW = 38;
    public static final int KEY_RIGHT_ARROW = 39;
    public static final int KEY_DOWN_ARROW = 40;
    public static final int KEY_INSERT = 45;
    public static final int KEY_DELETE = 46;
    public static final int KEY_0 = 48;
    public static final int KEY_1 = 49;
    public static final int KEY_2 = 50;
    public static final int KEY_3 = 51;
    public static final int KEY_4 = 52;
    public static final int KEY_5 = 53;
    public static final int KEY_6 = 54;
    public static final int KEY_7 = 55;
    public static final int KEY_8 = 56;
    public static final int KEY_9 = 57;
    public static final int KEY_A = 65;
    public static final int KEY_B = 66;
    public static final int KEY_C = 67;
    public static final int KEY_D = 68;
    public static final int KEY_E = 69;
    public static final int KEY_F = 70;
    public static final int KEY_G = 71;
    public static final int KEY_H = 72;
    public static final int KEY_I = 73;
    public static final int KEY_J = 74;
    public static final int KEY_K = 75;
    public static final int KEY_L = 76;
    public static final int KEY_M = 77;
    public static final int KEY_N = 78;
    public static final int KEY_O = 79;
    public static final int KEY_P = 80;
    public static final int KEY_Q = 81;
    public static final int KEY_R = 82;
    public static final int KEY_S = 83;
    public static final int KEY_T = 84;
    public static final int KEY_U = 85;
    public static final int KEY_V = 86;
    public static final int KEY_W = 87;
    public static final int KEY_X = 88;
    public static final int KEY_Y = 89;
    public static final int KEY_Z = 90;
    public static final int KEY_LEFT_WINDOW_KEY = 91;
    public static final int KEY_RIGHT_WINDOW_KEY = 92;
    public static final int KEY_SELECT_KEY = 93;
    public static final int KEY_NUMPAD_0 = 96;
    public static final int KEY_NUMPAD_1 = 97;
    public static final int KEY_NUMPAD_2 = 98;
    public static final int KEY_NUMPAD_3 = 99;
    public static final int KEY_NUMPAD_4 = 100;
    public static final int KEY_NUMPAD_5 = 101;
    public static final int KEY_NUMPAD_6 = 102;
    public static final int KEY_NUMPAD_7 = 103;
    public static final int KEY_NUMPAD_8 = 104;
    public static final int KEY_NUMPAD_9 = 105;
    public static final int KEY_MULTIPLY = 106;
    public static final int KEY_ADD = 107;
    public static final int KEY_SUBTRACT = 109;
    public static final int KEY_DECIMAL_POINT = 110;
    public static final int KEY_DIVIDE = 111;
    public static final int KEY_F1 = 112;
    public static final int KEY_F2 = 113;
    public static final int KEY_F3 = 114;
    public static final int KEY_F4 = 115;
    public static final int KEY_F5 = 116;
    public static final int KEY_F6 = 117;
    public static final int KEY_F7 = 118;
    public static final int KEY_F8 = 119;
    public static final int KEY_F9 = 120;
    public static final int KEY_F10 = 121;
    public static final int KEY_F11 = 122;
    public static final int KEY_F12 = 123;
    public static final int KEY_NUM_LOCK = 144;
    public static final int KEY_SCROLL_LOCK = 145;
    public static final int KEY_SEMI_COLON = 186;
    public static final int KEY_EQUAL_SIGN = 187;
    public static final int KEY_COMMA = 188;
    public static final int KEY_DASH = 189;
    public static final int KEY_PERIOD = 190;
    public static final int KEY_FORWARD_SLASH = 191;
    public static final int KEY_GRAVE_ACCENT = 192;
    public static final int KEY_OPEN_BRACKET = 219;
    public static final int KEY_BACK_SLASH = 220;
    public static final int KEY_CLOSE_BRAKET = 221;
    public static final int KEY_SINGLE_QUOTE = 222;
    private static final int KEY_COUNT = 256;
    // Current state of the keyboard
    private boolean[] currentKeys = null;
    // Polled keyboard state
    private KeyState[] keys = null;

    public KeyboardInput() {
        currentKeys = new boolean[KEY_COUNT];
        keys = new KeyState[KEY_COUNT];
        for (int i = 0; i < KEY_COUNT; ++i) {
            keys[i] = KeyState.RELEASED;
        }
    }

    public synchronized void poll() {
        for (int i = 0; i < KEY_COUNT; ++i) {
            // Set the key state
            if (currentKeys[i]) {
                // If the key is down now, but was not
                // down last frame, set it to ONCE,
                // otherwise, set it to PRESSED
                if (keys[i] == KeyState.RELEASED)
                    keys[i] = KeyState.ONCE;
                else
                    keys[i] = KeyState.PRESSED;
            } else {
                keys[i] = KeyState.RELEASED;
            }
        }
    }

    public boolean keyDown(int keyCode) {
        return keys[keyCode] == KeyState.ONCE ||
                keys[keyCode] == KeyState.PRESSED;
    }

    public boolean keyDownOnce(int keyCode) {
        return keys[keyCode] == KeyState.ONCE;
    }

    public synchronized void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < KEY_COUNT) {
            currentKeys[keyCode] = true;
        }
    }

    public synchronized void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < KEY_COUNT) {
            currentKeys[keyCode] = false;
        }
    }

    public void keyTyped(KeyEvent e) {
        // Not needed
    }

    private enum KeyState {
        RELEASED, // Not down
        PRESSED,  // Down, but not the first time
        ONCE      // Down for the first time
    }
}
