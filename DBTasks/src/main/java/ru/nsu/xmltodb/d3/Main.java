package ru.nsu.xmltodb.d3;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {

        File xml = new File("persons.xml");

        try (Connection connection =
                     DriverManager.getConnection(
                             "jdbc:postgresql://localhost:5432/postgres", "postgres", "1522227")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS spouse ");
            statement.executeUpdate("DROP TABLE IF EXISTS sibling ");
            statement.executeUpdate("DROP TABLE IF EXISTS parent ");
            statement.executeUpdate("DROP TABLE IF EXISTS person ");

            statement.executeUpdate
                    ("CREATE TABLE IF NOT EXISTS person("
                            + "id varchar(8) NOT NULL PRIMARY KEY,"
                            + "gender varchar(2) NOT NULL,"
                            + "firstname varchar(20) NOT NULL,"
                            + "lastname varchar(20) NOT NULL,"
                            + "CONSTRAINT gender_check CHECK (gender IN ('M', 'F'))"
                            + ")");
            statement.executeUpdate
                    ("CREATE TABLE IF NOT EXISTS spouse ("
                            + "wife_id varchar(8) NOT NULL,"
                            + "husband_id varchar(8) NOT NULL)");
            statement.executeUpdate
                    ("CREATE TABLE IF NOT EXISTS sibling ("
                            + "first_sibling_id varchar(8) NOT NULL,"
                            + "second_sibling_id varchar(8) NOT NULL)");
            statement.executeUpdate
                    ("CREATE TABLE IF NOT EXISTS parent ("
                            + "child_id varchar(8) NOT NULL,"
                            + "parent_id varchar(8) NOT NULL)");

            new XMLtoDB(
                    connection.prepareStatement("INSERT INTO person VALUES (?, ?, ?, ?)"),
                    connection.prepareStatement("INSERT INTO sibling VALUES (?, ?)"),
                    connection.prepareStatement("INSERT INTO spouse VALUES (?, ?)"),
                    connection.prepareStatement("INSERT INTO parent VALUES (?, ?)"),
                    xml).writePeople();

            statement.executeUpdate("ALTER TABLE sibling "
                    + " ADD FOREIGN KEY (first_sibling_id) REFERENCES person(id), "
                    + " ADD FOREIGN KEY (second_sibling_id) REFERENCES person(id)");
            statement.executeUpdate("ALTER TABLE spouse "
                    + " ADD FOREIGN KEY (wife_id) REFERENCES person(id), "
                    + " ADD FOREIGN KEY (husband_id) REFERENCES person(id)");
            statement.executeUpdate("ALTER TABLE parent "
                    + " ADD FOREIGN KEY (child_id) REFERENCES person(id), "
                    + " ADD FOREIGN KEY (parent_id) REFERENCES person(id)");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
