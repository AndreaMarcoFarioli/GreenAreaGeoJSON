package Utilities.UI.Layout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Semplice cardLayout carousel
 */

public class JPagesPanel extends JPanel {
    private int counter, size;
    private JButton
            next = new JButton(">"),
            back = new JButton("<");
    private JPanel
            center = new JPanel();
    private JLabel
            fieldCounter = new JLabel();

    private int directionChanged;

    private EventListenerList eventListenerList = new EventListenerList();

    public JPagesPanel(JPanel initialPanel){
        super();
        Dimension d = new Dimension(20,20);
        LineBorder b = new LineBorder(Color.BLACK, 1);
        next.setPreferredSize(d);
        back.setPreferredSize(d);


        back.setBorder(b);
        next.setBorder(b);
        fieldCounter.setBorder(b);
        fieldCounter.setPreferredSize(new Dimension(40,20));
        fieldCounter.setHorizontalAlignment(JLabel.CENTER);
        size++;

        fieldCounter.setText("0");

        setLayout(new BorderLayout());

        JPanel
                bottom = new JPanel();

        CardLayout cl = new CardLayout();
        center.setLayout(cl);

        center.add(initialPanel, "0");
        cl.show(center, "0");

        bottom.setLayout(new FlowLayout());
        bottom.add(back);
        bottom.add(fieldCounter);
        bottom.add(next);
        add(bottom, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        next.addActionListener(actionEvent -> {
            counter++;
            directionChanged = 1;
            onChangeClickButton(new ChangeEvent(this));
        });

        back.addActionListener(actionEvent -> {
            counter--;
            directionChanged = -1;
            onChangeClickButton(new ChangeEvent(this));
        });


        addClickButtonListener(changeEvent -> {

            if(!back.isEnabled() && counter > 0)
                back.setEnabled(true);
            else if(back.isEnabled() && counter == 0)
                back.setEnabled(false);

            if(!next.isEnabled() && counter < size-1)
                next.setEnabled(true);
            else if(next.isEnabled() && counter == size-1)
                next.setEnabled(false);
            cl.show(center, counter+"");

            fieldCounter.setText(counter+"");

            repaint();
            validate();
        });

        fieldCounter.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
            }
        });

        back.setEnabled(false);
    }

    private void onChangeClickButton(ChangeEvent changeEvent){
        Object[] listeners = eventListenerList.getListenerList();
        for(int i = listeners.length - 2; i >= 0; i -= 2){
            if(listeners[i]== ChangeListener.class){
                ((ChangeListener)listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    public void addPanel(JPanel p){
        center.add(p, size+"");
        size++;
    }

    public JButton getBack() {
        return back;
    }

    public JButton getNext() {
        return next;
    }

    public int getCounter() {
        return counter;
    }

    public void addClickButtonListener(ChangeListener changeListener){
        eventListenerList.add(ChangeListener.class, changeListener);
    }

    public void removeClickButtonListener(ChangeListener changeListener){
        eventListenerList.remove(ChangeListener.class, changeListener);
    }

    public int getDirectionChanged() {
        return directionChanged;
    }
}
