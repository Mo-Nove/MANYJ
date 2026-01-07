module com.example.manyj {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires javafx.media;

    opens com.example.manyj to javafx.fxml;
    exports com.example.manyj;
}