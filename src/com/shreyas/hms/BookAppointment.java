package com.shreyas.hms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class BookAppointment {
    private Connection connection;
    private Patient patient;
    private Doctor doctor;

    public BookAppointment(Connection connection,Patient patient, Doctor doctor){
        this.connection =  connection;
        this.patient = patient;
        this.doctor = doctor;
    }
    Scanner scanner = new Scanner(System.in);
    public void bookAppointment() throws SQLException{
        System.out.println("Enter patient id:");
        int patientId = scanner.nextInt();

        System.out.println("Enter doctor id: ");
        int doctorId = scanner.nextInt();

        System.out.print("Enter appointment date in(yyyy-mm--dd): ");
        String appointmentDate = scanner.next();

        if(!patient.getPatientById(patientId)){
            System.out.println("Please provide valid patient id");
            return;
        }
        if(!doctor.getDoctorById(doctorId)){
            System.out.println("Please provide valid doctor id");
            return;
        }
        if(!checkAvailability(connection, doctorId, appointmentDate)){
            System.out.println("Doctor not available");
            return;
        }
        String query = "insert into appointments(patient_id, doctor_id, appointment_date) values(?, ?, ?)";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, appointmentDate);

            if(ps.executeUpdate()>0){
                System.out.println("Appointment booked successfully");
            }
            else{
                System.out.println("Appointment cannot be booked");
            }
        }
    }
        public boolean checkAvailability(Connection connection, int doctorId, String appointmentDate) throws SQLException{
            String query = "select count(1) from appointments WHERE doctor_id = ? AND appointment_date = ?";
            try(PreparedStatement ps = connection.prepareStatement(query)){
                ps.setInt(1, doctorId);
                ps.setString(2, appointmentDate);

                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return rs.getInt(1) == 0;
                    }
                }
            }
            return false;
        }
    
}
