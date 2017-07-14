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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */

package simplenlg.syntax.portuguese;

import org.junit.Test;

import junit.framework.Assert;
import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Gender;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.portuguese.PortugueseCoordinatedPhraseElement;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.AdjPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class NounPhraseTest {
	
	@Test
	public void testPlural001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec np4 = nlgFactory.createNounPhrase("o", "rocha");
		np4.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals(
				"as rochas", realiser.realise(np4).getRealisation()); //$NON-NLS-1$
	}
	
	@Test
	public void testPlural002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec np5 = nlgFactory.createNounPhrase("o", "curtina");
		np5.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		Assert.assertEquals(
				"as curtinas", realiser.realise(np5).getRealisation()); //$NON-NLS-1$
	}
	
	@Test
	public void testPlural003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec np5 = nlgFactory.createNounPhrase("o", "curtina");
		np5.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
		Assert.assertEquals(
				"a curtina", realiser.realise(np5).getRealisation()); //$NON-NLS-1$
	}
	
	@Test
	public void testPronominalisation001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec singer = nlgFactory.createNounPhrase("o", "cantor");
		singer.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
		singer.setFeature(Feature.PRONOMINAL, true);
		Assert.assertEquals("ela", realiser.realise(singer).getRealisation());
	}
	
	@Test
	public void testPronominalisation002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec singer = nlgFactory.createNounPhrase("o", "cantor");
		singer.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
		singer.setFeature(Feature.PRONOMINAL, true);
		singer.setFeature(Feature.POSSESSIVE, true);
		Assert.assertEquals("dela", realiser.realise(singer).getRealisation());
	}
	
	@Test
	public void testPronominalisation003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec people = nlgFactory.createNounPhrase("algum", "pessoa");
		people.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		people.setFeature(Feature.PRONOMINAL, true);
		
		Assert.assertEquals("eles", realiser.realise(people).getRealisation());
	}
	
	@Test
	public void testPronominalisation004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec people = nlgFactory.createNounPhrase("algum", "pessoa");
		people.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		people.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
		people.setFeature(Feature.PRONOMINAL, true);
		people.setFeature(InternalFeature.DISCOURSE_FUNCTION, DiscourseFunction.OBJECT);
		
		Assert.assertEquals("lhes", realiser.realise(people).getRealisation());
	}
	
	@Test
	public void testPronominalisation005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("Mary");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.FIRST);
		AbstractSPhraseSpec sent = nlgFactory.createClause(pro, "gostar", nlgFactory.createNounPhrase("do ", "John"));
		Assert.assertEquals("Eu gosto do John.", realiser.realiseSentence(sent));
	}
	
	@Test
	public void testPronominalisation006() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("Mary");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.SECOND);
		AbstractSPhraseSpec sent = nlgFactory.createClause(pro, "gostar", nlgFactory.createNounPhrase("do ", "John"));
		Assert.assertEquals("Tu gostas do John.", realiser.realiseSentence(sent));
	}
	
	@Test
	public void testPronominalisation007() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("Mary");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.THIRD);
		pro.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
		AbstractSPhraseSpec sent = nlgFactory.createClause(pro, "gostar", nlgFactory.createNounPhrase("do ", "John"));
		Assert.assertEquals("Ela gosta do John.", realiser.realiseSentence(sent));
	}
	
	@Test
	public void testPronominalisation008() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("Mary");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.FIRST);
		pro.setPlural(true);
		AbstractSPhraseSpec sent = nlgFactory.createClause(pro, "gostar", nlgFactory.createNounPhrase("do ", "John"));
		Assert.assertEquals("Nós gostamos do John.", realiser.realiseSentence(sent));
	}
	
	@Test
	public void testPronominalisation009() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("Mary");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.SECOND);
		pro.setPlural(true);
		AbstractSPhraseSpec sent = nlgFactory.createClause(pro, "gostar", nlgFactory.createNounPhrase("do ", "John"));
		Assert.assertEquals("Vocês gostais do John.", realiser.realiseSentence(sent));
	}
	
	@Test
	public void testPronominalisation010() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("Mary");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.THIRD);
		pro.setPlural(true);
		pro.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
		AbstractSPhraseSpec sent = nlgFactory.createClause(pro, "gostar", nlgFactory.createNounPhrase("do ", "John"));
		Assert.assertEquals("Elas gostam do John.", realiser.realiseSentence(sent));
	}
	
	@Test
	public void testPronominalisation011() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("John");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.FIRST);
		AbstractSPhraseSpec sent = nlgFactory.createClause("Mary", "gostar", pro);
		Assert.assertEquals("Mary gosta me.", realiser.realiseSentence(sent));
	}
	
	@Test
	public void testPronominalisation012() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("John");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.SECOND);
		AbstractSPhraseSpec sent = nlgFactory.createClause("Mary", "gostar", pro);
		Assert.assertEquals("Mary gosta te.", realiser.realiseSentence(sent));
	}
	
	@Test
	public void testPronominalisation013() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("John");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.THIRD);
		pro.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
		AbstractSPhraseSpec sent = nlgFactory.createClause("Mary", "gostar", pro);
		Assert.assertEquals("Mary gosta lhe.", realiser.realiseSentence(sent));
	}

	@Test
	public void testPronominalisation014() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("John");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.FIRST);
		pro.setPlural(true);
		AbstractSPhraseSpec sent = nlgFactory.createClause("Mary", "gostar", pro);
		Assert.assertEquals("Mary gosta nos.", realiser.realiseSentence(sent));
	}
	
	@Test
	public void testPronominalisation015() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("John");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.SECOND);
		pro.setPlural(true);
		AbstractSPhraseSpec sent = nlgFactory.createClause("Mary", "gostar", pro);
		Assert.assertEquals("Mary gosta vos.", realiser.realiseSentence(sent));
	}

	@Test
	public void testPronominalisation016() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec pro = nlgFactory.createNounPhrase("John");
		pro.setFeature(Feature.PRONOMINAL, true);
		pro.setFeature(Feature.PERSON, Person.THIRD);
		pro.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
		pro.setPlural(true);
		AbstractSPhraseSpec sent = nlgFactory.createClause("Mary", "gostar", pro);
		Assert.assertEquals("Mary gosta lhes.", realiser.realiseSentence(sent));
	}
	
	@Test
	public void testPremodification001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
	
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		AdjPhraseSpec beatiful = nlgFactory.createAdjectivePhrase("lindo");
		man.addPreModifier(beatiful);
		
		Assert.assertEquals("o lindo homem", realiser.realise(man).getRealisation());
	}
	
	@Test
	public void testPremodification002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
	
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		AdjPhraseSpec beatiful = nlgFactory.createAdjectivePhrase("bonita");
		woman.addPreModifier(beatiful);
		
		Assert.assertEquals("a bonita mulher", realiser.realise(woman).getRealisation());
	}
	
	@Test
	public void testPremodification003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
	
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		AdjPhraseSpec stunning = nlgFactory.createAdjectivePhrase("impressionante");
		dog.addPreModifier(stunning);
		
		Assert.assertEquals("o impressionante cachorro", realiser.realise(dog).getRealisation());
	}

	@Test
	public void testPremodification004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
	
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		man.setPreModifier(nlgFactory.createWord("esperto", LexicalCategory.ADJECTIVE));
		
		Assert.assertEquals("o esperto homem", realiser.realise(man).getRealisation());
	}
	
	@Test
	public void testPostmodification001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		NPPhraseSpec rock = nlgFactory.createNounPhrase("na", "rocha");
		man.addPostModifier(rock);
		Assert.assertEquals("o homem na rocha", realiser.realise(man).getRealisation());
	}
	
	@Test
	public void testPostmodification002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		NPPhraseSpec behindTheCurtain = nlgFactory.createNounPhrase("atrás da", "cortina");
		woman.addPostModifier(behindTheCurtain);
		Assert.assertEquals("a mulher atrás da cortina", realiser.realise(woman).getRealisation());
	}
	
	@Test
	public void testPostmodification003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		man.setPostModifier(nlgFactory.createWord("esperto", LexicalCategory.ADJECTIVE));
		Assert.assertEquals("o homem esperto", realiser.realise(man).getRealisation());
	}
	
	@Test
	public void testComplementation001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec man = nlgFactory.createNounPhrase("o", "homem");
		man.setComplement(nlgFactory.createWord("esperto",  LexicalCategory.ADJECTIVE));
		Assert.assertEquals("o homem esperto", realiser.realise(man).getRealisation());
	}
	
	@Test
	public void testComplementation002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		NPPhraseSpec behindTheCurtain = nlgFactory.createNounPhrase("atrás da", "cortina");
		woman.addComplement(behindTheCurtain);
		
		Assert.assertEquals("a mulher atrás da cortina", realiser.realise(woman).getRealisation());
	}
	
	@Test
	public void testPossessive001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement possNP = nlgFactory.createNounPhrase("o", "homem"); //$NON-NLS-1$ //$NON-NLS-2$
		possNP.setFeature(Feature.POSSESSIVE, true);
		Assert.assertEquals("o seu homem", realiser.realise(possNP) //$NON-NLS-1$
				.getRealisation());
	}

	@Test
	public void testPossessive002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement man = nlgFactory.createNounPhrase("o", "homem");
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		
		PortugueseCoordinatedPhraseElement phrase = new PortugueseCoordinatedPhraseElement(man, dog);
		phrase.setFeature(Feature.POSSESSIVE, true);

		
		Assert.assertEquals("o homem e o seu cachorro", realiser.realise(phrase) //$NON-NLS-1$
				.getRealisation());
	}
	
	@Test
	public void testPossessive003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement possNP = nlgFactory.createNounPhrase("o", "homem");
		possNP.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
		possNP.setFeature(Feature.PRONOMINAL, true);
		possNP.setFeature(Feature.POSSESSIVE, true);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		dog.setFeature(InternalFeature.SPECIFIER, possNP);
		
		Assert.assertEquals("cachorro dele", realiser.realise(dog) //$NON-NLS-1$
				.getRealisation());
	}
	
	@Test
	public void testCoordination001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		
		AbstractCoordinatedPhraseElement cnp1 = new PortugueseCoordinatedPhraseElement(dog, woman);
		// simple coordination
		Assert.assertEquals("o cachorro e a mulher", realiser //$NON-NLS-1$
				.realise(cnp1).getRealisation());
	}
	
	@Test
	public void testCoordination002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		NPPhraseSpec behindTheCurtain = nlgFactory.createNounPhrase("atrás da", "cortina");
		
		AbstractCoordinatedPhraseElement cnp1 = new PortugueseCoordinatedPhraseElement(dog, woman);
		cnp1.addComplement(behindTheCurtain);
		// simple coordination
		Assert.assertEquals("o cachorro e a mulher atrás da cortina", realiser //$NON-NLS-1$
				.realise(cnp1).getRealisation());
	}
	
	@Test
	public void testCoordination003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		
		AbstractCoordinatedPhraseElement cnp1 = new PortugueseCoordinatedPhraseElement(dog, woman);
		cnp1.setFeature(Feature.RAISE_SPECIFIER, true);
		
		Assert.assertEquals("o cachorro e mulher", realiser //$NON-NLS-1$
				.realise(cnp1).getRealisation());
	}
	
	@Test
	public void testCoordination004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec rocha = nlgFactory.createNounPhrase("na", "rocha");
		dog.addComplement(rocha);
		
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		NPPhraseSpec behindTheCurtain = nlgFactory.createNounPhrase("atrás da", "cortina");
		woman.addComplement(behindTheCurtain);
		
		AbstractCoordinatedPhraseElement cnp1 = new PortugueseCoordinatedPhraseElement(dog, woman);
		
		Assert.assertEquals("o cachorro na rocha e a mulher atrás da cortina", realiser //$NON-NLS-1$
				.realise(cnp1).getRealisation());
	}
	
	@Test
	public void testCoordination005() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec rocha = nlgFactory.createNounPhrase("na", "rocha");
		dog.addComplement(rocha);
		
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		NPPhraseSpec behindTheCurtain = nlgFactory.createNounPhrase("atrás da", "cortina");
		woman.addComplement(behindTheCurtain);
		
		NPPhraseSpec room = nlgFactory.createNounPhrase("do", "quarto");
		AbstractCoordinatedPhraseElement cnp1 = new PortugueseCoordinatedPhraseElement(dog, woman);
		cnp1.addComplement(room);
		
		Assert.assertEquals("o cachorro na rocha e a mulher atrás da cortina do quarto", realiser //$NON-NLS-1$
				.realise(cnp1).getRealisation());
	}

	@Test
	public void testCoordination006() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec rocha = nlgFactory.createNounPhrase("na", "rocha");
		dog.addComplement(rocha);
		
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		NPPhraseSpec behindTheCurtain = nlgFactory.createNounPhrase("atrás da", "cortina");
		woman.addComplement(behindTheCurtain);
		
		NPPhraseSpec room = nlgFactory.createNounPhrase("do", "quarto");
		AbstractCoordinatedPhraseElement cnp1 = new PortugueseCoordinatedPhraseElement(dog, woman);
		cnp1.addComplement(room);
		
		NLGElement every = nlgFactory.createWord("todo", LexicalCategory.DETERMINER); //$NON-NLS-1$
		cnp1.setFeature(InternalFeature.SPECIFIER, every);
		
		Assert.assertEquals("todo cachorro na rocha e todo mulher atrás da cortina do quarto", realiser //$NON-NLS-1$
				.realise(cnp1).getRealisation());
	}
	
	@Test
	public void testCoordination007() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		dog.setFeature(Feature.PRONOMINAL, true);
		dog.setFeature(InternalFeature.SPECIFIER, nlgFactory.createWord("o", LexicalCategory.DETERMINER));
		NPPhraseSpec rocha = nlgFactory.createNounPhrase("na", "rocha");
		dog.addComplement(rocha);
		
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		NPPhraseSpec behindTheCurtain = nlgFactory.createNounPhrase("atrás da", "cortina");
		woman.addComplement(behindTheCurtain);
		
		NPPhraseSpec room = nlgFactory.createNounPhrase("do", "quarto");
		AbstractCoordinatedPhraseElement cnp1 = new PortugueseCoordinatedPhraseElement(dog, woman);
		cnp1.addComplement(room);
		
		NLGElement every = nlgFactory.createWord("toda", LexicalCategory.DETERMINER); //$NON-NLS-1$
		cnp1.setFeature(InternalFeature.SPECIFIER, every);
		
		Assert.assertEquals("você e toda mulher atrás da cortina do quarto", realiser //$NON-NLS-1$
				.realise(cnp1).getRealisation());
	}
	
	@Test
	public void testPossessiveCoordinate001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		
		AbstractCoordinatedPhraseElement cnp1 = new PortugueseCoordinatedPhraseElement(dog, woman);
		cnp1.setFeature(Feature.POSSESSIVE, true);
		
		Assert.assertEquals("o cachorro e a sua mulher", realiser.realise(cnp1).getRealisation());
	}
	
	@Test
	public void testPossessiveCoordinate002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec dog = nlgFactory.createNounPhrase("o", "cachorro");
		dog.setFeature(Feature.PRONOMINAL, true);
		dog.setFeature(Feature.POSSESSIVE, true);
		
		NPPhraseSpec woman = nlgFactory.createNounPhrase("o", "mulher");
		
		AbstractCoordinatedPhraseElement cnp1 = new PortugueseCoordinatedPhraseElement(dog, woman);
		cnp1.setFeature(Feature.POSSESSIVE, true);
		
		Assert.assertEquals("dele e a sua mulher", realiser.realise(cnp1).getRealisation());
	}
	
	@Test
	public void testUmUma001() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement _dog = nlgFactory.createNounPhrase("um", "cachorro"); //$NON-NLS-1$ //$NON-NLS-2$
		Assert.assertEquals("um cachorro", realiser.realise(_dog).getRealisation());
	}
	
	@Test
	public void testUmUma002() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement woman = nlgFactory.createNounPhrase("um", "mulher");
		Assert.assertEquals("uma mulher", realiser.realise(woman).getRealisation());
	}
	
	@Test
	public void testUmUma003() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement woman = nlgFactory.createNounPhrase("um", "elefante");
		woman.setPreModifier("enorme");
		Assert.assertEquals("um enorme elefante", realiser.realise(woman).getRealisation());
	}
	
	@Test
	public void testUmUma004() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		PhraseElement dog = nlgFactory.createNounPhrase("um", "cachorro");
		dog.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		dog.addPreModifier(nlgFactory.createAdjectivePhrase("enorme"));
		
		Assert.assertEquals("alguns enormes cachorros", realiser.realise(dog).getRealisation());
	}
	
	@Test
	public void testUmUmaCoord() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec _dog = nlgFactory.createNounPhrase("um", "cachorro");
		_dog.addPreModifier(nlgFactory.createCoordinatedPhrase("enorme", "preto"));
		String realisation = realiser.realise(_dog).getRealisation();
		Assert.assertEquals("um enorme e preto cachorro", realisation);
	}

}
