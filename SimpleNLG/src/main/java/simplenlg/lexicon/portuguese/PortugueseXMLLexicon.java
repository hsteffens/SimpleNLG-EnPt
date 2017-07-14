package simplenlg.lexicon.portuguese;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import simplenlg.features.LexicalFeature;
import simplenlg.framework.ElementCategory;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.language.EnLanguage;
import simplenlg.lexicon.AbstractXMLLexicon;

public class PortugueseXMLLexicon extends AbstractXMLLexicon {
	
	public PortugueseXMLLexicon(String lexicon, String path){
		super(lexicon, path);
		setLanguage(EnLanguage.PORTUGUESE);
	}
	
	public PortugueseXMLLexicon(String filename) {
		super(filename);
		setLanguage(EnLanguage.PORTUGUESE);
	}

	public PortugueseXMLLexicon(File file) {
		super(file);
		setLanguage(EnLanguage.PORTUGUESE);
	}

	
	public PortugueseXMLLexicon(URI lexiconURI) {
		super(lexiconURI);
		setLanguage(EnLanguage.PORTUGUESE);
	}

	/**
	 * add special cases to lexicon
	 * 
	 */
	@Override
	public void addSpecialCases() {
		// add variants of "be"
		WordElement be = getWord("ser", LexicalCategory.VERB);
		if (be != null) {
			updateIndex(be, "é", getIndexByVariant());
			updateIndex(be, "sou", getIndexByVariant());
			updateIndex(be, "é", getIndexByVariant());
			updateIndex(be, "fui", getIndexByVariant());
			updateIndex(be, "foi", getIndexByVariant());
		}
	}


	/**
	 * quick-and-dirty routine for getting morph variants should be replaced by
	 * something better!
	 * 
	 * @param word
	 * @return
	 */
	@Override
	public Set<String> getVariants(WordElement word) {
		Set<String> variants = new HashSet<String>();
		variants.add(word.getBaseForm());
		ElementCategory category = word.getCategory();
		if (category instanceof LexicalCategory) {
			switch ((LexicalCategory) category) {
			case NOUN:
				variants.add(getVariant(word, LexicalFeature.PLURAL, "s"));
				break;

			case ADJECTIVE:
				variants
						.add(getVariant(word, LexicalFeature.COMPARATIVE, "er"));
				variants
						.add(getVariant(word, LexicalFeature.SUPERLATIVE, "est"));
				break;

			case VERB:
				variants.add(getVariant(word, LexicalFeature.PRESENT3S, "s"));
				variants.add(getVariant(word, LexicalFeature.PAST, "ed"));
				variants.add(getVariant(word, LexicalFeature.PAST_PARTICIPLE,"ed"));
				variants.add(getVariant(word, LexicalFeature.PRESENT_PARTICIPLE, "ing"));
				break;

			default:
				// only base needed for other forms
				break;
			}
		}
		return variants;
	}


	/**
	 * quick-and-dirty routine for standard orthographic changes Should be
	 * replaced by something better!
	 * 
	 * @param base
	 * @param suffix
	 * @return
	 */
	@Override
	public String getForm(String base, String suffix) {
		// add a suffix to a base form, with orthographic changes

		// rule 1 - convert final "y" to "ie" if suffix does not start with "i"
		// eg, cry + s = cries , not crys
		if (base.endsWith("y") && !suffix.startsWith("i"))
			base = base.substring(0, base.length() - 1) + "ie";

		// rule 2 - drop final "e" if suffix starts with "e" or "i"
		// eg, like+ed = liked, not likeed
		if (base.endsWith("e")
				&& (suffix.startsWith("e") || suffix.startsWith("i")))
			base = base.substring(0, base.length() - 1);

		// rule 3 - insert "e" if suffix is "s" and base ends in s, x, z, ch, sh
		// eg, watch+s -> watches, not watchs
		if (suffix.startsWith("s")
				&& (base.endsWith("s") || base.endsWith("x")
						|| base.endsWith("z") || base.endsWith("ch") || base
						.endsWith("sh")))
			base = base + "e";

		// have made changes, now append and return
		return base + suffix; // eg, want + s = wants
	}
	
}

