package main;

import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.player.IPlayerPosition;
import com.ppstudios.footballmanager.api.contracts.player.PreferredFoot;
import com.ppstudios.footballmanager.api.contracts.team.IPlayerSelector;
import model.player.Player;
import model.player.PlayerPosition;
import model.team.Club;

import java.time.LocalDate;

public class Main {
    //TODO DAR RUN E VER O QUE TENS DE ARRANJAR odel.player.PlayerPosition@682a0b20 e Preferred Foot: BothStrength82
    public static void main(String[] args) {
        // Criando algumas posições de jogadores
        IPlayerPosition forward = new PlayerPosition("Forward");
        IPlayerPosition midfielder = new PlayerPosition("Midfielder");
        IPlayerPosition goalkeeper = new PlayerPosition("Goalkeeper");

        // Criando jogadores
        Player player1 = new Player(
                "Cristiano Ronaldo",
                LocalDate.of(1985, 2, 5),
                "Portuguese",
                forward,
                "cr7.jpg",
                7,
                95,  // shooting
                85,  // passing
                80,  // stamina
                85,  // speed
                1.87f, // height
                84.0f, // weight
                PreferredFoot.Right
        );

        Player player2 = new Player(
                "Lionel Messi",
                LocalDate.of(1987, 6, 24),
                "Argentine",
                midfielder,
                "messi.jpg",
                10,
                90,  // shooting
                92,  // passing
                85,  // stamina
                80,  // speed
                1.70f, // height
                72.0f, // weight
                PreferredFoot.Left
        );

        Player player3 = new Player(
                "Manuel Neuer",
                LocalDate.of(1986, 3, 27),
                "German",
                goalkeeper,
                "neuer.jpg",
                1,
                85,  // shooting
                80,  // passing
                90,  // stamina
                75,  // speed
                1.93f, // height
                92.0f, // weight
                PreferredFoot.Both
        );

        // Criando um clube
        Club club = new Club("FC Barcelona", "FCB", "Spain", 1899, false, "barcelona_logo.jpg", "Camp Nou");

        // Adicionando jogadores ao clube
        club.addPlayer(player1);
        club.addPlayer(player2);
        club.addPlayer(player3);

        // Imprimindo informações do clube e jogadores
        System.out.println(club);

        // Verificando se o clube é válido
        try {
            if (club.isValid()) {
                System.out.println("The club is valid.");
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Testando a remoção de um jogador
        try {
            club.removePlayer(player2);
            System.out.println("\nAfter removing Messi:");
            System.out.println(club);
        } catch (IllegalArgumentException e) {
            System.out.println("Error removing player: " + e.getMessage());
        }

        // Testando a seleção de um jogador (Exemplo de como um seletor pode ser usado)
        // Vamos imaginar que temos um seletor que escolhe o jogador baseado em uma posição
        IPlayerSelector selector = (team, position) -> {
            for (IPlayer p : team.getPlayers()) {
                if (p.getPosition().getDescription().equalsIgnoreCase(position.getDescription())) {
                    return p;
                }
            }
            return null;
        };

        // Selecionando um jogador para a posição "Goalkeeper"
        IPlayer selectedGoalkeeper = club.selectPlayer(selector, goalkeeper);
        System.out.println("\nSelected Goalkeeper: " + selectedGoalkeeper.getName());
    }
}
