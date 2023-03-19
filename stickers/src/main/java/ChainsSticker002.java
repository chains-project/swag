import processing.core.PApplet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import processing.core.PFont;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

/* reads all sha-256 checksums from a CycloneDX SBOM to generate an artwork
 * the CycloneDX SBOM is passed in the variable fileName
 */

public class ChainsSticker002 extends PApplet {

    String fileName = "bomJenkins.json";
    int shalen = 64; // length of a sha-256 in hex
    int bytelen = 16; // number of values in hex
    int offset = 276; // margin around the image
    // canvas is square, we add to bytelen to allow for 1 px space on the side of each element that represents one character in the sha
    int w = shalen * (bytelen + 2) + offset; 
    int h = w;
    float cx = w / 2;
    float cy = h / 2;
    float rad = w - offset;
    Random alea = new Random();
    HashMap<Character, Integer> maphex = new HashMap<Character, Integer>();
    Gson gson = new Gson();
    ArrayList<String> hashes = new ArrayList<>();
    int hashes_ind = 0;
    String sha;
    float angle = 0;
    double angle_step;

    @Override
    public void settings() {
        size(w, h);
    }

    @Override
    public void setup() {
        colorMode(HSB, 360, 100, 100);
        background(0, 0, 0);
        initMapHex();
        get_shas();
        angle_step = 180.0 / hashes.size();
    }


    @Override
    public void draw() {
        if (hashes_ind < hashes.size()) {
            drawshas();
            hashes_ind++;
        }
        else{
            noLoop();
            john("CHAINS", 191, false); //131
            rad += 80;
            john("https://chains.proj.kth.se", 62, true);
            save(fileName+".png");
        }
         
    }

    private void drawshas() {
        translate(w / 2, h / 2);
        noFill();
        stroke(0, 0, 100);
        strokeWeight(5);
        sha = hashes.get(hashes_ind);
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
                //line((x + 1 + off), 0, (x + 1 + +off + local), 0);
                ellipse(x+step/2,0,local,local);
                x += step;
            }
            angle += angle_step;
            System.out.println("::: angle: " + angle + "; hashes_ind: " + hashes_ind);
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

    /* finds all sha-256 checksums in fileName and stores them in the hashes Array */
    private void get_shas() {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            JsonElement sbom = JsonParser.parseReader(br);
            JsonObject sbomObj = sbom.getAsJsonObject();
            JsonArray components = sbomObj.get("components").getAsJsonArray();
            for (JsonElement c : components) {
                if (c.getAsJsonObject().get("hashes") != null) {
                    JsonArray hashcollection = c.getAsJsonObject().get("hashes").getAsJsonArray();
                    for (JsonElement h : hashcollection) {
                        if (h.getAsJsonObject().get("alg").getAsString().equals("SHA-256")) {
                            System.out.println(h.getAsJsonObject().get("content").getAsString());
                            hashes.add(h.getAsJsonObject().get("content").getAsString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        strokeWeight(17);
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
