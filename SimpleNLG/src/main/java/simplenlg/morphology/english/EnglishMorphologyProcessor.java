package simplenlg.morphology.english;

import java.util.ArrayList;
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.StringElement;
import simplenlg.morphology.AbstractMorphologyRules;
import simplenlg.morphology.MorphologyProcessor;

public class EnglishMorphologyProcessor extends MorphologyProcessor{
	
	private AbstractMorphologyRules morphologyRules;
	
	public EnglishMorphologyProcessor() {
		this.morphologyRules = new EnglishMorphologyRules();
	}
	
	public EnglishMorphologyProcessor(AbstractMorphologyRules morphologyRules) {
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
									morphologyRules.doDeterminerMorphology(determiner,firstChild,
									                                       firstChild.getChildren().get(0).getRealisation());
								} else {
									morphologyRules.doDeterminerMorphology(determiner, firstChild, firstChild.getRealisation());
								}
							}

						} else {
							// everything else: ensure det matches realisation
							morphologyRules.doDeterminerMorphology(determiner, currentElement, currentElement.getRealisation());
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
