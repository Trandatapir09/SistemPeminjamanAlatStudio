/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistempeminjamanalatstudio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Chris
 */
public class Koneksi {
    public Connection dbKoneksi; 
    public Statement statement; 
    public PreparedStatement preparedStatement; 
    
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/studio_db";
    private static final String USER = "root";
    private static final String PASS = "";

    public Koneksi() {
        this.dbKoneksi = null;
        this.statement = null;
        this.preparedStatement = null;
    }

    public void bukaKoneksi() {
        try {
            Class.forName(DRIVER);
            dbKoneksi = DriverManager.getConnection(URL, USER, PASS);
            
            System.out.println("Koneksi ke database berhasil.");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC MySQL tidak ditemukan. Pastikan JAR sudah ditambahkan.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Gagal koneksi ke database. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void tutupKoneksi() {
        try { 
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (dbKoneksi != null) {
                dbKoneksi.close();
                System.out.println("Koneksi ditutup.");
            }
        } catch (SQLException e) {
            System.err.println("Error saat menutup koneksi: " + e.getMessage());
            throw new RuntimeException("Error saat menutup koneksi: " + e.getMessage(), e);
        }
    }
}