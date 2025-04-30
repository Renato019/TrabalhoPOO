/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessao.model.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente implements Serializable {
    private int cdCliente;
    private String nome;
    private String telefone;
    private String email;
    private LocalDate dataNascimento;
    private List<Pacote> pacotesContratados;
    
    public Cliente() {
        this.pacotesContratados = new ArrayList<>();
    }
    
    public Cliente(int cdCliente, String nome, String telefone, String email, LocalDate dataNascimento) {
        this.cdCliente = cdCliente;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.pacotesContratados = new ArrayList<>();
    }
    
    public int getCdCliente() {
        return cdCliente;
    }
    
    public void setCdCliente(int cdCliente) {
        this.cdCliente = cdCliente;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
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
