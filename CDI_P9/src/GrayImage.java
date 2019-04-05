
import java.awt.image.*;
import java.io.File;
import javax.imageio.*;

class GrayImage {

    private BufferedImage img;
    private int width;
    private int height;
    public int[][] data;

    /*    GrayImage(int w, int h) {
        this.width = w;
        this.height = h;
        img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
    }
     */
    public GrayImage(String filename) {
        try {
            img = ImageIO.read(new File(filename));
            width = img.getWidth();
            height = img.getHeight();
            data = new int[width][height];
            Raster raster_in = img.getData();
            System.out.println("Ancho: " + width + " x Alto: " + height);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    final int d = raster_in.getSample(i, j, 0);
                    data[i][j] = d;
                }
            }
        } catch (Exception E) {
            System.out.println("Error reading");
        }
    }

    public int[][] getDataMatrix() {
        return data;
    }

    public int getWidth() {
        return width;
    }

    public void writeImage(String outfilename, int[][] rdata) {
        try {
            WritableRaster raster_out = img.getRaster();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    raster_out.setSample(i, j, 0, rdata[i][j]);
                }
            }
            img.setData(raster_out);
            File file_out = new File(outfilename);
            ImageIO.write(img, "png", file_out);
        } catch (Exception E) {
            System.out.println("Error writing");
        }
    }
}
