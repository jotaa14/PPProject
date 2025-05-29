package data;

import com.ppstudios.footballmanager.api.contracts.data.IExporter;
import com.ppstudios.footballmanager.api.contracts.data.htmlgenerators.ClubHtmlGenerator;
import com.ppstudios.footballmanager.api.contracts.data.htmlgenerators.SeasonHtmlGenerator;
import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.league.ILeague;
import com.ppstudios.footballmanager.api.contracts.league.ISchedule;
import com.ppstudios.footballmanager.api.contracts.league.ISeason;
import com.ppstudios.footballmanager.api.contracts.league.IStanding;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.player.IPlayer;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;
import main.Util;
import model.event.PlayerEvent;
import model.league.Schedule;
import model.league.Season;
import model.player.Player;
import model.event.Event;
import model.team.Club;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles exporting league, season, club, match, and player data to JSON and HTML formats.
 * Implements the IExporter interface and provides methods for serializing game data
 * structures for persistence and reporting.
 *
 * @author Diogo Fernando Águia Costa
 * Number: 8240696
 * Class: LSIRC1 T1
 * @author João Pedro Martins Ribeiro
 * Number:8230157
 * Class: LSIRC1 T2
 */
public class Exporter implements IExporter {

    /**
     * Exports all loaded leagues and their nested data (seasons, clubs, teams, matches, standings, etc.)
     * to a JSON file at "./JSON/leagues.json".
     * Outputs success or error messages to the console.
     */
    @Override
    public void exportToJson() {
        try{
            JSONArray leaguesJson = leaguesToJsonArray(Util.getGameLeagues());

            try (FileWriter file = new FileWriter("./JSON/leagues.json")) {
                file.write(leaguesJson.toJSONString());
                file.flush();
                System.out.println("League exported successfully to JSON file.");
                file.close();
            }
        }catch (IOException ex){
            System.out.println("Error exporting leagues to JSON: " + ex.getMessage());
        }
    }

    /**
     * Converts an array of leagues to a JSON array.
     *
     * @param leagues Array of ILeague objects
     * @return JSONArray representing all leagues
     */
    private JSONArray leaguesToJsonArray(ILeague[] leagues) {
        JSONArray leaguesArray = new JSONArray();
        for (ILeague league : leagues) {
            if(league != null){
                leaguesArray.add(leagueToJsonObject(league));
            }
        }
        return leaguesArray;
    }

    /**
     * Converts a single league to a JSON object, including its seasons.
     *
     * @param league The league to convert
     * @return JSONObject representing the league
     */
    private JSONObject leagueToJsonObject(ILeague league) {
        JSONObject leagueJson = new JSONObject();
        leagueJson.put("name", league.getName());
        leagueJson.put("seasons", seasonToJsonArray(league.getSeasons()));
        return leagueJson;
    }

    /**
     * Converts an array of seasons to a JSON array.
     *
     * @param seasons Array of ISeason objects
     * @return JSONArray representing all seasons
     */
    private JSONArray seasonToJsonArray(ISeason[] seasons) {
        JSONArray seasonsArray = new JSONArray();
        for (ISeason season : seasons) {
            if(season != null){
                seasonsArray.add(seasonToJsonObject(season));
            }
        }
        return seasonsArray;
    }

    /**
     * Converts a single season to a JSON object, including clubs, teams, matches, schedule, and standings.
     *
     * @param season The season to convert
     * @return JSONObject representing the season
     */
    private JSONObject seasonToJsonObject(ISeason season) {
        JSONObject seasonJson = new JSONObject();
        seasonJson.put("name", season.getName());
        seasonJson.put("year", season.getYear());
        seasonJson.put("current_round", season.getCurrentRound());
        seasonJson.put("max_teams", season.getMaxTeams());
        seasonJson.put("clubs", clubsToJsonArray(season.getCurrentClubs()));
        seasonJson.put("teams", teamsToJsonArray(((Season)season).getTeams()));
        seasonJson.put("matches", matchesToJsonArray(season.getMatches()));
        seasonJson.put("schedule", scheduleToJsonObject(season.getSchedule()));
        seasonJson.put("standings", standingsToJsonArray(season.getLeagueStandings()));
        return seasonJson;
    }

    /**
     * Converts an array of standings to a JSON array.
     *
     * @param standings Array of IStanding objects
     * @return JSONArray representing all standings
     */
    private JSONArray standingsToJsonArray(IStanding[] standings) {
        JSONArray standingsArray = new JSONArray();
        for (IStanding standing : standings) {
            if(standing != null){
                standingsArray.add(standingToJsonObject(standing));
            }
        }
        return standingsArray;
    }

    /**
     * Converts a single standing to a JSON object, including the team and statistics.
     *
     * @param standing The standing to convert
     * @return JSONObject representing the standing
     */
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

    /**
     * Converts an array of matches to a JSON array.
     *
     * @param matches Array of IMatch objects
     * @return JSONArray representing all matches
     */
    private JSONArray matchesToJsonArray(IMatch[] matches) {
        JSONArray matchesJsonArray = new JSONArray();
        for (IMatch match : matches) {
            if(match != null){
                matchesJsonArray.add(matchToJsonObject(match));
            }
        }
        return matchesJsonArray;
    }

    /**
     * Converts a single match to a JSON object, including teams, round, events, and play status.
     *
     * @param match The match to convert
     * @return JSONObject representing the match
     */
    private JSONObject matchToJsonObject(IMatch match) {
        JSONObject matchJsonObject = new JSONObject();
        matchJsonObject.put("home_team", teamToJsonObject(match.getHomeTeam()));
        matchJsonObject.put("away_team", teamToJsonObject(match.getAwayTeam()));
        matchJsonObject.put("round", match.getRound());
        matchJsonObject.put("events", eventsToJsonArray(match.getEvents()));
        matchJsonObject.put("is_played", match.isPlayed());
        return matchJsonObject;
    }

    /**
     * Converts a schedule to a JSON object, including number of rounds, max matches per round, and all matches.
     *
     * @param schedule The schedule to convert
     * @return JSONObject representing the schedule
     */
    private Object scheduleToJsonObject(ISchedule schedule) {
        JSONObject scheduleJson = new JSONObject();
        scheduleJson.put("number_of_rounds", schedule.getNumberOfRounds());
        scheduleJson.put("max_matches_per_round", ((Schedule)schedule).getMaxMatchesPerRound());
        scheduleJson.put("matches", matchesToJsonArray(schedule.getAllMatches()));
        return scheduleJson;
    }

    /**
     * Converts an array of teams to a JSON array.
     *
     * @param teams Array of ITeam objects
     * @return JSONArray representing all teams
     */
    private JSONArray teamsToJsonArray(ITeam[] teams) {
        JSONArray teamsArray = new JSONArray();
        for (ITeam team : teams) {
            if(team != null){
                teamsArray.add(teamToJsonObject(team));
            }
        }
        return teamsArray;
    }

    /**
     * Converts a single team to a JSON object, including formation, club, and players.
     *
     * @param team The team to convert
     * @return JSONObject representing the team
     */
    private JSONObject teamToJsonObject(ITeam team) {
        JSONObject teamJson = new JSONObject();
        teamJson.put("formation", team.getFormation().getDisplayName());
        teamJson.put("club", clubToJsonObject((Club) team.getClub()));
        teamJson.put("players", playersToJsonArray(team.getPlayers()));
        return teamJson;
    }

    /**
     * Converts an array of clubs to a JSON array.
     *
     * @param clubs Array of IClub objects
     * @return JSONArray representing all clubs
     */
    private JSONArray clubsToJsonArray(IClub[] clubs) {
        JSONArray clubsJson = new JSONArray();
        for (IClub club : clubs) {
            if(club != null){
                clubsJson.add(clubToJsonObject((Club) club));
            }
        }
        return clubsJson;
    }

    /**
     * Converts a single club to a JSON object, including metadata, players, and team.
     *
     * @param club The club to convert
     * @return JSONObject representing the club
     */
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
        clubJson.put("team", clubteamToJsonObject(club.getTeam()));
        return clubJson;
    }

    /**
     * Converts a club's team to a JSON object, including formation and players.
     *
     * @param team The team to convert
     * @return JSONObject representing the club's team
     */
    private JSONObject clubteamToJsonObject(ITeam team) {
        JSONObject teamJson = new JSONObject();
        teamJson.put("formation", team.getFormation().getDisplayName());
        teamJson.put("players", playersToJsonArray(team.getPlayers()));
        return teamJson;
    }

    /**
     * Converts an array of players to a JSON array.
     *
     * @param players Array of IPlayer objects
     * @return JSONArray representing all players
     */
    private JSONArray playersToJsonArray(IPlayer[] players) {
        JSONArray playersJson = new JSONArray();
        for (IPlayer player : players) {
            playersJson.add(playerToJsonObject((Player) player));
        }
        return playersJson;
    }

    /**
     * Converts a single player to a JSON object, including all attributes.
     *
     * @param player The player to convert
     * @return JSONObject representing the player
     */
    private JSONObject playerToJsonObject(Player player) {
        JSONObject playerJson = new JSONObject();
        playerJson.put("name", player.getName());
        playerJson.put("position", player.getPosition().getDescription());
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
        playerJson.put("preferredFoot", player.getPreferredFoot().toString());
        playerJson.put("photo", player.getPhoto());
        playerJson.put("birthDate", player.getBirthDate().toString());
        playerJson.put("clubCode", player.getClub());
        return playerJson;
    }

    /**
     * Converts an array of events to a JSON array.
     *
     * @param events Array of IEvent objects
     * @return JSONArray representing all events
     */
    public JSONArray eventsToJsonArray(IEvent[] events) {
        JSONArray eventsJson = new JSONArray();
        for (IEvent event : events) {
            if(event != null){
                eventsJson.add(eventToJsonObject((Event)event));
            }
        }
        return eventsJson;
    }

    /**
     * Converts a single event to a JSON object, including type, minute, description, and player (if applicable).
     *
     * @param event The event to convert
     * @return JSONObject representing the event
     */
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

    /**
     * Generates HTML reports for each season and club using the corresponding HTML generators.
     * Creates output directories if they do not exist.
     * Outputs progress and error messages to the console.
     */
    public void exportHtmlReports() {
        ILeague[] leagues = Util.getGameLeagues();

        if (leagues == null || leagues.length == 0) {
            System.out.println("No alloys loaded for export.");
            return;
        }

        File seasonDir = new File("output/html/seasons/");
        File clubDir = new File("output/html/clubs/");
        if (!seasonDir.exists()) seasonDir.mkdirs();
        if (!clubDir.exists()) clubDir.mkdirs();

        for (ILeague league : leagues) {
            if (league == null) continue;

            for (ISeason season : league.getSeasons()) {
                if (season == null) continue;

                String seasonPath = "output/html/seasons/" + season.getName().replace(" ", "") + "" + season.getYear() + ".html";
                try {
                    SeasonHtmlGenerator.generate(season, seasonPath);
                } catch (Exception e) {
                    System.out.println("Error when generating seasonal HTML: " + season.getName());
                    e.printStackTrace();
                }

                for (IClub club : season.getCurrentClubs()) {
                    if (club == null) continue;

                    String clubPath = "output/html/clubs/" + club.getName().replace(" ", "_") + ".html";
                    try {
                        ClubHtmlGenerator.generate(club, clubPath);
                    } catch (Exception e) {
                        System.out.println("Error generating club HTML: " + club.getName());
                        e.printStackTrace();
                    }
                }
            }
        }

        System.out.println("HTML export completed.");
    }
}
