package model.player;

public enum PlayerPositionType {
    GOALKEEPER,
    DEFENDER,
    MIDFIELDER,
    FORWARD;

    public String getPlayerPositionType() {

        return this.name();
    }

    public static PlayerPositionType fromString(String type) {
        for(PlayerPositionType p : values()) {
            if (p.name().equalsIgnoreCase(type)) {
                return p;
            }
        }

        throw new IllegalArgumentException("No constant with text " + type + " found");
    }

    public static String getPlayerPositionType(String type) {
        for(PlayerPositionType p : values()) {
            if (p.name().equalsIgnoreCase(type)) {
                return p.name();
            }
        }

        throw new IllegalArgumentException("No constant with text " + type + " found");
    }
}
