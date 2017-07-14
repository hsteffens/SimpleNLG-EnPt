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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */
package simplenlg.syntax.portuguese;

import org.junit.Test;

import junit.framework.Assert;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.portuguese.PortugueseCoordinatedPhraseElement;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.AdvPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class FeatureTest {
	
	/**
	 * Tests use of the Possessive Feature.
	 */
	@Test
	public void testPossessiveFeature_PastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		// Create the pronoun 'ela'
		NLGElement she = nlgFactory.createWord("ela",LexicalCategory.PRONOUN);

		// Set possessive on the pronoun to make it 'dela'
		she.setFeature(Feature.POSSESSIVE, true);

		// Create a noun phrase with the subject lover and the determiner
		// as she
		PhraseElement herLover = nlgFactory.createNounPhrase(she,"amante");

		// Create a clause to say 'ele ser amante dela'
		PhraseElement clause = nlgFactory.createClause("ele", "ser", herLover);

		// Add the cue phrase need the comma as orthography
		// currently doesn't handle this.
		// This could be expanded to be a noun phrase with determiner
		// 'two' and noun 'week', set to plural and with a premodifier of
		// 'after'
		clause.setFeature(Feature.CUE_PHRASE, "após duas semanas,");

		// Add the 'for a fortnight' as a post modifier. Alternatively
		// this could be added as a prepositional phrase 'for' with a
		// complement of a noun phrase ('a' 'fortnight')
		clause.addPostModifier("por uma quinzena");

		// Set 'be' to 'was' as past tense
		clause.setFeature(Feature.TENSE,Tense.PAST);

		// Add the clause to a sentence.
		DocumentElement sentence1 = nlgFactory.createSentence(clause);

		// Realise the sentence
		NLGElement realised = realiser.realise(sentence1);
 		
		Assert.assertEquals("Após duas semanas, ele foi amante dela por uma quinzena.",
				realised.getRealisation());
	}

	/**
	 * Basic tests.
	 */
	@Test
	public void testTwoPossessiveFeature_PastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);

		// Create the pronoun 'she'
		NLGElement she = nlgFactory.createWord("ela",LexicalCategory.PRONOUN);

		// Set possessive on the pronoun to make it 'her'
		she.setFeature(Feature.POSSESSIVE, true);

		// Create a noun phrase with the subject lover and the determiner
		// as she
		PhraseElement herLover = nlgFactory.createNounPhrase(she,"amante");
		herLover.setPlural(true);

		// Create the pronoun 'he'
		NLGElement he = nlgFactory.createNounPhrase(LexicalCategory.PRONOUN,"ele");
		he.setPlural(true);

		// Create a clause to say 'they be her lovers'
		PhraseElement clause = nlgFactory.createClause(he, "ser", herLover);
		clause.setFeature(Feature.POSSESSIVE, true);

		// Add the cue phrase need the comma as orthography
		// currently doesn't handle this.
		// This could be expanded to be a noun phrase with determiner
		// 'two' and noun 'week', set to plural and with a premodifier of
		// 'after'
		clause.setFeature(Feature.CUE_PHRASE, "após duas semanas,");

		// Add the 'for a fortnight' as a post modifier. Alternatively
		// this could be added as a prepositional phrase 'for' with a
		// complement of a noun phrase ('a' 'fortnight')
		clause.addPostModifier("por uma quinzena");

		// Set 'be' to 'was' as past tense
		clause.setFeature(Feature.TENSE,Tense.PAST);
		
		// Add the clause to a sentence.
		DocumentElement sentence1 = nlgFactory.createSentence(clause);

		// Realise the sentence
		NLGElement realised = realiser.realise(sentence1);

		Assert.assertEquals("Após duas semanas, eles foram amantes dela por uma quinzena.", //$NON-NLS-1$
				realised.getRealisation());
	}

	/**
	 * Test use of the Complementiser feature by combining two S's using cue phrase and gerund.
	 */
	@Test
	public void testComplementiserFeature_PastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);

		PhraseElement born = nlgFactory.createClause("Dave Bus", "nascer");
		born.setFeature(Feature.TENSE,Tense.PAST);
		born.setFeature(Feature.COMPLEMENTISER, "em que");

		PhraseElement theHouse = nlgFactory.createNounPhrase("a", "casa");
		theHouse.addComplement(born);

		PhraseElement clause = nlgFactory.createClause(theHouse, "ser", nlgFactory.createPrepositionPhrase("em", "Edinburgh"));
		DocumentElement sentence = nlgFactory.createSentence(clause);
		NLGElement realised = realiser.realise(sentence);

		// Retrieve the realisation and dump it to the console
		Assert.assertEquals("A casa em que Dave Bus nasceu é em Edinburgh.",
				realised.getRealisation());
	}

	/**
	 * Test use of the Complementiser feature in a {@link AbstractCoordinatedPhraseElement} by combine two S's using cue phrase and gerund.
	 */
	@Test
	public void testComplementiserFeatureInACoordinatePhrase_PastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NLGElement dave = nlgFactory.createWord("Dave Bus", LexicalCategory.NOUN);
		NLGElement albert = nlgFactory.createWord("Albert", LexicalCategory.NOUN);
		
		AbstractCoordinatedPhraseElement coord1 = new PortugueseCoordinatedPhraseElement(dave, albert);
		
		PhraseElement born = nlgFactory.createClause(coord1, "nascer");
		born.setFeature(Feature.TENSE,Tense.PAST);
		born.setFeature(Feature.COMPLEMENTISER, "em que");

		PhraseElement theHouse = nlgFactory.createNounPhrase("a", "casa");
		theHouse.addComplement(born);

		PhraseElement clause = nlgFactory.createClause(theHouse, "ser", nlgFactory.createPrepositionPhrase("em", "Edinburgh"));
		DocumentElement sentence = nlgFactory.createSentence(clause);
		
		NLGElement realised = realiser.realise(sentence);

		// Retrieve the realisation and dump it to the console
		Assert.assertEquals("A casa em que Dave Bus e Albert nasceram é em Edinburgh.",
				realised.getRealisation());
	}

	/**
	 * Test the use of the Progressive and Complementiser Features in future tense.
	 */
	@Test
	public void testProgressiveAndComplementiserFeatures_FutureTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);

		// Inner clause is 'I' 'make' 'sentence' 'for'.
		PhraseElement inner = nlgFactory.createClause("eu","fazer", "sentenças");
		// Inner clause set to progressive.
		inner.setFeature(Feature.PROGRESSIVE,true);
		
		//Complementiser on inner clause is 'whom'
		inner.setFeature(Feature.COMPLEMENTISER, "a quem");
		
		// create the engineer and add the inner clause as post modifier 
		PhraseElement engineer = nlgFactory.createNounPhrase("o engenheiro");
		engineer.addComplement(inner);
		
		// Outer clause is: 'the engineer' 'go' (preposition 'to' 'holidays')
		PhraseElement outer = nlgFactory.createClause(engineer,"ir",nlgFactory.createPrepositionPhrase("entrar de","férias"));

		// Outer clause tense is Future.
		outer.setFeature(Feature.TENSE, Tense.FUTURE);
		
		// Possibly progressive as well not sure.
		outer.setFeature(Feature.PROGRESSIVE,true);
		
		//Outer clause postmodifier would be 'tomorrow'
		outer.addPostModifier("amanhã");
		DocumentElement sentence = nlgFactory.createSentence(outer);
		NLGElement realised = realiser.realise(sentence);

		// Retrieve the realisation and dump it to the console
		Assert.assertEquals("O engenheiro a quem eu estou fazendo sentenças estará indo entrar de férias amanhã.",
				realised.getRealisation());
	}

	
	/**
	 * Tests the use of the Complementiser, Passive, Perfect features in past tense.
	 */
	@Test
	public void testComplementiserPassivePerfectFeatures_PastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement inner = nlgFactory.createClause("eu", "jogar", "poker");
		inner.setFeature(Feature.COMPLEMENTISER, "onde");
		inner.setFeature(Feature.TENSE,Tense.PAST);
		
		PhraseElement house = nlgFactory.createNounPhrase("a", "casa");
		house.addComplement(inner);
		
		AbstractSPhraseSpec outer = nlgFactory.createClause(null, "abandonar", house);
		outer.setFeature(Feature.TENSE,Tense.PAST);
		
		outer.addPostModifier("desde 1986");
		
		outer.setFeature(Feature.PASSIVE, true);
		outer.setFeature(Feature.PERFECT, true);
		
		DocumentElement sentence = nlgFactory.createSentence(outer);
		NLGElement realised = realiser.realise(sentence); 

		// Retrieve the realisation and dump it to the console
		Assert.assertEquals("A casa onde eu joguei poker foi abandonado desde 1986.",
				realised.getRealisation());
	}
	
	/**
	 * Tests the user of the progressive and complementiser featuers in past tense.
	 */
	@Test
	public void testProgressiveComplementiserFeatures_PastTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);

		NLGElement sandwich = nlgFactory.createNounPhrase(LexicalCategory.NOUN, "sanduíche");
		sandwich.setPlural(true);
		// 
		PhraseElement first = nlgFactory.createClause("eu", "fazer", sandwich);
		first.setFeature(Feature.TENSE,Tense.PAST);
		first.setFeature(Feature.PROGRESSIVE,true);
		first.setPlural(false);
		
		PhraseElement second = nlgFactory.createClause("a maionese", "acabar");
		second.setFeature(Feature.TENSE,Tense.PAST);
		// 
		second.setFeature(Feature.COMPLEMENTISER, "quando");
		
		first.addComplement(second);
		
		DocumentElement sentence = nlgFactory.createSentence(first);
		NLGElement realised = realiser.realise(sentence);

		// Retrieve the realisation and dump it to the console
		Assert.assertEquals("Eu estive fazendo sanduíches quando a maionese acabou.",
				realised.getRealisation());
	}
	
   /**
	* Test the use of Passive in creating a Passive sentence structure: <Object> + [be] + <verb> + [by] + [Subject].
	*/
	@Test
	public void testPassiveFeature() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
	
		PhraseElement phrase = nlgFactory.createClause("recessão", "afetar", "o valor");
		phrase.setFeature(Feature.PASSIVE, true);
		DocumentElement sentence = nlgFactory.createSentence(phrase);
		NLGElement realised = realiser.realise(sentence);
	
		Assert.assertEquals("O valor é afetado pela recessão.", realised.getRealisation());
	}
	
	
	/**
	 * Test for repetition of the future auxiliary "will", courtesy of Luxor
	 * Vlonjati
	 */
	@Test
	public void testFutureTense() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec test = nlgFactory.createClause();

		NPPhraseSpec subj = nlgFactory.createNounPhrase("eu");

		VPPhraseSpec verb = nlgFactory.createVerbPhrase("ir");

		AdvPhraseSpec adverb = nlgFactory.createAdverbPhrase("amanhã");

		test.setSubject(subj);
		test.setVerbPhrase(verb);
		test.setFeature(Feature.TENSE, Tense.FUTURE);
		test.addPostModifier(adverb);
		String sentence = realiser.realiseSentence(test);
		Assert.assertEquals("Eu irei amanhã.", sentence);
		
		AbstractSPhraseSpec test2 = nlgFactory.createClause();
		NLGElement vb = nlgFactory.createWord("ir", LexicalCategory.VERB);
		test2.setSubject(subj);
		test2.setVerb(vb);
		test2.setFeature(Feature.TENSE, Tense.FUTURE);
		test2.addPostModifier(adverb);
		String sentence2 = realiser.realiseSentence(test);
		Assert.assertEquals("Eu irei amanhã.", sentence2);

	}
	
	
}
