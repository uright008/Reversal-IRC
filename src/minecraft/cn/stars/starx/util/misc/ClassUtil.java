package cn.stars.starx.util.misc;

import org.apache.logging.log4j.core.config.plugins.ResolverUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassUtil {

    private static final Map<String, Boolean> cachedClasses = new HashMap<>();

    /**
     * Allows you to check for existing classes with the [className]
     */
    public static boolean hasClass(String className) {
        if (cachedClasses.containsKey(className)) {
            return cachedClasses.get(className);
        } else {
            try {
                Class.forName(className);
                cachedClasses.put(className, true);
                return true;
            } catch (ClassNotFoundException e) {
                cachedClasses.put(className, false);
                return false;
            }
        }
    }

    public static Object getObjectInstance(Class<?> clazz) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals("INSTANCE")) {
                return field.get(null);
            }
        }
        throw new IllegalAccessException("This class is not a Kotlin object");
    }


    /**
     * scan classes with specified superclass like what Reflections do but with log4j [ResolverUtil]
     * author: liulihaocai
     */
    public static <T> List<Class<? extends T>> resolvePackage(String packagePath, Class<T> klass) {
        ResolverUtil resolver = new ResolverUtil();

        resolver.setClassLoader(klass.getClassLoader());

        resolver.findInPackage(new ResolverUtil.ClassTest() {
            @Override
            public boolean matches(Class<?> type) {
                return true;
            }
        }, packagePath);

        List<Class<? extends T>> resultList = new ArrayList<>();

        for (Class<?> resolved : resolver.getClasses()) {
            if (klass.isAssignableFrom(resolved) && !resolved.isInterface() && !Modifier.isAbstract(resolved.getModifiers())) {
                boolean hasNativeMethod = false;
                for (Method method : resolved.getDeclaredMethods()) {
                    if (Modifier.isNative(method.getModifiers())) {
                        String klassName = resolved.getTypeName() + "." + method.getName();
                        throw new UnsatisfiedLinkError(klassName + "\n\tat " + klassName + "(Native Method)"); // 不允许包含本地方法
                    }
                }

                if (!hasNativeMethod) {
                    resultList.add((Class<? extends T>) resolved);
                }
            }
        }

        return resultList;
    }

    public static <T> List<T> instantiateList(List<Class<? extends T>> classes) {
        List<T> newList = new ArrayList<>();
        for (Class<? extends T> clazz : classes) {
            try {
                // 使用反射创建 Module 实例
                T obj = clazz.getDeclaredConstructor().newInstance();
                newList.add(obj);
            } catch (Exception e) {
                e.printStackTrace(); // 处理实例化异常
            }
        }
        return newList;
    }
}
