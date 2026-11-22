package com.fintrack.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import com.fintrack.dao.TransacaoDAO;
import com.fintrack.enums.Categoria;
import com.fintrack.exceptions.ValorInvalidoException;
import com.fintrack.model.Transacao;
import com.fintrack.model.TransacaoMensal;
import com.fintrack.repository.RepositorioGenerico;
import com.fintrack.utils.Formatador;

public class FinTracker {

    private TransacaoDAO dao;

    private RepositorioGenerico<Transacao> repositorio
            = new RepositorioGenerico<>();

    public FinTracker(
            TransacaoDAO dao
    ) {

        this.dao = dao;

        carregarTransacoes();

    }

    public void carregarTransacoes() {

        repositorio.limpar();

        repositorio.adicionarTodos(
                dao.listarTodos()
        );

    }

    public boolean adicionarTransacao(
            Transacao transacao
    ) throws ValorInvalidoException {

        if (transacao.getValor() <= 0) {

            throw new ValorInvalidoException(
                    "Valor deve ser maior que zero."
            );

        }

        boolean salvou
                = dao.inserir(transacao);

        if (salvou) {

            repositorio.adicionar(transacao);

        }

        return salvou;

    }

    public boolean editarTransacao(
            Transacao transacao,
            String novaDescricao,
            double novoValor,
            String novoTipo,
            Categoria novaCategoria,
            LocalDate novaData,
            int novoDia
    ) throws ValorInvalidoException {

        if (novoValor <= 0) {

            throw new ValorInvalidoException(
                    "Valor deve ser maior que zero."
            );

        }

        transacao.setDescricao(novaDescricao);
        transacao.setValor(novoValor);
        transacao.setTipo(novoTipo);
        transacao.setCategoria(novaCategoria);
        transacao.setData(novaData);

        if (transacao instanceof TransacaoMensal) {

            ((TransacaoMensal) transacao).setDiaPagamento(novoDia);

        }

        boolean salvou
                = dao.atualizar(transacao);

        if (!salvou) {

            carregarTransacoes();

        }

        return salvou;

    }

    public boolean removerTransacao(
            Transacao transacao
    ) {

        boolean removeu
                = dao.remover(transacao.getId());

        if (removeu) {

            repositorio.remover(transacao);

        }

        return removeu;

    }

    public ArrayList<Transacao> listarTransacoes() {

        return repositorio.listar();

    }

    public ArrayList<Transacao> buscarTransacao(
            String busca
    ) {

        ArrayList<Transacao> resultado
                = new ArrayList<>();

        for (Transacao t : repositorio.listar()) {

            if (t.getDescricao()
                    .toLowerCase()
                    .contains(
                            busca.toLowerCase()
                    )) {

                resultado.add(t);

            }

        }

        return resultado;

    }

    public double calcularTotalReceitas() {

        double total = 0;

        for (Transacao t : repositorio.listar()) {

            if (t.getTipo().equalsIgnoreCase(
                    "RECEITA"
            )) {

                total += t.getValor();

            }

        }

        return total;

    }

    public double calcularTotalDespesas() {

        double total = 0;

        for (Transacao t : repositorio.listar()) {

            if (t.getTipo().equalsIgnoreCase(
                    "DESPESA"
            )) {

                total += t.getValor();

            }

        }

        return total;

    }

    public double calcularSaldoTotal() {

        return calcularTotalReceitas()
                - calcularTotalDespesas();

    }

    public int quantidade() {

        return repositorio.tamanho();

    }

    public String relatorioPorCategoria() {

        String texto = "";

        for (Categoria c : Categoria.values()) {

            double total = 0;

            for (Transacao t : repositorio.listar()) {

                if (t.getTipo().equalsIgnoreCase("DESPESA")
                        && t.getCategoria() == c) {

                    total += t.getValor();

                }

            }

            if (total > 0) {

                texto += c
                        + ": "
                        + Formatador.formatarMoeda(total)
                        + "\n";

            }

        }

        if (texto.isEmpty()) {

            texto = "Nenhuma despesa cadastrada.";

        }

        return texto;

    }

}
