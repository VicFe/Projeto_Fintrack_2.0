package com.fintrack.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fintrack.dao.Conexao;
import com.fintrack.dao.TransacaoDAO;
import com.fintrack.enums.Categoria;
import com.fintrack.exceptions.ValorInvalidoException;
import com.fintrack.model.Transacao;

public class FinTrackerTest {

    private Connection conexao;
    private FinTracker finTracker;

    @BeforeEach
    public void setUp() throws ValorInvalidoException {

        conexao = Conexao.getConexao("jdbc:sqlite::memory:");

        Conexao.criarTabela(conexao);

        finTracker = new FinTracker(new TransacaoDAO(conexao));

        finTracker.adicionarTransacao(
                new Transacao("Salario", 3000.0, "RECEITA", Categoria.SALARIO)
        );

        finTracker.adicionarTransacao(
                new Transacao("Mercado", 450.0, "DESPESA", Categoria.ALIMENTACAO)
        );

        finTracker.adicionarTransacao(
                new Transacao("Onibus", 50.0, "DESPESA", Categoria.TRANSPORTE)
        );

    }

    @AfterEach
    public void tearDown() {

        Conexao.fechar(conexao);

    }

    @Test
    public void testCalcularSaldo() {

        assertEquals(3000.0, finTracker.calcularTotalReceitas());
        assertEquals(500.0, finTracker.calcularTotalDespesas());
        assertEquals(2500.0, finTracker.calcularSaldoTotal());

    }

    @Test
    public void testValorNegativo() {

        Transacao errada = new Transacao("Teste", -10.0, "DESPESA", Categoria.OUTROS);

        assertThrows(ValorInvalidoException.class, () -> {

            finTracker.adicionarTransacao(errada);

        });

        assertEquals(3, finTracker.quantidade());

    }

    @Test
    public void testRemoverAtualizaSaldo() {

        Transacao mercado = finTracker.buscarTransacao("mercado").get(0);

        finTracker.removerTransacao(mercado);

        assertEquals(2950.0, finTracker.calcularSaldoTotal());
        assertEquals(2, finTracker.quantidade());

    }

    @Test
    public void testEditar() throws ValorInvalidoException {

        Transacao onibus = finTracker.buscarTransacao("onibus").get(0);

        finTracker.editarTransacao(
                onibus,
                "Uber",
                100.0,
                "DESPESA",
                Categoria.TRANSPORTE,
                onibus.getData(),
                0
        );

        assertEquals(2450.0, finTracker.calcularSaldoTotal());

        finTracker.carregarTransacoes();

        assertEquals(1, finTracker.buscarTransacao("uber").size());

    }

}
