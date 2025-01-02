/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 19/09/2024
* Ultima alteracao.: 11/10/2024
* Nome.............: CamadaEnlaceDadosTransmissora
* Funcao...........: Organiza o fluxo bruto de bits em quadros para em caso de erro ter-se menos prejuizo
*************************************************************** */
package model;

import util.DataSingleton;

public class CamadaEnlaceDadosTransmissora {

 private static DataSingleton data = DataSingleton.getInstance(); // transmite informacao entre as telas

  public static void camadaEnlaceDadosTransmissora(int quadro[]){ 
      camadaEnlaceDadosEnquadramento(quadro);
  }

  public static  void camadaEnlaceDadosEnquadramento(int quadro[]){
 
    System.out.println("Camada De enlace de dados Transmissora");
    int quadroEnquadrado[];
   switch (data.getEnquadramento()) {
    case 0:{
       System.out.println("Enlace por Insercao de Caracteres");
      quadroEnquadrado = camadaDeEnlaceDadosTransmissoraEnquadramentoContagemDeCaracteres(quadro);
  
      System.out.println("-------------------------------------------------");
      CamadaFisicaTransmissora.camadaFisicaTransmissora(quadroEnquadrado);
      break; 
    }
    case 1:{
      System.out.println("Enlace por Insercao de Bytes");
     
      quadroEnquadrado = camadaEnlaceDadosEnquadramentoInsercaoDeBytes(quadro);

        System.out.println("-------------------------------------------------");
      CamadaFisicaTransmissora.camadaFisicaTransmissora(quadroEnquadrado);
     
      break;
    }
    case 2:{
            System.out.println("Enlace por Insercao de biys");
  
      quadroEnquadrado = camadaEnlaceDadosEnquadramentoInsercaoDeBits(quadro);
         System.out.println("-------------------------------------------------");
       CamadaFisicaTransmissora.camadaFisicaTransmissora(quadroEnquadrado);
       break;
    }

    case 3:{
       System.out.println("Enlace por violacao da camada fisica");
      quadroEnquadrado = camadaEnlaceDadosEnquadramentoViolacaoDaCamadaFisica(quadro);
       System.out.println("-------------------------------------------------");
        CamadaFisicaTransmissora.camadaFisicaTransmissora(quadroEnquadrado);
         // Passa direto para o meio de comunicacao pois pega o quadro codificado da manchester e manchester
         // diferencial e adiciona a violacao

    }
      
     
   
    default:
      break;
   }
  
  }
   public static void camadaEnlaceDadosControleDeErro(int quadro[]){
    
   
  }
   public static void camadaEnlaceDadosControleDeFluxo(int quadro[]){
    
    
  }

  public static int[] camadaDeEnlaceDadosTransmissoraEnquadramentoContagemDeCaracteres(int quadro []){
    int acumulador = 0; 
    int qtdIndices = 0;
    int cont = 0;
    int inteiro = 0;
    int bit = 0;
    int novoInteiro = 0;
    int mascara = 1<<31;
    int bitsProcessados = 0;
    int indiceNvQ = 0;

    int [] informacaoDeControle = new int[quadro.length];

    
      for(int i = 0; i < quadro.length; i++){
      
      System.out.println("Quantidade de caracteres dentro do indice: " + contaCaracteres(quadro[i]));
      informacaoDeControle[i] = contaCaracteres(quadro[i]);
      System.out.println("Valor da informacao de controle " + informacaoDeControle[i]);
      acumulador += informacaoDeControle[i];
    }

  //Aqui, o codigo calcula o numero de indices necessarios no novo quadro. Como o quadro eh processado em grupos de 3 bytes se o acumulador (numero total de caracteres) for maior que 3 o loop diminui o acumulador e incrementa qtdIndices. No final o nmero de indices necessarios eh armazenado em qtdIndices.
   if(acumulador > 3){
      while(acumulador > 3){
        acumulador -= 3;
        qtdIndices++;
      }
      qtdIndices += 1;
    }else{
      qtdIndices = 1;
    }

    System.out.println("Valor do acumulador " + acumulador);
    System.out.println("Quantida de Indices: " + qtdIndices); // indices novos necessarios

    int[] novoQuadro = new int[qtdIndices]; // novo quadro com o tamanho necessario para a informacao de controle


    for(int i =0; i < quadro.length ; i++){
      cont = 0; // controla quantos bits foram processados
      inteiro = quadro[i]; // armazena o valor do quadro para processar

   

      while (cont < 32) { // ira analisar os 32 bits do indice
        // nessa primeira parte vamos comecar a colocar os bits em um inteiro para o enquadramento ja que vamos ter que fazer um novo quadro
        bit = (inteiro & mascara) == 0 ? 0 : 1; // pega o bit mais significativo e armazena em bit
        novoInteiro  = novoInteiro << 1; // desloca um para esquerda para entrada do bit
        novoInteiro = novoInteiro | bit; // adiciona o bit
        inteiro = inteiro << 1; // faz um shitf left para a analise do proximo inteiro
        cont++; // controla o loop do quadro antigo 
        bitsProcessados++; // controla quantos bits ja foram analisados

        if(bitsProcessados % 24 == 0 && bitsProcessados != 0){ // faz o enquadramento apos 24 bits processado

        System.out.println("Valor do novo inteiro apos  a manipulacao de 24 bits: " + novoInteiro);

          if(indiceNvQ == novoQuadro.length-1){ // verifica se o indice atual eh o ultimo indice do novoQuadro 
            if(acumulador == 1){
              novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | 49; //adicionando informacao de controle 1
            }else if(acumulador == 2){
              novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | 50; //adicionando informacao de controle 2 
            }else if(acumulador == 3){
              novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | 51; //adicionando informacao de controle 3
            }//Fim else if
          }else{ // valor do enquadramento do primeiro quadro   
              novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | 51; //adicionando informacao de controle 3
            //Fim else if
          }
          int contador = 0;
         
         
         novoInteiro = moverBitsEsquerda(novoInteiro);

         System.out.println("Valor do novo Inteiro: " + novoInteiro);   

         while (contador < 24) { // adiciona o novo inteiro no novo quadro apos ter adicionado a informacao de controle
          bit = (novoInteiro & mascara) == 0 ? 0 : 1;
          novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] << 1; // abre espaco para entrada do bit
          novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // adiciona o bit
          novoInteiro = novoInteiro << 1; // move para esquerda para analisar o proximo bit
          contador++;
         } 

         if(indiceNvQ < qtdIndices -1){
          indiceNvQ++; // incrementa o indice do novo vetor se o indice atual for menor do que a quantidade de indices necessarias
         }else{
          // todos os quadros ja foram codificados e sai do looping
          break;
         }  
         novoInteiro = 0;
         bitsProcessados = 0;
          
        } // fim do if de enquadramento
       
     
      
      }
    
    }//Fim do for

   return novoQuadro;
  }//Fim metodo CamadaDeEnlaceDadosTransmissoraEnquadramentoContagemDeCaracteres


 public static int[] camadaEnlaceDadosEnquadramentoInsercaoDeBytes(int quadro[]) {
    int indiceNvQ = 0; // indice para o novo quadro
    int acumulador = 0; // Acumula o total de caracteres
    int qtdIndices = 0; // Quantidade de indices necessarios para o novo quadro
    int mascara = 1 << 31; // Mascara para identificar o bit mais significativo (MSB)
    int cont = 0; // Contador para processar bits por inteiro
    int bitsProcessados = 0; // Contador de bits processados
    int bit = 0; // Valor do bit atual
    int novoInteiro = 0; // Variavel para armazenar um novo inteiro apos processar bits
    int escape = 0;
    int flagEscape = 42;

    // Calcula o total de caracteres em todos os indices do quadro
    for (int i = 0; i < quadro.length; i++) {
        System.out.println("Quantidade de caracteres dentro do indice: " + contaCaracteres(quadro[i]));
        acumulador += contaCaracteres(quadro[i]); // Soma o total de caracteres
    }
    System.out.println("Valor do acumulador: " + acumulador);

    // Determina a quantidade de indices necessarios para o novo quadro
   qtdIndices = acumulador; // enquadramento de 1 em 1 caracter 


    System.out.println("A quantidade de indices necessários : " + qtdIndices);

    // Inicializa o novo quadro com a quantidade de indices calculada
    int novoQuadro[] = new int[qtdIndices];

    // Percorre o quadro original para processar os bits
    for (int i = 0; i < quadro.length; i++) {
        cont = 0; // Reseta o contador de bits por inteiro

        // Processa os bits do quadro atual
        while (cont < 32) { 
            bit = (quadro[i] & mascara) == 0 ? 0 : 1; // Verifica se o MSB e 0 ou 1
            novoInteiro <<= 1; // Desloca a esquerda para inserir o proximo bit
            novoInteiro = novoInteiro | bit; // Insere o bit processado no novo inteiro
            quadro[i] <<= 1; // Desloca o quadro original para o proximo bit
            cont++; // Incrementa o contador de bits processados
            bitsProcessados++; // Incrementa o total de bits processados

            int flag = 69; // Flag para indicar o inicio e fim do quadro
            int contador = 0; // Contador interno para controle de loops

            // Se tiver processado 16 bits, insere a flag e a carga util no novo quadro
            if (bitsProcessados % 8 == 0 && bitsProcessados != 0) {
                // Insere a flag de inicio (8 bits)
                contador = 0;
                flag = moverBitsEsquerda(flag); // Move a flag para a esquerda
                while (contador < 8) { 
                    bit = (flag & mascara) == 0 ? 0 : 1; // Extrai o MSB da flag
                    novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] << 1; // Desloca para a esquerda no novo quadro
                    novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // Insere o bit da flag no novo quadro
                    flag <<= 1; // Desloca a flag para o proximo bit
                    contador++; // Incrementa o contador
                }
              
                // Insere a carga util (8 bits)
                contador = 0;
                novoInteiro = moverBitsEsquerda(novoInteiro); // Desloca o novo inteiro para a esquerda
                while (contador < 8) {

                int analisaEscape = novoInteiro; // variavel para armazenar o valor do quadro e verificar o escape
                analisaEscape = moverBitsEsquerda(analisaEscape);

                  if(contador % 8 == 0){ // a cada 8 bits analise um caracter

                  for(int j = 0 ; j < 8 ; j++){ // analisa o valor do caracter para ver se eh igual ao da flag
                  bit = (analisaEscape & mascara) == 0 ? 0 : 1; // Extrai o MSB da carga util
                  escape <<=1;
                  escape = escape | bit;
                  analisaEscape<<=1;
                } 

                System.out.println("Valor do escape: " + escape);


                if(escape == 69){ // se o valor for igual ao da flag adiciona o escape
                  
                  flagEscape = moverBitsEsquerda(flagEscape);

                  for(int j = 0 ; j < 8 ; j++){ // adiciona o bit de escape quando a carga util for igual a flag
                     bit = (flagEscape & mascara) == 0 ? 0 : 1; // Extrai o MSB da carga util
                    novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] << 1; // Desloca no novo quadro
                    novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // Insere o bit da carga util
                    flagEscape <<= 1; // Desloca a carga util para o proximo bit
                    contador++;
                  }

                    for(int j = 0 ; j < 8 ; j++){ // apos adicionar o escape adiciona a carga util
                    bit = (novoInteiro & mascara) == 0 ? 0 : 1; // Extrai o MSB da carga util
                    novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] << 1; // Desloca no novo quadro
                    novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // Insere o bit da carga util
                    novoInteiro <<= 1; // Desloca a carga util para o proximo bit
                    contador++;
                } 

                }else{
                  for(int j = 0 ; j < 8 ; j++){
                    bit = (novoInteiro & mascara) == 0 ? 0 : 1; // Extrai o MSB da carga util
                    novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] << 1; // Desloca no novo quadro
                    novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // Insere o bit da carga util
                    novoInteiro <<= 1; // Desloca a carga util para o proximo bit
                    contador++;
                } 
                   
                }
                flagEscape = 42;
                escape = 0;

                  }

       
                    
                }

                // Insere a flag de fim (8 bits)
                contador = 0;
                flag = 69;
                flag = moverBitsEsquerda(flag); // Desloca a flag para a esquerda
                while (contador < 8) { 
                    bit = (flag & mascara) == 0 ? 0 : 1; // Extrai o MSB da flag
                    novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] << 1; // Desloca no novo quadro
                    novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // Insere o bit da flag
                    flag <<= 1; // Desloca a flag para o proximo bit
                    contador++; // Incrementa o contador
                }

                // Verifica se deve incrementar o indice do novo quadro
                if (indiceNvQ < qtdIndices - 1) {
                    indiceNvQ += 1; // Incrementa o indice
                } else {
                    break; // Sai do loop se for o ultimo quadro
                }

                novoInteiro = 0; // Reseta o valor do novo inteiro
                bitsProcessados = 0; // Reseta o contador de bits processados
            }
 
        }
    }
    return novoQuadro; // Retorna o novo quadro
}

    public static int[] camadaEnlaceDadosEnquadramentoInsercaoDeBits(int quadro[]){
    int indiceNvQ = 0; // indice para o novo quadro
    int acumulador = 0; // Acumula o total de caracteres
    int qtdIndices = 0; // Quantidade de indices necessarios para o novo quadro
    int mascara = 1 << 31; // Mascara para identificar o bit mais significativo (MSB)
    int cont = 0; // Contador para processar bits por inteiro
    int bitsProcessados = 0; // Contador de bits processados
    int bit = 0; // Valor do bit atual
    int novoInteiro = 0; // Variavel para armazenar um novo inteiro apos processar bits
    int flag = 129; // flag que representa 01111110 em binario

        // Calcula o total de caracteres em todos os indices do quadro
    for (int i = 0; i < quadro.length; i++) {
        System.out.println("Quantidade de caracteres dentro do indice: " + contaCaracteres(quadro[i]));
        acumulador += contaCaracteres(quadro[i]); // Soma o total de caracteres
    }
    System.out.println("Valor do acumulador: " + acumulador);

      // Determina a quantidade de indices necessarios para o novo quadro
   qtdIndices = acumulador; // enquadramento de 1 em 1 caracter


    System.out.println("A quantidade de indices necessários : " + qtdIndices);

    // Inicializa o novo quadro com a quantidade de indices calculada
    int novoQuadro[] = new int[qtdIndices];

    for(int i = 0 ; i < quadro.length ; i++){ // percorre o quadro original

          cont = 0;

        while (cont < 32) { // analisa os caracteres de cada indice do quadro

            bit = (quadro[i] & mascara) == 0 ? 0 : 1; // Verifica se o MSB eh 0 ou 1
            novoInteiro <<= 1; // Desloca a esquerda para inserir o proximo bit
            novoInteiro = novoInteiro | bit; // Insere o bit processado no novo inteiro
            quadro[i] <<= 1; // Desloca o quadro original para o proximo bit
            cont++; // Incrementa o contador de bits processados
            bitsProcessados++; // Incrementa o total de bits processados

        if(bitsProcessados % 8 == 0 && bitsProcessados !=0){ // enquadramento de um em um caracter
       
        // a flag eh 01111110 e devemos garantir que nao ocorra nunca uma sequencia de 6 bits 1 consecutivos
          flag = 126;
          flag = moverBitsEsquerda(flag);

            for(int j = 0 ; j < 8 ; j++){
            bit = (flag & mascara) == 0 ? 0 : 1; // Verifica se o MSB eh 0 ou 1
            novoQuadro[indiceNvQ] <<= 1; // Desloca a esquerda para inserir o proximo bit
            novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // Insere o bit processado no novo inteiro
            flag <<= 1; // analisa o proximo bit
            }

          novoInteiro = moverBitsEsquerda(novoInteiro);
          int verificaCincoUm = 0;

          
          for(int j = 0 ; j < 8 ; j++){
            

            bit = (novoInteiro & mascara) == 0 ? 0 : 1; // Verifica se o MSB eh 0 ou 1
            if(bit  == 1){
              verificaCincoUm++;
            }else{
              verificaCincoUm = 0; // reseta o contador se encontrar um bit 0
            }

            novoQuadro[indiceNvQ] <<= 1; // Desloca a esquerda para inserir o proximo bit
            novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // Insere o bit processado no novo inteiro
            novoInteiro <<= 1; // analisa o proximo bit

            System.out.println("Valor de 1 seguidos " + verificaCincoUm);

              if(verificaCincoUm == 5){ // faz o bitStuffing
                novoQuadro[indiceNvQ] <<= 1; // Desloca a esquerda para inserir o proximo bit
                novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | 0; // Insere o bit processado no novo inteiro
                verificaCincoUm = 0;
                System.out.println("Fez o bitStuffing");
              }

          }

          flag = 126;
          flag = moverBitsEsquerda(flag);

            for(int j = 0 ; j < 8 ; j++){
            bit = (flag & mascara) == 0 ? 0 : 1; // Verifica se o MSB eh 0 ou 1
            novoQuadro[indiceNvQ] <<= 1; // Desloca a esquerda para inserir o proximo bit
            novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit; // Insere o bit processado no novo inteiro
            flag <<= 1; // analisa o proximo bit
            }

            // Verifica se deve incrementar o indice do novo quadro
                if (indiceNvQ < qtdIndices - 1) {
                    indiceNvQ += 1; // Incrementa o indice
                } else {
                    break; // Sai do loop se for o ultimo quadro
                }

                novoInteiro = 0; // Reseta o valor do novo inteiro
                bitsProcessados = 0; // Reseta o contador de bits processados

        }

    }

    }
     System.out.println("Bits: " + quadro[0]);
    return novoQuadro;
  }

    public static int[] camadaEnlaceDadosEnquadramentoViolacaoDaCamadaFisica(int quadro []) {

    int indiceNvQ = 0;
    int cont = 0;
    int bitsProcessados = 0;
    int inteiro = 0;
    int mascara = 1 << 31;
    int bit = 0;
    int novoInteiro = 0;
    int acumulador = 0;
    int qtdIndices = 0;

    int [] informacaoDeControle = new int[quadro.length];

    for(int i = 0; i < quadro.length; i++){
      informacaoDeControle[i] = (retornaBitsSignificativos(quadro[i]))/8;
      acumulador += informacaoDeControle[i];
    }//Fim for

    qtdIndices = acumulador;

    int[] novoQuadro = new int[acumulador];

    for(int m = 0; m < quadro.length; m++){
      quadro[m] = moverBitsEsquerda(quadro[m]);
    }// fim for

    for(int j = 0; j < quadro.length; j++){
      cont = 0;
      inteiro = quadro[j];
      while(cont < 32){
        bit = (inteiro & mascara) == 0 ? 0 : 1;
        novoInteiro <<= 1;
        novoInteiro = novoInteiro | bit;
        inteiro <<= 1;
        cont++;
        bitsProcessados++;
        if(bitsProcessados % 8 == 0 && bitsProcessados != 0){
          novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | 3; //adiciona 11 para limitar o inicio do quadro
          int contador = 0;
          
          novoInteiro = moverBitsEsquerda(novoInteiro);

          @SuppressWarnings("unused")
          int contaUm = 0;

          novoQuadro[indiceNvQ] <<= 6;

          while(contador < 8){
            bit = (novoInteiro & mascara) == 0 ? 1 : 2;
            novoQuadro[indiceNvQ] <<= 2;
            novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit;
            novoInteiro <<= 1;
            contador++;
          }//Fim while

          novoQuadro[indiceNvQ] <<= 8;

          if(indiceNvQ < qtdIndices - 1){
            // System.out.println("ENTREI E SUBI O INDICE");
            indiceNvQ += 1;
          }else{
            break;
          }//Fim else
          novoInteiro = 0;
          bitsProcessados = 0;
        }//Fim if
        if(cont == 32 && j == quadro.length - 1 && novoInteiro != 0){
          novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | 3;
          int contador = 0;
        
          novoInteiro = moverBitsEsquerda(novoInteiro);

          @SuppressWarnings("unused")
          int contaUm = 0;

          novoQuadro[indiceNvQ] <<= 8;

          while(contador < 8){
            bit = (novoInteiro & mascara) == 0 ? 1 : 2;
            novoQuadro[indiceNvQ] <<= 2;
            novoQuadro[indiceNvQ] = novoQuadro[indiceNvQ] | bit;
            novoInteiro <<= 1;
            contador++;
          }//Fim while

          novoQuadro[indiceNvQ] <<= 8;

          if(indiceNvQ < qtdIndices - 1){
            // System.out.println("ENTREI E SUBI O INDICE");
            indiceNvQ += 1;
          }else{
            break;
          }//Fim else
          novoInteiro = 0;
          bitsProcessados = 0;
        }//Fim if
      }//Fim while
    }//Fim for

    for(int i =0; i < novoQuadro.length; i++){
      novoQuadro[i] = moverBitsEsquerda(novoQuadro[i]);
    }//FIm for
    return novoQuadro;
  }//fim do metodo CamadaEnlaceDadosTransmissoraViolacaoDaCamadaFisica






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




public static int contaCaracteres(int valorBruto){
  int contador = 0;
  for(int i= 0 ; i < 32 ; i+= 8){// incrementa de 8 em 8 bits pois cada caractere tem 8 bits
        // Verifica se o byte correspondente (8 bits) eh diferente de zero
    if((valorBruto & (0xFF << i)) != 0){ // 0xFF representa 11111111 em binario
      contador++;
    }
  }
  return contador;
}







}
