package com.shoppingcart;

import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.*;

public class MainController {

    @FXML private ComboBox<String> languageComboBox;
    @FXML private Button confirmLanguageBtn;
    @FXML private Label enterNumItemsLabel;
    @FXML private TextField numItemsField;
    @FXML private Button enterItemsBtn;
    @FXML private VBox itemsContainer;
    @FXML private Button calculateBtn;
    @FXML private Label totalLabel;
    @FXML private Label selectLanguageLabel;

    private ResourceBundle bundle;
    private final ShoppingCartCalculator calculator = new ShoppingCartCalculator();

    private final Map<String, Locale> localeMap = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        localeMap.put("English",  new Locale("en", "US"));
        localeMap.put("Finnish",  new Locale("fi", "FI"));
        localeMap.put("Swedish",  new Locale("sv", "SE"));
        localeMap.put("Japanese", new Locale("ja", "JP"));
        localeMap.put("Arabic",   new Locale("ar", "AR"));

        languageComboBox.getItems().addAll(localeMap.keySet());
        languageComboBox.setValue("English");
        loadBundle(new Locale("en", "US"));
        applyBundle();
    }

    @FXML
    private void onConfirmLanguage() {
        String selected = languageComboBox.getValue();
        Locale locale = localeMap.getOrDefault(selected, new Locale("en", "US"));
        loadBundle(locale);
        applyBundle();

        if (selected.equals("Arabic")) {
            languageComboBox.getScene().getRoot().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            languageComboBox.getScene().getRoot().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    @FXML
    private void onEnterItems() {
        itemsContainer.getChildren().clear();
        try {
            int numItems = Integer.parseInt(numItemsField.getText().trim());
            if (numItems <= 0) throw new NumberFormatException();

            for (int i = 1; i <= numItems; i++) {
                Label priceLabel = new Label(bundle.getString("enter.price") + " " + i + ":");
                TextField priceField = new TextField();
                priceField.setPromptText(bundle.getString("price.prompt"));

                Label qtyLabel = new Label(bundle.getString("enter.quantity") + " " + i + ":");
                TextField qtyField = new TextField();
                qtyField.setPromptText(bundle.getString("quantity.prompt"));

                itemsContainer.getChildren().addAll(priceLabel, priceField, qtyLabel, qtyField);
            }
        } catch (NumberFormatException e) {
            showAlert(bundle.getString("error.invalid.number"));
        }
    }

    @FXML
    private void onCalculateTotal() {
        List<double[]> items = new ArrayList<>();
        var children = itemsContainer.getChildren();

        // Fields are in groups of 4: priceLabel, priceField, qtyLabel, qtyField
        try {
            for (int i = 1; i < children.size(); i += 4) {
                TextField priceField = (TextField) children.get(i);
                TextField qtyField   = (TextField) children.get(i + 2);

                double price = Double.parseDouble(priceField.getText().trim());
                int qty      = Integer.parseInt(qtyField.getText().trim());
                items.add(new double[]{price, qty});
            }

            double total = calculator.calculateCartTotal(items);
            totalLabel.setText(bundle.getString("total.cost") + " " +
                    String.format("%.2f", total));

        } catch (NumberFormatException e) {
            showAlert(bundle.getString("error.invalid.input"));
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        }
    }

    private void loadBundle(Locale locale) {
        bundle = ResourceBundle.getBundle("i18n/MessagesBundle", locale);
    }

    private void applyBundle() {
        selectLanguageLabel.setText(bundle.getString("select.language"));
        confirmLanguageBtn.setText(bundle.getString("confirm.language"));
        enterNumItemsLabel.setText(bundle.getString("enter.num.items"));
        enterItemsBtn.setText(bundle.getString("enter.items.btn"));
        calculateBtn.setText(bundle.getString("calculate.btn"));
        totalLabel.setText(bundle.getString("total.cost") + " -");
        numItemsField.setPromptText(bundle.getString("num.items.prompt"));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}