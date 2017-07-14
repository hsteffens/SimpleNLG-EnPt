package simplenlg.framework.portuguese;

import java.util.Arrays;
import java.util.List;

import simplenlg.features.Feature;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.AdjPhraseSpec;
import simplenlg.phrasespec.portuguese.PortugueseSPhraseSpec;

/**
 * Esta classe contem métodos para criar frases sintáticas em Português.
 * 
 * @author Hélinton P. Steffens
 * @Jul 13, 2017
 */
public class PortugueseNLGFactory extends NLGFactory{

	public PortugueseNLGFactory() {
		super();
	}
	
	public PortugueseNLGFactory(Lexicon newLexicon) {
		super(newLexicon);
	}
	
	
	/** The list of English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> PRONOUNS               = Arrays.asList("eu",
	                                                                         "me",
	                                                                         "nós",
	                                                                         "tu",
	                                                                         "você",
	                                                                         "vós",
	                                                                         "vocês",
	                                                                         "ele",
	                                                                         "eles",
	                                                                         "ela",
	                                                                         "elas",
	                                                                         "nós",
	                                                                         "a mim",
	                                                                         "para mim",
	                                                                         "a nós",
	                                                                         "para nós",
	                                                                         "à gente",
	                                                                         "para a gente",
	                                                                         "para ti",
	                                                                         "para si",
	                                                                         "para vocês",
	                                                                         "a ele",
	                                                                         "para ele",
	                                                                         "a eles",
	                                                                         "para eles",
	                                                                         "a ela",
	                                                                         "para ela",
	                                                                         "a elas",
	                                                                         "para elas",
	                                                                         "comigo",
	                                                                         "conosco",
	                                                                         "contigo",
	                                                                         "consigo",
	                                                                         "convosco",
	                                                                         "com ele",
	                                                                         "com eles",
	                                                                         "com ela",
	                                                                         "com elas",
	                                                                         "a mim",
	                                                                         "meu",
	                                                                         "minha",
	                                                                         "meus",
	                                                                         "minhas",
	                                                                         "teu",
	                                                                         "tua",
	                                                                         "teus",
	                                                                         "tuas",
	                                                                         "seu",
	                                                                         "sua",
	                                                                         "seus",
	                                                                         "suas",
	                                                                         "nosso",
	                                                                         "nossa",
	                                                                         "nossos",
	                                                                         "nossas",
	                                                                         "vosso",
	                                                                         "vossa",
	                                                                         "vossos",
	                                                                         "vossas",
	                                                                         "sua",
	                                                                         "seus",
	                                                                         "seu",
	                                                                         "suas");

	

	/** The list of first-person English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> FIRST_PRONOUNS         = Arrays.asList("eu",
	                                                                         "nós",
	                                                                         "a gente",
	                                                                         "me",
	                                                                         "nos",
	                                                                         "a mim",
	                                                                         "para mim",
	                                                                         "a nós",
	                                                                         "para nós",
	                                                                         "à gente",
	                                                                         "para a gente",
	                                                                         "comigo",
	                                                                         "conosco",
	                                                                         "para nós",
	                                                                         "meu",
	                                                                         "minha",
	                                                                         "meus",
	                                                                         "minhas",
	                                                                         "nosso",
	                                                                         "nossa",
	                                                                         "nossos",
	                                                                         "nossas");

	/** The list of second person English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> SECOND_PRONOUNS        = Arrays.asList("tu",
	                                                                         "vós",
	                                                                         "vocês",
	                                                                         "para ti",
	                                                                         "para si",
	                                                                         "para vocês",
	                                                                         "contigo",
	                                                                         "consigo",
	                                                                         "convosco",
	                                                                         "teu",
	                                                                         "tua",
	                                                                         "teus",
	                                                                         "tuas",
	                                                                         "vosso",
	                                                                         "vossa",
	                                                                         "vossos",
	                                                                         "vossas");
	
	/** The list of reflexive English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> REFLEXIVE_PRONOUNS     = Arrays.asList("me",
	                                                                         "ti",
	                                                                         "si",
	                                                                         "consigo",
	                                                                         "nos",
	                                                                         "vos",
	                                                                         "comigo",
	                                                                         "contigo",
	                                                                         "convosco",
	                                                                         "conosco");

	/** The list of masculine English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> MASCULINE_PRONOUNS     = Arrays.asList("ele", 
																			 "eles",
																			 "lhe", 
																			 "a ele", 
																			 "para ele", 
																			 "lhes", 
																			 "a eles", 
																			 "para eles", 
																			 "com ele", 
																			 "seu", 
																			 "seus", 
																			 "vosso", 
																			 "vossos", 
																			 "meu", 
																			 "seu", 
																			 "nosso", 
																			 "seu", 
																			 "meus", 
																			 "teus", 
																			 "nossos", 
																			 "seus",
																			 "com eles");
	
	/** The list of feminine English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> FEMININE_PRONOUNS      = Arrays.asList("ela", 
																			 "elas", 
																			 "a ela", 
																			 "para ela", 
																			 "a ela", 
																			 "a elas", 
																			 "para elas", 
																			 "com ela", 
																			 "a ela", 
																			 "com elas", 
																			 "a ela", 
																			 "minha", 
																			 "tua", 
																			 "sua", 
																			 "nossa", 
																			 "vossa", 
																			 "sua", 
																			 "minhas", 
																			 "tuas", 
																			 "suas", 
																			 "nossas", 
																			 "vossas", 
																			 "suas");


	
	/** The list of possessive English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> POSSESSIVE_PRONOUNS    = Arrays.asList("seu", 
																			 "seus", 
																			 "vosso", 
																			 "vossos", 
																			 "meu", 
																			 "seu", 
																			 "nosso", 
																			 "seu", 
																			 "meus", 
																			 "teus", 
																			 "nossos", 
																			 "seus",
																			 "minha", 
																			 "tua", 
																			 "sua", 
																			 "nossa", 
																			 "vossa", 
																			 "sua", 
																			 "minhas", 
																			 "tuas", 
																			 "suas", 
																			 "nossas", 
																			 "vossas", 
																			 "suas");

	/** The list of plural English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> PLURAL_PRONOUNS        = Arrays.asList("nós",
	                                                                         "a gente",
	                                                                         "a nós",
	                                                                         "para nós",
	                                                                         "à gente",
	                                                                         "para a gente",
	                                                                         "conosco",
	                                                                         "vós",
	                                                                         "vocês",
	                                                                         "para vocês",
	                                                                         "convosco",
	                                                                         "eles",
	                                                                         "lhes",
	                                                                         "a eles",
	                                                                         "para eles",
	                                                                         "com eles",
	                                                                         "elas",
	                                                                         "a elas",
	                                                                         "para elas",
	                                                                         "com elas");

	
	/** The list of English pronouns that can be singular or plural. */
	@SuppressWarnings("nls")
	private static final List<String> EITHER_NUMBER_PRONOUNS = Arrays.asList("com");

	/** The list of expletive English pronouns. */
	@SuppressWarnings("nls")
	private static final List<String> EXPLETIVE_PRONOUNS     = Arrays.asList("me", "te", "se", "nos", "vos");
	
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
	
	/**
	 * Creates a new (empty) coordinated phrase
	 * 
	 * @return empty <code>CoordinatedPhraseElement</code>
	 */
	@Override
	public AbstractCoordinatedPhraseElement createCoordinatedPhrase() {
		return new PortugueseCoordinatedPhraseElement();
	}
	
	@Override
	public AbstractCoordinatedPhraseElement createCoordinatedPhrase(Object coord1, Object coord2) {
		return new PortugueseCoordinatedPhraseElement(coord1, coord2);
	}

	@Override
	public AbstractSPhraseSpec getSPhraseSpec() {
		return new PortugueseSPhraseSpec(this);
	}
	
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
			if (directObject instanceof AdjPhraseSpec) {
				if (phraseElement.getSubject() != null) {
					((AdjPhraseSpec) directObject).setParent(phraseElement.getSubject());
					NLGElement objectElement = phraseElement.getObject();
					Object head = objectElement.getFeature("head");
					if (head != null && head instanceof WordElement) {
						String id = ((WordElement) head).getId();
						if (id != null && !id.equals("")) {
							objectElement.setFeature(Feature.NUMBER, phraseElement.getSubject().getFeature(Feature.NUMBER));
						}
					}
				}
				
//				if(subject != null){
//					phraseElement.getObject().setParent(phraseElement.getSubject());
//				}
				
			}
			
		}

		return phraseElement;
	}
}
