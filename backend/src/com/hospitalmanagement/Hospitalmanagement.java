package com.hospitalmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Hospitalmanagement {

    private static final String URL = "jdbc:mysql://localhost:3306/hospitalmanagement";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root120924";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("connect JDBC");

            Patient patient = new Patient(connection, scanner);
            Doctors doctor = new Doctors(connection);

            while (true) {
                System.out.println("Welcome To ABC Hospital management");
                System.out.println("1, Add Patient");
                System.out.println("2, View Patient");
                System.out.println("3, View Doctors");
                System.out.println("4, Book Appointment");
                System.out.println("5, Exit");

                System.out.println("Enter Your Choice");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline after nextInt()

                switch (choice) {
                    case 1:
                        patient.addPatient();
                        break;
                    case 2:
                        patient.viewpatient();
                        break;
                    case 3:
                        doctor.viewDoctor();
                        break;
                    case 4:
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid request");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctors doctor, Connection connection, Scanner scanner) {
        System.out.println("Enter The Patient Id: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();  // consume newline

        System.out.println("Enter The Patient Name: ");
        String patientName = scanner.nextLine();  // read full name with spaces

        System.out.println("Enter The Doctor Id: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();  // consume newline

        System.out.println("Enter The Appointment Date(YYYY-MM-DD): ");
        String appointmentDate = scanner.nextLine();

        if (patient.getPatientId(patientId) && doctor.getDoctorId(doctorId)) {
            if (isDoctorAvailable(doctorId, appointmentDate, connection)) {
                String query = "INSERT INTO appointments(patient_id, Patient_name, doctor_id, appointment_date) VALUES (?, ?, ?, ?)";

                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setInt(1, patientId);
                    ps.setString(2, patientName);
                    ps.setInt(3, doctorId);
                    ps.setString(4, appointmentDate);

                    int rowsAffected = ps.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Appointment Booked");
                    } else {
                        System.out.println("Failed to book appointment");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor not available on the selected date.");
            }
        } else {
            System.out.println("Invalid Patient ID or Doctor ID.");
        }
    }

    public static boolean isDoctorAvailable(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, doctorId);
            ps.setString(2, appointmentDate);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
