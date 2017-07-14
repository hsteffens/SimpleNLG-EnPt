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

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.Assert;
import simplenlg.features.ClauseStatus;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.InternalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.portuguese.PortugueseCoordinatedPhraseElement;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.AdjPhraseSpec;
import simplenlg.phrasespec.AdvPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class ClauseTest{

	/**
	 * Initial test for basic sentences.
	 */
	@Test
	public void testBasic001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("a", "mulher");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("beijar")); //$NON-NLS-1$
		clause.setObject(nlgFactory.createNounPhrase("o", "homem"));
		
		AbstractSPhraseSpec clause2 = nlgFactory.createClause();
		clause2.setSubject(nlgFactory.createNounPhrase("lá")); //$NON-NLS-1$
		clause2.setVerbPhrase(nlgFactory.createVerbPhrase("estar")); //$NON-NLS-1$
		clause2.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause2.addPostModifier(nlgFactory.createPrepositionPhrase("na", nlgFactory.createNounPhrase("pedra")));
		
		Assert.assertEquals("a mulher beija o homem", realiser.realise(clause).getRealisation());
		Assert.assertEquals("lá está o cachorro na pedra", realiser.realise(clause2).getRealisation());
	}

	/**
	 * Initial test for basic sentences.
	 */
	@Test
	public void testBasic002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		
		Assert.assertEquals("o homem dá para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
	}
	
	/**
	 * Initial test for basic sentences.
	 */
	@Test
	public void testBasic003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		clause.setFeature(Feature.CUE_PHRASE, "embora"); //$NON-NLS-1$
		clause.addFrontModifier("amanhã"); //$NON-NLS-1$

		AbstractCoordinatedPhraseElement subject = nlgFactory.createCoordinatedPhrase(
						nlgFactory.createNounPhrase("Jane"), 
						nlgFactory.createNounPhrase("Andrew"));

		clause.setSubject(subject);

		PhraseElement pick = nlgFactory.createVerbPhrase("pegar"); //$NON-NLS-1$
		clause.setVerbPhrase(pick);
		clause.setObject("as bolas"); //$NON-NLS-1$
		clause.addPostModifier("na loja"); //$NON-NLS-1$
		clause.setFeature(Feature.TENSE, Tense.FUTURE);

		Assert.assertEquals("embora amanhã Jane e Andrew pegarão as bolas na loja", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
	}


	/**
	 * Test did not
	 */
	@Test
	public void testDidNot() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement s = nlgFactory.createClause("John", "comer");
		s.setFeature(Feature.TENSE, Tense.PAST);
		s.setFeature(Feature.NEGATED, true);

		Assert.assertEquals("John não comeu", realiser.realise(s).getRealisation());

	}

	/**
	 * Test did not
	 */
	@Test
	public void testVPNegation() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		// negate the VP
		PhraseElement vp = nlgFactory.createVerbPhrase("mentir");
		vp.setFeature(Feature.TENSE, Tense.PAST);
		vp.setFeature(Feature.NEGATED, true);
		PhraseElement compl = nlgFactory.createVerbPhrase("anestesiar");
		compl.setFeature(Feature.TENSE, Tense.PAST);
		vp.setComplement(compl);

		AbstractSPhraseSpec s = nlgFactory.createClause(nlgFactory.createNounPhrase("o", "paciente"), vp);

		Assert.assertEquals("o paciente não mentiu anestesiado", realiser.realise(s).getRealisation());

	}

	/**
	 * Tests for setting tense, aspect and passive from the sentence interface.
	 */
	@Test
	public void testPastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		
		clause.setFeature(Feature.TENSE, Tense.PAST);
		
		Assert.assertEquals("o homem deu para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
	}
	
	@Test
	public void testPerfectPastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		
		clause.setFeature(Feature.TENSE, Tense.PAST);
		clause.setFeature(Feature.PERFECT, true);
		
		Assert.assertEquals("o homem deu para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
	}
	
	@Test
	public void testNotPerfectPastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		
		clause.setFeature(Feature.TENSE, Tense.PAST);
		clause.setFeature(Feature.PERFECT, true);
		clause.setFeature(Feature.NEGATED, true);
		
		Assert.assertEquals("o homem não deu para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
	}

	@Test
	public void testProgressivePastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$
		
		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		
		clause.setFeature(Feature.TENSE, Tense.PAST);
		clause.setFeature(Feature.PERFECT, true);
		clause.setFeature(Feature.NEGATED, true);
		clause.setFeature(Feature.PROGRESSIVE, true);
		
		Assert.assertEquals("o homem não esteve dando para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
	}
	
	@Test
	public void testPassivePastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$
		
		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		
		clause.setFeature(Feature.TENSE, Tense.PAST);
		clause.setFeature(Feature.PERFECT, true);
		clause.setFeature(Feature.NEGATED, true);
		clause.setFeature(Feature.PROGRESSIVE, true);
		clause.setFeature(Feature.PASSIVE, true);
		
		Assert.assertEquals("a flor do John não foi dado para a mulher pelo homem", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
	}

	/**
	 * Test what happens when a sentence is subordinated as complement of a
	 * verb.
	 */
	@Test
	public void testSubordination() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		
		// subordinate sentence by setting it as complement of a verb
		VPPhraseSpec say = nlgFactory.createVerbPhrase("dizer");
		say.addComplement(clause);

		// check the getter
		Assert.assertEquals(ClauseStatus.SUBORDINATE, clause.getFeature(InternalFeature.CLAUSE_STATUS));

		// check realisation
		Assert.assertEquals("diz que o homem dá para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(say).getRealisation());
	}
	
	/**
	 * Test the various forms of a sentence, including subordinates.
	 */
	@Test
	public void testForm001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		clause.setSubject(nlgFactory.createNounPhrase("a", "mulher")); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("beijar")); //$NON-NLS-1$
		clause.setObject(nlgFactory.createNounPhrase("o", "homem"));
		
		// check the getter method
		Assert.assertEquals(Form.NORMAL, clause.getFeatureAsElement(
				InternalFeature.VERB_PHRASE).getFeature(Feature.FORM));

		// infinitive
		clause.setFeature(Feature.FORM, Form.INFINITIVE);
		Assert
				.assertEquals(
						"beija o homem", realiser.realise(clause).getRealisation()); //$NON-NLS-1$
	}

	@Test
	public void testForm002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		clause.setSubject(nlgFactory.createNounPhrase("lá")); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("estar")); //$NON-NLS-1$
		clause.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause.addPostModifier(nlgFactory.createPrepositionPhrase("na", nlgFactory.createNounPhrase("pedra")));
		
		// gerund with "there"
		clause.setFeature(Feature.FORM, Form.GERUND);
		Assert.assertEquals("lá estando o cachorro na pedra", realiser //$NON-NLS-1$
				.realise(clause).getRealisation());
		
	}

	@Test
	public void testForm003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		clause.setSubject(nlgFactory.createNounPhrase("lá")); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("estar")); //$NON-NLS-1$
		clause.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause.addPostModifier(nlgFactory.createPrepositionPhrase("na", nlgFactory.createNounPhrase("pedra")));
		
		// gerund with "there"
		clause.setFeature(Feature.FORM, Form.GERUND);
		Assert.assertEquals("lá estando o cachorro na pedra", realiser //$NON-NLS-1$
				.realise(clause).getRealisation());
		
	}

	@Ignore("Como não é tratado imperativo não conjunga o verbo, organização da frase a principio já está correta.Como não é tratado imperativo não conjunga o verbo, organização da frase a principio já está correta.")
	@Test
	public void testForm004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		
		// gerund with "there"
		clause.setFeature(Feature.FORM, Form.IMPERATIVE);

		Assert.assertEquals("dê para a mulher a flor do John", realiser //$NON-NLS-1$
				.realise(clause).getRealisation());
		
	}
	
	@Test
	public void testForm005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();

		AbstractCoordinatedPhraseElement subject = nlgFactory.createCoordinatedPhrase(
						nlgFactory.createNounPhrase("Jane"), 
						nlgFactory.createNounPhrase("Andrew"));

		clause.setSubject(subject);

		PhraseElement pick = nlgFactory.createVerbPhrase("pegar"); //$NON-NLS-1$
		clause.setVerbPhrase(pick);
		clause.setObject("as bolas"); //$NON-NLS-1$
		clause.addPostModifier("na loja"); //$NON-NLS-1$
		clause.setFeature(Feature.TENSE, Tense.FUTURE);
		
		// compose this with a new sentence
		// ER - switched direct and indirect object in sentence
		AbstractSPhraseSpec temp2 = nlgFactory.createClause("eu", "contar", clause); //$NON-NLS-1$ //$NON-NLS-2$
		temp2.setFeature(Feature.TENSE, Tense.FUTURE);

		PhraseElement indirectObject = nlgFactory.createNounPhrase("para", "John"); //$NON-NLS-1$

		temp2.setIndirectObject(indirectObject);

		Assert.assertEquals("eu contarei para John que Jane e Andrew pegarão as bolas na loja", realiser.realise(temp2).getRealisation());
	}
	
	@Test
	public void testForm006() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		
		// the man's giving the woman John's flower upset Peter
		AbstractSPhraseSpec _s4 = nlgFactory.createClause();
		_s4.setVerbPhrase(nlgFactory.createVerbPhrase("chatear")); //$NON-NLS-1$
		_s4.setFeature(Feature.TENSE, Tense.PAST);
		_s4.setObject(nlgFactory.createNounPhrase("Peter")); //$NON-NLS-1$
		clause.setFeature(Feature.PERFECT, true);
		
		_s4.setSubject(clause);

		// suppress the genitive realisation of the NP subject in gerund
		// sentences
		clause.setFeature(Feature.SUPPRESS_GENITIVE_IN_GERUND, true);

		// check the realisation: subject should not be genitive
		Assert.assertEquals(
				"o homem dando para a mulher a flor do John chateou Peter", //$NON-NLS-1$
				realiser.realise(_s4).getRealisation());
		
	}
	
	@Test
	public void testForm007() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		
		AbstractSPhraseSpec s5 = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase2 = nlgFactory.createNounPhrase("a", "mulher");
		s5.setSubject(createNounPhrase2); //$NON-NLS-1$ //$NON-NLS-2$
		s5.setVerbPhrase(nlgFactory.createVerbPhrase("acaraciar")); //$NON-NLS-1$
		s5.setObject(nlgFactory.createNounPhrase("o", "gato")); //$NON-NLS-1$ //$NON-NLS-2$
		s5.setFeature(Feature.SUPPRESS_GENITIVE_IN_GERUND, true);

		AbstractCoordinatedPhraseElement coord = new PortugueseCoordinatedPhraseElement(clause, s5);
		AbstractSPhraseSpec complexS = nlgFactory.createClause();
		complexS.setVerbPhrase(nlgFactory.createVerbPhrase("chatear")); //$NON-NLS-1$
		complexS.setFeature(Feature.TENSE, Tense.PAST);
		complexS.setObject(nlgFactory.createNounPhrase("Peter")); //$NON-NLS-1$
		complexS.setSubject(coord);
		clause.setFeature(Feature.PERFECT, true);
		clause.setFeature(Feature.SUPPRESS_GENITIVE_IN_GERUND, true);

		Assert.assertEquals("o homem dando para a mulher a flor do John " //$NON-NLS-1$
				+ "e a mulher acaraciando o gato chatearam Peter", //$NON-NLS-1$
				realiser.realise(complexS).getRealisation());

	}
	
	@Test
	public void testCoordination001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec s6 = nlgFactory.createClause();
		s6.setVerbPhrase(nlgFactory.createVerbPhrase("contar")); //$NON-NLS-1$
		s6.setFeature(Feature.TENSE, Tense.PAST);
		s6.setSubject(nlgFactory.createNounPhrase("o", "menino")); //$NON-NLS-1$ //$NON-NLS-2$
		// ER - switched indirect and direct object
		PhraseElement indirect = nlgFactory.createNounPhrase("a toda", //$NON-NLS-1$
				"garota"); //$NON-NLS-1$
		s6.setIndirectObject(indirect);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$
		clause.setFeature(Feature.TENSE, Tense.PAST);

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));

		AbstractSPhraseSpec s5 = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase2 = nlgFactory.createNounPhrase("a", "mulher");
		s5.setSubject(createNounPhrase2); //$NON-NLS-1$ //$NON-NLS-2$
		s5.setVerbPhrase(nlgFactory.createVerbPhrase("acaraciar")); //$NON-NLS-1$
		s5.setObject(nlgFactory.createNounPhrase("o", "gato")); //$NON-NLS-1$ //$NON-NLS-2$
		s5.setFeature(Feature.SUPPRESS_GENITIVE_IN_GERUND, true);
		s5.setFeature(Feature.TENSE, Tense.PAST);

		AbstractSPhraseSpec complexS = nlgFactory.createClause();
		complexS.setVerbPhrase(nlgFactory.createVerbPhrase("chatear")); //$NON-NLS-1$
		complexS.setFeature(Feature.TENSE, Tense.PAST);
		complexS.setObject(nlgFactory.createNounPhrase("Peter")); //$NON-NLS-1$
		
		AbstractCoordinatedPhraseElement coord = new PortugueseCoordinatedPhraseElement(clause, s5);
		coord.addComplement(complexS);
		s6.setObject(coord);
		
		Assert.assertEquals(
				"o menino contou a toda garota que o homem deu para a mulher a flor do John " //$NON-NLS-1$
						+ "e que a mulher acaraciou o gato " //$NON-NLS-1$
						+ "chateou Peter", //$NON-NLS-1$
						realiser.realise(s6).getRealisation());
		
	}

	@Test
	public void testCoordination002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		clause.setSubject(nlgFactory.createNounPhrase("a", "mulher")); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("beijar")); //$NON-NLS-1$
		clause.setObject(nlgFactory.createNounPhrase("o", "homem"));
		
		AbstractSPhraseSpec clause2 = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause2.setSubject(createNounPhrase); //$NON-NLS-1$
		clause2.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$
		clause2.setFeature(Feature.TENSE, Tense.PAST);

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause2.setObject(preposition);
		clause2.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));

		AbstractCoordinatedPhraseElement coord = new PortugueseCoordinatedPhraseElement(clause, clause2);
		coord.setFeature(Feature.TENSE, Tense.PAST);
			
		Assert
				.assertEquals(
						"a mulher beijou o homem e o homem deu para a mulher a flor do John", //$NON-NLS-1$
						realiser.realise(coord).getRealisation());
		
		
	}


	/**
	 * Tests recogition of strings in API.
	 */
	@Test
	public void testStringRecognition() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);

		// test recognition of forms of "be"
		PhraseElement _s1 = nlgFactory.createClause(
				"meu gato", "estar", "triste"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Assert.assertEquals(
				"meu gato está triste", realiser.realise(_s1).getRealisation()); //$NON-NLS-1$

		// test recognition of pronoun for afreement
		PhraseElement _s2 = nlgFactory
				.createClause("eu", "querer", "Mary"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		Assert.assertEquals(
				"eu quero Mary", realiser.realise(_s2).getRealisation()); //$NON-NLS-1$

		// test recognition of pronoun for correct form
		PhraseElement subject = nlgFactory.createNounPhrase("cachorro"); //$NON-NLS-1$
		subject.setFeature(InternalFeature.SPECIFIER, "o"); //$NON-NLS-1$
		subject.addPostModifier("da casa ao lado"); //$NON-NLS-1$
		PhraseElement object = nlgFactory.createNounPhrase("eu"); //$NON-NLS-1$
		PhraseElement s = nlgFactory.createClause(subject,
				"perseguir", object); //$NON-NLS-1$
		s.setFeature(Feature.PROGRESSIVE, true);
		Assert.assertEquals("o cachorro da casa ao lado está perseguindo me", //$NON-NLS-1$
				realiser.realise(s).getRealisation());
	}

	/**
	 * Tests complex agreement.
	 */
	@Test
	public void testAgreement() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);

		// basic agreement
//		NPPhraseSpec np = nlgFactory.createNounPhrase("cachorro"); //$NON-NLS-1$
//		np.setSpecifier("o"); //$NON-NLS-1$
//		np.addPostModifier("bravo"); //$NON-NLS-1$
//		PhraseElement _s1 = nlgFactory
//				.createClause(np, "perseguir", "John"); //$NON-NLS-1$ //$NON-NLS-2$
//		Assert.assertEquals("o cachorro bravo persegue John", realiser //$NON-NLS-1$
//				.realise(_s1).getRealisation());

		// plural
		NPPhraseSpec np = nlgFactory.createNounPhrase("cachorro"); //$NON-NLS-1$
		np.setSpecifier("o"); //$NON-NLS-1$
		AdjPhraseSpec bravo = nlgFactory.createAdjectivePhrase("bravo");
		np.setPostModifier(bravo);
//		bravo.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
//		np.setComplement(bravo);
		
		np.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		PhraseElement _s1 = nlgFactory.createClause(np, "perseguir", "John"); //$NON-NLS-1$ //$NON-NLS-2$
		Assert.assertEquals("os cachorros bravos perseguem John", realiser //$NON-NLS-1$
				.realise(_s1).getRealisation());

		// test agreement with "there is"
		np = nlgFactory.createNounPhrase("cachorro"); //$NON-NLS-1$
		np.addPostModifier("bravo"); //$NON-NLS-1$
		np.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		np.setSpecifier("um"); //$NON-NLS-1$
		
		VPPhraseSpec verb = nlgFactory.createVerbPhrase("haver");
		PhraseElement _s2 = nlgFactory.createClause(verb, np); //$NON-NLS-1$ //$NON-NLS-2$
		Assert.assertEquals("há um cachorro bravo", realiser //$NON-NLS-1$
				.realise(_s2).getRealisation());

		// plural with "there"
		np = nlgFactory.createNounPhrase("cachorro"); //$NON-NLS-1$
		np.setSpecifier("algum");
		
		bravo = nlgFactory.createAdjectivePhrase("bravo");
		np.setComplement(bravo);
		
		verb = nlgFactory.createVerbPhrase("haver");
		verb.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		PhraseElement _s3 = nlgFactory.createClause(verb, np); //$NON-NLS-1$ //$NON-NLS-2$
		_s3.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals("hão alguns cachorros bravos", realiser //$NON-NLS-1$
				.realise(_s3).getRealisation());
	}
	
	@Test
	public void testPassive001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec _s1 = nlgFactory.createClause(null,
				"incubar", nlgFactory.createNounPhrase("o", "bebê")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		_s1.setFeature(Feature.PASSIVE, true);
		_s1.setFeature(Feature.TENSE, Tense.PAST);

		Assert.assertEquals("o bebê foi incubado", realiser.realise(_s1).getRealisation());
	}

	@Test
	public void testPassive002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		// passive with subject and complement
		AbstractSPhraseSpec _s1 = nlgFactory.createClause(null,
				"incubar", nlgFactory.createNounPhrase("o", "bebê")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		_s1.setFeature(Feature.PASSIVE, true);
		_s1.setFeature(Feature.TENSE, Tense.PAST);
		
		_s1.setSubject(nlgFactory.createNounPhrase("a", "enfermeira")); //$NON-NLS-1$
		_s1.setFeature(Feature.PASSIVE, true);
		Assert.assertEquals("o bebê foi incubado pela enfermeira", //$NON-NLS-1$
				realiser.realise(_s1).getRealisation());
	}

	@Test
	public void testPassive003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		// passive with subject and indirect object
		AbstractSPhraseSpec _s2 = nlgFactory.createClause(null, "levar", //$NON-NLS-1$
				nlgFactory.createNounPhrase("o", "bebê")); //$NON-NLS-1$ //$NON-NLS-2$

		PhraseElement morphine = nlgFactory.createNounPhrase("para a enfermaria"); //$NON-NLS-1$
		_s2.setIndirectObject(morphine);
		_s2.setFeature(Feature.PASSIVE, true);
		_s2.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("o bebê foi levado para a enfermaria", //$NON-NLS-1$
				realiser.realise(_s2).getRealisation());
	}

	@Test
	public void testPassive004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		// test agreement in passive
		PhraseElement _s3 = nlgFactory.createClause(
				new PortugueseCoordinatedPhraseElement("meu cachorro", "seu gato"), "perseguir", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"George"); //$NON-NLS-1$
		_s3.setFeature(Feature.TENSE, Tense.PAST);
		_s3.addFrontModifier("ontem"); //$NON-NLS-1$
		Assert.assertEquals("ontem meu cachorro e seu gato perseguiram George", //$NON-NLS-1$
				realiser.realise(_s3).getRealisation());
	}

	@Test
	public void testPassive005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		// test agreement in passive
		PhraseElement _s3 = nlgFactory.createClause(
				new PortugueseCoordinatedPhraseElement("meu cachorro", "seu gato"), "perseguir", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"George"); //$NON-NLS-1$
		_s3.setFeature(Feature.TENSE, Tense.PAST);
		_s3.addFrontModifier("ontem"); //$NON-NLS-1$
		_s3.setFeature(Feature.PASSIVE, true);
		Assert.assertEquals("ontem George foi perseguido por meu cachorro e seu gato", //$NON-NLS-1$
				realiser.realise(_s3).getRealisation());
	}

	@Test
	public void testPassive006() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		// test correct pronoun forms
		NPPhraseSpec eu = nlgFactory.createNounPhrase("eu");
		NPPhraseSpec ele = nlgFactory.createNounPhrase("ele");
		
		PhraseElement _s4 = nlgFactory.createClause(ele, "perseguir",eu); 
		Assert.assertEquals("ele persegue me", realiser.realise(_s4).getRealisation());
		
		_s4 = nlgFactory.createClause(ele, "perseguir", eu);
		_s4.setFeature(Feature.PASSIVE, true);
		Assert.assertEquals("eu sou perseguido por lhe",realiser.realise(_s4).getRealisation()); //$NON-NLS-1$
	}
	
	/**
	 * Test that complements set within the VP are raised when sentence is
	 * passivised.
	 */
	@Test
	public void testPassiveWithInternalVPComplement() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement vp = nlgFactory.createVerbPhrase(nlgFactory.createWord("chatear", LexicalCategory.VERB));
		vp.addComplement(nlgFactory.createNounPhrase("o", "homem"));
		PhraseElement _s6 = nlgFactory.createClause(nlgFactory.createNounPhrase("a", "criança"), vp);
		_s6.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("a criança chateou o homem", realiser.realise(_s6).getRealisation());

		_s6.setFeature(Feature.PASSIVE, true);
		Assert.assertEquals("o homem foi chateado pela criança", 
				realiser.realise(_s6).getRealisation());
	}
	
	@Test
	public void testModal001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		clause.setFeature(Feature.MODAL, "deveria");
		
		Assert.assertEquals("o homem deveria dar para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
		
	}
	
	// modal + future -- uses present
	@Test
	public void testModal002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$

		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		clause.setFeature(Feature.MODAL, "deveria");
		clause.setFeature(Feature.TENSE, Tense.FUTURE);
		
		Assert.assertEquals("o homem deveria dar para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
	}

	// modal + present progressive
	@Test
	public void testModal003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$
		
		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		clause.setFeature(Feature.MODAL, "deveria");
		clause.setFeature(Feature.TENSE, Tense.FUTURE);
		clause.setFeature(Feature.PROGRESSIVE, true);
		
		Assert.assertEquals("o homem deveria estar dando para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
	}
	
	// modal + past tense
	@Test
	public void testModal004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$
		
		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		clause.setFeature(Feature.MODAL, "deveria");
		clause.setFeature(Feature.TENSE, Tense.PAST);
		
		Assert.assertEquals("o homem deveria ter dado para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
		
	}

	@Test
	public void testModal005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		NPPhraseSpec createNounPhrase = nlgFactory.createNounPhrase("o", "homem");
		clause.setSubject(createNounPhrase); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("dar")); //$NON-NLS-1$
		
		NPPhraseSpec flower = nlgFactory.createNounPhrase("flor"); //$NON-NLS-1$
		NPPhraseSpec john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		flower.setFeature(InternalFeature.SPECIFIER, john);
		
		PPPhraseSpec preposition = nlgFactory.createPrepositionPhrase("a");
		preposition.setComplement(flower);
		
		clause.setObject(preposition);
		clause.setIndirectObject(nlgFactory.createNounPhrase("para a", "mulher"));
		clause.setFeature(Feature.MODAL, "deveria");
		clause.setFeature(Feature.TENSE, Tense.PAST);
		clause.setFeature(Feature.PROGRESSIVE, true);
		
		Assert.assertEquals("o homem deveria ter estado dando para a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(clause).getRealisation());
		
	}

	/**
	 * Test for passivisation with mdoals
	 */
	@Test
	public void testModalWithPassive() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		
		NPPhraseSpec object = nlgFactory.createNounPhrase("a",
				"pizza");
		AdjPhraseSpec post = nlgFactory.createAdjectivePhrase("boa");
		AdvPhraseSpec as = nlgFactory.createAdverbPhrase("como");
		as.addComplement(post);
		VPPhraseSpec verb = nlgFactory.createVerbPhrase("classificar");
		verb.addPostModifier(as);
		verb.addComplement(object);
		AbstractSPhraseSpec s = nlgFactory.createClause();
		s.setVerbPhrase(verb);
		
		s.setFeature(Feature.MODAL, "pode");
		s.setFeature(Feature.PASSIVE, true);
		Assert.assertEquals("a pizza pode ser classificado como boa",
				realiser.realise(s).getRealisation());
	}

	@Test
	public void testPassiveWithPPMod() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		// passive with just complement
		NPPhraseSpec subject = nlgFactory.createNounPhrase("a","onda");
		subject.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		NPPhraseSpec object = nlgFactory.createNounPhrase("surfista");
		object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);

		
		AbstractSPhraseSpec _s1 = nlgFactory.createClause(subject,
				"transportar", object); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// add a PP complement
		PPPhraseSpec pp = nlgFactory.createPrepositionPhrase("para",
				nlgFactory.createNounPhrase("a", "praia"));
		pp.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		
		_s1.addPostModifier(pp);

		_s1.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		_s1.setFeature(Feature.PASSIVE, true);

		Assert.assertEquals(
				"surfistas são transportados para a praia pelas ondas", realiser //$NON-NLS-1$
						.realise(_s1).getRealisation());
	}

	@Test
	public void testCuePhrase() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec subject = nlgFactory.createNounPhrase("a","onda");
		subject.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		NPPhraseSpec object = nlgFactory.createNounPhrase("surfista");
		object.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);

		
		AbstractSPhraseSpec _s1 = nlgFactory.createClause(subject,
				"transportar", object); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// add a PP complement
		PPPhraseSpec pp = nlgFactory.createPrepositionPhrase("para",
				nlgFactory.createNounPhrase("a", "praia"));
		pp.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		
		_s1.addPostModifier(pp);
		_s1.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		_s1.setFeature(Feature.PASSIVE, true);
		_s1.addFrontModifier("contudo");

		//with comma separation
		realiser.setCommaSepCuephrase(true);
		Assert.assertEquals("contudo, surfistas são transportados para a praia pelas ondas", realiser.realise(_s1).getRealisation());

	}
	
	/**
	 * Check that setComplement replaces earlier complements
	 */
	@Test
	public void setComplementTest() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec s = nlgFactory.createClause();
		s.setSubject("eu");
		s.setVerb("ver");
		s.setObject("um cachorro");

		Assert.assertEquals("eu vejo um cachorro", realiser.realise(s).getRealisation());

		s.setObject("um gato");
		Assert.assertEquals("eu vejo um gato", realiser.realise(s).getRealisation());

		s.setObject("um lobo");
		Assert.assertEquals("eu vejo um lobo", realiser.realise(s).getRealisation());

	}
	
	@Test
	public void subclausesTest() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		// main sentence
		NPPhraseSpec acct = nlgFactory.createNounPhrase("um", "contador");

		// first postmodifier of "an accountant"
		VPPhraseSpec sub1 = nlgFactory.createVerbPhrase("chamar");
		sub1.addComplement("Jeff");
		sub1.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
		sub1.setFeature(Feature.APPOSITIVE, true);
		acct.setComplement(sub1);

		AbstractSPhraseSpec sub2 = nlgFactory.createClause();
		VPPhraseSpec subvp = nlgFactory.createVerbPhrase("viver");
		subvp.setFeature(Feature.TENSE, Tense.PAST);
		subvp.setComplement(nlgFactory.createPrepositionPhrase("em",
				nlgFactory.createNounPhrase("uma", "floresta")));
		sub2.setVerbPhrase(subvp);
		sub2.addPreModifier("que");
		acct.addPostModifier(sub2);

		// main sentence
		AbstractSPhraseSpec s = nlgFactory.createClause("era uma vez", nlgFactory.createVerbPhrase("haver"), acct);
		s.setFeature(Feature.TENSE, Tense.PAST);

		Assert.assertEquals(
				"era uma vez houve um contador chamado Jeff que viveu em uma floresta",
				realiser.realise(s).getRealisation());

	}

}
