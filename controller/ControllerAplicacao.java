/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 24/08/2024
* Ultima alteracao.: 08/09/2024
* Nome.............: Principal.java
* Funcao...........: Classe para o controle das acoes na aplicacao
*************************************************************** */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.AplicacaoTransmissora;
import model.CamadaFisicaTransmissora;
import util.DataSingleton;
import java.util.List; 
import java.util.ArrayList; 

public class ControllerAplicacao implements Initializable {

    private Parent root;
    private Scene scene;
    private Stage stage;
    private AnimationTimer timer; // Referência ao AnimationTimer

    @FXML
    private TextField entradaMensagem;
    @FXML
    private Button botaoTeste;
    @FXML
    private ImageView botaoEnviar;
    private static String mensagemRecebida;
    private static String mensagemTela;

    private static List<ImageView> ondasImagens = new ArrayList<>(); // Lista para armazenar os ImageViews das ondas

    @FXML
    private AnchorPane painelDeOndas;

    @FXML
    private Label labelTela;

    static DataSingleton data = DataSingleton.getInstance(); // conecta as telas

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       

        botaoEnviar.setOpacity(0); // Define a opacidade inicial do botao
        entradaMensagem.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                botaoEnviar.setOpacity(0); // Oculta o botao quando o campo esta vazio
            } else {
                botaoEnviar.setOpacity(1); // Exibe o botao quando ha texto
            }
        });

       
    }

        /* ***************************************************************
  * Metodo: enviarMensagem
  * Funcao: receber a mensagem do textField , armazenar e chamar a AplicacaoTransmissora
  * Parametros: event
  * Retorno: void
  *************************************************************** */
    @FXML
    public void enviarMensagem(MouseEvent event) {
        mensagemRecebida = entradaMensagem.getText();
        AplicacaoTransmissora.aplicacaoTransmissora();
        labelTela.setText(mensagemTela);

         startWaveAnimation(); // Inicia a animacao das ondas
    }
      

        /* ***************************************************************
  * Metodo: getMensagem
  * Funcao: chamar a mensagemRecebida
  * Parametros: void
  * Retorno: String
  *************************************************************** */
    public static String getMensagem() {
        return mensagemRecebida;
    }

        /* ***************************************************************
  * Metodo: setLabel
  * Funcao: Setar na aplicacao a mensagem enviada apos toda codificacao
  * Parametros: String mensagem
  * Retorno: void
  *************************************************************** */
    public static void setLabel(String mensagem) {
        mensagemTela = mensagem;
    }


         /* ***************************************************************
  * Metodo: criarOnda
  * Funcao: criar as ondas de acordo com o bit lido
  * Parametros: inteiro i , que sera lido indice por indice do vetor de bits 
  * Retorno: void
  *************************************************************** */
    private void criarOnda(int i) {
        String caminhoImagem = "";
        int posicaoY = 0;
        int larguraImagem = 45;
        boolean estadoAtual = true; // Supondo que o estado inicial seja "true" (alto-baixo)
        boolean estadoAnterior = (i == 0) ? false : (CamadaFisicaTransmissora.formaDeOnda.charAt(i - 1) == '1');

        switch (data.getOpcao()) {
            case 0: // Caso Binario
                caminhoImagem = "image/formaDeOndaBinaria.png";
                posicaoY = (CamadaFisicaTransmissora.formaDeOnda.charAt(i) == '1') ? 524 : 561;
                larguraImagem = 90;
                break;

            case 1: // Caso Manchester
                if (CamadaFisicaTransmissora.formaDeOnda.charAt(i) == '1') {
                    caminhoImagem = "image/ondaAltoBaixo.png";
                    posicaoY = 561;
                } else {
                    caminhoImagem = "image/ondaBaixoAlto.png";
                    posicaoY = 561;
                }
                larguraImagem = 45;
                break;

            case 2: // Caso Manchester Diferencial
                if (CamadaFisicaTransmissora.formaDeOnda.charAt(i) == '1') {
                    estadoAtual = !estadoAnterior;
                } else {
                    estadoAtual = estadoAnterior;
                }

                if (estadoAtual) {
                    caminhoImagem = "image/ondaAltoBaixo.png";
                } else {
                    caminhoImagem = "image/ondaBaixoAlto.png";
                }

                posicaoY = 561;
                larguraImagem = 45;
                break;

            default:
                System.out.println("Opção de onda não reconhecida."); // DEBUG
                return;
        }
                

        adicionarOnda(caminhoImagem, 210, posicaoY, 86, larguraImagem); // Chama o metodo para adicionar a onda
    }

       /* ***************************************************************
  * Metodo: adicionarOnda
  * Funcao: adiciona a onda em um ArrayList
  * Parametros: String url , e setups da Imagem
  * Retorno: void
  *************************************************************** */
    private void adicionarOnda(String caminhoImagem, int posicaoX, int posicaoY, int alturaImagem, int larguraImagem) {
        ImageView onda = new ImageView(new Image(caminhoImagem));
        onda.setLayoutX(posicaoX);
        onda.setLayoutY(posicaoY);
        onda.setFitHeight(alturaImagem);
        onda.setFitWidth(larguraImagem);

        ondasImagens.add(onda);
        painelDeOndas.getChildren().add(onda);
    }

         /* ***************************************************************
  * Metodo: starWaveAnimation
  * Funcao: iniciar a animacao
  * Parametros: void
  * Retorno: void
  *************************************************************** */
    private void startWaveAnimation() {
        stopWaveAnimation(); // Parar qualquer animacao existente antes de iniciar uma nova
        
        timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private int currentBitIndex = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 500_000_000) {
                    if (currentBitIndex < CamadaFisicaTransmissora.formaDeOnda.length()) {
                        criarOnda(currentBitIndex);
                        currentBitIndex++;
                    }
                    lastUpdate = now;
                }
                moveWaves();
            }
        };
        timer.start();
    }

     /* ***************************************************************
  * Metodo: moveWaves
  * Funcao: mover as ondas
  * Parametros: void
  * Retorno: void
  *************************************************************** */
    private void moveWaves() {
        List<ImageView> ondasParaRemover = new ArrayList<>();

        for (ImageView onda : ondasImagens) {
            onda.setLayoutX(onda.getLayoutX() + 2);

            if (onda.getLayoutX() > 745) {
                ondasParaRemover.add(onda);
            }
        }

        for (ImageView onda : ondasParaRemover) {
            painelDeOndas.getChildren().remove(onda);
            ondasImagens.remove(onda);
        }
    }

     /* ***************************************************************
  * Metodo: voltarTela
  * Funcao: voltar a tela de menu para escolher de novo o metodo
  * Parametros: event
  * Retorno: void
  *************************************************************** */
    @FXML
    public void voltarTela(MouseEvent event) throws IOException {
        stopWaveAnimation(); // Parar a animacao ao voltar

        root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


     /* ***************************************************************
  * Metodo: stopWaveAnimation
  * Funcao: parar animacao
  * Parametros: void
  * Retorno: void
  *************************************************************** */
    private void stopWaveAnimation() {
        if (timer != null) {
            timer.stop(); // Para o AnimationTimer
            timer = null; // Limpa a referência ao timer
        }     
    }

    
}
