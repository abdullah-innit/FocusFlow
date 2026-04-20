module com.focusflow {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.focusflow to javafx.fxml;
    exports com.focusflow;
}
