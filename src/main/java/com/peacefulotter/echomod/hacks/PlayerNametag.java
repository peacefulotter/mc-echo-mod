package com.peacefulotter.echomod.hacks;

import com.peacefulotter.echomod.LabelRenderer;
import com.peacefulotter.echomod.config.ConfigManager;
import com.peacefulotter.echomod.events.EventManager;
import com.peacefulotter.echomod.events.list.RenderLabelEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import static com.peacefulotter.echomod.EchoModClient.CLIENT_LOGGER;

public class PlayerNametag
{
    private static final NavigableMap<Integer, Formatting> healthColorMap = new TreeMap<>();
    private static final List<Class<? extends Entity>> allowedEntityClasses = List.of(
        MobEntity.class, PlayerEntity.class, VillagerEntity.class
    );

    static {
        healthColorMap.put(0, Formatting.DARK_RED);
        healthColorMap.put(5, Formatting.GOLD);
        healthColorMap.put(10, Formatting.YELLOW);
        healthColorMap.put(15, Formatting.GREEN);
    }

    PlayerNametag()
    {
        EventManager.addListener( ConfigManager.ENTITY_NAMETAG, RenderLabelEvent.class, this::onRenderLabel );
    }

    public <T extends Entity> void onRenderLabel( RenderLabelEvent<T> e )
    {
        if ( !allowedEntityClasses.contains( e.entity().getClass() ) ) return;
        Text text = e.entity().getDisplayName();
        if ( e.entity() instanceof LivingEntity )
            text = addHealth( (LivingEntity) e.entity(), text);

        LabelRenderer.render(
            e.dispatcher(), e.textRenderer(), e.entity(), text,
            e.matrices(), e.vertexConsumers(), e.light()
        );

        e.ci().cancel();
    }

    public Text addHealth( LivingEntity entity, Text text )
    {
        CLIENT_LOGGER.info( "Add Health: " + entity + "  " + text.getString() );
        int health = (int) entity.getHealth();
        Formatting color = healthColorMap.floorEntry(health).getValue();

        MutableText formattedHealth = Text.literal(" ")
            .append(Integer.toString(health))
            .formatted(color);

        return ((MutableText)text).append(formattedHealth);
    }
}
