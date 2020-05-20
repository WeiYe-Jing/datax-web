package com.wugui.datax.rpc.util.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxueli 2018-11-30
 */
public class BasicJsonReader {
    private static Logger logger = LoggerFactory.getLogger(BasicJsonwriter.class);


    public Map<String, Object> parseMap(String json) {
        if (json != null) {
            json = json.trim();
            if (json.startsWith("{")) {
                return parseMapInternal(json);
            }
        }
        throw new IllegalArgumentException("Cannot parse JSON");
    }

    public List<Object> parseList(String json) {
        if (json != null) {
            json = json.trim();
            if (json.startsWith("[")) {
                return parseListInternal(json);
            }
        }
        throw new IllegalArgumentException("Cannot parse JSON");
    }


    private List<Object> parseListInternal(String json) {
        List<Object> list = new ArrayList<Object>();
        json = trimLeadingCharacter(trimTrailingCharacter(json, ']'), '[');
        for (String value : tokenize(json)) {
            list.add(parseInternal(value));
        }
        return list;
    }

    private Object parseInternal(String json) {
        if (json.equals("null")) {
            return null;
        }
        if (json.startsWith("[")) {
            return parseListInternal(json);
        }
        if (json.startsWith("{")) {
            return parseMapInternal(json);
        }
        if (json.startsWith("\"")) {
            return trimTrailingCharacter(trimLeadingCharacter(json, '"'), '"');
        }
        try {
            return Long.valueOf(json);
        } catch (NumberFormatException ex) {
            // ignore
        }
        try {
            return Double.valueOf(json);
        } catch (NumberFormatException ex) {
            // ignore
        }
        return json;
    }

    private Map<String, Object> parseMapInternal(String json) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        json = trimLeadingCharacter(trimTrailingCharacter(json, '}'), '{');
        for (String pair : tokenize(json)) {
            String[] values = trimArrayElements(split(pair, ":"));
            String key = trimLeadingCharacter(trimTrailingCharacter(values[0], '"'), '"');
            Object value = parseInternal(values[1]);
            map.put(key, value);
        }
        return map;
    }

    // append start
    private static String[] split(String toSplit, String delimiter) {
        if (toSplit != null && !toSplit.isEmpty() && delimiter != null && !delimiter.isEmpty()) {
            int offset = toSplit.indexOf(delimiter);
            if (offset < 0) {
                return null;
            } else {
                String beforeDelimiter = toSplit.substring(0, offset);
                String afterDelimiter = toSplit.substring(offset + delimiter.length());
                return new String[]{beforeDelimiter, afterDelimiter};
            }
        } else {
            return null;
        }
    }

    private static String[] trimArrayElements(String[] array) {
        if (array == null || array.length == 0) {
            return new String[0];
        } else {
            String[] result = new String[array.length];

            for (int i = 0; i < array.length; ++i) {
                String element = array[i];
                result[i] = element != null ? element.trim() : null;
            }

            return result;
        }
    }


    private List<String> tokenize(String json) {
        List<String> list = new ArrayList<>();
        int index = 0;
        int inObject = 0;
        int inList = 0;
        boolean inValue = false;
        boolean inEscape = false;
        StringBuilder build = new StringBuilder();
        while (index < json.length()) {
            char current = json.charAt(index);
            if (inEscape) {
                build.append(current);
                index++;
                inEscape = false;
                continue;
            }
            if (current == '{') {
                inObject++;
            }
            if (current == '}') {
                inObject--;
            }
            if (current == '[') {
                inList++;
            }
            if (current == ']') {
                inList--;
            }
            if (current == '"') {
                inValue = !inValue;
            }
            if (current == ',' && inObject == 0 && inList == 0 && !inValue) {
                list.add(build.toString());
                build.setLength(0);
            } else if (current == '\\') {
                inEscape = true;
            } else {
                build.append(current);
            }
            index++;
        }
        if (build.length() > 0) {
            list.add(build.toString());
        }
        return list;
    }

    // plugin util
    private static String trimTrailingCharacter(String string, char c) {
        if (string.length() > 0 && string.charAt(string.length() - 1) == c) {
            return string.substring(0, string.length() - 1);
        }
        return string;
    }

    private static String trimLeadingCharacter(String string, char c) {
        if (string.length() > 0 && string.charAt(0) == c) {
            return string.substring(1);
        }
        return string;
    }

}
