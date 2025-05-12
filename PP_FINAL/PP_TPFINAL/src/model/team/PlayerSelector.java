package model.team;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;

public class PlayerSelector implements IPlayerSelector {

    @Override
    public IPlayer selectPlayer(IClub iClub, IPlayerPosition iPlayerPosition) {
        if (iClub == null || iPlayerPosition == null) {
            throw new IllegalArgumentException("Clube ou posição não pode ser nulo.");
        }

        IPlayer[] players = iClub.getPlayers(); // assumindo que retorna um array

        if (players == null || players.length == 0) {
            throw new IllegalStateException("O clube não possui jogadores.");
        }

        for (int i = 0; i < players.length; i++) {
            if (iPlayerPosition.equals(players[i].getPosition())) {
                return players[i];
            }
        }

        throw new IllegalStateException("Nenhum jogador encontrado para a posição especificada.");
    }
}
