package ru.demedyuk.randomize.constants;

import javafx.scene.paint.Paint;

public class PaintColors {


    public static final Paint BLACK = valueOf("000000");
    public static final Paint GRAY = valueOf("DBDBDB");

    private static final Paint valueOf(String value) {
        return Paint.valueOf(value);
    }
}
