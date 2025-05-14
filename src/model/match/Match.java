package model.match;

import com.ppstudios.footballmanager.api.contracts.event.IEvent;
import com.ppstudios.footballmanager.api.contracts.match.IMatch;
import com.ppstudios.footballmanager.api.contracts.team.IClub;
import com.ppstudios.footballmanager.api.contracts.team.ITeam;

import java.io.IOException;

public class Match implements IMatch {

    ITeam homeTeam;
    ITeam awayTeam;
    boolean played  = false;

    @Override
    public IClub getHomeClub(){
        if(homeTeam == null){
            throw  new IllegalStateException("Home Club is not initialized.");
        }
        return homeTeam.getClub();
    }

    @Override
    public IClub getAwayClub() {
        if(awayTeam == null){
            throw  new IllegalStateException("Away Club is not initialized.");
        }
        return awayTeam.getClub();
    }

    @Override
    public boolean isPlayed() {
        return played;
    }

    @Override
    public ITeam getHomeTeam() {
        if(homeTeam == null){
            throw  new IllegalStateException("Home Team is not initialized.");
        }
        return homeTeam;
    }

    @Override
    public ITeam getAwayTeam() {
        if(awayTeam == null){
            throw  new IllegalStateException("Away Team is not initialized.");
        }
        return awayTeam;
    }

    @Override
    public void setPlayed() {
        played = true;
    }

    @Override
    public int getTotalByEvent(Class aClass, IClub iClub) {
        return 0;
    }

    @Override
    public boolean isValid() {
        try{
            if(homeTeam == null ||  awayTeam == null){
                return false;
            }
            if(homeTeam.getClub() == null || awayTeam.getClub() == null){
                return false;
            }
            if(homeTeam.getClub().equals(awayTeam.getClub())){
                return false;
            }
            if(homeTeam.getFormation() == null || awayTeam.getFormation() == null){
                return false;
            }
            return true;
        }catch(IllegalStateException  e){
            return false;
        }
    }

    @Override
    public ITeam getWinner() {
         int homeGoal = 0;
         int awayGoal = 0;

         if(homeGoal > awayGoal){
             return homeTeam;
         }else if (homeGoal < awayGoal){
             return awayTeam;
         }else{
             return null;
        }
    }

    @Override
    public int getRound() {
        return 0;
    }

    @Override
    public void setTeam(ITeam iTeam) {

    }

    @Override
    public void exportToJson() throws IOException {

    }

    @Override
    public void addEvent(IEvent iEvent) {

    }

    @Override
    public IEvent[] getEvents() {
        return new IEvent[0];
    }

    @Override
    public int getEventCount() {
        return 0;
    }
}
