package ru.demedyuk.randomize.constants;

import javafx.scene.paint.Paint;

public class PaintColors {


    public static final Paint BLACK = valueOf("000000");
    public static final Paint GRAY = valueOf("DBDBDB");
    public static final Paint RED = valueOf("#FF0000");

    private static final Paint valueOf(String value) {
        return Paint.valueOf(value);
    }
}
