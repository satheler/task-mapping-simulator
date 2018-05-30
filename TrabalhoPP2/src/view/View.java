/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import gerais.Aplicativo;
import gerais.CanalCom;
import gerais.Celula;
import gerais.LeitorArquivo;
import gerais.MPSoC;
import heuristicas.FF;
import heuristicas.Heuristica;
import heuristicas.NN;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Lucas
 */
public class View extends Application {

    //<editor-fold defaultstate="collapsed" desc="Declaração Design">   
    Stage stage;
    Scene scene;
    Button buttonStart;
    Button buttonImport;
    Button buttonReset;
    Button buttonRunAll;
    ComboBox<String> comboBoxHeuristicas;
    Pane pane;
    Pane paneLegenda;
    Pane paneSettings;

    ScrollPane scrollCenter;
    ScrollPane scrollLegenda;
    TextField textFieldLinhas;
    TextField textFieldColunas;

    Label labelTask;
    Label labelTaskTotal;
    Label labelAplicativos;
    Label labelAplicativosTotal;
    Label labelGeral;

    VBox legendaBox;

    int windowWidth = 800;
    int windowHeight = 600;
    int hGap = 120;
    int vGap = 120;
    int width = 60;
    int height = 60;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Declaração Variavéis">
    ArrayList<Rectangle> list;
    ArrayList<Aplicativo> listaAplicativos;
    int contadorTasksTotal = 0;
    int contadorTasks = 0;
    int contadorApp = 0;
    int totalTasks = 0;
    int colunas = 6;
    int linhas = 6;
    List<Rectangle> listaRectangulos;
    List<Rectangle> listaConexoes;
    List<Color> listaCores;
    MPSoC mpsoc;
    Heuristica heuristica;
    long[] tempoExecucao;
    //</editor-fold>

    @Override
    public void start(Stage stage) {

        this.stage = stage;
        listaRectangulos = new ArrayList();
        listaConexoes = new ArrayList();
        listaCores = new ArrayList();
        listaCores.add(Color.BISQUE);
        listaCores.add(Color.CADETBLUE);
        listaCores.add(Color.DARKSALMON);
        listaCores.add(Color.KHAKI);
        listaCores.add(Color.ROYALBLUE);

        definirLayout();
        eventHandlers();
        buttonStart.setDisable(true);
        buttonReset.setDisable(true);
        buttonRunAll.setDisable(true);

    }

    private void eventHandlers() {

        if (buttonStart != null) {
            buttonStart.setOnAction(event -> {
                buttonStartClick();
            });
        }

        if (buttonImport != null) {
            buttonImport.setOnAction(event -> {
                buttonImportClick();
            });
        }

        if (buttonReset != null) {
            buttonReset.setOnAction(event -> {
                buttonResetClick();
            });
        }

        comboBoxHeuristicas.setOnAction(event -> {
            heuristicaChange();
        });

        if (buttonRunAll != null) {
            buttonRunAll.setOnAction(event -> {
                buttonRunAllClick();
            });
        }

    }

    //<editor-fold defaultstate="collapsed" desc="Implementacao Ações botões">
    private void buttonStartClick() {

        setLinhasColunas();

        textFieldColunas.setDisable(true);
        textFieldLinhas.setDisable(true);

        if (mpsoc == null) {
            criarMPSoC();
        }

        if (contadorTasks == 0 && contadorApp == 0) {
            switch (comboBoxHeuristicas.getValue()) {
                case "FF":
                    heuristica = new FF(mpsoc, listaAplicativos);
                    break;
                case "NN":
                    heuristica = new NN(mpsoc, listaAplicativos);
                    break;
            }
        }

        long inicio = System.nanoTime();

        heuristica.executar();

        tempoExecucao[contadorTasksTotal] = System.nanoTime() - inicio;

        contadorTasksTotal++;
        contadorTasks++;
        labelTask.setText(String.valueOf(contadorTasksTotal));
        contarApp();
        AtualizarLabel();

        if (contadorTasksTotal == totalTasks) {
            buttonStart.setDisable(true);
            buttonRunAll.setDisable(true);
            gerarDadosAnalise();
        }

    }

    private void buttonRunAllClick() {

        for (int i = contadorTasksTotal; i < totalTasks; i++) {
            buttonStartClick();
        }

    }

    private void buttonImportClick() {

        listaAplicativos = LeitorArquivo.montarLista(LeitorArquivo.carregarArquivo());

        if (listaAplicativos != null) {
            buttonResetClick();
            buttonStart.setDisable(false);
            buttonReset.setDisable(false);
            buttonRunAll.setDisable(false);
        }

    }

    private void buttonResetClick() {

        contadorTasksTotal = 0;
        totalTasks = 0;
        resetarContadores();
        resetarLegenda();
        calcularAplicativos();
        AtualizarLabel();

        mpsoc = null;
        pane.getChildren().clear();

        textFieldColunas.setDisable(false);
        textFieldLinhas.setDisable(false);

        if (listaAplicativos != null) {
            buttonStart.setDisable(false);
            buttonRunAll.setDisable(false);
        }

    }

    private void heuristicaChange() {

        if (mpsoc != null) {
            buttonResetClick();
        }

    }

    //</editor-fold>
    public static void main(String[] args) {
        launch(args);
    }

    private void definirLayout() {

        //<editor-fold defaultstate="collapsed" desc="Principal">
        BorderPane borderPane = new BorderPane();
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Menu Lateral">
        scrollLegenda = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15, 12, 15, 12));
        vBox.setSpacing(10);
        vBox.setStyle("-fx-background-color: #336699;");
        vBox.setMinSize(150, 500);

        paneLegenda = new Pane();
        paneLegenda.setMinSize(150, 250);
        legendaBox = new VBox();
        Label legendaTitle = new Label("Legenda");
        legendaBox.getChildren().add(legendaTitle);
        paneLegenda.getChildren().add(legendaBox);

        VBox vBoxSettings = new VBox();
        paneSettings = new Pane();
        paneSettings.setMinSize(150, 250);
        paneSettings.setStyle("-fx-border-color:black");
        Label settingsTitle = new Label("Settings");
        Label settingsLinhas = new Label("Linhas");
        Label settingsColunas = new Label("Colunas");
        Label settingsHeuristica = new Label("Heurística");
        settingsTitle.setStyle("-fx-text-fill: #FFF;-fx-font-weight: bold;");
        settingsLinhas.setStyle("-fx-text-fill: #FFF;-fx-font-weight: bold;");
        settingsColunas.setStyle("-fx-text-fill: #FFF;-fx-font-weight: bold;");
        settingsHeuristica.setStyle("-fx-text-fill: #FFF;-fx-font-weight: bold;");
        vBoxSettings.setPadding(new Insets(10, 10, 10, 10));
        vBoxSettings.getChildren().add(settingsTitle);
        textFieldLinhas = new TextField();
        textFieldColunas = new TextField();
        vBoxSettings.getChildren().add(settingsLinhas);
        vBoxSettings.getChildren().add(textFieldLinhas);
        vBoxSettings.getChildren().add(settingsColunas);
        vBoxSettings.getChildren().add(textFieldColunas);
        vBoxSettings.getChildren().add(settingsHeuristica);

        comboBoxHeuristicas = new ComboBox();
        comboBoxHeuristicas.getItems().add("FF");
        comboBoxHeuristicas.getItems().add("NN");
        comboBoxHeuristicas.getItems().add("H2");
        comboBoxHeuristicas.getSelectionModel().selectFirst();
        vBoxSettings.getChildren().add(comboBoxHeuristicas);
        paneSettings.getChildren().add(vBoxSettings);

        scrollLegenda.setContent(paneLegenda);

        scrollLegenda.setMinHeight(250);
        scrollLegenda.setMinWidth(150);
        scrollLegenda.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollLegenda.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        vBox.getChildren().add(scrollLegenda);
        vBox.getChildren().add(paneSettings);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Menu Bottom">
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");

        HBox hBox1 = new HBox();
        hBox1.setPadding(new Insets(15, 12, 15, 12));
        hBox1.setSpacing(10);
        hBox1.setStyle("-fx-background-color: #336699;");

        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(15, 12, 15, 12));
        hBox2.setSpacing(10);
        hBox2.setStyle("-fx-background-color: #336699;");

        labelAplicativos = new Label("0");
        labelAplicativosTotal = new Label("0");
        labelTask = new Label("0");
        labelTaskTotal = new Label("0");

        labelGeral = new Label("Nenhum Arquivo Carregado");
        labelGeral.setStyle("-fx-text-fill: #FFF;-fx-font-weight: bold;");

        hBox1.setAlignment(Pos.BASELINE_CENTER);
        hBox1.getChildren().add(labelGeral);
        hBox1.setMinWidth(300);

        buttonImport = new Button("Importar");
        buttonStart = new Button("Start");
        buttonReset = new Button("Reset");
        buttonRunAll = new Button("Run All");

        hBox2.getChildren().add(buttonReset);
        hBox2.setAlignment(Pos.BASELINE_LEFT);

        hBox.setMinSize(windowWidth, 0);

        Pane espacamento = new Pane();
        espacamento.setMinSize(20, 0);
        HBox.setHgrow(espacamento, Priority.ALWAYS);

        hBox.getChildren().add(hBox2);
        hBox.getChildren().add(espacamento);
        hBox.getChildren().add(hBox1);
        hBox.getChildren().add(buttonImport);
        hBox.getChildren().add(buttonStart);
        hBox.getChildren().add(buttonRunAll);
        hBox.setAlignment(Pos.BASELINE_RIGHT);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Centro">
        pane = new Pane();
        pane.setMinHeight(500);
        pane.setMinWidth(650);

        scrollCenter = new ScrollPane();
        scrollCenter.setContent(pane);
        scrollCenter.setPannable(true);
        scrollCenter.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollCenter.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //</editor-fold>

        borderPane.setRight(vBox);
        borderPane.setBottom(hBox);
        borderPane.setCenter(scrollCenter);

        scene = new Scene(borderPane);
        stage.setMinHeight(windowHeight);
        stage.setMinWidth(windowWidth);
        stage.setScene(scene);
        stage.setTitle("MPSoc");
        stage.show();

    }

    private void AtualizarLabel() {

        labelGeral.setText("Aplicativos: " + labelAplicativos.getText() + "/" + labelAplicativosTotal.getText() + " Tasks: " + labelTask.getText() + "/" + labelTaskTotal.getText());

    }

    private void calcularAplicativos() {

        labelAplicativosTotal.setText(String.valueOf(listaAplicativos.size()));

        int contCor = 0;

        for (Aplicativo app : listaAplicativos) {
            totalTasks += app.getTarefas().size();
            app.setCor(listaCores.get(contCor));
            criarLegenda(app.getNome(), listaCores.get(contCor));
            contCor++;
        }

        labelTaskTotal.setText(String.valueOf(totalTasks));

        tempoExecucao = new long[totalTasks];

    }

    private void criarLegenda(String nome, Color cor) {

        HBox legendaItem = new HBox();
        Label labelNome = new Label(nome);

        Rectangle quadradoCor = new Rectangle(10, 10);
        quadradoCor.setFill(cor);

        legendaItem.setPadding(new Insets(5, 5, 5, 5));
        legendaItem.getChildren().add(quadradoCor);
        legendaItem.getChildren().add(labelNome);

        legendaBox.getChildren().add(legendaItem);

    }

    private void contarApp() {

        if (listaAplicativos.get(contadorApp).getTarefas().size() == contadorTasks) {
            contadorApp++;
            contadorTasks = 0;
        }

        labelAplicativos.setText(String.valueOf(contadorApp));

    }

    private void resetarLegenda() {

        for (int i = legendaBox.getChildren().size() - 1; i > 0; i--) {
            legendaBox.getChildren().remove(i);
        }

    }

    private void resetarContadores() {
        contadorApp = 0;
        contadorTasks = 0;
        labelAplicativos.setText("0");
        labelTask.setText("0");

    }

    private void criarMPSoC() {

        mpsoc = new MPSoC(linhas, colunas);

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {

                mpsoc.getCelulas()[i][j].setImagem(new Rectangle(j * hGap + width, i * vGap + height, width, height));
                pane.getChildren().add(mpsoc.getCelulas()[i][j].getImagem());
                mpsoc.getCelulas()[i][j].setLabel(new Label());
                pane.getChildren().add(mpsoc.getCelulas()[i][j].getLabel());
                mpsoc.getCelulas()[i][j].getLabel().setLayoutX((j * hGap + width) + width / 2 - 10);
                mpsoc.getCelulas()[i][j].getLabel().setLayoutY((i * vGap + height) + height / 2 - 10);

                //Celulas que só possuem 1 na frente Horizontal
                if (j + 1 < colunas) {
                    CanalCom canal = new CanalCom(i, j, i, j + 1);
                    Rectangle canalRectangle = new Rectangle((j * hGap + width) + width, (i * vGap + (height / 2 - 5)) + height, width, 10);
                    Label canalLabel = new Label();
                    canalLabel.setLayoutX((j * hGap + width) + width);
                    canalLabel.setLayoutY((i * vGap + (height / 2 - 5)) + height - 20);
                    canal.setImagem(canalRectangle);
                    canal.setLabel(canalLabel);

                    pane.getChildren().add(canalRectangle);
                    pane.getChildren().add(canalLabel);
                    mpsoc.getCelulas()[i][j].addCanal(canal);
                }

                //pegar o canal que ja existe e colocar como canal de comunicação contrário Horizontal
                if (j - 1 >= 0) {
                    for (CanalCom canal : mpsoc.getCelulas()[i][j - 1].getListaCanais()) {
                        if (canal.equals(new CanalCom(i, j - 1, i, j))) {
                            mpsoc.getCelulas()[i][j].addCanal(canal);
                        }
                    }
                }

                //Celulas que só possuem 1 na frente Vertical
                if (i + 1 < linhas) {
                    CanalCom canal = new CanalCom(i, j, i + 1, j);
                    Rectangle canalRectangle = new Rectangle((j * hGap + (width / 2 - 5)) + width, (i * vGap + height) + height, 10, height);
                    Label canalLabel = new Label();
                    canalLabel.setLayoutX((j * hGap + (width / 2 - 5)) + width);
                    canalLabel.setLayoutY((i * vGap + height) + height + 20);
                    canalLabel.setRotate(-90);
                    canal.setImagem(canalRectangle);
                    canal.setLabel(canalLabel);

                    pane.getChildren().add(canalRectangle);
                    pane.getChildren().add(canalLabel);

                    mpsoc.getCelulas()[i][j].addCanal(canal);
                }

                //pegar o canal que ja existe e colocar como canal de comunicação contrário Vertical
                if (i - 1 >= 0) {
                    for (CanalCom canal : mpsoc.getCelulas()[i - 1][j].getListaCanais()) {
                        if (canal.equals(new CanalCom(i - 1, j, i, j))) {
                            mpsoc.getCelulas()[i][j].addCanal(canal);
                        }
                    }
                }

            }
        }

    }

    private void setLinhasColunas() {
        if (!textFieldLinhas.getText().isEmpty() && mpsoc == null) {
            linhas = Integer.valueOf(textFieldLinhas.getText());
        }

        if (!textFieldColunas.getText().isEmpty() && mpsoc == null) {
            colunas = Integer.valueOf(textFieldColunas.getText());
        }

        if (totalTasks > (linhas * colunas) && mpsoc == null) {

            int recomendado = (int) Math.ceil(Math.sqrt(totalTasks));

            Alert dialogBox = new Alert(Alert.AlertType.INFORMATION, "O tamanho do MPSoC é insuficiente para essa quantidade de tarefas,"
                    + " o tamanho foi automaticamente alterado para: " + String.valueOf(recomendado) + "x" + String.valueOf(recomendado));
            dialogBox.show();

            colunas = recomendado;
            linhas = recomendado;

        }

        if (textFieldLinhas.getText().isEmpty() || textFieldColunas.getText().isEmpty()) {
            textFieldLinhas.setText(String.valueOf(linhas));
            textFieldColunas.setText(String.valueOf(colunas));
        }

    }

    private void gerarDadosAnalise() {

        int contadorVerde = 0;
        int contadorAmarelo = 0;
        int contadorVermelho = 0;
        int totalCanais = 0;

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {

                for (CanalCom canal : mpsoc.getCelulas()[i][j].getListaCanais()) {

                    if (canal.getCargaIda() > 90 || canal.getCargaVolta() > 90) {
                        contadorVermelho++;
                    } else {
                        if (canal.getCargaIda() >= 50 || canal.getCargaVolta() >= 50) {
                            contadorAmarelo++;
                        } else {
                            contadorVerde++;
                        }
                    }

                    totalCanais++;

                }

            }
        }

        System.out.println(contadorVerde + " " + contadorAmarelo + " " + contadorVermelho + "\n");

        long tempoTotal = 0;

        for (int i = 0; i < tempoExecucao.length; i++) {

            tempoTotal += tempoExecucao[i];
        }

        long tempoMedio = tempoTotal / (long) tempoExecucao.length;

        System.out.println(tempoMedio+"\n");

    }

}
