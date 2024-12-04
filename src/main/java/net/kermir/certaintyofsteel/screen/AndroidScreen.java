package net.kermir.certaintyofsteel.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AndroidScreen extends Screen {


    public AndroidScreen(Component pTitle) {
        super(new TextComponent("Android Screen"));
    }

    @Override
    protected void init() {
        this.xOffset = 0;
        this.yOffset = 0;

        this.draggedButton = new Button(this.width/2+xOffset+20, this.height/2+yOffset-30, 20, 16, new TextComponent("faxc"), (button) -> { });

        this.addRenderableWidget(this.draggedButton);
        super.init();
    }

    public Button draggedButton;

    public int xOffset;
    public int yOffset;

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        //RenderSystem.setShaderTexture(0, TEXTURE);

        this.renderBackground(pPoseStack);

        draggedButton.x = this.width/2 + xOffset;
        draggedButton.y = this.height/2 + yOffset;

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);
    }


    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

        CertaintyOfSteel.LOGGER.debug("x: {} y: {}", (int)pDragX, (int)pDragY);

        this.xOffset += (int)pDragX;
        this.yOffset += (int)pDragY;


        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }
}
