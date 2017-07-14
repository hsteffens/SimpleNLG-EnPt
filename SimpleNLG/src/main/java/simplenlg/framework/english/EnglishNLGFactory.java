package simplenlg.framework.english;

import java.util.Arrays;
import java.util.List;

import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.english.EnglishSPhraseSpec;

public class EnglishNLGFactory extends NLGFactory{
	
	
	public EnglishNLGFactory() {
		super();
	}
	
	public EnglishNLGFactory(Lexicon newLexicon) {
		super(newLexicon);
	}

	/** The list of English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> PRONOUNS               = Arrays.asList("I",
	                                                                         "you",
	                                                                         "he",
	                                                                         "she",
	                                                                         "it",
	                                                                         "me",
	                                                                         "you",
	                                                                         "him",
	                                                                         "her",
	                                                                         "it",
	                                                                         "myself",
	                                                                         "yourself",
	                                                                         "himself",
	                                                                         "herself",
	                                                                         "itself",
	                                                                         "mine",
	                                                                         "yours",
	                                                                         "his",
	                                                                         "hers",
	                                                                         "its",
	                                                                         "we",
	                                                                         "you",
	                                                                         "they",
	                                                                         "they",
	                                                                         "they",
	                                                                         "us",
	                                                                         "you",
	                                                                         "them",
	                                                                         "them",
	                                                                         "them",
	                                                                         "ourselves",
	                                                                         "yourselves",
	                                                                         "themselves",
	                                                                         "themselves",
	                                                                         "themselves",
	                                                                         "ours",
	                                                                         "yours",
	                                                                         "theirs",
	                                                                         "theirs",
	                                                                         "theirs",
	                                                                         "there");

	/** The list of first-person English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> FIRST_PRONOUNS         = Arrays.asList("I",
	                                                                         "me",
	                                                                         "myself",
	                                                                         "we",
	                                                                         "us",
	                                                                         "ourselves",
	                                                                         "mine",
	                                                                         "my",
	                                                                         "ours",
	                                                                         "our");

	/** The list of second person English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> SECOND_PRONOUNS        = Arrays.asList("you",
	                                                                         "yourself",
	                                                                         "yourselves",
	                                                                         "yours",
	                                                                         "your");

	/** The list of reflexive English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> REFLEXIVE_PRONOUNS     = Arrays.asList("myself",
	                                                                         "yourself",
	                                                                         "himself",
	                                                                         "herself",
	                                                                         "itself",
	                                                                         "ourselves",
	                                                                         "yourselves",
	                                                                         "themselves");

	/** The list of masculine English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> MASCULINE_PRONOUNS     = Arrays.asList("he", "him", "himself", "his");

	/** The list of feminine English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> FEMININE_PRONOUNS      = Arrays.asList("she", "her", "herself", "hers");

	/** The list of possessive English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> POSSESSIVE_PRONOUNS    = Arrays.asList("mine",
	                                                                         "ours",
	                                                                         "yours",
	                                                                         "his",
	                                                                         "hers",
	                                                                         "its",
	                                                                         "theirs",
	                                                                         "my",
	                                                                         "our",
	                                                                         "your",
	                                                                         "her",
	                                                                         "their");

	/** The list of plural English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> PLURAL_PRONOUNS        = Arrays.asList("we",
	                                                                         "us",
	                                                                         "ourselves",
	                                                                         "ours",
	                                                                         "our",
	                                                                         "they",
	                                                                         "them",
	                                                                         "theirs",
	                                                                         "their");

	/** The list of English pronouns that can be singular or plural. */
	@SuppressWarnings("nls")
	private static final List<String> EITHER_NUMBER_PRONOUNS = Arrays.asList("there");

	/** The list of expletive English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> EXPLETIVE_PRONOUNS     = Arrays.asList("there");
	
	public List<String> getPronouns() {
		return PRONOUNS;
	}
	
	public List<String> getFirstPronouns() {
		return FIRST_PRONOUNS;
	}
	
	public List<String> getSecondPronouns() {
		return SECOND_PRONOUNS;
	}
	
	public List<String> getReflexivePronouns() {
		return REFLEXIVE_PRONOUNS;
	}
	
	public List<String> getMasculinePronouns() {
		return MASCULINE_PRONOUNS;
	}
	
	public List<String> getFemininePronouns() {
		return FEMININE_PRONOUNS;
	}
	
	public List<String> getPossessivePronouns() {
		return POSSESSIVE_PRONOUNS;
	}
	
	public List<String> getPluralPronouns() {
		return PLURAL_PRONOUNS;
	}
	
	public List<String> getEitherNumberPronouns() {
		return EITHER_NUMBER_PRONOUNS;
	}
	
	public List<String> getExpletivePronouns() {
		return EXPLETIVE_PRONOUNS;
	}

	@Override
	public AbstractCoordinatedPhraseElement createCoordinatedPhrase() {
		return new EnglishCoordinatedPhraseElement();
	}
	
	/**
	 * Creates a new coordinated phrase with two elements (initially)
	 * 
	 * @param coord1
	 *            - first phrase to be coordinated
	 * @param coord2
	 *            = second phrase to be coordinated
	 * @return <code>CoordinatedPhraseElement</code> for the two given elements
	 */
	@Override
	public AbstractCoordinatedPhraseElement createCoordinatedPhrase(Object coord1, Object coord2) {
		return new EnglishCoordinatedPhraseElement(coord1, coord2);
	}

	@Override
	public AbstractSPhraseSpec getSPhraseSpec() {
		return new EnglishSPhraseSpec(this);
	}
	
	/**
	 * Creates a clause with the given subject, verb or verb phrase and direct
	 * object but no indirect object.
	 * 
	 * @param subject
	 *            the subject for the clause as a <code>NLGElement</code> or
	 *            <code>String</code>. This forms a noun phrase.
	 * @param verb
	 *            the verb for the clause as a <code>NLGElement</code> or
	 *            <code>String</code>. This forms a verb phrase.
	 * @param directObject
	 *            the direct object for the clause as a <code>NLGElement</code>
	 *            or <code>String</code>. This forms a complement for the
	 *            clause.
	 * @return a <code>SPhraseSpec</code> representing this phrase.
	 */
	public AbstractSPhraseSpec createClause(Object subject, Object verb, Object directObject) {

		AbstractSPhraseSpec phraseElement = getSPhraseSpec();
		
		if(verb != null) {
			// AG: fix here: check if "verb" is a VPPhraseSpec or a Verb
			if(verb instanceof PhraseElement) {
				phraseElement.setVerbPhrase((PhraseElement) verb);
			} else {
				phraseElement.setVerb(verb);
			}
		}

		if(subject != null){
			phraseElement.setSubject(subject);
		}
		
		if(directObject != null) {
			phraseElement.setObject(directObject);
		}

		return phraseElement;
	}
}
