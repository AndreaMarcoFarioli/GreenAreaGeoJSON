package Modules;

import Program.Model.Green_Area;
import Utilities.UI.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DiagramLotto extends JPanel {
    private HashMap<String, ArrayList<Green_Area>> areas;
    private int x,y;
    public DiagramLotto(HashMap<String, ArrayList<Green_Area>> areas){
        super();
        this.areas=areas;
    }

    private BufferedImage image;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        createG(g);

    }

    private void createG(Graphics g){
        int counter = 0;
        int offset = 30;
        g.setColor(Color.WHITE);
        g.drawString("Lotti", 10, 15);
        g.drawString("Giardini per lotto", 50, 15);
        y = 30;
        for(String name : areas.keySet()){
            y+=30;
            g.setColor(Color.WHITE);
            g.drawString(name, 10,(15+(30*counter))+offset);
            g.setColor(Color.ORANGE);
            int size = areas.get(name).size();
            g.fillRect(50,(30*counter)+offset, size, 20);
            x = Math.max(x, size + 90);
            g.setColor(Color.WHITE);
            g.drawString(""+size, size+60, (15+(30*counter))+offset);
            counter++;
        }
        setPreferredSize(new Dimension(x, y));
    }

    public void saveGraph(Component c){
        image = new BufferedImage(x,y, 1);
        Graphics2D g = image.createGraphics();
        g.setBackground(Color.BLACK);
        createG(g);
        JFileChooser ma = new JFileChooser();
        ma.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
        if(!(ma.showSaveDialog(c) == JFileChooser.APPROVE_OPTION))
            return;
        File f = ma.getSelectedFile();

        f =FilenameUtils.checkExt(f, "png");

        System.out.println(f.getName());

        try {
            ImageIO.write(image, "png", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
