package com.peacefulotter.echomod.gui;

import java.awt.*;

public enum MenuColors
{
    MALACHITE(new Color( 4, 195, 68 )),
    BLACK_FOGRA(new Color( 0, 8, 20 )),
    OXFORD_BLUE(new Color( 0, 29, 61 )),
    PRUSSIAN_BLUE(new Color( 7, 71, 134 )),
    MIKADO_YELLOW(new Color( 255, 195, 0 )),
    VENETIAN_RED(new Color( 193, 18, 31 )),
    DARK_GREEN(new Color( 4, 68, 77 )),
    PURPLE(new Color( 128, 86, 226 ));

    private final Color color;

    MenuColors( Color color )
    {
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }
}
