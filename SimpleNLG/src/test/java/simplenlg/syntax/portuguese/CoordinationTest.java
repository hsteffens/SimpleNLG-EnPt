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
import simplenlg.features.Tense;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class CoordinationTest {
	
	/**
	 * Check that empty coordinate phrases are not realised as "null"
	 */
	@Test
	public void emptyCoordinationTest() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		// first a simple phrase with no coordinates
		AbstractCoordinatedPhraseElement coord = nlgFactory.createCoordinatedPhrase();
		Assert.assertEquals("", realiser.realise(coord).getRealisation());

		// now one with a premodifier and nothing else
		coord.addPreModifier(nlgFactory.createAdjectivePhrase("legal"));
		Assert.assertEquals("legal", realiser.realise(coord).getRealisation());
		
	}

	/**
	 * Test pre and post-modification of coordinate VPs inside a sentence.
	 */
	@Test
	public void testModifiedCoordVP() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		VPPhraseSpec getUp = nlgFactory.createVerbPhrase("levantar");
		VPPhraseSpec fallDown = nlgFactory.createVerbPhrase("cair");
		
		AbstractCoordinatedPhraseElement coord = nlgFactory.createCoordinatedPhrase(getUp, fallDown);
		coord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("levantou e caiu", realiser.realise(coord).getRealisation());

		// add a premodifier
		coord.addPreModifier("lentamente");
		Assert.assertEquals("lentamente levantou e caiu", realiser.realise(coord).getRealisation());

		// adda postmodifier
		PPPhraseSpec behindTheCurtain = nlgFactory.createPrepositionPhrase("atrás"); //$NON-NLS-1$
		NPPhraseSpec np5 = nlgFactory.createNounPhrase("da", "cortina"); //$NON-NLS-1$ //$NON-NLS-2$
		behindTheCurtain.addComplement(np5);
		
		coord.addPostModifier(behindTheCurtain);
		Assert.assertEquals("lentamente levantou e caiu atrás da cortina",
				realiser.realise(coord).getRealisation());

		// put within the context of a sentence
		AbstractSPhraseSpec s = nlgFactory.createClause("Jake", coord);
		s.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals(
				"Jake lentamente levantou e caiu atrás da cortina",
				realiser.realise(s).getRealisation());

		// add premod to the sentence
		s.addPreModifier(lexicon.getWord("contudo", LexicalCategory.ADVERB));
		Assert.assertEquals(
				"Jake contudo lentamente levantou e caiu atrás da cortina",
				realiser.realise(s).getRealisation());

		PPPhraseSpec inTheRoom = nlgFactory.createPrepositionPhrase("do"); //$NON-NLS-1$
		NPPhraseSpec np6 = nlgFactory.createNounPhrase("quarto"); //$NON-NLS-1$ //$NON-NLS-2$
		inTheRoom.addComplement(np6);
		
		// add postmod to the sentence
		s.addPostModifier(inTheRoom);
		Assert.assertEquals(
				"Jake contudo lentamente levantou e caiu atrás da cortina do quarto",
				realiser.realise(s).getRealisation());
	}

	@Test
	public void testCoordinateVPComplexSubject() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		// "As a result of the procedure the patient had an adverse contrast media reaction and went into cardiogenic shock."
		AbstractSPhraseSpec s = nlgFactory.createClause();

		s.setSubject(nlgFactory.createNounPhrase("o", "paciente"));

		// first VP
		VPPhraseSpec vp1 = nlgFactory.createVerbPhrase(lexicon.getWord("ter", LexicalCategory.VERB));
		NPPhraseSpec np1 = nlgFactory.createNounPhrase("uma", "reação");
		NPPhraseSpec np2 = nlgFactory.createNounPhrase("de", lexicon.getWord("mídia", LexicalCategory.NOUN));
		NPPhraseSpec np3 = nlgFactory.createNounPhrase("de", "contraste");
		
		np1.addComplement(np2);
		np2.addComplement(np3);
		
		np1.addPostModifier(lexicon.getWord("adversa",
				LexicalCategory.ADJECTIVE));
		vp1.addComplement(np1);

		// second VP
		VPPhraseSpec vp2 = nlgFactory.createVerbPhrase(lexicon.getWord("entrar", LexicalCategory.VERB));
		PPPhraseSpec pp = nlgFactory.createPrepositionPhrase("em", lexicon.getWord("choque cardiogênico", LexicalCategory.NOUN));
		vp2.addComplement(pp);

		// coordinate
		AbstractCoordinatedPhraseElement coord = nlgFactory.createCoordinatedPhrase(vp1, vp2);
		coord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals(
				"teve uma reação de mídia de contraste adversa e entrou em choque cardiogênico",
				realiser.realise(coord).getRealisation());

		// now put this in the sentence
		s.setVerbPhrase(coord);
		s.addFrontModifier("Como resultado de tratamento");
		Assert.assertEquals(
				"Como resultado de tratamento o paciente teve uma reação de mídia de contraste adversa e entrou em choque cardiogênico",
				realiser.realise(s).getRealisation());

	}

	/**
	 * Test setting a conjunction to null
	 */
	public void testNullConjunction() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause("eu", "estar", "feliz");
		AbstractSPhraseSpec q = nlgFactory.createClause("eu", "comer", "peixe");
		AbstractCoordinatedPhraseElement pq = nlgFactory.createCoordinatedPhrase();
		pq.addCoordinate(p);
		pq.addCoordinate(q);
		pq.setFeature(Feature.CONJUNCTION, "");

		// should come out without conjunction
		Assert.assertEquals("eu estou feliz eu como peixe", realiser.realise(pq)
				.getRealisation());

		// should come out without conjunction
		pq.setFeature(Feature.CONJUNCTION, null);
		Assert.assertEquals("eu estou feliz eu como peixe", realiser.realise(pq).getRealisation());

	}

	/**
	 * Check that the negation feature on a child of a coordinate phrase remains
	 * as set, unless explicitly set otherwise at the parent level.
	 */
	@Test
	public void testNegationFeature() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec s1 = nlgFactory.createClause("ele", "ter", "asma");
		AbstractSPhraseSpec s2 = nlgFactory.createClause("ele", "ter", "diabetes");
		s1.setFeature(Feature.NEGATED, true);
		AbstractCoordinatedPhraseElement coord = nlgFactory.createCoordinatedPhrase(s1, s2);
		String realisation = realiser.realise(coord).getRealisation();
		System.out.println(realisation);
		Assert.assertEquals("ele não tem asma e ele tem diabete", realisation);
	}
}
