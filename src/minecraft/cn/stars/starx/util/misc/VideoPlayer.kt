package cn.stars.starx.util.misc

import cn.stars.starx.util.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import java.io.File
import java.nio.IntBuffer
import javax.imageio.ImageIO

object VideoPlayer {
    private var currentFrame = 0
    var initialized = false
    init {

//        // 视频文件路径
        val videoFilePath = "D:/output.mp4"


        // 输出图片文件夹路径
        val outputFolderPath = "D:/images/"


        // 创建输出文件夹
        val outputFolder = File(outputFolderPath)
        if (!outputFolder.exists()) {
            outputFolder.mkdirs()
        }

        val grabber = FFmpegFrameGrabber(videoFilePath)
        try {
            grabber.start()
            println("Start")

            var frame: Frame?
            var i = 0
            val converter = Java2DFrameConverter()
            while ((grabber.grab().also { frame = it }) != null) {

                val image = converter.convert(frame) ?: continue

                val imagePath = outputFolderPath + "image_" + i + ".jpg"
                val outputFile = File(imagePath)
                if (!outputFile.exists()) {
                    ImageIO.write(image, "jpg", outputFile)

                    i++
                    println(i)
                }
            }
            grabber.stop()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun draw(width: Int, height: Int) {
        val maxFrames = File("D:/images").listFiles()?.size!! - 1
        if (!initialized) {
            println("Should load: $maxFrames")
            for(i in 0..maxFrames) {
                val image = ImageIO.read(File("D:/images/image_$i.jpg"))
                val dynamic = DynamicTexture(image)
                Minecraft.getMinecraft().textureManager.loadTexture(ResourceLocation("nmsl_$i"), dynamic)
                println("Try to load: $i")
            }
            initialized = true
        }
        if (currentFrame < maxFrames) {
            ++currentFrame
        } else {
            currentFrame = 0
        }
        RenderUtils.drawImage(ResourceLocation("nmsl_$currentFrame"), 0, 0, width, height)
    }
}