package com.br.analisadorlexico.analisadores;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.br.analisadorlexico.componentes.Token;

public class AnSintatico {
		
	public static void main(String[] args) {
		String caminho = JOptionPane
				.showInputDialog("Digite o caminho do arquivo");
		AnLexico analisadorLexico = new AnLexico(caminho);
		List<Token> listaTokens = new ArrayList<Token>();
		Token tk;
		int contador = 0;
		
		System.out.println("TOKENS ENCONTRADOS:");
		System.out.println("ORDEM | TOKEN | LEXEMA | POSI��O (lin, col)");
		
		do {
			tk = new Token();
			tk = analisadorLexico.nextToken();
			if(tk == null){
				//N�o exibe nada
			}
			else{
				listaTokens.add(tk);
				System.out.println(contador + " | "+tk.getToken()+" | "+tk.getLexema()+" | "+(tk.getLinha()+", "+tk.getColuna()));
			}

		} while (tk.getToken()!="EOF");
		System.out.println("");
		
		System.out.println("RELAT�RIO DE ERROS:");
		System.out.println("POSI��O (lin, col) | MENSAGEM ");
		
		for(String erro : analisadorLexico.retornarErros())
			System.out.println(erro);
		
		if(analisadorLexico.retornarErros().size() == 0)
			System.out.println("Nenhum erro l�xico foi encontrado.");
		
		System.out.println("");
		
		System.out.println("ESTADO DA TABELA DE S�MBOLOS:");
		System.out.println("ORDEM | TOKEN | LEXEMA | POSI��O FINAL (lin, col) ");
		//retorna tabela de simbolos - Alvinho
		
		
	}
}
