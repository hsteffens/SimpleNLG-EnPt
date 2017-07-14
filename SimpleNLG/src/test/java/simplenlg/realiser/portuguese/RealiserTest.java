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
package simplenlg.realiser.portuguese;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.Gender;
import simplenlg.features.LexicalFeature;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class RealiserTest {
	
	private Lexicon lexicon;
	private NLGFactory nlgFactory;
	private Realiser realiser = null;
	
	
	@Before
	public void setup() {
		lexicon = Lexicon.getPortugueseDefaultLexicon();
		nlgFactory = new PortugueseNLGFactory(lexicon);
		realiser = new Realiser(lexicon);
	}
	
	
	/**
	 * Test the realization of List of NLGElements that is null
	 */
	@Test
	public void emptyNLGElementRealiserTest() {
		ArrayList<NLGElement> elements = new ArrayList<NLGElement>();
		List<NLGElement> realisedElements = realiser.realise(elements);
		// Expect emtpy listed returned:
		Assert.assertNotNull(realisedElements);
		Assert.assertEquals(0, realisedElements.size());
		
	}
	
	
	/**
	 * Test the realization of List of NLGElements that is null
	 */
	@Test
	public void nullNLGElementRealiserTest() {
		ArrayList<NLGElement> elements = null;
		List<NLGElement> realisedElements = realiser.realise(elements);
		// Expect emtpy listed returned:
		Assert.assertNotNull(realisedElements);
		Assert.assertEquals(0, realisedElements.size());
		
	}
	
	/**
	 * Tests the realization of multiple NLGElements in a list.
	 */
	@Test
	public void multipleNLGElementListRealiserTest() {
		ArrayList<NLGElement> elements = new ArrayList<NLGElement>();
		// Create test NLGElements to realize:
		
		// "The cat jumping on the counter."
		DocumentElement sentence1 = nlgFactory.createSentence();
		NPPhraseSpec subject_1 = nlgFactory.createNounPhrase("o", "gato");
		VPPhraseSpec verb_1 = nlgFactory.createVerbPhrase("pular");
		verb_1.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
		NPPhraseSpec object_1 = nlgFactory.createNounPhrase();
		object_1.setDeterminer("o");
		object_1.setNoun("balcão");
		AbstractSPhraseSpec clause_1  = nlgFactory.createClause();
		clause_1.setSubject(subject_1);
		clause_1.setVerbPhrase(verb_1);
		clause_1.setObject(object_1);
		sentence1.addComponent(clause_1);
		
		// "The dog running on the counter."
		DocumentElement sentence2 = nlgFactory.createSentence();
		NPPhraseSpec subject_2 = nlgFactory.createNounPhrase("o", "cachorro");
		VPPhraseSpec verb_2 = nlgFactory.createVerbPhrase("correr");
		verb_2.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
		NPPhraseSpec object_2 = nlgFactory.createNounPhrase();
		object_2.setDeterminer("no");
		object_2.setNoun("balcão");
		AbstractSPhraseSpec clause_2  = nlgFactory.createClause();
		clause_2.setSubject(subject_2);
		clause_2.setVerbPhrase(verb_2);
		clause_2.setObject(object_2);
		sentence2.addComponent(clause_2);
		
		
		elements.add(sentence1);
		elements.add(sentence2);
		
		List<NLGElement> realisedElements = realiser.realise(elements);
		
		Assert.assertNotNull(realisedElements);
		Assert.assertEquals(2, realisedElements.size());
		Assert.assertEquals("O gato pulando o balcão.", realisedElements.get(0).getRealisation());
		Assert.assertEquals("O cachorro correndo no balcão.", realisedElements.get(1).getRealisation());
		
	}
	
	/**
	 * Tests the correct pluralization with possessives (GitHub issue #9)
	 */
	@Test
	public void correctPluralizationWithPossessives() {
                NPPhraseSpec sisterNP = nlgFactory.createNounPhrase("irmão");
                NLGElement word = nlgFactory.createInflectedWord("Albert Einstein",
                                                                 LexicalCategory.NOUN);
                word.setFeature(LexicalFeature.PROPER, true);
                NPPhraseSpec possNP = nlgFactory.createNounPhrase(word);
                possNP.setFeature(Feature.POSSESSIVE, true);
                sisterNP.setSpecifier(possNP);
                Assert.assertEquals("irmão do Albert Einstein", realiser.realise(sisterNP).getRealisation());
                sisterNP.setPlural(true);
                Assert.assertEquals("irmãos do Albert Einstein",
                                    realiser.realise(sisterNP).getRealisation());
                sisterNP.setPlural(false);
                possNP.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
                possNP.setFeature(Feature.PRONOMINAL, true);
                Assert.assertEquals("irmão dele",
                                    realiser.realise(sisterNP).getRealisation());
                sisterNP.setPlural(true);
                Assert.assertEquals("irmãos dele",
                                    realiser.realise(sisterNP).getRealisation());
	}

}
