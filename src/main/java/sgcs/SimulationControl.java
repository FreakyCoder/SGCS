package sgcs;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.text.DecimalFormat;

public class SimulationControl extends VBox {
    // JavaFX label for pane title
    private final Label parameterTitle;
    // JavaFX button to start the simulation
    private final Button startButton;
    // JavaFX label for robot count text field
    private final Label countLabel;
    // JavaFX text field for robot count
    private final TextField countField;
    // keep previous caret position for form validation
    private int countFieldPreviousCaretPosition;
    // JavaFX label for communication range text field
    private final Label commRangeLabel;
    // JavaFX text field for the communication range
    private final TextField commRangeField;
    // keep previous caret position for form validation
    private int commRangeFieldPreviousCaretPosition;
    // JavaFX label for the failure chance field
    private final Label failureChanceLabel;
    // JavaFX text field for the failure chance
    private final TextField failureChanceField;
    // keep previous caret position for form validation
    private int failureChanceFieldPreviousCaretPosition;
    // JavaFX label for the robot speed field
    private final Label speedLabel;
    // JavaFX text field for the robot speed
    private final TextField speedField;
    // keep previous caret position for form validation
    private int speedFieldPreviousCaretPosition;
    // JavaFX label for the rate of battery discharge field
    private final Label batteryDischargeRateLabel;
    // JavaFX text field for the rate of battery discharge
    private final TextField batteryDischargeRateField;
    // keep previous caret position for form validation
    private int batteryDischargeRateFieldPreviousCaretPosition;
    // JavaFX label for the considered future positions field
    private final Label consideredPositionsLabel;
    // JavaFX text field for the considered future positions
    private final TextField consideredPositionsField;
    // keep previous caret position for form validation
    private int consideredPositionsFieldPreviousCaretPosition;
    // JavaFX label for the pheromone decay rate field
    private final Label pheromoneDecayLabel;
    // JavaFX text field for the pheromone decay rate
    private final TextField pheromoneDecayField;
    // keep previous caret position for form validation
    private int pheromoneDecayFieldPreviousCaretPosition;
    // JavaFX label for the title of the simulation
    private final Label simulationTitle;
    // JavaFX label for the simulation parameters title
    private final Label simulationParametersLabel;
    // JavaFX label for the simulation parameters
    private final Label simulationParameters;
    // Show trail checkboxes
    private final ShowTrailCheckboxes showTrailCheckboxes = ShowTrailCheckboxes.getInstance();
    // JavaFX button to pause and play the simulation
    private final Button pausePlayButton;
    // JavaFX button to quite the application
    private final Button quitButton;
    // has the simulation begun
    private boolean isHome = true;
    // singleton instance
    public static final SimulationControl instance = new SimulationControl(Screen.getPrimary().getBounds().getWidth() / 2, 0, Screen.getPrimary().getBounds().getWidth() / 2, Screen.getPrimary().getBounds().getHeight());
    // prevent initialization
    private SimulationControl(double x, double y, double width, double height) {
        // set UI parameters
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setPrefSize(width, height);
        this.setStyle("-fx-border-color: black");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(height / 100);
        Font titleFont = Font.loadFont(getClass().getResourceAsStream("/fonts/font.ttf"), 48);
        Font labelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/font.ttf"), 36);
        Font simulationParametersFont = Font.loadFont(getClass().getResourceAsStream("/fonts/font.ttf"), 24);
        Font fieldFont = Font.loadFont(getClass().getResourceAsStream("/fonts/font.ttf"), 12);
        // get loop instance
        Loop loop = Loop.getInstance();
        // set the pane title and font
        parameterTitle = new Label("SGCS Simulation Parameters");
        parameterTitle.setFont(titleFont);
        // robot count parameter label and text field
        countLabel = new Label("Robot count(1-1000): ");
        countLabel.setFont(labelFont);
        countField = new TextField();
        countField.setFont(fieldFont);
        countField.setMaxWidth(600);
        countField.setText("1");
        // verify value on text change
        countField.textProperty().addListener((ObservableValue< ? extends String> observable, String oldValue, String newValue) -> {
            boolean valid = true;
            if (!newValue.isEmpty()) {
                try {
                    int number = Integer.parseInt(newValue);
                    if (number < 1 || number > 1000) {
                        valid = false;
                    }
                } catch (Exception e){
                    valid = false;
                }
                if (!valid) {
                    Platform.runLater(() -> {
                        countField.setText(oldValue);
                        countField.positionCaret(countFieldPreviousCaretPosition);
                    });
                }
            }
            if (valid) {
                countFieldPreviousCaretPosition = countField.getCaretPosition();
            }
        });
        // verify format on loss of focus
        countField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                boolean valid = true;
                try {
                    int number = Integer.parseInt(countField.getText());
                    if (number < 1 || number > 1000) {
                        valid = false;
                    }
                } catch (Exception e){
                    valid = false;
                }
                if (!valid) {
                    countField.setText("1");
                }
            } else {
                countFieldPreviousCaretPosition = countField.getCaretPosition();
            }
        });
        // communication range label and text field
        commRangeLabel = new Label("Communication range(1-10000): ");
        commRangeLabel.setFont(labelFont);
        commRangeField = new TextField();
        commRangeField.setFont(fieldFont);
        commRangeField.setMaxWidth(600);
        commRangeField.setText("1");
        // verify value on text change
        commRangeField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean valid = true;
            if (!newValue.isEmpty()) {
                try {
                    int number = Integer.parseInt(newValue);
                    if (number < 1 || number > 10000) {
                        valid = false;
                    }
                } catch (Exception e){
                    valid = false;
                }
                if (!valid) {
                    Platform.runLater(() -> {
                        commRangeField.setText(oldValue);
                        commRangeField.positionCaret(commRangeFieldPreviousCaretPosition);
                    });
                }
            }
            if (valid) {
                commRangeFieldPreviousCaretPosition = commRangeField.getCaretPosition();
            }
        });
        // verify value on loss of focus
        commRangeField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                boolean valid = true;
                try {
                    int number = Integer.parseInt(commRangeField.getText());
                    if (number < 1 || number > 10000) {
                        valid = false;
                    }
                } catch (Exception e){
                    valid = false;
                }
                if (!valid) {
                    commRangeField.setText("1");
                }
            } else {
                commRangeFieldPreviousCaretPosition = commRangeField.getCaretPosition();
            }
        });
        // failure chance label and text field
        failureChanceLabel = new Label("Failure chance(% per second): ");
        failureChanceLabel.setFont(labelFont);
        failureChanceField = new TextField();
        failureChanceField.setFont(fieldFont);
        failureChanceField.setMaxWidth(600);
        failureChanceField.setText("0");
        // verify value on text change
        failureChanceField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean valid = true;
            if (!newValue.isEmpty()) {
                try {
                    double number = Double.parseDouble(newValue);
                    if (number < 0 || number > 100) {
                        valid = false;
                    }
                } catch (Exception e) {
                    valid = false;
                }
                if (!valid) {
                    Platform.runLater(() -> {
                        failureChanceField.setText(oldValue);
                        failureChanceField.positionCaret(failureChanceFieldPreviousCaretPosition);
                    });
                }
            }
            if (valid) {
                failureChanceFieldPreviousCaretPosition = failureChanceField.getCaretPosition();
            }
        });
        // verify value on loss of focus
        failureChanceField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                boolean valid = true;
                try {
                    double number = Double.parseDouble(failureChanceField.getText());
                    if (number < 0 || number > 100) {
                        valid = false;
                    }
                } catch (Exception e) {
                    valid = false;
                }
                if (!valid) {
                    failureChanceField.setText("0");
                }
            } else {
                failureChanceFieldPreviousCaretPosition = failureChanceField.getCaretPosition();
            }
        });
        // robot speed label and text field
        speedLabel = new Label("Robot speed(0-10): ");
        speedLabel.setFont(labelFont);
        speedField = new TextField();
        speedField.setFont(fieldFont);
        speedField.setMaxWidth(600);
        speedField.setText("1");
        // verify value on text change
        speedField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean valid = true;
            if (!newValue.isEmpty()) {
                try {
                    double number = Double.parseDouble(newValue);
                    if (number < 0 || number > 10) {
                        valid = false;
                    }
                } catch (Exception e) {
                    valid = false;
                }
                if (!valid) {
                    Platform.runLater(() -> {
                        speedField.setText(oldValue);
                        speedField.positionCaret(speedFieldPreviousCaretPosition);
                    });
                }
            }
            if (valid) {
                speedFieldPreviousCaretPosition = speedField.getCaretPosition();
            }
        });
        // verify value on loss of focus
        speedField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                boolean valid = true;
                try {
                    double number = Double.parseDouble(speedField.getText());
                    if (number <= 0 || number > 10) {
                        valid = false;
                    }
                } catch (Exception e) {
                    valid = false;
                }
                if (!valid) {
                    speedField.setText("1");
                }
            } else {
                speedFieldPreviousCaretPosition = speedField.getCaretPosition();
            }
        });
        // battery discharge label and text field
        batteryDischargeRateLabel = new Label("Battery discharge rate(% per second): ");
        batteryDischargeRateLabel.setFont(labelFont);
        batteryDischargeRateField = new TextField();
        batteryDischargeRateField.setFont(fieldFont);
        batteryDischargeRateField.setMaxWidth(600);
        batteryDischargeRateField.setText("0");
        batteryDischargeRateField.textProperty().addListener((ObservableValue< ? extends String> observable, String oldValue, String newValue) -> {
            boolean valid = true;
            if (!newValue.isEmpty()) {
                try {
                    double number = Double.parseDouble(newValue);
                    if (number < 0 || number > 100) {
                        valid = false;
                    }
                } catch (Exception e) {
                    valid = false;
                }
                if (!valid) {
                    Platform.runLater(() -> {
                        batteryDischargeRateField.setText(oldValue);
                        batteryDischargeRateField.positionCaret(batteryDischargeRateFieldPreviousCaretPosition);
                    });
                }
            }
            if (valid) {
                batteryDischargeRateFieldPreviousCaretPosition = batteryDischargeRateField.getCaretPosition();
            }
        });
        batteryDischargeRateField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                boolean valid = true;
                try {
                    double number = Double.parseDouble(batteryDischargeRateField.getText());
                    if (number < 0 || number > 100) {
                        valid = false;
                    }
                } catch (Exception e) {
                    valid = false;
                }
                if (!valid) {
                    batteryDischargeRateField.setText("0");
                }
            } else {
                batteryDischargeRateFieldPreviousCaretPosition = batteryDischargeRateField.getCaretPosition();
            }
        });
        // considered future positions label and text field
        consideredPositionsLabel = new Label("Considered future positions(1-1000): ");
        consideredPositionsLabel.setFont(labelFont);
        consideredPositionsField = new TextField();
        consideredPositionsField.setFont(fieldFont);
        consideredPositionsField.setMaxWidth(600);
        consideredPositionsField.setText("5");
        // verify value on text change
        consideredPositionsField.textProperty().addListener((ObservableValue< ? extends String> observable, String oldValue, String newValue) -> {
            boolean valid = true;
            if (!newValue.isEmpty()) {
                try {
                    int number = Integer.parseInt(newValue);
                    if (number < 1 || number > 1000) {
                        valid = false;
                    }
                } catch (Exception e){
                    valid = false;
                }
                if (!valid) {
                    Platform.runLater(() -> {
                        consideredPositionsField.setText(oldValue);
                        consideredPositionsField.positionCaret(consideredPositionsFieldPreviousCaretPosition);
                    });
                }
            }
            if (valid) {
                consideredPositionsFieldPreviousCaretPosition = consideredPositionsField.getCaretPosition();
            }
        });
        // verify format on loss of focus
        consideredPositionsField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                boolean valid = true;
                try {
                    int number = Integer.parseInt(consideredPositionsField.getText());
                    if (number < 1 || number > 1000) {
                        valid = false;
                    }
                } catch (Exception e){
                    valid = false;
                }
                if (!valid) {
                    consideredPositionsField.setText("5");
                }
            } else {
                consideredPositionsFieldPreviousCaretPosition = consideredPositionsField.getCaretPosition();
            }
        });
        // pheromone decay rate label and text field
        pheromoneDecayLabel = new Label("Pheromone decay rate(% per second): ");
        pheromoneDecayLabel.setFont(labelFont);
        pheromoneDecayField = new TextField();
        pheromoneDecayField.setFont(fieldFont);
        pheromoneDecayField.setMaxWidth(600);
        pheromoneDecayField.setText("0");
        pheromoneDecayField.textProperty().addListener((ObservableValue< ? extends String> observable, String oldValue, String newValue) -> {
            boolean valid = true;
            if (!newValue.isEmpty()) {
                try {
                    double number = Double.parseDouble(newValue);
                    if (number < 0 || number > 100) {
                        valid = false;
                    }
                } catch (Exception e) {
                    valid = false;
                }
                if (!valid) {
                    Platform.runLater(() -> {
                        pheromoneDecayField.setText(oldValue);
                        pheromoneDecayField.positionCaret(pheromoneDecayFieldPreviousCaretPosition);
                    });
                }
            }
            if (valid) {
                pheromoneDecayFieldPreviousCaretPosition = pheromoneDecayField.getCaretPosition();
            }
        });
        pheromoneDecayField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                boolean valid = true;
                try {
                    double number = Double.parseDouble(pheromoneDecayField.getText());
                    if (number < 0 || number > 100) {
                        valid = false;
                    }
                } catch (Exception e) {
                    valid = false;
                }
                if (!valid) {
                    pheromoneDecayField.setText("0");
                }
            } else {
                pheromoneDecayFieldPreviousCaretPosition = pheromoneDecayField.getCaretPosition();
            }
        });
        // button to start the simulation
        startButton = new Button("Start Simulation");
        startButton.setFont(labelFont);
        startButton.setDefaultButton(true);
        // start the simulation on click
        startButton.setOnAction((ActionEvent e) -> startSimulation());
        // quit button
        quitButton = new Button("Quit");
        quitButton.setFont(labelFont);
        // quit on click
        quitButton.setOnAction(e -> Platform.exit());
        setScene();
        // running simulation UI
        simulationTitle = new Label("SGCS Simulation");
        simulationTitle.setFont(titleFont);
        simulationParametersLabel = new Label("Simulation parameters:");
        simulationParametersLabel.setFont(labelFont);
        simulationParameters = new Label("No simulation parameters entered.");
        simulationParameters.setFont(simulationParametersFont);
        // pause-play button
        pausePlayButton = new Button("Pause");
        pausePlayButton.setFont(labelFont);
        pausePlayButton.setDefaultButton(true);
        pausePlayButton.setOnAction((ActionEvent e) -> {
            if (loop.isPlaying()) {
                loop.pause();
                pausePlayButton.setText("Play");
            } else {
                loop.play();
                pausePlayButton.setText("Pause");
            }
        });
    }
    // get the singleton instance
    public static SimulationControl getInstance() {
        return instance;
    }
    private void startSimulation() {
        // get the animation loop and simulation instances
        Loop loop = Loop.getInstance();
        Simulation sim = Simulation.getInstance();
        this.isHome = false;
        // verify the set parameters and set them to default values if they are incorrect
        boolean valid = true;
        try {
            int number = Integer.parseInt(countField.getText());
            if (number < 1 || number > 1000) {
                valid = false;
            }
        } catch (Exception e) {
            valid = false;
        }
        if (!valid) {
            countField.setText("1");
        }
        valid = true;
        try {
            int number = Integer.parseInt(commRangeField.getText());
            if (number < 1 || number > 10000) {
                valid = false;
            }
        } catch (Exception e){
            valid = false;
        }
        if (!valid) {
            commRangeField.setText("1");
        }
        valid = true;
        try {
            double number = Double.parseDouble(failureChanceField.getText());
            if (number < 0 || number > 100) {
                valid = false;
            }
        } catch (Exception e) {
            valid = false;
        }
        if (!valid) {
            failureChanceField.setText("0");
        }
        valid = true;
        try {
            double number = Double.parseDouble(speedField.getText());
            if (number <= 0 || number > 10) {
                valid = false;
            }
        } catch (Exception e) {
            valid = false;
        }
        if (!valid) {
            speedField.setText("1");
        }
        valid = true;
        try {
            double number = Double.parseDouble(batteryDischargeRateField.getText());
            if (number < 0 || number > 100) {
                valid = false;
            }
        } catch (Exception e) {
            valid = false;
        }
        if (!valid) {
            batteryDischargeRateField.setText("0");
        }
        valid = true;
        try {
            int number = Integer.parseInt(consideredPositionsField.getText());
            if (number < 1 || number > 1000) {
                valid = false;
            }
        } catch (Exception e){
            valid = false;
        }
        if (!valid) {
            consideredPositionsField.setText("5");
        }
        valid = true;
        try {
            double number = Double.parseDouble(pheromoneDecayField.getText());
            if (number < 0 || number > 100) {
                valid = false;
            }
        } catch (Exception e) {
            valid = false;
        }
        if (!valid) {
            pheromoneDecayField.setText("0");
        }
        // set the simulation parameters
        sim.setParameters(Integer.parseInt(countField.getText()), Integer.parseInt(commRangeField.getText()), Double.parseDouble(failureChanceField.getText()), Double.parseDouble(speedField.getText()), Double.parseDouble(batteryDischargeRateField.getText()), Integer.parseInt(consideredPositionsField.getText()), Double.parseDouble(pheromoneDecayField.getText()));
        // start the animation loop
        loop.start();
        loop.play();
        setScene();
    }
    // switch between home and running simulation UI
    private void setScene() {
        // get the simulation instance
        Simulation sim = Simulation.getInstance();
        this.getChildren().clear();
        // if setting parameters
        if (this.isHome) {
            this.getChildren().addAll(parameterTitle, countLabel, countField, commRangeLabel, commRangeField, failureChanceLabel, failureChanceField, speedLabel, speedField, batteryDischargeRateLabel, batteryDischargeRateField, consideredPositionsLabel, consideredPositionsField, pheromoneDecayLabel, pheromoneDecayField, startButton, quitButton);
        } else {
            DecimalFormat doubleFormat = new DecimalFormat("0.#");
            simulationParameters.setText(String.format("Robot count: %d\nCommunication range: %d\nFailure chance: %s%%\nRobot speed: %s\nBattery discharge rate: %s%%\nConsidered future positions: %d\nPheromone decay rate: %s%%\nShow pheromones of: \n", sim.getCount(), sim.getCommRange(), doubleFormat.format(sim.getFailureChance()), doubleFormat.format(sim.getSpeed()), doubleFormat.format(sim.getBatteryDischargeRate()), sim.getConsideredPositions(), doubleFormat.format(sim.getPheromoneDecayRate())));
            showTrailCheckboxes.setCheckboxCount(sim.getCount());
            this.getChildren().addAll(simulationTitle, simulationParametersLabel, simulationParameters, showTrailCheckboxes, pausePlayButton, quitButton);
            // select the pause/play button by default
            Platform.runLater(pausePlayButton::requestFocus);
        }
    }
}
