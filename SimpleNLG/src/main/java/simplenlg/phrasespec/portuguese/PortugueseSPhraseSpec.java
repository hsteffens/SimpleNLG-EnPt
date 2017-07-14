package simplenlg.phrasespec.portuguese;

import simplenlg.framework.NLGFactory;
import simplenlg.phrasespec.AbstractSPhraseSpec;

/**
 * Esta classe define uma cláusula (sentenças semelhante a uma frase). 
 * 
 * @author Hélinton P. Steffens
 * @Jul 13, 2017
 */
public class PortugueseSPhraseSpec extends AbstractSPhraseSpec{

	public PortugueseSPhraseSpec(NLGFactory phraseFactory) {
		super(phraseFactory);
	}

	@Override
	public String getComplementiserWord() {
		return "que";
	}

}
