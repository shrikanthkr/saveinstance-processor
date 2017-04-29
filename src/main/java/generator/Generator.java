package generator;

import android.os.Parcelable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shrikanth on 11/19/16.
 */

public class Generator {
    static Map<String, Class> generatorMap = new HashMap();
    static {
        generatorMap.put("int", IntGenerator.class);
        generatorMap.put("java.lang.Integer", IntGenerator.class);

        generatorMap.put("java.lang.String", StringGenerator.class);

        generatorMap.put("java.lang.Boolean", BooleanGenerator.class);
        generatorMap.put("boolean", BooleanGenerator.class);

        generatorMap.put("java.lang.Byte", ByteGenerator.class);
        generatorMap.put("byte", ByteGenerator.class);

        generatorMap.put("java.lang.Character", CharGenerator.class);
        generatorMap.put("char", CharGenerator.class);

        generatorMap.put("java.lang.Short", ShortGenerator.class);
        generatorMap.put("short", ShortGenerator.class);

        generatorMap.put("float", FloatGenerator.class);
        generatorMap.put("java.lang.Float", FloatGenerator.class);

        generatorMap.put("java.lang.CharSequence", CharSequenceGenerator.class);
        generatorMap.put("android.os.Parcelable", ParcelableGenerator.class);

    }

    public  static SaveLoadBaseGenerator getGenerator(String type){
        Class generatorClass = generatorMap.get(type);
        try {
            return (SaveLoadBaseGenerator)generatorClass.getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
