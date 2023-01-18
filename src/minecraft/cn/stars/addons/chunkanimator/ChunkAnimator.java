package cn.stars.addons.chunkanimator;

public class ChunkAnimator{

    public static ChunkAnimator INSTANCE = new ChunkAnimator();

    public AnimationHandler animationHandler;
    public int mode = 1;

    public ChunkAnimator()
    {
        animationHandler = new AnimationHandler();
    }
}