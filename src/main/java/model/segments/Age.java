package model.segments;

import java.util.HashMap;
import java.util.Map;

public enum Age {
    U25(1, "<25"), U34(2, "25-34"), U44(3, "35-44"), U54(4, "45-54"), O54(5, ">54");

    public final int idx;
    public final String label;

    private static final Map<String, Age> BY_LABEL = new HashMap<>();
    private static final Map<Integer, Age> BY_IDX = new HashMap<>();

    static {
        for (Age e: values()) {
            BY_LABEL.put(e.label, e);
            BY_IDX.put(e.idx, e);
        }
    }

    private Age(int idx, String label) {
        this.idx = idx;
        this.label = label;
    }

    public static Age valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    public static Age valueOfIdx(int idx) {
        return BY_IDX.get(idx);
    }

}
