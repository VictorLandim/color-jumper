package com.lan.jumper.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

//TODO: cada plataforma possui n componentes de cores misturadas.
//TODO: se a cor do player estiver na lista de cores da plataforma, ele pula, se nao morre.

public enum EntityColor {
    BLUE,
    RED,
    YELLOW;

    public static EntityColor getRandomColor() {
        int r = MathUtils.random(1, 3);

        if (r == 1) return EntityColor.BLUE;
        else if (r == 2) return EntityColor.RED;
        return EntityColor.YELLOW;
    }

    public static boolean isBlue (EntityColor color) { return color == BLUE; }
    public static boolean isRed (EntityColor color) { return color == RED; }
    public static boolean isYellow (EntityColor color) { return color == YELLOW; }

    public boolean isBlue () { return this == BLUE; }
    public boolean isRed () { return this== RED; }
    public boolean isYellow () { return this == YELLOW; }

    public static Color getDebugColor(EntityColor color) {
        if (isBlue(color)) return new Color(0x2196f3ff);
        else if (isRed(color)) return new Color(0xf44336ff);
        else if (isYellow(color)) return new Color(0xffeb3bff);
        else throw new IllegalStateException("Invalid color");
    }
}
