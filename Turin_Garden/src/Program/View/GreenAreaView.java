package Program.View;

import Program.Model.Green_Area;
import Utilities.UI.Layout.AutoGridLayout;
import Utilities.UI.Layout.DisabledPanel;

import javax.swing.*;
import java.awt.*;

public class GreenAreaView {
    private JDialog dialog;
    private Green_Area green_area;

    private JTextField
            cit = new JTextField(),
            denominazione = new JTextField(),
            ambito = new JTextField(),
            tipo = new JTextField(),
            lotto = new JTextField(),
            gestione = new JTextField();

    private JMenuBar menuBar = new JMenuBar();

    private JMenu menu = new JMenu("Action");

    private JMenuItem menuItem = new JMenuItem("Salva modifiche");

    private Frame c;

    private int return_value = -1;

    public GreenAreaView(Frame c){
        menu.add(menuItem);
        menuBar.add(menu);
        this.c = c;
    }

    private void createWindow(){
        dialog = new JDialog(c, "Modifica", true);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.setSize(370,280);
        dialog.setLocationRelativeTo(null);
        dialog.add(menuBar, BorderLayout.NORTH);
        dialog.setResizable(false);
        DisabledPanel panel = new DisabledPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Green Area Field"));
        AutoGridLayout contentPanel = new AutoGridLayout(2);

        contentPanel.add(new JLabel("Cit"));
        contentPanel.add(cit);

        contentPanel.add(new JLabel("Denominazione"));
        contentPanel.add(denominazione);

        contentPanel.add(new JLabel("Ambito"));
        contentPanel.add(ambito);

        contentPanel.add(new JLabel("Tipo"));
        contentPanel.add(tipo);

        contentPanel.add(new JLabel("Lotto"));
        contentPanel.add(lotto);

        contentPanel.add(new JLabel("Gestione"));
        contentPanel.add(gestione);
        panel.setLayout(new BorderLayout());
        panel.add(contentPanel);

        dialog.getContentPane().add(panel, BorderLayout.CENTER);
    }

    public int setGreenAreaModel(Green_Area area){
        cit.setText(area.getCit().toString());
        denominazione.setText(area.getDenominazione());
        ambito.setText(area.getAmbito());
        tipo.setText(area.getTipo());
        lotto.setText(area.getLotto());
        gestione.setText(area.getGestione());
        createWindow();
        menuItem.addActionListener(actionEvent -> {
            return_value = 0;
            dialog.dispose();
        });


        dialog.setVisible(true);
        Green_Area area1 = new Green_Area();
        area1.setAmbito(ambito.getText());
        area1.setCit(cit.getText());
        area1.setDenominazione(denominazione.getText());
        area1.setGestione(gestione.getText());
        area1.setLotto(lotto.getText());
        area1.setTipo(tipo.getText());
        green_area = area1;
        return return_value;
    }

    public Green_Area getSelected(){
        return green_area;
    }

}
