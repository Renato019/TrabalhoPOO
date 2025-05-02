package sessao.model.dao;

/*
 * @author renato
*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sessao.model.domain.Fotografo;

public class FotografoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Fotografo fotografo) {
        String sql = "INSERT INTO fotografos(nome, especialidade, nivel_experiencia, telefone) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, fotografo.getNome());
            stmt.setString(2, fotografo.getEspecialidade());
            stmt.setString(3, fotografo.getNivelExperiencia());
            stmt.setString(4, fotografo.getTelefone());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Fotografo fotografo) {
        String sql = "UPDATE fotografos SET nome=?, especialidade=?, nivel_experiencia=?, telefone=? WHERE id_fotografo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, fotografo.getNome());
            stmt.setString(2, fotografo.getEspecialidade());
            stmt.setString(3, fotografo.getNivelExperiencia());
            stmt.setString(4, fotografo.getTelefone());
            stmt.setInt(5, fotografo.getIdFotografo());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Fotografo fotografo) {
        String sql = "DELETE FROM fotografos WHERE id_fotografo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, fotografo.getIdFotografo());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Fotografo> listar() {
        String sql = "SELECT * FROM fotografos";
        List<Fotografo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Fotografo fotografo = new Fotografo();
                fotografo.setIdFotografo(resultado.getInt("id_fotografo"));
                fotografo.setNome(resultado.getString("nome"));
                fotografo.setEspecialidade(resultado.getString("especialidade"));
                fotografo.setNivelExperiencia(resultado.getString("nivel_experiencia"));
                fotografo.setTelefone(resultado.getString("telefone"));
                
                retorno.add(fotografo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Fotografo buscar(Fotografo fotografo) {
        String sql = "SELECT * FROM fotografos WHERE id_fotografo=?";
        Fotografo retorno = new Fotografo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, fotografo.getIdFotografo());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setIdFotografo(resultado.getInt("id_fotografo"));
                retorno.setNome(resultado.getString("nome"));
                retorno.setEspecialidade(resultado.getString("especialidade"));
                retorno.setNivelExperiencia(resultado.getString("nivel_experiencia"));
                retorno.setTelefone(resultado.getString("telefone"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    // Método para verificar se o fotógrafo tem disponibilidade em determinado horário
    public boolean verificarDisponibilidade(Fotografo fotografo, LocalDateTime dataHora, int duracaoMinutos) {
        // Verificar se não há outro agendamento no intervalo de tempo
        String sql = "SELECT COUNT(*) FROM agendamentos a " +
                     "INNER JOIN pacotes p ON a.id_pacote = p.id_pacote " +
                     "WHERE a.id_fotografo = ? " +
                     "AND ((a.data_hora BETWEEN ? AND ? + INTERVAL '" + duracaoMinutos + " minutes') " +
                     "OR (a.data_hora + INTERVAL 'p.duracao_minutos minutes' BETWEEN ? AND ? + INTERVAL '" + duracaoMinutos + " minutes'))";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, fotografo.getIdFotografo());
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(dataHora));
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(dataHora));
            stmt.setTimestamp(5, java.sql.Timestamp.valueOf(dataHora));
            
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return resultado.getInt(1) == 0; // Retorna true se não houver agendamentos conflitantes
            }
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    // Método para contar o total de sessões realizadas por um fotógrafo
    public int contarSessoesRealizadas(Fotografo fotografo) {
        String sql = "SELECT COUNT(*) FROM agendamentos a " +
                     "INNER JOIN comparecimentos c ON a.id_agendamento = c.id_agendamento " +
                     "WHERE a.id_fotografo = ? AND c.status = 'confirmado'";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, fotografo.getIdFotografo());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    // Método para listar os fotógrafos com mais sessões realizadas
    public List<Fotografo> listarComMaisSessoes() {
        String sql = "SELECT f.*, COUNT(a.id_agendamento) as total_sessoes " +
                     "FROM fotografos f " +
                     "INNER JOIN agendamentos a ON f.id_fotografo = a.id_fotografo " +
                     "INNER JOIN comparecimentos c ON a.id_agendamento = c.id_agendamento " +
                     "WHERE c.status = 'confirmado' " +
                     "GROUP BY f.id_fotografo " +
                     "ORDER BY total_sessoes DESC";
        List<Fotografo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Fotografo fotografo = new Fotografo();
                fotografo.setIdFotografo(resultado.getInt("id_fotografo"));
                fotografo.setNome(resultado.getString("nome"));
                fotografo.setEspecialidade(resultado.getString("especialidade"));
                fotografo.setNivelExperiencia(resultado.getString("nivel_experiencia"));
                fotografo.setTelefone(resultado.getString("telefone"));
                
                retorno.add(fotografo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}