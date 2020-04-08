package Utilities.UI.Layout;

import Utilities.UI.Events.MultipleActionEvent;
import Utilities.UI.Events.MultipleActionListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.util.ArrayList;

/**
 * Classe per la gestione delle JCheckBox, funzionamento abbastanza simile al ButtonGroup
 */
public class CheckBoxGroup {
    private ArrayList<JCheckBox> list = new ArrayList<>();
    private boolean atLeastOne, initial;
    private short counterSelected;
    private EventListenerList listenerList = new EventListenerList();


    public CheckBoxGroup(boolean atLeastOne){
        this.atLeastOne = atLeastOne;
        counterSelected = atLeastOne? (short)1:(short)0;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public void setAtLeastOne(boolean atLeastOne) {
        this.atLeastOne = atLeastOne;
    }

    public boolean add(JCheckBox jCheckBox) {
        if(list.size() == 0 && atLeastOne)
            jCheckBox.setSelected(true);
        else{
            jCheckBox.setSelected(initial);
            if(initial)
                counterSelected++;
        }

        int m = list.size();

        jCheckBox.addActionListener(actionEvent -> {
            if(jCheckBox.isSelected())
                counterSelected++;
            else
                counterSelected--;

            if(atLeastOne && counterSelected == 0){
                jCheckBox.setSelected(true);
                counterSelected = 1;
            }

            fireMultipleAction(jCheckBox, m);
        });

        return list.add(jCheckBox);
    }

    public void addMultipleActionListener(MultipleActionListener multipleActionListener){
        listenerList.add(MultipleActionListener.class, multipleActionListener);
    }

    public void removeMultipleActionListener(MultipleActionListener multipleActionListener){
        if(multipleActionListener != null)
            listenerList.remove(MultipleActionListener.class, multipleActionListener);
    }

    public void fireMultipleAction(Object source,int id){
        MultipleActionListener[] l = listenerList.getListeners(MultipleActionListener.class);
        for(int i = l.length -1; i >= 0; i-=2){
            l[i].multipleActionPerformed(new MultipleActionEvent(source, id,""));
        }
    }

    public void setAll(){
        list.forEach(jCheckBox -> jCheckBox.setSelected(true));
        counterSelected = (short)(list.size() -1);
    }

    public void unsetAll(){
        boolean selection = false;
        for(int i = 0; i < list.size(); i++){
            JCheckBox c = list.get(i);
            if(atLeastOne && c.isSelected() && !selection){
                selection = true;
                break;
            }
            c.setSelected(false);
        }

        if(!selection && list.get(0) != null)
            list.get(0).setSelected(true);
    }
}
