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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */
package simplenlg.lexicon.english;

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

/**
 * This class loads words from an XML lexicon. All features specified in the
 * lexicon are loaded
 * 
 * @author ereiter
 * 
 */
public class EnglishXMLLexicon extends AbstractXMLLexicon {
	
	public EnglishXMLLexicon(String lexicon, String path){
		super(lexicon, path);
		setLanguage(EnLanguage.ENGLISH);
	}
	
	public EnglishXMLLexicon(String filename) {
		super(filename);
		setLanguage(EnLanguage.ENGLISH);
	}

	public EnglishXMLLexicon(File file) {
		super(file);
		setLanguage(EnLanguage.ENGLISH);
	}

	
	public EnglishXMLLexicon(URI lexiconURI) {
		super(lexiconURI);
		setLanguage(EnLanguage.ENGLISH);
	}
	
	
	/**
	 * add special cases to lexicon
	 * 
	 */
	@Override
	public void addSpecialCases() {
		// add variants of "be"
		WordElement be = getWord("be", LexicalCategory.VERB);
		if (be != null) {
			updateIndex(be, "is", getIndexByVariant());
			updateIndex(be, "am", getIndexByVariant());
			updateIndex(be, "are", getIndexByVariant());
			updateIndex(be, "was", getIndexByVariant());
			updateIndex(be, "were", getIndexByVariant());
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
