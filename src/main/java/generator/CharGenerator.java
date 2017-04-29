
package generator;

/**
 * Created by shrikanth on 11/19/16.
 */

public class CharGenerator extends SaveLoadBaseGenerator {

    @Override
    public String generateWrite(String bundleName, String key, String value) {
        return writeCode("putChar", bundleName, key, value);
    }

    @Override
    public String generateRead(String bundleName, String key, String param) {
        return readCode("getChar", bundleName, key, param);
    }
}
