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
        String sql = "INSERT INTO fotografos(nome, especialidade, nivelExperiencia, telefone) VALUES(?,?,?,?)";
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
        String sql = "UPDATE fotografos SET nome=?, especialidade=?, nivelExperiencia=?, telefone=? WHERE cdFotografo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, fotografo.getNome());
            stmt.setString(2, fotografo.getEspecialidade());
            stmt.setString(3, fotografo.getNivelExperiencia());
            stmt.setString(4, fotografo.getTelefone());
            stmt.setInt(5, fotografo.getCdFotografo());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Fotografo fotografo) {
        String sql = "DELETE FROM fotografos WHERE cdFotografo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, fotografo.getCdFotografo());
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
                fotografo.setCdFotografo(resultado.getInt("cdFotografo"));
                fotografo.setNome(resultado.getString("nome"));
                fotografo.setEspecialidade(resultado.getString("especialidade"));
                fotografo.setNivelExperiencia(resultado.getString("nivelExperiencia"));
                fotografo.setTelefone(resultado.getString("telefone"));
                
                retorno.add(fotografo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Fotografo buscar(Fotografo fotografo) {
        String sql = "SELECT * FROM fotografos WHERE cdFotografo=?";
        Fotografo retorno = new Fotografo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, fotografo.getCdFotografo());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setCdFotografo(resultado.getInt("cdFotografo"));
                retorno.setNome(resultado.getString("nome"));
                retorno.setEspecialidade(resultado.getString("especialidade"));
                retorno.setNivelExperiencia(resultado.getString("nivelExperiencia"));
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
                     "INNER JOIN pacotes p ON a.cdPacote = p.cdPacote " +
                     "WHERE a.cdFotografo = ? " +
                     "AND ((a.dataHora BETWEEN ? AND ADDTIME(?, CONCAT(?, ':00'))) " +
                     "OR (ADDTIME(a.dataHora, CONCAT(p.duracaoMinutos, ':00')) BETWEEN ? AND ADDTIME(?, CONCAT(?, ':00'))))";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, fotografo.getCdFotografo());
            stmt.setObject(2, dataHora);
            stmt.setObject(3, dataHora);
            stmt.setInt(4, duracaoMinutos);
            stmt.setObject(5, dataHora);
            stmt.setObject(6, dataHora);
            stmt.setInt(7, duracaoMinutos);
            
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
                     "INNER JOIN comparecimentos c ON a.cdAgendamento = c.cdAgendamento " +
                     "WHERE a.cdFotografo = ? AND c.status = 'COMPARECEU'";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, fotografo.getCdFotografo());
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
        String sql = "SELECT f.*, COUNT(a.cdAgendamento) as totalSessoes " +
                     "FROM fotografos f " +
                     "INNER JOIN agendamentos a ON f.cdFotografo = a.cdFotografo " +
                     "INNER JOIN comparecimentos c ON a.cdAgendamento = c.cdAgendamento " +
                     "WHERE c.status = 'COMPARECEU' " +
                     "GROUP BY f.cdFotografo " +
                     "ORDER BY totalSessoes DESC";
        List<Fotografo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Fotografo fotografo = new Fotografo();
                fotografo.setCdFotografo(resultado.getInt("cdFotografo"));
                fotografo.setNome(resultado.getString("nome"));
                fotografo.setEspecialidade(resultado.getString("especialidade"));
                fotografo.setNivelExperiencia(resultado.getString("nivelExperiencia"));
                fotografo.setTelefone(resultado.getString("telefone"));
                
                retorno.add(fotografo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FotografoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
