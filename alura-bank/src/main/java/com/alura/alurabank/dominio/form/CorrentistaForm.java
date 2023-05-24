package com.alura.alurabank.dominio.form;

import com.alura.alurabank.dominio.Correntista;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CorrentistaForm {

    @JsonProperty
    @CPF
    @NotNull
    private String cpf;

    @JsonProperty
    @NotBlank(message = "nome do correntista é obrigatório")
    private String nome;

    public Correntista toCorrentista(){
        return new Correntista(nome,cpf);
    }

}
