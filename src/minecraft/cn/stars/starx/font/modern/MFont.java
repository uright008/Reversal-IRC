package cn.stars.starx.font.modern;

public abstract class MFont {
    public abstract int drawString(String text, double x, double y, int color, boolean dropShadow);

    public abstract int drawString(final String text, final double x, final double y, final int color);

    public abstract int drawStringWithShadow(final String text, final double x, final double y, final int color);

    public abstract int width(String text);

    public int getWidth(String text) { return width(text); }

    public int getStringWidth(String text) { return width(text); }

    public abstract int drawCenteredString(final String text, final double x, final double y, final int color);

    public abstract int drawRightString(final String text, final double x, final double y, final int color);

    public abstract float height();

    public float getHeight() { return height(); }
}
