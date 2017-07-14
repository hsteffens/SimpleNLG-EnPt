package simplenlg.framework.portuguese;

import java.util.Arrays;
import java.util.List;

import simplenlg.framework.AbstractCoordinatedPhraseElement;

/**
 * Esta classe define a coordenação entre duas ou mais frases.
 * 
 * @author Hélinton P. Steffens
 * @Jul 13, 2017
 */
public class PortugueseCoordinatedPhraseElement extends AbstractCoordinatedPhraseElement{
	

	/** Coordinators which make the coordinate plural (eg, "and" but not "or")*/
	@SuppressWarnings("nls")
	private static final List<String> PLURAL_COORDINATORS = Arrays.asList("e");

	public PortugueseCoordinatedPhraseElement() {
		super();
	}

	public PortugueseCoordinatedPhraseElement(Object coordinate1, Object coordinate2){
		super(coordinate1, coordinate2);
	}
	
	
	@Override
	public List<String> getPluralCoordinators() {
		return PLURAL_COORDINATORS;
	}

	@Override
	public String getCoordinator() {
		return "e";
	}

}
