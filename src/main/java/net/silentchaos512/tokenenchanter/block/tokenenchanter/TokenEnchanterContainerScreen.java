package net.silentchaos512.tokenenchanter.block.tokenenchanter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.silentchaos512.tokenenchanter.TokenMod;

public class TokenEnchanterContainerScreen extends AbstractContainerScreen<TokenEnchanterContainerMenu> {
    public static final ResourceLocation TEXTURE = TokenMod.getId("textures/gui/token_enchanter.png");

    public TokenEnchanterContainerScreen(TokenEnchanterContainerMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int xPos = (this.width - this.imageWidth) / 2;
        int yPos = (this.height - this.imageHeight) / 2;
        blit(matrixStack, xPos, yPos, 0, 0, this.imageWidth, this.imageHeight);

        // Progress arrow
        int progress = menu.getProgress();
        int cost = menu.getProcessTime();
        int length = cost > 0 && progress > 0 && progress < cost ? progress * 24 / cost : 0;
        blit(matrixStack, xPos + 102, yPos + 34, 176, 14, length + 1, 16);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
    }
}
