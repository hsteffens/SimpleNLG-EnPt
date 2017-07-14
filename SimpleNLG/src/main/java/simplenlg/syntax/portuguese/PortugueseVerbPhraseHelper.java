package simplenlg.syntax.portuguese;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.InternalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.features.portuguese.PortugueseInterrogativeType;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.PhraseCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.StringElement;
import simplenlg.framework.WordElement;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.syntax.AbstractVerbPhraseHelper;
import simplenlg.syntax.PhraseHelper;
import simplenlg.syntax.SyntaxProcessor;

/**
 * Esta classe contem os processos que manipulam a sintaxe dos verbos em português.
 * 
 * @author Hélinton P. Steffens
 * @Jul 13, 2017
 */
public class PortugueseVerbPhraseHelper extends AbstractVerbPhraseHelper{

	/**
	 * Creates a stack of verbs for the verb phrase. Additional auxiliary verbs
	 * are added as required based on the features of the verb phrase.
	 * 
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @return the verb group as a <code>Stack</code> of <code>NLGElement</code>
	 *         s.
	 */
	@Override
	public final Stack<NLGElement> createVerbGroup(
			SyntaxProcessor parent, PhraseElement phrase) {

		String actualModal = null;
		Object formValue = phrase.getFeature(Feature.FORM);
		Tense tenseValue = (Tense) phrase.getFeature(Feature.TENSE);
		Object numberValue = phrase.getFeature(Feature.NUMBER);
		Object personValue = phrase.getFeature(Feature.PERSON);
		String modal = phrase.getFeatureAsString(Feature.MODAL);
		DiscourseFunction discourseFunction = (DiscourseFunction) phrase.getFeature(InternalFeature.DISCOURSE_FUNCTION);
		boolean modalPast = false;
		Stack<NLGElement> vgComponents = new Stack<NLGElement>();
		boolean interrogative = phrase.hasFeature(Feature.INTERROGATIVE_TYPE);

		if (Form.GERUND.equals(formValue) || Form.INFINITIVE.equals(formValue)) {
			tenseValue = Tense.PRESENT;
		}

		if (formValue == null || Form.NORMAL.equals(formValue)) {
			if (modal != null) {
				actualModal = modal;

				if (Tense.PAST.equals(tenseValue)) {
					modalPast = true;
				}
			}
		}

		pushParticles(phrase, parent, vgComponents);
		NLGElement frontVG = grabHeadVerb(phrase, tenseValue, numberValue, personValue, discourseFunction,modal != null);
		checkImperativeInfinitive(formValue, frontVG);


		if (phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
			frontVG = addBe(frontVG, vgComponents, Form.PAST_PARTICIPLE, "ser");
		} else if (phrase.getFeatureAsBoolean(Feature.PROGRESSIVE).booleanValue()) {
			frontVG = addBe(frontVG, vgComponents, Form.GERUND, "estar");
		}
		
		if (modalPast) {
			frontVG = addHave(frontVG, vgComponents, modal, tenseValue);
		}

		frontVG = pushIfModal(actualModal != null, phrase, frontVG,
				vgComponents);

		if (frontVG != null) {
			pushFrontVerb(phrase, vgComponents, frontVG, formValue,
					interrogative);
		}
		frontVG = createNot(phrase, vgComponents, frontVG, modal != null);

		pushModal(actualModal, phrase, vgComponents);
		return vgComponents;
	}

	/**
	 * Pushes the modal onto the stack of verb components.
	 * 
	 * @param actualModal
	 *            the modal to be used.
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 */
	private static void pushModal(String actualModal, PhraseElement phrase,
			Stack<NLGElement> vgComponents) {
		if (actualModal != null
				&& !phrase.getFeatureAsBoolean(InternalFeature.IGNORE_MODAL)
						.booleanValue()) {
			vgComponents.push(new InflectedWordElement(actualModal,
					LexicalCategory.MODAL));
		}
	}

	/**
	 * Pushes the front verb onto the stack of verb components.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @param frontVG
	 *            the first verb in the verb group.
	 * @param formValue
	 *            the <code>Form</code> of the phrase.
	 * @param interrogative
	 *            <code>true</code> if the phrase is interrogative.
	 */
	private void pushFrontVerb(PhraseElement phrase,
			Stack<NLGElement> vgComponents, NLGElement frontVG,
			Object formValue, boolean interrogative) {
		Object interrogType = phrase.getFeature(Feature.INTERROGATIVE_TYPE);
		
		if (Form.GERUND.equals(formValue)) {
			frontVG.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
			vgComponents.push(frontVG);

		} else if (Form.PAST_PARTICIPLE.equals(formValue)) {
			frontVG.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
			vgComponents.push(frontVG);

		} else if (Form.PRESENT_PARTICIPLE.equals(formValue)) {
			frontVG.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
			vgComponents.push(frontVG);

		} else if ((!(formValue == null || Form.NORMAL.equals(formValue)) || interrogative)
				&& !isCopular(phrase.getHead()) && vgComponents.isEmpty()) {

			// AG: fix below: if interrogative, only set non-morph feature in
			// case it's not WHO_SUBJECT OR WHAT_SUBJECT			
			if (!(PortugueseInterrogativeType.WHO_SUBJECT.equals(interrogType) || PortugueseInterrogativeType.WHAT_SUBJECT
					.equals(interrogType))) {
				frontVG.setFeature(InternalFeature.NON_MORPH, false);
			}

			vgComponents.push(frontVG);

		} else {
			NumberAgreement numToUse = determineNumber(phrase.getParent(),
					phrase);
			frontVG.setFeature(Feature.TENSE, phrase.getFeature(Feature.TENSE));
			frontVG.setFeature(Feature.PERSON, phrase
					.getFeature(Feature.PERSON));
			frontVG.setFeature(Feature.NUMBER, numToUse);
			
			//don't push the front VG if it's a negated interrogative WH object question
			if (!(phrase.getFeatureAsBoolean(Feature.NEGATED).booleanValue() && (PortugueseInterrogativeType.WHO_OBJECT
					.equals(interrogType) || PortugueseInterrogativeType.WHAT_OBJECT
					.equals(interrogType)))) {
				vgComponents.push(frontVG);
			}
		}
	}

	/**
	 * Adds <em>not</em> to the stack if the phrase is negated.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @param frontVG
	 *            the first verb in the verb group.
	 * @param hasModal
	 *            the phrase has a modal
	 * @return the new element for the front of the group.
	 */
	private NLGElement createNot(PhraseElement phrase,
			Stack<NLGElement> vgComponents, NLGElement frontVG, boolean hasModal) {
		NLGElement newFront = frontVG;

		if (phrase.getFeatureAsBoolean(Feature.NEGATED).booleanValue()) {
			if (!vgComponents.empty() || frontVG != null && isCopular(frontVG)) {
				vgComponents.push(new InflectedWordElement(
						"não", LexicalCategory.ADVERB)); //$NON-NLS-1$
			} else {
				if (frontVG != null && !hasModal) {
					frontVG.setFeature(Feature.NEGATED, true);
				}

				vgComponents.push(new InflectedWordElement(
						"não", LexicalCategory.ADVERB)); //$NON-NLS-1$
			}
		}

		return newFront;
	}

	/**
	 * Pushes the front verb on to the stack if the phrase has a modal.
	 * 
	 * @param hasModal
	 *            the phrase has a modal
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param frontVG
	 *            the first verb in the verb group.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @return the new element for the front of the group.
	 */
	private static NLGElement pushIfModal(boolean hasModal,
			PhraseElement phrase, NLGElement frontVG,
			Stack<NLGElement> vgComponents) {

		NLGElement newFront = frontVG;
		if (hasModal
				&& !phrase.getFeatureAsBoolean(InternalFeature.IGNORE_MODAL)
						.booleanValue()) {
			if (frontVG != null) {
				frontVG.setFeature(InternalFeature.NON_MORPH, true);
				vgComponents.push(frontVG);
			}
			newFront = null;
		}
		return newFront;
	}


	/**
	 * Adds the <em>be</em> verb to the front of the group.
	 * 
	 * @param frontVG
	 *            the first verb in the verb group.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @param frontForm
	 *            the form the current front verb is to take.
	 * @return the new element for the front of the group.
	 */
	private static NLGElement addBe(NLGElement frontVG,
			Stack<NLGElement> vgComponents, Form frontForm, String word) {

		if (frontVG != null) {
			frontVG.setFeature(Feature.FORM, frontForm);
			vgComponents.push(frontVG);
		}
		return new InflectedWordElement(word, LexicalCategory.VERB); //$NON-NLS-1$
	}

	/**
	 * Checks to see if the phrase is in imperative, infinitive or bare
	 * infinitive form. If it is then no morphology is done on the main verb.
	 * 
	 * @param formValue
	 *            the <code>Form</code> of the phrase.
	 * @param frontVG
	 *            the first verb in the verb group.
	 */
	private static void checkImperativeInfinitive(Object formValue,
			NLGElement frontVG) {

		if ((Form.IMPERATIVE.equals(formValue)
				|| Form.INFINITIVE.equals(formValue) || Form.BARE_INFINITIVE
				.equals(formValue))
				&& frontVG != null) {
			frontVG.setFeature(InternalFeature.NON_MORPH, true);
		}
	}

	/**
	 * Grabs the head verb of the verb phrase and sets it to future tense if the
	 * phrase is future tense. It also turns off negation if the group has a
	 * modal.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param tenseValue
	 *            the <code>Tense</code> of the phrase.
	 * @param hasModal
	 *            <code>true</code> if the verb phrase has a modal.
	 * @return the modified head element
	 */
	private static NLGElement grabHeadVerb(PhraseElement phrase, Tense tenseValue, Object number, Object person,DiscourseFunction discourseFunction, boolean hasModal) {
		NLGElement frontVG = phrase.getHead();

		if (frontVG != null) {
			if (frontVG instanceof WordElement) {
				frontVG = new InflectedWordElement((WordElement) frontVG);
			}

			// AG: tense value should always be set on frontVG
			if (tenseValue != null) {
				frontVG.setFeature(Feature.TENSE, tenseValue);
			}
			
			if (number != null) {
				frontVG.setFeature(Feature.NUMBER, number);
			}
			
			if (person != null) {
				frontVG.setFeature(Feature.PERSON, person);
			}
			
			if (discourseFunction != null) {
				frontVG.setFeature(InternalFeature.DISCOURSE_FUNCTION, discourseFunction);
			}

			// if (Tense.FUTURE.equals(tenseValue) && frontVG != null) {
			// frontVG.setFeature(Feature.TENSE, Tense.FUTURE);
			// }

			if (hasModal) {
				frontVG.setFeature(Feature.NEGATED, false);
			}
		}

		return frontVG;
	}

	/**
	 * Pushes the particles of the main verb onto the verb group stack.
	 * 
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 */
	private static void pushParticles(PhraseElement phrase,
			SyntaxProcessor parent, Stack<NLGElement> vgComponents) {
		Object particle = phrase.getFeature(Feature.PARTICLE);

		if (particle instanceof String) {
			vgComponents.push(new StringElement((String) particle));

		} else if (particle instanceof NLGElement) {
			vgComponents.push(parent.realise((NLGElement) particle));
		}
	}

	/**
	 * Determines the number agreement for the phrase ensuring that any number
	 * agreement on the parent element is inherited by the phrase.
	 * 
	 * @param parent
	 *            the parent element of the phrase.
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @return the <code>NumberAgreement</code> to be used for the phrase.
	 */
	private NumberAgreement determineNumber(NLGElement parent,
			PhraseElement phrase) {
		Object numberValue = phrase.getFeature(Feature.NUMBER);
		NumberAgreement number = null;
		if (numberValue != null && numberValue instanceof NumberAgreement) {
			number = (NumberAgreement) numberValue;
		} else {
			number = NumberAgreement.SINGULAR;
		}

		// Ehud Reiter = modified below to force number from VP for WHAT_SUBJECT
		// and WHO_SUBJECT interrogatuves
		if (parent instanceof PhraseElement) {
			if (parent.isA(PhraseCategory.CLAUSE)
					&& (PhraseHelper.isExpletiveSubject((PhraseElement) parent)
							|| PortugueseInterrogativeType.WHO_SUBJECT.equals(parent
									.getFeature(Feature.INTERROGATIVE_TYPE)) || PortugueseInterrogativeType.WHAT_SUBJECT
							.equals(parent
									.getFeature(Feature.INTERROGATIVE_TYPE)))
					&& isCopular(phrase.getHead())) {

				if (hasPluralComplement(phrase
						.getFeatureAsElementList(InternalFeature.COMPLEMENTS))) {
					number = NumberAgreement.PLURAL;
				} else {
					number = NumberAgreement.SINGULAR;
				}
			}
		}
		return number;
	}

	/**
	 * Checks to see if any of the complements to the phrase are plural.
	 * 
	 * @param complements
	 *            the list of complements of the phrase.
	 * @return <code>true</code> if any of the complements are plural.
	 */
	private static boolean hasPluralComplement(List<NLGElement> complements) {
		boolean plural = false;
		Iterator<NLGElement> complementIterator = complements.iterator();
		NLGElement eachComplement = null;
		Object numberValue = null;

		while (complementIterator.hasNext() && !plural) {
			eachComplement = complementIterator.next();

			if (eachComplement != null
					&& eachComplement.isA(PhraseCategory.NOUN_PHRASE)) {

				numberValue = eachComplement.getFeature(Feature.NUMBER);
				if (numberValue != null
						&& NumberAgreement.PLURAL.equals(numberValue)) {
					plural = true;
				}
			}
		}
		return plural;
	}

	/**
	 * Checks to see if the base form of the word is copular, i.e. <em>be</em>.
	 * 
	 * @param element
	 *            the element to be checked
	 * @return <code>true</code> if the element is copular.
	 */
	@Override
	public boolean isCopular(NLGElement element) {
		boolean copular = false;

		if (element instanceof InflectedWordElement) {
			copular = "ser".equalsIgnoreCase(((InflectedWordElement) element) //$NON-NLS-1$
					.getBaseForm());

		} else if (element instanceof WordElement) {
			copular = "ser".equalsIgnoreCase(((WordElement) element) //$NON-NLS-1$
					.getBaseForm());

		} else if (element instanceof PhraseElement) {
			// get the head and check if it's "be"
			NLGElement head = element instanceof AbstractSPhraseSpec ? ((AbstractSPhraseSpec) element)
					.getVerb()
					: ((PhraseElement) element).getHead();

			if (head != null) {
				copular = (head instanceof WordElement && "ser"
						.equals(((WordElement) head).getBaseForm()));
			}
		}

		return copular;
	}
	
	/**
	 * Splits the stack of verb components into two sections. One being the verb
	 * associated with the main verb group, the other being associated with the
	 * auxiliary verb group.
	 * 
	 * @param vgComponents
	 *            the stack of verb components in the verb group.
	 * @param mainVerbRealisation
	 *            the main group of verbs.
	 * @param auxiliaryRealisation
	 *            the auxiliary group of verbs.
	 */
	@Override
	public void splitVerbGroup(Stack<NLGElement> vgComponents,
			Stack<NLGElement> mainVerbRealisation,
			Stack<NLGElement> auxiliaryRealisation) {

		boolean mainVerbSeen = false;

		for (NLGElement word : vgComponents) {
			if (!mainVerbSeen) {
				if (!word.equals("não")) { //$NON-NLS-1$
					mainVerbRealisation.push(word);
					mainVerbSeen = true;
				}else{
					auxiliaryRealisation.push(word);
				}
			} else {
				auxiliaryRealisation.push(word);
			}
		}

	}
	
	private NLGElement addHave(NLGElement frontVG,
			Stack<NLGElement> vgComponents, String modal, Tense tenseValue) {
		NLGElement newFront = frontVG;

		if (frontVG != null) {
			frontVG.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
			vgComponents.push(frontVG);
		}
		newFront = new InflectedWordElement("ter", LexicalCategory.VERB); //$NON-NLS-1$
		newFront.setFeature(Feature.TENSE, tenseValue);
		if (modal != null) {
			newFront.setFeature(InternalFeature.NON_MORPH, true);
		}
		return newFront;
	}
	
	/**
	 * Realises the complements of this phrase.
	 * 
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param realisedElement
	 *            the current realisation of the noun phrase.
	 */
	public void realiseComplements(SyntaxProcessor parent,
			PhraseElement phrase, ListElement realisedElement) {

		ListElement indirects = new ListElement();
		ListElement directs = new ListElement();
		ListElement unknowns = new ListElement();
		Object discourseValue = null;
		NLGElement currentElement = null;

		for (NLGElement complement : phrase
				.getFeatureAsElementList(InternalFeature.COMPLEMENTS)) {

			discourseValue = complement
					.getFeature(InternalFeature.DISCOURSE_FUNCTION);
			currentElement = parent.realise(complement);
			if (currentElement != null) {
				currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
						DiscourseFunction.COMPLEMENT);

				if (DiscourseFunction.INDIRECT_OBJECT.equals(discourseValue)) {
					indirects.addComponent(currentElement);
				} else if (DiscourseFunction.OBJECT.equals(discourseValue)) {
					directs.addComponent(currentElement);
				} else {
					unknowns.addComponent(currentElement);
				}
			}
		}
		
		
		PortugueseInterrogativeType interrogativeType = (PortugueseInterrogativeType) phrase.getFeature(Feature.INTERROGATIVE_TYPE);
		if (!PortugueseInterrogativeType.WHO_INDIRECT_OBJECT.equals(interrogativeType)) {
			realisedElement.addComponents(indirects.getChildren());
		}
		if (!phrase.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) {
			if (!(PortugueseInterrogativeType.WHO_OBJECT.equals(interrogativeType) || PortugueseInterrogativeType.WHAT_OBJECT.equals(interrogativeType))) {
				realisedElement.addComponents(directs.getChildren());
			}
			
			realisedElement.addComponents(unknowns.getChildren());
		}
	}
}
