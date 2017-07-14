package simplenlg.syntax.english;

import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.IInterrogativeType;
import simplenlg.features.InternalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.english.EnglishInterrogativeType;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.syntax.AbstractClauseHelper;
import simplenlg.syntax.AbstractVerbPhraseHelper;
import simplenlg.syntax.PhraseHelper;
import simplenlg.syntax.SyntaxProcessor;

public class EnglishClauseHelper extends AbstractClauseHelper{

	public EnglishClauseHelper(AbstractVerbPhraseHelper verbPhraseHelper) {
		super(verbPhraseHelper);
	}

	/**
	 * Realises the subjects of a passive clause.
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
	@Override
	public void addPassiveSubjects(PhraseElement phrase,
			SyntaxProcessor parent,
			ListElement realisedElement,
			NLGFactory phraseFactory) {
		NLGElement currentElement = null;

		if(phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
			List<NLGElement> allSubjects = phrase.getFeatureAsElementList(InternalFeature.SUBJECTS);

			if(allSubjects.size() > 0 || phrase.hasFeature(Feature.INTERROGATIVE_TYPE)) {
				realisedElement.addComponent(parent.realise(phraseFactory.createPrepositionPhrase("by"))); //$NON-NLS-1$
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

	/**
	 * Adds a <em>do</em> verb to the realisation of this clause.
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
	@Override
	public void addDoAuxiliary(PhraseElement phrase,
			SyntaxProcessor parent,
			NLGFactory phraseFactory,
			ListElement realisedElement) {

		PhraseElement doPhrase = phraseFactory.createVerbPhrase("do"); //$NON-NLS-1$
		doPhrase.setFeature(Feature.TENSE, phrase.getFeature(Feature.TENSE));
		doPhrase.setFeature(Feature.PERSON, phrase.getFeature(Feature.PERSON));
		doPhrase.setFeature(Feature.NUMBER, phrase.getFeature(Feature.NUMBER));
		realisedElement.addComponent(parent.realise(doPhrase));
	}

	/**
	 * Realises the verb part of the clause.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the clause.
	 * @param splitVerb
	 *            an <code>NLGElement</code> representing the subjects that
	 *            should split the verb
	 * @param verbElement
	 *            the <code>NLGElement</code> representing the verb phrase for
	 *            this clause.
	 * @param whObj
	 *            whether the VP is part of an object WH-interrogative
	 */
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
				currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.VERB_PHRASE);

				realisedElement.addComponent(currentElement);

			} else {
				if(currentElement instanceof ListElement) {
					List<NLGElement> children = currentElement.getChildren();
					currentElement = children.get(0);
					currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.VERB_PHRASE);
					realisedElement.addComponent(currentElement);
					realisedElement.addComponent(splitVerb);

					for(int eachChild = 1; eachChild < children.size(); eachChild++ ) {
						currentElement = children.get(eachChild);
						currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.VERB_PHRASE);
						realisedElement.addComponent(currentElement);
					}
				} else {
					currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.VERB_PHRASE);

					if(whObj) {
						realisedElement.addComponent(currentElement);
						realisedElement.addComponent(splitVerb);
					} else {
						realisedElement.addComponent(splitVerb);
						realisedElement.addComponent(currentElement);
					}
					//				}
				}
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
			switch((EnglishInterrogativeType) type){
			case YES_NO :
				splitVerb = realiseYesNo(phrase, parent, verbElement, phraseFactory, realisedElement);
				break;

			case WHO_SUBJECT :
			case WHAT_SUBJECT :
				realiseInterrogativeKeyWord(((EnglishInterrogativeType) type).getQuestion(),
				                            LexicalCategory.PRONOUN,
				                            parent,
				                            realisedElement, //$NON-NLS-1$
				                            phraseFactory);
				phrase.removeFeature(InternalFeature.SUBJECTS);
				break;

			case HOW_MANY :
				realiseInterrogativeKeyWord("how", LexicalCategory.PRONOUN, parent, realisedElement, //$NON-NLS-1$
				                            phraseFactory);
				realiseInterrogativeKeyWord("many", LexicalCategory.ADVERB, parent, realisedElement, //$NON-NLS-1$
				                            phraseFactory);
				break;

			case HOW :
			case WHY :
			case WHERE :
			case WHO_OBJECT :
			case WHO_INDIRECT_OBJECT :
			case WHAT_OBJECT :
				splitVerb = realiseObjectWHInterrogative(((EnglishInterrogativeType) type).getQuestion(),
				                                         phrase,
				                                         parent,
				                                         realisedElement,
				                                         phraseFactory);
				break;

			case HOW_PREDICATE :
				splitVerb = realiseObjectWHInterrogative("how", phrase, parent, realisedElement, phraseFactory);
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
				interrogObj = (EnglishInterrogativeType.WHAT_OBJECT.equals(inter)
				               || EnglishInterrogativeType.WHO_OBJECT.equals(inter)
				               || EnglishInterrogativeType.HOW_PREDICATE.equals(inter) || EnglishInterrogativeType.HOW.equals(inter)
				               || EnglishInterrogativeType.WHY.equals(inter) || EnglishInterrogativeType.WHERE.equals(inter));
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
		   && !EnglishInterrogativeType.WHAT_OBJECT.equals(phrase.getFeature(Feature.INTERROGATIVE_TYPE))) {

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
						coordSubj = (conj != null && conj.equals("and"));
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

		if(EnglishInterrogativeType.WHO_INDIRECT_OBJECT.equals(phrase.getFeature(Feature.INTERROGATIVE_TYPE))) {
			NLGElement word = phraseFactory.createWord("to", LexicalCategory.PREPOSITION); //$NON-NLS-1$
			realisedElement.addComponent(parent.realise(word));
		}
	}
}