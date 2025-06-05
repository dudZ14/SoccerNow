module pt.ul.fc.di.css.javafxexample {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.graphics;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires com.fasterxml.jackson.annotation;

    opens pt.ul.fc.di.css.javafxexample to javafx.fxml, javafx.web;
    opens pt.ul.fc.di.css.javafxexample.controller to javafx.fxml;
    opens pt.ul.fc.di.css.javafxexample.dto to com.fasterxml.jackson.databind;
    opens pt.ul.fc.di.css.javafxexample.model to javafx.base;


    exports pt.ul.fc.di.css.javafxexample;
    exports pt.ul.fc.di.css.javafxexample.dto;
}
