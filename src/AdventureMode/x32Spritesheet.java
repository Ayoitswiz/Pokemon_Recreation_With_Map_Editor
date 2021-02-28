package AdventureMode;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

public interface x32Spritesheet {

/*    x32Spritesheet x = new x32Spritesheet() {


        @Override
        public void createSpriteSheetImg(x32Spritesheet x) throws IOException {

        }

        @Override
        public void createSprites(BufferedImage img) throws IOException {

        }
    };*/
    void createSpriteSheetImg() throws IOException;

    void createSprites(BufferedImage img) throws IOException;


}
