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

package simplenlg.syntax.portuguese;

import org.junit.Test;

import junit.framework.Assert;
import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class DeterminerTest {

	@Test
	public void testLowercaseConstant() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence = nlgFactory.createClause();

		NPPhraseSpec subject = nlgFactory.createNounPhrase("a", "cachorro");
		sentence.setSubject(subject);

		String output = realiser.realiseSentence(sentence);

		Assert.assertEquals("O cachorro.", output);
	}

	@Test
	public void testLowercaseVowel() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence = nlgFactory.createClause();

		NPPhraseSpec subject = nlgFactory.createNounPhrase("o", "lata");
		sentence.setSubject(subject);

		String output = realiser.realiseSentence(sentence);

		Assert.assertEquals("A lata.", output);
	}

	/**
	 * testUppercaseConstant - Test for when there is a upper case constant
	 */
	@Test
	public void testUppercaseConstant() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);

		AbstractSPhraseSpec sentence = nlgFactory.createClause();

		NPPhraseSpec subject = nlgFactory.createNounPhrase("a", "Gato");
		sentence.setSubject(subject);

		String output = realiser.realiseSentence(sentence);

		Assert.assertEquals("O Gato.", output);
	}

	@Test
	public void testUppercaseVowel() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence = nlgFactory.createClause();

		NPPhraseSpec subject = nlgFactory.createNounPhrase("o", "Emu");
		sentence.setSubject(subject);

		String output = realiser.realiseSentence(sentence);

		Assert.assertEquals("O Emu.", output);
	}

	/**
	 * testNumericA - Test for "a" specifier with a numeric subject 
	 */
	@Test
	public void testNumericA() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		AbstractSPhraseSpec sentence = nlgFactory.createClause();

		NPPhraseSpec subject = nlgFactory.createNounPhrase("a", "7");
		sentence.setSubject(subject);

		String output = realiser.realiseSentence(sentence);

		Assert.assertEquals("O 7.", output);
	}

	/**
	 * testNumericAn - Test for "an" specifier with a numeric subject 
	 */
	@Test
	public void testNumericAn() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence = nlgFactory.createClause();

		NPPhraseSpec subject = nlgFactory.createNounPhrase("o", "11");
		subject.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		sentence.setSubject(subject);

		String output = realiser.realiseSentence(sentence);

		Assert.assertEquals("Os 11.", output);
	}

	@Test
	public void testSingluarThisDeterminerNPObject() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence_1 = nlgFactory.createClause();

		NPPhraseSpec nounPhrase_1 = nlgFactory.createNounPhrase("este", "macaco");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Este macaco.", realiser.realiseSentence(sentence_1));
	}

	@Test
	public void testPluralThisDeterminerNPObject() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence_1 = nlgFactory.createClause();

		NPPhraseSpec nounPhrase_1 = nlgFactory.createNounPhrase("macaco");
		nounPhrase_1.setPlural(true);
		nounPhrase_1.setDeterminer("este");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Estes macacos.", realiser.realiseSentence(sentence_1));

	}

	@Test
	public void testSingluarThatDeterminerNPObject() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence_1 = nlgFactory.createClause();

		NPPhraseSpec nounPhrase_1 = nlgFactory.createNounPhrase("aquele", "macaco");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Aquele macaco.", realiser.realiseSentence(sentence_1));
	}

	@Test
	public void testPluralThatDeterminerNPObject() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence_1 = nlgFactory.createClause();

		NPPhraseSpec nounPhrase_1 = nlgFactory.createNounPhrase("macaco");
		nounPhrase_1.setPlural(true);
		nounPhrase_1.setDeterminer("Aquele");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Aqueles macacos.", realiser.realiseSentence(sentence_1));

	}

	@Test
	public void testSingularThoseDeterminerNPObject() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence_1 = nlgFactory.createClause();

		NPPhraseSpec nounPhrase_1 = nlgFactory.createNounPhrase("macaco");
		nounPhrase_1.setPlural(false);
		nounPhrase_1.setDeterminer("aqueles");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Aquele macaco.", realiser.realiseSentence(sentence_1));

	}

	@Test
	public void testSingularTheseDeterminerNPObject() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence_1 = nlgFactory.createClause();

		NPPhraseSpec nounPhrase_1 = nlgFactory.createNounPhrase("macaco");
		nounPhrase_1.setPlural(false);
		nounPhrase_1.setDeterminer("estes");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Este macaco.", realiser.realiseSentence(sentence_1));

	}

	@Test
	public void testPluralThoseDeterminerNPObject() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence_1 = nlgFactory.createClause();

		NPPhraseSpec nounPhrase_1 = nlgFactory.createNounPhrase("macaco");
		nounPhrase_1.setPlural(true);
		nounPhrase_1.setDeterminer("aqueles");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Aqueles macacos.", realiser.realiseSentence(sentence_1));

	}

	@Test
	public void testPluralTheseDeterminerNPObject() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence_1 = nlgFactory.createClause();
		
		NPPhraseSpec nounPhrase_1 = nlgFactory.createNounPhrase("macaco");
		nounPhrase_1.setPlural(true);
		nounPhrase_1.setDeterminer("estes");
		sentence_1.setObject(nounPhrase_1);

		Assert.assertEquals("Estes macacos.", realiser.realiseSentence(sentence_1));

	}
	
	@Test
	public void testDeterminer001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence = nlgFactory.createClause("eu", "ter");

		NPPhraseSpec object = nlgFactory.createNounPhrase("um", "sopa");
		sentence.setObject(object);

		String output = realiser.realiseSentence(sentence);

		Assert.assertEquals("Eu tenho uma sopa.", output);
	}

	@Test
	public void testDeterminer002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence = nlgFactory.createClause("eu", "ter");
		
		NPPhraseSpec object = nlgFactory.createNounPhrase("um", "sopa");
		object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		sentence.setObject(object);
		
		String output = realiser.realiseSentence(sentence);
		
		Assert.assertEquals("Eu tenho algumas sopas.", output);
	}
	
	@Test
	public void testDeterminer003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence = nlgFactory.createClause("eu", "comer");
		
		NPPhraseSpec object = nlgFactory.createNounPhrase("um", "feijão");
		object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		sentence.setObject(object);
		
		String output = realiser.realiseSentence(sentence);
		
		Assert.assertEquals("Eu como alguns feijões.", output);
	}

	@Test
	public void testDeterminer004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec sentence = nlgFactory.createClause("eu", "ver");
		
		NPPhraseSpec object = nlgFactory.createNounPhrase("este", "salada");
		object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		sentence.setObject(object);
		
		String output = realiser.realiseSentence(sentence);
		
		Assert.assertEquals("Eu vejo estas saladas.", output);
	}

}
