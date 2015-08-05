package nl.robojan.real_pipboy.util;

/**
 * Created by s120330 on 16-7-2015.
 */
public class StringUtil {
    public static boolean isInteger(String str)
    {
        try
        {
            Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public static String getFileExtension(String filePath) {
        String extension = "";
        int i = filePath.lastIndexOf('.');
        int p = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if(i>p) {
            extension = filePath.substring(i + 1);
        }
        return extension;
    }

    public static String getFilePathWithoutExtension(String filePath) {
        String path = filePath;
        int i = filePath.lastIndexOf('.');
        int p = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if(i>p) {
            path = filePath.substring(0, i);
        }
        return path;
    }
}
