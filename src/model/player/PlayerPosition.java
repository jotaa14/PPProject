package model.player;

import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;

public class PlayerPosition implements IPlayerPosition {
    private PlayerPositionType type;


    public PlayerPosition(String type) {
      this.type = PlayerPositionType.fromString(type);
    }


    @Override
    public String getDescription() {
        return type.getPlayerPositionType();
    }

}
