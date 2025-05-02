package sessao.model.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Comparecimento implements Serializable {
    private int id_comparecimento;
    private Agendamento agendamento;
    private Cliente cliente;
    private String status; // "COMPARECEU", "FALTOU", "CANCELADO"
    private String observacoes;
    private LocalDateTime dataRegistro;
    
    public Comparecimento() {
        this.dataRegistro = LocalDateTime.now();
    }
    
    public Comparecimento(int id_comparecimento, Agendamento agendamento, Cliente cliente, String status, String observacoes) {
        this.id_comparecimento = id_comparecimento;
        this.agendamento = agendamento;
        this.cliente = cliente;
        this.status = status;
        this.observacoes = observacoes;
        this.dataRegistro = LocalDateTime.now();
    }
    
    public int getIdComparecimento() {
        return id_comparecimento;
    }
    
    public void setIdComparecimento(int id_comparecimento) {
        this.id_comparecimento = id_comparecimento;
    }
    
    public Agendamento getAgendamento() {
        return agendamento;
    }
    
    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }
    
    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
    
    // Método para verificar se o status é de comparecimento
    public boolean isCompareceu() {
        return "COMPARECEU".equals(this.status);
    }
    
    @Override
    public String toString() {
        return "Comparecimento #" + id_comparecimento + " - Cliente: " + cliente.getNome() + " - Status: " + status;
    }
}
