package com.alura.alurabank.repositorio;

import com.alura.alurabank.dominio.ContaCorrente;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class RepositorioContasCorrente {
    private Set<ContaCorrente> contas;

    public RepositorioContasCorrente(){
        this.contas = new HashSet<>();
    }
    public void salvar(ContaCorrente conta) {
        contas.add(conta);
    }

    public Optional<ContaCorrente> buscar(String banco, String agencia, String numero) {
        return this.contas.stream()
        .filter( c-> c.identificadaPor(banco,agencia,numero))
                .findFirst();
    }

    public void fechar(ContaCorrente conta) {
    contas.remove(conta);

    }

    public Optional<ContaCorrente> buscar(ContaCorrente conta) {
        return buscar(conta.getBanco(), conta.getAgencia(), conta.getNumero());
    }
}
