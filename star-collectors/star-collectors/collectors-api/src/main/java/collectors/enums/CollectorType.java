package collectors.enums;

public enum CollectorType {
    LIMITED_MULTI_ITEM("Limited_Multi"),
    LIMITED_SINGLE_ITEM("Limited_Single"),
    INFINITE_MULTI_ITEM("Inf_Multi"),
    INFINITE_SINGLE_ITEM("Inf_Single");

    private final String name;
    CollectorType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}