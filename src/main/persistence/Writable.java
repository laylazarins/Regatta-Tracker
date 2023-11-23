package persistence;

import org.json.JSONObject;

// NOTE: all persistence methods are heavily referenced
// from JsonSerializationDemo provided in Phase 2
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
