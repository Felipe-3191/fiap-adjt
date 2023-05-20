package com.alura.alurabank.com.alura.alurabank.controller;

import com.alura.alurabank.dominio.ContaCorrente;
import com.alura.alurabank.dominio.Correntista;
import com.alura.alurabank.dominio.MovimentacaoDeConta;
import com.alura.alurabank.dominio.form.CorrentistaForm;
import com.alura.alurabank.repositorio.RepositorioContasCorrente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/contas")
public class ContaCorrenteController {

    @Autowired
    private RepositorioContasCorrente repositorioContasCorrente;

    @GetMapping
    public String consultarSaldo(@RequestParam(name="banco") String banco,
                                 @RequestParam(name="agencia") String agencia,
                                 @RequestParam(name="numero") String numero){

        ContaCorrente contaCorrente =
                repositorioContasCorrente.buscar(banco, agencia, numero)
                        .orElse(new ContaCorrente());

        return String.format("Banco: %s, Agencia: %s, Conta: %s. Saldo: %s", banco, agencia, numero, contaCorrente.lerSaldo());

    }

    @PostMapping
    public ResponseEntity<ContaCorrente> criarNovaConta(@RequestBody Correntista correntista) {

        //Correntista correntista = correntistaForm.toCorrentista();

        String banco = "333";
        String agencia = "4444";
        String numero = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
        ContaCorrente conta = new ContaCorrente(banco,agencia,numero, correntista);

        repositorioContasCorrente.salvar(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(conta);

    }

    @DeleteMapping
    public String fecharConta(@RequestBody ContaCorrente conta){
        if (repositorioContasCorrente.buscar(conta).isEmpty()) {
            return "Conta não encontrada";
        }
        repositorioContasCorrente.fechar(conta);
        return "Conta fechada com sucesso";
    }

    @PutMapping
    public ResponseEntity<String> movimentarConta(@RequestBody MovimentacaoDeConta movimentacaoDeConta){

        Optional<ContaCorrente>  opContaCorrente = repositorioContasCorrente
                .buscar(movimentacaoDeConta.getBanco(), movimentacaoDeConta.getAgencia(), movimentacaoDeConta.getNumero());

        if(opContaCorrente.isEmpty()) {
            return ResponseEntity.badRequest().body("Conta corrente não existe");
        }

        else {
            ContaCorrente contaCorrente = opContaCorrente.get();
            movimentacaoDeConta.executarEm(contaCorrente);
            repositorioContasCorrente.salvar(contaCorrente);
            return ResponseEntity.ok("Movimentação realizada com sucesso");
        }

    }

}
