package data;

import com.ppstudios.footballmanager.api.contracts.data.IExporter;
import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import model.event.PlayerEvent;
import model.league.Season;
import model.player.Player;
import model.event.Event;
import model.team.Club;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class Exporter implements IExporter {

    private ILeague[] leagues;

    @Override
    public void exportToJson() throws IOException {
        JSONArray leaguesJson = leaguesToJsonArray(leagues);

        try (FileWriter file = new FileWriter("./files/leagues.json")) {
            file.write(leaguesJson.toJSONString());
            file.flush();
            System.out.println("League exported successfully to JSON file.");
        }
    }

    private JSONArray leaguesToJsonArray(ILeague[] leagues) {
        JSONArray leaguesArray = new JSONArray();
        for (ILeague league : leagues) {
            if(league != null){
                leaguesArray.add(leagueToJsonObject(league));
            }
        }
        return leaguesArray;
    }

    private JSONObject leagueToJsonObject(ILeague league) {
        JSONObject leagueJson = new JSONObject();
        leagueJson.put("name", league.getName());
        leagueJson.put("seasons", seasonToJsonArray(league.getSeasons()));
        return leagueJson;
    }

    private JSONArray seasonToJsonArray(ISeason[] seasons) {
        JSONArray seasonsArray = new JSONArray();
        for (ISeason season : seasons) {
            if(season != null){
                seasonsArray.add(seasonToJsonObject(season));
            }
        }
        return seasonsArray;
    }

    private JSONObject seasonToJsonObject(ISeason season) {
        JSONObject seasonJson = new JSONObject();
        seasonJson.put("name", season.getName());
        seasonJson.put("year", season.getYear());
        seasonJson.put("current_round", season.getCurrentRound());
        seasonJson.put("max_teams", season.getMaxTeams());
        seasonJson.put("clubs", clubsToJsonArray(season.getCurrentClubs()));
        seasonJson.put("teams", teamsToJsonArray((Season)season).getTeams());
        seasonJson.put("matches", matchesToJsonArray(season.getMatches()));
        seasonJson.put("schedule", scheduleToJsonObject(season.getSchedule()));
        seasonJson.put("standings", standingsToJsonArray(season.getLeagueStandings()));

        return seasonJson;
    }

    private JSONArray standingsToJsonArray(IStanding[] standings) {
        JSONArray standingsArray = new JSONArray();
        for (IStanding standing : standings) {
            if(standing != null){
                standingsArray.add(standingToJsonObject(standing));
            }
        }
        return standingsArray;
    }

    private JSONObject standingToJsonObject(IStanding standing) {
        JSONObject standingJson = new JSONObject();
        standingJson.put("team", teamToJsonObject(standing.getTeam()));
        standingJson.put("points", standing.getPoints());
        standingJson.put("wins", standing.getWins());
        standingJson.put("draws", standing.getDraws());
        standingJson.put("losses", standing.getLosses());
        standingJson.put("goals_scored", standing.getGoalScored());
        standingJson.put("goals_conceded", standing.getGoalsConceded());

        return standingJson;
    }

    private JSONArray matchesToJsonArray(IMatch[] matches) {
        JSONArray matchesJsonArray = new JSONArray();
        for (IMatch match : matches) {
            if(match != null){
                matchesJsonArray.add(matchToJsonObject(match));
            }
        }
        return matchesJsonArray;
    }

    private JSONObject matchToJsonObject(IMatch match) {
        JSONObject matchJsonObject = new JSONObject();
        matchJsonObject.put("home_team", teamToJsonObject(match.getHomeTeam()));
        matchJsonObject.put("away_team", teamToJsonObject(match.getAwayTeam()));
        matchJsonObject.put("round", match.getRound());
        matchJsonObject.put("events", eventsToJsonArray(match.getEvents()));
        matchJsonObject.put("is_played", match.isPlayed());
        return matchJsonObject;
    }

    private Object scheduleToJsonObject(ISchedule schedule) {
        JSONObject scheduleJson = new JSONObject();
        scheduleJson.put("number_of_rounds", schedule.getNumberOfRounds());
        scheduleJson.put("max_matches_per_round", schedule.getMaxMatchesPerRound());
        scheduleJson.put("matches", matchesToJsonArray(schedule.getAllMatches()));
        return scheduleJson;
    }

    private JSONArray teamsToJsonArray(ITeam[] teams) {
        JSONArray teamsArray = new JSONArray();
        for (ITeam team : teams) {
            if(team != null){
                teamsArray.add(teamToJsonObject(team));
            }
        }
        return teamsArray;
    }

    private JSONObject teamToJsonObject(ITeam team) {
        JSONObject teamJson = new JSONObject();
        teamJson.put("formation", team.getFormation().getDisplayName());
        teamJson.put("club", clubToJsonObject((Club) team.getClub()));
        teamJson.put("players", playersToJsonArray(team.getPlayers()));

        return teamJson;
    }

    private JSONArray clubsToJsonArray(IClub[] clubs) {
        JSONArray clubsJson = new JSONArray();

        for (IClub club : clubs) {
            if(club != null){
                clubsJson.add(clubToJsonObject((Club) club));
            }
        }
        return clubsJson;
    }

    private JSONObject clubToJsonObject(Club club) {
        JSONObject clubJson = new JSONObject();
        clubJson.put("name", club.getName());
        clubJson.put("code", club.getCode());
        clubJson.put("stadium", club.getStadiumName());
        clubJson.put("logo", club.getLogo());
        clubJson.put("country", club.getCountry());
        clubJson.put("foundedYear", club.getFoundedYear());
        clubJson.put("isNationalTeam", club.isNationalTeam());
        clubJson.put("players", playersToJsonArray(club.getPlayers()));
        return clubJson;
    }

    private JSONArray playersToJsonArray(IPlayer[] players) {
        JSONArray playersJson = new JSONArray();

        for (IPlayer player : players) {
            playersJson.add(playerToJsonObject((Player) player));
        }
        return playersJson;
    }

    private JSONObject playerToJsonObject(Player player) {
        JSONObject playerJson = new JSONObject();
        playerJson.put("name", player.getName());
        playerJson.put("position", player.getPosition().getDescription());
        playerJson.put("age", player.getAge());
        playerJson.put("number", player.getNumber());
        playerJson.put("shooting", player.getShooting());
        playerJson.put("passing", player.getPassing());
        playerJson.put("stamina", player.getStamina());
        playerJson.put("speed", player.getSpeed());
        playerJson.put("defense", player.getDefense());
        playerJson.put("goalkeeping", player.getGoalkeeping());
        playerJson.put("height", player.getHeight());
        playerJson.put("weight", player.getWeight());
        playerJson.put("nationality", player.getNationality());
        playerJson.put("preferredFoot", player.getPreferredFoot());
        playerJson.put("photo", player.getPhoto());
        playerJson.put("birthDate", player.getBirthDate().toString());
        playerJson.put("clubCode", player.getClub());
        playerJson.put("strength", player.getStrength());

        return playerJson;
    }

    public JSONArray eventsToJsonArray(IEvent[] events) {
        JSONArray eventsJson = new JSONArray();
        for (IEvent event : events) {
            if(event != null){
                eventsJson.add(eventToJsonObject((Event)event));
            }
        }
        return eventsJson;
    }

    private JSONObject eventToJsonObject(Event event) {
        JSONObject eventJson = new JSONObject();
        eventJson.put("type", event.getType());
        eventJson.put("minute", event.getMinute());
        eventJson.put("description", event.getDescription());
        if(event instanceof PlayerEvent){
            eventJson.put("player", playerToJsonObject((Player)((PlayerEvent) event).getPlayer()));
        }
        return eventJson;
    }
}