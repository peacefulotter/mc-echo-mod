package com.peacefulotter.echomod.gui;

import java.awt.*;

public enum MenuColors
{
    WHITE(new Color( 255, 255, 255 )),
    BLACK_FOGRA(new Color( 0, 8, 20 )),
    PURPLE(new Color( 128, 86, 226 )),
    PERSIAN_INDIGO(new Color( 39, 24, 126 )),
    OXFORD_BLUE(new Color( 0, 29, 61 )),
    PRUSSIAN_BLUE(new Color( 7, 71, 134 )),
    DODGER_BLUE(new Color( 16, 152, 247 )),
    CORNFLOWER_BLUE(new Color( 117, 139, 253 )),
    SEA_GREEN_CRAYOLA(new Color( 68, 255, 210 )),
    MOUTAIN_MEADOW(new Color( 23, 184, 144 )),
    MALACHITE(new Color( 4, 195, 68 )),
    GREEN_PANTONE(new Color( 77, 170, 87 )),
    DARK_GREEN(new Color( 4, 68, 77 )),
    ARMY_GREEN(new Color( 65, 82, 31 )),
    MIKADO_YELLOW(new Color( 255, 195, 0 )),
    PEACH(new Color( 255, 191, 160 )),
    DARK_ORANGE(new Color( 255, 134, 0 )),
    RED_CRAYOLA(new Color( 254, 95, 85 )),
    VENETIAN_RED(new Color( 193, 18, 31 )),
    AMARANTH_PURPLE(new Color( 179, 37, 86 ));

    private final Color color;

    public static final MenuColors WIDGET_BACK = MenuColors.AMARANTH_PURPLE;
    public static final MenuColors WIDGET_TEXT = MenuColors.WHITE;

    MenuColors( Color color )
    {
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }
}
