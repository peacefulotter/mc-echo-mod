package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.gui.EchoMenuScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.peacefulotter.echomod.EchoMod.MOD_LOGGER;

@Mixin( GameMenuScreen.class )
public class MenuBtnAccess extends Screen
{
    protected MenuBtnAccess( Text title )
    {
        super( title );
    }

    @Inject(at = @At("HEAD"), method = "initWidgets")
    private void addMenu( CallbackInfo ci )
    {
        ButtonWidget button = ButtonWidget.builder( Text.literal("echo-mod"), btn -> {
            if ( this.client == null ) return;
            this.client.setScreen(new EchoMenuScreen(this, this.client));
        } )
                .size( 100, 20 )
                .position( 10, 10 )
                .narrationSupplier( null )
                .build();
        this.addDrawableChild( button );
    }
}
