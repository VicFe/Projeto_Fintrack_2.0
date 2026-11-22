package com.fintrack.model;

import com.fintrack.enums.Categoria;

public class TransacaoMensal
        extends Transacao {

    private int diaPagamento;

    public TransacaoMensal(
            String descricao,
            double valor,
            String tipo,
            Categoria categoria,
            int diaPagamento
    ) {

        super(
                descricao,
                valor,
                tipo,
                categoria
        );

        this.diaPagamento
                = diaPagamento;

    }

    public int getDiaPagamento() {

        return diaPagamento;

    }

    public void setDiaPagamento(
            int diaPagamento
    ) {

        this.diaPagamento
                = diaPagamento;

    }

}
