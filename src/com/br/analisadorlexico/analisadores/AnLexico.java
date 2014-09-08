package com.br.analisadorlexico.analisadores;

import java.util.ArrayList;
import java.util.List;
import com.br.analisadorlexico.componentes.TabSimbolos;
import com.br.analisadorlexico.componentes.Token;

public class AnLexico {
	private List<String> palavrasEncontradas = new ArrayList<String>();
	private List<Token> tokensEncontrados = new ArrayList<Token>();
	private List<String> errosEncontrados = new ArrayList<String>();
	private TabSimbolos tabSimbolos = new TabSimbolos();
	private ErrorHandler erroHandler = new ErrorHandler();
	private String palavraAtual = "";

	public void lerPalavra(char simbolo) {
		// checar se � um espa�o em branco
		if (" ".equals(simbolo)) {
			System.out
					.print("Terminador espa�o em branco encontrado. Analisar pr�ximo token");

			// Checar se a palavra atual n�o � vazia
			if (!palavraAtual.isEmpty()) {
				palavrasEncontradas.add(palavraAtual);
				palavraAtual = "";
			}
		}

		else if (";".equals(simbolo)) {
			// Checar se a palavra atual n�o � vazia
			if (!palavraAtual.isEmpty()) {
				palavrasEncontradas.add(palavraAtual);
				palavraAtual = "";
			}
			palavrasEncontradas.add(";");
		}

		else {
			palavraAtual += simbolo;
		}

	}

	public List<String> getListaPalavras() {
		return palavrasEncontradas;
	}

	public boolean encontreToken() {
		boolean codigoValido = true;
		Token token = new Token();

		for (String simbolo : palavrasEncontradas) {
			token = tabSimbolos.pesquisaPalavra(simbolo);

			// Verifica se um token foi encontrado
			if (token != null)
				tokensEncontrados.add(token);

			// Caso n�o ache o erro, cadastra novo erro
			else
				errosEncontrados.add("Palavra " + simbolo
						+ " � inv�lida nesse formato e na linguagem."
						+ "Coluna: Linha: ");
		}

		if (errosEncontrados.size() != 0) {
			codigoValido = false;
			erroHandler.setError(errosEncontrados);

		}

		return codigoValido;
	}
}