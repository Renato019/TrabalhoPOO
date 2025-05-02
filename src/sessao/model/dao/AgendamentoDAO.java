package sessao.model.dao;

/*
 * @author renato
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
        String sql = "INSERT INTO agendamentos(id_cliente, id_fotografo, id_pacote, data_hora) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, agendamento.getCliente().getIdCliente());
            stmt.setInt(2, agendamento.getFotografo().getIdFotografo());
            stmt.setInt(3, agendamento.getServico().getIdPacote());
            stmt.setTimestamp(4, Timestamp.valueOf(agendamento.getDataHora()));
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AgendamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Agendamento agendamento) {
        String sql = "UPDATE agendamentos SET id_cliente=?, id_fotografo=?, id_pacote=?, data_hora=? WHERE id_agendamento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, agendamento.getCliente().getIdCliente());
            stmt.setInt(2, agendamento.getFotografo().getIdFotografo());
            stmt.setInt(3, agendamento.getServico().getIdPacote());
            stmt.setTimestamp(4, Timestamp.valueOf(agendamento.getDataHora()));
            stmt.setInt(5, agendamento.getIdAgendamento());

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AgendamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Agendamento agendamento) {
        String sql = "DELETE FROM agendamentos WHERE id_agendamento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, agendamento.getIdAgendamento());
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

                agendamento.setIdAgendamento(resultado.getInt("id_agendamento"));

                Cliente cliente = new Cliente();
                cliente.setIdCliente(resultado.getInt("id_cliente"));
                agendamento.setCliente(cliente);

                Fotografo fotografo = new Fotografo();
                fotografo.setIdFotografo(resultado.getInt("id_fotografo"));
                agendamento.setFotografo(fotografo);

                Pacote pacote = new Pacote();
                pacote.setIdPacote(resultado.getInt("id_pacote"));
                agendamento.setServico(pacote);

                LocalDateTime dataHora = resultado.getTimestamp("data_hora").toLocalDateTime();
                agendamento.setDataHora(dataHora);

                lista.add(agendamento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AgendamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}