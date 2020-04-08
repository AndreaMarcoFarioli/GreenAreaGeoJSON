package Utilities.IO;

import java.io.File;
import java.io.IOException;

public class IOMethods {
    public static void deleteDirectory(File folder) throws IOException {
        if (!folder.isDirectory())
            throw new IOException("The paramater \"folder\" must be a folder path");
        File[] files = folder.listFiles();
        if(files != null)
            for(File f : files)
                if(f.isDirectory())
                    deleteDirectory(f);
                else
                    f.delete();
        folder.delete();
    }
}
