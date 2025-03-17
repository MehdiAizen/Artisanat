package com.example.artisanconnect.controller;

import com.example.artisanconnect.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class OrderConfirmationController {
    @FXML private Label orderIdLabel;
    @FXML private Label statusLabel;

    public void loadOrder(String orderId) {
        Order order = OrderTracker.getInstance().findOrder(orderId);
        orderIdLabel.setText("Commande #" + order.getOrderId());
        statusLabel.setText("Statut : " + order.getStatus());
    }

    @FXML
    private void returnToHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/artisanconnect/view/Login.fxml"));
            Stage stage = (Stage) orderIdLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}