package ui;

import model.Event;
import model.EventLog;
import model.Regatta;
import model.SailingTeam;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents the Graphical User Interface for the Regatta App
public class RegattaGUI extends JFrame {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 900;
    private JButton newRegattaButton;
    private JButton loadButton;
    private JPanel startPanel;
    private JFrame frame;
    private Regatta regatta;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/regatta.json";

    public static void main(String[] args) {
        new RegattaGUI();
    }

    // EFFECTS: Initializes the GUI to its starting state.
    public RegattaGUI() {
        doSplashScreen();
        frame = new JFrame("Regatta App");
        newRegattaButton = new JButton("New Regatta");
        loadButton = new JButton("Load from File");
        startPanel = new JPanel();
        startPanel.add(newRegattaButton);
        startPanel.add(loadButton);
        frame.add(startPanel);
        addStarterFunctions();
        logPrinter();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // EFFECTS: Displays splash screen
    private void doSplashScreen() {
        BufferedImage img = null;
        try {
            File imageFile = new File("./data/splash.jpg");
            img = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.out.println("Failed to load splashscreen.");
            System.exit(1);
        }
        JLabel label = new JLabel(new ImageIcon(img));
        JWindow splashScreen = new JWindow();
        splashScreen.add(label);
        splashScreen.setSize(img.getWidth(), img.getHeight());
        splashScreen.setLocationRelativeTo(null);
        splashScreen.setVisible(true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Failed to load splashscreen at Thread.sleep");
            System.exit(1);
        }
        splashScreen.dispose();
    }

    // EFFECTS: adds functionality to the New Regatta and Load from File buttons
    private void addStarterFunctions() {
        newRegattaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewRegatta();
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRegatta();
            }
        });
    }

    // MODIFIES: regatta
    // EFFECTS: creates new regatta with inputted name
    private void createNewRegatta() {
        String name = JOptionPane.showInputDialog("Please enter the name of your Regatta:");
        if (name != null) {
            regatta = new Regatta(name);
            addParticipants();
        }
    }

    // MODIFIES: regatta
    // EFFECTS: Prompts user to add participating teams to regatta
    private void addParticipants() {
        String name = JOptionPane.showInputDialog("Please enter the name of the first participating team");
        regatta.addParticipant(new SailingTeam(name));
        name = JOptionPane.showInputDialog("Please enter the name of the second participating team");
        regatta.addParticipant(new SailingTeam(name));
        boolean adding = true;
        while (adding) {
            String nextTeam = JOptionPane.showInputDialog("Please enter the name of the next participating team, "
                    + "or simply 'CANCEL' to finish adding teams");
            if (nextTeam != null) {
                regatta.addParticipant(new SailingTeam(nextTeam));
            } else {
                frame.remove(startPanel);
                addStandardState();
                adding = false;
            }
        }
    }

    // MODIFIES: frame
    // EFFECTS: Sets main window to have 'Add Race', 'Current Standings', and 'Save' buttons
    private void addStandardState() {
        JPanel standardPanel = new JPanel();
        JButton addRaceButton = new JButton("Add Race");
        JButton viewStandingsButton = new JButton("View Current Standings");
        JButton saveButton = new JButton("Save");
        standardPanel.add(addRaceButton);
        standardPanel.add(viewStandingsButton);
        standardPanel.add(saveButton);
        frame.add(standardPanel);
        frame.pack();
        setStandardButtonFunctions(addRaceButton, viewStandingsButton, saveButton);
    }

    // MODIFIES: addRaceButton, viewStandingsButton, saveButton
    // EFFECTS: implements functionality to inputted buttons
    private void setStandardButtonFunctions(JButton addRaceButton, JButton viewStandingsButton, JButton saveButton) {
        addRaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRace();
                displayRaceResults();
            }
        });
        viewStandingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStandings();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRegatta();
            }
        });
    }

    // EFFECTS: writes regatta to file, and ends program
    private void saveRegatta() {
        try {
            jsonWriter = new JsonWriter(JSON_STORE);
            jsonWriter.open();
            jsonWriter.write(regatta);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Regatta saved to File!", "Success", JOptionPane.INFORMATION_MESSAGE);
            for (Event event : EventLog.getInstance()) {
                System.out.println(event.getDescription());
            }
            System.exit(0);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "UNABLE TO WRITE TO FILE", "Error", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    // EFFECTS: loads regatta from file, and sets main window to standard state
    private void loadRegatta() {
        try {
            jsonReader = new JsonReader(JSON_STORE);
            regatta = jsonReader.read();
            JOptionPane.showMessageDialog(null, "Regatta Loaded!", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.remove(startPanel);
            addStandardState();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "UNABLE TO READ FROM FILE", "Error", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    // MODIFIES: Regatta
    // EFFECTS: Organizes regatta participants by standing, and displays standings in a pop-up window
    private void viewStandings() {
        String[] columnNames = {"Position", "Team", "Score"};
        Object[][] results = new Object[regatta.getParticipants().size()][3];
        regatta.orderByScores();
        for (int i = 0; i < regatta.getParticipants().size(); i++) {
            results[i][0] = i + 1;
            results[i][1] = regatta.getParticipants().get(i).getName();
            results[i][2] = regatta.getParticipants().get(i).getScore();
        }
        JTable table = new JTable(results, columnNames);
        table.setDefaultEditor(Object.class, null); // taken from StackOverflow https://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
        table.setPreferredScrollableViewportSize(new Dimension(500, 100));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        JOptionPane.showMessageDialog(null, scrollPane, "Race Results", JOptionPane.PLAIN_MESSAGE);
    }

    // MODIFIES: regatta
    // EFFECTS: adds race to regatta, prompting user to input times for each competing team
    private void addRace() {
        for (int i = 1; i <= regatta.getParticipants().size(); i++) {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField textField1 = new JTextField(10);
            JTextField textField2 = new JTextField(10);
            JTextField textField3 = new JTextField(10);
            String title = "Please enter the time for team " + regatta.getParticipants().get(i - 1).getName();
            panel.add(new JLabel(title));
            panel.add(new JLabel("Hours"));
            panel.add(textField1);
            panel.add(new JLabel("Minutes"));
            panel.add(textField2);
            panel.add(new JLabel("Seconds"));
            panel.add(textField3);
            int result = JOptionPane.showConfirmDialog(null, panel, regatta.getRegattaName(),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                int time = Integer.parseInt(textField1.getText()) * 3600
                        + Integer.parseInt(textField2.getText()) * 60
                        + Integer.parseInt(textField3.getText());
                regatta.getParticipants().get(i - 1).addTime(time);
            }
        }
    }

    // MODIFIES: regatta
    // EFFECTS: Orders regatta participants by latest race results, and displays results to user
    private void displayRaceResults() {
        String[] columnNames = {"Position", "Team", "Time"};
        Object[][] results = new Object[regatta.getParticipants().size()][3];
        regatta.assignScores(regatta.getParticipants().get(0).getTimes().size());
        for (int i = 0; i < regatta.getParticipants().size(); i++) {
            results[i][0] = i + 1;
            results[i][1] = regatta.getParticipants().get(i).getName();
            results[i][2] =
                    regatta.getParticipants().get(i).convertTime(regatta.getParticipants().get(i).getTimes().size());
        }
        JTable table = new JTable(results, columnNames);
        table.setDefaultEditor(Object.class, null); // taken from StackOverflow https://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
        table.setPreferredScrollableViewportSize(new Dimension(500, 100));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        JOptionPane.showMessageDialog(null, scrollPane, "Race Results", JOptionPane.PLAIN_MESSAGE);
    }

    // EFFECTS: prints EventLog to the console on window close
    private void logPrinter() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event.getDescription());
                }
                System.exit(0);
            }
        });
    }
}
