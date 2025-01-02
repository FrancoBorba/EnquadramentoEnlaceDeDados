/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 28/08/2024
* Ultima alteracao.: 01/09/2024
* Nome.............: AplicacaoTransmissora
* Funcao...........: Classe que recebe a mensagem
*************************************************************** */
package model;



import controller.ControllerAplicacao;

public class AplicacaoTransmissora {


  
  /* ***************************************************************
  * Metodo: aplicacaoTransmissora
  * Funcao: receber a mensagem da aplicacao e armazenar
  * Parametros: void
  * Retorno: void
  *************************************************************** */
  public static void aplicacaoTransmissora(){

    String mensagemEnviada = ControllerAplicacao.getMensagem();
    System.out.println(mensagemEnviada);
    CamadaAplicacaoTransmissora.camadaAplicacaoTransmissora(mensagemEnviada);
  
   
  }

  
  
  
 
  
}
