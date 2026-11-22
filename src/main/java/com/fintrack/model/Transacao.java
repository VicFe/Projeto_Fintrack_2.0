package com.fintrack.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.fintrack.enums.Categoria;
import com.fintrack.utils.Formatador;

public class Transacao {

    // Atributos
    private int id;
    private String descricao;
    private double valor;
    private String tipo;
    private LocalDate data;
    private Categoria categoria;

    // Construtor
    public Transacao(
            String descricao,
            double valor,
            String tipo,
            Categoria categoria
    ) {

        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.categoria = categoria;

        this.data = LocalDate.now();
    }

    // Getters
    public int getId() {

        return id;

    }

    public String getDescricao() {

        return descricao;

    }

    public double getValor() {

        return valor;

    }

    public String getTipo() {

        return tipo;

    }

    public Categoria getCategoria() {

        return categoria;

    }

    public LocalDate getData() {

        return data;

    }

    // Setters
    public void setId(
            int id
    ) {

        this.id = id;

    }

    public void setDescricao(
            String descricao
    ) {

        this.descricao = descricao;

    }

    public void setValor(
            double valor
    ) {

        this.valor = valor;

    }

    public void setTipo(
            String tipo
    ) {

        this.tipo = tipo;

    }

    public void setCategoria(
            Categoria categoria
    ) {

        this.categoria = categoria;

    }

    public void setData(
            LocalDate data
    ) {

        this.data = data;

    }

    public String getDataFormatada() {

        DateTimeFormatter formatador
                = DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );

        return data.format(
                formatador
        );

    }

    public String getValorFormatado() {

        return Formatador.formatarMoeda(
                valor
        );

    }

}
