/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistempeminjamanalatstudio;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AlatController {

   @FXML private TableView<Alat> tabelAlat;
    @FXML private TableColumn<Alat, String> kolomId;
    @FXML private TableColumn<Alat, String> kolomNama;
    @FXML private TableColumn<Alat, String> kolomKategori;
    @FXML private TableColumn<Alat, Integer> kolomStok;
    @FXML private TableColumn<Alat, Double> kolomHarga;
    @FXML private TextField txtIdAlat;
    @FXML private TextField txtNamaAlat;
    @FXML private TextField txtKategori;
    @FXML private TextField txtStok;
    @FXML private TextField txtHarga;
    @FXML private Button btnSimpan;
    @FXML private Button btnUpdate;
    @FXML private Button btnHapus;
    @FXML private Button btnBatal;
    @FXML private Button btnKembali;

    private ObservableList<Alat> dataAlat = FXCollections.observableArrayList();
    private Koneksi koneksi = new Koneksi();

    @FXML
    public void initialize() {
        kolomId.setCellValueFactory(new PropertyValueFactory<>("idAlat"));
        kolomNama.setCellValueFactory(new PropertyValueFactory<>("namaAlat"));
        kolomKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        kolomStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        kolomHarga.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        
        loadDataFromDatabase();
        setupTableClickListener();
    }
    
    public void loadDataFromDatabase() {
        dataAlat.clear();
        try {
            koneksi.bukaKoneksi();
            String query = "SELECT id_alat, nama_alat, kategori, stok, harga_sewa FROM alat"; 
            koneksi.statement = koneksi.dbKoneksi.createStatement();
            ResultSet rs = koneksi.statement.executeQuery(query);
            
            while (rs.next()) {
                dataAlat.add(new Alat(
                    rs.getString("id_alat"),
                    rs.getString("nama_alat"),
                    rs.getString("kategori"),
                    rs.getInt("stok"),
                    rs.getDouble("harga_sewa")
                ));
            }
            tabelAlat.setItems(dataAlat);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            koneksi.tutupKoneksi();
        }
    }

    @FXML
    private void handleSimpanButton() {
        try {
            // Ambil data dari textfield
            String id = txtIdAlat.getText();
            String nama = txtNamaAlat.getText();
            String kategori = txtKategori.getText();
            
            int stok = Integer.parseInt(txtStok.getText()); 
            double harga = Double.parseDouble(txtHarga.getText());
            
            String query = "INSERT INTO alat (id_alat, nama_alat, kategori, stok, harga_sewa) VALUES (?, ?, ?, ?, ?)";
            
            koneksi.bukaKoneksi();
            koneksi.preparedStatement = koneksi.dbKoneksi.prepareStatement(query);
            koneksi.preparedStatement.setString(1, id);
            koneksi.preparedStatement.setString(2, nama);
            koneksi.preparedStatement.setString(3, kategori);
            koneksi.preparedStatement.setInt(4, stok);
            koneksi.preparedStatement.setDouble(5, harga);
            
            koneksi.preparedStatement.executeUpdate();
            
            System.out.println("Data berhasil disimpan!");
            
            showAlert(AlertType.INFORMATION, "Sukses", "Data berhasil disimpan!");
            loadDataFromDatabase();
            handleBatalButton();
            
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Gagal menyimpan data.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Input Error", "Input Stok dan Harga harus berupa angka.");
        } finally {
            koneksi.tutupKoneksi();
        }
    }
    
    @FXML
    private void handleBatalButton() {
        txtIdAlat.clear();
        txtNamaAlat.clear();
        txtKategori.clear();
        txtStok.clear();
        txtHarga.clear();
        
        txtIdAlat.setEditable(true);
        btnSimpan.setDisable(false);
        
        tabelAlat.getSelectionModel().clearSelection();
    }
    
    private void setupTableClickListener() {
        tabelAlat.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Alat alatTerpilih = newSelection;
                txtIdAlat.setText(alatTerpilih.getIdAlat());
                txtNamaAlat.setText(alatTerpilih.getNamaAlat());
                txtKategori.setText(alatTerpilih.getKategori());
                txtStok.setText(String.valueOf(alatTerpilih.getStok()));
                txtHarga.setText(String.valueOf(alatTerpilih.getHargaSewa()));
                
                txtIdAlat.setEditable(false);
                btnSimpan.setDisable(true);
            }
        });
    }
    
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void handleKembaliButton(ActionEvent event) {
        try {
            Parent dashboardViewRoot = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Scene dashboardScene = new Scene(dashboardViewRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Dashboard Studio");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleUpdateButton() {
        String id = txtIdAlat.getText();
        if (id.isEmpty()) {
            showAlert(AlertType.WARNING, "Peringatan", "Pilih data di tabel yang ingin Anda update.");
            return;
        }
        
        try {
            String nama = txtNamaAlat.getText();
            String kategori = txtKategori.getText();
            int stok = Integer.parseInt(txtStok.getText());
            double harga = Double.parseDouble(txtHarga.getText());
            
            String query = "UPDATE alat SET nama_alat = ?, kategori = ?, stok = ?, harga_sewa = ? " +
                           "WHERE id_alat = ?";
            
            koneksi.bukaKoneksi();
            koneksi.preparedStatement = koneksi.dbKoneksi.prepareStatement(query);
            
            koneksi.preparedStatement.setString(1, nama);
            koneksi.preparedStatement.setString(2, kategori);
            koneksi.preparedStatement.setInt(3, stok);
            koneksi.preparedStatement.setDouble(4, harga);
            koneksi.preparedStatement.setString(5, id);
            
            koneksi.preparedStatement.executeUpdate();
            
            showAlert(AlertType.INFORMATION, "Sukses", "Data berhasil di-update.");
            
            loadDataFromDatabase();
            handleBatalButton();

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Gagal meng-update data.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Input Error", "Input Stok dan Harga harus berupa angka.");
        } finally {
            koneksi.tutupKoneksi();
        }
    }

@FXML
    private void handleHapusButton() {
        String id = txtIdAlat.getText();
        if (id.isEmpty()) {
            showAlert(AlertType.WARNING, "Peringatan", "Pilih data di tabel yang ingin Anda hapus.");
            return;
        }
        
        Alert konfirmasi = new Alert(AlertType.CONFIRMATION);
        konfirmasi.setTitle("Konfirmasi Hapus");
        konfirmasi.setHeaderText("Hapus Data Alat");
        konfirmasi.setContentText("Apakah Anda yakin ingin menghapus alat '" + txtNamaAlat.getText() + "'?");
        
        Optional<ButtonType> hasil = konfirmasi.showAndWait();
        
        if (hasil.isPresent() && hasil.get() == ButtonType.OK) {
            try {
                String query = "DELETE FROM alat WHERE id_alat = ?";
                
                koneksi.bukaKoneksi();
                koneksi.preparedStatement = koneksi.dbKoneksi.prepareStatement(query);
                koneksi.preparedStatement.setString(1, id);
                
                koneksi.preparedStatement.executeUpdate();
                
                showAlert(AlertType.INFORMATION, "Sukses", "Data berhasil dihapus.");
                
                loadDataFromDatabase();
                handleBatalButton();
                
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451) {
                    showAlert(AlertType.ERROR, "Gagal Hapus", "Data tidak bisa dihapus karena sedang digunakan di tabel transaksi.");
                } else {
                    showAlert(AlertType.ERROR, "Database Error", "Gagal menghapus data.");
                    e.printStackTrace();
                }
            } finally {
                koneksi.tutupKoneksi();
            }
        }
    }

}
