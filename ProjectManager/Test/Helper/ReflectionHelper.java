package Helper;

import application.ProjectManager;

import javax.swing.*;
import java.lang.reflect.Field;

/**
 * Created by Sam on 2015-03-31.
 */
public class ReflectionHelper {

    public static <T> T getElement(String elementName, Class sourceClass, ProjectManager pm) throws NoSuchFieldException, IllegalAccessException{
        Field f = sourceClass.getDeclaredField(elementName);
        f.setAccessible(true);
        T elem;
        if (pm.getActivePanel() instanceof JTabbedPane){
            elem = (T) f.get(((JTabbedPane)pm.getActivePanel()).getComponent(0));
        }else{
            elem = (T) f.get(pm.getActivePanel());

        }
        return elem;
    }

}
