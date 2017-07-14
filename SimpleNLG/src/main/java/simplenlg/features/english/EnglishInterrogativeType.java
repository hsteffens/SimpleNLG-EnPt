package simplenlg.features.english;

import simplenlg.features.IInterrogativeType;

public enum EnglishInterrogativeType implements IInterrogativeType {

	/**
	 * The type of interrogative relating to the manner in which an event
	 * happened. For example, <em>John kissed Mary</em> becomes
	 * <em>How did John kiss
	 * Mary?</em>
	 */
	HOW("how"),
	
	/**
	 * A how question related to a predicative sentence, such as <i>John is fine</i>, which becomes <i>How is John?</i> 
	 */
	HOW_PREDICATE("how"),

	/**
	 * This type of interrogative is a question pertaining to the object of a
	 * phrase. For example, <em>John bought a horse</em> becomes <em>what did 
	 * John buy?</em> while <em>John gave Mary a flower</em> becomes
	 * <em>What did 
	 * John give Mary?</em>
	 */
	WHAT_OBJECT("what"),

	/**
	 * This type of interrogative is a question pertaining to the subject of a
	 * phrase. For example, <em>A hurricane destroyed the house</em> becomes
	 * <em>what destroyed the house?</em>
	 */
	WHAT_SUBJECT("what"),

	/**
	 * This type of interrogative concerns the object of a verb that is to do
	 * with location. For example, <em>John went to the beach</em> becomes
	 * <em>Where did John go?</em>
	 */
	WHERE("where"),

	/**
	 * This type of interrogative is a question pertaining to the indirect
	 * object of a phrase when the indirect object is a person. For example,
	 * <em>John gave Mary a flower</em> becomes
	 * <em>Who did John give a flower to?</em>
	 */
	WHO_INDIRECT_OBJECT("who"),

	/**
	 * This type of interrogative is a question pertaining to the object of a
	 * phrase when the object is a person. For example,
	 * <em>John kissed Mary</em> becomes <em>who did John kiss?</em>
	 */
	WHO_OBJECT("who"),

	/**
	 * This type of interrogative is a question pertaining to the subject of a
	 * phrase when the subject is a person. For example,
	 * <em>John kissed Mary</em> becomes <em>Who kissed Mary?</em> while
	 * <em>John gave Mary a flower</em> becomes <em>Who gave Mary a flower?</em>
	 */
	WHO_SUBJECT("who"),

	/**
	 * The type of interrogative relating to the reason for an event happening.
	 * For example, <em>John kissed Mary</em> becomes <em>Why did John kiss
	 * Mary?</em>
	 */
	WHY("why"),

	/**
	 * This represents a simple yes/no questions. So taking the example phrases
	 * of <em>John is a professor</em> and <em>John kissed Mary</em> we can
	 * construct the questions <em>Is John a professor?</em> and
	 * <em>Did John kiss Mary?</em>
	 */
	YES_NO("yes/no"),

	/**
	 * This represents a "how many" questions. For example of
	 * <em>dogs chased John/em> becomes <em>How many dogs chased John</em>
	 */
	HOW_MANY("how many");
	
	private String question;
	
	private EnglishInterrogativeType(String question){
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

