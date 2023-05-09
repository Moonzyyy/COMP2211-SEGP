package view.components;

public record CompareItem(String label, String value) {
    @Override
    public String toString() {
        return label;
    }
}
