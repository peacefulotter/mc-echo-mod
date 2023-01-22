package com.peacefulotter.echomod.mixin;

import com.peacefulotter.echomod.gui.ModMenuBtn;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin( TitleScreen.class )
public class TitleModMenuBtn extends Screen
{
    protected TitleModMenuBtn( Text title )
    {
        super( title );
    }

    @Inject(at = @At("HEAD"), method = "initWidgetsNormal")
    private void addMenu( CallbackInfo ci )
    {
        ButtonWidget btn = ModMenuBtn.getBtn( this, client );
        this.addDrawableChild( btn );
    }
}
