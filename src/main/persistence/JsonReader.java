package persistence;

import model.Regatta;
import model.SailingTeam;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// NOTE: all persistence methods are heavily referenced
// from JsonSerializationDemo provided in Phase 2

// Represents a reader that reads Regatta from JSON data stored in file
public class  JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads regatta from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Regatta read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRegatta(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses regatta from JSON object and returns it
    private Regatta parseRegatta(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Regatta r = new Regatta(name);
        addParticipants(r, jsonObject);
        return r;
    }

    // MODIFIES: r
    // EFFECTS: parses participants from JSON object and adds them to regatta
    private void addParticipants(Regatta r, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("participants");
        for (Object json : jsonArray) {
            JSONObject nextParticipant = (JSONObject) json;
            addParticipant(r, nextParticipant);
        }
    }

    // MODIFIES: r
    // EFFECTS: parses SailingTeam from JSON object and adds it to regatta
    private void addParticipant(Regatta r, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        SailingTeam team = new SailingTeam(name);
        team.addScore(jsonObject.getInt("overallScore"));
        JSONArray times = (jsonObject.getJSONArray("timesList"));
        for (int count = 1; count <= times.length(); count++) {
            team.addTime(times.getInt(count - 1));
        }
        r.addParticipant(team);
    }
}
