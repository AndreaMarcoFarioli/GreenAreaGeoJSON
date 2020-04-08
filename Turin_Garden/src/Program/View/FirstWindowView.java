package Program.View;

import Modules.DiagramLotto;
import Program.Model.Green_Area;
import Utilities.UI.*;
import Utilities.UI.Events.MultipleActionListener;
import Utilities.UI.Layout.AutoGridLayout;
import Utilities.UI.Layout.CheckBoxGroup;
import Utilities.UI.Layout.DisabledPanel;
import Utilities.UI.Tables.FilterTableModel;
import Utilities.UI.Tables.TableObject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class FirstWindowView {

    //region VARIABLES

    private JFileChooser chooser = new JFileChooser();
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("GeoJSON type", "geojson");
    private JFrame frame =  new JFrame();
    private JMenuItem loadJSON, searchItem, loadJSONUrl, exportJSON, ripristinaTable = new JMenuItem("Ripristina");

    private JMenu
            fileMenu = new JMenu("File"),
            importMenu = new JMenu("Importa"),
            viewMenu = new JMenu("Vista"),
            exportMenu = new JMenu("Esporta");

    private DisabledPanel filtersPanel = new DisabledPanel(), centerPanel = new DisabledPanel();

    private JRadioButton
            cit = new JRadioButton("Cit"),
            denom = new JRadioButton("Denominazione"),
            ambit = new JRadioButton("Ambito"),
            tipo = new JRadioButton("Tipo"),
            lotto = new JRadioButton("Lotto"),
            gestione = new JRadioButton("Gestione");

    private JCheckBox
            citL = new JCheckBox("Cit"),
            denomL = new JCheckBox("Denominazione"),
            ambitL = new JCheckBox("Ambito"),
            tipoL = new JCheckBox("Tipo"),
            lottoL = new JCheckBox("Lotto"),
            gestioneL = new JCheckBox("Gestione");

    private JButton
            openGreenArea = new JButton("Apri Finestra"),
            openGraph = new JButton("Grafico");

    private ButtonGroup orderByGroup = new ButtonGroup();

    private FilterTableModel filterTableModel;

    private CheckBoxGroup checkBoxes = new CheckBoxGroup(true);

    private JTable table;

    //endregion

    //region CONSTRUCTORS
    public FirstWindowView() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {

            chooser.setFileFilter(filter);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.getContentPane().setLayout(new BorderLayout());

            frame.setTitle("GeoJSON Viewer");

            createUpperPanel();
            createCenterPanel();
            createLeftPanel();
            createBottomPanel();

            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
        });
    }
    //endregion

    //region panel_creators
    private void createUpperPanel(){
        ImageIcon
                iconFile = resizedIcon("Images/app-json-icon.png", 15,15),
                iconURL =resizedIcon("Images/document-url-icon.png", 15,15),
                iconSearch = resizedIcon("Images/search-icon.png", 15,15),
                geoJSONIcon = resizedIcon("Images/geojson-logo.png", 15,15);
        exportMenu.setEnabled(false);
        viewMenu.setEnabled(false);
        //Menu
        JMenuBar
                bar = new JMenuBar();

        loadJSON = new JMenuItem("File", iconFile);
        searchItem = new JMenuItem("Search", iconSearch);
        loadJSONUrl = new JMenuItem("URL", iconURL);
        exportJSON = new JMenuItem("GeoJSON", geoJSONIcon);
        viewMenu.add(searchItem);
        viewMenu.add(ripristinaTable);
        importMenu.add(loadJSON);
        importMenu.add(loadJSONUrl);

        exportMenu.add(exportJSON);

        fileMenu.add(importMenu);
        fileMenu.add(exportMenu);

        bar.add(fileMenu);
        bar.add(viewMenu);

        frame.getContentPane().add(bar, BorderLayout.NORTH);
    }

    private void createBottomPanel(){
        frame.getContentPane().add(new JLabel("Andrea Marco Farioli"), BorderLayout.PAGE_END);
    }

    private void createCenterPanel(){
        centerPanel.setPreferredSize(new Dimension(600,500));
        centerPanel.setBackground(new Color(50, 50, 50));
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx=0;
        constraints.gridy=0;
        constraints.weightx=.5;
        centerPanel.add(new JLabel("<html>Seleziona un file per continuare</html>"), constraints);
        constraints.gridy =1;
        JLabel label = new JLabel("<html><b>File > Importa > </b></html>");
        centerPanel.add(label, constraints);
        System.out.println(UIManager.getDefaults());
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
        centerPanel.repaint();
        centerPanel.validate();
    }

    private void createLeftPanel(){
        filtersPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
        filtersPanel.setPreferredSize(new Dimension(270,500));
        filtersPanel.setLayout(new BorderLayout());

        AutoGridLayout
                filters = new AutoGridLayout(2),
                excludeColumn = new AutoGridLayout(2),
                bottomPanel = new AutoGridLayout(2);

        //region setBorder
        filters.setBorder(BorderFactory.createTitledBorder("Order by"));
        excludeColumn.setBorder(BorderFactory.createTitledBorder("Column filter"));
        //endregion

        //region setMax
        filters.setMaximumSize(new Dimension(filters.getMaximumSize().width, 1));
        excludeColumn.setMaximumSize(new Dimension(filters.getMaximumSize().width, 1));
        bottomPanel.setMaximumSize(new Dimension(filters.getMaximumSize().width, 1));
        //endregion

        //region ButtonGroup
        orderByGroup.add(cit);
        orderByGroup.add(denom);
        orderByGroup.add(ambit);
        orderByGroup.add(tipo);
        orderByGroup.add(lotto);
        orderByGroup.add(gestione);
        cit.setSelected(true);
        //endregion

        //region filters
        filters.add(cit);
        filters.add(denom);
        filters.add(ambit);
        filters.add(tipo);
        filters.add(lotto);
        filters.add(gestione);
        //endregion


        //region excludeColumn
        checkBoxes.setInitial(true);
        excludeColumn.add(citL);
        excludeColumn.add(denomL);
        excludeColumn.add(ambitL);
        excludeColumn.add(tipoL);
        excludeColumn.add(lottoL);
        excludeColumn.add(gestioneL);
        checkBoxes.add(citL);
        checkBoxes.add(denomL);
        checkBoxes.add(ambitL);
        checkBoxes.add(tipoL);
        checkBoxes.add(lottoL);
        checkBoxes.add(gestioneL);
        //endregion

        bottomPanel.add(openGreenArea);
        bottomPanel.add(openGraph);

        //region Vertical Box
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(Box.createRigidArea(new Dimension(0, 5)));
        verticalBox.add(filters);
        verticalBox.add(Box.createRigidArea(new Dimension(0, 5)));
        verticalBox.add(excludeColumn);
        verticalBox.add(Box.createGlue());
        verticalBox.add(bottomPanel);
        filtersPanel.add(verticalBox, BorderLayout.CENTER);
        //endregion

        frame.getContentPane().add(filtersPanel, BorderLayout.LINE_START);

        filtersPanel.setEnabled(false);
    }

    public void createTable(TableObject[] objects, Object[] columns){
        centerPanel.removeAll();

        centerPanel.setLayout(new BorderLayout());
        filterTableModel = new FilterTableModel(UIMethods.tableObjectToData(objects),columns);
        table = new JTable(filterTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(table);
        centerPanel.add(tableScrollPane);
        centerPanel.repaint();
        centerPanel.validate();
    }
    //endregion

    //region Window Management

    public void showWindow(){
        frame.setVisible(true);
    }

    public void closeWindow(){
        frame.setVisible(false);
    }

    public void disposeWindow(){
        frame.dispose();
    }

    //endregion

    private ImageIcon resizedIcon(String path, int width, int height){
        ImageIcon icon = new ImageIcon(path);
        icon = new ImageIcon(icon.getImage().getScaledInstance(width,height, Image.SCALE_SMOOTH));
        return icon;
    }

    //region LISTENERS

    public void loadJSONAddActionListener(ActionListener listener){
        loadJSON.addActionListener(listener);
    }

    public void loadJSONUrlAddActionListener(ActionListener listener){
        loadJSONUrl.addActionListener(listener);
    }

    public void searchItemAddActionListener(ActionListener listener){
        searchItem.addActionListener(listener);
    }

    public void checkBoxesAddMultipleActionListener(MultipleActionListener multipleActionListener){
        checkBoxes.addMultipleActionListener(multipleActionListener);
    }

    public void openGraphAddActionListener(ActionListener listener){
        openGraph.addActionListener(listener);
    }

    public void exportJSONAddActionListener(ActionListener listener) {exportJSON.addActionListener(listener);}

    public void openGreenAreaAddActionListener(ActionListener listener) {openGreenArea.addActionListener(listener);}

    public void ripristinaTableAddActionListener(ActionListener listener){ripristinaTable.addActionListener(listener);}

    //endregion

    //region PREDEFINED FORM
    public String URLPicker(){

        return (String) JOptionPane.showInputDialog(frame, "Put here the url", "Url Picker", JOptionPane.INFORMATION_MESSAGE, resizedIcon("Images/document-url-icon.png", 35,35), null, null);
    }

    public String searchField(){
        return (String) JOptionPane.showInputDialog(frame, "Put here the url", "Url Picker", JOptionPane.INFORMATION_MESSAGE, resizedIcon("Images/search-icon.png", 35,35), null, null);
    }

    public File JSONPicker(){
        return chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION? chooser.getSelectedFile():null;
    }

    public void openError(String message){
        JOptionPane.showConfirmDialog(frame, message, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    public void openLoading(){
        frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        frame.setEnabled(false);
        frame.repaint();
    }

    public void closeLoading(){
        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        frame.setEnabled(true);
        frame.repaint();
    }
    //endregion

    //region VIEW SETTERS

    public void setExportMenuEnabled(boolean enabled){
        exportMenu.setEnabled(enabled);
        exportMenu.repaint();
    }

    public void setViewMenuEnabled(boolean enabled){
        viewMenu.setEnabled(enabled);
        viewMenu.repaint();
    }

    public void setPanelFiltersEnabled(boolean enabled){
        filtersPanel.setEnabled(enabled);
    }

    public void setFileOpened(boolean fileOpened){
        setExportMenuEnabled(fileOpened);
        setViewMenuEnabled(fileOpened);
        setPanelFiltersEnabled(fileOpened);
    }

    public void setTitle(String title){
        frame.setTitle(title);
    }

    //endregion

    public short getFilter(){
        var a = orderByGroup.getElements();
        for(short i = 0; i < orderByGroup.getButtonCount(); i++){
            if(a.nextElement().isSelected())
                return i;
        }
        return -1;
    }

    //region TableManagement
    public void setDataTable(HideList<Object[]> dataTable){
        filterTableModel.setData(dataTable);
    }
    public void setColumnVisible(int id){
        filterTableModel.showColumn(id);
    }

    public void setColumnInvisible(int id){
        filterTableModel.hideColumn(id);
    }

    //endregion

    public void addFilterCheckedListener(ActionListener listener) {
        var a = orderByGroup.getElements();
        for(short i = 0; i < orderByGroup.getButtonCount(); i++){
            a.nextElement().addActionListener(listener);
        }
    }

    public void openGraphWindow(HashMap<String, ArrayList<Green_Area>> areas){
        JDialog frame = new JDialog(this.frame, "Graph", true);
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem item = new JMenuItem("Salva");

        file.add(item);
        bar.add(file);
        DiagramLotto lotto = new DiagramLotto(areas);
        frame.getContentPane().setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane(lotto);
        frame.getContentPane().add(pane, BorderLayout.CENTER);
        frame.getContentPane().add(bar, BorderLayout.NORTH);

        item.addActionListener(actionEvent -> lotto.saveGraph(frame));
        frame.setSize(790,460);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public File saveFile(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(filter);
        chooser.showSaveDialog(frame);
        return chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION? chooser.getSelectedFile():null;
    }

    public JFrame getFrame() {
        return frame;
    }

    public int getSelectedRow(){
        return table.getSelectedRow();
    }

    public void showError(String message){
        JOptionPane.showConfirmDialog(frame, message, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    public void setRow(int index, TableObject object){
        filterTableModel.setRow(index,object);
    }
}
