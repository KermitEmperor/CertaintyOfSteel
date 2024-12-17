package net.kermir.certaintyofsteel.screen.android;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.kermir.certaintyofsteel.android.abilities.data.AbilitiesJsonListener;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.kermir.certaintyofsteel.android.abilities.util.CustomAbilityWidget;
import net.kermir.certaintyofsteel.registry.AbilityRegistry;
import net.kermir.certaintyofsteel.screen.android.util.AndroidScreen;
import net.kermir.certaintyofsteel.screen.android.util.AndroidScreens;
import net.kermir.certaintyofsteel.screen.android.widgets.AbilityWidget;
import net.kermir.certaintyofsteel.screen.util.DraggableBackgroundScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class AndroidAbilitiesScreen extends DraggableBackgroundScreen implements AndroidScreen {
    private AndroidPlayer android;


    public AndroidAbilitiesScreen(String playerName, AndroidPlayer android) {
        super(new TextComponent(String.format("%s's Android Panel", playerName)));
        this.android = android;
    }

    @Override
    protected void init() {
        this.addRenderableDraggableWidget(new Button(this.width/2+20, this.height/2-30, 20, 16, new TextComponent("Sex"), (button) -> { }));

        //TODO datapack system for location of the ability, item requirement (optional), and required unlocks
        //TODO lines that connect each ability according to above latter
        for (Ability ability : AbilityRegistry.ABILITIES_REGISTRY.getValues()) {
            JsonObject json = AbilitiesJsonListener.EXTRA_ABILITY_DATA.get(ability.getRegistryName().toString()).getAsJsonObject();

            int x = this.width / 2;
            int y = this.height / 2;

            if (json != null) {
                x += json.get("x").getAsInt();
                y += json.get("y").getAsInt();
            }

            AbilityWidget widget;
            if (ability instanceof CustomAbilityWidget abilityWithWidget)
                widget = abilityWithWidget.customWidget(x, y, ability, this::addRenderableDraggableWidget, this::removeWidget);
            else
                widget = new AbilityWidget(
                        x,
                        y,
                        ability,
                        this::addRenderableDraggableWidget,
                        this::removeWidget
                );

            this.addRenderableDraggableWidget(widget);
        }

        //this.addRenderableDraggableWidget(new AbilityWidget(this.width/2, this.height/2, new TextComponent("a"), this::addRenderableDraggableWidget, this::removeWidget));
        super.init();
    }


    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        //RenderSystem.setShaderTexture(0, TEXTURE);

        drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, this.height / 2 + 44, 16777215);

        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public AndroidScreens getScreenType() {
        return AndroidScreens.ABILITIES;
    }
}
