package net.kermir.certaintyofsteel.screen.android;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.android.LocalAndroidPlayer;
import net.kermir.certaintyofsteel.android.abilities.data.AbilitySettingsJL;
import net.kermir.certaintyofsteel.android.abilities.data.AbilityTreeJL;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.kermir.certaintyofsteel.registry.AbilityRegistry;
import net.kermir.certaintyofsteel.screen.android.widgets.AbilityWidget;
import net.kermir.certaintyofsteel.util.json.JsonOptionalObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Quintet;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AndroidAbilitiesScreen extends DraggableAndroidBGScreen {
    /**
     * String: Required ability name
     * Quintent:
     *  1st Integer the level required
     *  2nd the x position of the requiring ability widget
     *  3rd the y position of the requiring ability widget
     *  4th the x position of the required ability widget
     *  5th the y position of the required ability widget
    * */
    private static final HashMap<String, Quintet<Integer, Integer, Integer, Integer, Integer>> lineTargets = new HashMap<>();

    public AndroidAbilitiesScreen(String playerName) {
        super(new TextComponent(String.format("%s's Android Panel", playerName)));
    }

    @Override
    protected void init() {
        this.addRenderableDraggableWidget(new Button(this.width/2+20, this.height/2-30, 65, 20, new TextComponent("Test button"), (button) -> { }));

        AbilityTreeJL.ABILITY_TREE_DATA.forEach((key, data) -> {
            ResourceLocation resloc = ResourceLocation.tryParse(key);
            if (AbilityRegistry.ABILITIES_REGISTRY.containsKey(resloc)) {

                JsonOptionalObject ability_tree_data = new JsonOptionalObject(data.getAsJsonObject());
                JsonOptionalObject ability_settings_data = new JsonOptionalObject(AbilitySettingsJL.ABILITY_SETTINGS_DATA.get(key).getAsJsonObject());

                int start_x = this.width / 2 + ability_tree_data.getInt("x", 0);
                int start_y = this.height / 2 + ability_tree_data.getInt("y", 0);

                Ability ability = AbilityRegistry.ABILITIES_REGISTRY.getValue(resloc);

                AbilityWidget firstWidget = new AbilityWidget(
                        start_x,
                        start_y,
                        ability,
                        1
                );

                firstWidget.setBlitOffset(-2);
                this.addRenderableDraggableWidget(firstWidget);

                ability_tree_data.ifHas("tiling", tiling_element -> {
                    JsonOptionalObject tiling_data = new JsonOptionalObject(tiling_element.getAsJsonObject());

                    int offset_x = tiling_data.getInt("offset_x", 35);
                    int offset_y = tiling_data.getInt("offset_y", 0);
                    for (int level = 1; level <= ability_settings_data.getInt("top_level", 1); level++) {

                        CertaintyOfSteel.LOGGER.info("AA");

                        AbilityWidget secondaryWidget = new AbilityWidget(
                                start_x + offset_x * level,
                                start_y + offset_y * level,
                                ability,
                                level
                        );
                        this.addRenderableDraggableWidget(secondaryWidget);
                    }


                    tiling_data.ifHas("exceptions");
                });
            }
        });

        /*
        for (Ability ability : AbilityRegistry.ABILITIES_REGISTRY.getValues()) {
            JsonObject json = AbilityTreeJL.ABILITY_TREE_DATA.get(ability.getRegistryName().toString()).getAsJsonObject();

            int x = this.width / 2;
            int y = this.height / 2;

            if (json != null) {
                x += json.get("x").getAsInt();
                y += json.get("y").getAsInt();
            }

            AbilityWidget widget = new AbilityWidget(
                    x,
                    y,
                    ability,
                    LocalAndroidPlayer.INSTANCE.hasAbility(ability)
            );

            widget.setBlitOffset(-2);

            this.addRenderableDraggableWidget(widget);
        }

        this.buildLineCoordinates();
         */

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

    public void buildLineCoordinates() {
        //caching results :D
        lineTargets.clear();

        for (Widget widget : this.renderables) {
            if (widget instanceof AbilityWidget abilityWidget) {
                JsonObject json = AbilityTreeJL.ABILITY_TREE_DATA.get(abilityWidget.getAbility().getRegistryName().toString()).getAsJsonObject();

                if (json != null) {

                    if (json.has("requirements")) {
                        JsonObject dependencies = json.getAsJsonObject("requirements");
                        if (dependencies.has("ability")) {
                            JsonObject abilityDep = dependencies.getAsJsonObject("ability");
                            for (String key : abilityDep.keySet()) {
                                for (Widget genericWidget : this.draggableWidgets) {
                                    if (genericWidget instanceof AbilityWidget possibleTargetWidget) {
                                        if (possibleTargetWidget.getAbility().getRegistryName().toString().equals(key)) {

                                            lineTargets.put(possibleTargetWidget.getAbility().getRegistryName().toString(),
                                                    new Quintet<>(
                                                            abilityDep.get(key).getAsInt(),
                                                            abilityWidget.x+abilityWidget.getWidth()/2,
                                                            abilityWidget.y+abilityWidget.getHeight()/2,
                                                            possibleTargetWidget.x+possibleTargetWidget.getWidth()/2,
                                                            possibleTargetWidget.y+possibleTargetWidget.getHeight()/2
                                                    ));
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

    public void renderLines(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        for (String key : lineTargets.keySet()) {
            Quintet<Integer, Integer, Integer, Integer, Integer> quintet = lineTargets.get(key);

            boolean levelMet;
            if (LocalAndroidPlayer.INSTANCE.getAbilityInfo(key) != null && LocalAndroidPlayer.INSTANCE.getAbilityInfo(key).contains("level")) {
                levelMet = LocalAndroidPlayer.INSTANCE.getAbilityInfo(key).getInt("level") >= quintet.getA();
            }
            else levelMet = true;


            lineConnection(
                    pPoseStack,
                    quintet.getB() + xOffsetGlobal,
                    quintet.getC() + yOffsetGlobal,
                    quintet.getD() + xOffsetGlobal,
                    quintet.getE() + yOffsetGlobal,
                    //TODO move Hardcoded colours to client config (use a static brother)
                    LocalAndroidPlayer.INSTANCE.hasAbility(key)
                            && levelMet ? 0xFFFFBF00 : 0xFF1F1F33,
                    false);
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
        pMinecraft.setScreen(new AndroidAbilitiesScreen(pMinecraft.player.getDisplayName().getString()));
    }

    public <T extends GuiEventListener & Widget & NarratableEntry> T exposedAddDraggableWidget(T widget) {
        return addRenderableDraggableWidget(widget);
    }

    public void exposedRemoveWidget(GuiEventListener widget) {
        this.removeWidget(widget);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
