package hospitalmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;

// Patient class to hold patient details
class Patient {
    String name;
    String age;
    String sex;
    String disease;
    String doctor;
    String dateAdmitted;
    String roomNumber;

    public Patient(String name, String age, String sex, String disease, String doctor, String dateAdmitted, String roomNumber) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.disease = disease;
        this.doctor = doctor;
        this.dateAdmitted = dateAdmitted;
        this.roomNumber = roomNumber;
    }

    @Override
    public String toString() {
        return name + ", " + age + " years old, " + sex + ", " + disease + ", Doctor: " + doctor + ", Admitted on: " + dateAdmitted + ", Room: " + roomNumber;
    }
}

public class HospitalManagementSystem extends JFrame {

    // Components
    private JTextField txtPatientID, txtName, txtAge, txtDisease;
    private JComboBox<String> doctorComboBox, roomComboBox, sexComboBox;
    private JButton btnAdd, btnRemove;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private HashMap<String, Patient> patientMap;
    private HashMap<String, Boolean> roomAvailability;

    private JSpinner dateSpinner; // Date picker for "Date Admitted"

    public HospitalManagementSystem() {
        patientMap = new HashMap<>();
        roomAvailability = new HashMap<>();

        // Adding some rooms and marking them as available
        for (int i = 1; i <= 10; i++) {
            roomAvailability.put("Room " + i, true);
        }

        // Setting up the JFrame
        setTitle("Hospital Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // Panel for adding patient details
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Patient ID
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Patient ID:"), gbc);
        gbc.gridx = 1;
        txtPatientID = new JTextField(15);
        inputPanel.add(txtPatientID, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(15);
        inputPanel.add(txtName, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        txtAge = new JTextField(15);
        inputPanel.add(txtAge, gbc);

        // Sex
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Sex:"), gbc);
        gbc.gridx = 1;
        sexComboBox = new JComboBox<>(new String[] {"Male", "Female"});
        inputPanel.add(sexComboBox, gbc);

        // Disease
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Disease:"), gbc);
        gbc.gridx = 1;
        txtDisease = new JTextField(15);
        inputPanel.add(txtDisease, gbc);

        // Date Admitted
        gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(new JLabel("Date Admitted:"), gbc);
        gbc.gridx = 1;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        inputPanel.add(dateSpinner, gbc);

        // Select Doctor
        gbc.gridx = 0; gbc.gridy = 6;
        inputPanel.add(new JLabel("Select Doctor:"), gbc);
        gbc.gridx = 1;
        doctorComboBox = new JComboBox<>(new String[] {"Dr. Gabriel - Cardiologist", "Dr. Jannosins - Neurologist", "Dr. Jerome - Pediatrician"});
        inputPanel.add(doctorComboBox, gbc);

        // Select Room
        gbc.gridx = 0; gbc.gridy = 7;
        inputPanel.add(new JLabel("Select Room:"), gbc);
        gbc.gridx = 1;
        roomComboBox = new JComboBox<>();
        updateAvailableRooms();
        inputPanel.add(roomComboBox, gbc);

        // Buttons to add, remove patients
        gbc.gridx = 0; gbc.gridy = 8;
        btnAdd = new JButton("Add Patient");
        btnRemove = new JButton("Remove Patient");

        inputPanel.add(btnAdd, gbc);
        gbc.gridx = 1;
        inputPanel.add(btnRemove, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Table for displaying patient data
        tableModel = new DefaultTableModel(new String[] {"Patient ID", "Name", "Age", "Sex", "Disease", "Doctor", "Date Admitted", "Room Number"}, 0);
        patientTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(patientTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Button actions
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        btnRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removePatient();
            }
        });
    }

    // Update available rooms combo box
    private void updateAvailableRooms() {
        roomComboBox.removeAllItems();
        for (String room : roomAvailability.keySet()) {
            if (roomAvailability.get(room)) {
                roomComboBox.addItem(room);
            }
        }
    }

    // Add a new patient to the HashMap and table
    private void addPatient() {
        String id = txtPatientID.getText();
        String name = txtName.getText();
        String age = txtAge.getText();
        String sex = (String) sexComboBox.getSelectedItem();
        String disease = txtDisease.getText();
        String selectedDoctor = (String) doctorComboBox.getSelectedItem();
        String dateAdmitted = new java.text.SimpleDateFormat("yyyy-MM-dd").format((java.util.Date) dateSpinner.getValue());
        String selectedRoom = (String) roomComboBox.getSelectedItem();

        Patient newPatient = new Patient(name, age, sex, disease, selectedDoctor, dateAdmitted, selectedRoom);
        patientMap.put(id, newPatient);

        roomAvailability.put(selectedRoom, false);
        updateAvailableRooms();

        tableModel.addRow(new Object[] {id, name, age, sex, disease, selectedDoctor, dateAdmitted, selectedRoom});

        // Clear input fields
        txtPatientID.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtDisease.setText("");
        dateSpinner.setValue(new java.util.Date());
    }

    // Remove a patient from the HashMap and the table
    private void removePatient() {
        String id = txtPatientID.getText();

        if (patientMap.containsKey(id)) {
            Patient patient = patientMap.get(id);
            String roomNumber = patient.roomNumber;

            patientMap.remove(id);
            roomAvailability.put(roomNumber, true);
            updateAvailableRooms();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(id)) {
                    tableModel.removeRow(i);
                    break;
                }
            }

            JOptionPane.showMessageDialog(this, "Patient removed successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Patient ID not found.");
        }

        txtPatientID.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HospitalManagementSystem().setVisible(true);
            }
        });
    }
}
