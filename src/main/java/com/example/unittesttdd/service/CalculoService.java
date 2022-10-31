package com.example.unittesttdd.service;

import com.example.unittesttdd.util.DateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CalculoService {

    private static final Double PRECO_FIXO_LIVRO = 5.0;
    private static final Double PRECO_MULTA_LIVRO = 0.4;
    private static final Double PERCENT_LIMITED = 0.6;

    private final DateUtil dateUtil;

    public CalculoService(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }

    public Double valorAluguel(LocalDateTime dataPrevista, LocalDateTime dataDevolucao, LocalDateTime dataEmprestimo){
        if (dateUtil.excedeuPrazo(dataPrevista, dataDevolucao)){
            System.out.println("devolução excedeu o prazo");
            Double valorMulta = ( PRECO_FIXO_LIVRO * dateUtil.diasEntreDatas(dataDevolucao, dataEmprestimo) ) + ( aplicarMulta( dataPrevista, dataDevolucao, dataEmprestimo ) );
            System.out.println("Valor com multa aplicada: " + valorMulta);
            return valorMulta;
        }
        System.out.println("devolução dentro do prazo");
        return aluguelSemMulta( dateUtil.diasEntreDatas( dataDevolucao, dataEmprestimo) );
    }

    public Double aluguelSemMulta(int diasEmprestados){
        return  PRECO_FIXO_LIVRO * diasEmprestados;
    }

    public Double aplicarMulta(LocalDateTime dataPrevista, LocalDateTime dataDevolucao, LocalDateTime dataEmprestimo){

        Double multa = PRECO_MULTA_LIVRO * dateUtil.diasEntreDatas(dataDevolucao, dataPrevista);
        Double limite = calcularLimite(dataEmprestimo, dataPrevista);


        System.out.println("Multa para o Período: " + PRECO_MULTA_LIVRO * dateUtil.diasEntreDatas(dataDevolucao, dataPrevista));

        if(multa > limite ){
            return limite;
        }

        return multa;
    }


    public double calcularLimite(LocalDateTime dataAluguel, LocalDateTime dataPrevista){
        /** limitada a 60% do valor do aluguel. */

        Double limite;
        int diasPrevistos = dateUtil.diasEntreDatas(dataPrevista, dataAluguel);
        Double valorPadraoDoAluguel = aluguelSemMulta(diasPrevistos);

        limite = valorPadraoDoAluguel * PERCENT_LIMITED;
        return limite;
    }
}
