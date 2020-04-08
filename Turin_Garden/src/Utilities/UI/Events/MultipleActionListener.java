package Utilities.UI.Events;

import java.util.EventListener;

@FunctionalInterface
public interface MultipleActionListener extends EventListener {
    void multipleActionPerformed(MultipleActionEvent multipleActionEvent);
}
