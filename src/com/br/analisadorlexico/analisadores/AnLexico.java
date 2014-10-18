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
	private ArrayList<Character> caractersPendentes = new ArrayList();
	private boolean primeiroCaracter = true;
	private FileHandler leitorArquivo;
	private TabSimbolos tabSimbolos = TabSimbolos.getInstance();
	private ErrorHandler errorHandler = ErrorHandler.getInstance();
	private Token token = new Token();
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
				case '*':
				case '/':
				case ';':
				case '(':
				case ')':
					primeiroCaracter = true;
					return tabSimbolos.pesquisaPalavra(
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
					token = tabSimbolos.pesquisaPalavra(String.valueOf(lexema),
							leitorArquivo.getLine(), leitorArquivo.getColumn());
					if (token == null) {
						// insere na tabela
					}
					return tabSimbolos.pesquisaPalavra(String.valueOf(lexema),
							leitorArquivo.getLine(), leitorArquivo.getColumn());

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
					return tabSimbolos.pesquisaPalavra(String.valueOf(lexema),
							leitorArquivo.getLine(), leitorArquivo.getColumn());

					// verifica operador <-
				case '<':
					caracter = leitorArquivo.getNextChar();
					if (caracter == '-') {
						lexema.append('<');
						lexema.append(caracter);
						caracter = leitorArquivo.getNextChar();
						return tabSimbolos.pesquisaPalavra(
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
						return tabSimbolos.pesquisaPalavra(
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
					if (caracter == Character.LINE_SEPARATOR) {
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
						// Enquando n�o sair do padr�o de n�mero, . (somente uma
						// ocorrencia) e E (somente uma ocorrencia)
						while (Character.isDigit(caracter)
								|| (caracter == '.' && indicePonto <= 1)
								|| (caracter == 'E' && indiceE <= 1)) {
							lexema.append(caracter);
							if (caracter == 'E') {
								indiceE = exponencialEncontrado();
								if (indiceE == 0) // caso + n�o seja encontrado,
									// mantem somente os n�mero
									// e retorna.
									return tabSimbolos.pesquisaPalavra(
											String.valueOf(lexema),
											leitorArquivo.getLine(),
											leitorArquivo.getColumn());// E ir�
																		// para
																		// erro
								// na pr�xima
								// execu��o
							}
							if (caracter == '.') {
								caracter = leitorArquivo.getNextChar();
								if (Character.isDigit(caracter)
										|| caracter == 'E') {
									// verifica se existe um E depois do .
									if (caracter == 'E') {
										caracter = leitorArquivo.getNextChar();
										if (caracter == '+') {
											lexema.append(".E");
											lexema.append(caracter);
											indiceE++;
										} else {
											caracter = 'E';
											return tabSimbolos.pesquisaPalavra(
													String.valueOf(lexema),
													leitorArquivo.getLine(),
													leitorArquivo.getColumn());// E
											// ir�
											// para
											// erro
											// na
											// pr�xima
											// execu��o
										}
									}
									// Somente adiciona o . no lexema
									else {
										lexema.append(".");
										lexema.append(caracter);
										indicePonto++;
									}
								}
								// Seta erro para ".", j� que n�o existem
								// lexemas come�ados com ".".
								else {
									errorHandler
											.setError(leitorArquivo.getLine()
													+ ", "
													+ leitorArquivo.getColumn()
													+ " | O uso de . mais de uma vez para um n�mero � inv�lido.");
									caracter = leitorArquivo.getNextChar();
									return tabSimbolos.pesquisaPalavra(
											String.valueOf(lexema),
											leitorArquivo.getLine(),
											leitorArquivo.getColumn());// . ir�
																		// para
																		// erro
									// na pr�xima
									// execu��o
								}
							}
							// Pede um novo caracter ao arquivo
							caracter = leitorArquivo.getNextChar();
						}
						return tabSimbolos.pesquisaPalavra(
								String.valueOf(lexema),
								leitorArquivo.getLine(),
								leitorArquivo.getColumn());// retorna token e
															// finaliza a
						// execu��o do m�todo
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
						return tabSimbolos.pesquisaPalavra(
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
			}

		} catch (EOFException eo) {
			return tabSimbolos.pesquisaPalavra("eof", leitorArquivo.getLine(),
					leitorArquivo.getColumn());
		}

		catch (IOException io) {
			return tabSimbolos.pesquisaPalavra("eof", leitorArquivo.getLine(),
					leitorArquivo.getColumn());
		} catch (Exception e) {
			System.out.println("N�o foi poss�vel recuperar caracter.");
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

	public int exponencialEncontrado() throws EOFException, IOException { // Verifica
																			// se
																			// depois
																			// do
																			// E,
																			// o
																			// pr�ximo
		// caracter � +. Caso n�o, retorna
		// 0, para o apontamento de um erro
		// e retorna E para que outro lexema
		// possa ser formado.
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