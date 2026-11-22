package com.fintrack.app;

import java.sql.Connection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.fintrack.controller.FinTracker;
import com.fintrack.controller.TelaPrincipalController;
import com.fintrack.dao.Conexao;
import com.fintrack.dao.TransacaoDAO;

public class FinApp extends Application {

    private Connection conexao;

    @Override
    public void start(
            Stage stage
    ) throws Exception {

        conexao = Conexao.getConexao();

        Conexao.criarTabela(conexao);

        FinTracker finTracker
                = new FinTracker(
                        new TransacaoDAO(conexao)
                );

        FXMLLoader loader
                = new FXMLLoader(
                        getClass().getResource(
                                "/com/fintrack/view/TelaPrincipal.fxml"
                        )
                );

        Parent root = loader.load();

        TelaPrincipalController controller
                = loader.getController();

        controller.setFinTracker(finTracker);

        Scene scene
                = new Scene(root);

        scene.getStylesheets().add(
                getClass().getResource(
                        "/com/fintrack/view/style.css"
                ).toExternalForm()
        );

        stage.setTitle("FinTrack - Financas Pessoais");
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void stop() {

        Conexao.fechar(conexao);

    }

    public static void main(
            String[] args
    ) {

        launch(args);

    }

}
