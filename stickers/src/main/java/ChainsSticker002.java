import processing.core.PApplet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import processing.core.PFont;

public class ChainsSticker002 extends PApplet {
    int shalen = 64; // length of a sha-256 in hex
    int bytelen = 16; // number of values in hex
    int offset = 176;
    int w = shalen * (bytelen + 2) + offset; // canvas is square, we add to bytelen to allow for 1 px space on the side
                                             // of each element that represents one character in the sha
    int h = w;
    float cx = w / 2;
    float cy = h / 2;
    float rad = w - offset;
    Random alea = new Random();
    HashMap<Character, Integer> maphex = new HashMap<Character, Integer>();
    ArrayList<String> hashes = new ArrayList<>();
    int hashes_ind = 0;
    float angle = 0;
    float angle_step;
    String sha;
    float x1 = -(w / 2 - 88);
    float x2 = w / 2 - 88;

    @Override
    public void settings() {
        size(w, h);
    }

    @Override
    public void setup() {
        colorMode(HSB, 360, 100, 100);
        initMapHex();
        background(0, 0, 0);
        hashes.add("21af30c92267bd6122c0e0b4d20cccb6641a37eaf956c6540ec471d584e64a7b");
        hashes.add("21af39c92267bd6122c0e0b4d23cdcb6641a37eaf956c6540ec471d584e64a7b");
        hashes.add("21af39c92267bd6142c0e0b4d23cdcb6641a3beaf956c6540ec471df84e64a7b");
        angle_step = 360/hashes.size();
    }

    @Override
    public void draw() {
        drawshas();
        /*
         * john("CHAINS", 131, false);
         * rad += 80;
         * john("https://chains.proj.kth.se", 62, true);
         * save("chains-sticker002.png");
         */
        // noLoop();
        if (frameCount == 84) {
            noLoop();
        }
    }

    private void drawshas() {
        translate(w / 2, h / 2);
        stroke(0, 0, 100);
        strokeWeight(3);
        if (hashes_ind < hashes.size()) {
            sha=hashes.get(hashes_ind);
            if (sha.length() == shalen) {
                int step = bytelen + 2;
                float x = -rad / 2;
                float y = h / 2;
                int local;
                float off;
                rotate(radians(angle));
                for (int i = 0; i < sha.length(); i++) {
                    local = maphex.get(sha.charAt(i));
                    if (local == 15) {
                        stroke(0, 100, 100);
                    } else {
                        stroke(0, 0, 100);
                    }
                    off = (step - local) / 2;
                    line((x + 1 + off), 0, (x + 1 + +off + local), 0);
                    System.out.println(i + "::: x: " + x + "; off: " + off + "; local: " + local);
                    x += step;
                }
                angle+=angle_step;
            }
            hashes_ind++;
        }
    }

    private void initMapHex() {
        maphex.put('0', 0);
        maphex.put('1', 1);
        maphex.put('2', 2);
        maphex.put('3', 3);
        maphex.put('4', 4);
        maphex.put('5', 5);
        maphex.put('6', 6);
        maphex.put('7', 7);
        maphex.put('8', 8);
        maphex.put('9', 9);
        maphex.put('a', 10);
        maphex.put('b', 11);
        maphex.put('c', 12);
        maphex.put('d', 13);
        maphex.put('e', 14);
        maphex.put('f', 15);
    }

    // https://processing.org/tutorials/text/#displaying-text-character-by-character
    private void john(String baldessari, int art, boolean opposite) {
        PFont f = createFont("FreeMono", art, true);// DejaVuSansMono
        textFont(f);
        // The text must be centered
        textAlign(CENTER);
        smooth();
        noFill();
        stroke(0, 0, 100);
        // We must keep track of our position along the curve
        float arclength = 0;
        // For every box
        for (int i = 0; i < baldessari.length(); i++) {
            // Instead of a constant width, we check the width of each character.
            char currentChar = baldessari.charAt(i);
            float tw = textWidth(currentChar) * 4;

            // Each box is centered so we move half the width
            arclength += tw / 2;
            // Angle in radians is the arclength divided by the radius
            // Starting on the left side of the circle by adding PI
            float theta;
            if (opposite) {
                theta = arclength / rad / 2;
            } else {
                theta = PI + arclength / rad / 2;
            }

            pushMatrix();
            // Polar to cartesian coordinate conversion
            translate(cx + rad / 2 * cos(theta), cy + rad / 2 * sin(theta));
            // Rotate the box
            rotate(theta + PI / 2); // rotation is offset by 90 degrees
            // Display the character
            fill(0, 0, 100);
            text(currentChar, 0, 0);
            popMatrix();
            // Move halfway again
            arclength += tw / 2;
        }
    }

    public static void main(String[] args) {
        String[] processingArgs = { "ChainsSticker002" };
        ChainsSticker002 mySketch = new ChainsSticker002();
        PApplet.runSketch(processingArgs, mySketch);
    }

}
