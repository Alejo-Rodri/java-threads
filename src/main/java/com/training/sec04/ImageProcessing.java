package com.training.sec04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//  Latency
public class ImageProcessing {
    private static final Logger log = LoggerFactory.getLogger(ImageProcessing.class);
    public static final String SOURCE_FILE = "src/main/resources/margaritas4k.jpg";
    public static final String DESTINATION_FILE = "src/main/resources/margaritas4k_result.jpg";

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        long startTime = System.currentTimeMillis();
        recolorMultiThreaded(originalImage, resultImage, 6);
        //recolorSingleThreaded(originalImage, resultImage);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);

        log.info("{}", duration);
    }

    public static void recolorMultiThreaded(
            BufferedImage originalImage,
            BufferedImage resultImage,
            int numberOfThreads
    ) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;

            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;

                recolorImage(originalImage, resultImage, leftCorner, topCorner, width, height);
            });

            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(
            BufferedImage originalImage,
            BufferedImage resultImage,
            int leftCorner,
            int topCorner,
            int width,
            int height
    ) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    public static void recolorPixel(
            BufferedImage originalImage,
            BufferedImage resultImage,
            int x,
            int y
    ) {
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed = red;
        int newGreen = green;
        int newBlue = blue;

        if (isShadeOfGray(red, green, blue)) {
            // to ensure we do not go above 255
            newRed = Math.min(255, red + 10);
            // to ensure we do not go below 0
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, newRGB, x, y);
    }

    public static void setRGB(BufferedImage image, int rgb, int x, int y) {
        image.getRaster().setDataElements(
                x,
                y,
                image.getColorModel().getDataElements(rgb, null)
        );
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 & Math.abs(red - blue) < 30 & Math.abs(green - blue) < 30;
    }

    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    // masks out the alpha, red and blue components
    // shifts the value 8 bits to the right because green is the second byte from the right
    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        // applies a bit mask on the pixel making all of it 0 except for the
        // rightmost byte which is the blue one
        return rgb & 0x000000FF;
    }
}
