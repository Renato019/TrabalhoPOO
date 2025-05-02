package sessao.model.dao;

/*
 * @author renato
*/

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sessao.model.domain.Cliente;
import sessao.model.domain.Pacote;


public class ClienteDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Cliente cliente) {
        String sql = "INSERT INTO clientes(nome, cpf, telefone, email, data_nascimento) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setDate(5, Date.valueOf(cliente.getDataNascimento()));
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE clientes SET nome=?, cpf=?, telefone=?, email=?, data_nascimento=? WHERE id_cliente=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setDate(5, Date.valueOf(cliente.getDataNascimento()));
            stmt.setInt(6, cliente.getIdCliente());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Cliente cliente) {
        String sql = "DELETE FROM clientes WHERE id_cliente=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getIdCliente());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Cliente> listar() {
        String sql = "SELECT * FROM clientes";
        List<Cliente> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(resultado.getInt("id_cliente"));
                cliente.setNome(resultado.getString("nome"));
                cliente.setCpf(resultado.getString("cpf"));
                cliente.setTelefone(resultado.getString("telefone"));
                cliente.setEmail(resultado.getString("email"));
                cliente.setDataNascimento(resultado.getDate("data_nascimento").toLocalDate());
                
                // Buscando os pacotes contratados pelo cliente
                String sqlPacotes = "SELECT p.* FROM pacotes p INNER JOIN pacotes_contratados pc ON p.id_pacote = pc.id_pacote WHERE pc.id_cliente = ?";
                PreparedStatement stmtPacotes = connection.prepareStatement(sqlPacotes);
                stmtPacotes.setInt(1, cliente.getIdCliente());
                ResultSet resultadoPacotes = stmtPacotes.executeQuery();
                
                while (resultadoPacotes.next()) {
                    Pacote pacote = new Pacote();
                    pacote.setIdPacote(resultadoPacotes.getInt("id_pacote"));
                    pacote.setNome(resultadoPacotes.getString("nome"));
                    pacote.setPreco(resultadoPacotes.getDouble("preco"));
                    pacote.setDuracaoMinutos(resultadoPacotes.getInt("duracao_minutos"));
                    pacote.setDescricao(resultadoPacotes.getString("descricao"));
                    pacote.setQtdFotos(resultadoPacotes.getInt("qtd_fotos"));
                    
                    cliente.adicionarPacote(pacote);
                }
                
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Cliente buscar(Cliente cliente) {
        String sql = "SELECT * FROM clientes WHERE id_cliente=?";
        Cliente retorno = new Cliente();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getIdCliente());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setIdCliente(resultado.getInt("id_cliente"));
                retorno.setNome(resultado.getString("nome"));
                retorno.setNome(resultado.getString("cpf"));
                retorno.setTelefone(resultado.getString("telefone"));
                retorno.setEmail(resultado.getString("email"));
                retorno.setDataNascimento(resultado.getDate("data_nascimento").toLocalDate());
                
                // Buscando os pacotes contratados pelo cliente
                String sqlPacotes = "SELECT p.* FROM pacotes p INNER JOIN pacotes_contratados pc ON p.id_pacote = pc.id_pacote WHERE pc.id_cliente = ?";
                PreparedStatement stmtPacotes = connection.prepareStatement(sqlPacotes);
                stmtPacotes.setInt(1, retorno.getIdCliente());
                ResultSet resultadoPacotes = stmtPacotes.executeQuery();
                
                while (resultadoPacotes.next()) {
                    Pacote pacote = new Pacote();
                    pacote.setIdPacote(resultadoPacotes.getInt("id_pacote"));
                    pacote.setNome(resultadoPacotes.getString("nome"));
                    pacote.setPreco(resultadoPacotes.getDouble("preco"));
                    pacote.setDuracaoMinutos(resultadoPacotes.getInt("duracao_minutos"));
                    pacote.setDescricao(resultadoPacotes.getString("descricao"));
                    pacote.setQtdFotos(resultadoPacotes.getInt("qtd_fotos"));
                    
                    retorno.adicionarPacote(pacote);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    public boolean adicionarPacoteContratado(Cliente cliente, Pacote pacote) {
        String sql = "INSERT INTO pacotes_contratados(id_cliente, id_pacote, data_contratacao) VALUES(?,?,CURRENT_DATE)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getIdCliente());
            stmt.setInt(2, pacote.getIdPacote());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean removerPacoteContratado(Cliente cliente, Pacote pacote) {
        String sql = "DELETE FROM pacotes_contratados WHERE id_cliente=? AND id_pacote=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getIdCliente());
            stmt.setInt(2, pacote.getIdPacote());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    // Método para calcular quantas sessões o cliente já agendou no mês atual
    public int contarAgendamentosNoMes(Cliente cliente, int mes, int ano) {
        String sql = "SELECT COUNT(*) FROM agendamentos WHERE id_cliente=? AND EXTRACT(MONTH FROM data_hora)=? AND EXTRACT(YEAR FROM data_hora)=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getIdCliente());
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    // Método para contar sessões concluídas com presença confirmada
    public int contarSessoesConcluidasComPresenca(Cliente cliente) {
        String sql = "SELECT COUNT(*) FROM comparecimentos WHERE id_cliente=? AND status='confirmado'";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getIdCliente());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}