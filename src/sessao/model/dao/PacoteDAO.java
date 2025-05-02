package sessao.model.dao;

/*
 * @author renato
*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sessao.model.domain.Pacote;

public class PacoteDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Pacote pacote) {
        String sql = "INSERT INTO pacotes(nome, preco, duracao_minutos, descricao, qtd_fotos) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, pacote.getNome());
            stmt.setDouble(2, pacote.getPreco());
            stmt.setInt(3, pacote.getDuracaoMinutos());
            stmt.setString(4, pacote.getDescricao());
            stmt.setInt(5, pacote.getQtdFotos());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PacoteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Pacote pacote) {
        String sql = "UPDATE pacotes SET nome=?, preco=?, duracao_minutos=?, descricao=?, qtd_fotos=? WHERE id_pacote=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, pacote.getNome());
            stmt.setDouble(2, pacote.getPreco());
            stmt.setInt(3, pacote.getDuracaoMinutos());
            stmt.setString(4, pacote.getDescricao());
            stmt.setInt(5, pacote.getQtdFotos());
            stmt.setInt(6, pacote.getIdPacote());
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PacoteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Pacote pacote) {
        String sql = "DELETE FROM pacotes WHERE id_pacote=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pacote.getIdPacote());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PacoteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Pacote> listar() {
        String sql = "SELECT * FROM pacotes";
        List<Pacote> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Pacote pacote = new Pacote();
                pacote.setIdPacote(resultado.getInt("id_pacote"));
                pacote.setNome(resultado.getString("nome"));
                pacote.setPreco(resultado.getDouble("preco"));
                pacote.setDuracaoMinutos(resultado.getInt("duracao_minutos"));
                pacote.setDescricao(resultado.getString("descricao"));
                pacote.setQtdFotos(resultado.getInt("qtd_fotos"));
                
                retorno.add(pacote);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PacoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Pacote buscar(Pacote pacote) {
        String sql = "SELECT * FROM pacotes WHERE id_pacote=?";
        Pacote retorno = new Pacote();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pacote.getIdPacote());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setIdPacote(resultado.getInt("id_pacote"));
                retorno.setNome(resultado.getString("nome"));
                retorno.setPreco(resultado.getDouble("preco"));
                retorno.setDuracaoMinutos(resultado.getInt("duracao_minutos"));
                retorno.setDescricao(resultado.getString("descricao"));
                retorno.setQtdFotos(resultado.getInt("qtd_fotos"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PacoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    // Método para listar pacotes por popularidade (mais agendados)
    public List<Pacote> listarPorPopularidade() {
        String sql = "SELECT p.*, COUNT(a.id_agendamento) as total " +
                     "FROM pacotes p " +
                     "INNER JOIN agendamentos a ON p.id_pacote = a.id_pacote " +
                     "GROUP BY p.id_pacote " +
                     "ORDER BY total DESC";
        List<Pacote> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Pacote pacote = new Pacote();
                pacote.setIdPacote(resultado.getInt("id_pacote"));
                pacote.setNome(resultado.getString("nome"));
                pacote.setPreco(resultado.getDouble("preco"));
                pacote.setDuracaoMinutos(resultado.getInt("duracao_minutos"));
                pacote.setDescricao(resultado.getString("descricao"));
                pacote.setQtdFotos(resultado.getInt("qtd_fotos"));
                
                retorno.add(pacote);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PacoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    // Método para contar comparecimentos por pacote em um mês específico
    public int contarComparecimentosPorPacoteNoMes(Pacote pacote, int mes, int ano) {
        String sql = "SELECT COUNT(*) FROM agendamentos a " +
                     "INNER JOIN comparecimentos c ON a.id_agendamento = c.id_agendamento " +
                     "WHERE a.id_pacote = ? AND EXTRACT(MONTH FROM a.data_hora) = ? AND EXTRACT(YEAR FROM a.data_hora) = ? " +
                     "AND c.status = 'confirmado'";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pacote.getIdPacote());
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PacoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    // Método para listar pacotes por tipo de serviço (cada pacote é um tipo de serviço)
    public List<Object[]> listarSessoesPorTipoServico() {
        String sql = "SELECT p.nome, COUNT(a.id_agendamento) as totalSessoes " +
                     "FROM pacotes p " +
                     "INNER JOIN agendamentos a ON p.id_pacote = a.id_pacote " +
                     "GROUP BY p.id_pacote " +
                     "ORDER BY totalSessoes DESC";
        List<Object[]> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Object[] linha = {
                    resultado.getString("nome"),
                    resultado.getInt("totalSessoes")
                };
                retorno.add(linha);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PacoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}