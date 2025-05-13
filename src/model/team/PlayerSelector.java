package model.team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;

public class PlayerSelector implements IPlayerSelector {

   @Override
    public IPlayer selectPlayer(IClub club, IPlayerPosition position) {
       if (club == null || position == null) {
           throw new IllegalArgumentException("Club or Position cant 't be null");
       }

       IPlayer[] players = club.getPlayers();

       if (players.length == 0) {
           throw new IllegalArgumentException("Club is empty");
       }

       for (IPlayer player : players) {
           if (position.getDescription().equalsIgnoreCase(position.getDescription())) {
               return player;
           }
       }
       throw new IllegalArgumentException("No player found for the specified position" +  position.getDescription());
   }
}