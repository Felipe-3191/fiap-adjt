package com.alura.alurabank.dominio.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ContaCorrenteForm {

    @JsonProperty
    @NotBlank(message = "banco não pode ser nulo nem estar em branco")

    private String banco;
    @JsonProperty
    @NotBlank(message = "agencia não pode ser nulq nem estar em branco")

    private String agencia;
    @JsonProperty
    @NotBlank(message = "número da conta não pode ser nulo nem estar em branco")

    private String numero;


}
