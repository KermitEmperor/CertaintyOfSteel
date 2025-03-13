package net.kermir.certaintyofsteel.util.json;


import com.google.gson.*;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import org.jetbrains.annotations.Nullable;

import javax.json.Json;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class JsonOptionalObject extends JsonElement {
    private final LinkedTreeMap<String, JsonElement> members = new LinkedTreeMap();

    public JsonOptionalObject() {
    }

    public JsonOptionalObject(JsonObject object) {
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            this.members.put(entry.getKey(), entry.getValue().deepCopy());
        }
    }

    public com.google.gson.JsonObject deepCopy() {
        com.google.gson.JsonObject result = new com.google.gson.JsonObject();
        Iterator var2 = this.members.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry)var2.next();
            result.add((String)entry.getKey(), ((JsonElement)entry.getValue()).deepCopy());
        }

        return result;
    }

    public void add(String property, JsonElement value) {
        this.members.put(property, value == null ? JsonNull.INSTANCE : value);
    }

    public JsonElement remove(String property) {
        return (JsonElement)this.members.remove(property);
    }

    public void addProperty(String property, String value) {
        this.add(property, (JsonElement)(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
    }

    public void addProperty(String property, Number value) {
        this.add(property, (JsonElement)(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
    }

    public void addProperty(String property, Boolean value) {
        this.add(property, (JsonElement)(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
    }

    public void addProperty(String property, Character value) {
        this.add(property, (JsonElement)(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
    }

    public Set<Map.Entry<String, JsonElement>> entrySet() {
        return this.members.entrySet();
    }

    public Set<String> keySet() {
        return this.members.keySet();
    }

    public int size() {
        return this.members.size();
    }

    public boolean has(String memberName) {
        return this.members.containsKey(memberName);
    }

    public Optional<JsonElement> ifHas(String memberName) {
        if (this.has(memberName))
            return Optional.of(get(memberName));
        else
            return Optional.empty();
    }

    public void ifHas(String memberName, Consumer<JsonElement> consumer) {
        this.ifHas(memberName).ifPresent(consumer);
    }

    public JsonElement get(String memberName) {
        return (JsonElement)this.members.get(memberName);
    }

    public Integer getInt(String memberName, @Nullable Integer onNone) {
        JsonElement element = this.members.get(memberName);
        return element != null ? Integer.valueOf(element.getAsInt()) : onNone;
    }

    public JsonPrimitive getAsJsonPrimitive(String memberName) {
        return (JsonPrimitive)this.members.get(memberName);
    }

    public JsonArray getAsJsonArray(String memberName) {
        return (JsonArray)this.members.get(memberName);
    }

    public com.google.gson.JsonObject getAsJsonObject(String memberName) {
        return (com.google.gson.JsonObject)this.members.get(memberName);
    }

    public JsonOptionalObject getAsJsonOptionalObject(String memberName) {
        return (JsonOptionalObject) this.members.get(memberName);
    }

    public boolean equals(Object o) {
        return o == this || o instanceof JsonOptionalObject && ((JsonOptionalObject)o).members.equals(this.members);
    }

    public int hashCode() {
        return this.members.hashCode();
    }
}

