/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 28/08/2024
* Ultima alteracao.: 04/09/2024
* Nome.............: AplicacaoReceptora
* Funcao...........: Classe que mostra a mensagem na aplicacao
*************************************************************** */
package model;

import controller.ControllerAplicacao;

public class AplicacaoReceptora {
       /* ***************************************************************
  * Metodo: aplicacaoReceptora
  * Funcao: Mostrar a mensagem codificada
  * Parametros: String mensagem
  * Retorno: void
  *************************************************************** */
  public static void aplicacaoReceptora(String mensagem){
    System.out.println("A mensagem recebida foi: " + mensagem);
    ControllerAplicacao.setLabel(mensagem);
   
  }
}
