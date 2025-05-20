package com.hospitalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Doctors {
    private Connection connection;

    public Doctors(Connection connection) {
        this.connection = connection;
    }

    public void viewDoctor() {
        String query = "SELECT * FROM doctors";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            System.out.println("doctor");
            System.out.println("_____***______");
            System.out.println("|doctor id |Name  |dept");

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String department = result.getString("dept");

                System.out.printf("| %-10s | %-7s | %-14s|\n", id, name, department);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorId(int id) {
        String query = "SELECT * FROM doctors WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            return result.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
