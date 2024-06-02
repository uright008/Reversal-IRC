package cn.stars.starx.util.shader.base;

import cn.stars.starx.StarX;
import cn.stars.starx.util.StarXLogger;
import org.lwjgl.opengl.*;
import java.io.*;
import java.nio.charset.*;

public class ShaderToy
{
    private final int programId;
    private final int timeUniform;
    private final int mouseUniform;
    private final int resolutionUniform;

    public ShaderToy(final String fragmentShaderLocation) throws IOException {
        final int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, this.createShader(ShaderToy.class.getResourceAsStream("/assets/minecraft/starx/shader/passthrough.vsh"), 35633));
        GL20.glAttachShader(program, this.createShader(ShaderToy.class.getResourceAsStream("/assets/minecraft/starx/shader/main_menu/" + fragmentShaderLocation), 35632));
        GL20.glLinkProgram(program);
        final int linked = GL20.glGetProgrami(program, 35714);
        if (linked == 0) {
            System.err.println(GL20.glGetProgramInfoLog(program, GL20.glGetProgrami(program, 35716)));
            StarXLogger.fatal("Error while loading shaders", new IllegalStateException("Failed to link shader"));
            StarX.INSTANCE.isAMDShaderCompatibility = true;
        }
        GL20.glUseProgram(this.programId = program);
        this.timeUniform = GL20.glGetUniformLocation(program, "time");
        this.mouseUniform = GL20.glGetUniformLocation(program, "mouse");
        this.resolutionUniform = GL20.glGetUniformLocation(program, "resolution");
        GL20.glUseProgram(0);
    }

    public void useShader(final int width, final int height, final float time) {
        if (StarX.INSTANCE.isAMDShaderCompatibility) return;
        GL20.glUseProgram(this.programId);
        GL20.glUniform2f(this.resolutionUniform, (float)width, (float)height);
        GL20.glUniform2f(this.mouseUniform, (float)width, 1.0f - height);
        GL20.glUniform1f(this.timeUniform, time);
    }

    public void useShader(final int width, final int height, final float time, final int mouseX, final int mouseY) {
        if (StarX.INSTANCE.isAMDShaderCompatibility) return;
        GL20.glUseProgram(this.programId);
        GL20.glUniform2f(this.resolutionUniform, (float) width, (float) height);
        GL20.glUniform2f(this.mouseUniform, (float) mouseX,(float) mouseY);
        GL20.glUniform1f(this.timeUniform, time);
    }

    private int createShader(final InputStream inputStream, final int shaderType) throws IOException {
        final int shader = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shader, this.readStreamToString(inputStream));
        GL20.glCompileShader(shader);
        final int compiled = GL20.glGetShaderi(shader, 35713);
        if (compiled == 0) {
            System.err.println(GL20.glGetShaderInfoLog(shader, GL20.glGetShaderi(shader, 35716)));
            StarXLogger.fatal("Error while loading shaders", new IllegalStateException("Failed to compile shader"));
            StarX.INSTANCE.isAMDShaderCompatibility = true;
            return 0;
        }
        return shader;
    }

    private String readStreamToString(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[512];
        int read;
        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, read);
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
}
