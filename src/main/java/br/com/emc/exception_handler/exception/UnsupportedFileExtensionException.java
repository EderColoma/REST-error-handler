package br.com.emc.exception_handler.exception;

/**
 * Classe responsável por propagar a exceção quando uma extensão não suportada de arquivo é solicitada.  
 */
public class UnsupportedFileExtensionException extends Exception {

	private static final long serialVersionUID = -1876570110303640802L;

	/**
	 * Construtor padrão.
	 * 
	 * @param extension Extensão requisitada.
	 */
	public UnsupportedFileExtensionException(String extension) {
        super(UnsupportedFileExtensionException.generateMessage(extension));
    }
    
	/**
	 * Cria uma mensagem com as informações da exceção.
	 * @param extension Extensão requisitada.
	 */
    private static String generateMessage(String extension) {
        return "Unsupported file extension: " + extension;
    }
	
}
