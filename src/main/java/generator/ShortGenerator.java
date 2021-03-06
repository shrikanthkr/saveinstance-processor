
package generator;

import helpers.AnnotatedField;

/**
 * Created by shrikanth on 11/19/16.
 */

public class ShortGenerator extends SaveLoadBaseGenerator {

    public ShortGenerator(AnnotatedField field) {
        super(field);
    }

    @Override
    public String generateWrite(String bundleName, String key, String value) {
        return writeCode("putShort", bundleName, key, value);
    }

    @Override
    public String generateRead(String bundleName, String key, String param) {
        return readCode("getShort", bundleName, key, param);
    }
}
