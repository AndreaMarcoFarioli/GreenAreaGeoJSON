package Program;


import Modules.DiagramLotto;
import Program.Controller.FirstWindowController;
import Program.Model.ProgramModel;
import Program.View.FirstWindowView;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public ProgramModel model = new ProgramModel();
    public FirstWindowView fwV = new FirstWindowView();
    public FirstWindowController fwC = new FirstWindowController(fwV, model);

    public Main() throws InvocationTargetException, InterruptedException {

    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        FlatDarculaLaf.install();
        new Main().run();
    }

    public void run(){
        fwC.openWindow();
    }

}

