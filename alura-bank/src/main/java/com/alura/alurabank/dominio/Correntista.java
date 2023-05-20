package com.alura.alurabank.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Correntista {
@JsonProperty
    private String cpf;
  @JsonProperty
    private String nome;

  private LocalDate dataDeEntrada = LocalDate.now();


    public Correntista(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public LocalDate getDataDeEntrada() {
        return dataDeEntrada;
    }
}
