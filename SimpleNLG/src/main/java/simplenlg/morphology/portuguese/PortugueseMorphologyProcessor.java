package simplenlg.morphology.portuguese;

import java.util.ArrayList;
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.InternalFeature;
import simplenlg.features.Tense;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.StringElement;
import simplenlg.framework.WordElement;
import simplenlg.morphology.AbstractMorphologyRules;
import simplenlg.morphology.MorphologyProcessor;
import simplenlg.morphology.english.DeterminerAgrHelper;

/**
 * Classe contendo as manipulações morfologicas em Português.
 * 
 * @author Hélinton P. Steffens
 * @Jul 13, 2017
 */
public class PortugueseMorphologyProcessor extends MorphologyProcessor{
	
	private AbstractMorphologyRules morphologyRules;
	
	public PortugueseMorphologyProcessor() {
		this.morphologyRules = new PortugueseMorphologyRules();
	}
	
	public PortugueseMorphologyProcessor(AbstractMorphologyRules morphologyRules) {
		this.morphologyRules = morphologyRules;
	}

	@Override
	public AbstractMorphologyRules getMorphologyRules() {
		return morphologyRules;
	}

	@Override
	public List<NLGElement> realise(List<NLGElement> elements) {
		List<NLGElement> realisedElements = new ArrayList<NLGElement>();
		NLGElement currentElement = null;
		NLGElement determiner = null;
		NLGElement prevElement = null;

		if(elements != null) {
			for(NLGElement eachElement : elements) {
			
				if (prevElement != null && LexicalCategory.VERB.equals(prevElement.getCategory())) {
					if (LexicalCategory.VERB.equals(eachElement.getCategory()) && !eachElement.hasFeature(Feature.FORM)) {
						String baseForm = ((WordElement) prevElement.getFeatureAsElement(InternalFeature.BASE_WORD)).getDefaultSpellingVariant();
						if (morphologyRules.getAuxGerundio().contains(baseForm)) {
							eachElement.setFeature(Feature.FORM, Form.GERUND);
						}else if (!morphologyRules.getNotAuxParticiple().contains(baseForm)) {
							if (Tense.PAST.equals(eachElement.getFeature(Feature.TENSE))) {
								eachElement.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
							} else if (Tense.PRESENT.equals(eachElement.getFeature(Feature.TENSE))) {
								eachElement.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
							}
						}
						
					}
				}

				currentElement = realise(eachElement);

				if(currentElement != null) {
					//pass the discourse function and appositive features -- important for orth processor
					currentElement.setFeature(Feature.APPOSITIVE, eachElement.getFeature(Feature.APPOSITIVE));
					Object function = eachElement.getFeature(InternalFeature.DISCOURSE_FUNCTION);

					if(function != null) {
						currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, function);
					}

					if(prevElement != null && prevElement instanceof StringElement
					   && eachElement instanceof InflectedWordElement
					   && ((InflectedWordElement) eachElement).getCategory().equals(LexicalCategory.NOUN)) {

						String prevString = prevElement.getRealisation();

						//realisedElements.get(realisedElements.size() - 1)

						prevElement.setRealisation(DeterminerAgrHelper.checkEndsWithIndefiniteArticle(prevString,
						                                                                              currentElement.getRealisation()));

					}

					// realisedElements.add(realise(currentElement));
					realisedElements.add(currentElement);

					if(determiner == null && DiscourseFunction.SPECIFIER.equals(currentElement.getFeature(
							InternalFeature.DISCOURSE_FUNCTION))) {
						determiner = currentElement;
						determiner.setFeature(Feature.NUMBER, eachElement.getFeature(Feature.NUMBER));
						// MorphologyRules.doDeterminerMorphology(determiner,
						// currentElement.getRealisation());

					} else if(determiner != null) {

						if(currentElement instanceof ListElement) {
							// list elements: ensure det matches first element
							NLGElement firstChild = ((ListElement) currentElement).getChildren().get(0);

							if(firstChild != null) {
								//AG: need to check if child is a coordinate
								if(firstChild instanceof AbstractCoordinatedPhraseElement) {
									morphologyRules.doDeterminerMorphology(determiner,eachElement,
									                                       firstChild.getChildren().get(0).getRealisation());
								} else {
									morphologyRules.doDeterminerMorphology(determiner, eachElement, firstChild.getRealisation());
								}
							}

						} else {
							// everything else: ensure det matches realisation
							WordElement wordElement = (WordElement) eachElement.getFeatureAsElement(InternalFeature.BASE_WORD);
							if (wordElement == null) {
								continue;
							}
							morphologyRules.doDeterminerMorphology(determiner, eachElement, wordElement.getBaseForm());
						}

						determiner = null;
					}
				}
				prevElement = eachElement;
			}
		}

		return realisedElements;
	}
}
