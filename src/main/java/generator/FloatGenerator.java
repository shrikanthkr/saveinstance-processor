package generator;

/**
 * Created by shrikanth on 11/19/16.
 */

public class FloatGenerator extends SaveLoadBaseGenerator {

    @Override
    public String generateWrite(String bundleName, String key, String value) {
        return writeCode("putFloat", bundleName, key, value);
    }

    @Override
    public String generateRead(String bundleName, String key, String param) {
        return readCode("getFloat", bundleName, key, param);
    }
}
