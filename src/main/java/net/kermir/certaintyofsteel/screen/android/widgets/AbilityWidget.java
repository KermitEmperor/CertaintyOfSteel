package net.kermir.certaintyofsteel.screen.android.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.android.LocalAndroidPlayer;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.kermir.certaintyofsteel.save.AndroidsSD;
import net.kermir.certaintyofsteel.screen.widgets.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AbilityWidget extends AbstractWidget {
    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(CertaintyOfSteel.MOD_ID,"textures/gui/android_panel_widgets.png");
    private Minecraft mc;
    private Function<AbstractWidget, GuiComponent> addMethod;
    private Consumer<AbstractWidget> removeMethod;
    private boolean isUnlocked;
    private boolean isUnlockable;
    private Ability ability;
    public Component title;
    public Component description;
    private List<String> splitDescription;
    protected int settingsHeight = 0;
    private List<AbstractWidget> widgets = new ArrayList<>();
    protected Rectangle descriptionBounds = new Rectangle();

    public AbilityWidget(int pX, int pY, Ability ability, boolean isUnlocked, Function<AbstractWidget, GuiComponent> addMethod, Consumer<AbstractWidget> removeMethod) {
        super(pX, pY, 26, 26, new TextComponent(""));
        this.mc = Minecraft.getInstance();
        this.addMethod = addMethod;
        this.removeMethod = removeMethod;
        this.ability = ability;
        this.isUnlocked = isUnlocked;
        this.isUnlockable = false;
        this.title = this.ability.name();
        this.description = this.ability.description();
        this.splitDescription = List.of(this.description.getString().split("\\R"));
        this.setBlitOffset(-2);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableDepthTest();

        if (this.isFocused()) {
            this.renderDropdown(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }


        if (this.visible) {
            if (!isMouseOver(pMouseX, pMouseY))
                if (isUnlocked)
                    blit(pPoseStack, this.x, this.y,this.getBlitOffset(), 0, 26, 26, 26,256,256);
                else
                    blit(pPoseStack, this.x, this.y,this.getBlitOffset(), 78, 26, 26, 26,256,256);

            else {
                if (isUnlocked)
                    blit(pPoseStack, this.x, this.y,this.getBlitOffset(), 0, 0, 26, 26,256,256);
                else
                    blit(pPoseStack, this.x, this.y,this.getBlitOffset(), 78, 0, 26, 26,256,256);
            }
        }

        //super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        RenderSystem.disableDepthTest();
    }

    public void renderDropdown(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.setBlitOffset(-1);
        int infoWidgetX = this.x+4;
        int infoWidgetY = this.y+8;

        //Background
        //15 is for the learn/upgrade, enable/disable
        renderDropdownBg(pPoseStack, infoWidgetX, infoWidgetY, this.width , 27+20+mc.font.lineHeight*splitDescription.size(), pMouseX, pMouseY, pPartialTick);
        //Settings
        renderSettings(pPoseStack, infoWidgetX, infoWidgetY, pMouseX, pMouseY, pPartialTick);

        //Title
        renderString(pPoseStack, this.title.getString(), infoWidgetX+this.width, infoWidgetY+1, 0xFFFFFF);

        //Desc
        int i = 0;
        for (String string : this.splitDescription) {
            i += mc.font.lineHeight;
            renderString(pPoseStack, string, infoWidgetX, infoWidgetY+10+i, 0xFFFFFF);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
    }

    public Rectangle getDescriptionBounds() {
        return descriptionBounds;
    }

    public void renderDropdownBg(PoseStack pPoseStack, int x, int y, int pWidth , int height , int pMouseX, int pMouseY, float pPartialTick) {
        int stringwidth = getStringWidth(this.title.getString());
        int width = pWidth;


        for (String line : splitDescription) {
            width = Math.max(getStringWidth(line)-21, width);
        }


        width = Math.max(Math.max(width, stringwidth+3), 84);

        //Padding is calculated by counting the pixels from VOffset until you are inline with the inner part of the box
        //VHeight is texture height + padding-1
        //PvOffset is actual offset - PVHeight?
        //WHAT?????
        //Yeah ignore previous instructions, method variables come from trial and error,
        //if anyone figures out how render9Sprite works (Which is taken from the AdvancementsTab) tell me please
        //Desc
        int descWidth = this.width+width+6;
        int descHeight = height+settingsHeight;
        render9Sprite(pPoseStack, x-6, y+6, descWidth, descHeight, 7, 200, 26, 0,81);

        //Blackdot at 80 for help
        //Title
        int titleWidth = this.width+width+13;
        int titleHeight = 26;
        render9Sprite(pPoseStack, x-9, y-11,  titleWidth, titleHeight, 7, 200, 26, 0, 106);

        this.descriptionBounds.setBounds(x-9, y-7, titleWidth, descHeight+11);
    }

    public void renderSettings(PoseStack pPoseStack, int x, int y,int pMouseX, int pMouseY, float pPartialTick) {

    }

    protected void addButtons(int x, int y) {
        //TODO custom button style
        //TODO functionality to unlock and disable/enable ability
        //TODO change text and logo (learn -> upgrade, disable <-> enable)
        Button learnButton = (Button) addWidget(new Button(x, y, 50, 20, new TranslatableComponent("ability.dropdown.learn"), (pButton) -> {
            LocalAndroidPlayer.INSTANCE.addUnlockedAbility(this.ability);
            CertaintyOfSteel.LOGGER.info("what");
            this.isUnlocked = true;
        }));
        setUnlockable(checkUnlockability());
        learnButton.active = isUnlockable;

        addWidget(new Button(x+54, y, 50, 20, new TranslatableComponent("ability.dropdown.disable"), (pButton) -> {

        }));
    }

    public void addSettingsWidgets(int x, int y) {
        //example
        addWidget(new Button(x, y, 20, 20, new TextComponent(":)"), (pButton) -> {}));
    }

    //Yoinked from AdvancementWidget class
    //AAAAAAAAAAAAAAAAAA
    private int getStringWidth(String string) {
        return mc.font.width(string);
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        //No
    }

    public void setUnlockable(boolean newValue) {
        this.isUnlockable = newValue;
    }

    public boolean checkUnlockability() {
        return this.ability.hasRequirements(AndroidsSD.getInstance().getAndroid(mc.player.getUUID()));
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        this.changeFocus(this.isFocused());
        CertaintyOfSteel.LOGGER.info("{}",this.isFocused());
        if (this.isFocused()) {
            this.setBlitOffset(-1);
            addSettingsWidgets(this.x+4, this.height + 8 + this.y + this.splitDescription.size()*mc.font.lineHeight);
            addButtons(this.x+4, this.settingsHeight + this.height + 8 + this.y + this.splitDescription.size()*mc.font.lineHeight);
            for (AbstractWidget widget : widgets) {
                widget.setBlitOffset(Math.max(0, widget.getBlitOffset()+this.getBlitOffset()+1));
                addMethod.apply(widget);
            }
        } else {
            for (AbstractWidget widget : widgets.toArray(new AbstractWidget[0])) {
                this.setBlitOffset(-2);
                removeMethod.accept(widget);
                widgets.remove(widget);
            }
        }
        CertaintyOfSteel.LOGGER.info("blit {}",this.getBlitOffset());
        super.onClick(pMouseX, pMouseY);
    }

    protected AbstractWidget addWidget(AbstractWidget pWidget) {
        widgets.add(pWidget);
        return pWidget;
    }

    protected void removeWidget(AbstractWidget pWidget) {
        widgets.remove(pWidget);
    }

    protected void renderString(PoseStack poseStack, String string ,int pX, int pY, int pColor) {
        TextUtil.renderString(poseStack, string, pX, pY, this.getBlitOffset()+1, pColor);
    }

    public Ability getAbility() {
        return ability;
    }

    protected void render9Sprite(PoseStack pPoseStack, int pX, int pY, int pWidth, int pHeight, int pPadding, int pUWidth, int pVHeight, int pUOffset, int pVOffset) {
        this.blit(pPoseStack, pX, pY, pUOffset, pVOffset, pPadding, pPadding);
        this.renderRepeating(pPoseStack, pX + pPadding, pY, pWidth - pPadding - pPadding, pPadding, pUOffset + pPadding, pVOffset, pUWidth - pPadding - pPadding, pVHeight);
        this.blit(pPoseStack, pX + pWidth - pPadding, pY, pUOffset + pUWidth - pPadding, pVOffset, pPadding, pPadding);
        this.blit(pPoseStack, pX, pY + pHeight - pPadding, pUOffset, pVOffset + pVHeight - pPadding, pPadding, pPadding);
        this.renderRepeating(pPoseStack, pX + pPadding, pY + pHeight - pPadding, pWidth - pPadding - pPadding, pPadding, pUOffset + pPadding, pVOffset + pVHeight - pPadding, pUWidth - pPadding - pPadding, pVHeight);
        this.blit(pPoseStack, pX + pWidth - pPadding, pY + pHeight - pPadding, pUOffset + pUWidth - pPadding, pVOffset + pVHeight - pPadding, pPadding, pPadding);
        this.renderRepeating(pPoseStack, pX, pY + pPadding, pPadding, pHeight - pPadding - pPadding, pUOffset, pVOffset + pPadding, pUWidth, pVHeight - pPadding - pPadding);
        this.renderRepeating(pPoseStack, pX + pPadding, pY + pPadding, pWidth - pPadding - pPadding, pHeight - pPadding - pPadding, pUOffset + pPadding, pVOffset + pPadding, pUWidth - pPadding - pPadding, pVHeight - pPadding - pPadding);
        this.renderRepeating(pPoseStack, pX + pWidth - pPadding, pY + pPadding, pPadding, pHeight - pPadding - pPadding, pUOffset + pUWidth - pPadding, pVOffset + pPadding, pUWidth, pVHeight - pPadding - pPadding);
    }

    protected void renderRepeating(PoseStack pPoseStack, int pX, int pY, int pBorderToU, int pBorderToV, int pUOffset, int pVOffset, int pUWidth, int pVHeight) {
        for(int i = 0; i < pBorderToU; i += pUWidth) {
            int j = pX + i;
            int k = Math.min(pUWidth, pBorderToU - i);

            for(int l = 0; l < pBorderToV; l += pVHeight) {
                int i1 = pY + l;
                int j1 = Math.min(pVHeight, pBorderToV - l);
                this.blit(pPoseStack, j, i1, pUOffset, pVOffset, k, j1);
            }
        }

    }
}
