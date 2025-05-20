package com.hospitalmanagement;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Patient {

    private Connection connection;
    private Scanner scn;

    public Patient(Connection connection, Scanner scn) {
        this.connection = connection;
        this.scn = scn;
    }

    public void addPatient() {
        System.out.println("Enter the Patient Details :");
        String name = scn.next();
        System.out.println("Enter the Patient Age :");
        int age = scn.nextInt();
        System.out.println("Enter patient Gender :");
        String gender = scn.next();

        try {
            String query = "INSERT INTO patients(name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Patient Added");
            } else {
                System.out.println("Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewpatient() {
        String query = "SELECT * FROM patients";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("Patients");
            System.out.println("_____*****_____");
            System.out.println("|patient id | Name  |Age  |Gender  | ");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                System.out.printf("|%-12s|%-8s|%-8s|%-10s|\n", id, name, age, gender);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientId(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
