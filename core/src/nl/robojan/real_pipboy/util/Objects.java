package nl.robojan.real_pipboy.util;

/**
 * Created by s120330 on 11-8-2015.
 */
public class Objects {
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}
