module com.petrina.client {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.kordamp.bootstrapfx.core;
  requires lombok;
    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires org.apache.logging.log4j;
    opens com.petrina.client to javafx.fxml;
  exports com.petrina.client;
  exports com.petrina.client.controllers;
  opens com.petrina.client.controllers to javafx.fxml;
}