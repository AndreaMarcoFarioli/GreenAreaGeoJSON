package Utilities.UI;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameUtils {
    public static boolean hasFilenameExtension(File f, String extension){
        Pattern p = Pattern.compile("\\.+\\.("+extension+")");
        Matcher mat = p.matcher(f.getName());
        return mat.matches();
    }

    public static File checkExt(File f, String extension){
        if(!hasFilenameExtension(f, extension))
            return new File(f.getAbsolutePath() +"." + extension);
        return f;
    }
}
