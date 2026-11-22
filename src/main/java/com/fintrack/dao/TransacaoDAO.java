package com.fintrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.fintrack.enums.Categoria;
import com.fintrack.model.Transacao;
import com.fintrack.model.TransacaoMensal;

public class TransacaoDAO {

    private static final Logger LOG
            = Logger.getLogger("TransacaoDAO");

    private Connection conexao;

    public TransacaoDAO(
            Connection conexao
    ) {

        this.conexao = conexao;

    }

    public boolean inserir(
            Transacao t
    ) {

        String sql
                = "INSERT INTO transacoes "
                + "(descricao, valor, tipo, data, categoria, dia_pagamento) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps
                = conexao.prepareStatement(sql)) {

            ps.setString(1, t.getDescricao());
            ps.setDouble(2, t.getValor());
            ps.setString(3, t.getTipo());
            ps.setString(4, t.getData().toString());
            ps.setString(5, t.getCategoria().name());

            if (t instanceof TransacaoMensal) {

                TransacaoMensal mensal
                        = (TransacaoMensal) t;

                ps.setInt(6, mensal.getDiaPagamento());

            } else {

                ps.setNull(6, java.sql.Types.INTEGER);

            }

            ps.executeUpdate();

            Statement st
                    = conexao.createStatement();

            ResultSet rs
                    = st.executeQuery(
                            "SELECT last_insert_rowid()"
                    );

            if (rs.next()) {

                t.setId(rs.getInt(1));

            }

            rs.close();
            st.close();

            return true;

        } catch (SQLException e) {

            LOG.severe(
                    "Erro ao inserir transacao: "
                    + e.getMessage()
            );

            return false;

        }

    }

    public ArrayList<Transacao> listarTodos() {

        ArrayList<Transacao> lista
                = new ArrayList<>();

        String sql
                = "SELECT * FROM transacoes ORDER BY data DESC";

        try {

            PreparedStatement ps
                    = conexao.prepareStatement(sql);

            ResultSet rs
                    = ps.executeQuery();

            while (rs.next()) {

                String descricao
                        = rs.getString("descricao");

                double valor
                        = rs.getDouble("valor");

                String tipo
                        = rs.getString("tipo");

                Categoria categoria
                        = Categoria.valueOf(
                                rs.getString("categoria")
                        );

                int dia
                        = rs.getInt("dia_pagamento");

                Transacao t;

                // mensal
                if (dia > 0) {

                    t = new TransacaoMensal(
                            descricao,
                            valor,
                            tipo,
                            categoria,
                            dia
                    );

                } else {

                    t = new Transacao(
                            descricao,
                            valor,
                            tipo,
                            categoria
                    );

                }

                t.setId(rs.getInt("id"));

                t.setData(
                        LocalDate.parse(
                                rs.getString("data")
                        )
                );

                lista.add(t);

            }

            rs.close();
            ps.close();

        } catch (SQLException e) {

            LOG.severe(
                    "Erro ao listar transacoes: "
                    + e.getMessage()
            );

        }

        return lista;

    }

    public boolean atualizar(
            Transacao t
    ) {

        String sql
                = "UPDATE transacoes SET descricao = ?, valor = ?, tipo = ?, "
                + "data = ?, categoria = ?, dia_pagamento = ? WHERE id = ?";

        try {

            PreparedStatement ps
                    = conexao.prepareStatement(sql);

            ps.setString(1, t.getDescricao());
            ps.setDouble(2, t.getValor());
            ps.setString(3, t.getTipo());
            ps.setString(4, t.getData().toString());
            ps.setString(5, t.getCategoria().name());

            if (t instanceof TransacaoMensal) {

                ps.setInt(6, ((TransacaoMensal) t).getDiaPagamento());

            } else {

                ps.setNull(6, java.sql.Types.INTEGER);

            }

            ps.setInt(7, t.getId());

            int linhas
                    = ps.executeUpdate();

            ps.close();

            return linhas > 0;

        } catch (SQLException e) {

            LOG.severe(
                    "Erro ao atualizar transacao: "
                    + e.getMessage()
            );

            return false;

        }

    }

    public boolean remover(
            int id
    ) {

        String sql
                = "DELETE FROM transacoes WHERE id = ?";

        try {

            PreparedStatement ps
                    = conexao.prepareStatement(sql);

            ps.setInt(1, id);

            int linhas
                    = ps.executeUpdate();

            ps.close();

            return linhas > 0;

        } catch (SQLException e) {

            LOG.severe(
                    "Erro ao remover transacao: "
                    + e.getMessage()
            );

            return false;

        }

    }

    // Insere varias com commit/rollback
    public boolean inserirVarias(
            List<Transacao> lista
    ) {

        try {

            conexao.setAutoCommit(false);

            for (Transacao t : lista) {

                if (!inserir(t)) {

                    throw new SQLException(
                            "Falha ao inserir: "
                            + t.getDescricao()
                    );

                }

            }

            conexao.commit();

            return true;

        } catch (SQLException e) {

            LOG.severe(
                    "Erro, fazendo rollback: "
                    + e.getMessage()
            );

            try {

                conexao.rollback();

            } catch (SQLException ex) {

                LOG.severe(
                        "Erro no rollback: "
                        + ex.getMessage()
                );

            }

            return false;

        } finally {

            try {

                conexao.setAutoCommit(true);

            } catch (SQLException e) {

                LOG.warning(
                        "Erro ao voltar autoCommit: "
                        + e.getMessage()
                );

            }

        }

    }

}
