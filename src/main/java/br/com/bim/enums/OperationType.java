package br.com.bim.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum OperationType {

    SUM("SUM"), SUBTRACT("SUBTRACT");

    private static final Map<String, OperationType> map = new HashMap<>();

    static {
        for (final OperationType operationType : OperationType.values()) {
            map.put(operationType.getType(), operationType);
        }
    }

    private final String type;

    public static boolean exists (final String type) {
        if (StringUtils.isEmpty(type)) {
            return false;
        }
        return map.containsKey(type);
    }
}
