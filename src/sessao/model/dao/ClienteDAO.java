/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessao.model.dao;

/**
 *
 * @author renat
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
        String sql = "INSERT INTO clientes(nome, telefone, email, dataNascimento) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setDate(4, Date.valueOf(cliente.getDataNascimento()));
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE clientes SET nome=?, telefone=?, email=?, dataNascimento=? WHERE cdCliente=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setDate(4, Date.valueOf(cliente.getDataNascimento()));
            stmt.setInt(5, cliente.getCdCliente());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Cliente cliente) {
        String sql = "DELETE FROM clientes WHERE cdCliente=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getCdCliente());
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
                cliente.setCdCliente(resultado.getInt("cdCliente"));
                cliente.setNome(resultado.getString("nome"));
                cliente.setTelefone(resultado.getString("telefone"));
                cliente.setEmail(resultado.getString("email"));
                cliente.setDataNascimento(resultado.getDate("dataNascimento").toLocalDate());
                
                // Buscando os pacotes contratados pelo cliente
                String sqlPacotes = "SELECT p.* FROM pacotes p INNER JOIN cliente_pacote cp ON p.cdPacote = cp.cdPacote WHERE cp.cdCliente = ?";
                PreparedStatement stmtPacotes = connection.prepareStatement(sqlPacotes);
                stmtPacotes.setInt(1, cliente.getCdCliente());
                ResultSet resultadoPacotes = stmtPacotes.executeQuery();
                
                while (resultadoPacotes.next()) {
                    Pacote pacote = new Pacote();
                    pacote.setCdPacote(resultadoPacotes.getInt("cdPacote"));
                    pacote.setNome(resultadoPacotes.getString("nome"));
                    pacote.setPreco(resultadoPacotes.getDouble("preco"));
                    pacote.setDuracaoMinutos(resultadoPacotes.getInt("duracaoMinutos"));
                    pacote.setDescricao(resultadoPacotes.getString("descricao"));
                    pacote.setQtdFotos(resultadoPacotes.getInt("qtdFotos"));
                    
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
        String sql = "SELECT * FROM clientes WHERE cdCliente=?";
        Cliente retorno = new Cliente();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getCdCliente());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setCdCliente(resultado.getInt("cdCliente"));
                retorno.setNome(resultado.getString("nome"));
                retorno.setTelefone(resultado.getString("telefone"));
                retorno.setEmail(resultado.getString("email"));
                retorno.setDataNascimento(resultado.getDate("dataNascimento").toLocalDate());
                
                // Buscando os pacotes contratados pelo cliente
                String sqlPacotes = "SELECT p.* FROM pacotes p INNER JOIN cliente_pacote cp ON p.cdPacote = cp.cdPacote WHERE cp.cdCliente = ?";
                PreparedStatement stmtPacotes = connection.prepareStatement(sqlPacotes);
                stmtPacotes.setInt(1, retorno.getCdCliente());
                ResultSet resultadoPacotes = stmtPacotes.executeQuery();
                
                while (resultadoPacotes.next()) {
                    Pacote pacote = new Pacote();
                    pacote.setCdPacote(resultadoPacotes.getInt("cdPacote"));
                    pacote.setNome(resultadoPacotes.getString("nome"));
                    pacote.setPreco(resultadoPacotes.getDouble("preco"));
                    pacote.setDuracaoMinutos(resultadoPacotes.getInt("duracaoMinutos"));
                    pacote.setDescricao(resultadoPacotes.getString("descricao"));
                    pacote.setQtdFotos(resultadoPacotes.getInt("qtdFotos"));
                    
                    retorno.adicionarPacote(pacote);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    public boolean adicionarPacoteContratado(Cliente cliente, Pacote pacote) {
        String sql = "INSERT INTO cliente_pacote(cdCliente, cdPacote) VALUES(?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getCdCliente());
            stmt.setInt(2, pacote.getCdPacote());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean removerPacoteContratado(Cliente cliente, Pacote pacote) {
        String sql = "DELETE FROM cliente_pacote WHERE cdCliente=? AND cdPacote=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getCdCliente());
            stmt.setInt(2, pacote.getCdPacote());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    // Método para calcular quantas sessões o cliente já agendou no mês atual
    public int contarAgendamentosNoMes(Cliente cliente, int mes, int ano) {
        String sql = "SELECT COUNT(*) FROM agendamentos WHERE cdCliente=? AND MONTH(dataHora)=? AND YEAR(dataHora)=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getCdCliente());
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
        String sql = "SELECT COUNT(*) FROM comparecimentos WHERE cdCliente=? AND status='COMPARECEU'";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getCdCliente());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    // Método para verificar se cliente tem direito a sessão gratuita
    public boolean temDireitoASessaoGratuita(Cliente cliente) {
        int sessoesPresentes = contarSessoesConcluidasComPresenca(cliente);
        return (sessoesPresentes % 3 == 0) && (sessoesPresentes > 0);
    }
}
