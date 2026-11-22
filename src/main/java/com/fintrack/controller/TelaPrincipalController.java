package com.fintrack.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.fintrack.enums.Categoria;
import com.fintrack.model.Transacao;
import com.fintrack.utils.Formatador;

public class TelaPrincipalController {

    @FXML
    private TableView<Transacao> tabela;

    @FXML
    private TableColumn<Transacao, String> colData;

    @FXML
    private TableColumn<Transacao, String> colDescricao;

    @FXML
    private TableColumn<Transacao, String> colValor;

    @FXML
    private TableColumn<Transacao, String> colTipo;

    @FXML
    private TableColumn<Transacao, Categoria> colCategoria;

    @FXML
    private Label lblReceitas;

    @FXML
    private Label lblDespesas;

    @FXML
    private Label lblSaldo;

    @FXML
    private TextField txtBusca;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnLimpar;

    @FXML
    private Button btnNova;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnRemover;

    @FXML
    private Button btnRelatorio;

    private FinTracker finTracker;

    @FXML
    public void initialize() {

        colData.setCellValueFactory(
                new PropertyValueFactory<>("dataFormatada")
        );

        colDescricao.setCellValueFactory(
                new PropertyValueFactory<>("descricao")
        );

        colValor.setCellValueFactory(
                new PropertyValueFactory<>("valorFormatado")
        );

        colTipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo")
        );

        colCategoria.setCellValueFactory(
                new PropertyValueFactory<>("categoria")
        );

        btnNova.setOnAction(e -> abrirTelaTransacao(null));

        btnEditar.setOnAction(e -> editar());

        btnRemover.setOnAction(e -> remover());

        btnRelatorio.setOnAction(e -> abrirRelatorio());

        btnBuscar.setOnAction(e -> buscar());

        btnLimpar.setOnAction(e -> {

            txtBusca.setText("");
            atualizarTela();

        });

    }

    public void setFinTracker(
            FinTracker finTracker
    ) {

        this.finTracker = finTracker;

        atualizarTela();

    }

    private void atualizarTela() {

        mostrarNaTabela(
                finTracker.listarTransacoes()
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

    }

    private void mostrarNaTabela(
            ArrayList<Transacao> lista
    ) {

        tabela.setItems(
                FXCollections.observableArrayList(lista)
        );

    }

    private void buscar() {

        mostrarNaTabela(
                finTracker.buscarTransacao(
                        txtBusca.getText()
                )
        );

    }

    private void editar() {

        Transacao selecionada
                = tabela.getSelectionModel().getSelectedItem();

        if (selecionada == null) {

            mostrarAviso("Selecione uma transacao na tabela.");
            return;

        }

        abrirTelaTransacao(selecionada);

    }

    private void remover() {

        Transacao selecionada
                = tabela.getSelectionModel().getSelectedItem();

        if (selecionada == null) {

            mostrarAviso("Selecione uma transacao na tabela.");
            return;

        }

        Alert confirmacao
                = new Alert(Alert.AlertType.CONFIRMATION);

        confirmacao.setTitle("Remover");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText(
                "Deseja remover a transacao \""
                + selecionada.getDescricao()
                + "\"?"
        );

        Optional<ButtonType> resposta
                = confirmacao.showAndWait();

        if (resposta.isPresent()
                && resposta.get() == ButtonType.OK) {

            if (finTracker.removerTransacao(selecionada)) {

                atualizarTela();

            } else {

                mostrarAviso("Erro ao remover do banco.");

            }

        }

    }

    private void abrirTelaTransacao(
            Transacao transacao
    ) {

        try {

            FXMLLoader loader
                    = new FXMLLoader(
                            getClass().getResource(
                                    "/com/fintrack/view/TelaTransacao.fxml"
                            )
                    );

            Parent root = loader.load();

            TelaTransacaoController controller
                    = loader.getController();

            controller.setFinTracker(finTracker);
            controller.setTransacao(transacao);

            abrirJanela(root, "Transacao");

            atualizarTela();

        } catch (IOException e) {

            System.out.println(
                    "Erro ao abrir tela: "
                    + e.getMessage()
            );

        }

    }

    private void abrirRelatorio() {

        try {

            FXMLLoader loader
                    = new FXMLLoader(
                            getClass().getResource(
                                    "/com/fintrack/view/TelaRelatorio.fxml"
                            )
                    );

            Parent root = loader.load();

            TelaRelatorioController controller
                    = loader.getController();

            controller.carregarDados(finTracker);

            abrirJanela(root, "Relatorio");

        } catch (IOException e) {

            System.out.println(
                    "Erro ao abrir relatorio: "
                    + e.getMessage()
            );

        }

    }

    private void abrirJanela(
            Parent root,
            String titulo
    ) {

        Scene scene
                = new Scene(root);

        scene.getStylesheets().add(
                getClass().getResource(
                        "/com/fintrack/view/style.css"
                ).toExternalForm()
        );

        Stage stage
                = new Stage();

        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    }

    private void mostrarAviso(
            String mensagem
    ) {

        Alert alerta
                = new Alert(Alert.AlertType.WARNING);

        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();

    }

}
