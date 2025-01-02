/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 23/08/2024
* Ultima alteracao.: 23/08/2024
* Nome.............: Principal.java
* Funcao...........: Classe principal do programa
*************************************************************** */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import controller.ControllerAplicacao;
import controller.ControllerCapa;
@SuppressWarnings("unused")


public class Principal extends Application{
  public static void main(String[] args) { 
    launch(args); 
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
   Parent root = FXMLLoader.load(getClass().getResource("view/menu.fxml")); // carrega o fxml na tela
   Scene scene = new Scene(root); // adiciona a root na scene
   Image icon = new Image("image/icon.png"); // adiciona a imagem ao object 
   primaryStage.setTitle("Camada fisica"); // define um titulo
   primaryStage.getIcons().add(icon); // adiciona um icon
   primaryStage.setScene(scene); // adiciona a tela no palco
   primaryStage.setResizable(false); // nao permite aumentar a tela
   primaryStage.show(); // mostra a gui
  }


}