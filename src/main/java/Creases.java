import processing.core.PApplet;
import processing.core.PVector;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class Creases extends PApplet {
    public static void main(String[] args) {
        PApplet.main(Creases.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        noFill();
        frameRate(-1);
    }

    @Override
    public void draw() {
        PVector p = new PVector(MARGIN + SKIP * (frameCount % (1 / SKIP * (width - 2 * MARGIN))),
                MARGIN + SKIP * floor(SKIP * frameCount / (width - 2 * MARGIN)));
        if (p.y > height - MARGIN) {
            noLoop();
            saveSketch(this);
        }
        for (int i = 0; i < PALETTE.length - 1; i++) {
            p = new PVector(MARGIN + SKIP * (frameCount % (1 / SKIP * (width - 2 * MARGIN))),
                    MARGIN + SKIP * floor(SKIP * frameCount / (width - 2 * MARGIN)));
            stroke(PALETTE[i].red(), PALETTE[i].green(), PALETTE[i].blue(), STROKE_ALPHA);
            float weight = map(i, 0, PALETTE.length - 1, MIN_WEIGHT, MAX_WEIGHT);
            p.add(PVector.fromAngle(random(PI)).mult(SKIP * randomGaussian()));
            for (int k = 0; k < MAX_ITERATIONS; k++) {
                float scale = map(p.x, MARGIN, width - MARGIN, MIN_SCALE, MAX_SCALE);
                float f = 1 - weight
                        + weight * sqrt(noise(map(p.x, MARGIN, width - MARGIN, 0, NOISE_MAP_FACTOR),
                        map(p.x, MARGIN, height - MARGIN, 0, NOISE_MAP_FACTOR)));
                PVector q = p.copy();
                p.x = f * p.x + (1 - f) * (MARGIN + noise(p.x * scale, p.y * scale) * (width - 2 * MARGIN));
                p.y = f * p.y + (1 - f) * (MARGIN + noise(p.x * scale, p.y * scale) * (height - 2 * MARGIN));
                line(p.x, p.y, q.x, q.y);

                if (sq(p.x - q.x) + sq(p.y - q.y) < 1) {
                    break;
                }
            }
        }

    }
}
