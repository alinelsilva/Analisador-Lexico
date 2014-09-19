package com.br.analisadorlexico.analisadores;

import java.io.FileNotFoundException;
import java.util.List;

import com.br.analisadorlexico.componentes.TabSimbolos;
import com.br.analisadorlexico.componentes.Token;
import com.br.analisadorlexico.leitorarquivo.FileHandler;


public class AnLexico {
	private String caminhoArquivo = "";
	private String lexema;
	private char caracter;
	private boolean codigoValido = false;
	private FileHandler leitorArquivo;
	private TabSimbolos tabSimbolos = TabSimbolos.getInstance();
    private ErrorHandler errorHandler = ErrorHandler.getInstance();
	public Token nextToken() {
		try{
		while (true) {
			// Chama, da classe do professor, a leitura do arquivo, passando
			// como par�metro o caminho
			lexema = "";
			caracter = leitorArquivo.getNextChar(); 
																
			switch (caracter) {
			// Verifique se o caracter (que � �nico, � v�lido)
			case '+':
			case '-':
			case '*':
			case '/':
			case ';':
			case '(':
				return tabSimbolos.pesquisaPalavra(String.valueOf(caracter));
				
			case '_':
				
				lexema += String.valueOf(caracter);
				
				while(!Character.isWhitespace(caracter)){
					
					caracter = leitorArquivo.getNextChar();
					
					//Caso o caracter saia dos padr�es (_, n�mero ou letra), vai para o erro.
					if(!Character.isDigit(caracter) || !Character.isLetter(caracter) || caracter != '_'){
						codigoValido = false;
						while (!Character.isWhitespace(caracter)) {
							lexema += leitorArquivo.getNextChar();
							
						}
						// Salva erro.
						 errorHandler.setError("Token "+ lexema+ " inv�lido. O n�mero tem caracters que n�o fazem parte da gram�tica de n�meros ");//+leitorArquivo.retornaLinha(),leitorArquivo.retornaColuna()
						break;
					}
					
					else if(Character.isDigit(caracter) || Character.isLetter(caracter) || caracter == '_'){
						codigoValido = true;
						lexema += caracter;						
						}
					return tabSimbolos.pesquisaPalavra(lexema);
					}
				break;
				
				
				//** CASO N�O SEJA SOMENTE UM S�MBOLO E SIM UM CONJUNTO **//
			default:
				
				// Verifica se � espa�o em branco e ignora, continuando para a pr�xima intera��o do while (checar um novo caracter)
				if (Character.isWhitespace(caracter)) {
					continue;
				}
				
				
				// Verifica se � coment�rio e ignora, continuando para a pr�xima intera��o do while (checar um novo caracter)
				if (caracter == '#') {
					caracter = leitorArquivo.getNextChar();
					continueLeituraComentario();
					continue;
				}
				
				//** An�lise para n�meros (inteiros ou decimais)
				// Verifica se � um n�mero e faz a an�lise se a continua��o dele � v�lida e retorna o token (float e int)
				if (Character.isDigit(caracter)) {
					
					int indicePonto = 0, indiceE = 0;
					
					while (true) {
						
						lexema += String.valueOf(caracter);
						
						// Pede um novo caracter ao arquivo
						caracter = leitorArquivo.getNextChar();
						
						// Caso o caracter em an�lise seja um espa�o em branco e o n�mero est� ok (n�o termina com um .), pesquisar token.
						if (Character.isWhitespace(caracter) && !lexema.endsWith(".")) {
							return tabSimbolos.pesquisaPalavra(lexema);// retorna token e finaliza a execu��o do m�todo
							  
						}
						// Caso o caracter em an�lise seja um ponto, pela primeira vez
						if (caracter == '.' && indicePonto == 0) {
							lexema += String.valueOf(caracter);
							indicePonto++;
						}
						// Caso o caracter em an�lise seja um E, pela primeira vez
						//Falar com o professor
						if (caracter == 'E' && indiceE == 0) {
							lexema += String.valueOf(caracter);
							indiceE++;
						}
						if (caracter == '.' && indicePonto == 1) {
							while (!Character.isWhitespace(caracter)) {
								lexema += leitorArquivo.getNextChar();
							}
							// Salva erro.
							 errorHandler.setError("Token "+lexema+" inv�lido. O n�mero n�o est� definido corretamente, j� que . poderia ser incluso no n�mero somente uma vez.");//+leitorArquivo.retornaLinha(),leitorArquivo.retornaColuna()
							break;
						}
						if (caracter == 'E' && indiceE == 1) {
							while (!Character.isWhitespace(caracter)) {
								lexema += leitorArquivo.getNextChar();
							}
							// Salva erro.
							errorHandler.setError("Token "+ lexema + " inv�lido. O n�mero n�o est� definido corretamente, j� que E poderia ser incluso no n�mero somente uma vez.");//,leitorArquivo.retornaLinha(),leitorArquivo.retornaColuna()
							break;
						}
				
						if (!Character.isDigit(caracter) && caracter != '.'
								&& caracter != 'E'
								&& !Character.isWhitespace(caracter)) {
							while (!Character.isWhitespace(caracter)) {
								lexema += leitorArquivo.getNextChar();
							}
							// Salva erro.
							 errorHandler .setError("Token "+ lexema+" inv�lido. O n�mero tem caracters que n�o fazem parte da gram�tica de n�meros ");//,leitorArquivo.retornaLinha(),leitorArquivo.retornaColuna()
							break;
						}
					}
					continue; // caso nenhum break ou return seja acionado, a
								// execu��o � feita novamente (looping infito
								// principal desse case de d�gitos.
				}
				
				//** An�lise para n�meros (inteiros ou decimais)
				// Verifica se � uma letra
				if (Character.isLetter(caracter)) {
					lexema += caracter;
					while (true){
						caracter = leitorArquivo.getNextChar();
						
						if (!Character.isDigit(caracter) && Character.isLetter(caracter) && caracter != '_' ){
							
						}
					}
				}

				}
			}
		}catch(Exception e){
				System.out.println("N�o foi poss�vel recuperar caracter.");
			}
		return null; //caso nada seja encontrado.
	}
		

	public AnLexico(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
		try {
			leitorArquivo = new FileHandler(caminhoArquivo);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo n�o encontrado.");
		}
	}

	public AnLexico() {
		super();
	}

	public void continueLeituraComentario() {
		// enquanto n�o encontrar o fim do coment�rio
		while (true) {
			if (caracter != '#') {
				break;
			}
			try{
			caracter = leitorArquivo.getNextChar();
			}catch(Exception e){
				System.out.println("N�o foi poss�vel recuperar caracter. ");
			}
		}
	}
	public List <String> retornarErros(){
		return errorHandler.getErrors();
	}
}
// DICAS PARA O TRATAMENTO DE D�GITOS E LETRAS
// Descartar espa�os em branco
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