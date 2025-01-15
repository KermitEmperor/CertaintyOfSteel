package net.kermir.certaintyofsteel.screen.android;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.android.AndroidPlayer;
import net.kermir.certaintyofsteel.android.abilities.data.AbilitiesJsonListener;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.kermir.certaintyofsteel.android.abilities.util.CustomAbilityWidget;
import net.kermir.certaintyofsteel.registry.AbilityRegistry;
import net.kermir.certaintyofsteel.screen.android.widgets.AbilityWidget;
import net.kermir.certaintyofsteel.screen.android.util.DraggableAndroidBGScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AndroidAbilitiesScreen extends DraggableAndroidBGScreen {
    private AndroidPlayer android;

    public AndroidAbilitiesScreen(String playerName, AndroidPlayer android) {
        super(new TextComponent(String.format("%s's Android Panel", playerName)));
        this.android = android;
    }

    @Override
    protected void init() {
        this.addRenderableDraggableWidget(new Button(this.width/2+20, this.height/2-30, 65, 20, new TextComponent("Test button"), (button) -> { }));

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
                widget = abilityWithWidget.customWidget(x, y, ability, android.hasAbility(ability) ,this::addRenderableDraggableWidget, this::removeWidget);
            else
                widget = new AbilityWidget(
                        x,
                        y,
                        ability,
                        android.hasAbility(ability),
                        this::addRenderableDraggableWidget,
                        this::removeWidget
                );
            widget.setBlitOffset(-2);

            this.addRenderableDraggableWidget(widget);
        }

        //this.addRenderableDraggableWidget(new AbilityWidget(this.width/2, this.height/2, new TextComponent("a"), this::addRenderableDraggableWidget, this::removeWidget));
        super.init();
    }


    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pPoseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        //RenderSystem.setShaderTexture(0, TEXTURE);


        drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, this.height / 2 + 44, 16777215);

        this.renderLines(pPoseStack, pMouseX, pMouseY, pPartialTicks);

        pPoseStack.translate(0,0,-100);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);
    }

    public void lineConnection(PoseStack poseStack,int fromX, int fromY, int toX, int toY, int color, boolean preferDrawingBottom /*The answer is always yes*/) {
        if (fromY == toY) {
            this.draggableHLine(poseStack, fromX, toX, fromY, color);
        }
        else if (fromX == toX) {
            this.draggableVLine(poseStack, fromX, fromY, toY, color);
        }
        else if (!preferDrawingBottom) {
            this.draggableVLine(poseStack, fromX, fromY, toY, color );
            this.draggableHLine(poseStack, fromX, toX, toY, color);
        }
        else if (preferDrawingBottom) {
            this.draggableHLine(poseStack, fromX, toX, fromY, color);
            this.draggableVLine(poseStack, toX, fromY, toY, color);
        }
    };

    public void renderLines(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        int x = this.width / 2;
        int y = this.height / 2;

        //TODO optimize whatever is going on here
        //Collect the positions of the dependent stuff and chace their positions

        for (Widget widget : this.renderables) {
            if (widget instanceof AbilityWidget abilityWidget) {
                JsonObject json = AbilitiesJsonListener.EXTRA_ABILITY_DATA.get(abilityWidget.getAbility().getRegistryName().toString()).getAsJsonObject();

                if (json != null) {

                    if (json.has("dependencies")) {
                        JsonObject dependencies = json.getAsJsonObject("dependencies");
                        if (dependencies.has("ability")) {
                            JsonObject abilityDep = dependencies.getAsJsonObject("ability");
                            for (String key : abilityDep.keySet()) {
                                for (Widget genericWidget : this.draggableWidgets) {
                                    if (genericWidget instanceof AbilityWidget possibleTargetWidget) {
                                        if (possibleTargetWidget.getAbility().getRegistryName().toString().equals(key)) {

                                            boolean levelMet;
                                            if (android.getAbilityInfo(possibleTargetWidget.getAbility()).contains("level"))
                                                levelMet = android.getAbilityInfo(possibleTargetWidget.getAbility()).getInt("level") >= abilityDep.get(key).getAsInt();
                                            else levelMet = true;

                                            lineConnection(
                                                    pPoseStack,
                                                    abilityWidget.x+abilityWidget.getWidth()/2,
                                                    abilityWidget.y+abilityWidget.getHeight()/2,
                                                    possibleTargetWidget.x+possibleTargetWidget.getWidth()/2,
                                                    possibleTargetWidget.y+possibleTargetWidget.getHeight()/2,
                                                    //TODO move Hardcoded colours to client config
                                                    android.hasAbility(possibleTargetWidget.getAbility())
                                                            && levelMet ? 0xFFFFBF00 : 0xFF1F1F33,
                                                    false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        List<Rectangle> descsBounds = new ArrayList<>();
        for(GuiEventListener guiEventListener : this.children()) {
            if (guiEventListener instanceof AbilityWidget abilityWidget) {
                if (abilityWidget.isFocused()) {
                    Rectangle rectangle = abilityWidget.getDescriptionBounds();
                    descsBounds.add(rectangle);

                    CertaintyOfSteel.LOGGER.info("X {} Y {} Width {} Height {}", rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
            }
        }

        boolean isClickInsideABound = false;
        for (Rectangle rectangle : descsBounds) {
            isClickInsideABound |= rectangle.contains(pMouseX, pMouseY);
            if (isClickInsideABound) break;
        }


        for(GuiEventListener guieventlistener : this.children()) {
            boolean isAbilityWidget = guieventlistener instanceof AbilityWidget;

            //TODO Find a better solution to whatever the hell is this

            //TODO when clicking on empty hide the selected panel

            //Least cursed boolean fuckery

            if ((isAbilityWidget && ((AbilityWidget)guieventlistener).isFocused()) || !(isAbilityWidget) || (isAbilityWidget && !isClickInsideABound)) {
                if (guieventlistener.mouseClicked(pMouseX, pMouseY, pButton)) {
                    for (GuiEventListener listener : this.children().toArray(new GuiEventListener[0])) {
                        if (guieventlistener instanceof AbilityWidget gwidget) {
                            if (listener instanceof AbilityWidget widget) {
                                if (widget.isFocused() && !Objects.equals(widget.toString(), gwidget.toString())) {
                                    widget.onClick(0,0);
                                    CertaintyOfSteel.LOGGER.info("purged");
                                }
                            }
                        }
                    }
                    this.setFocused(guieventlistener);
                    if (pButton == 0) {
                        this.setDragging(true);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        super.resize(pMinecraft, pWidth, pHeight);
        if (this.minecraft != null && this.minecraft.player != null)
            this.minecraft.player.sendMessage(new TranslatableComponent("screen.ability.waring.resized").withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
        super.onClose();
    }

    public void updateAndroid(AndroidPlayer androidPlayer) {
        this.android = androidPlayer;
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
