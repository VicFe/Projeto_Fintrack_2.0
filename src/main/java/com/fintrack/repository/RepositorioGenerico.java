package com.fintrack.repository;

import java.util.ArrayList;
import java.util.List;

// Repositorio generico
public class RepositorioGenerico<T> {

    private ArrayList<T> itens
            = new ArrayList<>();

    public void adicionar(
            T item
    ) {

        itens.add(item);

    }

    // ? extends T
    public void adicionarTodos(
            List<? extends T> lista
    ) {

        for (T item : lista) {

            itens.add(item);

        }

    }

    public boolean remover(
            T item
    ) {

        return itens.remove(item);

    }

    public ArrayList<T> listar() {

        return new ArrayList<>(itens);

    }

    // ? super T
    public void copiarPara(
            List<? super T> destino
    ) {

        for (T item : itens) {

            destino.add(item);

        }

    }

    public int tamanho() {

        return itens.size();

    }

    public void limpar() {

        itens.clear();

    }

    // ? qualquer tipo
    public static void imprimir(
            List<?> lista
    ) {

        for (Object item : lista) {

            System.out.println(item);

        }

    }

}
