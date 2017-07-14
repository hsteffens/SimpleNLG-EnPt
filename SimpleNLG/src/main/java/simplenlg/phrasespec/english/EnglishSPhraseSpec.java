package simplenlg.phrasespec.english;

import simplenlg.framework.NLGFactory;
import simplenlg.phrasespec.AbstractSPhraseSpec;

public class EnglishSPhraseSpec extends AbstractSPhraseSpec{

	public EnglishSPhraseSpec(NLGFactory phraseFactory) {
		super(phraseFactory);
	}

	@Override
	public String getComplementiserWord() {
		return "that";
	}

}
