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
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.StringElement;
import simplenlg.framework.portuguese.PortugueseCoordinatedPhraseElement;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.AdjPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class AdjectivePhraseTest {

	@Test
	public void testAdj() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		AdjPhraseSpec devasso = nlgFactory.createAdjectivePhrase("devasso");
		devasso.addPreModifier(nlgFactory.createAdverbPhrase("incrivelmente")); //$NON-NLS-1$
		Assert.assertEquals("incrivelmente devasso", realizer.realise(devasso).getRealisation());

		AdjPhraseSpec bonito = nlgFactory.createAdjectivePhrase("lindo");
		bonito.addPreModifier("maravilhosamente"); //$NON-NLS-1$
		Assert.assertEquals("maravilhosamente lindo", realizer.realise(bonito).getRealisation());

		// coordinate the two aps
		AbstractCoordinatedPhraseElement coordap = new PortugueseCoordinatedPhraseElement(devasso, bonito);
		Assert.assertEquals("incrivelmente devasso e maravilhosamente lindo", realizer.realise(coordap).getRealisation());

		// changing the inner conjunction
		coordap.setFeature(Feature.CONJUNCTION, "ou"); //$NON-NLS-1$
		Assert.assertEquals("incrivelmente devasso ou maravilhosamente lindo", realizer.realise(coordap).getRealisation());

		AdjPhraseSpec impressionante = nlgFactory.createAdjectivePhrase("impressionante");
		// coordinate this with a new AdjPhraseSpec
		AbstractCoordinatedPhraseElement coord2 = new PortugueseCoordinatedPhraseElement(coordap, impressionante);
		Assert.assertEquals(
				"incrivelmente devasso ou maravilhosamente lindo e impressionante", //$NON-NLS-1$
				realizer.realise(coord2).getRealisation());

		// add a premodifier the coordinate phrase, yielding
		// "seriously and undeniably incredibly salacious or amazingly beautiful
		// and stunning"
		AbstractCoordinatedPhraseElement preMod = new PortugueseCoordinatedPhraseElement(
				new StringElement("sério"), new StringElement("inegalvemente")); //$NON-NLS-1$//$NON-NLS-2$

		coord2.addPreModifier(preMod);
		Assert.assertEquals(
				"sério e inegalvemente incrivelmente devasso ou maravilhosamente lindo e impressionante", //$NON-NLS-1$
				realizer.realise(coord2).getRealisation());

		// adding a coordinate rather than coordinating should give a different
		// result
		coordap.addCoordinate(impressionante);
		Assert.assertEquals(
				"incrivelmente devasso, maravilhosamente lindo ou impressionante", //$NON-NLS-1$
				realizer.realise(coordap).getRealisation());

	}

	@Test
	public void testAdv() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);

		PhraseElement sent = nlgFactory.createClause("John", "comer"); //$NON-NLS-1$ //$NON-NLS-2$

		PhraseElement adv = nlgFactory.createAdjectivePhrase("rápido"); //$NON-NLS-1$
		sent.setComplement(adv);
		
		Assert.assertEquals("John come rápido", realizer.realise(sent).getRealisation());

		adv.addPreModifier("muito"); //$NON-NLS-1$

		Assert.assertEquals("John come muito rápido", realizer.realise( //$NON-NLS-1$
				sent).getRealisation());

	}
	
	@Test
	public void testMultipleModifiers() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		PhraseElement np = nlgFactory.createNounPhrase(lexicon.getWord("mensagem",
						LexicalCategory.NOUN));
		np.addPostModifier(lexicon.getWord("impermeável", LexicalCategory.ADJECTIVE));
		np.addPostModifier(lexicon.getWord("guerreiro", LexicalCategory.ADJECTIVE));
		np.addPostModifier(lexicon.getWord("rápido", LexicalCategory.ADJECTIVE));
		Assert.assertEquals("mensagem impermeável, guerreiro e rápido", realizer.realise(np).getRealisation());

		VPPhraseSpec verb = nlgFactory.createVerbPhrase("ser");
		AdjPhraseSpec adjetive = nlgFactory.createAdjectivePhrase("rápido");
		adjetive.addPreModifier(lexicon.getWord("muito", LexicalCategory.ADJECTIVE));
		adjetive.addPreModifier(lexicon.getWord("muito", LexicalCategory.ADJECTIVE));
		
		AbstractSPhraseSpec p = nlgFactory.createClause("eu", verb, adjetive);
		Assert.assertEquals("eu sou muito muito rápido", realizer.realise(p).getRealisation());
		
	}
	
	
	@Test
	public void testAdjectivePhrase001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		NPPhraseSpec warrior = nlgFactory.createNounPhrase("o", "guerreiro");
		
		VPPhraseSpec be = nlgFactory.createVerbPhrase("ser");
		
		AdjPhraseSpec stronger = nlgFactory.createAdjectivePhrase("forte");
		
		AbstractSPhraseSpec phrase = nlgFactory.createClause(warrior, be, stronger);
		Assert.assertEquals("o guerreiro é forte", realizer.realise(phrase).getRealisation());
	}
	
	@Test
	public void testAdjectivePhrase002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		NPPhraseSpec warrior = nlgFactory.createNounPhrase("o", "guerreira");
		
		VPPhraseSpec be = nlgFactory.createVerbPhrase("ser");
		
		AdjPhraseSpec stronger = nlgFactory.createAdjectivePhrase("forte");
		
		AbstractSPhraseSpec phrase = nlgFactory.createClause(warrior, be, stronger);
		Assert.assertEquals("a guerreira é forte", realizer.realise(phrase).getRealisation());
	}
	
	@Test
	public void testAdjectivePhrase003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		VPPhraseSpec be = nlgFactory.createVerbPhrase("ser");
		
		AdjPhraseSpec stronger = nlgFactory.createAdjectivePhrase("bonito");
		
		AbstractSPhraseSpec phrase = nlgFactory.createClause("ele", be, stronger);
		Assert.assertEquals("ele é bonito", realizer.realise(phrase).getRealisation());
	}
	
	@Test
	public void testAdjectivePhrase004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		VPPhraseSpec be = nlgFactory.createVerbPhrase("ser");
		
		AdjPhraseSpec stronger = nlgFactory.createAdjectivePhrase("bonito");
		
		AbstractSPhraseSpec phrase = nlgFactory.createClause("ela", be, stronger);
		Assert.assertEquals("ela é bonita", realizer.realise(phrase).getRealisation());
	}
	
	@Test
	public void testAdjectivePhrase005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		VPPhraseSpec be = nlgFactory.createVerbPhrase("ver");
		AdjPhraseSpec beautiful = nlgFactory.createAdjectivePhrase("lindo");
		NPPhraseSpec woman = nlgFactory.createNounPhrase("um", "mulher");
		woman.setPreModifier(beautiful);
		
		AbstractSPhraseSpec phrase = nlgFactory.createClause("eu", be, woman);
		Assert.assertEquals("eu vejo uma linda mulher", realizer.realise(phrase).getRealisation());
	}
	
	@Test
	public void testAdjectivePhrase006() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		VPPhraseSpec be = nlgFactory.createVerbPhrase("ver");
		AdjPhraseSpec beautiful = nlgFactory.createAdjectivePhrase("bonito");
		NPPhraseSpec woman = nlgFactory.createNounPhrase("um", "mulher");
		woman.setPostModifier(beautiful);
		
		AbstractSPhraseSpec phrase = nlgFactory.createClause("eu", be, woman);
		Assert.assertEquals("eu vejo uma mulher bonita", realizer.realise(phrase).getRealisation());
	}
	
	@Test
	public void testAdjectivePhrase007() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		VPPhraseSpec be = nlgFactory.createVerbPhrase("ver");
		AdjPhraseSpec smart = nlgFactory.createAdjectivePhrase("inteligente");
		NPPhraseSpec woman = nlgFactory.createNounPhrase("um", "mulher");
		woman.setPostModifier(smart);
		
		AbstractSPhraseSpec phrase = nlgFactory.createClause("eu", be, woman);
		Assert.assertEquals("eu vejo uma mulher inteligente", realizer.realise(phrase).getRealisation());
	}
	
	@Test
	public void testAdjectivePhrase008() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		VPPhraseSpec be = nlgFactory.createVerbPhrase("ser");
		
		AdjPhraseSpec stronger = nlgFactory.createAdjectivePhrase("bonito");
		
		AbstractSPhraseSpec phrase = nlgFactory.createClause("nós", be, stronger);
		Assert.assertEquals("nós somos bonitos", realizer.realise(phrase).getRealisation());
	}
	
	@Test
	public void testAdjectivePhrase009() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		VPPhraseSpec be = nlgFactory.createVerbPhrase("ser");
		
		AdjPhraseSpec stronger = nlgFactory.createAdjectivePhrase("bonito");
		
		AbstractSPhraseSpec phrase = nlgFactory.createClause("eles", be, stronger);
		Assert.assertEquals("eles são bonitos", realizer.realise(phrase).getRealisation());
	}
	
	@Test
	public void testAdjectivePhrase010() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		VPPhraseSpec be = nlgFactory.createVerbPhrase("ser");
		
		AdjPhraseSpec stronger = nlgFactory.createAdjectivePhrase("bonito");
		
		AbstractSPhraseSpec phrase = nlgFactory.createClause("elas", be, stronger);
		Assert.assertEquals("elas são bonitas", realizer.realise(phrase).getRealisation());
	}
	
}
