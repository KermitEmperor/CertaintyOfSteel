package net.kermir.certaintyofsteel.screen.android.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Function;

public class AbilityWidget extends AbstractWidget {
    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(CertaintyOfSteel.MOD_ID,"textures/gui/android_panel_widgets.png");
    public boolean renderExtra = false;
    private Minecraft mc;
    private Function<AbstractWidget, GuiComponent> addMethod;
    private Consumer<AbstractWidget> removeMethod;

    private Button removale;


    public AbilityWidget(int pX, int pY, Component pMessage) {
        super(pX, pY, 26, 26, pMessage);
        this.mc = Minecraft.getInstance();
    }

    public AbilityWidget(int pX, int pY, Component pMessage, Function<AbstractWidget, GuiComponent> addMethod, Consumer<AbstractWidget> removeMethod) {
        super(pX, pY, 26, 26, pMessage);
        this.mc = Minecraft.getInstance();
        this.addMethod = addMethod;
        this.removeMethod = removeMethod;
    }


    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);

        if (renderExtra) {
            String text = "GAAAAAAAAY";
            int stringwidth = getStringWidth(text);
            blit(pPoseStack, this.x+this.width, this.y, 0, 52, stringwidth, 20);
            drawString(pPoseStack, mc.font, text, this.x+this.width, this.y, 0xFFFFFF);

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

    private int getStringWidth(String string) {
        return mc.font.width(string);
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        renderExtra = !renderExtra;
        if (renderExtra) {
            removale = (Button) addMethod.apply(new Button(this.x+this.width+10, this.y, 16, 16, new TextComponent(""), (button) -> {}));
        } else {
            removeMethod.accept(removale);
        }
        super.onClick(pMouseX, pMouseY);
    }
}
