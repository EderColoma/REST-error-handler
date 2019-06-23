package br.com.emc.exception_handler;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

/**
 * Resposavel pela conversão dos tipos Java em JSON. 
 */
class LowerCaseClassNameResolver extends TypeIdResolverBase {

	/**
	 * Converte o nome da classe do objeto passado como parâmetro em letras minúsculas.
	 * @param value {@link Object} do qual se deseja obter o nome da classe em letras minúsculas.  
	 * @return O nome da classe do objeto passado como parâmetro em letras minúsculas. 
	 */
    @Override
    public String idFromValue(Object value) {
        return value.getClass().getSimpleName().toLowerCase();
    }

    /**
     * Determina o tipo à partir da combinação de valor e tipo, 
     * usando o tipo sugerido (fornecido pelo serializador)
     * e possivelmente o valor desse tipo.
     * 
	 * @param value {@link Object} do qual se deseja obter o tipo.
	 * @param suggestedType {@link Object} Tipo sugerido para o {@link Object}.  
	 * @return O nome da classe do {@link Object}. 
	 */
    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    /**
	 * Determina o tipo JSON à partir do tipo do objeto.
	 * @return O tipo JSON. 
	 */
    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}