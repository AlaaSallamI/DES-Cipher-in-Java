package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    public TextField messageTextField;
    public TextField C0KeyTextField;
    public TextField D0KeyTextField;
    public Button encryptButton;
    public Button decryptButton;
    public Label headerLabel;
    public Label encryptedLabel;

    DesCipher desCipher;

    @FXML
    private void initialize() {

        desCipher = new DesCipher(2);

        encryptButton.setOnMouseClicked(mouseEvent -> handleEncryptButton());
        decryptButton.setOnMouseClicked(mouseEvent -> handleDecryptedButton());
    }

    private void handleEncryptButton() {
        String message = messageTextField.getText();
        String C0Key = C0KeyTextField.getText();
        String D0Key = D0KeyTextField.getText();

        desCipher.setKeys(C0Key, D0Key);
        String encryptedMessage = desCipher.encrypt(message);

        showEncryptedMessage(encryptedMessage);
    }

    private void handleDecryptedButton() {
        String encryptedMessage = encryptedLabel.getText();
        String decryptedMessage = desCipher.decrypt(encryptedMessage);
        showDecryptedMessage(decryptedMessage);
    }

    private void showEncryptedMessage(String encryptedMessage) {
        headerLabel.setVisible(true);
        encryptedLabel.setText(encryptedMessage);
        messageTextField.clear();
    }

    private void showDecryptedMessage(String decryptedMessage) {
        headerLabel.setVisible(false);
        messageTextField.setText(decryptedMessage);
        encryptedLabel.setText("");
    }

}