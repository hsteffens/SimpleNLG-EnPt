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
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.features.portuguese.PortugueseInterrogativeType;
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
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class InterrogativeTest {

	/**
	 * Tests a couple of fairly simple questions.
	 */
	@Test
	public void testSimpleQuestions001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement woman = nlgFactory.createNounPhrase("a", "mulher");
		VPPhraseSpec kiss = nlgFactory.createVerbPhrase("beijar"); 
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");

		// simple present
		AbstractSPhraseSpec s1 = nlgFactory.createClause(woman, kiss, man);
		s1.setFeature(Feature.TENSE, Tense.PRESENT);
		s1.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);

		DocumentElement sent = nlgFactory.createSentence(s1);
		Assert.assertEquals("A mulher beija o homem?", realiser.realise(sent).getRealisation());
	}	
	
	@Test
	public void testSimpleQuestions002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement woman = nlgFactory.createNounPhrase("a", "mulher");
		VPPhraseSpec kiss = nlgFactory.createVerbPhrase("beijar"); 
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");

		// simple present
		AbstractSPhraseSpec s1 = nlgFactory.createClause(woman, kiss, man);
		s1.setFeature(Feature.TENSE, Tense.PAST);
		s1.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		Assert.assertEquals("a mulher beijou o homem", realiser.realise(s1).getRealisation());
	}
	
	@Test
	public void testSimpleQuestions003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("cachorro");
		NPPhraseSpec onTheRock = nlgFactory.createNounPhrase("na", "rocha");
		AbstractSPhraseSpec s2 = nlgFactory.createClause("há", "algum", dog); //$NON-NLS-1$ //$NON-NLS-2$
		s2.addPostModifier(onTheRock);
		s2.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		Assert.assertEquals("há algum cachorro na rocha", realiser.realise(s2).getRealisation());
	}
	
	@Test
	public void testSimpleQuestions004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("cachorro");
		NPPhraseSpec onTheRock = nlgFactory.createNounPhrase("na", "rocha");
		AbstractSPhraseSpec s2 = nlgFactory.createClause("há", "algum", dog); //$NON-NLS-1$ //$NON-NLS-2$
		s2.addPostModifier(onTheRock);
		s2.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		s2.setFeature(Feature.PERFECT, true);
		Assert.assertEquals("há algum cachorro na rocha", realiser.realise(s2).getRealisation());
	}
	
	@Test
	public void testSimpleQuestions005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		VPPhraseSpec give = nlgFactory.createVerbPhrase("dar");
		PhraseElement woman = nlgFactory.createNounPhrase("a", "mulher");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		// progressive
		PhraseElement john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		PhraseElement flower = nlgFactory.createNounPhrase(john, "flor"); //$NON-NLS-1$
		flower.addPreModifier("a");
		AbstractSPhraseSpec s3 = nlgFactory.createClause(man, give, flower);
		s3.setIndirectObject(woman);
		s3.setFeature(Feature.TENSE, Tense.PAST);
		s3.setFeature(Feature.PROGRESSIVE, true);
		s3.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		NLGElement realised = realiser.realise(s3);
		Assert.assertEquals("o homem esteve dando a mulher a flor do John", //$NON-NLS-1$
				realised.getRealisation());
	}
	
	@Test
	public void testSimpleQuestions006() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		VPPhraseSpec give = nlgFactory.createVerbPhrase("dar");
		PhraseElement woman = nlgFactory.createNounPhrase("a", "mulher");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		// progressive
		PhraseElement john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		PhraseElement flower = nlgFactory.createNounPhrase(john, "flor"); //$NON-NLS-1$
		flower.addPreModifier("a");
		woman = nlgFactory.createNounPhrase("a", "mulher"); //$NON-NLS-1$ //$NON-NLS-2$
		AbstractSPhraseSpec s3 = nlgFactory.createClause(man, give, flower);
		s3.setIndirectObject(woman);
		s3.setFeature(Feature.TENSE, Tense.PAST);
		s3.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		s3.setFeature(Feature.MODAL, "deveria"); //$NON-NLS-1$
		Assert.assertEquals(
				"o homem deveria ter dado a mulher a flor do John", //$NON-NLS-1$
				realiser.realise(s3).getRealisation());
	}
	
	@Test
	public void testSimpleQuestions007() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		AbstractCoordinatedPhraseElement subjects = new PortugueseCoordinatedPhraseElement(
				nlgFactory.createNounPhrase("Jane"), //$NON-NLS-1$
				nlgFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		AbstractSPhraseSpec s4 = nlgFactory.createClause(subjects, "pegar", //$NON-NLS-1$
				"as bolas"); //$NON-NLS-1$
		s4.addPostModifier("na loja"); //$NON-NLS-1$
		s4.setFeature(Feature.CUE_PHRASE, "contudo,"); //$NON-NLS-1$
		s4.addFrontModifier("amanhã"); //$NON-NLS-1$
		s4.setFeature(Feature.TENSE, Tense.FUTURE);
		s4.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		Assert.assertEquals(
				"contudo, Jane e Andrew pegarão as bolas na loja amanhã", //$NON-NLS-1$
				realiser.realise(s4).getRealisation());
	}
	
	@Test
	public void testNegatedQuestions001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);

		PhraseElement woman = nlgFactory.createNounPhrase("a", "mulher");
		VPPhraseSpec kiss = nlgFactory.createVerbPhrase("beijar"); 
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		// sentence: "the woman did not kiss the man"
		AbstractSPhraseSpec s1 = nlgFactory.createClause(woman, kiss, man);
		s1.setFeature(Feature.TENSE, Tense.PAST);
		s1.setFeature(Feature.NEGATED, true);
		s1.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		Assert.assertEquals("a mulher não beijou o homem", realiser.realise(s1).getRealisation());
	}
	
	@Test
	public void testNegatedQuestions002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);

		AbstractCoordinatedPhraseElement subjects = new PortugueseCoordinatedPhraseElement(
				nlgFactory.createNounPhrase("Jane"), //$NON-NLS-1$
				nlgFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		AbstractSPhraseSpec s4 = nlgFactory.createClause(subjects, "pegar", "as bolas"); //$NON-NLS-1$
		s4.addPostModifier("na loja"); //$NON-NLS-1$
		s4.setFeature(Feature.CUE_PHRASE, "contudo,"); //$NON-NLS-1$
		s4.addFrontModifier("amanhã"); //$NON-NLS-1$
		s4.setFeature(Feature.NEGATED, true);
		s4.setFeature(Feature.TENSE, Tense.FUTURE);
		s4.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		Assert.assertEquals(
				"contudo, Jane e Andrew não pegarão as bolas na loja amanhã", //$NON-NLS-1$
				realiser.realise(s4).getRealisation());
	}
	
	@Test
	public void testCoordinateVPQuestions001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		VPPhraseSpec kiss = nlgFactory.createVerbPhrase("beijar"); 
		VPPhraseSpec walk = nlgFactory.createVerbPhrase("caminhar");
		NPPhraseSpec inTheRoom = nlgFactory.createNounPhrase("no", "quarto");
		
		AbstractCoordinatedPhraseElement complex = new PortugueseCoordinatedPhraseElement(
				kiss, walk);
		kiss.addComplement(dog);
		walk.addComplement(inTheRoom);

		// sentence: "However, tomorrow, Jane and Andrew will kiss the dog and
		// will walk in the room"
		AbstractCoordinatedPhraseElement subjects = new PortugueseCoordinatedPhraseElement(
				nlgFactory.createNounPhrase("Jane"), //$NON-NLS-1$
				nlgFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		AbstractSPhraseSpec s4 = nlgFactory.createClause(subjects, complex);
		s4.setFeature(Feature.CUE_PHRASE, "contudo"); //$NON-NLS-1$
		s4.addFrontModifier("amanhã"); //$NON-NLS-1$
		s4.setFeature(Feature.TENSE, Tense.FUTURE);

		Assert.assertEquals(
				"contudo amanhã Jane e Andrew beijarão o cachorro e caminharão no quarto", //$NON-NLS-1$
				realiser.realise(s4).getRealisation());
		
	}
	
	@Test
	public void testCoordinateVPQuestions002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		VPPhraseSpec kiss = nlgFactory.createVerbPhrase("beijar"); 
		VPPhraseSpec walk = nlgFactory.createVerbPhrase("caminhar");
		NPPhraseSpec inTheRoom = nlgFactory.createNounPhrase("no", "quarto");
		
		AbstractCoordinatedPhraseElement subjects = new PortugueseCoordinatedPhraseElement(
				nlgFactory.createNounPhrase("Jane"), //$NON-NLS-1$
				nlgFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		kiss.addComplement(dog);
		walk.addComplement(inTheRoom);
		
		AbstractCoordinatedPhraseElement complex = new PortugueseCoordinatedPhraseElement(kiss, walk);
		AbstractSPhraseSpec s4 = nlgFactory.createClause(subjects, complex);
		s4.setFeature(Feature.CUE_PHRASE, "contudo"); //$NON-NLS-1$
		s4.addFrontModifier("amanhã"); //$NON-NLS-1$
		s4.setFeature(Feature.TENSE, Tense.FUTURE);
		s4.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);

		Assert.assertEquals(
				"contudo Jane e Andrew beijarão o cachorro e caminharão no quarto amanhã", //$NON-NLS-1$
				realiser.realise(s4).getRealisation());
		
	}
	
	@Test
	public void testSimpleQuestions008() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement s = nlgFactory.createClause("a mulher", "beijar", "o homem"); //$NON-NLS-1$
		s.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		Assert.assertEquals("a mulher beija o homem", realiser.realise(s).getRealisation());
	}

	@Test
	public void testSimpleQuestions009() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement woman = nlgFactory.createNounPhrase("a", "mulher");
		PhraseElement s = nlgFactory.createClause(woman, "beijar", "o homem"); //$NON-NLS-1$
		s.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		s.setFeature(Feature.PASSIVE, true);
		Assert.assertEquals("o homem é beijado pela mulher", realiser.realise(s).getRealisation());
	}
	
	@Test
	public void testSimpleQuestions010() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement woman = nlgFactory.createNounPhrase("a", "mulher");
		PhraseElement s = nlgFactory.createClause(woman, "beijar", "o homem"); //$NON-NLS-1$
		s.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);

		Assert.assertEquals("quem beija o homem", realiser.realise(s).getRealisation());
	}
	
	@Test
	public void testSimpleQuestions011() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement woman = nlgFactory.createNounPhrase("a", "mulher");
		PhraseElement s = nlgFactory.createClause(woman, "beijar", "o homem"); //$NON-NLS-1$
		s.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_OBJECT);

		Assert.assertEquals("quem a mulher beija", realiser.realise(s).getRealisation());
	}
	
	@Test
	public void testSimpleQuestions012() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement woman = nlgFactory.createNounPhrase("a", "mulher");
		PhraseElement s = nlgFactory.createClause(woman, "beijar", "o homem"); //$NON-NLS-1$
		s.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);
		s.setFeature(Feature.PASSIVE, true);

		Assert.assertEquals("quem é o homem beijado", realiser.realise(s).getRealisation());
	}
	
	@Test
	public void testWHQuestions001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractCoordinatedPhraseElement subjects = new PortugueseCoordinatedPhraseElement(
				nlgFactory.createNounPhrase("Jane"), //$NON-NLS-1$
				nlgFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		AbstractSPhraseSpec s4 = nlgFactory.createClause(subjects, "pegar", "as bolas"); //$NON-NLS-1$
		s4.addPostModifier("na loja"); //$NON-NLS-1$
		s4.setFeature(Feature.CUE_PHRASE, "contudo,"); //$NON-NLS-1$
		s4.addFrontModifier("amanhã"); //$NON-NLS-1$
		s4.setFeature(Feature.TENSE, Tense.FUTURE);
		s4.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);
		
		Assert.assertEquals(
				"contudo, quem pegará as bolas na loja amanhã", //$NON-NLS-1$
				realiser.realise(s4).getRealisation());
	}
	
	@Test
	public void testWHQuestions002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractCoordinatedPhraseElement subjects = new PortugueseCoordinatedPhraseElement(
				nlgFactory.createNounPhrase("Jane"), //$NON-NLS-1$
				nlgFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		AbstractSPhraseSpec s4 = nlgFactory.createClause(subjects, "pegar", "as bolas"); //$NON-NLS-1$
		s4.addPostModifier("na loja"); //$NON-NLS-1$
		s4.setFeature(Feature.CUE_PHRASE, "contudo,"); //$NON-NLS-1$
		s4.addFrontModifier("amanhã"); //$NON-NLS-1$
		s4.setFeature(Feature.TENSE, Tense.FUTURE);
		s4.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_OBJECT);
		
		Assert.assertEquals(
				"contudo, que pegarão Jane e Andrew na loja amanhã", //$NON-NLS-1$
				realiser.realise(s4).getRealisation());
	}

	@Test
	public void testWHQuestions003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractCoordinatedPhraseElement subjects = new PortugueseCoordinatedPhraseElement(
				nlgFactory.createNounPhrase("Jane"), //$NON-NLS-1$
				nlgFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		AbstractSPhraseSpec s4 = nlgFactory.createClause(subjects, "pegar", "as bolas"); //$NON-NLS-1$
		s4.addPostModifier("na loja"); //$NON-NLS-1$
		s4.setFeature(Feature.CUE_PHRASE, "contudo,"); //$NON-NLS-1$
		s4.addFrontModifier("amanhã"); //$NON-NLS-1$
		s4.setFeature(Feature.TENSE, Tense.FUTURE);
		s4.setFeature(Feature.PASSIVE, true);
		s4.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_OBJECT);
		
		Assert.assertEquals(
				"contudo, que será pegado na loja por Jane e Andrew amanhã", //$NON-NLS-1$
				realiser.realise(s4).getRealisation());
	}
	
	@Test
	public void testWHQuestions004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractCoordinatedPhraseElement subjects = new PortugueseCoordinatedPhraseElement(
				nlgFactory.createNounPhrase("Jane"), //$NON-NLS-1$
				nlgFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		AbstractSPhraseSpec s4 = nlgFactory.createClause(subjects, "pegar", "as bolas"); //$NON-NLS-1$
		s4.addPostModifier("na loja"); //$NON-NLS-1$
		s4.setFeature(Feature.CUE_PHRASE, "contudo,"); //$NON-NLS-1$
		s4.addFrontModifier("amanhã"); //$NON-NLS-1$
		s4.setFeature(Feature.TENSE, Tense.FUTURE);
		s4.setFeature(Feature.PASSIVE, true);
		s4.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.HOW);
		
		Assert.assertEquals(
				"contudo, como será as bolas pegado na loja por Jane e Andrew amanhã", //$NON-NLS-1$
				realiser.realise(s4).getRealisation());
	}
	
	@Test
	public void testWHQuestions005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractCoordinatedPhraseElement subjects = new PortugueseCoordinatedPhraseElement(
				nlgFactory.createNounPhrase("Jane"), //$NON-NLS-1$
				nlgFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		AbstractSPhraseSpec s4 = nlgFactory.createClause(subjects, "pegar", "o livro"); //$NON-NLS-1$
		s4.addPostModifier("na loja"); //$NON-NLS-1$
		s4.setFeature(Feature.CUE_PHRASE, "contudo,"); //$NON-NLS-1$
		s4.addFrontModifier("amanhã"); //$NON-NLS-1$
		s4.setFeature(Feature.PASSIVE, true);
		s4.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHY);
		
		Assert.assertEquals(
				"contudo, por que o livro é pegado na loja por Jane e Andrew amanhã", //$NON-NLS-1$
				realiser.realise(s4).getRealisation());
	}
	
	@Test
	public void testWHQuestions006() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractCoordinatedPhraseElement subjects = new PortugueseCoordinatedPhraseElement(
				nlgFactory.createNounPhrase("Jane"), //$NON-NLS-1$
				nlgFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
		AbstractSPhraseSpec s4 = nlgFactory.createClause(subjects, "pegar", "o livro"); //$NON-NLS-1$
		s4.addPostModifier("na loja"); //$NON-NLS-1$
		s4.setFeature(Feature.CUE_PHRASE, "contudo,"); //$NON-NLS-1$
		s4.addFrontModifier("amanhã"); //$NON-NLS-1$
		s4.setFeature(Feature.MODAL, "deveria"); //$NON-NLS-1$
		s4.setFeature(Feature.PASSIVE, true);
		s4.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.HOW);
		
		Assert.assertEquals(
				"contudo, como deveria o livro ser pegado na loja por Jane e Andrew amanhã", //$NON-NLS-1$
				realiser.realise(s4).getRealisation());
	}
	
	@Test
	public void testWHQuestions007() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		VPPhraseSpec give = nlgFactory.createVerbPhrase("dar");
		PhraseElement woman = nlgFactory.createNounPhrase("a", "mulher");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		// progressive
		PhraseElement john = nlgFactory.createNounPhrase("John"); //$NON-NLS-1$
		john.setFeature(Feature.POSSESSIVE, true);
		PhraseElement flower = nlgFactory.createNounPhrase(john, "flor"); //$NON-NLS-1$
		flower.addPreModifier("a");
		AbstractSPhraseSpec s3 = nlgFactory.createClause(man, give, flower);
		s3.setIndirectObject(woman);
		s3.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_INDIRECT_OBJECT);
		
		Assert.assertEquals(
				"para quem o homem dá a flor do John", //$NON-NLS-1$
				realiser.realise(s3).getRealisation());
	}
	

	/**
	 * WH movement in the progressive
	 */
	@Test
	public void testProgrssiveWHSubjectQuestions() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject("Mary");
		p.setVerb("comer");
		p.setObject(nlgFactory.createNounPhrase("a", "torta"));
		p.setFeature(Feature.PROGRESSIVE, true);
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("quem está comendo a torta", //$NON-NLS-1$
				realiser.realise(p).getRealisation());
	}

	/**
	 * WH movement in the progressive
	 */
	@Test
	public void testProgrssiveWHObjectQuestions() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject("Mary");
		p.setVerb("comer");
		p.setObject(nlgFactory.createNounPhrase("a", "torta"));
		p.setFeature(Feature.PROGRESSIVE, true);
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("que está Mary comendo", //$NON-NLS-1$
				realiser.realise(p).getRealisation());

	}

	/**
	 * Negation with WH movement for subject
	 */
	@Test
	public void testNegatedWHSubjQuestions() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		
		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject("Mary");
		p.setVerb("comer");
		p.setObject(nlgFactory.createNounPhrase("a", "torta"));
		p.setFeature(Feature.NEGATED, true);
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("quem não come a torta", //$NON-NLS-1$
				realiser.realise(p).getRealisation());
	}

	/**
	 * Negation with WH movement for object
	 */
	@Test
	public void testNegatedWHObjQuestions() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject("Mary");
		p.setVerb("comer");
		p.setObject(nlgFactory.createNounPhrase("a", "torta"));
		p.setFeature(Feature.NEGATED, true);

		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_OBJECT);
		NLGElement realisation = realiser.realise(p);
		Assert.assertEquals("que Mary não come", //$NON-NLS-1$
				realisation.getRealisation());
	}

	/**
	 * Test questyions in the tutorial.
	 */
	@Test
	public void testTutorialQuestions001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);

		PhraseElement p = nlgFactory.createClause("Mary", "perseguir", //$NON-NLS-1$ //$NON-NLS-2$
				"George"); //$NON-NLS-1$
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		Assert.assertEquals("Mary persegue George", realiser.realise(p) //$NON-NLS-1$
				.getRealisation());

	}
	
	/**
	 * Test questyions in the tutorial.
	 */
	@Test
	public void testTutorialQuestions002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);

		PhraseElement p = nlgFactory.createClause("Mary", "perseguir", //$NON-NLS-1$ //$NON-NLS-2$
				"George"); //$NON-NLS-1$
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_OBJECT);
		Assert.assertEquals("quem Mary persegue", realiser.realise(p) //$NON-NLS-1$
				.getRealisation());

	}
	
	@Test
	public void testModalWHSubjectQuestion001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear",man);
		p.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("o cachorro chateou o homem", realiser.realise(p)
				.getRealisation());
	}
	
	@Test
	public void testModalWHSubjectQuestion002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear",man);
		p.setFeature(Feature.TENSE, Tense.PAST);
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("quem chateou o homem", realiser.realise(p)
				.getRealisation());
	}
	
	@Test
	public void testModalWHSubjectQuestion003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear",man);
		p.setFeature(Feature.TENSE, Tense.PAST);
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("que chateou o homem", realiser.realise(p)
				.getRealisation());
	}
	
	@Test
	public void testModalWHSubjectQuestion004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear",man);
		p.setFeature(Feature.TENSE, Tense.PAST);
		p.setFeature(Feature.MODAL, "pode");
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("quem pode ter chateado o homem", realiser.realise(p)
				.getRealisation());
	}
	
	@Test
	public void testModalWHSubjectQuestion005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear",man);
		p.setFeature(Feature.TENSE, Tense.FUTURE);
		p.setFeature(Feature.MODAL, "poderá");
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("quem poderá chatear o homem", realiser.realise(p)
				.getRealisation());
	}
	
	@Test
	public void testModalWHObjectQuestion001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear", man);
		p.setFeature(Feature.TENSE, Tense.PAST);
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_OBJECT);

		Assert.assertEquals("quem o cachorro chateou", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testModalWHObjectQuestion002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear", man);
		p.setFeature(Feature.TENSE, Tense.PAST);
		p.setFeature(Feature.MODAL, "pode");
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_OBJECT);

		Assert.assertEquals("quem pode o cachorro ter chateado", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testModalWHObjectQuestion003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear", man);
		p.setFeature(Feature.TENSE, Tense.PAST);
		p.setFeature(Feature.MODAL, "pode");
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_OBJECT);

		Assert.assertEquals("que pode o cachorro ter chateado", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testModalWHObjectQuestion004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear", man);
		p.setFeature(Feature.TENSE, Tense.FUTURE);
		p.setFeature(Feature.MODAL, "pode");
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_OBJECT);

		Assert.assertEquals("quem pode o cachorro chatear", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testModalWHObjectQuestion005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear", man);
		p.setFeature(Feature.TENSE, Tense.FUTURE);
		p.setFeature(Feature.MODAL, "pode");
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_OBJECT);

		Assert.assertEquals("que pode o cachorro chatear", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testAuxWHSubjectQuestion001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear", man);
		p.setFeature(Feature.TENSE, Tense.PRESENT);
		p.setFeature(Feature.PERFECT, true);
		Assert.assertEquals("o cachorro chatea o homem",
				realiser.realise(p).getRealisation());
	}

	@Test
	public void testAuxWHSubjectQuestion002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear", man);
		p.setFeature(Feature.TENSE, Tense.PRESENT);
		p.setFeature(Feature.PERFECT, true);
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("quem chatea o homem",
				realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testAuxWHSubjectQuestion003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear", man);
		p.setFeature(Feature.TENSE, Tense.PRESENT);
		p.setFeature(Feature.PERFECT, true);
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("que chatea o homem",
				realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testAuxWHObjectQuestion001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear", man);
		p.setFeature(Feature.TENSE, Tense.FUTURE);
		p.setFeature(Feature.PERFECT, true);
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_OBJECT);

		Assert.assertEquals("quem o cachorro chateará", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testAuxWHObjectQuestion002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		
		AbstractSPhraseSpec p = nlgFactory.createClause(dog, "chatear", man);
		p.setFeature(Feature.TENSE, Tense.FUTURE);
		p.setFeature(Feature.PERFECT, true);
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_OBJECT);

		Assert.assertEquals("que chateará o cachorro", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testBeQuestions001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause(
				nlgFactory.createNounPhrase("um", "bola"),
				nlgFactory.createWord("ser", LexicalCategory.VERB),
				nlgFactory.createNounPhrase("um", "brinquedo"));

		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("que é uma bola", realiser.realise(p)
				.getRealisation());
	}
	
	@Test
	public void testBeQuestions002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause(
				nlgFactory.createNounPhrase("um", "bola"),
				nlgFactory.createWord("ser", LexicalCategory.VERB),
				nlgFactory.createNounPhrase("um", "brinquedo"));

		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		Assert.assertEquals("uma bola é um brinquedo", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testBeQuestions003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause(
				nlgFactory.createNounPhrase("um", "bola"),
				nlgFactory.createWord("ser", LexicalCategory.VERB),
				nlgFactory.createNounPhrase("um", "brinquedo"));

		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("que é um brinquedo", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testBeQuestions004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p2 = nlgFactory.createClause("ela", "ser", "bonita");
		p2.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHY);
		Assert.assertEquals("por que ela é bonita", realiser.realise(p2).getRealisation());
	}
	
	@Test
	public void testBeQuestions005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p2 = nlgFactory.createClause("ela", "precisar", "ir");
		p2.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHERE);
		Assert.assertEquals("onde ela precisa ir", realiser.realise(p2).getRealisation());
	}
	
	@Test
	public void testBeQuestionsFuture001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause(
				nlgFactory.createNounPhrase("um", "bola"),
				nlgFactory.createWord("ser", LexicalCategory.VERB),
				nlgFactory.createNounPhrase("um", "brinquedo"));
		p.setFeature(Feature.TENSE, Tense.FUTURE);

		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("que será uma bola", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testBeQuestionsFuture002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause(
				nlgFactory.createNounPhrase("um", "bola"),
				nlgFactory.createWord("ser", LexicalCategory.VERB),
				nlgFactory.createNounPhrase("um", "brinquedo"));
		p.setFeature(Feature.TENSE, Tense.FUTURE);

		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		Assert.assertEquals("uma bola será um brinquedo", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testBeQuestionsFuture003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause(
				nlgFactory.createNounPhrase("um", "bola"),
				nlgFactory.createWord("ser", LexicalCategory.VERB),
				nlgFactory.createNounPhrase("um", "brinquedo"));
		p.setFeature(Feature.TENSE, Tense.FUTURE);

		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("que será um brinquedo", realiser.realise(p).getRealisation());
	}
	
	@Test
	public void testBeQuestionsFuture004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p2 = nlgFactory.createClause("Mary", "ser", "bonita");
		p2.setFeature(Feature.TENSE, Tense.FUTURE);
		p2.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHY);
		Assert.assertEquals("por que Mary será bonita", realiser.realise(p2).getRealisation());
	}
	
	@Test
	public void testBeQuestionsFuture005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p2 = nlgFactory.createClause("Mary", "ser", "bonita");
		p2.setFeature(Feature.TENSE, Tense.FUTURE);
		p2.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHERE);
		Assert.assertEquals("onde Mary será bonita", realiser.realise(p2).getRealisation());
	}
	
	@Test
	public void testBeQuestionsFuture006() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p2 = nlgFactory.createClause("Mary", "ser", "bonita");
		p2.setFeature(Feature.TENSE, Tense.FUTURE);
		p2.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("quem será bonita", realiser.realise(p2).getRealisation());
	}
	
	@Test
	public void testBeQuestionsPast001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		AbstractSPhraseSpec p = nlgFactory.createClause(
				nlgFactory.createNounPhrase("um", "bola"),
				nlgFactory.createWord("ser", LexicalCategory.VERB),
				nlgFactory.createNounPhrase("um", "brinquedo"));
		p.setFeature(Feature.TENSE, Tense.PAST);

		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_OBJECT);
		Assert.assertEquals("que foi uma bola", realiser.realise(p)
				.getRealisation());
	}
	
	@Test
	public void testBeQuestionsPast002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		AbstractSPhraseSpec p = nlgFactory.createClause(
				nlgFactory.createNounPhrase("um", "bola"),
				nlgFactory.createWord("ser", LexicalCategory.VERB),
				nlgFactory.createNounPhrase("um", "brinquedo"));
		p.setFeature(Feature.TENSE, Tense.PAST);

		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO);
		Assert.assertEquals("uma bola foi um brinquedo", realiser.realise(p)
				.getRealisation());
	}
	
	@Test
	public void testBeQuestionsPast003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		AbstractSPhraseSpec p = nlgFactory.createClause(
				nlgFactory.createNounPhrase("um", "bola"),
				nlgFactory.createWord("ser", LexicalCategory.VERB),
				nlgFactory.createNounPhrase("um", "brinquedo"));
		p.setFeature(Feature.TENSE, Tense.PAST);

		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHAT_SUBJECT);
		Assert.assertEquals("que foi um brinquedo", realiser.realise(p)
				.getRealisation());
	}
	
	@Test
	public void testBeQuestionsPast004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		AbstractSPhraseSpec p2 = nlgFactory.createClause("Mary", "ser", "bonita");
		p2.setFeature(Feature.TENSE, Tense.PAST);
		p2.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHY);
		Assert.assertEquals("por que Mary foi bonita", realiser.realise(p2).getRealisation());
	}
	
	@Test
	public void testBeQuestionsPast005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		AbstractSPhraseSpec p2 = nlgFactory.createClause("Mary", "ser", "bonita");
		p2.setFeature(Feature.TENSE, Tense.PAST);
		p2.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHERE);
		Assert.assertEquals("onde Mary foi bonita", realiser.realise(p2).getRealisation());
	}
	
	@Test
	public void testBeQuestionsPast006() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		AbstractSPhraseSpec p2 = nlgFactory.createClause("Mary", "ser", "bonita");
		p2.setFeature(Feature.TENSE, Tense.PAST);
		p2.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_SUBJECT);
		Assert.assertEquals("quem foi bonita", realiser.realise(p2).getRealisation());
	}
	
	/**
	 * Test a simple "how" question, based on query from Albi Oxa
	 */
	@Test
	public void testHowPredicateQuestion() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec test = nlgFactory.createClause();
		NPPhraseSpec subject = nlgFactory.createNounPhrase("Você");

		subject.setFeature(Feature.PRONOMINAL, true);
		subject.setFeature(Feature.PERSON, Person.THIRD);
		test.setSubject(subject);
		test.setVerb("estar");

		test.setFeature(Feature.INTERROGATIVE_TYPE,
				PortugueseInterrogativeType.HOW_PREDICATE);
		test.setFeature(Feature.TENSE, Tense.PRESENT);

		String result = realiser.realiseSentence(test);
		Assert.assertEquals("Como você está?", result);
	}
	
	/**
	 * Case 1 checks that "What do you think about John?" can be generated.
	 * 
	 * Case 2 checks that the same clause is generated, even when an object is
	 * declared.
	 */
	@Test
	public void testWhatObjectInterrogative() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);

		// Case 1, no object is explicitly given:
		AbstractSPhraseSpec clause = nlgFactory.createClause("você", "achar");
		PPPhraseSpec aboutJohn = nlgFactory.createPrepositionPhrase("sobre", "John");
		clause.addPostModifier(aboutJohn);
		clause.setFeature(Feature.INTERROGATIVE_TYPE,
				PortugueseInterrogativeType.WHAT_OBJECT);
		String realisation = realiser.realiseSentence(clause);
		System.out.println(realisation);
		Assert.assertEquals("Que você acha sobre John?", realisation);
		
		// Case 2:
		// Add "bad things" as the object so the object doesn't remain null:
		clause.setObject("coisas ruins");
		realisation = realiser.realiseSentence(clause);
		Assert.assertEquals("Que você acha sobre John?", realisation);
	}


	/**
	 * Test WHERE, HOW and WHY questions, with copular predicate "be"
	 */
	@Test
	public void testSimpleBeWHQuestions() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause("eu", "estar");
		
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHERE);		
		Assert.assertEquals("Onde eu estou?", realiser.realiseSentence(p));
		
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHY);		
		Assert.assertEquals("Por que eu estou?", realiser.realiseSentence(p));
		
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.HOW);		
		Assert.assertEquals("Como eu estou?", realiser.realiseSentence(p));

	}
	
}
