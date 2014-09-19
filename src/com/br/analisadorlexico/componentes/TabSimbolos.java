import java.util.ArrayList;


public class TabSimbolos {
	private static TabSimbolos instancia = new TabSimbolos();
	private ArrayList<Token> listaToken = new ArrayList<Token>();
	private ArrayList<Codigo> listaCodigo = new ArrayList<Codigo>();
	
	private TabSimbolos(){ // Iniciliza tokens
		listaToken.add(new Token(1,"NUM_INT","inteiro"));
		listaToken.add(new Token(2,"NUM_FLOAT","float"));
		listaToken.add(new Token(3,"LITERAL","literal"));
		listaToken.add(new Token(4,"ID","variavel"));
		listaToken.add(new Token(5,"REL_OP","<"));
		listaToken.add(new Token(5,"REL_OP",">"));
		listaToken.add(new Token(5,"REL_OP","<="));
		listaToken.add(new Token(5,"REL_OP",">="));
		listaToken.add(new Token(5,"REL_OP","="));
		listaToken.add(new Token(5,"REL_OP","<>"));
		listaToken.add(new Token(6,"ADDSUB_OP","-"));
		listaToken.add(new Token(6,"ADDSUB_OP","+"));
		listaToken.add(new Token(7,"MULTDIV_OP","*"));
		listaToken.add(new Token(7,"MULTDIV_OP","/"));
		listaToken.add(new Token(8,"ATTRIB_OP","<-"));
		listaToken.add(new Token(9,"TERM",";"));
		listaToken.add(new Token(10,"L_PAR","("));
		listaToken.add(new Token(11,"R_PAR",")"));
		listaToken.add(new Token(12,"LOGIC_VAL","true"));
		listaToken.add(new Token(12,"LOGIC_VAL","false"));
		listaToken.add(new Token(13,"LOGIC_OP","not"));
		listaToken.add(new Token(13,"LOGIC_OP","and"));
		listaToken.add(new Token(13,"LOGIC_OP","or"));
		listaToken.add(new Token(14,"TYPE","bool"));
		listaToken.add(new Token(14,"TYPE","text"));
		listaToken.add(new Token(14,"TYPE","int"));
		listaToken.add(new Token(14,"TYPE","float"));
		listaToken.add(new Token(15,"PROGRAM","program"));
		listaToken.add(new Token(16,"END_PROG","end_prog"));
		listaToken.add(new Token(17,"BEGIN","begin"));
		listaToken.add(new Token(18,"END","end"));
		listaToken.add(new Token(19,"IF","if"));
		listaToken.add(new Token(20,"THEN","then"));
		listaToken.add(new Token(21,"ELSE","else"));
		listaToken.add(new Token(22,"FOR","for"));
		listaToken.add(new Token(23,"WHILE","while"));
		listaToken.add(new Token(24,"DECLARE","declare"));
		listaToken.add(new Token(25,"TO","to"));
	}
	public Token pesquisaPalavra(String token){
		
		if(!isInt(token.substring(0,1) && token.charAt(0) <> '\"'))
			token = "variavel";
		
		if(token.charAt(0) == '\"')
			token = "literal";			// Retorna LITERAL se comecar com aspas
		
		if(isInt(token))
			token = "inteiro";			// Retorna NUM_INT primeiro
										// Retornar NUM_FLOAT primeiro vai dar errado
		if(isFloat(token))
			token = "float";			// Retorna NUM_FLOAT se for float
		
		for (Token ltoken : listaToken) 			// Pega token por token
			if(ltoken.getLexema().equals(token))	// Verifica se o token existe
				return ltoken; 			// Se existe, retorna o token

		return null; 			// Para qualquer outra coisa, retorna null
	}
	
	public TabSimbolos getInstance(){
		return instancia;
	}
	
	public void gravaCodigo(int linha, int coluna, String token, String lexema){
		listaCodigo.add(new Codigo(linha, coluna, token, lexema));
	}
	
    public static boolean isInt(String texto) { // Verifica se 'e INT
        try {
            Integer.parseInt(texto);  
            return true;  
        } catch (NumberFormatException err) {  
            return false;  
        }  
    }
    
    public static boolean isFloat(String texto) { // Verifica se 'e FLOAT
        try {  
            Float.parseFloat(texto);  
            return true;  
        } catch (NumberFormatException err) {  
            return false;  
        }  
    }
}
