package de.paul.triebel.billmaker.assets;

import java.io.InputStream;

public class Assets {

    public static InputStream getFile(String name) {
        return Assets.class.getResourceAsStream(name);
    }

    public static String getResourceExternal(String name) {
        return Assets.class.getResource(name).toExternalForm();
    }
}
