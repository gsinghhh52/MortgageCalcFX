import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import java.text.NumberFormat;

public class MortgageCalculator extends Application {
    private TextField houseCostField, interestRateField, paymentField;
    private CheckBox financeClosingCost;
    private RadioButton term15Years, term30Years;
    
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        
        // Top Pane
        Label titleLabel = new Label("Mortgage Calculator");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);
        StackPane topPane = new StackPane(titleLabel);
        topPane.setStyle("-fx-background-color: navy;");
        root.setTop(topPane);
        
        // Center Pane
        GridPane centerPane = new GridPane();
        centerPane.setPadding(new Insets(20));
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        
        centerPane.add(new Label("House Cost ($):"), 0, 0);
        houseCostField = new TextField();
        centerPane.add(houseCostField, 1, 0);
        
        centerPane.add(new Label("Annual Interest Rate (%):"), 0, 1);
        interestRateField = new TextField();
        centerPane.add(interestRateField, 1, 1);
        
        financeClosingCost = new CheckBox("Include $5000 Closing Cost");
        centerPane.add(financeClosingCost, 1, 2);
        
        centerPane.add(new Label("Years of Loan:"), 0, 3);
        ToggleGroup loanTermGroup = new ToggleGroup();
        term15Years = new RadioButton("15 Years");
        term15Years.setToggleGroup(loanTermGroup);
        term15Years.setSelected(true);
        term30Years = new RadioButton("30 Years");
        term30Years.setToggleGroup(loanTermGroup);
        centerPane.add(new HBox(10, term15Years, term30Years), 1, 3);
        
        centerPane.add(new Label("Monthly Payment:"), 0, 4);
        paymentField = new TextField();
        paymentField.setEditable(false);
        centerPane.add(paymentField, 1, 4);
        
        root.setCenter(centerPane);
        
        // Bottom Pane
        Button computeButton = new Button("Compute");
        computeButton.setOnAction(event -> computePayment());
        HBox bottomPane = new HBox(computeButton);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPadding(new Insets(15));
        bottomPane.setStyle("-fx-background-color: lightgray;");
        root.setBottom(bottomPane);
        
        Scene scene = new Scene(root, 450, 320);
        stage.setTitle("Mortgage Calculator");
        stage.setScene(scene);
        stage.show();
    }
    
    private void computePayment() {
        try {
            double houseCost = Double.parseDouble(houseCostField.getText().trim());
            double interestRate = Double.parseDouble(interestRateField.getText().trim()) / 100;
            
            if (houseCost <= 0) {
                showAlert("House cost must be positive.");
                return;
            }
            if (interestRate < 0.0 || interestRate > 1.0) {
                showAlert("Interest rate must be between 0.0% and 100.0%.");
                return;
            }
            
            if (financeClosingCost.isSelected()) {
                houseCost += 5000;
            }
            
            int loanYears = term15Years.isSelected() ? 15 : 30;
            int months = loanYears * 12;
            double monthlyRate = interestRate / 12;
            
            double monthlyPayment = (houseCost * monthlyRate) /
                    (1 - Math.pow(1 + monthlyRate, -months));
            
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            paymentField.setText(currencyFormat.format(monthlyPayment));
        } catch (NumberFormatException e) {
            showAlert("Please enter valid numeric values.");
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch();
    }
}
