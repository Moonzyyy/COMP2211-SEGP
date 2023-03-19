package core.segments;

import java.util.HashMap;
import java.util.Map;

public enum Context {
    NEWS(1, "News"), SHOPPING(2, "Shopping"), SOCIAL(3, "Social Media"), BLOG(4, "Blog"), HOBBIES(5, "Hobbies"), TRAVEL(6, "Travel");

    public final int idx;
    public final String label;

    private static final Map<String, Context> BY_LABEL = new HashMap<>();
    private static final Map<Integer, Context> BY_IDX = new HashMap<>();

    static {
        for (Context e: values()) {
            BY_LABEL.put(e.label, e);
            BY_IDX.put(e.idx, e);
        }
    }

    private Context(int idx, String label) {
        this.idx = idx;
        this.label = label;
    }

    public static Context valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    public static Context valueOfIdx(int idx) {
        return BY_IDX.get(idx);
    }

}
