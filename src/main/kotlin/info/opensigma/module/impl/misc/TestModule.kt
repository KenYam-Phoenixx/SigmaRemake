package info.opensigma.module.impl.misc

import info.opensigma.OpenSigma
import info.opensigma.event.impl.render.Render2DEvent
import info.opensigma.module.Module
import info.opensigma.setting.impl.primitive.PrimitiveSetting
import me.x150.renderer.font.FontRenderer
import me.x150.renderer.render.Renderer2d
import meteordevelopment.orbit.EventHandler
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import java.awt.Color
import java.awt.Font

class TestModule : Module("Test", "A module purely for testing purposes",GLFW.GLFW_KEY_V) {

    val font: Font = Font.decode("SauceCodePro Nerd Font")
    var renderer: FontRenderer? = FontRenderer(font, 10f)
    val testSetting = PrimitiveSetting("Hello", "Purely for testing purposes", true)

    override fun onEnable() {
        if (testSetting.value) {
            println("Hello, my bind is $key")
        } else {
            println("My bind is $key")
        }
    }

    override fun onDisable() {
        super.onDisable()
        OpenSigma.instance.eventBus.unsubscribe(this)
        renderer?.close()
        renderer = null
    }

    @EventHandler
    fun on2D(event: Render2DEvent) {
        if (renderer == null)
            renderer = FontRenderer(font, 10f)
        renderer?.roundCoordinates(true)
        val theText = Text.literal("OpenSigma\nJello");
        var x = 5.0f
        var y = 5.0f
        val mat = event.context.matrices
        mat.push()
//		mat.scale(6, 6, 0)
        renderer?.drawText(mat, theText, x, y, 1f)
    }

}