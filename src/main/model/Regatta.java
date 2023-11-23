package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Represents a Regatta with a name and list of participating SailingTeams
public class Regatta implements Writable {

    private String regattaName;
    private ArrayList<SailingTeam> participants;

    // EFFECTS: regattaName is set to inputted regattaName,
    // participants is set to an empty list
    public Regatta(String regattaName) {
        this.regattaName = regattaName;
        this.participants = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Created a new Regatta with name " + regattaName));
    }

    public String getRegattaName() {
        return regattaName;
    }

    // MODIFIES: this
    // EFFECTS: adds team to list of participants
    public void addParticipant(SailingTeam team) {
        participants.add(team);
        EventLog.getInstance().logEvent(new Event("Added team " + team.getName() + " to the regatta"));
    }

    public ArrayList<SailingTeam> getParticipants() {
        return this.participants;
    }

    // REQUIRES: raceNumber > 0
    // MODIFIES: this
    // EFFECTS: assigns appropriate scores to each participant
    // based on how they did relative to their peers in specified race,
    // ordering participants from quickest to slowest in the process with
    // disqualifications at the end
    public void assignScores(int raceNumber) {
        Collections.sort(this.participants, (team1, team2) -> {
            if (team1.getTime(raceNumber) < 0) {
                return 1;
            } else if (team2.getTime(raceNumber) < 0) {
                return -1;
            } else {
                return Integer.compare(team1.getTime(raceNumber), team2.getTime(raceNumber));
            }
        });
        int positives = 1;
        for (int count = 1; count <= this.participants.size(); count++) {
            SailingTeam currentTeam = this.participants.get(count - 1);
            if (currentTeam.getTime(raceNumber) < 0) {
                currentTeam.addScore(this.participants.size());
            } else {
                currentTeam.addScore(positives);
                positives++;
            }
        }
        EventLog.getInstance().logEvent(new Event("Ordered teams based on Race #" + raceNumber));
    }

    // MODIFIES: this
    // EFFECTS: orders participants from lowest to highest score
    public void orderByScores() {
        Collections.sort(participants, new Comparator<SailingTeam>() {
            @Override
            public int compare(SailingTeam team1, SailingTeam team2) {
                return Integer.compare(team1.getScore(), team2.getScore());
            }
        });
        EventLog.getInstance().logEvent(new Event("Ordered teams based on cumulative score"));
    }
    // MODIFIES: this
    // EFFECTS: disqualifies team for specified race

    public void disqualifyTeam(SailingTeam team) {
        team.addTime(-1);
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", regattaName);
        json.put("participants", participantsToJson());
        return json;
    }

    // EFFECTS: returns regatta participants as a JSON array
    private JSONArray participantsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (SailingTeam t : participants) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }
}
