package com.alura.alurabank.com.alura.alurabank.controller;

import com.alura.alurabank.dominio.ContaCorrente;
import com.alura.alurabank.dominio.Correntista;
import com.alura.alurabank.dominio.MovimentacaoDeConta;
import com.alura.alurabank.dominio.form.ContaCorrenteForm;
import com.alura.alurabank.dominio.form.CorrentistaForm;
import com.alura.alurabank.repositorio.RepositorioContasCorrente;
import com.googlecode.jmapper.JMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contas")
public class ContaCorrenteController {

    @Autowired
    private RepositorioContasCorrente repositorioContasCorrente;

    @Autowired
    private JMapper<ContaCorrente, ContaCorrenteForm> contaCorrenteMapper;

    @Autowired
    private Validator validator;
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
    public ResponseEntity criarNovaConta(@RequestBody CorrentistaForm correntistaForm) {
        Map<Path, String> violacoesMap = validar(correntistaForm);

        if (!violacoesMap.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesMap);
        }
        Correntista correntista = correntistaForm.toCorrentista();

        String banco = "333";
        String agencia = "4444";
        String numero = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
        ContaCorrente conta = new ContaCorrente(banco,agencia,numero, correntista);

        repositorioContasCorrente.salvar(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(conta);

    }

    private <T> Map<Path, String> validar(T form) {
        Set<ConstraintViolation<T>> violacoes = validator.validate(form);
        Map<Path, String> violacoesMap = violacoes.stream().collect(Collectors.toMap(violacao -> violacao.getPropertyPath(), violacao -> violacao.getMessage()));
        return violacoesMap;
    }

    @DeleteMapping
    public ResponseEntity fecharConta(@RequestBody ContaCorrenteForm contaForm){
        Map<Path, String> violacoesMap = validar(contaForm);

        if (!violacoesMap.isEmpty()) {
            return ResponseEntity.badRequest().body(violacoesMap);
        }

        ContaCorrente conta = contaCorrenteMapper.getDestination(contaForm);
        if (repositorioContasCorrente.buscar(conta).isEmpty()) {
            return ResponseEntity.badRequest().body("Conta não encontrada");
        }
        repositorioContasCorrente.fechar(conta);
        return ResponseEntity.ok("Conta fechada com sucesso");
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
