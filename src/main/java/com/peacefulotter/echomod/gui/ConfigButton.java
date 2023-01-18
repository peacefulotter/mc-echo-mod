package com.peacefulotter.echomod.gui;

import com.peacefulotter.echomod.config.Config;
import net.minecraft.text.Text;


public class ConfigButton extends ColoredButton
{
    public ConfigButton( Config config, int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier )
    {
        super(
            config.get() ? Config.ACTIVE_TEXT_COLOR : Config.DEFAULT_TEXT_COLOR,
            config.get() ? Config.ACTIVE_BGRD_COLOR : Config.DEFAULT_BGRD_COLOR,
            x, y, width, height, message, onPress, narrationSupplier
        );
        config.onChange( v -> {
            if ( v )
            {
                setTextColor( Config.ACTIVE_TEXT_COLOR );
                setBackgroundColor( Config.ACTIVE_BGRD_COLOR );
            }
            else
            {
                setTextColor( Config.DEFAULT_TEXT_COLOR );
                setBackgroundColor( Config.DEFAULT_BGRD_COLOR );
            }
        } );
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
