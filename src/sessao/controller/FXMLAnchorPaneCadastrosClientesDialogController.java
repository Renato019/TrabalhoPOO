package sessao.controller;

/*
    Angelo
*/

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sessao.model.domain.Cliente;

public class FXMLAnchorPaneCadastrosClientesDialogController implements Initializable {

    @FXML
    private Label labelClienteNome;

    @FXML
    private Label labelClienteCpf;

    @FXML
    private Label labelClienteTelefone;

    @FXML
    private Label labelClienteEmail;

    @FXML
    private Label labelClienteDataNascimento;

    @FXML
    private TextField textFieldClienteNome;

    @FXML
    private TextField textFieldClienteCpf;

    @FXML
    private TextField textFieldClienteTelefone;

    @FXML
    private TextField textFieldClienteEmail;

    @FXML
    private DatePicker dataPickerFieldClienteDataNascimento;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private Button buttonCancelar;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Nenhuma inicialização necessária por enquanto
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.textFieldClienteNome.setText(cliente.getNome());
        this.textFieldClienteCpf.setText(cliente.getCpf());
        this.textFieldClienteTelefone.setText(cliente.getTelefone());
        this.textFieldClienteEmail.setText(cliente.getEmail());
        this.dataPickerFieldClienteDataNascimento.setValue(cliente.getDataNascimento());
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    @FXML
    public void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            cliente.setNome(textFieldClienteNome.getText());
            cliente.setCpf(textFieldClienteCpf.getText());
            cliente.setTelefone(textFieldClienteTelefone.getText());
            cliente.setEmail(textFieldClienteEmail.getText());
            cliente.setDataNascimento(dataPickerFieldClienteDataNascimento.getValue());

            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar() {
        getDialogStage().close();
    }

    // Validar entrada de dados para o cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldClienteNome.getText() == null || textFieldClienteNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (textFieldClienteCpf.getText() == null || textFieldClienteCpf.getText().length() == 0) {
            errorMessage += "CPF inválido!\n";
        }
        if (textFieldClienteTelefone.getText() == null || textFieldClienteTelefone.getText().length() == 0) {
            errorMessage += "Telefone inválido!\n";
        }
        if (textFieldClienteEmail.getText() == null || textFieldClienteEmail.getText().length() == 0) {
            errorMessage += "Email inválido!\n";
        }
        if (dataPickerFieldClienteDataNascimento.getValue() == null) {
            errorMessage += "Data de nascimento inválida!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Mostrando a mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
