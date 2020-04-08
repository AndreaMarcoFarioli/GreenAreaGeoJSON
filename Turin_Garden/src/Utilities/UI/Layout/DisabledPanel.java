package Utilities.UI.Layout;

import javax.swing.*;
import java.awt.*;

/**
 * JPanel il quale setEnabled ha valore su tutti i component interni
  */
public class DisabledPanel extends JPanel {
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setEnabled(this, enabled);
        repaint();
    }


    public static void setEnabled(Container c, boolean enabled){
        for (Component comp : c.getComponents())
        {
            comp.setEnabled(enabled);
            setEnabled((Container)comp, enabled);
        }
    }
}
