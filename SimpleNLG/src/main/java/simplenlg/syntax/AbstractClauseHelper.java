package simplenlg.syntax;

import java.util.List;

import simplenlg.features.ClauseStatus;
import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.InternalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;

public abstract class AbstractClauseHelper {

	private AbstractVerbPhraseHelper verbPhraseHelper;

	public AbstractClauseHelper(AbstractVerbPhraseHelper verbPhraseHelper) {
		this.verbPhraseHelper = verbPhraseHelper;
	}
	
	public abstract void addPassiveSubjects(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement, NLGFactory phraseFactory);
	public abstract void addDoAuxiliary(PhraseElement phrase, SyntaxProcessor parent, NLGFactory phraseFactory, ListElement realisedElement);
	public abstract void realiseVerb(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement, NLGElement splitVerb, NLGElement verbElement, boolean whObj);
	public abstract NLGElement realiseInterrogative(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement, NLGFactory phraseFactory, NLGElement verbElement);
	public abstract NLGElement realise(SyntaxProcessor parent, PhraseElement phrase);
	
	/**
	 * Adds the front modifiers to the end of the clause when dealing with
	 * interrogatives.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the clause.
	 */
	public void addInterrogativeFrontModifiers(PhraseElement phrase,
	                                                   SyntaxProcessor parent,
	                                                   ListElement realisedElement) {
		NLGElement currentElement = null;
		if(phrase.hasFeature(Feature.INTERROGATIVE_TYPE)) {
			for(NLGElement subject : phrase.getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS)) {
				currentElement = parent.realise(subject);
				if(currentElement != null) {
					currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.FRONT_MODIFIER);

					realisedElement.addComponent(currentElement);
				}
			}
		}
	}

	/**
	 * Ensures that the verb inherits the features from the clause.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param verbElement
	 *            the <code>NLGElement</code> representing the verb phrase for
	 *            this clause.
	 */
	public void setVerbFeatures(PhraseElement phrase, NLGElement verbElement) {
		// this routine copies features from the clause to the VP.
		// it is disabled, as this copying is now done automatically
		// when features are set in SPhraseSpec
		// if (verbElement != null) {
		// verbElement.setFeature(Feature.INTERROGATIVE_TYPE, phrase
		// .getFeature(Feature.INTERROGATIVE_TYPE));
		// verbElement.setFeature(InternalFeature.COMPLEMENTS, phrase
		// .getFeature(InternalFeature.COMPLEMENTS));
		// verbElement.setFeature(InternalFeature.PREMODIFIERS, phrase
		// .getFeature(InternalFeature.PREMODIFIERS));
		// verbElement.setFeature(Feature.FORM, phrase
		// .getFeature(Feature.FORM));
		// verbElement.setFeature(Feature.MODAL, phrase
		// .getFeature(Feature.MODAL));
		// verbElement.setNegated(phrase.isNegated());
		// verbElement.setFeature(Feature.PASSIVE, phrase
		// .getFeature(Feature.PASSIVE));
		// verbElement.setFeature(Feature.PERFECT, phrase
		// .getFeature(Feature.PERFECT));
		// verbElement.setFeature(Feature.PROGRESSIVE, phrase
		// .getFeature(Feature.PROGRESSIVE));
		// verbElement.setTense(phrase.getTense());
		// verbElement.setFeature(Feature.FORM, phrase
		// .getFeature(Feature.FORM));
		// verbElement.setFeature(LexicalFeature.GENDER, phrase
		// .getFeature(LexicalFeature.GENDER));
		// }
	}

	/**
	 * Adds the subjects to the beginning of the clause unless the clause is
	 * infinitive, imperative or passive, or the subjects split the verb.
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
	 */
	public void addSubjectsToFront(PhraseElement phrase,
	                                       SyntaxProcessor parent,
	                                       ListElement realisedElement,
	                                       NLGElement splitVerb) {
		if(!Form.INFINITIVE.equals(phrase.getFeature(Feature.FORM))
		   && !Form.IMPERATIVE.equals(phrase.getFeature(Feature.FORM))
		   && !phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue() && splitVerb == null) {
			realisedElement.addComponents(realiseSubjects(phrase, parent).getChildren());
		}
	}

	/**
	 * Realises the subjects for the clause.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the clause.
	 */
	private static ListElement realiseSubjects(PhraseElement phrase, SyntaxProcessor parent) {

		NLGElement currentElement = null;
		ListElement realisedElement = new ListElement();

		for(NLGElement subject : phrase.getFeatureAsElementList(InternalFeature.SUBJECTS)) {

			subject.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.SUBJECT);
			if(Form.GERUND.equals(phrase.getFeature(Feature.FORM))
			   && !phrase.getFeatureAsBoolean(Feature.SUPPRESS_GENITIVE_IN_GERUND).booleanValue()) {
				subject.setFeature(Feature.POSSESSIVE, true);
			}
			currentElement = parent.realise(subject);
			if(currentElement != null) {
				realisedElement.addComponent(currentElement);
			}
		}
		return realisedElement;
	}

	/*
	 * Check if a sentence has an auxiliary (needed to relise questions
	 * correctly)
	 */
	private static boolean hasAuxiliary(PhraseElement phrase) {
		return phrase.hasFeature(Feature.MODAL) || phrase.getFeatureAsBoolean(Feature.PERFECT).booleanValue()
		       || phrase.getFeatureAsBoolean(Feature.PROGRESSIVE).booleanValue()
		       || Tense.FUTURE.equals(phrase.getFeature(Feature.TENSE));
	}

	/**
	 * Controls the realisation of <em>wh</em> object questions.
	 * 
	 * @param keyword
	 *            the wh word
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the clause.
	 * @param phraseFactory
	 *            the phrase factory to be used.
	 * @param subjects
	 *            the <code>List</code> of subjects in the clause.
	 * @return an <code>NLGElement</code> representing a subject that should
	 *         split the verb
	 */
	public NLGElement realiseObjectWHInterrogative(String keyword,
	                                                       PhraseElement phrase,
	                                                       SyntaxProcessor parent,
	                                                       ListElement realisedElement,
	                                                       NLGFactory phraseFactory) {
		NLGElement splitVerb = null;
		realiseInterrogativeKeyWord(keyword, LexicalCategory.PRONOUN, parent, realisedElement, //$NON-NLS-1$
		                            phraseFactory);

		// if (!Tense.FUTURE.equals(phrase.getFeature(Feature.TENSE)) &&
		// !copular) {
		if(!hasAuxiliary(phrase) && !verbPhraseHelper.isCopular(phrase)) {
			addDoAuxiliary(phrase, parent, phraseFactory, realisedElement);

		} else if(!phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
			splitVerb = realiseSubjects(phrase, parent);
		}

		return splitVerb;
	}

	/**
	 * Realises the key word of the interrogative. For example, <em>who</em>,
	 * <em>what</em>
	 * 
	 * @param keyWord
	 *            the key word of the interrogative.
	 * @param cat
	 *            the category (usually pronoun, but not in the case of
	 *            "how many")
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the clause.
	 * @param phraseFactory
	 *            the phrase factory to be used.
	 */
	public void realiseInterrogativeKeyWord(String keyWord,
	                                                LexicalCategory cat,
	                                                SyntaxProcessor parent,
	                                                ListElement realisedElement,
	                                                NLGFactory phraseFactory) {

		if(keyWord != null) {
			NLGElement question = phraseFactory.createWord(keyWord, cat);
			NLGElement currentElement = parent.realise(question);

			if(currentElement != null) {
				realisedElement.addComponent(currentElement);
			}
		}
	}

	/**
	 * Performs the realisation for YES/NO types of questions. This may involve
	 * adding an optional <em>do</em> auxiliary verb to the beginning of the
	 * clause. The method also determines if there is a subject that will split
	 * the verb group of the clause. For example, the clause
	 * <em>the man <b>should give</b> the woman the flower</em> has the verb
	 * group indicated in <b>bold</b>. The phrase is rearranged as yes/no
	 * question as
	 * <em><b>should</b> the man <b>give</b> the woman the flower</em> with the
	 * subject <em>the man</em> splitting the verb group.
	 * 
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
	 * @param subjects
	 *            the <code>List</code> of subjects in the clause.
	 * @return an <code>NLGElement</code> representing a subject that should
	 *         split the verb
	 */
	public NLGElement realiseYesNo(PhraseElement phrase,
	                                       SyntaxProcessor parent,
	                                       NLGElement verbElement,
	                                       NLGFactory phraseFactory,
	                                       ListElement realisedElement) {

		NLGElement splitVerb = null;

		if(!(verbElement instanceof VPPhraseSpec && verbPhraseHelper.isCopular(((VPPhraseSpec) verbElement).getVerb()))
		   && !phrase.getFeatureAsBoolean(Feature.PROGRESSIVE).booleanValue() && !phrase.hasFeature(Feature.MODAL)
		   && !Tense.FUTURE.equals(phrase.getFeature(Feature.TENSE))
		   && !phrase.getFeatureAsBoolean(Feature.NEGATED).booleanValue()
		   && !phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
			addDoAuxiliary(phrase, parent, phraseFactory, realisedElement);
		} else {
			splitVerb = realiseSubjects(phrase, parent);
		}
		return splitVerb;
	}

	/**
	 * Realises the cue phrase for the clause if it exists.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the clause.
	 */
	public void addCuePhrase(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement) {

		NLGElement currentElement = parent.realise(phrase.getFeatureAsElement(Feature.CUE_PHRASE));

		if(currentElement != null) {
			currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.CUE_PHRASE);
			realisedElement.addComponent(currentElement);
		}
	}

	/**
	 * Checks to see if this clause is a subordinate clause. If it is then the
	 * complementiser is added as a component to the realised element
	 * <b>unless</b> the complementiser has been suppressed.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the clause.
	 */
	public void addComplementiser(PhraseElement phrase, SyntaxProcessor parent, ListElement realisedElement) {

		NLGElement currentElement;

		if(ClauseStatus.SUBORDINATE.equals(phrase.getFeature(InternalFeature.CLAUSE_STATUS))
		   && !phrase.getFeatureAsBoolean(Feature.SUPRESSED_COMPLEMENTISER).booleanValue()) {

			currentElement = parent.realise(phrase.getFeatureAsElement(Feature.COMPLEMENTISER));

			if(currentElement != null) {
				realisedElement.addComponent(currentElement);
			}
		}
	}

	/**
	 * Copies the front modifiers of the clause to the list of post-modifiers of
	 * the verb only if the phrase has infinitive form.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param verbElement
	 *            the <code>NLGElement</code> representing the verb phrase for
	 *            this clause.
	 */
	public void copyFrontModifiers(PhraseElement phrase, NLGElement verbElement) {
		List<NLGElement> frontModifiers = phrase.getFeatureAsElementList(InternalFeature.FRONT_MODIFIERS);
		Object clauseForm = phrase.getFeature(Feature.FORM);

		// bug fix by Chris Howell (Agfa) -- do not overwrite existing post-mods
		// in the VP
		if(verbElement != null) {
			List<NLGElement> phrasePostModifiers = phrase.getFeatureAsElementList(InternalFeature.POSTMODIFIERS);

			if(verbElement instanceof PhraseElement) {
				List<NLGElement> verbPostModifiers = verbElement.getFeatureAsElementList(InternalFeature.POSTMODIFIERS);

				for(NLGElement eachModifier : phrasePostModifiers) {

					// need to check that VP doesn't already contain the
					// post-modifier
					// this only happens if the phrase has already been realised
					// and later modified, with realiser called again. In that
					// case, postmods will be copied over twice
					if(!verbPostModifiers.contains(eachModifier)) {
						((PhraseElement) verbElement).addPostModifier(eachModifier);
					}
				}
			}
		}

		// if (verbElement != null) {
		// verbElement.setFeature(InternalFeature.POSTMODIFIERS, phrase
		// .getFeature(InternalFeature.POSTMODIFIERS));
		// }

		if(Form.INFINITIVE.equals(clauseForm)) {
			phrase.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);

			for(NLGElement eachModifier : frontModifiers) {
				if(verbElement instanceof PhraseElement) {
					((PhraseElement) verbElement).addPostModifier(eachModifier);
				}
			}
			phrase.removeFeature(InternalFeature.FRONT_MODIFIERS);
			if(verbElement != null) {
				verbElement.setFeature(InternalFeature.NON_MORPH, true);
			}
		}
	}

	/**
	 * Checks the discourse function of the clause and alters the form of the
	 * clause as necessary. The following algorithm is used: <br>
	 * 
	 * <pre>
	 * If the clause represents a direct or indirect object then 
	 *      If form is currently Imperative then
	 *           Set form to Infinitive
	 *           Suppress the complementiser
	 *      If form is currently Gerund and there are no subjects
	 *      	 Suppress the complementiser
	 * If the clause represents a subject then
	 *      Set the form to be Gerund
	 *      Suppress the complementiser
	 * </pre>
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 */
	public void checkDiscourseFunction(PhraseElement phrase) {
		List<NLGElement> subjects = phrase.getFeatureAsElementList(InternalFeature.SUBJECTS);
		Object clauseForm = phrase.getFeature(Feature.FORM);
		Object discourseValue = phrase.getFeature(InternalFeature.DISCOURSE_FUNCTION);

		if(DiscourseFunction.OBJECT.equals(discourseValue) || DiscourseFunction.INDIRECT_OBJECT.equals(discourseValue)) {

			if(Form.IMPERATIVE.equals(clauseForm)) {
				phrase.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);
				phrase.setFeature(Feature.FORM, Form.INFINITIVE);
			} else if(Form.GERUND.equals(clauseForm) && subjects.size() == 0) {
				phrase.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);
			}
		} else if(DiscourseFunction.SUBJECT.equals(discourseValue)) {
			phrase.setFeature(Feature.FORM, Form.GERUND);
			phrase.setFeature(Feature.SUPRESSED_COMPLEMENTISER, true);
		}
	}

	/**
	 * Checks the subjects of the phrase to determine if there is more than one
	 * subject. This ensures that the verb phrase is correctly set. Also set
	 * person correctly
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this clause.
	 * @param verbElement
	 *            the <code>NLGElement</code> representing the verb phrase for
	 *            this clause.
	 */
	public void checkSubjectNumberPerson(PhraseElement phrase, NLGElement verbElement) {
		NLGElement currentElement = null;
		List<NLGElement> subjects = phrase.getFeatureAsElementList(InternalFeature.SUBJECTS);
		boolean pluralSubjects = false;
		Person person = null;

		if(subjects != null) {
			switch(subjects.size()){
			case 0 :
				break;

			case 1 :
				currentElement = subjects.get(0);
				// coordinated NP with "and" are plural (not coordinated NP with
				// "or")
				if(currentElement instanceof AbstractCoordinatedPhraseElement
				   && ((AbstractCoordinatedPhraseElement) currentElement).checkIfPlural())
					pluralSubjects = true;
				else if((currentElement.getFeature(Feature.NUMBER) == NumberAgreement.PLURAL)
				        && !(currentElement instanceof AbstractSPhraseSpec)) // ER mod-
				                                                     // clauses
				                                                     // are
				                                                     // singular
				                                                     // as
				                                                     // NPs,
				                                                     // even
				                                                     // if
				                                                     // they
				                                                     // are
				                                                     // plural
				                                                     // internally
					pluralSubjects = true;
				else if(currentElement.isA(PhraseCategory.NOUN_PHRASE)) {
					NLGElement currentHead = currentElement.getFeatureAsElement(InternalFeature.HEAD);
					person = (Person) currentElement.getFeature(Feature.PERSON);

					if(currentHead == null) {
						// subject is null and therefore is not gonna be plural
						pluralSubjects = false;
					} else if((currentHead.getFeature(Feature.NUMBER) == NumberAgreement.PLURAL))
						pluralSubjects = true;
					else if(currentHead instanceof ListElement) {
						pluralSubjects = true;
						/*
						 * } else if (currentElement instanceof
						 * CoordinatedPhraseElement &&
						 * "and".equals(currentElement.getFeatureAsString(
						 * //$NON-NLS-1$ Feature.CONJUNCTION))) { pluralSubjects
						 * = true;
						 */
					}
				}
				break;

			default :
				pluralSubjects = true;
				break;
			}
		}
		if(verbElement != null) {
			verbElement.setFeature(Feature.NUMBER, pluralSubjects ? NumberAgreement.PLURAL
			        : phrase.getFeature(Feature.NUMBER));
			if(person != null)
				verbElement.setFeature(Feature.PERSON, person);
		}
	}
	
}
