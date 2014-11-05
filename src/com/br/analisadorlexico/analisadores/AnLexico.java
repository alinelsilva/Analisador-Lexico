package com.br.analisadorlexico.analisadores;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.br.analisadorlexico.componentes.TabSimbolos;
import com.br.analisadorlexico.componentes.Token;
import com.br.analisadorlexico.utils.FileHandler;

public class AnLexico {

	private StringBuilder lexema = new StringBuilder();
	private char caracter;
	// private ArrayList<Character> caractersPendentes = new ArrayList();
	private boolean primeiroCaracter = true;
	private FileHandler leitorArquivo;
	private TabSimbolos tabSimbolos = TabSimbolos.getInstance();
	private ErrorHandler errorHandler = ErrorHandler.getInstance();
	// private Token token = new Token();
	public static String caminhoArquivo;

	public Token nextToken() {

		try {

			if (primeiroCaracter) {
				primeiroCaracter = false;
				// Chama, da classe do professor, a leitura do arquivo, passando
				// como par�metro o caminho
				caracter = leitorArquivo.getNextChar();
			}

			while (true) {

				lexema.delete(0, lexema.length());

				switch (caracter) {

				// Verifique se o caracter (que � �nico) � v�lido

				case '+':
				case '-':
					primeiroCaracter = true;
					return tabSimbolos.retornaOperadorAddSub(
							String.valueOf(caracter), leitorArquivo.getLine(),
							leitorArquivo.getColumn());
				case '*':
				case '/':
					primeiroCaracter = true;
					return tabSimbolos.retornaOperadorMultDiv(
							String.valueOf(caracter), leitorArquivo.getLine(),
							leitorArquivo.getColumn());
				case ';':
					primeiroCaracter = true;
					return tabSimbolos.retornaTerm(String.valueOf(caracter),
							leitorArquivo.getLine(), leitorArquivo.getColumn());
				case '(':
				case ')':
					primeiroCaracter = true;
					return tabSimbolos.retornaParenteses(
							String.valueOf(caracter), leitorArquivo.getLine(),
							leitorArquivo.getColumn());

					// Verifica _ se � seguido pelos valores aceitos na
					// linguagem
				case '_':
					lexema.append(caracter);

					while (Character.isDigit(caracter)
							|| Character.isLetter(caracter) || caracter == '_') {

						caracter = leitorArquivo.getNextChar();
						lexema.append(caracter);
					}
					return tabSimbolos.retornaSalvaPalavra(
							String.valueOf(lexema), leitorArquivo.getLine(),
							leitorArquivo.getColumn());

					// Verifica aspas simples, se � seguido pelos valores
					// aceitos na linguagem
				case '\'':
					lexema.append(caracter);
					caracter = leitorArquivo.getNextChar();

					while (caracter != '\'') {
						lexema.append(caracter);
						caracter = leitorArquivo.getNextChar();
					}
					if (caracter == '\'') {
						lexema.append(caracter);
						caracter = leitorArquivo.getNextChar();
					}
					return tabSimbolos.retornaLiteral(String.valueOf(lexema),
							leitorArquivo.getLine(), leitorArquivo.getColumn());

					// verifica operador <-
				case '<':
					caracter = leitorArquivo.getNextChar();
					if (caracter == '-') {
						lexema.append('<');
						lexema.append(caracter);
						caracter = leitorArquivo.getNextChar();
						return tabSimbolos.retornaOperadorAtr(
								String.valueOf(lexema),
								leitorArquivo.getLine(),
								leitorArquivo.getColumn());

					} else
						errorHandler
								.setError(leitorArquivo.getLine()
										+ ", "
										+ leitorArquivo.getColumn()
										+ " | O caracter < deveria ser seguido por \"-\". ");
					caracter = leitorArquivo.getNextChar();
					break;

				// Verifica��o para operadores
				case '&':
					lexema.append(caracter);
					caracter = leitorArquivo.getNextChar();
					boolean primeiraRodada = true;
					while (caracter != '&'
							&& (caracter == '<' || caracter == '>' || caracter == '=')) {
						if (!primeiraRodada) {
							caracter = leitorArquivo.getNextChar();
						}
						primeiraRodada = false;
						lexema.append(caracter);
					}
					caracter = leitorArquivo.getNextChar();
					String lexAnalise = lexema.toString().trim();
					// teste para os lexemas v�lidos. Caso n�o seja

					if (lexAnalise.equals("&<&") || lexAnalise.equals("&>&")
							|| lexAnalise.equals("&>=&")
							|| lexAnalise.equals("&<=&")
							|| lexAnalise.equals("&=&")
							|| lexAnalise.equals("&<>&"))
						return tabSimbolos.retornaOperadorRelacional(
								String.valueOf(lexema),
								leitorArquivo.getLine(),
								leitorArquivo.getColumn());

					else {
						errorHandler.setError(leitorArquivo.getLine() + ", "
								+ leitorArquivo.getColumn() + " | Operador "
								+ lexema + " n�o � v�lido.");
					}
					break; // em caso de erro, realiza a leitura do caracter
							// novamente, para ver se n�o pertence � outro
							// padr�o

				// ** CASO N�O SEJA SOMENTE UM S�MBOLO E SIM UM CONJUNTO **//
				default:

					// Verifica se � espa�o em branco e ignora, continuando para
					// a pr�xima intera��o do while (checar um novo caracter)
					if (Character.isWhitespace(caracter)) {
						caracter = leitorArquivo.getNextChar();
						break;
					}
					// Verifica se /n e ignora
					if (caracter == Character.LINE_SEPARATOR || caracter == '\n' ) {
						caracter = leitorArquivo.getNextChar();
						break;
					}

					// Verifica se � coment�rio e ignora, continuando para a
					// pr�xima intera��o do while (checar um novo caracter)
					if (caracter == '#') {
						caracter = leitorArquivo.getNextChar();
						continueLeituraComentario();
						caracter = leitorArquivo.getNextChar();
						continue;
					}

					// ** An�lise para n�meros (inteiros ou decimais)
					// Verifica se � um n�mero e faz a an�lise se a continua��o
					// dele � v�lida e retorna o token (float e int)
					if (Character.isDigit(caracter)) {
						int indicePonto = 0, indiceE = 0;
						boolean primeiroLooping = true;
						
						// Enquando n�o sair do padr�o de n�mero, . (somente uma
						// ocorrencia) e E (somente uma ocorrencia)
						while ((Character.isDigit(caracter) || caracter == 'E' || caracter == '+' || caracter == '.')) {
							if (primeiroLooping){
								primeiroLooping = false;
								lexema.append(caracter);
								caracter = leitorArquivo.getNextChar();
							}
								
							if(Character.isDigit(caracter)&&lexema.charAt(lexema.length()-1)!= 'E'){
								lexema.append(caracter);
								caracter = leitorArquivo.getNextChar();
							}
							
							else if(caracter == 'E' && indiceE == 0){
								indiceE++;
								caracter = leitorArquivo.getNextChar();
								if(caracter == '+'){
									lexema.append("E"+caracter);
									caracter = leitorArquivo.getNextChar();
									}else{
									//devolve dos simbolos
									leitorArquivo.reset();
									leitorArquivo.reset();
									return(tabSimbolos.retornaNumero(String.valueOf(lexema), leitorArquivo.getLine(), leitorArquivo.getColumn()));
								}
							}
							
							else if(caracter == '.' && indicePonto == 0){
								indicePonto++;
								caracter = leitorArquivo.getNextChar();
								if (Character.isDigit(caracter)){
									lexema.append("."+caracter);
									caracter = leitorArquivo.getNextChar();
								}else{
									//devolve dos simbolos
									leitorArquivo.reset();
									leitorArquivo.reset();
									return(tabSimbolos.retornaNumero(String.valueOf(lexema), leitorArquivo.getLine(), leitorArquivo.getColumn()));
								}
							}
							else{
								leitorArquivo.reset();
								return(tabSimbolos.retornaNumero(String.valueOf(lexema), leitorArquivo.getLine(), leitorArquivo.getColumn()));
							}
						}
					}
					}

					// ** An�lise para letras (poss�veis palavras reservadas
					if (Character.isLetter(caracter)) {
						lexema.append(caracter);
						while (Character.isDigit(caracter)
								|| Character.isLetter(caracter)
								|| caracter == '_') {
							caracter = leitorArquivo.getNextChar();
							if (Character.isDigit(caracter)
									|| Character.isLetter(caracter)
									|| caracter == '_')
								lexema.append(caracter);
						}
						return tabSimbolos.retornaSalvaPalavra(
								String.valueOf(lexema),
								leitorArquivo.getLine(),
								leitorArquivo.getColumn());
					}

					// Erros gerais n�o pegos anteriormente.
					else {
						errorHandler.setError(leitorArquivo.getLine() + ", "
								+ leitorArquivo.getColumn() + " | Caracter "
								+ caracter + " n�o � v�lido. ");
						caracter = leitorArquivo.getNextChar();
					}
					break;

				}
			
		} catch (EOFException eo) {
			return new Token(1, "EOF", "EOF", leitorArquivo.getLine(),
					leitorArquivo.getColumn());
		}

		catch (IOException io) {
			return new Token(1, "EOF", "EOF", leitorArquivo.getLine(),
					leitorArquivo.getColumn());
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("N�o foi poss�vel recuperar caracter.");
		}
		return null; // caso nada seja encontrado.
	}

	public void continueLeituraComentario() throws EOFException, IOException {
		// enquanto n�o encontrar o fim do coment�rio
		while (true) {

			caracter = leitorArquivo.getNextChar();

			if (caracter == '#')
				break;

		}
	}

	public List<String> retornarErros() {
		return errorHandler.getError();
	}

	public int exponencialEncontrado() throws EOFException, IOException {
		int indiceE = 0;

		caracter = leitorArquivo.getNextChar();

		if (caracter == '+') {
			// lexema.append("E");
			lexema.append(caracter);
			indiceE++;
		} else
			caracter = 'E';

		return indiceE;// E deve pertencer � outra palavra
	}

	public AnLexico(String caminhoArquivo) {
		try {
			this.caminhoArquivo = caminhoArquivo;
			leitorArquivo = new FileHandler(caminhoArquivo);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo n�o encontrado.");
		}
	}

	public AnLexico() {
		super();
	}

}