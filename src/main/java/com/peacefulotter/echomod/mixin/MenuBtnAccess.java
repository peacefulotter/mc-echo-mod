package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.gui.EchoMenuScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin( GameMenuScreen.class )
public class MenuBtnAccess extends Screen
{
    private static final Text TITLE = Text.literal("menu@echo-mod");

    protected MenuBtnAccess( Text title )
    {
        super( title );
    }

    @Inject(at = @At("HEAD"), method = "initWidgets")
    private void addMenu( CallbackInfo ci )
    {
        ButtonWidget button = new ButtonWidget.Builder( TITLE, btn -> {
            if ( this.client == null ) return;
            this.client.setScreen(new EchoMenuScreen(this, this.client));
        } )
                .size( 100, 20 )
                .position( 10, 10 )
                .build();
        this.addDrawableChild( button );
    }
}
