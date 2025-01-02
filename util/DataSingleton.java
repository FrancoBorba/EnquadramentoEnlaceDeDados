/* ***************************************************************
* Autor: Franco Ribeiro Borba
* Matricula........: 202310445
* Inicio...........: 23/08/2024
* Ultima alteracao.: 23/08/2024
* Nome.............: DataSingleton.java
* Funcao...........: Classe que conecta a troca de informacoes entre telas
*************************************************************** */
package util;

public class DataSingleton {
  private static DataSingleton instance;
  private int opcao;
  private int enquadramento;

    /* ***************************************************************
  * Metodo: getInstance
  * Funcao: Garantir que apenas uma instancia seja criada e fornecer um ponto de acesso global
  * Parametros: Nao recebe
  * Retorno: DataSingleton
  *************************************************************** */
  public static DataSingleton getInstance(){
    if(instance == null){
      instance = new DataSingleton();
    }
      return instance;
  }

  /* ***************************************************************
  * Metodo: getOpcao
  * Funcao: Retornar o que foi escolhido no choice box
  * Parametros: Nao recebe
  * Retorno: inteiro
  *************************************************************** */
    public int getOpcao(){
  return opcao;
    }

  /* ***************************************************************
  * Metodo: setOpcao
  * Funcao: determinar uma nova opcao
  * Parametros: inteiro que sera armazenado em opcao
  * Retorno: void
  *************************************************************** */
    public void setOpcao(int opcao){
        this.opcao = opcao;
    }


      /* ***************************************************************
  * Metodo: getEnquadramento
  * Funcao: Retornar o que foi escolhido no Radio Buttom
  * Parametros: Nao recebe
  * Retorno: inteiro
  *************************************************************** */
    public int getEnquadramento(){
      return enquadramento;
    }

    /* ***************************************************************
  * Metodo: setEnquadramento
  * Funcao: determina o que foi escolhido como tipo de enquadramento
  * Parametros: Nao recebe
  * Retorno: inteiro
  *************************************************************** */
    public void setEnquadramento(int enquadramento){
      this.enquadramento = enquadramento;
    }
}
