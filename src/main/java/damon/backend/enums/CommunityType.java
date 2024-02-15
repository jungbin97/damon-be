package damon.backend.enums;

public enum CommunityType {
    번개("번개"),
    자유("자유");

    private final String type;

    CommunityType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}