package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RegattaTest {
    private Regatta testRegatta;
    private SailingTeam team1 = new SailingTeam("Team 1");
    private SailingTeam team2 = new SailingTeam("Team 2");
    private SailingTeam team3 = new SailingTeam("Team 3");
    private ArrayList<SailingTeam> testList = new ArrayList<>();

    @BeforeEach
    void runBefore() {
        testRegatta = new Regatta("Test Regatta");
    }

    @Test
    void testConstructor() {
        assertEquals("Test Regatta", testRegatta.getRegattaName());
        assertEquals(testList, testRegatta.getParticipants());
    }

    @Test
    void testAddParticipant() {
        testRegatta.addParticipant(team1);
        testRegatta.addParticipant(team2);
        testRegatta.addParticipant(team3);
        testList.add(team1);
        testList.add(team2);
        testList.add(team3);
        assertEquals(testList, testRegatta.getParticipants());
    }

    @Test
    void testAssignScores() {
        testRegatta.addParticipant(team1);
        testRegatta.addParticipant(team2);
        testRegatta.addParticipant(team3);
        team1.addTime(360);
        team2.addTime(440);
        team3.addTime(400);
        testRegatta.assignScores(1);
        assertEquals(1, team1.getScore());
        assertEquals(2, team3.getScore());
        assertEquals(3, team2.getScore());
        team1.addTime(-1);
        team2.addTime(-1);
        team3.addTime(60);
        testRegatta.assignScores(2);
        assertEquals(4, team1.getScore());
        assertEquals(3, team3.getScore());
        assertEquals(6, team2.getScore());
    }

    @Test
    void testOrderByScores() {
        testRegatta.addParticipant(team1);
        testRegatta.addParticipant(team2);
        testRegatta.addParticipant(team3);
        team1.addTime(360);
        team2.addTime(440);
        team3.addTime(400);
        testRegatta.assignScores(1);
        testRegatta.orderByScores();
        testList.add(team1);
        testList.add(team3);
        testList.add(team2);
        assertEquals(testList, testRegatta.getParticipants());
    }

    @Test
    void testDisqualifyTeam() {
        testRegatta.addParticipant(team1);
        testRegatta.addParticipant(team2);
        testRegatta.addParticipant(team3);
        team1.addTime(360);
        testRegatta.disqualifyTeam(team2);
        testRegatta.disqualifyTeam(team3);
        assertEquals(-1, team2.getTime(1));
        assertEquals(-1, team3.getTime(1));
    }

}