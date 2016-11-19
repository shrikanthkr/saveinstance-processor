package generator;

/**
 * Created by shrikanth on 11/19/16.
 */

public class BooleanGenerator extends SaveLoadBaseGenerator {

    @Override
    public String generateWrite(String bundleName, String key, String value) {
        return writeCode("putBoolean", bundleName, key, value);
    }

    @Override
    public String generateRead(String bundleName, String key, String param) {
        return readCode("getBoolean", bundleName, key, param);
    }
}
