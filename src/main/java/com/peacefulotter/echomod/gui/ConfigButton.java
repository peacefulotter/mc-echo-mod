package com.peacefulotter.echomod.gui;

import com.peacefulotter.echomod.config.Config;
import net.minecraft.text.Text;


public class ConfigButton extends ColoredButton
{
    public ConfigButton( Config config, int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier )
    {
        super(
            config.get() ? Config.ACTIVE_TEXT_COLOR : Config.DEFAULT_TEXT_COLOR, MenuColors.WIDGET_BACK,
            x, y, width, height, message, onPress, narrationSupplier
        );
        config.onChange( v -> setTextColor( v ? Config.ACTIVE_TEXT_COLOR : Config.DEFAULT_TEXT_COLOR ) );
    }

    public static class Builder extends ColoredButton.Builder
    {
        private final Config config;

        public Builder( Config config )
        {
            super( config.getName(), (w) -> config.toggle() );
            this.config = config;
        }

        @Override
        public ColoredButton build() {
            return new ConfigButton( config, x, y, width, height, message, onPress, NARRATION_SUPPLIER );
        }
    }
}
