/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessao.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sessao.model.domain.Agendamento;
import sessao.model.domain.Cliente;
import sessao.model.domain.Comparecimento;

public class ComparecimentoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Comparecimento comparecimento) {
        String sql = "INSERT INTO comparecimentos(cdAgendamento, cdCliente, status, observacoes, dataRegistro) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, comparecimento.getAgendamento().getCdAgendamento());
            stmt.setInt(2, comparecimento.getCliente().getCdCliente());
            stmt.setString(3, comparecimento.getStatus());
            stmt.setString(4, comparecimento.getObservacoes());
            stmt.setTimestamp(5, Timestamp.valueOf(comparecimento.getDataRegistro()));

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ComparecimentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Comparecimento comparecimento) {
        String sql = "UPDATE comparecimentos SET cdAgendamento=?, cdCliente=?, status=?, observacoes=?, dataRegistro=? WHERE cdComparecimento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, comparecimento.getAgendamento().getCdAgendamento());
            stmt.setInt(2, comparecimento.getCliente().getCdCliente());
            stmt.setString(3, comparecimento.getStatus());
            stmt.setString(4, comparecimento.getObservacoes());
            stmt.setTimestamp(5, Timestamp.valueOf(comparecimento.getDataRegistro()));
            stmt.setInt(6, comparecimento.getCdComparecimento());

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ComparecimentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Comparecimento comparecimento) {
        String sql = "DELETE FROM comparecimentos WHERE cdComparecimento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, comparecimento.getCdComparecimento());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ComparecimentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Comparecimento> listar() {
        List<Comparecimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM comparecimentos";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                Comparecimento comparecimento = new Comparecimento();

                comparecimento.setCdComparecimento(resultado.getInt("cdComparecimento"));

                Agendamento agendamento = new Agendamento();
                agendamento.setCdAgendamento(resultado.getInt("cdAgendamento"));
                comparecimento.setAgendamento(agendamento);

                Cliente cliente = new Cliente();
                cliente.setCdCliente(resultado.getInt("cdCliente"));
                comparecimento.setCliente(cliente);

                comparecimento.setStatus(resultado.getString("status"));
                comparecimento.setObservacoes(resultado.getString("observacoes"));

                LocalDateTime dataRegistro = resultado.getTimestamp("dataRegistro").toLocalDateTime();
                comparecimento.setDataRegistro(dataRegistro);

                lista.add(comparecimento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ComparecimentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}