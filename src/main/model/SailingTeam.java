package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// represents a Sailing Team, with a name, list of race times, and overall score
public class SailingTeam implements Writable {
    private String name;
    private ArrayList<Integer> timesList;
    private int overallScore;

    // EFFECTS: creates a new sailing team, with name set to inputted name,
    // an empty list of times (in seconds) representing individual races, and overall
    // score set to 0.
    public SailingTeam(String name) {
        this.name = name;
        this.timesList = new ArrayList<>();
        this.overallScore = 0;
        EventLog.getInstance().logEvent(new Event("Created Team " + name));
    }

    public String getName() {
        return name;
    }

    // REQUIRES: time > 0
    // MODIFIES: this
    // EFFECTS: adds race time to team's list of times
    public void addTime(int time) {
        this.timesList.add(time);
        EventLog.getInstance().logEvent(new Event("Added a time to Team " + name));
    }

    // REQUIRES: raceNumber <= size of timesList
    // EFFECTS: returns time (in seconds) based on specified raceNumber
    public int getTime(int raceNumber) {
        return this.timesList.get(raceNumber - 1);
    }

    // REQUIRES: score > 0
    // MODIFIES: this
    // EFFECTS: adds score to overallScore
    public void addScore(int score) {
        this.overallScore += score;
        EventLog.getInstance().logEvent(new Event("Added a score of " + score + " to Team " + name));
    }

    public ArrayList<Integer> getTimes() {
        return this.timesList;
    }

    public int getScore() {
        return overallScore;
    }

    // REQUIRES: raceNumber < size of timesList
    // EFFECTS: returns a more readable String of the team's time for specified raceNumber
    // in hours, minutes, and seconds, or "DISQUALIFIED" if team was disqualified.
    public String convertTime(int raceNumber) {
        int time = getTime(raceNumber);
        if (time < 0) {
            return "DISQUALIFIED";
        }
        int hours = time / 3600;
        time -= 3600 * hours;
        int minutes = time / 60;
        time -= 60 * minutes;
        return hours + "h " + minutes + "m " + time + "s";
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("timesList", timesList);
        json.put("overallScore", overallScore);
        return json;
    }
}
