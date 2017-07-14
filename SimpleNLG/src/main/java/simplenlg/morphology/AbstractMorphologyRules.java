package simplenlg.morphology;

import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Gender;
import simplenlg.features.Inflection;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Person;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGModule;
import simplenlg.framework.StringElement;
import simplenlg.framework.WordElement;

public abstract class AbstractMorphologyRules  extends NLGModule {

	public abstract String[][][] getPronouns();
	public abstract String[] getWHPronouns();
	public abstract List<String> getAuxGerundio();
	public abstract List<String> getNotAuxParticiple();
	public abstract String defaultVerbFormat(Object numberValue, Object personValue, String baseForm);
	public abstract String buildGerundVerb(InflectedWordElement element, WordElement baseWord, Object patternValue, String baseForm);
	public abstract String buildParticiplePresentVerb(InflectedWordElement element, WordElement baseWord, Object patternValue, String baseForm);
	public abstract String buildParticiplePastVerb(InflectedWordElement element, WordElement baseWord, 
														String baseForm, Object number, Object person, Object patternValue);
	public abstract String buildPastVerb(InflectedWordElement element, WordElement baseWord, String baseForm, 
														Object numberValue, Object personValue, Object patternValue);
	public abstract String buildPresentVerb(InflectedWordElement element, WordElement baseWord, 
														String baseForm, Object number, Object person, DiscourseFunction discourseFunction);
	public abstract String buildFutureVerb(InflectedWordElement element, WordElement baseWord, 
			String baseForm, Object number, Object person, DiscourseFunction discourseFunction);
	public abstract String buildRegularPlural(NLGElement baseForm, String word);
	public abstract void checkPossessive(InflectedWordElement element, StringBuffer realised);
	public abstract NLGElement doVerbMorphology(InflectedWordElement element, WordElement baseWord);
	public abstract NLGElement doDeterminerMorphology(InflectedWordElement element);
	public abstract NLGElement doAdjectiveMorphology(InflectedWordElement element, WordElement baseWord);
	public abstract NLGElement doAdverbMorphology(InflectedWordElement element, WordElement baseWord);
	public abstract NLGElement doPrepositionMorphology(InflectedWordElement element, WordElement baseWord);
	public abstract void doDeterminerMorphology(NLGElement determiner, NLGElement postElement,String realisation);
	
	
	/**
	 * This method performs the morphology for nouns.
	 *
	 * @param element
	 *            the <code>InflectedWordElement</code>.
	 * @param baseWord
	 *            the <code>WordElement</code> as created from the lexicon
	 *            entry.
	 * @return a <code>StringElement</code> representing the word after
	 *         inflection.
	 */
	public StringElement doNounMorphology(InflectedWordElement element, WordElement baseWord) {
		StringBuffer realised = new StringBuffer();

		// base form from baseWord if it exists, otherwise from element
		String baseForm = getBaseForm(element, baseWord);

		if(element.isPlural() && !element.getFeatureAsBoolean(LexicalFeature.PROPER).booleanValue()) {

			String pluralForm = null;

			// AG changed: now check if default infl is uncount
			// if (element.getFeatureAsBoolean(LexicalFeature.NON_COUNT)
			// .booleanValue()) {
			// pluralForm = baseForm;
			Object elementDefaultInfl = element.getFeature(LexicalFeature.DEFAULT_INFL);

			if(elementDefaultInfl != null && Inflection.UNCOUNT.equals(elementDefaultInfl)) {
				pluralForm = baseForm;

			} else {
				pluralForm = element.getFeatureAsString(LexicalFeature.PLURAL);
			}

			if(pluralForm == null && baseWord != null) {
				// AG changed: now check if default infl is uncount
				// if (baseWord.getFeatureAsBoolean(LexicalFeature.NON_COUNT)
				// .booleanValue()) {
				// pluralForm = baseForm;
				String baseDefaultInfl = baseWord.getFeatureAsString(LexicalFeature.DEFAULT_INFL);
				if(baseDefaultInfl != null && baseDefaultInfl.equals("uncount")) {
					pluralForm = baseForm;
				} else {
					pluralForm = baseWord.getFeatureAsString(LexicalFeature.PLURAL);
				}
			}

			if(pluralForm == null) {
				Object pattern = element.getFeature(LexicalFeature.DEFAULT_INFL);
				if(Inflection.GRECO_LATIN_REGULAR.equals(pattern)) {
					pluralForm = buildGrecoLatinPluralNoun(baseForm);
				} else {
					pluralForm = buildRegularPlural(element, baseForm);
				}
			}
			realised.append(pluralForm);

		} else {
			realised.append(baseForm);
		}

		checkPossessive(element, realised);
		StringElement realisedElement = new StringElement(realised.toString());
		realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
		                           element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
		return realisedElement;
	}


	/**
	 * Builds a plural for Greco-Latin regular nouns. The rules are performed in
	 * this order:
	 * <ul>
	 * <li>For nouns ending <em>-us</em> the ending becomes <em>-i</em>. For
	 * example, <em>focus</em> becomes <em>foci</em>.</li>
	 * <li>For nouns ending <em>-ma</em> the ending becomes <em>-mata</em>. For
	 * example, <em>trauma</em> becomes <em>traumata</em>.</li>
	 * <li>For nouns ending <em>-a</em> the ending becomes <em>-ae</em>. For
	 * example, <em>larva</em> becomes <em>larvae</em>.</li>
	 * <li>For nouns ending <em>-um</em> or <em>-on</em> the ending becomes
	 * <em>-a</em>. For example, <em>taxon</em> becomes <em>taxa</em>.</li>
	 * <li>For nouns ending <em>-sis</em> the ending becomes <em>-ses</em>. For
	 * example, <em>analysis</em> becomes <em>analyses</em>.</li>
	 * <li>For nouns ending <em>-is</em> the ending becomes <em>-ides</em>. For
	 * example, <em>cystis</em> becomes <em>cystides</em>.</li>
	 * <li>For nouns ending <em>-men</em> the ending becomes <em>-mina</em>. For
	 * example, <em>foramen</em> becomes <em>foramina</em>.</li>
	 * <li>For nouns ending <em>-ex</em> the ending becomes <em>-ices</em>. For
	 * example, <em>index</em> becomes <em>indices</em>.</li>
	 * <li>For nouns ending <em>-x</em> the ending becomes <em>-ces</em>. For
	 * example, <em>matrix</em> becomes <em>matrices</em>.</li>
	 * </ul>
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @return the inflected word.
	 */
	private static String buildGrecoLatinPluralNoun(String baseForm) {
		String plural = null;
		if(baseForm != null) {
			if(baseForm.endsWith("us")) { //$NON-NLS-1$
				plural = baseForm.replaceAll("us\\b", "i"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(baseForm.endsWith("ma")) { //$NON-NLS-1$
				plural = baseForm + "ta"; //$NON-NLS-1$
			} else if(baseForm.endsWith("a")) { //$NON-NLS-1$
				plural = baseForm + "e"; //$NON-NLS-1$
			} else if(baseForm.matches(".*[(um)(on)]\\b")) { //$NON-NLS-1$
				plural = baseForm.replaceAll("[(um)(on)]\\b", "a"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(baseForm.endsWith("sis")) { //$NON-NLS-1$
				plural = baseForm.replaceAll("sis\\b", "ses"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(baseForm.endsWith("is")) { //$NON-NLS-1$
				plural = baseForm.replaceAll("is\\b", "ides"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(baseForm.endsWith("men")) { //$NON-NLS-1$
				plural = baseForm.replaceAll("men\\b", "mina"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(baseForm.endsWith("ex")) { //$NON-NLS-1$
				plural = baseForm.replaceAll("ex\\b", "ices"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(baseForm.endsWith("x")) { //$NON-NLS-1$
				plural = baseForm.replaceAll("x\\b", "ces"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				plural = baseForm;
			}
		}
		return plural;
	}

	/**
	 * return the base form of a word
	 *
	 * @param element
	 * @param baseWord
	 * @return
	 */
	public String getBaseForm(InflectedWordElement element, WordElement baseWord) {
		// unclear what the right behaviour should be
		// for now, prefer baseWord.getBaseForm() to element.getBaseForm() for
		// verbs (ie, "is" mapped to "be")
		// but prefer element.getBaseForm() to baseWord.getBaseForm() for other
		// words (ie, "children" not mapped to "child")

		// AG: changed this to get the default spelling variant
		// needed to preserve spelling changes in the VP

		if(LexicalCategory.VERB == element.getCategory()) {
			if(baseWord != null && baseWord.getDefaultSpellingVariant() != null)
				return baseWord.getDefaultSpellingVariant();
			else
				return element.getBaseForm();
		} else {
			if(element.getBaseForm() != null)
				return element.getBaseForm();
			else if(baseWord == null)
				return null;
			else
				return baseWord.getDefaultSpellingVariant();
		}

		// if (LexicalCategory.VERB == element.getCategory()) {
		// if (baseWord != null && baseWord.getBaseForm() != null)
		// return baseWord.getBaseForm();
		// else
		// return element.getBaseForm();
		// } else {
		// if (element.getBaseForm() != null)
		// return element.getBaseForm();
		// else if (baseWord == null)
		// return null;
		// else
		// return baseWord.getBaseForm();
		// }
	}

	/**
	 * This method performs the morphology for pronouns.
	 *
	 * @param element
	 *            the <code>InflectedWordElement</code>.
	 * @return a <code>StringElement</code> representing the word after
	 *         inflection.
	 */
	public NLGElement doPronounMorphology(InflectedWordElement element) {
		String realised = null;

		if(!element.getFeatureAsBoolean(InternalFeature.NON_MORPH).booleanValue() && !isWHPronoun(element)) {
			realised = getPronoun(element);
		} else {
			realised = element.getBaseForm();
		}
		StringElement realisedElement = new StringElement(realised);
		realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
		                           element.getFeature(InternalFeature.DISCOURSE_FUNCTION));

		return realisedElement;
	}
	
	public String getPronoun(InflectedWordElement element) {
		String realised;
		Object genderValue = element.getFeature(LexicalFeature.GENDER);
		Object personValue = element.getFeature(Feature.PERSON);
		Object discourseValue = element.getFeature(InternalFeature.DISCOURSE_FUNCTION);

		int numberIndex = element.isPlural() ? 1 : 0;
		int genderIndex = (genderValue instanceof Gender) ? ((Gender) genderValue).ordinal() : 2;

		int personIndex = (personValue instanceof Person) ? ((Person) personValue).ordinal() : 2;

		if(personIndex == 2) {
			personIndex += genderIndex;
		}

		int positionIndex = 0;

		if(element.getFeatureAsBoolean(LexicalFeature.REFLEXIVE).booleanValue()) {
			positionIndex = 2;
		} else if(element.getFeatureAsBoolean(Feature.POSSESSIVE).booleanValue()) {
			positionIndex = 3;
			if(DiscourseFunction.SPECIFIER.equals(discourseValue)) {
				positionIndex++;
			}
		} else {
			positionIndex = (DiscourseFunction.SUBJECT.equals(discourseValue) && 
							!element.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) || 
							(DiscourseFunction.OBJECT.equals(discourseValue) && element.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) || 
							DiscourseFunction.SPECIFIER.equals(discourseValue) || 
							(DiscourseFunction.COMPLEMENT.equals(discourseValue) && element.getFeatureAsBoolean(Feature.PASSIVE).booleanValue()) ? 0 : 1;
		}
		realised = getPronouns()[numberIndex][positionIndex][personIndex];
		return realised;
	}

	private boolean isWHPronoun(InflectedWordElement word) {
		String base = word.getBaseForm();
		boolean wh = false;

		if(base != null) {
			for(int i = 0; i < getWHPronouns().length && !wh; i++) {
				wh = getWHPronouns()[i].equals(base);
			}
		}

		return wh;

	}
}

