package Utilities.UI.Layout;

import javax.swing.*;
import java.awt.*;

/**
 * Settato un numero massimo di componenti per linea autoincrementa le righe quando i componenti raggiungono il numero massimo
 */
public class AutoGridLayout extends JPanel {
    private GridLayout layout;
    private int counter = 0;
    public AutoGridLayout(int maxColumns){
        super();
        layout = new GridLayout(0, maxColumns);
        layout.setColumns(maxColumns);
    }

    @Override
    public Component add(Component comp) {
        if (counter == 0)
            layout.setRows(layout.getRows()+1);
        counter++;
        counter%=layout.getColumns();

        setLayout(layout);
        return super.add(comp);
    }
}
