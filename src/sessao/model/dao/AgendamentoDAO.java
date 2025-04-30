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
import sessao.model.domain.Fotografo;
import sessao.model.domain.Pacote;

public class AgendamentoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Agendamento agendamento) {
        String sql = "INSERT INTO agendamentos(cdCliente, cdFotografo, cdPacote, dataHora, gratuito) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, agendamento.getCliente().getCdCliente());
            stmt.setInt(2, agendamento.getFotografo().getCdFotografo());
            stmt.setInt(3, agendamento.getServico().getCdPacote());
            stmt.setTimestamp(4, Timestamp.valueOf(agendamento.getDataHora()));
            stmt.setBoolean(5, agendamento.isGratuito());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AgendamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Agendamento agendamento) {
        String sql = "UPDATE agendamentos SET cdCliente=?, cdFotografo=?, cdPacote=?, dataHora=?, gratuito=? WHERE cdAgendamento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, agendamento.getCliente().getCdCliente());
            stmt.setInt(2, agendamento.getFotografo().getCdFotografo());
            stmt.setInt(3, agendamento.getServico().getCdPacote());
            stmt.setTimestamp(4, Timestamp.valueOf(agendamento.getDataHora()));
            stmt.setBoolean(5, agendamento.isGratuito());
            stmt.setInt(6, agendamento.getCdAgendamento());

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AgendamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Agendamento agendamento) {
        String sql = "DELETE FROM agendamentos WHERE cdAgendamento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, agendamento.getCdAgendamento());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AgendamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Agendamento> listar() {
        List<Agendamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                Agendamento agendamento = new Agendamento();

                agendamento.setCdAgendamento(resultado.getInt("cdAgendamento"));

                Cliente cliente = new Cliente();
                cliente.setCdCliente(resultado.getInt("cdCliente"));
                agendamento.setCliente(cliente);

                Fotografo fotografo = new Fotografo();
                fotografo.setCdFotografo(resultado.getInt("cdFotografo"));
                agendamento.setFotografo(fotografo);

                Pacote pacote = new Pacote();
                pacote.setCdPacote(resultado.getInt("cdPacote"));
                agendamento.setServico(pacote);

                LocalDateTime dataHora = resultado.getTimestamp("dataHora").toLocalDateTime();
                agendamento.setDataHora(dataHora);

                agendamento.setGratuito(resultado.getBoolean("gratuito"));

                lista.add(agendamento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgendamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}
