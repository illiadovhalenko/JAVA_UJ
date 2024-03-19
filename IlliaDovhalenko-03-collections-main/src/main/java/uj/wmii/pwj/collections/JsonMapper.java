package uj.wmii.pwj.collections;

import java.util.*;

public interface JsonMapper {

    String toJson(Map<String, ?> map);

    static JsonMapper defaultInstance() {
        return new Json();
    }

}
class Json implements JsonMapper {
    StringBuilder result;

    Json() {
        result = new StringBuilder();
    }

    void all_types_apply(Object obj) {
        if (obj instanceof String) { apply((String) obj); return; }
        if (obj instanceof Map<?, ?>) { apply((Map<String, ?>) obj); return; }
        if (obj instanceof List<?>) { apply((List<Object>) obj); return; }
        apply_numbers(obj);
    }

    void apply_numbers(Object obj) {
        result.append(obj);
    }

    void apply(Map<String, ?> map) {
        result.append("{");
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            result.append("\"").append(entry.getKey()).append("\":");
            all_types_apply(entry.getValue());
            result.append(",");
        }
        result.deleteCharAt(result.lastIndexOf(","));
        result.append("}");
    }

    void apply(List<Object> list) {
        result.append("[");
        if (list.isEmpty()) {
            result.append("]");
            return;
        }
        for (Object obj : list) {
            all_types_apply(obj);
            result.append(",");
        }
        result.setCharAt(result.lastIndexOf(","), ']');
    }

    void apply(String str) {
        result.append("\"").append(String.join("\\\"", str.split("\""))).append("\"");
    }

    @Override
    public String toJson(Map<String, ?> map) {
        if (map == null || map.isEmpty())
            return "{}";
        all_types_apply(map);
        System.out.println(result.toString());
        return result.toString();
    }
}