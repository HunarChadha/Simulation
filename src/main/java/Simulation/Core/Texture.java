package Simulation.Core;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private static final int bytes = 4;
    List<Integer> TextureVbo = new ArrayList<>();

    public int loadTexture(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight() * 4];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * bytes);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();
        int TextureID = glGenTextures();
        //TextureVbo.add(TextureID);
        glBindTexture(GL_TEXTURE_2D, TextureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        return TextureID;
    }

    public BufferedImage loadImage(String filename) {
        String normalizedPath = filename.startsWith("/") ? filename : "/" + filename;
        InputStream is = getClass().getResourceAsStream(normalizedPath);
        if (is == null) {
            System.out.println("Cannot find this File: " + filename);
            return null;
        }
        try (InputStream stream = is) {
            return ImageIO.read(stream);
        } catch (IOException e) {
            System.out.println("Cannot find this File");
            e.printStackTrace();
            return null;
        }
    }


    public void cleanUp(int textID) {
        glDeleteTextures(textID);
    }
}

