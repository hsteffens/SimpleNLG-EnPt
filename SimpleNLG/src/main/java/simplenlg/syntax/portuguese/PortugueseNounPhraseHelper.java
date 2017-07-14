package simplenlg.syntax.portuguese;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Gender;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Person;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.WordElement;
import simplenlg.syntax.AbstractNounPhraseHelper;
import simplenlg.syntax.SyntaxProcessor;

/**
 * Esta classe contem os processos que manipulam a sintaxe dos substantivos em português.
 * 
 * @author Hélinton P. Steffens
 * @Jul 13, 2017
 */
public class PortugueseNounPhraseHelper extends AbstractNounPhraseHelper{

	@Override
	public NLGElement createPronoun(SyntaxProcessor parent, PhraseElement phrase) {
		String pronoun = "isto"; //$NON-NLS-1$
		NLGFactory phraseFactory = phrase.getFactory();
		Object personValue = phrase.getFeature(Feature.PERSON);

		if (Person.FIRST.equals(personValue)) {
			pronoun = "eu"; //$NON-NLS-1$
		} else if (Person.SECOND.equals(personValue)) {
			pronoun = "tu"; //$NON-NLS-1$
		} else {
			Object genderValue = phrase.getFeature(LexicalFeature.GENDER);
			if (Gender.FEMININE.equals(genderValue)) {
				pronoun = "ela"; //$NON-NLS-1$
			} else if (Gender.MASCULINE.equals(genderValue)) {
				pronoun = "ele"; //$NON-NLS-1$
			} else if (Gender.NEUTER.equals(genderValue)) {
				pronoun = "você"; //$NON-NLS-1$
			}
		}
		// AG: createWord now returns WordElement; so we embed it in an
		// inflected word element here
		NLGElement element;
		NLGElement proElement = phraseFactory.createWord(pronoun,
				LexicalCategory.PRONOUN);
		
		if (proElement instanceof WordElement) {
			element = new InflectedWordElement((WordElement) proElement);
			element.setFeature(LexicalFeature.GENDER, ((WordElement) proElement).getFeature(LexicalFeature.GENDER));	
			// Ehud - also copy over person
			element.setFeature(Feature.PERSON, ((WordElement) proElement).getFeature(Feature.PERSON));	
		} else {
			element = proElement;
		}
		
		element.setFeature(InternalFeature.DISCOURSE_FUNCTION,
				DiscourseFunction.SPECIFIER);
		element.setFeature(Feature.POSSESSIVE, phrase
				.getFeature(Feature.POSSESSIVE));
		element
				.setFeature(Feature.NUMBER, phrase.getFeature(Feature.NUMBER));

		
		if (phrase.hasFeature(InternalFeature.DISCOURSE_FUNCTION)) {
			element.setFeature(InternalFeature.DISCOURSE_FUNCTION, phrase
					.getFeature(InternalFeature.DISCOURSE_FUNCTION));
		}		

		return element;
	}
	
	@Override
	public void sortElements(ListElement realisedElement) {
		if (realisedElement != null) {
			if (realisedElement.getChildren() != null && !realisedElement.getChildren().isEmpty()) {
				List<NLGElement> components = new ArrayList<NLGElement>(realisedElement.getChildren());
				Collections.sort(components, new Comparator<NLGElement>() {

					public int compare(NLGElement o1, NLGElement o2) {
						if (DiscourseFunction.SPECIFIER.equals(o1.getFeature(InternalFeature.DISCOURSE_FUNCTION)) &&
								o1.getFeatureAsBoolean(Feature.POSSESSIVE).booleanValue()) {
							return -1;
						} else if (DiscourseFunction.SPECIFIER.equals(o2.getFeature(InternalFeature.DISCOURSE_FUNCTION)) &&
								o2.getFeatureAsBoolean(Feature.POSSESSIVE).booleanValue()) {
							return -1;
						}
						return 0;
					}
					
				});
				realisedElement.setComponents(null);
				realisedElement.setComponents(components);
			}
		}
	}

}
