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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import simplenlg.aggregation.BackwardConjunctionReductionRule;
import simplenlg.aggregation.ClauseCoordinationRule;
import simplenlg.aggregation.ForwardConjunctionReductionRule;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.realiser.english.Realiser;

/**
 * Some tests for aggregation.
 * 
 * @author Albert Gatt, University of Malta & University of Aberdeen
 * 
 */
public class ClauseAggregationTest {
	

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see simplenlg.test.SimplenlgTest#setUp()
//	 */
//	@Override
//	@Before
//	protected void setUp() {
//		super.setUp();
//
//		// the woman kissed the man behind the curtain
//		this.s1 = this.phraseFactory.createClause();
//		this.s1.setSubject(this.woman);
//		this.s1.setVerbPhrase(this.phraseFactory.createVerbPhrase("kiss"));
//		this.s1.setObject(this.man);
//		this.s1.addPostModifier(this.phraseFactory
//				.createPrepositionPhrase("behind", this.phraseFactory
//						.createNounPhrase("the", "curtain")));
//
//		// the woman kicked the dog on the rock
//		this.s2 = this.phraseFactory.createClause();
//		this.s2.setSubject(this.phraseFactory.createNounPhrase("the", "woman")); //$NON-NLS-1$
//		this.s2.setVerbPhrase(this.phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
//		this.s2.setObject(this.phraseFactory.createNounPhrase("the", "dog"));
//		this.s2.addPostModifier(this.onTheRock);
//		
//		// the woman kicked the dog behind the curtain
//		this.s3 = this.phraseFactory.createClause();
//		this.s3.setSubject(this.phraseFactory.createNounPhrase("the", "woman")); //$NON-NLS-1$
//		this.s3.setVerbPhrase(this.phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
//		this.s3.setObject(this.phraseFactory.createNounPhrase("the", "dog"));
//		this.s3.addPostModifier(this.phraseFactory
//				.createPrepositionPhrase("behind", this.phraseFactory
//						.createNounPhrase("the", "curtain")));
//
//		// the man kicked the dog behind the curtain
//		this.s4 = this.phraseFactory.createClause();
//		this.s4.setSubject(this.man); //$NON-NLS-1$
//		this.s4.setVerbPhrase(this.phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
//		this.s4.setObject(this.phraseFactory.createNounPhrase("the", "dog"));
//		this.s4.addPostModifier(this.behindTheCurtain);
//
//		// the girl kicked the dog behind the curtain
//		this.s5 = this.phraseFactory.createClause();
//		this.s5.setSubject(this.phraseFactory.createNounPhrase("the", "girl")); //$NON-NLS-1$
//		this.s5.setVerbPhrase(this.phraseFactory.createVerbPhrase("kick")); //$NON-NLS-1$
//		this.s5.setObject(this.phraseFactory.createNounPhrase("the", "dog"));
//		this.s5.addPostModifier(this.behindTheCurtain);
//
//		// the woman kissed the dog behind the curtain
//		this.s6 = this.phraseFactory.createClause();
//		this.s6.setSubject(this.phraseFactory.createNounPhrase("the", "woman")); //$NON-NLS-1$
//		this.s6.setVerbPhrase(this.phraseFactory.createVerbPhrase("kiss")); //$NON-NLS-1$
//		this.s6.setObject(this.phraseFactory.createNounPhrase("the", "dog"));
//		this.s6.addPostModifier(this.phraseFactory
//				.createPrepositionPhrase("behind", this.phraseFactory
//						.createNounPhrase("the", "curtain")));
//	}
//	

	/**
	 * Test clause coordination with 2 sentences with same VP: succeeds
	 */
	@Test
	public void testCoordinationSameVP() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		ClauseCoordinationRule coord = new ClauseCoordinationRule();
		coord.setFactory(nlgFactory);
		
		Realiser realizer = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		clause.setSubject(nlgFactory.createNounPhrase("a", "mulher")); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("chutar")); //$NON-NLS-1$
		clause.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause.addPostModifier(nlgFactory.createPrepositionPhrase("atrás", nlgFactory.createNounPhrase("da", "cortina")));

		// the man kicked the dog behind the curtain
		AbstractSPhraseSpec clause2 = nlgFactory.createClause();
		clause2.setSubject(nlgFactory.createNounPhrase("o", "homem")); //$NON-NLS-1$
		clause2.setVerbPhrase(nlgFactory.createVerbPhrase("chutar")); //$NON-NLS-1$
		clause2.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause2.addPostModifier(nlgFactory.createPrepositionPhrase("atrás", nlgFactory.createNounPhrase("da", "cortina")));
		
		
		List<NLGElement> elements = Arrays.asList((NLGElement) clause, (NLGElement) clause2);
		List<NLGElement> result = coord.apply(elements);
		Assert.assertTrue(result.size() == 1); // should only be one sentence
		NLGElement aggregated = result.get(0);
		Assert.assertEquals(
				"a mulher e o homem chutam o cachorro atrás da cortina", //$NON-NLS-1$
				realizer.realise(aggregated).getRealisation());
	}

	/**
	 * Coordination of sentences with front modifiers: should preserve the mods
	 */
	@Test
	public void testCoordinationWithModifiers() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		ClauseCoordinationRule coord = new ClauseCoordinationRule();
		coord.setFactory(nlgFactory);
		
		Realiser realizer = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		clause.setSubject(nlgFactory.createNounPhrase("a", "mulher")); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("chutar")); //$NON-NLS-1$
		clause.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause.addPostModifier(nlgFactory.createPrepositionPhrase("atrás", nlgFactory.createNounPhrase("da", "cortina")));
		clause.addFrontModifier(nlgFactory.createAdverbPhrase("no entanto"));

		// the man kicked the dog behind the curtain
		AbstractSPhraseSpec clause2 = nlgFactory.createClause();
		clause2.setSubject(nlgFactory.createNounPhrase("o", "homem")); //$NON-NLS-1$
		clause2.setVerbPhrase(nlgFactory.createVerbPhrase("chutar")); //$NON-NLS-1$
		clause2.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause2.addPostModifier(nlgFactory.createPrepositionPhrase("atrás", nlgFactory.createNounPhrase("da", "cortina")));
		clause2.addFrontModifier(nlgFactory.createAdverbPhrase("no entanto"));
		
		List<NLGElement> elements = Arrays.asList((NLGElement) clause, (NLGElement) clause2);
		List<NLGElement> result = coord.apply(elements);
		Assert.assertTrue(result.size() == 1); // should only be one sentence
		NLGElement aggregated = result.get(0);
		
		realizer.setCommaSepCuephrase(true);
		
		Assert.assertEquals(
				"no entanto, a mulher e o homem chutam o cachorro atrás da cortina", //$NON-NLS-1$
				realizer.realise(aggregated).getRealisation());
	}

	/**
	 * Test coordination of 3 sentences with the same VP
	 */
	@Test
	public void testCoordinationSameVP2() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		ClauseCoordinationRule coord = new ClauseCoordinationRule();
		coord.setFactory(nlgFactory);
		
		Realiser realizer = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		clause.setSubject(nlgFactory.createNounPhrase("a", "mulher")); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("chutar")); //$NON-NLS-1$
		clause.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause.addPostModifier(nlgFactory.createPrepositionPhrase("atrás", nlgFactory.createNounPhrase("da", "cortina")));

		// the man kicked the dog behind the curtain
		AbstractSPhraseSpec clause2 = nlgFactory.createClause();
		clause2.setSubject(nlgFactory.createNounPhrase("o", "homem")); //$NON-NLS-1$
		clause2.setVerbPhrase(nlgFactory.createVerbPhrase("chutar")); //$NON-NLS-1$
		clause2.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause2.addPostModifier(nlgFactory.createPrepositionPhrase("atrás", nlgFactory.createNounPhrase("da", "cortina")));

		// the girl kicked the dog behind the curtain
		AbstractSPhraseSpec clause3 = nlgFactory.createClause();
		clause3.setSubject(nlgFactory.createNounPhrase("a", "garota")); //$NON-NLS-1$
		clause3.setVerbPhrase(nlgFactory.createVerbPhrase("chutar")); //$NON-NLS-1$
		clause3.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause3.addPostModifier(nlgFactory.createPrepositionPhrase("atrás", nlgFactory.createNounPhrase("da", "cortina")));
		
		
		List<NLGElement> elements = Arrays.asList((NLGElement) clause, (NLGElement) clause2, (NLGElement) clause3);
		List<NLGElement> result = coord.apply(elements);
		Assert.assertTrue(result.size() == 1); // should only be one sentence
		NLGElement aggregated = result.get(0);
		Assert
				.assertEquals(
						"a mulher e o homem e a garota chutam o cachorro atrás da cortina", //$NON-NLS-1$
						realizer.realise(aggregated).getRealisation());
	}

	/**
	 * Forward conjunction reduction test
	 */
	@Test
	public void testForwardConjReduction() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		ForwardConjunctionReductionRule fcr = new ForwardConjunctionReductionRule();
		fcr.setFactory(nlgFactory);
		
		Realiser realizer = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		clause.setSubject(nlgFactory.createNounPhrase("a", "mulher")); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("chutar")); //$NON-NLS-1$
		clause.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause.addPostModifier(nlgFactory.createPrepositionPhrase("na", nlgFactory.createNounPhrase("pedra")));

		AbstractSPhraseSpec clause2 = nlgFactory.createClause();
		clause2.setSubject(nlgFactory.createNounPhrase("a", "mulher")); //$NON-NLS-1$
		clause2.setVerbPhrase(nlgFactory.createVerbPhrase("chutar")); //$NON-NLS-1$
		clause2.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause2.addPostModifier(nlgFactory.createPrepositionPhrase("atrás", nlgFactory.createNounPhrase("da", "cortina")));
		
		
		NLGElement aggregated = fcr.apply(clause, clause2);
		Assert
				.assertEquals(
						"a mulher chuta o cachorro na pedra e chuta o cachorro atrás da cortina", //$NON-NLS-1$
						realizer.realise(aggregated).getRealisation());
	}

	/**
	 * Backward conjunction reduction test
	 */
	@Test
	public void testBackwardConjunctionReduction() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		BackwardConjunctionReductionRule bcr = new BackwardConjunctionReductionRule();
		bcr.setFactory(nlgFactory);
		
		Realiser realizer = new Realiser(lexicon);
		
		AbstractSPhraseSpec clause = nlgFactory.createClause();
		clause.setSubject(nlgFactory.createNounPhrase("a", "mulher")); //$NON-NLS-1$
		clause.setVerbPhrase(nlgFactory.createVerbPhrase("chutar")); //$NON-NLS-1$
		clause.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause.addPostModifier(nlgFactory.createPrepositionPhrase("atrás", nlgFactory.createNounPhrase("da", "cortina")));
		
		AbstractSPhraseSpec clause2 = nlgFactory.createClause();
		clause2.setSubject(nlgFactory.createNounPhrase("a", "mulher")); //$NON-NLS-1$
		clause2.setVerbPhrase(nlgFactory.createVerbPhrase("beijar")); //$NON-NLS-1$
		clause2.setObject(nlgFactory.createNounPhrase("o", "cachorro"));
		clause2.addPostModifier(nlgFactory.createPrepositionPhrase("atrás", nlgFactory.createNounPhrase("da", "cortina")));
		
		NLGElement aggregated = bcr.apply(clause, clause2);
		Assert.assertEquals(
						"a mulher chuta e a mulher beija o cachorro atrás da cortina",
						realizer.realise(aggregated).getRealisation());
	}
	

}
