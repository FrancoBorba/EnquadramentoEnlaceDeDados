/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 28/08/2024
* Ultima alteracao.: 28/08/2024
* Nome.............: MeioDeComunicacao
* Funcao...........: Enviar o sinal de onda relativo ao escolhido na GUI 
*************************************************************** */
package model;

public class MeioDeComunicacao {

     /* ***************************************************************
  * Metodo: meioDeComunicacao
  * Funcao: simular a macada fisica passando os bits de um meio a outro
  * Parametros: vetor de bits
  * Retorno: void
  *************************************************************** */
  public  static void meioDeComunicacao(int fluxoBrutoDeBits[]){
    int fluxoBrutoDeBitsPontoA[], fluxoBrutoDeBitsPontoB[]; 



    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    fluxoBrutoDeBitsPontoB = new int[fluxoBrutoDeBitsPontoA.length];
/* 
teria problema desta forma? Ja que os valores "brutos"  de cada indice ja estao convertidos e cada indice recebe o valor combinado dos bits de cada indice? (DUVIDA)
 "
    for(int i = 0; i < fluxoBrutoDeBitsPontoA.length ; i++){
      fluxoBrutoDeBitsPontoB[i] = fluxoBrutoDeBitsPontoA[i];
      
    }//Fim for*/ 

    for(int i = 0; i < fluxoBrutoDeBitsPontoA.length ; i++){
      fluxoBrutoDeBitsPontoB[i] = (fluxoBrutoDeBitsPontoB[i] << 32);
      fluxoBrutoDeBitsPontoB[i] = fluxoBrutoDeBitsPontoB[i] | fluxoBrutoDeBitsPontoA[i];
    }//Fim for

   
    CamadaFisicaReceptora.camadaFisicaReceptora(fluxoBrutoDeBitsPontoB);

   
     

}
}
