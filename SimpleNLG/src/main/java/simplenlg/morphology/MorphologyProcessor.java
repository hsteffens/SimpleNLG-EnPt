/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is "Simplenlg".
 *
 * The Initial Developer of the Original Code is Ehud Reiter, Albert Gatt and Dave Westwater.
 * Portions created by Ehud Reiter, Albert Gatt and Dave Westwater are Copyright (C) 2010-11 The University of Aberdeen. All Rights Reserved.
 *
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.morphology;

import java.util.List;

import simplenlg.features.InternalFeature;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.ElementCategory;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGModule;
import simplenlg.framework.StringElement;
import simplenlg.framework.WordElement;

/**
 * <p>
 * This is the processor for handling morphology within the SimpleNLG. The
 * processor inflects words form the base form depending on the features applied
 * to the word. For example, <em>kiss</em> is inflected to <em>kissed</em> for
 * past tense, <em>dog</em> is inflected to <em>dogs</em> for pluralisation.
 * </p>
 *
 * <p>
 * As a matter of course, the processor will first use any user-defined
 * inflection for the world. If no inflection is provided then the lexicon, if
 * it exists, will be examined for the correct inflection. Failing this a set of
 * very basic rules will be examined to inflect the word.
 * </p>
 *
 * <p>
 * All processing modules perform realisation on a tree of
 * <code>NLGElement</code>s. The modules can alter the tree in whichever way
 * they wish. For example, the syntax processor replaces phrase elements with
 * list elements consisting of inflected words while the morphology processor
 * replaces inflected words with string elements.
 * </p>
 *
 * <p>
 * <b>N.B.</b> the use of <em>module</em>, <em>processing module</em> and
 * <em>processor</em> is interchangeable. They all mean an instance of this
 * class.
 * </p>
 *
 *
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 */
public abstract class MorphologyProcessor extends NLGModule {

	@Override
	public void initialise() {
		// Do nothing
	}
	
	public abstract List<NLGElement> realise(List<NLGElement> elements);
	public abstract AbstractMorphologyRules getMorphologyRules();

	@Override
	public NLGElement realise(NLGElement element) {
		NLGElement realisedElement = null;

		if(element instanceof InflectedWordElement) {
			realisedElement = doMorphology((InflectedWordElement) element);

		} else if(element instanceof StringElement) {
			realisedElement = element;

		} else if(element instanceof WordElement) {
			// AG: now retrieves the default spelling variant, not the baseform
			// String baseForm = ((WordElement) element).getBaseForm();
			String defaultSpell = ((WordElement) element).getDefaultSpellingVariant();

			if(defaultSpell != null) {
				realisedElement = new StringElement(defaultSpell);
			}

		} else if(element instanceof DocumentElement) {
			List<NLGElement> children = element.getChildren();
			((DocumentElement) element).setComponents(realise(children));
			realisedElement = element;

		} else if(element instanceof ListElement) {
			realisedElement = new ListElement();
			((ListElement) realisedElement).addComponents(realise(element.getChildren()));

		} else if(element instanceof AbstractCoordinatedPhraseElement) {
			List<NLGElement> children = element.getChildren();
			((AbstractCoordinatedPhraseElement) element).clearCoordinates();

			if(children != null && children.size() > 0) {
				((AbstractCoordinatedPhraseElement) element).addCoordinate(realise(children.get(0)));

				for(int index = 1; index < children.size(); index++) {
					((AbstractCoordinatedPhraseElement) element).addCoordinate(realise(children.get(index)));
				}

				realisedElement = element;
			}

		} else if(element != null) {
			realisedElement = element;
		}

		return realisedElement;
	}

	/**
	 * This is the main method for performing the morphology. It effectively
	 * examines the lexical category of the element and calls the relevant set
	 * of rules from <code>MorphologyRules</em>.
	 *
	 * @param element
	 *            the <code>InflectedWordElement</code>
	 * @return an <code>NLGElement</code> reflecting the correct inflection for
	 *         the word.
	 */
	private NLGElement doMorphology(InflectedWordElement element) {
		NLGElement realisedElement = null;
		if(element.getFeatureAsBoolean(InternalFeature.NON_MORPH).booleanValue()) {
			realisedElement = new StringElement(element.getBaseForm());
			realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
			                           element.getFeature(InternalFeature.DISCOURSE_FUNCTION));

		} else {
			NLGElement baseWord = element.getFeatureAsElement(InternalFeature.BASE_WORD);

			if(baseWord == null && this.lexicon != null) {
				baseWord = this.lexicon.lookupWord(element.getBaseForm());
			}

			ElementCategory category = element.getCategory();

			if(category instanceof LexicalCategory) {
				switch((LexicalCategory) category){
				case PRONOUN:
					realisedElement = getMorphologyRules().doPronounMorphology(element);
					break;

				case NOUN:
					realisedElement = getMorphologyRules().doNounMorphology(element, (WordElement) baseWord);
					break;

				case VERB:
					realisedElement = getMorphologyRules().doVerbMorphology(element, (WordElement) baseWord);
					break;

				case ADJECTIVE:
					realisedElement = getMorphologyRules().doAdjectiveMorphology(element, (WordElement) baseWord);
					break;

				case ADVERB:
					realisedElement = getMorphologyRules().doAdverbMorphology(element, (WordElement) baseWord);
					break;
				
				case DETERMINER:
					realisedElement = getMorphologyRules().doDeterminerMorphology(element);
					break;
					
				case PREPOSITION:
					realisedElement = getMorphologyRules().doPrepositionMorphology(element, (WordElement) baseWord);
					break;
					
				default:
					realisedElement = new StringElement(element.getBaseForm());
					realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
							element.getFeature(InternalFeature.DISCOURSE_FUNCTION));

				}
			}
		}
		return realisedElement;
	}

}
