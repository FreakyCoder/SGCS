package com.everfreaky.sgcs;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class ShowTrailCheckboxes extends VBox {
    // singleton instance
    private static final ShowTrailCheckboxes instance = new ShowTrailCheckboxes(Screen.getPrimary().getBounds().getHeight());
    // prevent initialization
    private ShowTrailCheckboxes(double height) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(height / 200);
    }
    // access to the singleton instance
    public static ShowTrailCheckboxes getInstance() { return instance; }
    public void setCheckboxCount(int robotCount) {
        Simulation sim = Simulation.getInstance();
        this.getChildren().removeAll();
        for (int i = 0; i < robotCount; ++ i) {
            CheckBox checkBox = new CheckBox(String.format("Robot %d", i + 1));
            // ensure no concurrency problems with lambda
            final int index = i;
            checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> sim.setTrailVisibility(index, newValue));
            this.getChildren().add(checkBox);
        }
    }
}
