package sessao.model.domain;

/*
 * @author renat
*/

import java.io.Serializable;

public class Fotografo implements Serializable {
    private int id_fotografo;
    private String nome;
    private String especialidade;
    private String nivelExperiencia;
    private String telefone;
    
    public Fotografo() {
    }
    
    public Fotografo(int id_fotografo, String nome, String especialidade, String nivelExperiencia, String telefone) {
        this.id_fotografo = id_fotografo;
        this.nome = nome;
        this.especialidade = especialidade;
        this.nivelExperiencia = nivelExperiencia;
        this.telefone = telefone;
    }
    
    public int getIdFotografo() {
        return id_fotografo;
    }
    
    public void setIdFotografo(int id_fotografo) {
        this.id_fotografo = id_fotografo;
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
