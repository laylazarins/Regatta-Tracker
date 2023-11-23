package ui;

import model.Regatta;
import model.SailingTeam;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class RegattaApp {
    private Scanner scanner = new Scanner(System.in);
    private Regatta regatta;
    private Integer raceNumber;
    private static final String JSON_STORE = "./data/regatta.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public RegattaApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        startRegatta();
    }

    // MODIFIES: regatta
    // EFFECTS: runs starting sequence for the program
    private void startRegatta() {
        boolean running = true;
        System.out.println("Welcome to the Regatta App! Please enter the name of your Regatta, "
                + "or L to load from file.");
        String response = scanner.next();
        if (response.equals("L")) {
            loadRegatta();
        } else {
            regatta = new Regatta(scanner.nextLine());
            raceNumber = 0;
            startingSequence();
        }
        while (running) {
            running = runRegatta();
        }
    }

    // MODIFIES: regatta
    // EFFECTS: Runs the main part of the program, assigning times,
    // handling ranking, and prompting whether to add another race, end, or save to file
    private boolean runRegatta() {
        raceNumber++;
        assignTimes();
        handleRanking();
        System.out.println("Do you want to add another race? Press F to output final results, S to save races"
                + " to file, or anything else to add another race.");
        String response = scanner.next();
        scanner.nextLine();
        if (response.equals("F")) {
            printFinalRanking();
            return false;
        } else if (response.equals("S")) {
            saveRegatta();
            return false;
        } else  {
            return true;
        }
    }

    // MODIFIES: regatta
    // EFFECTS: runs the initial adding of teams to the regatta
    private void startingSequence() {
        System.out.println("Please enter the team name of the first participating team");
        regatta.addParticipant(new SailingTeam(scanner.nextLine()));
        System.out.println("Added! Please enter the team name of the second participating team");
        regatta.addParticipant(new SailingTeam(scanner.nextLine()));
        boolean addingTeams = true;
        while (addingTeams) {
            System.out.println("Added! Please enter the name of the next team, or enter * to finish adding teams");
            String nextTeam = scanner.nextLine();
            if (nextTeam.equals("*")) {
                addingTeams = false;
            } else {
                regatta.addParticipant(new SailingTeam(nextTeam));
            }
        }
    }

    // MODIFIES: regatta
    // EFFECTS: handles user assignment of times to participating teams
    private void assignTimes() {
        for (int count = 1; count <= regatta.getParticipants().size(); count++) {
            int time;
            SailingTeam currentTeam = regatta.getParticipants().get(count - 1);
            System.out.println("Please enter the HOURS it took for team " + currentTeam.getName()
                    + " to complete the race, or enter 'DQ' to disqualify the team!");
            String dqInput = scanner.nextLine();
            if (dqInput.equals("DQ")) {
                regatta.disqualifyTeam(currentTeam);
            } else {
                time = Integer.parseInt(dqInput) * 3600;
                System.out.println("Please enter the MINUTES it took for team " + currentTeam.getName()
                        + " to complete the race!");
                time += scanner.nextInt() * 60;
                System.out.println("Please enter the SECONDS it took for team " + currentTeam.getName()
                        + " to complete the race!");
                time += scanner.nextInt();
                scanner.nextLine();
                currentTeam.addTime(time);
            }
        }
    }

    // MODIFIES: regatta
    // EFFECTS: assigns scores to participants and prints results
    private void handleRanking() {
        regatta.assignScores(raceNumber);
        System.out.println("RACE RESULTS FOR RACE #" + raceNumber);
        System.out.println(" ");
        System.out.println(" ");
        for (int count = 1; count <= regatta.getParticipants().size(); count++) {
            SailingTeam currentTeam = regatta.getParticipants().get(count - 1);
            System.out.println("#" + count + " | "
                    + currentTeam.convertTime(raceNumber) + " | " + currentTeam.getName());
        }
    }

    // MODIFIES: regatta
    // EFFECTS: prints ranked results of the regatta
    private void printFinalRanking() {
        regatta.orderByScores();
        System.out.println("FINAL RESULTS for " + regatta.getRegattaName());
        System.out.println(" ");
        System.out.println(" ");
        for (int count = 1; count <= regatta.getParticipants().size(); count++) {
            SailingTeam currentTeam = regatta.getParticipants().get(count - 1);
            System.out.println("#" + count + " | " + currentTeam.getScore() + " points | " + currentTeam.getName());
        }
    }

    // EFFECTS: writes regatta to file
    private void saveRegatta() {
        try {
            jsonWriter.open();
            jsonWriter.write(regatta);
            jsonWriter.close();
            System.out.println("Saved " + regatta.getRegattaName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: loads regatta from file
    private void loadRegatta() {
        try {
            regatta = jsonReader.read();
            raceNumber = regatta.getParticipants().get(0).getTimes().size();
            System.out.println("Loaded " + regatta.getRegattaName() + " from " + JSON_STORE);
            scanner.nextLine();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
