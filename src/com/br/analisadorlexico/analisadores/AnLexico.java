package com.br.analisadorlexico.analisadores;

import com.br.analisadorlexico.componentes.Token;
import com.br.analisadorlexico.leitorarquivo.LeitorDeArquivo;

public class AnLexico {
	private String caminhoArquivo = "";
	private String lexema;
	private char caracter;
	private LeitorDeArquivo leitorArquivo;
	//private ErrorHandler errorHandler = ErrorHandler.getInstance();

	public Token nextToken() {

		while (true) {
			// Chama, da classe do professor, a leitura do arquivo, passando
			// como par�metro o caminho
			caracter = leitorArquivo.retornaChar(caminhoArquivo); // modificar para a classe real.

			switch (caracter) {
			// Verifique se o caracter (que � �nico, � v�lido)
			case '+':
			case '-':
			case '*':
			case '/':
			case ';':
			case '(':
				return pesquisaToken(String.valueOf(caracter));
				
			default:
				// Verifica se � espa�o em branco e ignora, continuando para a pr�xima intera��o do while (checar um novo caracter)
				if (Character.isWhitespace(caracter)) {
					continueLeitura(' ');
					continue;
				}
				// Verifica se � coment�rio e ignora, continuando para a pr�xima intera��o do while (checar um novo caracter)
				if (caracter == '#') {
					continueLeitura('#');
					continue;
				}
				// Verifica se � um n�mero e faz a an�lise se a continua��o dele � v�lida e retorna o token (float e int)
				if (Character.isDigit(caracter)) {
					int indicePonto = 0, indiceE = 0;
					Token token = new Token();

					while (true) {
						lexema += String.valueOf(caracter);
						
						//Pede um novo caracter ao arquivo
						caracter = leitorArquivo.retornaChar(caminhoArquivo);
						
						//Caso o caracter em an�lise seja um espa�o em branco e o n�mero est� ok, pesquisar token.
						if (Character.isWhitespace(caracter)&& String.valueOf(lexema.charAt(lexema.length()-2))!=".") {
							token = pesquisaToken(lexema);
							return token; //retorna token e finaliza a execu��o do m�todo
						}
						
						//Caso o caracter em an�lise seja um ponto, pela primeira vez
						if (caracter == '.' && indicePonto == 0) {
							lexema += String.valueOf(caracter);
							indicePonto++;
						}
						
						//Caso o caracter em an�lise seja um E, pela primeira vez
						// Falar com o professor
						if (caracter == 'E' && indiceE == 0) {
							lexema += String.valueOf(caracter);
							indiceE++;
						}


						if (caracter == '.' && indicePonto == 1) {
							while (!Character.isWhitespace(caracter)) {
								lexema += leitorArquivo
										.retornaChar(caminhoArquivo);
							}
							// Salva erro.
							//errorHandler.setError("Token "+lexema+ " inv�lido. O n�mero n�o est� definido corretamente, j� que . poderia ser incluso no n�mero somente uma vez.",leitorArquivo.retornaLinha(),leitorArquivo.retornaColuna());
							lexema = "";
							break;
						}

						if (caracter == 'E' && indiceE == 1) {
							while (!Character.isWhitespace(caracter)) {
								lexema += leitorArquivo
										.retornaChar(caminhoArquivo);
							}
							// Salva erro.
							//errorHandler.setError("Token "+ lexema + " inv�lido. O n�mero n�o est� definido corretamente, j� que E poderia ser incluso no n�mero somente uma vez.",leitorArquivo.retornaLinha(),leitorArquivo.retornaColuna());

							lexema = "";
							break;
						}
						if (Character.isWhitespace(caracter)&& String.valueOf(lexema.charAt(lexema.length()-2))== ".") {
							// Salva erro.
							//errorHandler
							//		.setError("Token "+ lexema+ " inv�lido. O n�mero n�o est� definido corretamente, j� que um n�mero n�o deve terminar com \".\"",leitorArquivo.retornaLinha(),leitorArquivo.retornaColuna());

							lexema = "";
							break;
						}
						if (!Character.isDigit(caracter) || caracter != '.'
								|| caracter != 'E'
								|| !Character.isWhitespace(caracter)) {
							while (!Character.isWhitespace(caracter)) {
								lexema += leitorArquivo
										.retornaChar(caminhoArquivo);
							}
							// Salva erro.
							//errorHandler		.setError("Token "+ lexema+ " inv�lido. O n�mero tem caracters que n�o fazem parte da gram�tica de n�meros ",leitorArquivo.retornaLinha(),leitorArquivo.retornaColuna());
							lexema = "";
							break;
						}
					}
					continue; //caso nenhum break ou return seja acionado, a execu��o � feita novamente (looping infito principal desse case de d�gitos.
				}
				
				
				// Verifica se � uma letra
				if (Character.isLetter(caracter)) {

				}
				// Verifica se � um "_"
				if (caracter == '_') {

				}

			}
		}
	}

	public AnLexico(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}
	public AnLexico() {
		super();
	}

	public Token pesquisaToken(String palavra) {
		return null;
	}

	public void continueLeitura(char c) {
		// enquanto n�o encontrar o fim do coment�rio
		while (true) {
			if (caracter == c) {
				break;
			}
			caracter = leitorArquivo.retornaChar(caminhoArquivo);
		}
	}
}
// DICAS PARA O TRATAMENTO DE D�GITOS E LETRAS
//Descartar espa�os em branco
// Descarta coment�rios
// switch (c){
//
// case '(':
// fa�a algo
// case '"':
// fa�a algo
// default:
// if (Character.isLetter(c) ou _){
// while (Character.isLetter(c) ou n�mero ou _){
// lexema += c;
// c = Chama, da classe do professor, a leitura do arquivo, passando como
// par�metro o caminho novamente (se necess�rio)
//
// }
// }
// Token de retorno = chama o m�todo reconhecerId(lexema)
// retorno token;
// caso seja null, tratar
// no caso de letras, inserir na tabela de simbolos, caso seja v�lido, com
// linha e coluna (oferecidos pelo professor)
// Sen�o, salvar erro
