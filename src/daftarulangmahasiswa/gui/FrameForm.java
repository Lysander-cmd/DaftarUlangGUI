package daftarulangmahasiswa.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class FrameForm extends JFrame {
    private JTextField namaField, nomorpendaftaranField, jurusanField, emailField, teleponField, alamatField;
    private JButton submitButton, tampilkanButton;
    private Connection connection;

    public FrameForm() {
        setTitle("Form Daftar Ulang Mahasiswa Baru");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 1, 10));
        mainPanel.setBackground(new Color(230, 230, 250));
        
        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2),
                "Data Mahasiswa Baru", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), Color.BLUE));
        panel.setBackground(new Color(255, 255, 255)); 
        
        panel.add(new JLabel("Nama : "));
        namaField = new JTextField();
        panel.add(namaField);
        
        panel.add(new JLabel("Nomor Pendaftaran : "));
        nomorpendaftaranField = new JTextField();
        panel.add(nomorpendaftaranField);
        
        panel.add(new JLabel("Jurusan : "));
        jurusanField = new JTextField();
        panel.add(jurusanField);
        
        panel.add(new JLabel("Email : "));
        emailField = new JTextField();
        panel.add(emailField);
        
        panel.add(new JLabel("No. Telepon : "));
        teleponField = new JTextField();
        panel.add(teleponField);
        
        panel.add(new JLabel("Alamat : "));
        alamatField = new JTextField();
        panel.add(alamatField);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 230, 250)); 
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(100, 149, 237)); 
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(new SubmitButtonListener());
        buttonPanel.add(submitButton);
        
        tampilkanButton = new JButton("Tampilkan Data");
        tampilkanButton.setFont(new Font("Arial", Font.BOLD, 14));
        tampilkanButton.setBackground(new Color(100, 149, 237));
        tampilkanButton.setForeground(Color.WHITE);
        tampilkanButton.setFocusPainted(false);
        tampilkanButton.addActionListener(new TampilkanButtonListener());
        buttonPanel.add(tampilkanButton);
        
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/tes_database";
            String user = "root";
            String password = "Akuyafilo11";
            connection = DriverManager.getConnection(url, user, password);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class SubmitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (namaField.getText().isEmpty() || nomorpendaftaranField.getText().isEmpty() || jurusanField.getText().isEmpty() ||
                emailField.getText().isEmpty() || teleponField.getText().isEmpty() || alamatField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(FrameForm.this, "Semua kolom harus diisi !!","Peringatan", JOptionPane.WARNING_MESSAGE);   
            } else {
                int response = JOptionPane.showConfirmDialog(FrameForm.this,"Apakah anda yakin data yang anda isi sudah benar", "Konfirmasi",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE );
                if(response == JOptionPane.OK_OPTION){
                    saveDataToDatabase(namaField.getText(), nomorpendaftaranField.getText(), jurusanField.getText(),
                            emailField.getText(), teleponField.getText(), alamatField.getText());
                    clearFields();
                }
            }
        }
    }

    private class TampilkanButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new DataTableFrame().setVisible(true);
        }
    }

    private void saveDataToDatabase(String nama, String nomorPendaftaran, String jurusan, String email, String no_telepon, String alamat) {
        try {
            String query = "INSERT INTO daftarulang (nama, nomor_pendaftaran, jurusan, email, no_telepon, alamat) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, nama);
            pstmt.setString(2, nomorPendaftaran);
            pstmt.setString(3, jurusan);
            pstmt.setString(4, email);
            pstmt.setString(5, no_telepon);
            pstmt.setString(6, alamat);
            pstmt.executeUpdate();
              JOptionPane.showMessageDialog(FrameForm.this, "Data berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(FrameForm.this, "Terjadi kesalahan saat menyimpan data.", "Error", JOptionPane.ERROR_MESSAGE);
    }
        
    }

    private void clearFields() {
        namaField.setText("");
        nomorpendaftaranField.setText("");
        jurusanField.setText("");
        emailField.setText("");
        teleponField.setText("");
        alamatField.setText("");
    }

    class DataTableFrame extends JFrame {
        private JTable table;
        private DefaultTableModel tableModel;

        public DataTableFrame() {
            setTitle("Data Mahasiswa");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            
            tableModel = new DefaultTableModel(new Object[]{"Nama", "Nomor Pendaftaran", "Jurusan", "Email", "Telepon", "Alamat"}, 0);
            table = new JTable(tableModel);
            loadData();
            
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        }

        private void loadData() {
            try {
                String query = "SELECT * FROM daftarulang";
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    String nama = rs.getString("nama");
                    String nomorPendaftaran = rs.getString("nomor_pendaftaran");
                    String jurusan = rs.getString("jurusan");
                    String email = rs.getString("email");
                    String telepon = rs.getString("no_telepon");
                    String alamat = rs.getString("alamat");
                    tableModel.addRow(new Object[]{nama, nomorPendaftaran, jurusan, email, telepon, alamat});
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new FrameForm().setVisible(true);
    }
}
