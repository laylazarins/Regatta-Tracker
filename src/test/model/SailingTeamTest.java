package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SailingTeamTest {
    private SailingTeam testSailingTeam;

    @BeforeEach
    void runBefore() {
        testSailingTeam = new SailingTeam("Test Team");
    }

    @Test
    void testConstructor() {
        assertEquals("Test Team", testSailingTeam.getName());
        assertEquals(0, testSailingTeam.getScore());
    }

    @Test
    void testAddTime() {
        testSailingTeam.addTime(360);
        assertEquals(360, testSailingTeam.getTime(1));
        testSailingTeam.addTime(370);
        ArrayList<Integer> testTimes = new ArrayList<>();
        testTimes.add(360);
        testTimes.add(370);
        assertEquals(testTimes, testSailingTeam.getTimes());
    }

    @Test
    void testAddScore() {
        testSailingTeam.addScore(12);
        assertEquals(12, testSailingTeam.getScore());
    }

    @Test
    void testConvertTime() {
        testSailingTeam.addTime(3661);
        assertEquals("1h 1m 1s", testSailingTeam.convertTime(1));
        testSailingTeam.addTime(-1);
        assertEquals("DISQUALIFIED", testSailingTeam.convertTime(2));
    }


}
