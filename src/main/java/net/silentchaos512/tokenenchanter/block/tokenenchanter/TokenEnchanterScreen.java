package net.silentchaos512.tokenenchanter.block.tokenenchanter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.tokenenchanter.TokenMod;

public class TokenEnchanterScreen extends ContainerScreen<TokenEnchanterContainer> {
    public static final ResourceLocation TEXTURE = TokenMod.getId("textures/gui/token_enchanter.png");

    public TokenEnchanterScreen(TokenEnchanterContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (minecraft == null) return;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(TEXTURE);
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
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
    }
}
