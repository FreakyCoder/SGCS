module com.everfreaky.sgcs {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.everfreaky.sgcs to javafx.fxml;
    exports com.everfreaky.sgcs;
}