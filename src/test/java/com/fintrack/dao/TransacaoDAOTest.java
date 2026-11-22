package com.fintrack.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fintrack.enums.Categoria;
import com.fintrack.model.Transacao;
import com.fintrack.model.TransacaoMensal;

// SQLite em memoria
public class TransacaoDAOTest {

    private Connection conexao;
    private TransacaoDAO dao;

    @BeforeEach
    public void setUp() {

        conexao = Conexao.getConexao("jdbc:sqlite::memory:");

        Conexao.criarTabela(conexao);

        dao = new TransacaoDAO(conexao);

    }

    @AfterEach
    public void tearDown() {

        Conexao.fechar(conexao);

    }

    @Test
    public void testInserir() {

        Transacao t = new Transacao("Salario", 3000.0, "RECEITA", Categoria.SALARIO);

        assertTrue(dao.inserir(t));

        assertTrue(t.getId() > 0);

        assertEquals(1, dao.listarTodos().size());

    }

    @Test
    public void testListar() {

        dao.inserir(new Transacao("Salario", 3000.0, "RECEITA", Categoria.SALARIO));
        dao.inserir(new TransacaoMensal("Aluguel", 1200.0, "DESPESA", Categoria.OUTROS, 10));

        List<Transacao> lista = dao.listarTodos();

        assertEquals(2, lista.size());

    }

    @Test
    public void testTransacaoMensalVoltaDoBanco() {

        dao.inserir(new TransacaoMensal("Aluguel", 1200.0, "DESPESA", Categoria.OUTROS, 10));

        Transacao lida = dao.listarTodos().get(0);

        assertTrue(lida instanceof TransacaoMensal);
        assertEquals(10, ((TransacaoMensal) lida).getDiaPagamento());

    }

    @Test
    public void testAtualizar() {

        Transacao t = new Transacao("Mercado", 200.0, "DESPESA", Categoria.ALIMENTACAO);

        dao.inserir(t);

        t.setDescricao("Feira");
        t.setValor(80.0);

        assertTrue(dao.atualizar(t));

        Transacao lida = dao.listarTodos().get(0);

        assertEquals("Feira", lida.getDescricao());
        assertEquals(80.0, lida.getValor());

    }

    @Test
    public void testRemover() {

        Transacao t = new Transacao("Mercado", 200.0, "DESPESA", Categoria.ALIMENTACAO);

        dao.inserir(t);

        assertTrue(dao.remover(t.getId()));
        assertEquals(0, dao.listarTodos().size());

        assertFalse(dao.remover(t.getId()));

    }

    @Test
    public void testRollback() {

        List<Transacao> lista = new ArrayList<>();

        lista.add(new Transacao("Salario", 3000.0, "RECEITA", Categoria.SALARIO));

        // descricao null da erro no banco
        lista.add(new Transacao(null, 50.0, "DESPESA", Categoria.OUTROS));

        assertFalse(dao.inserirVarias(lista));

        assertEquals(0, dao.listarTodos().size());

    }

}
