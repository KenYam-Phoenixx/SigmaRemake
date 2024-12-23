package com.skidders.sigma.screens;

import com.skidders.SigmaReborn;
import com.skidders.sigma.module.Category;
import com.skidders.sigma.module.Module;
import com.skidders.sigma.utils.IMinecraft;
import com.skidders.sigma.utils.font.Renderer;
import com.skidders.sigma.utils.render.RenderUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ClickGUI extends Screen implements IMinecraft {

    private final Renderer light25 = SigmaReborn.INSTANCE.fontManager.getFont("HelveticaNeue-Light", 25);
    private final Renderer light20 = SigmaReborn.INSTANCE.fontManager.getFont("HelveticaNeue-Light", 20);
    private final Map<Category, Point> categoryPositions = new HashMap<>();
    private Category draggingCategory = null;
    private int dragOffsetX = 0, dragOffsetY = 0;

    public ClickGUI(String title) {
        super(Text.of(title));

        float xOffsetStart = 7;
        float xOffset = xOffsetStart, yOffset = 10;
        int count = 0, columns = 4;

        for (Category category : Category.values()) {
            categoryPositions.put(category, new Point((int) xOffset, (int) yOffset));
            xOffset += 110 + 5;

            if (++count % columns == 0) {
                xOffset = xOffsetStart;
                yOffset += 27 + 140 + 5;
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        GameRenderer gameRenderer = mc.gameRenderer;

        if (gameRenderer instanceof GameRendererAccessor accessor) {
            accessor.sigmaRemake$invokeLoadShader(new Identifier("shaders/post/blur.json"));
        } else {
            throw new IllegalStateException("GameRenderer does not implement GameRendererAccessor");
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        mc.gameRenderer.disableShader();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { //left mouse button
            for (Map.Entry<Category, Point> entry : categoryPositions.entrySet()) {
                Category category = entry.getKey();
                Point position = entry.getValue();

                if (mouseX >= position.x && mouseX <= position.x + 110 &&
                        mouseY >= position.y && mouseY <= position.y + 27) {
                    draggingCategory = category;
                    dragOffsetX = (int) (mouseX - position.x);
                    dragOffsetY = (int) (mouseY - position.y);

                    return true;
                }
            }

            for (Category category : Category.values()) {
                Point position = categoryPositions.get(category);
                if (position == null) continue;

                float xOffset = position.x;
                float yOffset = position.y;

                float modOffset = yOffset + 27;
                for (Module module : SigmaReborn.INSTANCE.moduleManager.getModulesByCategory(category)) {
                    if (RenderUtil.hovered(mouseX, mouseY, xOffset, modOffset, 110, 14)) {
                        module.setEnabled(!module.enabled);
                    }
                    modOffset += 14;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingCategory != null) { //left mouse button released
            draggingCategory = null; //stop dragging
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0 && draggingCategory != null) { //left mouse button dragging
            Point position = categoryPositions.get(draggingCategory);
            if (position != null) {
                position.setLocation(mouseX - dragOffsetX, mouseY - dragOffsetY);
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        for (Category category : Category.values()) {
            Point position = categoryPositions.get(category);
            if (position == null) continue;

            float xOffset = position.x;
            float yOffset = position.y;

            RenderUtil.drawRectangle(matrices, xOffset, yOffset, 110, 27, new Color(250, 250, 250, 230));
            RenderUtil.drawRectangle(matrices, xOffset, yOffset + 27, 110, 140, new Color(250, 250, 250));
            light25.drawString(category.name, xOffset + 8, yOffset + 8, new Color(119, 121, 124).getRGB());

            float modOffset = yOffset + 27;
            for (Module module : SigmaReborn.INSTANCE.moduleManager.getModulesByCategory(category)) {
                RenderUtil.drawRectangle(matrices, xOffset, modOffset, 110, 14, module.enabled ? new Color(41, 166, 255) : new Color(250, 250, 250));
                light20.drawString(module.name, xOffset + (module.enabled ? 10 : 8), modOffset + 2, module.enabled ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
                modOffset += 14;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        //detect which category I am hovering over,
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
}
