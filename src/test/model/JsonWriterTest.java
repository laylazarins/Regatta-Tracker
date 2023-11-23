package model;

import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Regatta r = new Regatta("COOL REGATTA!");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }



    @Test
    void testWriterRegatta() {
        try {
            Regatta r = new Regatta("YIPPEE");
            SailingTeam team1 = new SailingTeam("UBC");
            SailingTeam team2 = new SailingTeam("SFU");
            r.addParticipant(team1);
            r.addParticipant(team2);
            JsonWriter writer = new JsonWriter("./data/testWriterRegatta.json");
            writer.open();
            writer.write(r);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterRegatta.json");
            r = reader.read();
            assertEquals("YIPPEE", r.getRegattaName());
            List<SailingTeam> teams = r.getParticipants();
            assertEquals(2, teams.size());
            assertEquals("UBC", teams.get(0).getName());
            assertEquals("SFU", teams.get(1).getName());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
