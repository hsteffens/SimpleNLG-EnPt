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
package simplenlg.morphology.english;

import java.util.ArrayList;
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.Inflection;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.StringElement;
import simplenlg.framework.WordElement;
import simplenlg.morphology.AbstractMorphologyRules;

/**
 * <p>
 * This abstract class contains a number of rules for doing simple inflection.
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
 * @version 4.0 16-Mar-2011 modified to use correct base form (ER)
 */
public class EnglishMorphologyRules extends AbstractMorphologyRules {

	/**
	 * A triple array of Pronouns organised by singular/plural,
	 * possessive/reflexive/subjective/objective and by gender/person.
	 */
	@SuppressWarnings("nls")
	private static final String[][][] PRONOUNS = {{{"I", "you", "he", "she", "it"},
		{"me", "you", "him", "her", "it"},
		{"myself", "yourself", "himself", "herself", "itself"},
		{"mine", "yours", "his", "hers", "its"},
		{"my", "your", "his", "her", "its"}},
			{{"we", "you", "they", "they", "they"},
			{"us", "you", "them", "them", "them"},
			{"ourselves",
				"yourselves",
				"themselves",
				"themselves",
			"themselves"},
			{"ours", "yours", "theirs", "theirs", "theirs"},
			{"our", "your", "their", "their", "their"}}};

	private static final String[] WH_PRONOUNS = {"who", "what", "which", "where", "why", "how", "how many"};

	private static final List<String> AUX_GERUNDIO = new ArrayList<String>(0);
	private static final List<String> NOT_AUX_PARTICIPLE = new ArrayList<String>(0);
	
	@Override
	public String[][][] getPronouns() {
		return PRONOUNS;
	}

	@Override
	public String[] getWHPronouns() {
		return WH_PRONOUNS;
	}

	@Override
	public void initialise() {
		// TODO Auto-generated method stub

	}

	@Override
	public NLGElement realise(NLGElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NLGElement> realise(List<NLGElement> elements) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * This method performs the morphology for verbs.
	 *
	 * @param element
	 *            the <code>InflectedWordElement</code>.
	 * @param baseWord
	 *            the <code>WordElement</code> as created from the lexicon
	 *            entry.
	 * @return a <code>StringElement</code> representing the word after
	 *         inflection.
	 */
	@Override
	public NLGElement doVerbMorphology(InflectedWordElement element, WordElement baseWord) {

		String realised = null;
		Object numberValue = element.getFeature(Feature.NUMBER);
		Object personValue = element.getFeature(Feature.PERSON);
		Object tense = element.getFeature(Feature.TENSE);
		Tense tenseValue;

		// AG: change to avoid deprecated getTense
		// if tense value is Tense, cast it, else default to present
		if(tense instanceof Tense) {
			tenseValue = (Tense) tense;
		} else {
			tenseValue = Tense.PRESENT;
		}

		Object formValue = element.getFeature(Feature.FORM);
		Object patternValue = element.getFeature(LexicalFeature.DEFAULT_INFL);
		DiscourseFunction discourseFunction = (DiscourseFunction) element.getFeature(InternalFeature.DISCOURSE_FUNCTION);
		
		// base form from baseWord if it exists, otherwise from element
		String baseForm = getBaseForm(element, baseWord);

		if(element.getFeatureAsBoolean(Feature.NEGATED) || Form.BARE_INFINITIVE.equals(formValue)) {
			realised = baseForm;
		} else if(Form.GERUND.equals(formValue)){
			realised = buildGerundVerb(element, baseWord, patternValue, baseForm);
		} else if(Form.PRESENT_PARTICIPLE.equals(formValue)) {
			realised = buildParticiplePresentVerb(element, baseWord, patternValue, baseForm);
		}else if(Form.PAST_PARTICIPLE.equals(formValue)){
			realised = buildParticiplePastVerb(element, baseWord, baseForm, numberValue, personValue, patternValue);
		} else if(Tense.PAST.equals(tenseValue)) {
			realised = buildPastVerb(element, baseWord, baseForm, numberValue, personValue, patternValue);
		} else if(tenseValue != null && Tense.PRESENT.equals(tenseValue)) {
			realised = buildPresentVerb(element, baseWord, baseForm, numberValue, personValue, discourseFunction);
		} else if (tenseValue == null || Tense.FUTURE.equals(tenseValue)) {
			realised = buildFutureVerb(element, baseWord, baseForm, numberValue, personValue, discourseFunction);
		} else {
			realised = defaultVerbFormat(numberValue, personValue, baseForm);
		}
		StringElement realisedElement = new StringElement(realised);
		realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
		                           element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
		return realisedElement;
	}
	
	@Override
	public String defaultVerbFormat(Object numberValue, Object personValue, String baseForm) {
		String realised;
		if("be".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
			if(Person.FIRST.equals(personValue) && (NumberAgreement.SINGULAR.equals(numberValue)
			                                        || numberValue == null)) {
				realised = "am"; //$NON-NLS-1$
			} else {
				realised = "are"; //$NON-NLS-1$
			}
		} else {
			realised = baseForm;
		}
		return realised;
	}
	
	/**
	 * Builds the third-person singular form for regular verbs. The rules are
	 * performed in this order:
	 * <ul>
	 * <li>If the verb is <em>be</em> the realised form is <em>is</em>.</li>
	 * <li>For verbs ending <em>-ch</em>, <em>-s</em>, <em>-sh</em>, <em>-x</em>
	 * or <em>-z</em> the ending becomes <em>-es</em>. For example,
	 * <em>preach</em> becomes <em>preaches</em>.</li>
	 * <li>For verbs ending <em>-y</em> the ending becomes <em>-ies</em>. For
	 * example, <em>fly</em> becomes <em>flies</em>.</li>
	 * <li>For every other verb, <em>-s</em> is added to the end of the word.</li>
	 * </ul>
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @return the inflected word.
	 */
	private String buildPresent3SVerb(String baseForm) {
		String morphology = null;
		if(baseForm != null) {
			if(baseForm.equalsIgnoreCase("be")) { //$NON-NLS-1$
				morphology = "is"; //$NON-NLS-1$
			} else if(baseForm.matches(".*[szx(ch)(sh)]\\b")) { //$NON-NLS-1$
				morphology = baseForm + "es"; //$NON-NLS-1$
			} else if(baseForm.matches(".*[b-z&&[^eiou]]y\\b")) { //$NON-NLS-1$
				morphology = baseForm.replaceAll("y\\b", "ies"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				morphology = baseForm + "s"; //$NON-NLS-1$
			}
		}
		return morphology;
	}
	
	/**
	 * Builds the past-tense form for regular verbs. The rules are performed in
	 * this order:
	 * <ul>
	 * <li>If the verb is <em>be</em> and the number agreement is plural then
	 * the realised form is <em>were</em>.</li>
	 * <li>If the verb is <em>be</em> and the number agreement is singular then
	 * the realised form is <em>was</em>, unless the person is second, in which
	 * case it's <em>were</em>.</li>
	 * <li>For verbs ending <em>-e</em> the ending becomes <em>-ed</em>. For
	 * example, <em>chased</em> becomes <em>chased</em>.</li>
	 * <li>For verbs ending <em>-Cy</em>, where C is any consonant, the ending
	 * becomes <em>-ied</em>. For example, <em>dry</em> becomes <em>dried</em>.</li>
	 * <li>For every other verb, <em>-ed</em> is added to the end of the word.</li>
	 * </ul>
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @param number
	 *            the number agreement for the word.
	 * @param person
	 *            the person
	 * @return the inflected word.
	 */
	@Override
	public String buildPastVerb(InflectedWordElement element, WordElement baseWord, String baseForm, Object numberValue, Object personValue, Object patternValue) {
		String realised = element.getFeatureAsString(LexicalFeature.PAST);

		if(realised == null && baseWord != null) {
			if (Person.FIRST.equals(personValue)) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PAST1S);
			}else if (realised == null && Person.SECOND.equals(personValue)) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PAST2S);
			}else if (realised == null && Person.THIRD.equals(personValue)) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PAST3S);
			}
			
			if (realised == null) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PAST);
			}
		}

		if(realised == null) {
			if(Inflection.REGULAR_DOUBLE.equals(patternValue)) {
				realised = buildDoublePastVerb(baseForm);
			} else {
				realised = buildRegularPastVerb(baseForm, numberValue, personValue);
			}
		}
		return realised;
	}
	
	private String buildRegularPastVerb(String baseForm, Object number, Object person) {
		String morphology = null;
		if(baseForm != null) {
			if(baseForm.equalsIgnoreCase("be")) { //$NON-NLS-1$
				if(NumberAgreement.PLURAL.equals(number)) {
					morphology = "were"; //$NON-NLS-1$

					// AG - bug fix to handle second person past (courtesy of
					// Minh Le)
				} else if(Person.SECOND.equals(person)) {
					morphology = "were"; //$NON-NLS-1$
				} else {
					morphology = "was";
				}
			} else if(baseForm.endsWith("e")) { //$NON-NLS-1$
				morphology = baseForm + "d"; //$NON-NLS-1$
			} else if(baseForm.matches(".*[b-z&&[^eiou]]y\\b")) { //$NON-NLS-1$
				morphology = baseForm.replaceAll("y\\b", "ied"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				morphology = baseForm + "ed"; //$NON-NLS-1$
			}
		}
		return morphology;
	}
	
	/**
	 * Builds a plural for regular nouns. The rules are performed in this order:
	 * <ul>
	 * <li>For nouns ending <em>-Cy</em>, where C is any consonant, the ending
	 * becomes <em>-ies</em>. For example, <em>fly</em> becomes <em>flies</em>.</li>
	 * <li>For nouns ending <em>-ch</em>, <em>-s</em>, <em>-sh</em>, <em>-x</em>
	 * or <em>-z</em> the ending becomes <em>-es</em>. For example, <em>box</em>
	 * becomes <em>boxes</em>.</li>
	 * <li>All other nouns have <em>-s</em> appended the other end. For example,
	 * <em>dog</em> becomes <em>dogs</em>.</li>
	 * </ul>
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @return the inflected word.
	 */
	@Override
	public String buildRegularPlural(NLGElement element, String baseForm) {
		String plural = null;
		if(baseForm != null) {
			if(baseForm.matches(".*[b-z&&[^eiou]]y\\b")) { //$NON-NLS-1$
				plural = baseForm.replaceAll("y\\b", "ies"); //$NON-NLS-1$ //$NON-NLS-2$

				//AG: changed regex from ".*[szx(ch)(sh)]\\b" (tip of the hat to Ian Tabolt)				
			} else if(baseForm.matches(".*([szx]|[cs]h)\\b")) { //$NON-NLS-1$
				plural = baseForm + "es"; //$NON-NLS-1$

			} else {
				plural = baseForm + "s"; //$NON-NLS-1$
			}
		}
		return plural;
	}

	@Override
	public String buildPresentVerb(InflectedWordElement element, WordElement baseWord, String baseForm, Object numberValue, Object personValue, DiscourseFunction discourseFunction) {
		String realised = null;
		if((numberValue == null || NumberAgreement.SINGULAR.equals(numberValue)) && 
				(personValue == null || Person.THIRD.equals(personValue)) && 
				(discourseFunction == null || (!discourseFunction.equals(DiscourseFunction.OBJECT) && !discourseFunction.equals(DiscourseFunction.COMPLEMENT)))) {

			realised = element.getFeatureAsString(LexicalFeature.PRESENT3S);

			if(realised == null && baseWord != null && !"be".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT3S);
			}
			if(realised == null) {
				realised = buildPresent3SVerb(baseForm);
			}

		} else if((numberValue == null || NumberAgreement.SINGULAR.equals(numberValue)) && 
				(personValue == null || Person.SECOND.equals(personValue)) && 
				(discourseFunction == null || (!discourseFunction.equals(DiscourseFunction.OBJECT) && !discourseFunction.equals(DiscourseFunction.COMPLEMENT)))) {

			realised = element.getFeatureAsString(LexicalFeature.PRESENT2S);

			if(realised == null && baseWord != null && !"be".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT2S);
			}
			if(realised == null) {
				realised = defaultVerbFormat(numberValue, personValue, baseForm);
			}

		}  else if((numberValue == null || NumberAgreement.SINGULAR.equals(numberValue)) && 
				(personValue == null || Person.FIRST.equals(personValue)) && 
				(discourseFunction == null || (!discourseFunction.equals(DiscourseFunction.OBJECT) && !discourseFunction.equals(DiscourseFunction.COMPLEMENT)))) {

			realised = element.getFeatureAsString(LexicalFeature.PRESENT1S);

			if(realised == null && baseWord != null && !"be".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT1S);
			}

			if(realised == null) {
				realised = defaultVerbFormat(numberValue, personValue, baseForm);
			}

		}  else if (realised == null) {
			if("be".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				if(Person.FIRST.equals(personValue) && (NumberAgreement.SINGULAR.equals(numberValue)
				                                        || numberValue == null)) {
					realised = "am"; //$NON-NLS-1$
				} else {
					realised = "are"; //$NON-NLS-1$
				}
			} else {
				realised = baseForm;
			}
		}

		return realised;
	}

	@Override
	public String buildFutureVerb(InflectedWordElement element, WordElement baseWord, String baseForm,
			Object numberValue, Object personValue, DiscourseFunction discourseFunction) {
		String realised = null;
		if((numberValue == null || NumberAgreement.SINGULAR.equals(numberValue)) && 
				(personValue == null || Person.FIRST.equals(personValue)) && 
				(discourseFunction == null || (!discourseFunction.equals(DiscourseFunction.OBJECT) && !discourseFunction.equals(DiscourseFunction.COMPLEMENT)))) {
			
			realised = element.getFeatureAsString(LexicalFeature.FUTURE1S);

			if(realised == null && baseWord != null && !"be".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.FUTURE1S);
			}
			
			if(realised == null) {
				realised = defaultVerbFormat(numberValue, personValue, baseForm);
			}
			
		} else if((numberValue == null || NumberAgreement.SINGULAR.equals(numberValue)) && 
				(personValue == null || Person.SECOND.equals(personValue)) && 
				(discourseFunction == null || (!discourseFunction.equals(DiscourseFunction.OBJECT) && !discourseFunction.equals(DiscourseFunction.COMPLEMENT)))) {
			
			realised = element.getFeatureAsString(LexicalFeature.FUTURE2S);

			if(realised == null && baseWord != null && !"be".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.FUTURE2S);
			}
			
			if(realised == null) {
				realised = defaultVerbFormat(numberValue, personValue, baseForm);
			}
			
		} else if((numberValue == null || NumberAgreement.SINGULAR.equals(numberValue)) && 
				(personValue == null || Person.THIRD.equals(personValue)) && 
				(discourseFunction == null || (!discourseFunction.equals(DiscourseFunction.OBJECT) && !discourseFunction.equals(DiscourseFunction.COMPLEMENT)))) {
			
			realised = element.getFeatureAsString(LexicalFeature.FUTURE3S);

			if(realised == null && baseWord != null && !"be".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.FUTURE3S);
			}
			
			if(realised == null) {
				realised = defaultVerbFormat(numberValue, personValue, baseForm);
			}
			
		}
		return realised;
	}

	@Override
	public String buildParticiplePastVerb(InflectedWordElement element, WordElement baseWord, String baseForm, 
													Object numberValue, Object personValue, Object patternValue) {
		String realised = element.getFeatureAsString(LexicalFeature.PAST_PARTICIPLE);

		if(realised == null && baseWord != null) {
			realised = baseWord.getFeatureAsString(LexicalFeature.PAST_PARTICIPLE);
		}

		if(realised == null) {
			if("be".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = "been"; //$NON-NLS-1$
			} else if(Inflection.REGULAR_DOUBLE.equals(patternValue)) {
				realised = buildDoublePastVerb(baseForm);
			} else {
				realised = buildRegularPastVerb(baseForm, numberValue, personValue);
			}
		}
		return realised;
	}
	
	/**
	 * Builds the past-tense form for verbs that follow the doubling form of the
	 * last consonant. <em>-ed</em> is added to the end after the last consonant
	 * is doubled. For example, <em>tug</em> becomes <em>tugged</em>.
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @return the inflected word.
	 */
	private static String buildDoublePastVerb(String baseForm) {
		String morphology = null;
		if(baseForm != null) {
			morphology = baseForm + baseForm.charAt(baseForm.length() - 1) + "ed"; //$NON-NLS-1$
		}
		return morphology;
	}

	@Override
	public String buildParticiplePresentVerb(InflectedWordElement element, WordElement baseWord, Object patternValue,
			String baseForm) {
		String realised;
		realised = element.getFeatureAsString(LexicalFeature.PRESENT_PARTICIPLE);

		if(realised == null && baseWord != null) {
			realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT_PARTICIPLE);
		}

		if(realised == null) {
			if(Inflection.REGULAR_DOUBLE.equals(patternValue)) {
				realised = buildDoublePresPartVerb(baseForm);
			} else {
				realised = buildRegularPresPartVerb(baseForm);
			}
		}
		return realised;
	}
	
	/**
	 * Builds the present participle form for regular verbs. The rules are
	 * performed in this order:
	 * <ul>
	 * <li>If the verb is <em>be</em> then the realised form is <em>being</em>.</li>
	 * <li>For verbs ending <em>-ie</em> the ending becomes <em>-ying</em>. For
	 * example, <em>tie</em> becomes <em>tying</em>.</li>
	 * <li>For verbs ending <em>-ee</em>, <em>-oe</em> or <em>-ye</em> then
	 * <em>-ing</em> is added to the end. For example, <em>canoe</em> becomes
	 * <em>canoeing</em>.</li>
	 * <li>For other verbs ending in <em>-e</em> the ending becomes
	 * <em>-ing</em>. For example, <em>chase</em> becomes <em>chasing</em>.</li>
	 * <li>For all other verbs, <em>-ing</em> is added to the end. For example,
	 * <em>dry</em> becomes <em>drying</em>.</li>
	 * </ul>
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @return the inflected word.
	 */
	private static String buildRegularPresPartVerb(String baseForm) {
		String morphology = null;
		if(baseForm != null) {
			if(baseForm.equalsIgnoreCase("be")) { //$NON-NLS-1$
				morphology = "being"; //$NON-NLS-1$
			} else if(baseForm.endsWith("ie")) { //$NON-NLS-1$
				morphology = baseForm.replaceAll("ie\\b", "ying"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(baseForm.matches(".*[^iyeo]e\\b")) { //$NON-NLS-1$
				morphology = baseForm.replaceAll("e\\b", "ing"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				morphology = baseForm + "ing"; //$NON-NLS-1$
			}
		}
		return morphology;
	}

	/**
	 * Builds the present participle form for verbs that follow the doubling
	 * form of the last consonant. <em>-ing</em> is added to the end after the
	 * last consonant is doubled. For example, <em>tug</em> becomes
	 * <em>tugging</em>.
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @return the inflected word.
	 */
	private static String buildDoublePresPartVerb(String baseForm) {
		String morphology = null;
		if(baseForm != null) {
			morphology = baseForm + baseForm.charAt(baseForm.length() - 1) + "ing"; //$NON-NLS-1$
		}
		return morphology;
	}

	@Override
	public String buildGerundVerb(InflectedWordElement element, WordElement baseWord, Object patternValue,
			String baseForm) {
		return baseForm;
	}

	@Override
	public List<String> getAuxGerundio() {
		return AUX_GERUNDIO;
	}

	@Override
	public List<String> getNotAuxParticiple() {
		return NOT_AUX_PARTICIPLE;
	}
	
	/**
	 * Checks to see if the noun is possessive. If it is then nouns in ending in
	 * <em>-s</em> become <em>-s'</em> while every other noun has <em>-'s</em> appended to
	 * the end.
	 *
	 * @param element
	 *            the <code>InflectedWordElement</code>
	 * @param realised
	 *            the realisation of the word.
	 */
	@Override
	public void checkPossessive(InflectedWordElement element, StringBuffer realised) {

		if(element.getFeatureAsBoolean(Feature.POSSESSIVE).booleanValue()) {
			if(realised.charAt(realised.length() - 1) == 's') {
				realised.append('\'');

			} else {
				realised.append("'s"); //$NON-NLS-1$
			}
		}
	}

	@Override
	public NLGElement doDeterminerMorphology(InflectedWordElement element) {
		return doDefaultMorphology(element);
	}

	private NLGElement doDefaultMorphology(InflectedWordElement element) {
		NLGElement realisedElement = new StringElement(element.getBaseForm());
		realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
				element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
		
		return realisedElement;
	}
	
	/**
	 * This method performs the morphology for adjectives.
	 *
	 * @param element
	 *            the <code>InflectedWordElement</code>.
	 * @param baseWord
	 *            the <code>WordElement</code> as created from the lexicon
	 *            entry.
	 * @return a <code>StringElement</code> representing the word after
	 *         inflection.
	 */
	@Override
	public NLGElement doAdjectiveMorphology(InflectedWordElement element, WordElement baseWord) {

		String realised = null;
		Object patternValue = element.getFeature(LexicalFeature.DEFAULT_INFL);

		// base form from baseWord if it exists, otherwise from element
		String baseForm = getBaseForm(element, baseWord);

		if(element.getFeatureAsBoolean(Feature.IS_COMPARATIVE).booleanValue()) {
			realised = element.getFeatureAsString(LexicalFeature.COMPARATIVE);

			if(realised == null && baseWord != null) {
				realised = baseWord.getFeatureAsString(LexicalFeature.COMPARATIVE);
			}
			if(realised == null) {
				if(Inflection.REGULAR_DOUBLE.equals(patternValue)) {
					realised = buildDoubleCompAdjective(baseForm);
				} else {
					realised = buildRegularComparative(baseForm);
				}
			}
		} else if(element.getFeatureAsBoolean(Feature.IS_SUPERLATIVE).booleanValue()) {

			realised = element.getFeatureAsString(LexicalFeature.SUPERLATIVE);

			if(realised == null && baseWord != null) {
				realised = baseWord.getFeatureAsString(LexicalFeature.SUPERLATIVE);
			}
			if(realised == null) {
				if(Inflection.REGULAR_DOUBLE.equals(patternValue)) {
					realised = buildDoubleSuperAdjective(baseForm);
				} else {
					realised = buildRegularSuperlative(baseForm);
				}
			}
		} else {
			realised = baseForm;
		}
		StringElement realisedElement = new StringElement(realised);
		realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
		                           element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
		return realisedElement;
	}
	
	/**
	 * This method performs the morphology for adverbs.
	 *
	 * @param element
	 *            the <code>InflectedWordElement</code>.
	 * @param baseWord
	 *            the <code>WordElement</code> as created from the lexicon
	 *            entry.
	 * @return a <code>StringElement</code> representing the word after
	 *         inflection.
	 */
	@Override
	public NLGElement doAdverbMorphology(InflectedWordElement element, WordElement baseWord) {

		String realised = null;

		// base form from baseWord if it exists, otherwise from element
		String baseForm = getBaseForm(element, baseWord);

		if(element.getFeatureAsBoolean(Feature.IS_COMPARATIVE).booleanValue()) {
			realised = element.getFeatureAsString(LexicalFeature.COMPARATIVE);

			if(realised == null && baseWord != null) {
				realised = baseWord.getFeatureAsString(LexicalFeature.COMPARATIVE);
			}
			if(realised == null) {
				realised = buildRegularComparative(baseForm);
			}
		} else if(element.getFeatureAsBoolean(Feature.IS_SUPERLATIVE).booleanValue()) {

			realised = element.getFeatureAsString(LexicalFeature.SUPERLATIVE);

			if(realised == null && baseWord != null) {
				realised = baseWord.getFeatureAsString(LexicalFeature.SUPERLATIVE);
			}
			if(realised == null) {
				realised = buildRegularSuperlative(baseForm);
			}
		} else {
			realised = baseForm;
		}
		StringElement realisedElement = new StringElement(realised);
		realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
		                           element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
		return realisedElement;
	}
	
	/**
	 * Builds the comparative form for adjectives that follow the doubling form
	 * of the last consonant. <em>-er</em> is added to the end after the last
	 * consonant is doubled. For example, <em>fat</em> becomes <em>fatter</em>.
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @return the inflected word.
	 */
	private static String buildDoubleCompAdjective(String baseForm) {
		String morphology = null;
		if(baseForm != null) {
			morphology = baseForm + baseForm.charAt(baseForm.length() - 1) + "er"; //$NON-NLS-1$
		}
		return morphology;
	}
	
	/**
	 * Builds the superlative form for regular adjectives. The rules are
	 * performed in this order:
	 * <ul>
	 * <li>For verbs ending <em>-Cy</em>, where C is any consonant, the ending
	 * becomes <em>-iest</em>. For example, <em>brainy</em> becomes
	 * <em>brainiest</em>.</li>
	 * <li>For verbs ending <em>-e</em> the ending becomes <em>-est</em>. For
	 * example, <em>fine</em> becomes <em>finest</em>.</li>
	 * <li>For all other verbs, <em>-est</em> is added to the end. For example,
	 * <em>clear</em> becomes <em>clearest</em>.</li>
	 * </ul>
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @return the inflected word.
	 */
	private String buildRegularSuperlative(String baseForm) {
		String morphology = null;
		if(baseForm != null) {
			if(baseForm.matches(".*[b-z&&[^eiou]]y\\b")) { //$NON-NLS-1$
				morphology = baseForm.replaceAll("y\\b", "iest"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(baseForm.endsWith("e")) { //$NON-NLS-1$
				morphology = baseForm + "st"; //$NON-NLS-1$
			} else {
				morphology = baseForm + "est"; //$NON-NLS-1$
			}
		}
		return morphology;
	}
	
	/**
	 * Builds the comparative form for regular adjectives. The rules are
	 * performed in this order:
	 * <ul>
	 * <li>For adjectives ending <em>-Cy</em>, where C is any consonant, the
	 * ending becomes <em>-ier</em>. For example, <em>brainy</em> becomes
	 * <em>brainier</em>.</li>
	 * <li>For adjectives ending <em>-e</em> the ending becomes <em>-er</em>.
	 * For example, <em>fine</em> becomes <em>finer</em>.</li>
	 * <li>For all other adjectives, <em>-er</em> is added to the end. For
	 * example, <em>clear</em> becomes <em>clearer</em>.</li>
	 * </ul>
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @return the inflected word.
	 */
	private static String buildRegularComparative(String baseForm) {
		String morphology = null;
		if(baseForm != null) {
			if(baseForm.matches(".*[b-z&&[^eiou]]y\\b")) { //$NON-NLS-1$
				morphology = baseForm.replaceAll("y\\b", "ier"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if(baseForm.endsWith("e")) { //$NON-NLS-1$
				morphology = baseForm + "r"; //$NON-NLS-1$
			} else {
				morphology = baseForm + "er"; //$NON-NLS-1$
			}
		}
		return morphology;
	}

	/**
	 * Builds the superlative form for adjectives that follow the doubling form
	 * of the last consonant. <em>-est</em> is added to the end after the last
	 * consonant is doubled. For example, <em>fat</em> becomes <em>fattest</em>.
	 *
	 * @param baseForm
	 *            the base form of the word.
	 * @return the inflected word.
	 */
	private static String buildDoubleSuperAdjective(String baseForm) {
		String morphology = null;
		if(baseForm != null) {
			morphology = baseForm + baseForm.charAt(baseForm.length() - 1) + "est"; //$NON-NLS-1$
		}
		return morphology;
	}

	@Override
	public NLGElement doPrepositionMorphology(InflectedWordElement element, WordElement baseWord) {
		return doDefaultMorphology(element);
	}
	
	/**
	 * This method performs the morphology for determiners.
	 *
	 * @param determiner
	 *            the <code>InflectedWordElement</code>.
	 * @param realisation
	 *            the current realisation of the determiner.
	 */
	@Override
	public void doDeterminerMorphology(NLGElement determiner, NLGElement postElement, String realisation) {

		if(realisation != null) {

			if(!(determiner.getRealisation().equals("a"))) {
				if(determiner.isPlural()) {
					// Use default inflection rules:
					if("that".equals(determiner.getRealisation())) {
						determiner.setRealisation("those");
					} else if("this".equals(determiner.getRealisation())) {
						determiner.setRealisation("these");
					}
				} else if(!determiner.isPlural()) {
					// Use default push back to base form rules:
					if("those".equals(determiner.getRealisation())) {
						determiner.setRealisation("that");
					} else if("these".equals(determiner.getRealisation())) {
						determiner.setRealisation("this");
					}

				}
			}

			// Special "a" determiner and perform a/an agreement:
			if(determiner.getRealisation().equals("a")) { //$NON-NLS-1$
				if(determiner.isPlural()) {
					determiner.setRealisation("some");
				} else if(DeterminerAgrHelper.requiresAn(realisation)) {
					determiner.setRealisation("an");
				}
			}

		}
	}

}
