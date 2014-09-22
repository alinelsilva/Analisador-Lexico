package com.br.analisadorlexico.analisadores;

import java.io.FileNotFoundException;
import java.util.List;

import com.br.analisadorlexico.componentes.TabSimbolos;
import com.br.analisadorlexico.componentes.Token;
import com.br.analisadorlexico.leitorarquivo.FileHandler;


public class AnLexico {

	private String lexema;
	private char caracter;
	private boolean primeiroCaracter = true;
	private FileHandler leitorArquivo;
	private TabSimbolos tabSimbolos = TabSimbolos.getInstance();
    private ErrorHandler errorHandler = ErrorHandler.getInstance();
    private Token token = new Token();  
	public Token nextToken() {
		try{
		
			if (primeiroCaracter){
				primeiroCaracter = false;
				// Chama, da classe do professor, a leitura do arquivo, passando
				// como par�metro o caminho
				caracter = leitorArquivo.getNextChar();
			}
			 
		while (true) {
			
			lexema = "";
			
			switch (caracter) {
			// Verifique se o caracter (que � �nico, � v�lido)
			case '+':
			case '-':
			case '*':
			case '/':
			case ';':
			case '(':
			case ')':
				return tabSimbolos.pesquisaPalavra(String.valueOf(caracter));
			
				//Verifica _ se � segudo pelos valores aceitos na linguagem
			case '_':			
				lexema += String.valueOf(caracter);
				
				while(Character.isDigit(caracter) || Character.isLetter(caracter) || caracter == '_'){
					
					caracter = leitorArquivo.getNextChar();
					lexema += caracter;
				}
				token = tabSimbolos.pesquisaPalavra(lexema);
				if(token == null){
					//insere na tabela
				}
				return tabSimbolos.pesquisaPalavra(lexema);
			
			case '\'':
				lexema += caracter;
				caracter = leitorArquivo.getNextChar();
				while(caracter != '\''){
					lexema += caracter;
					caracter = leitorArquivo.getNextChar();
				}
				if(caracter == '\''){
					lexema += caracter;
					caracter = leitorArquivo.getNextChar();
				}
				return tabSimbolos.pesquisaPalavra(lexema);
				
			case '<':
				caracter = leitorArquivo.getNextChar();
				if (caracter == '-'){
					lexema += '<'+caracter;
					return tabSimbolos.pesquisaPalavra(lexema);
				}
				else
					errorHandler.setError("O caracter < deveria ser seguido por \"-\". \n Erro: Linha - "+leitorArquivo.getColumn()+" Coluna - "+leitorArquivo.getLine());
				break;
				
				//Verifica��o para operadores
			case '&':
				lexema += caracter;
				while (caracter !='&' && (caracter =='<' || caracter =='>' || caracter =='=') ){
					caracter = leitorArquivo.getNextChar();
					lexema += caracter;
				}
				caracter = leitorArquivo.getNextChar();
				//teste para os lexemas v�lidos. Caso n�o seja
				if(lexema == "&<&" || lexema == "&>&" || lexema == "&>=&" || lexema == "&<=&" || lexema == "&=&" || lexema == "&<>&" )
					return tabSimbolos.pesquisaPalavra(lexema);
				else{
					errorHandler.setError("Operador " +lexema+ " n�o � v�lido. \n Erro: Linha - "+leitorArquivo.getColumn()+" Coluna - "+leitorArquivo.getLine());
				}
				break; // em caso de erro, realiza a leitura do caracter novamente, para ver se n�o pertence � outro padr�o

				//** CASO N�O SEJA SOMENTE UM S�MBOLO E SIM UM CONJUNTO **//
			default:
				
				// Verifica se � espa�o em branco e ignora, continuando para a pr�xima intera��o do while (checar um novo caracter)
				if (Character.isWhitespace(caracter)) {
					caracter = leitorArquivo.getNextChar();
					break;
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

					//Enquando n�o sair do padr�o de n�mero, . (somente uma ocorrencia) e E (somente uma ocorrencia)
					while (Character.isDigit(caracter) || (caracter == '.' && indicePonto <= 1 ) || (caracter == 'E'&& indiceE <= 1)) {
						lexema += String.valueOf(caracter);
							if (caracter == 'E'){
								indiceE = exponencialEncontrado();
								if(indiceE == 0)
									return tabSimbolos.pesquisaPalavra(lexema);//E ir� para erro na pr�xima execu��o	
							}
							
							if(caracter == '.'){
								caracter = leitorArquivo.getNextChar();
								
								if(Character.isDigit(caracter) || caracter == 'E'){
									
									if (caracter == 'E'){
										indiceE = exponencialEncontrado();
										if(indiceE == 0)
										return tabSimbolos.pesquisaPalavra(lexema);//E ir� para erro na pr�xima execu��o
									}
									
									else{
									lexema += "."+String.valueOf(caracter);	
									indicePonto++;
									}
									
								}
								else{
									caracter = '.';
									return tabSimbolos.pesquisaPalavra(lexema);//. ir� para erro na pr�xima execu��o	
								}
							}
						// Pede um novo caracter ao arquivo
						caracter = leitorArquivo.getNextChar();
					}
					return tabSimbolos.pesquisaPalavra(lexema);// retorna token e finaliza a execu��o do m�todo
						
				}
				
				//** An�lise para n�meros (inteiros ou decimais)
				if (Character.isLetter(caracter)) {
					lexema += caracter;
					while (Character.isDigit(caracter) && Character.isLetter(caracter) && caracter == '_' ){
						caracter = leitorArquivo.getNextChar();
						lexema += caracter;
					}
					token = tabSimbolos.pesquisaPalavra(lexema);
					if(token == null){
						//insere na tabela
					}
					return tabSimbolos.pesquisaPalavra(lexema);
				}
				
				else{
					errorHandler.setError("Caracter " +caracter+ " n�o � v�lido. \n Erro: Linha - "+leitorArquivo.getColumn()+" Coluna - "+leitorArquivo.getLine());
					caracter = leitorArquivo.getNextChar();
				}
				break;

				}
			}
		}catch(Exception e){
				System.out.println("N�o foi poss�vel recuperar caracter.");
			}
		return null; //caso nada seja encontrado.
	}
		

	public AnLexico(String caminhoArquivo) {
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
			
			try{
				caracter = leitorArquivo.getNextChar();
				
				if (caracter == '#') 
					break;
				
			}catch(Exception e){
				System.out.println("N�o foi poss�vel recuperar caracter. ");
			}
		}
	}
	public List <String> retornarErros(){
		return errorHandler.getErrors();
	}
	public int exponencialEncontrado(){
		int indiceE = 0;
		try {
			caracter = leitorArquivo.getNextChar();
		} catch (Exception e) {
			System.out.println("N�o foi poss�vel recuperar caracter. ");
		} 
		if(caracter == '+'){
			lexema += "E"+String.valueOf(caracter);	
			indiceE++;
		}
		else
			caracter = 'E';
		
		return indiceE;//E deve pertencer � outra palavra
	}

}
