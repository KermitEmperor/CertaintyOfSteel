package net.kermir.certaintyofsteel.screen.android.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.android.abilities.util.Ability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.GuiUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AbilityWidget extends AbstractWidget {
    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(CertaintyOfSteel.MOD_ID,"textures/gui/android_panel_widgets.png");
    public boolean renderExtra = false;
    private Minecraft mc;
    private Function<AbstractWidget, GuiComponent> addMethod;
    private Consumer<AbstractWidget> removeMethod;
    private Ability ability;
    private Component title;
    private Component description;
    protected int extraInfoWidth = 0;
    private List<String> splitDescription;

    public AbilityWidget(int pX, int pY, Function<AbstractWidget, GuiComponent> addMethod, Consumer<AbstractWidget> removeMethod) {
        this(pX, pY, null, addMethod, removeMethod);
        this.mc = Minecraft.getInstance();
        this.addMethod = addMethod;
        this.removeMethod = removeMethod;
    }

    public AbilityWidget(int pX, int pY, Ability ability, Function<AbstractWidget, GuiComponent> addMethod, Consumer<AbstractWidget> removeMethod) {
        super(pX, pY, 26, 26, new TextComponent(""));
        this.mc = Minecraft.getInstance();
        this.addMethod = addMethod;
        this.removeMethod = removeMethod;
        this.ability = ability;
        this.title = this.ability.name();
        this.description = this.ability.description();
        this.splitDescription = List.of(this.description.getString().split(System.lineSeparator()));
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);

        if (renderExtra) {
            int stringwidth = getStringWidth(this.title.getString());
            int infoWidgetX = this.x+4;
            int infoWidgetY = this.y+8;
            int width = stringwidth+this.width;

            for (String line : splitDescription) {
                width = Math.max(getStringWidth(line)+this.width-21, width);
            }

            //Padding is calculated by counting the pixels from VOffset until you are inline with the inner part of the box
            //VHeight is texture height + padding-1
            //PvOffset is actual offset - PVHeight?
            //WHAT?????
            //Yeah ignore previous instructions, method variables come from trial and error,
            //if anyone figures out how render9Sprite works (Which is taken from the AdvancementsTab) tell me please
            //Desc
            render9Sprite(pPoseStack, infoWidgetX-6, infoWidgetY+6, width+6, 10+23*splitDescription.size(), 7, 200, 26, 0,81);

            //Blackdot at 80 for help
            //Title
            render9Sprite(pPoseStack, infoWidgetX-3-6, infoWidgetY-11,  width+7+6, 26, 7, 200, 26, 0, 106);

            drawString(pPoseStack, mc.font, this.title, infoWidgetX+this.width, infoWidgetY+1, 0xFFFFFF);

            drawString(pPoseStack, mc.font, this.description, infoWidgetX, infoWidgetY+20, 0xFFFFFF);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        }


        if (this.visible) {
            if (!isMouseOver(pMouseX, pMouseY))
                blit(pPoseStack, this.x, this.y, 0, 26, 26, 26);
            else
                blit(pPoseStack, this.x, this.y, 0, 0, 26, 26);
        }

        //super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    //Yoinked from AdvancementWidget class
    //AAAAAAAAAAAAAAAAAA

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

    private int getStringWidth(String string) {
        return mc.font.width(string);
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        //No
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        renderExtra = !renderExtra;
        super.onClick(pMouseX, pMouseY);
    }
}
