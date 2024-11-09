package cn.stars.reversal.util.animation.simple;

import cn.stars.reversal.util.math.TimeUtil;

public class AnimationUtils {
    private static float defaultSpeed;
    private final TimeUtil timerUtil;

    public AnimationUtils() {
        this.timerUtil = new TimeUtil();
    }

    public static float calculateCompensation(final float target, float current, long delta, final double speed) {
        final float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (delta > 1000L) {
            delta = 16L;
        }
        if (diff > speed) {
            final double xD = Math.max(speed * delta / 16.0, 0.5);
            if ((current -= (float) xD) < target) {
                current = target;
            }
        } else if (diff < -speed) {
            final double xD = Math.max(speed * delta / 16.0, 0.5);
            if ((current += (float) xD) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float) (0.01 * speed);
        animation = animation < finalState ? (Math.min(animation + add, finalState)) : (Math.max(animation - add, finalState));
        return animation;
    }

    public static float getAnimationState(float animation, float finalState, float speed) {
        float add = (float) (0.01 * speed);
        animation = animation < finalState ? (Math.min(animation + add, finalState)) : (Math.max(animation - add, finalState));
        return animation;
    }

    public static float calculateCompensation(final float target, float current, final double speed, long delta) {

        final float diff = current - target;

        double add =  (delta * (speed / 50));

        if (diff > speed){
            if(current - add > target) {
                current -= add;
            }else {
                current = target;
            }
        }
        else if (diff < -speed) {
            if(current + add < target) {
                current += add;
            }else {
                current = target;
            }
        }
        else{
            current = target;
        }

        return current;
    }

    public float moveUD(final float current, final float end, final float minSpeed) {
        return this.moveUD(current, end, AnimationUtils.defaultSpeed, minSpeed);
    }

    public double animate(final double target, double current, double speed) {
        if (this.timerUtil.hasReached(4000L)) {
            final boolean larger = target > current;
            if (speed < 0.0) {
                speed = 0.0;
            } else if (speed > 1.0) {
                speed = 1.0;
            }
            final double dif = Math.max(target, current) - Math.min(target, current);
            double factor = dif * speed;
            if (factor < 0.1) {
                factor = 0.1;
            }
            current = (larger ? (current + factor) : (current - factor));
            this.timerUtil.reset();
        }
        return current;
    }

    public float animate(final float target, float current, float speed) {
        if (this.timerUtil.hasReached(4000L)) {
            final boolean larger = target > current;
            if (speed < 0.0f) {
                speed = 0.0f;
            } else if (speed > 1.0) {
                speed = 1.0f;
            }
            final float dif = Math.max(target, current) - Math.min(target, current);
            float factor = dif * speed;
            if (factor < 0.1f) {
                factor = 0.1f;
            }
            current = (larger ? (current + factor) : (current - factor));
            this.timerUtil.reset();
        }
        if (Math.abs(current - target) < 0.2) {
            return target;
        }
        return current;
    }

    public float moveUD(final float current, final float end, final float smoothSpeed, final float minSpeed) {
        float movement = 0.0f;
        if (this.timerUtil.hasReached(20000L)) {
            movement = (end - current) * smoothSpeed;
            if (movement > 0.0f) {
                movement = Math.max(minSpeed, movement);
                movement = Math.min(end - current, movement);
            } else if (movement < 0.0f) {
                movement = Math.min(-minSpeed, movement);
                movement = Math.max(end - current, movement);
            }
            this.timerUtil.reset();
        }
        return current + movement;
    }

    static {
        AnimationUtils.defaultSpeed = 0.125f;
    }
}
