package com.fintrack.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Conexao {

    private static final Logger LOG
            = Logger.getLogger("Conexao");

    private static final String URL
            = "jdbc:sqlite:fintrack.db";

    public static Connection getConexao() {

        return getConexao(URL);

    }

    public static Connection getConexao(
            String url
    ) {

        try {

            return DriverManager.getConnection(
                    url
            );

        } catch (SQLException e) {

            LOG.severe(
                    "Erro ao conectar no banco: "
                    + e.getMessage()
            );

            return null;

        }

    }

    public static void criarTabela(
            Connection conexao
    ) {

        String sql
                = "CREATE TABLE IF NOT EXISTS transacoes ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " descricao VARCHAR(100) NOT NULL,"
                + " valor DECIMAL(10,2) NOT NULL,"
                + " tipo VARCHAR(10) NOT NULL,"
                + " data DATE NOT NULL,"
                + " categoria VARCHAR(20),"
                + " dia_pagamento INTEGER"
                + ")";

        try {

            Statement st
                    = conexao.createStatement();

            st.execute(sql);

            st.close();

        } catch (SQLException e) {

            LOG.severe(
                    "Erro ao criar tabela: "
                    + e.getMessage()
            );

        }

    }

    public static void fechar(
            Connection conexao
    ) {

        try {

            if (conexao != null) {

                conexao.close();

            }

        } catch (SQLException e) {

            LOG.warning(
                    "Erro ao fechar conexao: "
                    + e.getMessage()
            );

        }

    }

}
