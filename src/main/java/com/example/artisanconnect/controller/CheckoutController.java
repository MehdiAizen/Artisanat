package com.example.artisanconnect.controller;

import com.example.artisanconnect.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class CheckoutController {
    @FXML private TextField fullNameField;
    @FXML private TextField addressField;
    @FXML private TextField cardNumberField;
    @FXML private PasswordField passwordField;
    @FXML private Button backButton;

    private ShoppingCart cart;
    private final String CLIENT_PASSWORD = "client123";

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    @FXML
    private void handlePayment() {
        if (validateForm()) {
            if (!passwordField.getText().trim().equals(CLIENT_PASSWORD)) {
                showAlert("Accès refusé", "Mot de passe client incorrect");
                return;
            }

            // Création de la commande
            Order newOrder = new Order(
                    "CMD-" + System.currentTimeMillis(),
                    cart.getItems(),
                    cart.getTotalPrice(),
                    OrderStatus.EN_PREPARATION
            );

            OrderTracker.getInstance().addOrder(newOrder);

            // Redirection vers OrderTracking
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artisanconnect/view/OrderTracking.fxml"));
                Parent root = loader.load();
                OrderTrackingController controller = loader.getController();
                controller.loadOrder(newOrder.getOrderId(), "CLIENT"); // Rôle CLIENT

                Stage stage = (Stage) fullNameField.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
            } catch (IOException e) {
                showAlert("Erreur", "Impossible de charger le suivi.");
            }

            cart.clearCart();
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/artisanconnect/view/Cart.fxml"));
            Parent root = loader.load();
            CartController controller = loader.getController();
            controller.initialize(null, null);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("ArtisanConnect - Panier");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de retourner au panier.");
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        String fullName = fullNameField.getText().trim();
        String address = addressField.getText().trim();
        String cardNumber = cardNumberField.getText().trim().replaceAll("\\s+", "");

        resetFieldStyle(fullNameField, addressField, cardNumberField);

        if (fullName.isEmpty()) {
            highlightError(fullNameField);
            isValid = false;
        }
        if (address.isEmpty()) {
            highlightError(addressField);
            isValid = false;
        }
        if (!cardNumber.matches("\\d{16}")) {
            highlightError(cardNumberField);
            isValid = false;
        }

        if (!isValid) {
            showAlert("Erreur de formulaire", "Veuillez corriger les champs invalides");
        }

        return isValid;
    }

    private void highlightError(TextField field) {
        field.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
    }

    private void resetFieldStyle(TextField... fields) {
        for (TextField field : fields) {
            field.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px;");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}