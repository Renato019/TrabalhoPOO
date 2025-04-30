/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessao.model.domain;

import java.io.Serializable;

public class Fotografo implements Serializable {
    private int cdFotografo;
    private String nome;
    private String especialidade;
    private String nivelExperiencia;
    private String telefone;
    
    public Fotografo() {
    }
    
    public Fotografo(int cdFotografo, String nome, String especialidade, String nivelExperiencia, String telefone) {
        this.cdFotografo = cdFotografo;
        this.nome = nome;
        this.especialidade = especialidade;
        this.nivelExperiencia = nivelExperiencia;
        this.telefone = telefone;
    }
    
    public int getCdFotografo() {
        return cdFotografo;
    }
    
    public void setCdFotografo(int cdFotografo) {
        this.cdFotografo = cdFotografo;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEspecialidade() {
        return especialidade;
    }
    
    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
    
    public String getNivelExperiencia() {
        return nivelExperiencia;
    }
    
    public void setNivelExperiencia(String nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    @Override
    public String toString() {
        return this.nome;
    }
}
