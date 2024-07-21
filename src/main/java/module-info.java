module org.example.exam {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.exam to javafx.fxml;
    exports org.example.exam;
//    exports Vue;
    //opens Vue to javafx.fxml;
    //exports;
    //opens to
}