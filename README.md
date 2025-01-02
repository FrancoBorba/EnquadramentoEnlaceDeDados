# Simulação da Camada Física e Enlace de Dados - Redes 1

Este repositório contém a continuação do projeto que simula a camada física de comunicação em redes de computadores. Agora, foi implementada a camada de enlace de dados, com o objetivo de realizar o **enquadramento** e **desenquadramento** das mensagens antes da transmissão e após o recebimento. A mensagem é codificada e, em seguida, enquadrada e transmitida, utilizando diversas técnicas de manipulação de bits.

## Objetivo

O objetivo desta parte do trabalho é simular o processo de enquadramento de dados, que envolve a segmentação das mensagens em unidades menores para a transmissão, e desenquadramento, que restaura a mensagem original após a recepção. Foram implementadas as seguintes técnicas de enquadramento e manipulação de bits:

- **Enquadramento por contagem de caracteres**: O número de caracteres em cada quadro é contado para determinar o início e o fim do enquadramento.
- **Inserção de bits (Bit Stuffing)**: Durante a transmissão, inserem-se bits extras para garantir que determinados padrões de bits não apareçam em partes do quadro que poderiam ser interpretadas como flag.
- **Inserção de bytes**: Introdução de bytes para começo e fim da mensagem 
- **Inserção de bits de violação da camada física**: A introdução de padrões de violação da camada física (como '11' ou '00') nos quadros para indicar erros ou falhas no canal.
- **Desenquadramento**: O processo de remoção de dados extras e a restauração da mensagem original.

Este trabalho foi desenvolvido como parte da disciplina **Redes 1**, ministrada pelo **Professor Marlos Marques**.

## Funcionalidades

- **Enquadramento de mensagem**: A mensagem é segmentada e enquadrada utilizando uma das técnicas implementadas, com contagem de caracteres ou inserção de bits.
- **Desenquadramento**: A sequência de bits transmitida é processada para remover os bits extras e restaurar a mensagem original.
- **Suporte a técnicas de manipulação de bits**: O trabalho implementa inserção de bits e enquadramento por contagem de caracteres.

## Tecnologias Utilizadas

- **Java**: Linguagem de programação utilizada para implementar a simulação.
- **Estruturas de Dados**: Utilização de vetores e manipulação de bits para realizar o enquadramento e desenquadramento.
- **JavaFX**: Para a interface gráfica que simula a visualização do processo de codificação, enquadramento, transmissão e recepção de mensagens.

## Instruções de Uso

## Como Executar
1. Clone este repositório:
   ```bash
   git clone https://github.com/FrancoBorba/EnquadramentoEnlaceDeDados.git
2. Abra o projeto em sua IDE de preferência.
3. Compile e execute o código principal para iniciar a simulação.

   
## Aprendizados
- Manipulação de bits
- Codigos de enquadramento
- Processo de funcionamento de uma rede com seus protocolos e camadas


## Autor
**Franco Ribeiro Borba**
- **Curso**: Ciência da Computação, 4º semestre
- **Instituição**: UESB (Universidade Estadual do Sudoeste da Bahia)
- **Professor Orientador**: Marlos Marques

## Licença
Este projeto é licenciado sob a Licença MIT. Consulte o arquivo `LICENSE` para mais informações.

## Contato
- [LinkedIn](https://www.linkedin.com/in/franco-borba-37462825b/)
- Email: franco.borba14@gmail.com

