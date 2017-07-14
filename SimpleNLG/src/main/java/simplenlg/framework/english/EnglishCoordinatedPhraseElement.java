package simplenlg.framework.english;

import java.util.Arrays;
import java.util.List;

import simplenlg.framework.AbstractCoordinatedPhraseElement;

public class EnglishCoordinatedPhraseElement extends AbstractCoordinatedPhraseElement{

	/** Coordinators which make the coordinate plural (eg, "and" but not "or")*/
	@SuppressWarnings("nls")
	private static final List<String> PLURAL_COORDINATORS = Arrays.asList("and");
	
	public EnglishCoordinatedPhraseElement() {
		super();
	}
	
	public EnglishCoordinatedPhraseElement(Object coordinate1, Object coordinate2){
		super(coordinate1, coordinate2);
	}

	@Override
	public List<String> getPluralCoordinators() {
		return PLURAL_COORDINATORS;
	}

	@Override
	public String getCoordinator() {
		return "and";
	}
}
