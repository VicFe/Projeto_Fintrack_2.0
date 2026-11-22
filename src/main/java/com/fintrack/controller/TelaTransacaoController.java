package com.fintrack.controller;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.fintrack.enums.Categoria;
import com.fintrack.exceptions.ValorInvalidoException;
import com.fintrack.model.Transacao;
import com.fintrack.model.TransacaoMensal;

public class TelaTransacaoController {

    @FXML
    private Label lblTitulo;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtValor;

    @FXML
    private ComboBox<String> cbTipo;

    @FXML
    private ComboBox<Categoria> cbCategoria;

    @FXML
    private DatePicker dpData;

    @FXML
    private CheckBox chkMensal;

    @FXML
    private TextField txtDia;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    private FinTracker finTracker;

    // null = nova transacao
    private Transacao transacao;

    @FXML
    public void initialize() {

        cbTipo.getItems().addAll(
                "RECEITA",
                "DESPESA"
        );

        cbCategoria.getItems().addAll(
                Categoria.values()
        );

        dpData.setValue(LocalDate.now());

        txtDia.setDisable(true);

        chkMensal.setOnAction(e -> {

            txtDia.setDisable(!chkMensal.isSelected());

        });

        btnSalvar.setOnAction(e -> salvar());

        btnCancelar.setOnAction(e -> fechar());

    }

    public void setFinTracker(
            FinTracker finTracker
    ) {

        this.finTracker = finTracker;

    }

    public void setTransacao(
            Transacao transacao
    ) {

        this.transacao = transacao;

        if (transacao == null) {

            return;

        }

        lblTitulo.setText("Editar Transacao");

        txtDescricao.setText(transacao.getDescricao());
        txtValor.setText(String.valueOf(transacao.getValor()));
        cbTipo.setValue(transacao.getTipo());
        cbCategoria.setValue(transacao.getCategoria());
        dpData.setValue(transacao.getData());

        chkMensal.setDisable(true);

        if (transacao instanceof TransacaoMensal) {

            chkMensal.setSelected(true);
            txtDia.setDisable(false);
            txtDia.setText(
                    String.valueOf(
                            ((TransacaoMensal) transacao).getDiaPagamento()
                    )
            );

        }

    }

    private void salvar() {

        if (txtDescricao.getText().isEmpty()
                || txtValor.getText().isEmpty()
                || cbTipo.getValue() == null
                || cbCategoria.getValue() == null
                || dpData.getValue() == null) {

            mostrarErro("Preencha todos os campos.");
            return;

        }

        double valor;

        try {

            valor = Double.parseDouble(
                    txtValor.getText().replace(",", ".")
            );

        } catch (NumberFormatException e) {

            mostrarErro("Valor invalido. Digite apenas numeros.");
            return;

        }

        int dia = 0;

        if (chkMensal.isSelected()) {

            try {

                dia = Integer.parseInt(txtDia.getText());

            } catch (NumberFormatException e) {

                mostrarErro("Dia de pagamento invalido.");
                return;

            }

            if (dia < 1 || dia > 31) {

                mostrarErro("O dia deve ser entre 1 e 31.");
                return;

            }

        }

        try {

            boolean salvou;

            if (transacao == null) {

                Transacao nova;

                if (chkMensal.isSelected()) {

                    nova = new TransacaoMensal(
                            txtDescricao.getText(),
                            valor,
                            cbTipo.getValue(),
                            cbCategoria.getValue(),
                            dia
                    );

                } else {

                    nova = new Transacao(
                            txtDescricao.getText(),
                            valor,
                            cbTipo.getValue(),
                            cbCategoria.getValue()
                    );

                }

                nova.setData(dpData.getValue());

                salvou = finTracker.adicionarTransacao(nova);

            } else {

                salvou = finTracker.editarTransacao(
                        transacao,
                        txtDescricao.getText(),
                        valor,
                        cbTipo.getValue(),
                        cbCategoria.getValue(),
                        dpData.getValue(),
                        dia
                );

            }

            if (salvou) {

                fechar();

            } else {

                mostrarErro("Erro ao salvar no banco de dados.");

            }

        } catch (ValorInvalidoException e) {

            mostrarErro(e.getMessage());

        }

    }

    private void fechar() {

        Stage stage
                = (Stage) btnSalvar.getScene().getWindow();

        stage.close();

    }

    private void mostrarErro(
            String mensagem
    ) {

        Alert alerta
                = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();

    }

}
