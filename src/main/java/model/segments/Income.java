package model.segments;

import java.util.HashMap;
import java.util.Map;

public enum Income {
    HIGH(1, "High"), MEDIUM(2, "Medium"), LOW(3, "Low");

    public final int idx;
    public final String label;

    private static final Map<String, Income> BY_LABEL = new HashMap<>();
    private static final Map<Integer, Income> BY_IDX = new HashMap<>();

    static {
        for (Income e: values()) {
            BY_LABEL.put(e.label, e);
            BY_IDX.put(e.idx, e);
        }
    }

    private Income(int idx, String label) {
        this.idx = idx;
        this.label = label;
    }

    public static Income valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    public static Income valueOfIdx(int idx) {
        return BY_IDX.get(idx);
    }

}
