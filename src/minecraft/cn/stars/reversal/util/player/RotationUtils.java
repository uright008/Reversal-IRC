package cn.stars.reversal.util.player;

public class RotationUtils {
    public static Rotation rotation = new Rotation(0,0);
    public static Rotation prevRotation = new Rotation(0,0);
    public static void setRotation(Rotation rotation1) {
        prevRotation.setRotation(rotation);
        rotation.setRotation(rotation1);
    }
}
