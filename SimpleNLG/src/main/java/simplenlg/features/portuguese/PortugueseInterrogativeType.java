package simplenlg.features.portuguese;

import simplenlg.features.IInterrogativeType;

/**
 * Enum contendo os tipos de perguntas suportados em português.
 * 
 * @author Hélinton P. Steffens
 * @Jul 13, 2017
 */
public enum PortugueseInterrogativeType implements IInterrogativeType {

		/**
		 * Tipo de interrogativo relativo à maneira em que um evento aconteçe. 
		 * Por exemplo: </em> Como eu irei para a aula.
		 */
		HOW("como"),
		
		/**
		 * Uma questão com como relacionada ao predicato da sentença, dada a sentença <i> João é legal</i>, se tornaria <i>Como João é?</i>
		 */
		HOW_PREDICATE("como"),

		/**
		 * Tipo interrogativo relativo ao objeto de uma frase.
		 * Por exemplo, <em>Uma bola é um brinquedo</em> se torna <em>Que é uma bola?</em>
		 */
		WHAT_OBJECT("que"),

		/**
		 * Tipo interrogativo relativo ao sujeito de uma frase. 
		 * Por exemplo, <em>Uma bola é um brinquedo</em> se torna <em>Que é um brinquedo?</em>
		 */
		WHAT_SUBJECT("que"),

		/**
		 * Tipo de interrogativa que diz respeito ao objeto de um verbo que se relaciona com a localização. 
		 * Por exemplo, <em>Eu estou em casa</em> se torna
		 * <em>Onde eu estou?</em>
		 */
		WHERE("onde"),

		/**
		 * Tipo de interrogativa que é uma questão relativa ao objeto indireto de uma frase quando o objeto indireto é uma pessoa. 
		 * Por exemplo, <em>O homem dá a flor do John para a mulher</em> se torna
		 * <em>Para quem o homem dá a flor do John?</em>
		 */
		WHO_INDIRECT_OBJECT("quem"),

		/**
		 * Tipo de interrogativa que é uma questão relativa ao objeto de uma frase quando o objeto é uma pessoa. 
		 * Por exemplo, <em>João beijou Maria</em> se torna <em>Quem João beijou?</em>
		 */
		WHO_OBJECT("quem"),

		/**
		 * Tipo de interrogativa que é uma questão relativa ao sujeito de uma frase quando o sujeito é uma pessoa. 
		 * Por exemplo, <em>João beijou Maria</em> se torna <em>Quem beijou Maria?</em>
		 */
		WHO_SUBJECT("quem"),

		/**
		 * Tipo de interrogativa que é relativa a razão para um evento ter acontencido.
		 * Por exemplo, <em>João beijou Maria</em> se torna <em>Por que João beijou Maria?</em>
		 */
		WHY("por que"),

		/**
		 * Tipo de interrogativa que represente uma questão de sim ou não. 
		 * Por exemplo,  <em>João beijou Maria</em> se torna <em>João beijou Maria?</em>
		 */
		YES_NO("sim/não"),

		/**
		 * Tipo de interrogativa que represente uma questão quantitativa. 
		 * Por exemplo <em>cachorros perseguem João</em> se torna <em>Quantos cachorros perseguem João</em>
		 */
		HOW_MANY("quanto");
		
		private String question;
		
		private PortugueseInterrogativeType(String question){
			this.question = question;
		}

		public IInterrogativeType getInstance(String value) {
			if (this.getQuestion().equalsIgnoreCase(value)) {
				return this;
			}
			return null;
		}

		public String getQuestion() {
			return question;
		}

		/**
		 * A method to determine if the {@code InterrogativeType} is a question
		 * concerning an element with the discourse function of an object.
		 * 
		 * @param type
		 *            the interrogative type to be checked
		 * @return <code>true</code> if the type concerns an object,
		 *         <code>false</code> otherwise.
		 */
		public boolean isObject(Object type) {
			return WHO_OBJECT.equals(type) || WHAT_OBJECT.equals(type);
		}
		
		/**
		 * A method to determine if the {@code InterrogativeType} is a question
		 * concerning an element with the discourse function of an indirect object.
		 * 
		 * @param type
		 *            the interrogative type to be checked
		 * @return <code>true</code> if the type concerns an indirect object,
		 *         <code>false</code> otherwise.
		 */
		public boolean isIndirectObject(Object type) {
			return WHO_INDIRECT_OBJECT.equals(type);
		}

	}

