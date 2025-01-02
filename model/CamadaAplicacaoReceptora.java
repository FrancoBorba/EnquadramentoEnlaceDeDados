/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 28/08/2024
* Ultima alteracao.: 04/09/2024
* Nome.............: CamadaAplicacaoReceptora
* Funcao...........: Descodificar os bits e armazenar em uma String
*************************************************************** */
package model;



import util.DataSingleton;

public class CamadaAplicacaoReceptora {


   static DataSingleton data = DataSingleton.getInstance(); // concecta as telas

        /* ***************************************************************
  * Metodo: camadaDeAplicacaoReceptora
  * Funcao: transforma os bits em String
  * Parametros: vetor de bits
  * Retorno: void
  *************************************************************** */
       /* ***************************************************************
  * Metodo: camadaDeAplicacaoReceptora
  * Funcao: transforma os bits em String
  * Parametros: vetor de bits
  * Retorno: void
  *************************************************************** */
  public static void camadaDeAplicacaoReceptora(int quadro[]) {
    StringBuilder mensagem = new StringBuilder();
    int inteiro;

    for (int j = 0; j < quadro.length; j++) {
        // Extrai bytes enquanto houver bits validos
        for (int i = 0; i < 4; i++) { // Lê ate 4 bytes de cada quadro
            inteiro = retornaPrimeiroByte(quadro[j]);

            if (inteiro != 0) { // Adiciona apenas se o byte for valido
                mensagem.append((char) inteiro);
            }

            // Desloca os bits do quadro[j] para analisar o proximo byte
            quadro[j] <<= 8; 
        }
    }

    AplicacaoReceptora.aplicacaoReceptora(mensagem.toString()); // Chama a aplicacao receptora
}

public static int retornaPrimeiroByte(int numero) {
    int primeiroByte = 0;
    int displayMask = 1 << 31; // Mascara para capturar o bit mais significativo

    for (int i = 0; i < 8; i++) { // Extrai 8 bits (1 byte)
        int bit = (numero & displayMask) == 0 ? 0 : 1;
        primeiroByte <<= 1;
        primeiroByte |= bit; // Adiciona o bit ao primeiro byte
        numero <<= 1; // Move para o próximo bit
    }

    return primeiroByte; // Retorna o byte extraido
}

}