package generator;

import helpers.AnnotatedField;

/**
 * Created by shrikanth on 11/19/16.
 */

public abstract class SaveLoadBaseGenerator  {
    AnnotatedField field;

    public SaveLoadBaseGenerator(AnnotatedField field) {
        this.field = field;
    }

    public abstract String generateWrite(String bundleName, String key, String value);
    public abstract String generateRead(String bundleName, String key, String param);

    public String writeCode(String methodName, String bundleName, String key, String value){
        return String.format(bundleName+".%s(\"%s\", %s)", methodName, key, value);
    }

    public String readCode(String methodName, String bundleName, String key, String param){
        return String.format("%s = " + getTypeCast() + bundleName + ".%s(\"%s\")",param, methodName, key);
    }

    public String getTypeCast(){
        return "";
    }
}
