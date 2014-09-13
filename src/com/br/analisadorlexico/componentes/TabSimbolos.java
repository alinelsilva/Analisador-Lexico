import java.util.ArrayList;


public class TabSimbolos {
	private static TabSimbolos instancia = new TabSimbolos();
	private ArrayList<Token> listaToken = new ArrayList<Token>();
	private ArrayList<Codigo> listaCodigo = new ArrayList<Codigo>();
	
	private TabSimbolos(){ // Iniciliza tokens
		listaToken.add(new Token(1,"NUM_INT",""));
		listaToken.add(new Token(2,"NUM_FLOAT",""));
		listaToken.add(new Token(3,"LITERAL",""));
		listaToken.add(new Token(4,"ID",""));
		listaToken.add(new Token(5,"REL_OP","<"));
		listaToken.add(new Token(6,"REL_OP",">"));
		listaToken.add(new Token(7,"REL_OP","<="));
		listaToken.add(new Token(8,"REL_OP",">="));
		listaToken.add(new Token(9,"REL_OP","="));
		listaToken.add(new Token(10,"REL_OP","<>"));
		listaToken.add(new Token(11,"ADDSUB_OP","-"));
		listaToken.add(new Token(12,"ADDSUB_OP","+"));
		listaToken.add(new Token(13,"MULTDIV_OP","*"));
		listaToken.add(new Token(14,"MULTDIV_OP","/"));
		listaToken.add(new Token(15,"ATTRIB_OP","<-"));
		listaToken.add(new Token(16,"TERM",";"));
		listaToken.add(new Token(17,"L_PAR","("));
		listaToken.add(new Token(18,"R_PAR",")"));
		listaToken.add(new Token(19,"LOGIC_VAL","true"));
		listaToken.add(new Token(20,"LOGIC_VAL","false"));
		listaToken.add(new Token(21,"LOGIC_OP","not"));
		listaToken.add(new Token(22,"LOGIC_OP","and"));
		listaToken.add(new Token(23,"LOGIC_OP","or"));
		listaToken.add(new Token(24,"TYPE","bool"));
		listaToken.add(new Token(25,"TYPE","text"));
		listaToken.add(new Token(26,"TYPE","int"));
		listaToken.add(new Token(27,"TYPE","float"));
		listaToken.add(new Token(28,"PROGRAM","program"));
		listaToken.add(new Token(29,"END_PROG","end_prog"));
		listaToken.add(new Token(30,"BEGIN","begin"));
		listaToken.add(new Token(31,"END","end"));
		listaToken.add(new Token(32,"IF","if"));
		listaToken.add(new Token(33,"THEN","then"));
		listaToken.add(new Token(34,"ELSE","else"));
		listaToken.add(new Token(35,"FOR","for"));
		listaToken.add(new Token(36,"WHILE","while"));
		listaToken.add(new Token(37,"DECLARE","declare"));
		listaToken.add(new Token(38,"TO","to"));
	}
	public String pesquisaPalavra(String token){
		for (Token ltoken : listaToken) 			// Pega token por token
			if(ltoken.getLexema().equals(token))	// Verifica se o token existe
				return ltoken.getToken(); 			// Se existe, retorna o token
		return null; 								// Se nao, retorna null
	}
	
	public TabSimbolos getInstance(){
		return instancia;
	}
	
	public void gravaCodigo(int linha, int coluna, String token){
		listaCodigo.add(new Codigo(linha, coluna, token));
	}
}
