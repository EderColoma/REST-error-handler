package br.com.emc.exception_handler.exception;

/**
 * Classe responsável por propagar a exceção quando uma operação não retorna resultados.  
 */
public class NoResultsException extends Exception {

	private static final long serialVersionUID = 6887246230859892702L;

	/**
	 * Construtor padrão.
	 * 
	 * @param searchParameter Parâmetros utilizados na operação.
	 */
	public NoResultsException(String searchParameter) {
        super(NoResultsException.generateMessage(searchParameter));
    }
    
	/**
	 * Cria uma mensagem com as informações da exceção.
	 * @param searchParameter Parâmetros utilizados na operação.
	 */
    private static String generateMessage(String searchParameter) {
        return "No results were found for " + searchParameter;
    }
}