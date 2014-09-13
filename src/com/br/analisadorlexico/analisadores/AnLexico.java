package com.br.analisadorlexico.analisadores;

import com.br.analisadorlexico.componentes.Token;
import com.br.analisadorlexico.leitorarquivo.LeitorDeArquivo;

public class AnLexico {
	private String caminhoArquivo;
	private String lexema;
	private char caracter;
	private LeitorDeArquivo leitorArquivo;

	// Cria inst�ncia da classe do professor

	// private List<String> palavrasEncontradas = new ArrayList<String>();
	// private List<Token> tokensEncontrados = new ArrayList<Token>();
	// private List<String> errosEncontrados = new ArrayList<String>();
	// private TabSimbolos tabSimbolos = new TabSimbolos();
	// private ErrorHandler erroHandler = new ErrorHandler();
	// private String palavraAtual = "";
	//
	// public void lerPalavra(char simbolo) {
	// // checar se � um espa�o em branco
	// if (" ".equals(simbolo)) {
	// System.out
	// .print("Terminador espa�o em branco encontrado. Analisar pr�ximo token");
	//
	// // Checar se a palavra atual n�o � vazia
	// if (!palavraAtual.isEmpty()) {
	// palavrasEncontradas.add(palavraAtual);
	// palavraAtual = "";
	// }
	// }
	//
	// else if (";".equals(simbolo)) {
	// // Checar se a palavra atual n�o � vazia
	// if (!palavraAtual.isEmpty()) {
	// palavrasEncontradas.add(palavraAtual);
	// palavraAtual = "";
	// }
	// palavrasEncontradas.add(";");
	// }
	//
	// else {
	// palavraAtual += simbolo;
	// }
	//
	// }
	//
	// public List<String> getListaPalavras() {
	// return palavrasEncontradas;
	// }
	//
	// public boolean encontreToken() {
	// boolean codigoValido = true;
	// Token token = new Token();
	//
	// for (String simbolo : palavrasEncontradas) {
	// token = tabSimbolos.pesquisaPalavra(simbolo);
	//
	// // Verifica se um token foi encontrado
	// if (token != null)
	// tokensEncontrados.add(token);
	//
	// // Caso n�o ache o erro, cadastra novo erro
	// else
	// errosEncontrados.add("Palavra " + simbolo
	// + " � inv�lida nesse formato e na linguagem."
	// + "Coluna: Linha: ");
	// }
	//
	// if (errosEncontrados.size() != 0) {
	// codigoValido = false;
	// erroHandler.setError(errosEncontrados);
	//
	// }
	//
	// return codigoValido;

	public AnLexico(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;

	}

	public Token nextToken(){
		
		while (true){
			//Chama, da classe do professor, a leitura do arquivo, passando como par�metro o caminho
			caracter = leitorArquivo.retornaChar(caminhoArquivo); //modificar para a classe real.
			
			switch(caracter){
			
			//Verifique se o caracter (que � �nico, � v�lido) 
				case '+':
				case '-':
				case '*':
				case '/':
				case ';':
				case '(':
					return pesquisaToken(String.valueOf(caracter));
					
				default:
					// Verifica se � espa�o em branco
					if (Character.isWhitespace(caracter)){
						continueLeitura(' ');
						continue;
					}
					// Verifica se � coment�rio
					if (caracter == '#'){
						continueLeitura('#');
						continue;
					}
					// Verifica se � um n�mero
					if (Character.isDigit(caracter)){
						int indicePonto = 0,indiceE  = 0;
						boolean valido = true;
						
						while (true){
							lexema += String.valueOf(caracter);
							caracter = leitorArquivo.retornaChar(caminhoArquivo);
							if(caracter == '.'  && indicePonto == 0 ){
								lexema += String.valueOf(caracter);
								indicePonto++;
							}
							if(caracter == 'E' && indiceE == 0 ){
								lexema += String.valueOf(caracter);
								indiceE++;
							}
							if(caracter == '.'  && indicePonto == 1 ){
								//Mando erro para Error Handdler
							}
							if(caracter == 'E' && indiceE == 1 ){
								//Mando erro para Error Handdler
							}
						}
					}
					// Verifica se � uma letra
					if (Character.isLetter(caracter)){
						
					}
					//Verifica se � um "_"
					if (caracter == '_'){
						
					}
			
			}
		}
	}
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

	public Token pesquisaToken(String palavra) {
		return null;
	}

	public void erroEncontrado() {

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

