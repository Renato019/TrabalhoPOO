package sessao.model.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente implements Serializable {
    private int id_cliente;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private LocalDate dataNascimento;
    private List<Pacote> pacotesContratados;
    
    public Cliente() {
        this.pacotesContratados = new ArrayList<>();
    }
    
    public Cliente(int id_cliente, String nome, String cpf, String telefone, String email, LocalDate dataNascimento) {
        this.id_cliente = id_cliente;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.pacotesContratados = new ArrayList<>();
    }
    
    public int getIdCliente() {
        return id_cliente;
    }
    
    public void setIdCliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    public List<Pacote> getPacotesContratados() {
        return pacotesContratados;
    }
    
    public void setPacotesContratados(List<Pacote> pacotesContratados) {
        this.pacotesContratados = pacotesContratados;
    }
    
    public void adicionarPacote(Pacote pacote) {
        if (this.pacotesContratados == null) {
            this.pacotesContratados = new ArrayList<>();
        }
        this.pacotesContratados.add(pacote);
    }
    
    @Override
    public String toString() {
        return this.nome;
    }
}
