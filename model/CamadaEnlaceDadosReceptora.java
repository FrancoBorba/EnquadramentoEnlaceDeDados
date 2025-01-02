/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 19/09/2024
* Ultima alteracao.: 11/10/2024
* Nome.............: CamadaEnlaceDadosReceptora
* Funcao...........: Organiza o fluxo bruto de bits em quadros para em caso de erro ter-se menos prejuizo
*************************************************************** */
package model;

import util.DataSingleton;

public class CamadaEnlaceDadosReceptora {

  private static DataSingleton data = DataSingleton.getInstance(); // transmite informacao entre as telas

  public static void camadaEnlaceDadosReceptora(int quadro[]){
    camadaEnlaceDadosReceptoraEnquadramento(quadro);
  }
  public static void camadaEnlaceDadosReceptoraEnquadramento(int quadro[]){
     int quadroEnquadrado[];
   switch (data.getEnquadramento()) {
    case 0:{
      quadroEnquadrado = camadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres(quadro);
      CamadaAplicacaoReceptora.camadaDeAplicacaoReceptora(quadroEnquadrado);
      break; 
    }
    case 1:{
      System.out.println("Desenquadramento por Bytes");
      quadroEnquadrado = camadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes(quadro);
      System.out.println("-----------------------------------");
      CamadaAplicacaoReceptora.camadaDeAplicacaoReceptora(quadroEnquadrado);

      break;
    }
    case 2:{

      quadroEnquadrado = camadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBits(quadro);
      CamadaAplicacaoReceptora.camadaDeAplicacaoReceptora(quadroEnquadrado);
       break;
    }

    case 3:{
      quadroEnquadrado = camadaEnlaceDadosReceptoraEnquadramentoViolacaoDaCamadaFisica(quadro);
      CamadaAplicacaoReceptora.camadaDeAplicacaoReceptora(quadroEnquadrado);
    }
      
     
   
    default:
      break;
   }
  }
  public static void camadaEnlaceDadosReceptoraControleDeErro(int quadro[]){

  }
  public static void camadaEnlaceDadosReceptoraControleDeFluxo(int quadro[]){

  }
  
  public static int[] camadaEnlaceDadosReceptoraEnquadramentoContagemDeCaracteres(int quadro[]){
  int novoQuadro[] = new int[quadro.length]; // quadro para simular o desenquadramento
  int informacao = 0;
  int mascara = 1 << 31;
  int bit = 0;
  int contador = 0;
  int indiceNvQ = 0;

  for(int i = 0 ; i < quadro.length ; i++){
    informacao = 0;
    for(int j= 0 ; j < 8 ; j++){ // captura o primeiro byte para identificar quantos caracteres serao lidos
     bit = (mascara & quadro[i]) == 0 ? 0 : 1;
     informacao <<=1;
      informacao = informacao | bit;
      quadro[i] <<= 1;
 
    }
    System.out.println("O valor da informação eh: " + informacao); // valor do enquadramento
    
  

    switch (informacao){ // de acordo com o valor faz a insercao correta em um novo quadro
      
      case 49:{
        for(int k = 0 ; k < 8 ; k++){ // se o valor for 1 insere 8 vezes
          bit = (quadro[i] & mascara) == 0 ? 0 : 1; // pega o bit mais significativo
          novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] << 1 ; // abre espaco para entrar o bit
          novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // adiciona o bit
          quadro[i] = quadro[i] << 1; // analisa o proximo bit mais significativo
          contador++;
          System.out.println("Entrou nesse metodo");
       if(contador == 32 ){
        indiceNvQ++;
        contador = 0;
        
    }
        }
        break;
      }
      case 50:{
         for(int k = 0 ; k < 16 ; k++){
          bit = (quadro[i] & mascara) == 0 ? 0 : 1; // pega o bit mais significativo
          novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] << 1; // abre espaco para entrar o bit
          novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // adiciona o bit
          quadro[i] = quadro[i] << 1; // analisa o proximo bit mais significativo
          contador++;
      if(contador == 32){
        indiceNvQ++;
        contador = 0;
      
    }
        }
        break;
      }
      case 51:{
      
           for(int k = 0 ; k < 24 ; k++){
          bit = (quadro[i] & mascara) == 0 ? 0 : 1; // pega o bit mais significativo
          novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] << 1; // abre espaco para entrar o bit
          novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // adiciona o bit
          quadro[i] = quadro[i] << 1; // analisa o proximo bit mais significativo
          contador++;
          if(contador == 32){
        indiceNvQ++;
        contador = 0;
    }
   
        }
        
        break;
      }
      default:
        break;
    }
    
  }
   return novoQuadro;
  }
public static int[] camadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBytes(int quadro[]) {
    int novoQuadro[] = new int[quadro.length]; // Quadro para armazenar a carga util sem as flags
    int informacao = 0;
    int mascara = 1 << 31; 
    int bit = 0;
    int indiceNvQ = 0;
    int cont = 0;
    int bitsProcessados = 0;
    int flags = 0;

    boolean escape = false; // Variavel para identificar quando o asterisco (*) aparece

    for (int i = 0; i < quadro.length; i++) {

        int inteiro = quadro[i];

        // Processando os bits no quadro atual (de 32 bits)
        while (cont < 32) {

            for (int j = 0; j < 8; j++) { // Armazena os caracteres para procurar flags e escape
                bit = (inteiro & mascara) == 0 ? 0 : 1;
                informacao <<= 1;
                informacao = informacao | bit;
                inteiro <<= 1; // Desloca o quadro para a esquerda para processar o proximo bit
                cont++;
            }

            // Verifica se encontrou o escape (*)
            if (informacao == 42) {
              System.out.println("Achou o escape");
              escape = true; // Proximo 'E' sera considerado parte da carga util
            } 
            // Verifica se encontrou a flag (E), mas so se nao estiver em modo escape
            else if (informacao == 69) {
             
                if (escape) {
                    // O 'E' eh parte da carga util entao tratamos ele como dado
                    escape = false; // Desativa o modo escape
                    for (int j = 0; j < 8; j++) {
                       informacao = moverBitsEsquerda(informacao);
                        bit = (informacao & mascara) == 0 ? 0 : 1;
                        novoQuadro[indiceNvQ] <<= 1;
                        novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit;
                        informacao <<= 1; // Desloca o byte para processar o proximo bit
                    }
                    bitsProcessados += 8;
                } else {
                  System.out.println("Encontrou flag");
                    flags++; // Flag 'E' encontrada
                }
            } 
            // Se for outro byte, trata como parte da carga util
            else {
                 informacao = moverBitsEsquerda(informacao);
                if (flags >= 1 && flags < 2) { // Copia da carga util entre flags
                    for (int j = 0; j < 8; j++) {
                        bit = (informacao & mascara) == 0 ? 0 : 1;
                        novoQuadro[indiceNvQ] <<= 1;
                        novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit;
                        informacao <<= 1; // Desloca o byte para processar o proximo bit
                    }
                    bitsProcessados += 8;
                }
            }

            // Avanca o indice no novo quadro a cada 32 bits processados
            if (bitsProcessados == 32) {
                indiceNvQ++;
                bitsProcessados = 0; // Reinicia a contagem de bits processados
            }

            // Reseta a variavel de informacao para o prximo byte
            informacao = 0;
        }
        flags = 0;
        cont = 0; // Reseta o contador de bits por quadro
    }

    return novoQuadro; // Retorna o quadro desenquadrado, sem as flags
}



public static int[] camadaEnlaceDadosReceptoraEnquadramentoInsercaoDeBits(int[] quadro) {
    int mascara = 1 << 31; // Mascara para identificar o bit mais significativo 
    int[] novoQuadro = new int[quadro.length]; // Inicializa um novo quadro para o resultado
    int indiceNvQ = 0; // indice do novo quadro
    int bitsProcessados = 0; // Contador de bits processados
    int inteiro = 0;
    int bit = 0;
    int cont = 0;
    int informacao = 0;

    for (int i = 0; i < quadro.length; i++) {
      
        inteiro = quadro[i];
    while (cont < 32) {

      for (int j = 0; j < 8; j++) { // Armazena os caracteres para procurar a flag
                bit = (inteiro & mascara) == 0 ? 0 : 1;
                informacao <<= 1;
                informacao = informacao | bit;
                inteiro <<= 1; // Desloca o quadro para a esquerda para processar o proximo bit
                cont++;
            }

        

          if(informacao == 126){ // encontrou a flag 
          // Reseta a variavel de informacao para o proximo byte
            informacao = 0;
            // nao insere
          } else{
            int verificaCincoUm = 0;
            informacao = moverBitsEsquerda(informacao);

            for(int j = 0 ; j < 8 ; j++){
            bit = (informacao & mascara) == 0 ? 0 : 1;
            if(bit  == 1){
              verificaCincoUm++;
            }else{
              verificaCincoUm = 0; // reseta o contador se encontrar um bit 0
            }

             if(verificaCincoUm == 5){
              informacao<<=1; // descarta o bit que seria lido pois eh inutil
              cont++;
              verificaCincoUm = 0;
              
            }
                novoQuadro[indiceNvQ] <<=1;
                novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit;
                informacao <<= 1; // Desloca o quadro para a esquerda para processar o proximo bit
                bitsProcessados++;

            }

            // Avanca o indice no novo quadro a cada 32 bits processados
            if (bitsProcessados == 32) {
                indiceNvQ++;
                bitsProcessados = 0; // Reinicia a contagem de bits processados
            }

            // Reseta a variavel de informacao para o proximo byte
            informacao = 0;
          }
          
    }
            cont =0;

        
    }

    // Retorna o novo quadro desenquadrado
  
    return novoQuadro;
}


  public static int[] camadaEnlaceDadosReceptoraEnquadramentoViolacaoDaCamadaFisica(int quadro []){
    int[] novoQuadro = new int [quadro.length];
    int contador = 0;
    int bit = 0;
    int mask = 1 << 31;

    for(int j = 0; j < quadro.length; j++){
      contador = 0;
      quadro[j] <<= 8;
      while(contador < 8){
        bit = (quadro[j] & mask) == 0 ? 0 : 1;
        novoQuadro[j] <<= 1;
        novoQuadro[j] = novoQuadro[j] | bit;
        quadro[j] <<= 2;
        contador++;
      }
    }
    for(int i =0; i < quadro.length; i++){
      novoQuadro[i] = moverBitsEsquerda(novoQuadro[i]);
    }
    return novoQuadro;
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





}