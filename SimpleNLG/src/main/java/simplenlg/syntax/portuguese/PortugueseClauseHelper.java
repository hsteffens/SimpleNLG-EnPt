package simplenlg.syntax.portuguese;

import java.util.ArrayList;
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.IInterrogativeType;
import simplenlg.features.InternalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.portuguese.PortugueseInterrogativeType;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.WordElement;
import simplenlg.morphology.portuguese.PortugueseMorphologyRules;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.syntax.AbstractClauseHelper;
import simplenlg.syntax.AbstractVerbPhraseHelper;
import simplenlg.syntax.PhraseHelper;
import simplenlg.syntax.SyntaxProcessor;

/**
 * Esta classe contem os processos que manipulam a sintaxe em português.
 * 
 * @author Hélinton P. Steffens
 * @Jul 13, 2017
 */
public class PortugueseClauseHelper extends AbstractClauseHelper {

	public PortugueseClauseHelper(AbstractVerbPhraseHelper verbPhraseHelper) {
		super(verbPhraseHelper);
	}

	@Override
	public void addPassiveSubjects(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement,
			NLGFactory phraseFactory) {
		NLGElement currentElement = null;

		if(phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
			List<NLGElement> allSubjects = phrase.getFeatureAsElementList(InternalFeature.SUBJECTS);

			if(allSubjects.size() > 0 || (phrase.hasFeature(Feature.INTERROGATIVE_TYPE) && phrase.getFeatureAsElementList(InternalFeature.SUBJECTS).size() > 0)) {
				PPPhraseSpec preposition = getPrepositionPhrase(phraseFactory, phrase);
				realisedElement.addComponent(parent.realise(preposition)); //$NON-NLS-1$
			}

			for(NLGElement subject : allSubjects) {

				subject.setFeature(Feature.PASSIVE, true);
				if(subject.isA(PhraseCategory.NOUN_PHRASE) || subject instanceof AbstractCoordinatedPhraseElement) {
					currentElement = parent.realise(subject);
					if(currentElement != null) {
						currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.SUBJECT);
						realisedElement.addComponent(currentElement);
					}
				}
			}
		}
	}

	private PPPhraseSpec getPrepositionPhrase(NLGFactory phraseFactory, PhraseElement phrase) {
		String preposition = "por";
		Object number = null;
		List<NLGElement> children = phrase.getChildren();
		if (children != null && children.size() > 0) {
			for (NLGElement nlgElement : children) {
				if (nlgElement instanceof NPPhraseSpec) {
					number = nlgElement.getFeature(Feature.NUMBER);
					List<NLGElement> parts = nlgElement.getChildren();
					NLGElement removeBuffer = null;
					for (NLGElement part : parts) {
						if (part instanceof WordElement) {
							WordElement word = (WordElement) part;
							if ("o".equals(word.getBaseForm())) {
								removeBuffer = part;
								preposition = "pelo";
								break;
							} else if ("a".equals(word.getBaseForm())) {
								removeBuffer = part;
								preposition = "pela";
								break;
							} else if ("as".equals(word.getBaseForm())) {
								removeBuffer = part;
								preposition = "pelas";
								break;
							} else if ("os".equals(word.getBaseForm())) {
								removeBuffer = part;
								preposition = "pelos";
								break;
							}else if (word.getCategory().equals(LexicalCategory.NOUN)) {
								if (PortugueseMorphologyRules.isRegularFeminineWord(word, word.getBaseForm())) {
									if (NumberAgreement.PLURAL.equals(number)) {
										removeBuffer = part;
										preposition = "pelas";
									}else{
										removeBuffer = part;
										preposition = "pela";
									}
									break;
								} else if (!PortugueseMorphologyRules.isRegularFeminineWord(word, word.getRealisation())) {
									if (NumberAgreement.PLURAL.equals(number)) {
										removeBuffer = part;
										preposition = "pelos";
									}else{
										removeBuffer = part;
										preposition = "pelo";
									}
									break;
								}
							}
						}	
					}
					if (removeBuffer != null) {
						NLGElement specifier = ((NPPhraseSpec) nlgElement).getSpecifier();
						if (specifier != null) {
							if (specifier.equals(removeBuffer)) {
								nlgElement.removeFeature(InternalFeature.SPECIFIER);
							}
						}
					}

				}
			}
		}

		PPPhraseSpec prepositionPhrase = phraseFactory.createPrepositionPhrase(preposition);
		if (!preposition.equals("por") && number != null && NumberAgreement.PLURAL.equals(number)) {
			prepositionPhrase.setFeature(Feature.NUMBER, number);
		}

		return prepositionPhrase;
	}

	@Override
	public void addDoAuxiliary(PhraseElement phrase,
			SyntaxProcessor parent,
			NLGFactory phraseFactory,
			ListElement realisedElement) {
		return;
	}

	@Override
	public void realiseVerb(PhraseElement phrase,
			SyntaxProcessor parent,
			ListElement realisedElement,
			NLGElement splitVerb,
			NLGElement verbElement,
			boolean whObj) {

		setVerbFeatures(phrase, verbElement);

		NLGElement currentElement = parent.realise(verbElement);
		if(currentElement != null) {
			if(splitVerb == null) {
				if (whObj) {
					List<NLGElement> children = currentElement.getChildren();
					if (children != null && children.size() > 0) {
						for (NLGElement child : currentElement.getChildren()) {
							if (LexicalCategory.VERB.equals(child.getCategory())) {
								child.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
								child.setFeature(Feature.PERSON, Person.THIRD);
							}
						}
					}
				}
				
				currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.VERB_PHRASE);

				realisedElement.addComponent(currentElement);

			} else {
				currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.VERB_PHRASE);

				if(whObj) {
					boolean hasAdd = false;
					if (PortugueseInterrogativeType.WHY.equals(phrase.getFeature(Feature.INTERROGATIVE_TYPE))
							|| PortugueseInterrogativeType.WHERE.equals(phrase.getFeature(Feature.INTERROGATIVE_TYPE))) {
						realisedElement.addComponent(splitVerb);
						hasAdd = true;
					}
					
					List<NLGElement> children = currentElement.getChildren();
					if (children != null && children.size() > 0) {
						for (int i = 0; i < currentElement.getChildren().size(); i++) {
							if (i == 0) {
								realisedElement.addComponent(currentElement.getChildren().get(i));
								continue;
							}
							if (!hasAdd) {
								realisedElement.addComponent(splitVerb);
								hasAdd = true;
							}
	
							realisedElement.addComponent(currentElement.getChildren().get(i));
						}
						
						return;
					}else if (PortugueseInterrogativeType.WHAT_OBJECT.equals(phrase.getFeature(Feature.INTERROGATIVE_TYPE))) {
						realisedElement.addComponent(currentElement);
						realisedElement.addComponent(splitVerb);
						
						return;
					}
					
				}
				
				realisedElement.addComponent(splitVerb);
				realisedElement.addComponent(currentElement);
			}
		}
	}
	
	/**
	 * This is the main controlling method for handling interrogative clauses.
	 * The actual steps taken are dependent on the type of question being asked.
	 * The method also determines if there is a subject that will split the verb
	 * group of the clause. For example, the clause
	 * <em>the man <b>should give</b> the woman the flower</em> has the verb
	 * group indicated in <b>bold</b>. The phrase is rearranged as yes/no
	 * question as
	 * <em><b>should</b> the man <b>give</b> the woman the flower</em> with the
	 * subject <em>the man</em> splitting the verb group.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the clause.
	 * @param phraseFactory
	 *            the phrase factory to be used.
	 * @param verbElement
	 *            the <code>NLGElement</code> representing the verb phrase for
	 *            this clause.
	 * @return an <code>NLGElement</code> representing a subject that should
	 *         split the verb
	 */
	public NLGElement realiseInterrogative(PhraseElement phrase,
	                                               SyntaxProcessor parent,
	                                               ListElement realisedElement,
	                                               NLGFactory phraseFactory,
	                                               NLGElement verbElement) {
		NLGElement splitVerb = null;

		if(phrase.getParent() != null) {
			phrase.getParent().setFeature(InternalFeature.INTERROGATIVE, true);
		}

		Object type = phrase.getFeature(Feature.INTERROGATIVE_TYPE);

		if(type instanceof IInterrogativeType) {
			switch((PortugueseInterrogativeType) type){
			case YES_NO :
				splitVerb = realiseYesNo(phrase, parent, verbElement, phraseFactory, realisedElement);
				break;

			case WHO_SUBJECT :
			case WHAT_SUBJECT :
				realiseInterrogativeKeyWord(((PortugueseInterrogativeType) type).getQuestion(),
				                            LexicalCategory.PRONOUN,
				                            parent,
				                            realisedElement, //$NON-NLS-1$
				                            phraseFactory);
				phrase.removeFeature(InternalFeature.SUBJECTS);
				break;

			case HOW_MANY :
				realiseInterrogativeKeyWord("quanto", LexicalCategory.PRONOUN, parent, realisedElement, //$NON-NLS-1$
				                            phraseFactory);
				break;

			case HOW :
			case WHY :
			case WHERE :
			case WHO_OBJECT :
			case WHO_INDIRECT_OBJECT :
			case WHAT_OBJECT :
				splitVerb = realiseObjectWHInterrogative(((PortugueseInterrogativeType) type).getQuestion(),
				                                         phrase,
				                                         parent,
				                                         realisedElement,
				                                         phraseFactory);
				break;

			case HOW_PREDICATE :
				splitVerb = realiseObjectWHInterrogative("como", phrase, parent, realisedElement, phraseFactory);
				break;

			default :
				break;
			}
		}

		return splitVerb;
	}
	
	/**
	 * The main method for controlling the syntax realisation of clauses.
	 * 
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that called this
	 *            method.
	 * @param phrase
	 *            the <code>PhraseElement</code> representation of the clause.
	 * @return the <code>NLGElement</code> representing the realised clause.
	 */
	public NLGElement realise(SyntaxProcessor parent, PhraseElement phrase) {
		ListElement realisedElement = null;
		NLGFactory phraseFactory = phrase.getFactory();
		NLGElement splitVerb = null;
		boolean interrogObj = false;

		if(phrase != null) {
			realisedElement = new ListElement();
			NLGElement verbElement = phrase.getFeatureAsElement(InternalFeature.VERB_PHRASE);

			if(verbElement == null) {
				verbElement = phrase.getHead();
			}

			checkSubjectNumberPerson(phrase, verbElement);
			checkDiscourseFunction(phrase);
			copyFrontModifiers(phrase, verbElement);
			addComplementiser(phrase, parent, realisedElement);
			addCuePhrase(phrase, parent, realisedElement);

			if(phrase.hasFeature(Feature.INTERROGATIVE_TYPE)) {
				Object inter = phrase.getFeature(Feature.INTERROGATIVE_TYPE);
				interrogObj = (PortugueseInterrogativeType.WHAT_OBJECT.equals(inter) 
							   || PortugueseInterrogativeType.WHO_SUBJECT.equals(inter)
				               || PortugueseInterrogativeType.WHO_OBJECT.equals(inter)
				               || PortugueseInterrogativeType.HOW_PREDICATE.equals(inter) || PortugueseInterrogativeType.HOW.equals(inter)
				               || PortugueseInterrogativeType.WHY.equals(inter) || PortugueseInterrogativeType.WHERE.equals(inter));
				splitVerb = realiseInterrogative(phrase, parent, realisedElement, phraseFactory, verbElement);
			} else {
				PhraseHelper.realiseList(parent,
				                         realisedElement,
				                         phrase.getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS),
				                         DiscourseFunction.FRONT_MODIFIER);
			}

			addSubjectsToFront(phrase, parent, realisedElement, splitVerb);

			NLGElement passiveSplitVerb = addPassiveComplementsNumberPerson(phrase,
			                                                                parent,
			                                                                realisedElement,
			                                                                verbElement);

			if(passiveSplitVerb != null) {
				splitVerb = passiveSplitVerb;
			}

			// realise verb needs to know if clause is object interrogative
			realiseVerb(phrase, parent, realisedElement, splitVerb, verbElement, interrogObj);
			addPassiveSubjects(phrase, parent, realisedElement, phraseFactory);
			addInterrogativeFrontModifiers(phrase, parent, realisedElement);
			addEndingTo(phrase, parent, realisedElement, phraseFactory);
		}
		return realisedElement;
	}
	
	/**
	 * Realises the complements of passive clauses; also sets number, person for
	 * passive
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the clause.
	 * @param verbElement
	 *            the <code>NLGElement</code> representing the verb phrase for
	 *            this clause.
	 */
	private NLGElement addPassiveComplementsNumberPerson(PhraseElement phrase,
	                                                            SyntaxProcessor parent,
	                                                            ListElement realisedElement,
	                                                            NLGElement verbElement) {
		Object passiveNumber = null;
		Object passivePerson = null;
		NLGElement currentElement = null;
		NLGElement splitVerb = null;
		NLGElement verbPhrase = phrase.getFeatureAsElement(InternalFeature.VERB_PHRASE);

		// count complements to set plural feature if more than one
		int numComps = 0;
		boolean coordSubj = false;

		if(phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue() && verbPhrase != null
		   && !PortugueseInterrogativeType.WHAT_OBJECT.equals(phrase.getFeature(Feature.INTERROGATIVE_TYPE))) {

			// complements of a clause are stored in the VPPhraseSpec
			for(NLGElement subject : verbPhrase.getFeatureAsElementList(InternalFeature.COMPLEMENTS)) {

				// AG: complement needn't be an NP
				// subject.isA(PhraseCategory.NOUN_PHRASE) &&
				if(DiscourseFunction.OBJECT.equals(subject.getFeature(InternalFeature.DISCOURSE_FUNCTION))) {
					subject.setFeature(Feature.PASSIVE, true);
					numComps++ ;
					currentElement = parent.realise(subject);

					if(currentElement != null) {
						currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.OBJECT);

						if(phrase.hasFeature(Feature.INTERROGATIVE_TYPE)) {
							splitVerb = currentElement;
						} else {
							realisedElement.addComponent(currentElement);
						}
					}

					// flag if passive subject is coordinated with an "and"
					if(!coordSubj && subject instanceof AbstractCoordinatedPhraseElement) {
						String conj = ((AbstractCoordinatedPhraseElement) subject).getConjunction();
						coordSubj = (conj != null && conj.equals("e"));
					}

					if(passiveNumber == null) {
						passiveNumber = subject.getFeature(Feature.NUMBER);
					} else {
						passiveNumber = NumberAgreement.PLURAL;
					}

					if(Person.FIRST.equals(subject.getFeature(Feature.PERSON))) {
						passivePerson = Person.FIRST;
					} else if(Person.SECOND.equals(subject.getFeature(Feature.PERSON))
					          && !Person.FIRST.equals(passivePerson)) {
						passivePerson = Person.SECOND;
					} else if(passivePerson == null) {
						passivePerson = Person.THIRD;
					}

					if(Form.GERUND.equals(phrase.getFeature(Feature.FORM))
					   && !phrase.getFeatureAsBoolean(Feature.SUPPRESS_GENITIVE_IN_GERUND).booleanValue()) {
						subject.setFeature(Feature.POSSESSIVE, true);
					}
				}
			}
		} else {
			for(NLGElement subject : phrase.getFeatureAsElementList(InternalFeature.SUBJECTS)) {
				// AG: complement needn't be an NP
				// subject.isA(PhraseCategory.NOUN_PHRASE) &&
				numComps++ ;

				if(passiveNumber == null) {
					passiveNumber = phrase.getFeature(Feature.NUMBER);
				} else {
					passiveNumber = NumberAgreement.PLURAL;
				}

				if(Person.FIRST.equals(subject.getFeature(Feature.PERSON))) {
					passivePerson = Person.FIRST;
				} else if(Person.SECOND.equals(subject.getFeature(Feature.PERSON))
						&& !Person.FIRST.equals(passivePerson)) {
					passivePerson = Person.SECOND;
				} else if(passivePerson == null) {
					passivePerson = Person.THIRD;
				}

				if(Form.GERUND.equals(phrase.getFeature(Feature.FORM))
						&& !phrase.getFeatureAsBoolean(Feature.SUPPRESS_GENITIVE_IN_GERUND).booleanValue()) {
					subject.setFeature(Feature.POSSESSIVE, true);
				}
			}
		}

		if(verbElement != null) {
			if(passivePerson != null) {
				verbElement.setFeature(Feature.PERSON, passivePerson);
				// below commented out. for non-passive, number and person set
				// by checkSubjectNumberPerson
				// } else {
				// verbElement.setFeature(Feature.PERSON, phrase
				// .getFeature(Feature.PERSON));
			}

			if(numComps > 1 || coordSubj) {
				verbElement.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
			} else if(passiveNumber != null) {
				verbElement.setFeature(Feature.NUMBER, passiveNumber);
			}
		}
		return splitVerb;
	}
	
	/**
	 * Adds <em>to</em> to the end of interrogatives concerning indirect
	 * objects. For example, <em>who did John give the flower <b>to</b></em>.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the clause.
	 * @param phraseFactory
	 *            the phrase factory to be used.
	 */
	private void addEndingTo(PhraseElement phrase,
	                                SyntaxProcessor parent,
	                                ListElement realisedElement,
	                                NLGFactory phraseFactory) {

		if(PortugueseInterrogativeType.WHO_INDIRECT_OBJECT.equals(phrase.getFeature(Feature.INTERROGATIVE_TYPE))) {
			NLGElement word = phraseFactory.createWord("para", LexicalCategory.PREPOSITION); //$NON-NLS-1$
			List<NLGElement> components = new ArrayList<NLGElement>();
			components.add(parent.realise(word));
			for (NLGElement nlgElement : realisedElement.getChildren()) {
				components.add(nlgElement);
			}
			realisedElement.setComponents(components);
		}
	}

}
