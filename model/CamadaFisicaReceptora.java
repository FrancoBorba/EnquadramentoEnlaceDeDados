/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 28/08/2024
* Ultima alteracao.: 04/09/2024
* Nome.............: CamadaFisicaTransmissora
* Funcao...........: Desfazer o sinal de onda e transformar em binario de novo
*************************************************************** */
package model;

import util.DataSingleton;

public class CamadaFisicaReceptora {

    static DataSingleton data = DataSingleton.getInstance(); // concecta as telas
    static String formaDeOndaBinaria = ""; // utilizar para gerar a onda
     /* ***************************************************************
  * Metodo: camadaFisicaReceptora
  * Funcao: receber os bits e os transormar em binario
  * Parametros: vetor de bits
  * Retorno: void
  *************************************************************** */
  public static void camadaFisicaReceptora(int quadro[]){
    

    System.out.println("CAMADA FISICA RECEPTORA :");
         int fluxoBrutoDeBits [] = new int[quadro.length]; 

     
      switch (data.getOpcao()) { // verifica pelo Singleton qual vai ser a conversao 
        case 0:{ // binario
          fluxoBrutoDeBits = camadaFisicaReceptoraDecodificacaoBinaria(quadro);
          for(int i = 0 ; i < quadro.length ; i++){
            System.out.println("Valor dos bits armazenados no indice(binario) : " +fluxoBrutoDeBits[i]);
          }
         
         
          System.out.println("---------------------------------------------------------");
      
        
          break;
        }
        case 1:{ // manchester
         fluxoBrutoDeBits = camadaFisicaReceptoraDecodificacaoManchester(quadro);
         for(int i = 0 ; i < quadro.length/2 ; i++){
         System.out.println("Valor dos bits armazenados no indice(manchester) : " +fluxoBrutoDeBits[i]);
          }
          
          System.out.println("---------------------------------------------------------");

          break;
        }
        case 2:{ // manchester diferencial 
          fluxoBrutoDeBits = camadaFisicaReceptoraDecodificacaoManchesterDiferencial(quadro);
          for(int i = 0 ; i < quadro.length/2 ; i++){
          System.out.println("Valor dos bits armazenados no indice(manchester diferencial) : " +fluxoBrutoDeBits[i]);
          }
         
          System.out.println("---------------------------------------------------------");
          break;
        }

          
          
      
        default:
          break;
      }
     CamadaEnlaceDadosReceptora.camadaEnlaceDadosReceptora(fluxoBrutoDeBits); // chama a proxima camada
}

   /* ***************************************************************
  * Metodo: camadaFisicaReceptoraDecodificacaoBinaria
  * Funcao: transformar os bits em binario nesse metodo nao fazemos nada pois nao houve conversao
  * Parametros: vetor de bits
  * Retorno: int[] (vetor de bits)
  *************************************************************** */
public static int[] camadaFisicaReceptoraDecodificacaoBinaria(int quadro[]){
    // nao precisa decodificar pois a binaria nao sofre alteracoes
    return  quadro;
  }

     /* ***************************************************************
  * Metodo: camadaFisicaReceptoraDecodificacaoManchester
  * Funcao: transformar os sinais da manchester em binario 
  * Parametros: vetor de bits
  * Retorno: int[] (vetor de bits)
  *************************************************************** */
  public static int[] camadaFisicaReceptoraDecodificacaoManchester(int quadro[]) {
    int novoQuadro[] = new int[quadro.length / 2]; // Apos a decodificacao Manchester, o quadro diminui de tamanho
    int indiceNQ = 0; // indice do novo quadro
    int mascara = 3; // Mascara para capturar 2 bits de cada vez (Manchester usa 2 bits por 1 bit original)
    int contador = 0; // Para controlar a atualizacao do indice no novo quadro
    String formaDeOndaBinaria = ""; // Utilizado para depuracao e identificacao de erros
    int vezes = 16; // Para cada 32 bits (2 em 2), troca o indice do quadro original

      if(data.getEnquadramento() == 3){ // se o enquadramento for pela violacao da camada fisica deve se retirar as flags aq
      
  // quadro = desenquadramentoViolacaoDaCamadaFisica(quadro);
  }

  System.out.println(quadro[0]);

    for (int i = 0; i < quadro.length; i++) {
        vezes = 16; // Restabelece o contador para cada novo inteiro do quadro original

        while (vezes > 0) {
            // Mascara para capturar os 2 bits mais significativos (da esquerda para a direita)
            int doisBits = (quadro[i] >> (vezes * 2 - 2)) & mascara; // Pega os 2 bits mais à esquerda

            if (doisBits == 1) { // Codificacao Manchester "01" (que corresponde ao bit 0)
                novoQuadro[indiceNQ] = novoQuadro[indiceNQ] << 1; // Desloca 1 bit para a esquerda e adiciona 0
                formaDeOndaBinaria = "0" + formaDeOndaBinaria;
                contador++;
            } else if (doisBits == 2) { // Codificacao Manchester "10" (que corresponde ao bit 1)
                novoQuadro[indiceNQ] = (novoQuadro[indiceNQ] << 1) | 1; // Desloca 1 bit para a esquerda e adiciona 1
                formaDeOndaBinaria = "1" + formaDeOndaBinaria;
                contador++;
            }

            vezes--; // Reduz o numero de bits restantes a serem analisados

            if (contador == 32) { // Quando o contador chega a 32 (64 bits decodificados) muda o indice no novo quadro
                indiceNQ++;
                contador = 0;
            }
        }
    }

    System.out.println(formaDeOndaBinaria);
    System.out.println("Tamanho da onda = " + formaDeOndaBinaria.length());

    return novoQuadro;
}


       /* ***************************************************************
  * Metodo: camadaFisicaReceptoraDecodificacaoManchesterDiferencial
  * Funcao: transformar os sinais da manchester diferencial em binario 
  * Parametros: vetor de bits
  * Retorno: int[] (vetor de bits)
  *************************************************************** */

  public static int[] camadaFisicaReceptoraDecodificacaoManchesterDiferencial(int quadro[]) {

  
   // Cria um novo array para armazenar o quadro decodificado, com metade do tamanho do array original,
// ja que a codificacao Manchester Diferencial duplica o numero de bits.
int[] novoQuadro = new int[quadro.length / 2];


int indiceNvQ = 0; 
int cont = 0; 
int bitsProcessados = 0; 
int inteiro = 0; 
int mascara = 1 << 31; 
int bit = 0; 
int novoInteiro = 0; 

  if(data.getEnquadramento() == 3){ // se o enquadramento for pela violacao da camada fisica deve se retirar as flags aq
 //quadro = desenquadramentoViolacaoDaCamadaFisica(quadro);
  }
// Loop que percorre cada inteiro no array 'quadro' (codificado em Manchester Diferencial).
for (int j = 0; j < quadro.length; j++) {
    cont = 0; // Reseta o contador de bits para o inteiro atual.
    inteiro = quadro[j]; 

    

    // Loop que percorre os 16 primeiros bits de cada inteiro do quadro.
    // Cada iteracao deste loop processa 2 bits codificados em Manchester.
    while (cont < 16) {
     
        bit = (inteiro & mascara) == 0 ? 0 : 1;

     
        novoInteiro <<= 1;

    
        novoInteiro = novoInteiro | bit;

      
        inteiro <<= 2;

        // Incrementa o contador de bits processados no inteiro atual.
        cont++;

        // Incrementa o contador geral de bits processados (para controlar quando completar 32 bits).
        bitsProcessados++;

        // Se 32 bits foram processados (um inteiro completo foi reconstruido):
        if (bitsProcessados % 32 == 0 && bitsProcessados != 0) {
            // Armazena o novo inteiro decodificado no array novoQuadro.
            novoQuadro[indiceNvQ] = novoInteiro;

            // Atualiza o indice para o proximo inteiro decodificado no novoQuadro.
            if (indiceNvQ < (quadro.length / 2) - 1) {
                indiceNvQ += 1;
            }

            // Reseta o novoInteiro para comecar a decodificacao de um novo inteiro.
            novoInteiro = 0;

            // Reseta o contador geral de bits pois ja completamos 32 bits.
            bitsProcessados = 0;
        }
    }
}

// Retorna o array contendo o quadro decodificado.
return novoQuadro;

  }


public static int[] desenquadramentoViolacaoDaCamadaFisica(int quadro[]) {
  
    return quadro;
}

  public static int retornaBitsSignificativos(int numero) {
    int numeroDeBits = Integer.toBinaryString(numero).length();
    if (numeroDeBits <= 8) {
      numeroDeBits = 8;
    } else if (numeroDeBits <= 16) {
      numeroDeBits = 16;
    } else if (numeroDeBits <= 24) {
      numeroDeBits = 24;
    } else if (numeroDeBits <= 32) {
      numeroDeBits = 32;
    }
    return numeroDeBits;
  }

  public static int moverBitsEsquerda(int numero) {
    numero <<= (32 - retornaBitsSignificativos(numero));
    return numero;
  }
   public static int retornaBytesSignificativos(int numero) {
    int numeroDeBits = Integer.toBinaryString(numero).length();
    int numeroDeBytes = 0;
    if (numeroDeBits <= 8) {
      numeroDeBytes = 1;
    } else if (numeroDeBits <= 16) {
      numeroDeBytes = 2;
    } else if (numeroDeBits <= 24) {
      numeroDeBytes = 3;
    } else if (numeroDeBits <= 32) {
      numeroDeBytes = 4;
    }
    return numeroDeBytes;
  }



}


