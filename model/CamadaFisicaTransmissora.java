/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 28/08/2024
* Ultima alteracao.: 29/09/2024 (alterei os codigos de coficcacao para utilizar o bit mais significativo)
* Nome.............: CamadaFisicaTransmissora
* Funcao...........:  Transformar o sinal de onda relativo ao escolhido na GUI 
*************************************************************** */
package model;

import util.DataSingleton;

public class CamadaFisicaTransmissora {
  static DataSingleton data = DataSingleton.getInstance(); // concecta as telas

   public static String formaDeOnda = ""; // utilizada para debug e para gerar a animacao

  


     /* ***************************************************************
  * Metodo: camadaFisicaTransmissora
  * Funcao: transformar os bits nos sinais de onda
  * Parametros: void
  * Retorno: void
  *************************************************************** */
    public static void camadaFisicaTransmissora(int quadro[]){

        formaDeOnda = ""; // reseta a forma de onda a cada
        System.out.println("CAMADA FISICA TRANSMISSORA:");
         int fluxoBrutoDeBits [] = new int[quadro.length]; 

     
      switch (data.getOpcao()) { // verifica pelo Singleton qual vai ser a conversao 
        case 0:{
          fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoBinaria(quadro);
          for(int i = 0 ; i < quadro.length ; i++){
            System.out.println("Valor dos bits armazenados no indice(binario) : " +fluxoBrutoDeBits[i]);
          }
          
   
          System.out.println("---------------------------------------------------------");
      
        
          break;
        }
        case 1:{
          fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchester(quadro);
          for(int i = 0 ; i < quadro.length ; i++){
               System.out.println("Valor dos bits armazenados no indice(manchester) : " +fluxoBrutoDeBits[i]);
          }
          
       
        
              System.out.println("---------------------------------------------------------");

          break;
        }
        case 2:{
              fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchesterDiferencial(quadro);
           for(int i = 0 ; i < quadro.length ; i++){
           System.out.println("Valor dos bits armazenados no indice(manchester diferencial) : " +fluxoBrutoDeBits[i]);
          }
      
          
     
          System.out.println("---------------------------------------------------------");
          break;
        }

          
          
      
        default:
          break;
      }
      
      MeioDeComunicacao.meioDeComunicacao(fluxoBrutoDeBits); // chama a proxima camada

      
    }

     /* ***************************************************************
  * Metodo: camadaFisicaTransmissoraCodificacaoBinaria
  * Funcao: apenas gerar uma string com os bits ja que nao e necessario modificar os bits
  * Parametros: vetor de bits
  * Retorno: int[] (vetor de bits)
  *************************************************************** */
     public static int[] camadaFisicaTransmissoraCodificacaoBinaria(int quadro[]){
    int mascara =1; //para verificar cada bit individualmente
    //String formaDeOnda = "" ; // ira conter os bits apos a codificacao

    // Como nao alteramos o quadro nao eeh necessario criar um novo vetor para retornar
   int arrayDeRetorno [] = new int[quadro.length]; // esse array sera retornado

   for( int i =0 ; i < quadro.length ; i++){ // passa o quadro para o outro array , devemos fazer isso pois a manipulacao do quadro com a mascara altera o array
      arrayDeRetorno[i] = quadro[i];
  } 


     // Itera sobre cada elemento do quadro.
     /*Operacao &: A operacao E bit a bit (&) compara cada bit de dois numeros e retorna 1 se ambos os bits forem 1, e 0 caso contrario. */
    for(int j = 0; j < quadro.length; j++){
      while(quadro[j] > 0){
        if((quadro[j] & mascara) == 0){
          formaDeOnda = "0" + formaDeOnda; // Se o bit atual for 0, adiciona "0"  sequência.
        }//Fim if
        else if((quadro[j] & mascara) == 1){
          formaDeOnda = "1" + formaDeOnda; // Se o bit atual for 1, adiciona "1"  sequência.
        }//Fim else if
        quadro[j] = quadro[j] >> 1; // Move para o proximo bit da direita para a esquerda.
      }//Fim while
    }//Fim for j

    formaDeOnda = "0" + formaDeOnda; // Adiciona um bit "0" no inicio da sequência

    System.out.println("Forma de onda binaria da palavra: " + formaDeOnda); // Teste da forma de onda( ver se os bits estao certos)
     System.out.println("Tamanho da onda = " + formaDeOnda.length());
   

    return arrayDeRetorno;
  }

      /* ***************************************************************
  * Metodo: camadaFisicaTransmissoraCodificacaoManchester
  * Funcao: transformar os bits em sinais manchester 
  * Parametros: vetor de bits
  * Retorno: int[] (vetor de bits)
  *************************************************************** */
  public static int[] camadaFisicaTransmissoraCodificacaoManchester(int quadro[]) {
    int novoQuadro[] = new int[quadro.length * 2]; // vetor com o dobro de tamanho do quadro 
    int estadoAtual = 0; // Variavel para armazenar se o bit eh 0 ou 1.
    // estado 0 = baixo-alto, estado 1 = alto-baixo
    String formaDeOndaManchester = ""; // utilizada para analisar se a onda esta correta

    int contador = 0; 
    int indiceNQ = 0; 

    int vezes = 32; 

    for (int i = 0; i < quadro.length; i++) {
        vezes = 32; // Restabelece o contador de bits a cada novo inteiro do quadro

        while (vezes > 0) {
            novoQuadro[indiceNQ] = novoQuadro[indiceNQ] << 2; // Desloca 2 bits para a esquerda para a entrada do estadoAtual

            int bit = (quadro[i] >> (vezes - 1)) & 1; // Pega o bit mais significativo e vai andando para a direita

            if (bit == 0) { // Verifica se o bit eh 0
                formaDeOndaManchester = "01" + formaDeOndaManchester; // Representacao Manchester para bit 0
                formaDeOnda = "0" + formaDeOnda;
                estadoAtual = 1; // Codificacao Manchester para bit 0 eh "01" (1 em decimal)
                novoQuadro[indiceNQ] = novoQuadro[indiceNQ] | estadoAtual; // Adiciona no novoQuadro
                contador++;
            } else { // Caso o bit seja 1
                formaDeOndaManchester = "10" + formaDeOndaManchester;
                 formaDeOnda = "1" + formaDeOnda;
                estadoAtual = 2; // Codificacao Manchester para bit 1 eh "10" (2 em decimal)
                novoQuadro[indiceNQ] = novoQuadro[indiceNQ] | estadoAtual; // Adiciona no novoQuadro
                contador++;
            }

            vezes--; // Reduz o numero de bits restantes a serem analisados

            // Quando o contador chega a 16, precisamos pular para o proximo indice do novo quadro
            if (contador == 16) {
                indiceNQ++;
                contador = 0;
            }
        }
    }

    System.out.println(formaDeOndaManchester); // Teste da forma de onda (ver se os bits estao corretos)
    System.out.println("Tamanho da onda = " + formaDeOndaManchester.length());
    return novoQuadro;
}


    /* ***************************************************************
  * Metodo: camadaFisicaTransmissoraCodificacaoManchesterDiferencial
  * Funcao: transformar os bits em sinais manchester diferencial
  * Parametros: vetor de bits
  * Retorno: int[] (vetor de bits)
  *************************************************************** */
public static  int[] camadaFisicaTransmissoraCodificacaoManchesterDiferencial(int quadro[]) {
    // Cria um novo array para armazenar o quadro codificado com o dobro do tamanho,
    // pois a codificacao Manchester Diferencial duplica o numero de bits.
    int[] novoQuadro = new int[quadro.length * 2];

    int mascara = 1 << 31;
    int inteiro = 0;
    int cont = 0;
    int bit = 0;
    int inteiroManchesterDif = 0; // Armazena os bits codificados
    int indiceManch = 0; // 
    @SuppressWarnings("unused")
    int umBitaFrente = 0;

    // Itera sobre cada inteiro no array original 'quadro'.
    for (int i = 0; i < quadro.length; i++) {
        cont = 0;
        inteiro = quadro[i]; // Inteiro do quadro original a ser codificado
        umBitaFrente = inteiro; 
        umBitaFrente <<= 1; 

 
   
      

        // Para cada inteiro, percorre seus 32 bits.

        while (cont < 32) {
          
       
            // Aplica a mascara para verificar o bit mais a esquerda. 
          
            bit = (inteiro & mascara) == 0 ? 1 : 3;

            // Adiciona o bit verificado na forma de onda.
            formaDeOnda = formaDeOnda + ((inteiro & mascara) == 0 ? "01" : "10");

            inteiroManchesterDif <<= 2;
            inteiroManchesterDif = inteiroManchesterDif | bit;
            inteiro <<= 1;

            cont++; 

            // A cada 16 bits processados (ou seja 32 bits codificados em Manchester)
            // armazena o resultado no novo quadro.
            if (cont % 16 == 0 && cont != 0) {
                novoQuadro[indiceManch] = inteiroManchesterDif; // Salva o valor codificado
                if (indiceManch < (quadro.length * 2) - 1) {
                    indiceManch += 1; 
                }
                inteiroManchesterDif = 0; // Reseta o valor para processar os proximos bits
            }
        }
    }
    System.out.println(formaDeOnda);
    return novoQuadro; // Retorna o novo quadro codificado
}


  
}
 //o