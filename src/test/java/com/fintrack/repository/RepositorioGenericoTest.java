package com.fintrack.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fintrack.enums.Categoria;
import com.fintrack.model.Transacao;
import com.fintrack.model.TransacaoMensal;

public class RepositorioGenericoTest {

    private RepositorioGenerico<Transacao> repositorio;

    @BeforeEach
    public void setUp() {

        repositorio = new RepositorioGenerico<>();

        repositorio.adicionar(
                new Transacao("Salario", 3000.0, "RECEITA", Categoria.SALARIO)
        );

        repositorio.adicionar(
                new Transacao("Mercado", 400.0, "DESPESA", Categoria.ALIMENTACAO)
        );

    }

    @Test
    public void testAdicionarEListar() {

        assertEquals(2, repositorio.tamanho());
        assertEquals("Salario", repositorio.listar().get(0).getDescricao());

    }

    @Test
    public void testRemover() {

        Transacao mercado = repositorio.listar().get(1);

        assertTrue(repositorio.remover(mercado));
        assertEquals(1, repositorio.tamanho());

    }

    @Test
    public void testAdicionarListaDeSubclasse() {

        // ? extends T
        List<TransacaoMensal> mensais = new ArrayList<>();

        mensais.add(new TransacaoMensal("Aluguel", 1200.0, "DESPESA", Categoria.OUTROS, 10));
        mensais.add(new TransacaoMensal("Internet", 100.0, "DESPESA", Categoria.OUTROS, 15));

        repositorio.adicionarTodos(mensais);

        assertEquals(4, repositorio.tamanho());

    }

    @Test
    public void testCopiarParaListaDeObject() {

        // ? super T
        List<Object> destino = new ArrayList<>();

        repositorio.copiarPara(destino);

        assertEquals(2, destino.size());

    }

}
