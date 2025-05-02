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
        String sql = "INSERT INTO comparecimentos(id_agendamento, id_cliente, status, observacoes, data_registro) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, comparecimento.getAgendamento().getIdAgendamento());
            stmt.setInt(2, comparecimento.getCliente().getIdCliente());
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
        String sql = "UPDATE comparecimentos SET id_agendamento=?, id_cliente=?, status=?, observacoes=?, data_registro=? WHERE id_comparecimento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, comparecimento.getAgendamento().getIdAgendamento());
            stmt.setInt(2, comparecimento.getCliente().getIdCliente());
            stmt.setString(3, comparecimento.getStatus());
            stmt.setString(4, comparecimento.getObservacoes());
            stmt.setTimestamp(5, Timestamp.valueOf(comparecimento.getDataRegistro()));
            stmt.setInt(6, comparecimento.getIdComparecimento());

            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ComparecimentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Comparecimento comparecimento) {
        String sql = "DELETE FROM comparecimentos WHERE id_comparecimento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, comparecimento.getIdComparecimento());
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

                comparecimento.setIdComparecimento(resultado.getInt("id_comparecimento"));

                Agendamento agendamento = new Agendamento();
                agendamento.setIdAgendamento(resultado.getInt("id_agendamento"));
                comparecimento.setAgendamento(agendamento);

                Cliente cliente = new Cliente();
                cliente.setIdCliente(resultado.getInt("id_cliente"));
                comparecimento.setCliente(cliente);

                comparecimento.setStatus(resultado.getString("status"));
                comparecimento.setObservacoes(resultado.getString("observacoes"));

                LocalDateTime dataRegistro = resultado.getTimestamp("data_registro").toLocalDateTime();
                comparecimento.setDataRegistro(dataRegistro);

                lista.add(comparecimento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ComparecimentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}