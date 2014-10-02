package io.dwak.holohackernews.app.manager.hackernews;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
* Created by vishnu on 10/1/14.
*/
public class LongTypeAdapter implements JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if ("".equals(json.getAsString())) {
            return 0l;
        }
        else {
            return json.getAsLong();
        }
    }
}
