/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistempeminjamanalatstudio;

import java.io.IOException;
import javafx.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chris
 */

public class DashboardController {

    @FXML
    private Label lblTersedia;

    @FXML
    private Label lblDisewa; //

    private Koneksi koneksi = new Koneksi();

    @FXML
    public void initialize() {
        loadStatistik();
    }

    private void loadStatistik() {
        int totalStok = 0;
        int totalDisewa = 0;

        try {
            koneksi.bukaKoneksi();

            String queryStok = "SELECT SUM(stok) AS total_stok FROM alat";
            koneksi.statement = koneksi.dbKoneksi.createStatement();
            ResultSet rsStok = koneksi.statement.executeQuery(queryStok);

            if (rsStok.next()) {
                totalStok = rsStok.getInt("total_stok");
            }

            String queryDisewa = "SELECT COUNT(d.id_detail_transaksi) AS total_disewa " + 
                                 "FROM detail_transaksi d " +
                                 "JOIN transaksi t ON d.id_transaksi = t.id_transaksi " +
                                 "WHERE t.status = 'Disewa'"; // <-- DIUBAH (dari t.status_transaksi)
            

            koneksi.statement = koneksi.dbKoneksi.createStatement();
            ResultSet rsDisewa = koneksi.statement.executeQuery(queryDisewa);

            if (rsDisewa.next()) {
                totalDisewa = rsDisewa.getInt("total_disewa");
            }

        } catch (SQLException e) {
            System.err.println("Error saat load statistik dashboard: " + e.getMessage());
            e.printStackTrace();
        } finally {
            koneksi.tutupKoneksi();
        }

        lblTersedia.setText(String.valueOf(totalStok));
        lblDisewa.setText(String.valueOf(totalDisewa));
    }
    @FXML
    private void handleBukaManajemenAlat(ActionEvent event) {
        try {
            Parent alatViewRoot = FXMLLoader.load(getClass().getResource("AlatView.fxml"));
            
            Scene alatScene = new Scene(alatViewRoot);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            stage.setScene(alatScene);
            stage.setTitle("Manajemen Data Alat");
            stage.show();
            
        } catch (IOException e) {
            System.err.println("Gagal memuat halaman AlatView.fxml");
            e.printStackTrace();
        }
    }
}