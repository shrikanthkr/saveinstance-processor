
package generator;

import helpers.AnnotatedField;

/**
 * Created by shrikanth on 11/19/16.
 */

public class StringGenerator extends SaveLoadBaseGenerator {

    public StringGenerator(AnnotatedField field) {
        super(field);
    }

    @Override
    public String generateWrite(String bundleName, String key, String value) {
        return writeCode("putString", bundleName, key, value);
    }

    @Override
    public String generateRead(String bundleName, String key, String param) {
        return readCode("getString", bundleName, key, param);
    }
}
