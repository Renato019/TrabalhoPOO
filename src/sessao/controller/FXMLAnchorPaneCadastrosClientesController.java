package sessao.controller;

/*
    Angelo
*/

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sessao.model.dao.ClienteDAO;
import sessao.model.domain.Cliente;
import sessao.model.database.Database;
import sessao.model.database.DatabaseFactory;

public class FXMLAnchorPaneCadastrosClientesController implements Initializable {

    @FXML
    private TableView<Cliente> tableViewClientes;
    
    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;
    
    @FXML
    private TableColumn<Cliente, String> tableColumnClienteCpf;
    
    @FXML
    private Button buttonInserir;
    
    @FXML
    private Button buttonAlterar;
    
    @FXML
    private Button buttonRemover;
    
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

    private List<Cliente> listClientes;
    private ObservableList<Cliente> observableListClientes;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection (connection);
        
        carregarTableViewClientes();

        // Limpando a exibição dos detalhes do cliente
        selecionarItemTableViewClientes(null);

        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
    }

    public void carregarTableViewClientes() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        // Define formatação do CPF na exibição
        tableColumnClienteCpf.setCellFactory(column -> new TableCell<Cliente, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.length() != 11) {
                    setText(null);
                } else {
                    // Formata CPF no padrão 000.000.000-00
                    String cpfFormatado = item.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
                    setText(cpfFormatado);
                }
            }
        });

        // Carrega dados da tabela
        listClientes = clienteDAO.listar();
        observableListClientes = FXCollections.observableArrayList(listClientes);
        tableViewClientes.setItems(observableListClientes);
    }

    public void selecionarItemTableViewClientes(Cliente cliente) {
        if (cliente != null) {
            labelClienteNome.setText(cliente.getNome());

            // Formata CPF: 000.000.000-00
            String cpf = cliente.getCpf();
            if (cpf != null && cpf.length() == 11) {
                cpf = cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
            }
            labelClienteCpf.setText(cpf != null ? cpf : "");

            // Formata telefone: (XX) XXXXX-XXXX ou (XX) XXXX-XXXX
            String telefone = cliente.getTelefone();
            if (telefone != null) {
                if (telefone.length() == 11) {
                    telefone = telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
                } else if (telefone.length() == 10) {
                    telefone = telefone.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
                }
            }
            labelClienteTelefone.setText(telefone != null ? telefone : "");

            labelClienteEmail.setText(cliente.getEmail());
            labelClienteEmail.setWrapText(true);
            System.out.println(cliente.getEmail());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            labelClienteDataNascimento.setText(cliente.getDataNascimento().format(formatter));
        } else {
            labelClienteNome.setText("");
            labelClienteCpf.setText("");
            labelClienteTelefone.setText("");
            labelClienteEmail.setText("");
            labelClienteDataNascimento.setText("");
        }
    }

    @FXML
    public void handleButtonInserir() throws IOException {
        Cliente cliente = new Cliente();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosClientesDialog(cliente);
        if (buttonConfirmarClicked) {
            clienteDAO.inserir(cliente);
            carregarTableViewClientes();
        }
    }

    @FXML
    public void handleButtonAlterar() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();//Obtendo cliente selecionado
        if (cliente != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosClientesDialog(cliente);
            if (buttonConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViewClientes();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonRemover() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            clienteDAO.remover(cliente);
            carregarTableViewClientes();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na Tabela!");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneCadastrosClientesDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastrosClientesDialogController.class.getResource("/sessao/view/FXMLAnchorPaneCadastrosClientesDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Clientes");
        
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        FXMLAnchorPaneCadastrosClientesDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();

    }

}
