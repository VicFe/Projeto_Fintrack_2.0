package com.fintrack.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import com.fintrack.utils.Formatador;

public class TelaRelatorioController {

    @FXML
    private Label lblQuantidade;

    @FXML
    private Label lblReceitas;

    @FXML
    private Label lblDespesas;

    @FXML
    private Label lblSaldo;

    @FXML
    private TextArea txtCategorias;

    @FXML
    private Button btnFechar;

    @FXML
    public void initialize() {

        btnFechar.setOnAction(e -> {

            Stage stage
                    = (Stage) btnFechar.getScene().getWindow();

            stage.close();

        });

    }

    public void carregarDados(
            FinTracker finTracker
    ) {

        lblQuantidade.setText(
                "Transacoes: " + finTracker.quantidade()
        );

        lblReceitas.setText(
                "Receitas: "
                + Formatador.formatarMoeda(finTracker.calcularTotalReceitas())
        );

        lblDespesas.setText(
                "Despesas: "
                + Formatador.formatarMoeda(finTracker.calcularTotalDespesas())
        );

        lblSaldo.setText(
                "Saldo: "
                + Formatador.formatarMoeda(finTracker.calcularSaldoTotal())
        );

        txtCategorias.setText(
                finTracker.relatorioPorCategoria()
        );

    }

}
