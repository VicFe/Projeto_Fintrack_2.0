package com.fintrack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.fintrack.enums.Categoria;

public class TransacaoTest {

    @Test
    public void testCriarTransacao() {

        Transacao t = new Transacao(
                "Salario",
                3000.0,
                "RECEITA",
                Categoria.SALARIO
        );

        assertEquals("Salario", t.getDescricao());
        assertEquals(3000.0, t.getValor());
        assertEquals("RECEITA", t.getTipo());
        assertEquals(Categoria.SALARIO, t.getCategoria());

        assertEquals(LocalDate.now(), t.getData());

    }

    @Test
    public void testDataFormatada() {

        Transacao t = new Transacao(
                "Mercado",
                200.0,
                "DESPESA",
                Categoria.ALIMENTACAO
        );

        t.setData(LocalDate.of(2026, 11, 5));

        assertEquals("05/11/2026", t.getDataFormatada());

    }

    @Test
    public void testTransacaoMensal() {

        TransacaoMensal aluguel = new TransacaoMensal(
                "Aluguel",
                1200.0,
                "DESPESA",
                Categoria.OUTROS,
                10
        );

        assertEquals(10, aluguel.getDiaPagamento());
        assertEquals("Aluguel", aluguel.getDescricao());

    }

}
