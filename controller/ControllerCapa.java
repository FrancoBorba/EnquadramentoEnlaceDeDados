/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 23/08/2024
* Ultima alteracao.: 11/10/2024
* Nome.............: Principal.java
* Funcao...........: Classe para o controle das acoes na tela inicial
*************************************************************** */
package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import util.DataSingleton;

public class ControllerCapa {

   private Parent root;
  private Scene scene;
  private Stage stage;

  @FXML
  private Rectangle codificacaoBinaria , codificacaoManchester , codificacaoManchesterDiferencial; // quadrados opacos para receber a informacao do clique

      @FXML
    private RadioButton contagemDeCaracter;

    @FXML
    private RadioButton insercaoDeBits;

    @FXML
    private RadioButton insercaoDeBytes;

    @FXML
    private RadioButton violacaoCamadaFisica;

    @FXML
    private ToggleGroup tipoDeEnquadramento;


  DataSingleton data = DataSingleton.getInstance(); // concecta as telas

  
        /*
   * ***************************************************************
   * Metodo: codificacaoBinaria
   * Funcao: informar um valor para o dataSingleton
   * Parametros: event
   * Retorno: void
   */
    @FXML
    void codificacaoBinaria(MouseEvent event) throws IOException {
      data.setOpcao(0);
      trocarTela(event);
    }

        /*
   * ***************************************************************
   * Metodo: codificacaoBinaria
   * Funcao: informar um valor para o dataSingleton
   * Parametros: event
   * Retorno: void
   */
    @FXML
    void codificacaoManchester(MouseEvent event) throws IOException {
      data.setOpcao(1);
      trocarTela(event);
    }

        /*
   * ***************************************************************
   * Metodo: codificacaoBinaria
   * Funcao: informar um valor para o dataSingleton
   * Parametros: event
   * Retorno: void
   */
    @FXML
    void codificacaoManchesterDiferencial(MouseEvent event) throws IOException {
        data.setOpcao(2);
        trocarTela(event);
    }


            /*
   * ***************************************************************
   * Metodo: escolherEnquadramento
   * Funcao: informar um valor para o dataSingleton com base nos radio bttons
   * Parametros: event
   * Retorno: void
   */
    @FXML
    public void escolherEnquadramento(ActionEvent event){
      if(contagemDeCaracter.isSelected()){
        data.setEnquadramento(0);
      } else if(insercaoDeBytes.isSelected()){
        data.setEnquadramento(1);
      }else if(insercaoDeBits.isSelected()){
        data.setEnquadramento(2);
      }else if(violacaoCamadaFisica.isSelected()){
        data.setEnquadramento(3);
      }
    }

  

      /*
   * ***************************************************************
   * Metodo: trocarTela
   * Funcao: trocar a tela do menu para aplicacao
   * Parametros: event
   * Retorno: void
    * ***************************************************************
   */
    public void trocarTela(MouseEvent event) throws IOException{
    root = FXMLLoader.load(getClass().getResource("/view/telaPrincipal.fxml")); // Carrega o FXML para a aplicacao
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root); // Passa a scene para o root
    stage.setScene(scene); // passa a scene para o stage
    stage.show(); // exibe o stage

    }
  
}
