package CCPortable.API;

import java.lang.reflect.Method;

public class CCPortableAPI {

    private static Class baseClass = null;
    private static Method register = null;

    public static void registerHandler(Class <? extends CCPortable.API.IPDACommandHandler> clz) {
        initBase();
        try {
            register.invoke(clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initBase() {
        try {
            baseClass = Class.forName( "CCPortable.common.CCPortable" );
            register = baseClass.getMethod("registerHandler");
        } catch(Exception e) {
            System.out.println("No main class found: CCPortable");
        }
    }
}