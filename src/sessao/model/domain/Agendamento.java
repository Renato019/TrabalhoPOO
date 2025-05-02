package sessao.model.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Agendamento implements Serializable {
    private int id_agendamento;
    private Cliente cliente;
    private Fotografo fotografo;
    private Pacote servico;
    private LocalDateTime dataHora;
    private boolean gratuito;
    
    public Agendamento() {
    }
    
    public Agendamento(int id_agendamento, Cliente cliente, Fotografo fotografo, Pacote servico, LocalDateTime dataHora) {
        this.id_agendamento = id_agendamento;
        this.cliente = cliente;
        this.fotografo = fotografo;
        this.servico = servico;
        this.dataHora = dataHora;
        this.gratuito = false;
    }
    
    public int getIdAgendamento() {
        return id_agendamento;
    }
    
    public void setIdAgendamento(int id_agendamento) {
        this.id_agendamento = id_agendamento;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Fotografo getFotografo() {
        return fotografo;
    }
    
    public void setFotografo(Fotografo fotografo) {
        this.fotografo = fotografo;
    }
    
    public Pacote getServico() {
        return servico;
    }
    
    public void setServico(Pacote servico) {
        this.servico = servico;
    }
    
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    
    public boolean isGratuito() {
        return gratuito;
    }
    
    public void setGratuito(boolean gratuito) {
        this.gratuito = gratuito;
    }
    
    // Método para verificar se o horário do agendamento já passou
    public boolean isPeriodoPassado() {
        LocalDateTime agora = LocalDateTime.now();
        return dataHora.isBefore(agora);
    }
    
    @Override
    public String toString() {
        return "Agendamento #" + id_agendamento + " - " + cliente.getNome() + " com " + fotografo.getNome();
    }
}
