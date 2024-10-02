package cn.stars.addons.optimization.entityculling;

public interface Cullable {

    public void setTimeout();
    public boolean isForcedVisible();

    public void setCulled(boolean value);
    public boolean isCulled();

    public void setOutOfCamera(boolean value);
    public boolean isOutOfCamera();

}