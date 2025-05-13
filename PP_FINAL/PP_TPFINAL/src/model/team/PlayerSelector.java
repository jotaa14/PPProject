package model.team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;

public class PlayerSelector implements IPlayerSelector {
    IPlayer selectPlayer(IClub club, IPlayerPosition position){
        if(club == null || position == null){
            throw new IllegalArgumentException("club or position cant 't be null");
        }

        IPlayer[] players = club.getPlayers();

        if(position.getDescription().equalsIgnoreCase(""))

    }
}



