package Program.Controller;

import Program.Model.Green_Area;
import Program.Model.ProgramModel;
import Program.View.FirstWindowView;
import Program.View.GreenAreaView;
import Utilities.UI.FilenameUtils;
import Utilities.UI.HideList;
import Utilities.UI.UIMethods;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FirstWindowController {

    final FirstWindowView view;
    final ProgramModel model;
    final GreenAreaView areaView;
    public FirstWindowController(FirstWindowView view, ProgramModel model){
        this.view = view;
        this.model = model;
        areaView = new GreenAreaView(view.getFrame());

        initializeListeners();
    }

    private void initializeListeners(){
        view.loadJSONUrlAddActionListener(actionEvent -> openFileFromUrl());
        view.loadJSONAddActionListener(actionEvent -> openFile());
        view.searchItemAddActionListener(actionEvent -> searchField());
        view.addFilterCheckedListener(actionEvent -> {
            JRadioButton button = (JRadioButton) actionEvent.getSource();
            if(button.isSelected()) {
                setFilter();
                fireFilterChanged();
            }
        });
        view.checkBoxesAddMultipleActionListener(multipleActionEvent -> {
            JCheckBox c = (JCheckBox) multipleActionEvent.getSource();
            if(c.isSelected())
                view.setColumnVisible(multipleActionEvent.getID());
            else
                view.setColumnInvisible(multipleActionEvent.getID());
        });
        view.openGraphAddActionListener(actionEvent -> {
            HashMap<String, ArrayList<Green_Area>> map = new HashMap<>();
            for(Green_Area area :  model.getTableObjects()){
                if(!map.containsKey(area.getLotto()))
                    map.put(area.getLotto(), new ArrayList<>());
                    map.get(area.getLotto()).add(area);
            }
            view.openGraphWindow(map);
        });
        view.exportJSONAddActionListener(actionEvent -> {
            saveGeoJSON();
        });
        view.openGreenAreaAddActionListener(actionEvent -> {
            if(view.getSelectedRow() > -1) {
                int aa = areaView.setGreenAreaModel(model.getTableObjects()[view.getSelectedRow()]);
                if(aa == 0){
                    Green_Area a = areaView.getSelected();
                    model.setRow(view.getSelectedRow(), a);
                    view.setRow(view.getSelectedRow(), a);
                }
            }
            else
                view.showError("Devi selezionare prima una riga");
        });

        view.ripristinaTableAddActionListener(actionEvent -> {
            HideList<Object[]> m = new HideList<>();
            for(Green_Area area : model.getTableObjects())
                m.add(area.getContent());
            view.setDataTable(m);
        });
    }

    public void openWindow(){
        view.showWindow();
    }

    private void openFileFromUrl(){
        //Synchronous Input Dialog
        String u = view.URLPicker();
        if (u == null)
            return;
        if(model.getFileUrl(u) == 0)
            loadFile((InputStream) model.getBufferObject());
    }

    private void openFile(){
        File f = view.JSONPicker();
        if(f == null)
            return;

        try {
            loadFile(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadFile(InputStream stream){
        model.loadFile(stream);
        setFilter();
        view.setFileOpened(true);
        view.createTable(model.getTableObjects(), Green_Area.getHeader());
    }

    public void setFilter(){
        model.orderBy(view.getFilter());
    }

    public void fireFilterChanged(){
        Object[][] t = UIMethods.tableObjectToData(model.getTableObjects());
        view.setDataTable(new HideList<>(Arrays.asList(t)));
    }

    private void searchField(){
        String return_value = view.searchField();
        HideList<Object[]> m = model.search(return_value);
        view.setDataTable(m);
    }

    private void saveGeoJSON(){
        File f = view.saveFile();
        f = FilenameUtils.checkExt(f, "geojson");
        try {
            PrintWriter writer = new PrintWriter(f);
            writer.print(model.saveGeoJSON());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
