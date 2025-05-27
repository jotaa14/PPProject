package model.player;

public enum PlayerPositionType {
    GOALKEEPER,
    DEFENDER,
    MIDFIELDER,
    FORWARD;

    public static PlayerPositionType fromString(String text) {
        for (PlayerPositionType type : PlayerPositionType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid position type: " + text);
    }

    public String getPlayerPositionType() {
        return this.name();
    }
}
