package generator;

import helpers.AnnotatedField;

/**
 * Created by shrikanth on 11/19/16.
 */

public class SerializableGenerator extends SaveLoadBaseGenerator {

    public SerializableGenerator(AnnotatedField field) {
        super(field);
    }

    @Override
    public String generateWrite(String bundleName, String key, String value) {
        return writeCode("putSerializable", bundleName, key, value);
    }

    @Override
    public String generateRead(String bundleName, String key, String param) {
        return readCode("getSerializable", bundleName, key, param);
    }

    @Override
    public String getTypeCast() {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(field.getFieldType()).append(")");
        return builder.toString();
    }
}
