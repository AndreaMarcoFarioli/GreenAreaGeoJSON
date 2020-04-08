package Utilities.UI.Events;

import java.awt.event.ActionEvent;

public class MultipleActionEvent extends ActionEvent {
    public MultipleActionEvent(Object source, int id, String command) {
        super(source, id, command);
    }
}
