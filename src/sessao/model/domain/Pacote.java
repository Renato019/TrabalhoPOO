/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessao.model.domain;

/**
 *
 * @author renat
 */
import java.io.Serializable;

public class Pacote implements Serializable {
    private int cdPacote;
    private String nome;
    private double preco;
    private int duracaoMinutos;
    private String descricao;
    private int qtdFotos;
    
    public Pacote() {
    }
    
    public Pacote(int cdPacote, String nome, double preco, int duracaoMinutos, String descricao, int qtdFotos) {
        this.cdPacote = cdPacote;
        this.nome = nome;
        this.preco = preco;
        this.duracaoMinutos = duracaoMinutos;
        this.descricao = descricao;
        this.qtdFotos = qtdFotos;
    }
    
    public int getCdPacote() {
        return cdPacote;
    }
    
    public void setCdPacote(int cdPacote) {
        this.cdPacote = cdPacote;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public double getPreco() {
        return preco;
    }
    
    public void setPreco(double preco) {
        this.preco = preco;
    }
    
    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }
    
    public void setDuracaoMinutos(int duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public int getQtdFotos() {
        return qtdFotos;
    }
    
    public void setQtdFotos(int qtdFotos) {
        this.qtdFotos = qtdFotos;
    }
    
    @Override
    public String toString() {
        return this.nome;
    }
}