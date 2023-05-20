package com.alura.alurabank.dominio.form;

import com.alura.alurabank.dominio.Correntista;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CorrentistaForm {

    @JsonProperty
    private String cpf;

    @JsonProperty
    private String nome;

    public Correntista toCorrentista(){
        return new Correntista(nome,cpf);
    }

}
