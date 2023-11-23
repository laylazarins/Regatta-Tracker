package model;

import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/RAHHHHHHHHHHHHHHHHH.json");
        try {
            Regatta r = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }


    @Test
    void testReaderRegatta() {
        JsonReader reader = new JsonReader("./data/testReaderRegatta.json");
        try {
            Regatta r = reader.read();
            assertEquals("Extra Cool Regatta", r.getRegattaName());
            List<SailingTeam> teams = r.getParticipants();
            assertEquals(2, teams.size());
            assertEquals("UBC", teams.get(0).getName());
            assertEquals("SFU", teams.get(1).getName());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
