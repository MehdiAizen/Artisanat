package com.example.artisanconnect.controller;

import com.example.artisanconnect.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class OrderTrackingController {
    @FXML private Label orderIdLabel;
    @FXML private Label statusLabel;
    @FXML private ComboBox<OrderStatus> statusComboBox;
    @FXML private PasswordField artisanPasswordField;
    @FXML private VBox artisanControls; // Conteneur des éléments Artisan
    @FXML private Label instructionLabel; // Libellé d'instructions

    private final String ARTISAN_PASSWORD = "artisan123";
    private Order currentOrder;

    public void loadOrder(String orderId, String userRole) { // Ajout du paramètre userRole
        currentOrder = OrderTracker.getInstance().findOrder(orderId);
        boolean isArtisan = "ARTISAN".equals(userRole);

        // Masquer les éléments Artisan pour les clients
        artisanControls.setVisible(isArtisan);
        instructionLabel.setVisible(isArtisan);

        if (currentOrder != null) {
            orderIdLabel.setText("Commande #" + currentOrder.getOrderId());
            statusLabel.setText("Statut : " + currentOrder.getStatus());

            Platform.runLater(() -> {
                statusComboBox.getItems().setAll(OrderStatus.values());
                statusComboBox.setValue(currentOrder.getStatus());

                statusComboBox.setCellFactory(lv -> new ListCell<>() {
                    @Override
                    protected void updateItem(OrderStatus item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item == null ? "" : item.toString());
                    }
                });

                statusComboBox.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(OrderStatus item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item == null ? "" : item.toString());
                    }
                });
            });
        }
    }

    @FXML
    private void updateStatus() {
        if (artisanPasswordField.getText().equals(ARTISAN_PASSWORD)) {
            if (currentOrder != null) {
                currentOrder.setStatus(statusComboBox.getValue());
                statusLabel.setText("Statut : " + currentOrder.getStatus());
            }
        } else {
            showAlert("Erreur", "Mot de passe incorrect !");
        }
    }

    @FXML
    private void handleReturnToHome() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/artisanconnect/view/Login.fxml"));
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}