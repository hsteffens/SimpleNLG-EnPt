package simplenlg.syntax.portuguese;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.features.portuguese.PortugueseInterrogativeType;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.AdjPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class TutorialTest {

	// no code in sections 1 and 2
	
	/**
	 * test section 3 code
	 */
	@Test
	public void section3_Test() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser r = new Realiser(lexicon);

		NLGElement s1 = nlgFactory.createSentence("meu cachorro está feliz");
		
		String output = r.realiseSentence(s1);
		
		assertEquals("Meu cachorro está feliz.", output);
	 }
	
	/**
	 * test section 5 code
	 */
	@Test
	public void section5_Test() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser r = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject("meu cachorro");
		p.setVerb("perseguir");
		p.setObject("George");
		
		String output = r.realiseSentence(p);
		assertEquals("Meu cachorro persegue George.", output);
	 }
	
	/**
	 * test section 6 code
	 */
	@Test
	public void section6_Test() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject("Mary");
		p.setVerb("perseguir");
		p.setObject("George");
		
		p.setFeature(Feature.TENSE, Tense.PAST); 
		String output = realiser.realiseSentence(p);
		assertEquals("Mary perseguiu George.", output);

		p.setFeature(Feature.TENSE, Tense.FUTURE); 
		output = realiser.realiseSentence(p);
		assertEquals("Mary perseguirá George.", output);

		p.setFeature(Feature.NEGATED, true); 
		output = realiser.realiseSentence(p);
		assertEquals("Mary não perseguirá George.", output);

		p = nlgFactory.createClause();
		p.setSubject("Mary");
		p.setVerb("perseguir");
		p.setObject("George");
 
		p.setFeature(Feature.INTERROGATIVE_TYPE,
				PortugueseInterrogativeType.YES_NO);
		output = realiser.realiseSentence(p);
		assertEquals("Mary persegue George?", output);

		p.setSubject("Mary");
		p.setVerb("perseguir");
		p.setFeature(Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_OBJECT);
		output = realiser.realiseSentence(p);
		assertEquals("Quem Mary persegue?", output);

		p = nlgFactory.createClause();
		p.setSubject("o cão");
		p.setVerb("acordar");
		output = realiser.realiseSentence(p);
		assertEquals("O cão acorda.", output);

	 }
	
	/**
	 * test ability to use variant words
	 */
	@Test
	public void variantsTest() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject("meu cão");
		p.setVerb("ser");  // variant of be
		p.setObject("George");
		
		String output = realiser.realiseSentence(p);
		assertEquals("Meu cão é George.", output);
		
		p = nlgFactory.createClause();
		p.setSubject("meu cão");
		p.setVerb("perseguir");  // variant of chase
		p.setObject("George");
		
		output = realiser.realiseSentence(p);
		assertEquals("Meu cão persegue George.", output);
		

		NPPhraseSpec cao = nlgFactory.createNounPhrase("o", "cão");
		cao.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		
		AdjPhraseSpec feliz = nlgFactory.createAdjectivePhrase("feliz");
		
		p = nlgFactory.createClause();
		p.setSubject(cao);   // variant of "dog"
		p.setVerb("estar");  // variant of be
		p.setObject(feliz);  // variant of happy
		assertEquals("Os cães estão felizes.", realiser.realiseSentence(p));
		
		NPPhraseSpec criança = nlgFactory.createNounPhrase("o", "criança");
		criança.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		
		p = nlgFactory.createClause();
		p.setSubject(criança);   // variant of "child"
		p.setVerb("estar");  // variant of be
		p.setObject(feliz);  // variant of happy
		output = realiser.realiseSentence(p);
		assertEquals("As crianças estão felizes.", output);

		cao = nlgFactory.createNounPhrase("o", "cão");
		feliz = nlgFactory.createAdjectivePhrase("feliz");
		
		// following functionality is enabled
		p = nlgFactory.createClause();
		p.setSubject(cao);   // variant of "dog"
		p.setVerb("estar");  // variant of be
		p.setObject(feliz);  // variant of happy
		output = realiser.realiseSentence(p);
		assertEquals("O cão está feliz.", output); //corrected automatically
	}
		
	/**
	 * test section 5 to match simplenlg tutorial version 4's code
	 */
	@Test
	public void section5A_Test() { 
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);

		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject( "Mary" );
		p.setVerb( "perseguir" );
		p.setObject( "o macaco" );

		String output = realiser.realiseSentence( p );
		assertEquals( "Mary persegue o macaco.", output );
	} 

	/**
	 * test section 6 to match simplenlg tutorial version 4' code
	 */
	@Test
	public void section6A_Test() { 
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);

	
		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject( "Mary" );
		p.setVerb( "perseguir" );
		p.setObject( "o macaco" );
	
		p.setFeature( Feature.TENSE, Tense.PAST ); 
		String output = realiser.realiseSentence( p );
		assertEquals( "Mary perseguiu o macaco.", output );

		p.setFeature( Feature.TENSE, Tense.FUTURE ); 
		output = realiser.realiseSentence( p );
		assertEquals( "Mary perseguirá o macaco.", output );

		p.setFeature( Feature.NEGATED, true ); 
		output = realiser.realiseSentence( p );
		assertEquals( "Mary não perseguirá o macaco.", output );

		p = nlgFactory.createClause();
		p.setSubject( "Mary" );
		p.setVerb( "perseguir" );
		p.setObject( "o macaco" );

		p.setFeature( Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.YES_NO );
		output = realiser.realiseSentence( p );
		assertEquals( "Mary persegue o macaco?", output );

		p.setSubject( "Mary" );
		p.setVerb( "perseguir" );
		p.setFeature( Feature.INTERROGATIVE_TYPE, PortugueseInterrogativeType.WHO_OBJECT );
		output = realiser.realiseSentence( p );
		assertEquals( "Quem Mary persegue?", output );
	} 
	
	/**
	 * test section 7 code
	 */
	@Test
	public void section7_Test() { 
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject( "Mary" );
		p.setVerb( "perseguir" );
		p.setObject( "o macaco" );
		p.addComplement( "muito rapidamente" );
		p.addComplement( "apesar de sua exaustão" );
		
		String output = realiser.realiseSentence( p );
		assertEquals( "Mary persegue o macaco muito rapidamente apesar de sua exaustão.", output );
	 }
	
	/**
	 * test section 8 code
	 */
	@Test
	public void section8_Test() { 
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec subject = nlgFactory.createNounPhrase( "Mary" );
		NPPhraseSpec object = nlgFactory.createNounPhrase( "o macaco" );
		VPPhraseSpec verb = nlgFactory.createVerbPhrase( "perseguir" );
		subject.addModifier( "rápidamente" );
		
		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject( subject );
		p.setVerb( verb );
		p.setObject( object );
		
		String outputA = realiser.realiseSentence( p );
		assertEquals( "Mary rápidamente persegue o macaco.", outputA );
		
		verb = nlgFactory.createVerbPhrase( "conseguir" );
		verb.addComplement(nlgFactory.createVerbPhrase( "perseguir" ));
		p.setVerb(verb);
	 } 
	
	// there is no code specified in section 9
	
	/**
	 * test section 10 code
	 */
	@Test
	public void section10_Test() { 
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		NPPhraseSpec subject1 = nlgFactory.createNounPhrase( "Mary" );
		NPPhraseSpec subject2 = nlgFactory.createNounPhrase( "sua", "girafa" );
		subject2.addPreModifier(nlgFactory.createInflectedWord("o", LexicalCategory.DETERMINER));
		
		// next line is not correct ~ should be nlgFactory.createCoordinatedPhrase ~ may be corrected in the API
		AbstractCoordinatedPhraseElement subj = nlgFactory.createCoordinatedPhrase( subject1, subject2 );
		
		VPPhraseSpec verb = nlgFactory.createVerbPhrase( "perseguir" );

		AbstractSPhraseSpec p = nlgFactory.createClause();
		p.setSubject( subj );
		p.setVerb( verb );
		p.setObject( "o macaco" );
		
		String outputA = realiser.realiseSentence( p );
		assertEquals( "Mary e a girafa dela perseguem o macaco.", outputA );
		
		NPPhraseSpec object1 = nlgFactory.createNounPhrase( "o macaco" );
		NPPhraseSpec object2 = nlgFactory.createNounPhrase( "George" );
		
		// next line is not correct ~ should be nlgFactory.createCoordinatedPhrase ~ may be corrected in the API
		AbstractCoordinatedPhraseElement obj = nlgFactory.createCoordinatedPhrase( object1, object2 );
		obj.addCoordinate( "Martha" );
		p.setObject( obj );
		
		String outputB = realiser.realiseSentence( p );
		assertEquals( "Mary e a girafa dela perseguem o macaco, George e Martha.", outputB );

		obj.setFeature( Feature.CONJUNCTION, "ou" );
		
		String outputC = realiser.realiseSentence( p );
		assertEquals( "Mary e a girafa dela perseguem o macaco, George ou Martha.", outputC );
	}
	
	/**
	 * test section 11 code
	 */
	@Test
	public void section11_Test() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
		
		AbstractSPhraseSpec pA = nlgFactory.createClause( "Mary", "perseguir", "o macaco" );
		pA.addComplement( "no parque" );
		
		String outputA = realiser.realiseSentence( pA );		
		assertEquals( "Mary persegue o macaco no parque.", outputA );
		
		// alternative build paradigm
		NPPhraseSpec place = nlgFactory.createNounPhrase( "parque" );
		AbstractSPhraseSpec pB = nlgFactory.createClause( "Mary", "perseguir", "o macaco" );
		
		// next line is depreciated ~ may be corrected in the API
		PPPhraseSpec pp = nlgFactory.createPrepositionPhrase();
		pp.addComplement( place );
		pp.setPreposition( "no" );
		
		pB.addComplement( pp );
		
		String outputB = realiser.realiseSentence( pB );		
		assertEquals( "Mary persegue o macaco no parque.", outputB );
		
		place.addPreModifier( "frondoso" );
		
		String outputC = realiser.realiseSentence( pB );		
		assertEquals( "Mary persegue o macaco no frondoso parque.", outputC );
	 } // testSection11

	// section12 only has a code table as illustration
	
	/**
	 * test section 13 code
	 */
	@Test
	public void section13_Test() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
	
		NPPhraseSpec peixe = nlgFactory.createNounPhrase("de", "peixe");
		AbstractSPhraseSpec s1 = nlgFactory.createClause("meu gato",   "gostar", peixe);
		
		NPPhraseSpec ossos = nlgFactory.createNounPhrase("de", "ossos grandes");
		AbstractSPhraseSpec s2 = nlgFactory.createClause("meu cachorro",  "gostar",  ossos);
		
		NPPhraseSpec grama = nlgFactory.createNounPhrase("de", "grama");
		AbstractSPhraseSpec s3 = nlgFactory.createClause("meu cavalo", "gostar", grama);		
		
		AbstractCoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
		c.addCoordinate( s1 );
		c.addCoordinate( s2 ); 
		c.addCoordinate( s3 );
		
		String outputA = realiser.realiseSentence( c );
		assertEquals( "Meu gato gosta de peixe, meu cachorro gosta de ossos grandes e meu cavalo gosta de grama.", outputA );
		
		AbstractSPhraseSpec p = nlgFactory.createClause( "eu", "estar",  "feliz" );
		AbstractSPhraseSpec q = nlgFactory.createClause( "eu", "comer", "peixe" );
		q.setFeature( Feature.COMPLEMENTISER, "porque" );
		q.setFeature( Feature.TENSE, Tense.PAST );
		p.addComplement( q );
		
		String outputB = realiser.realiseSentence( p );
		assertEquals( "Eu estou feliz porque eu comi peixe.", outputB );
	} 
	
	/**
	 * test section 14 code
	 */
	@Test
	public void section14_Test() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);
	
		AbstractSPhraseSpec p1 = nlgFactory.createClause("Mary", "perseguir", "o macaco");
		AbstractSPhraseSpec p2 = nlgFactory.createClause("O macaco", "lutar" );
		AbstractSPhraseSpec p3 = nlgFactory.createClause("Mary", "estar", "nervosa" );
		
		DocumentElement s1 = nlgFactory.createSentence( p1 );
		DocumentElement s2 = nlgFactory.createSentence( p2 );
		DocumentElement s3 = nlgFactory.createSentence( p3 );
		
		DocumentElement par1 = nlgFactory.createParagraph( Arrays.asList( s1, s2, s3 ) );
		
		String output14a = realiser.realise( par1 ).getRealisation();
		assertEquals( "Mary persegue o macaco. O macaco luta. Mary está nervosa.\n\n", output14a );
 
		DocumentElement section = nlgFactory.createSection( "A provação e aflição de Mary e do Macaco" );
        section.addComponent( par1 );
        String output14b = realiser.realise( section ).getRealisation();
        assertEquals( "A provação e aflição de Mary e do Macaco\nMary persegue o macaco. O macaco luta. Mary está nervosa.\n\n", output14b );
	}

} 
