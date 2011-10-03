/*
* @author  Oleg Varaksin (ovaraksin@googlemail.com)
* $$Id$$
*/

package org.primefaces.extensions.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
* Singleton instance of Gson {@link http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html}.
*/
public class GsonConverter
{
    private static final GsonConverter INSTANCE = new GsonConverter();
    private Gson gson;

    private GsonConverter() {
        GsonBuilder gsonBilder = new GsonBuilder();
        gsonBilder.serializeNulls();
        gson = gsonBilder.create();
    }

    public static Gson getGson() {
        return INSTANCE.gson;
    }
}
