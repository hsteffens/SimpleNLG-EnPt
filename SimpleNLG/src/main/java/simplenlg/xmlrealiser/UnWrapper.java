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
package simplenlg.xmlrealiser;

import java.io.Reader;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import simplenlg.features.ClauseStatus;
import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.Inflection;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.features.english.EnglishInterrogativeType;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.DocumentCategory;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.ElementCategory;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseCategory;
import simplenlg.framework.PhraseElement;
import simplenlg.framework.WordElement;
import simplenlg.framework.english.EnglishCoordinatedPhraseElement;
import simplenlg.framework.english.EnglishNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AdjPhraseSpec;
import simplenlg.phrasespec.AdvPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.xmlrealiser.wrapper.XmlWordElement;

// TODO: Auto-generated Javadoc
/**
 * UnWrapper maps from classes generated by xjc from RealizerSchema.xsd to
 * SimpleNLG classes. There are classes of the same name in two packages. The
 * xjc wrapper classes are in the simplenlg.xmlrealiser.wrapper package, and are
 * prefixed with the package name. The real simplenlg classes are referenced
 * without package name prefix.
 * 
 * @author Christopher Howell Agfa Healthcare Corporation
 * @author Albert Gatt, University of Malta
 * 
 */
public class UnWrapper {

	/**
	 * Create wrapper objects from xml for a request to realise, or the xml for
	 * a recording. Both are elements of NLGSpec.
	 * 
	 * @param xmlReader
	 *            the xml reader
	 * @return the nLG spec
	 * @throws XMLRealiserException
	 *             the xML realiser exception
	 */
	public static simplenlg.xmlrealiser.wrapper.NLGSpec getNLGSpec(
			Reader xmlReader) throws XMLRealiserException {
		simplenlg.xmlrealiser.wrapper.NLGSpec wt = null;
		try {
			JAXBContext jc = JAXBContext
					.newInstance(simplenlg.xmlrealiser.wrapper.NLGSpec.class);
			Unmarshaller u = jc.createUnmarshaller();
			Object obj = u.unmarshal(xmlReader);

			if (obj instanceof simplenlg.xmlrealiser.wrapper.NLGSpec) {
				wt = (simplenlg.xmlrealiser.wrapper.NLGSpec) obj;
			}
		}

		catch (Throwable e) {
			throw new XMLRealiserException("XML unmarshal error", e);
		}

		return wt;
	}

	/** The factory. */
	NLGFactory factory = null;

	/**
	 * Initialise an UnWrapper with a lexicon.
	 * 
	 * @param lexicon
	 *            the lexicon to use
	 */
	public UnWrapper(Lexicon lexicon) {
		
		factory = new EnglishNLGFactory(lexicon);
	}

	/**
	 * Create simplenlg objects from wrapper objects.
	 * 
	 * @param wt
	 *            the wrapper DocumentElement object
	 * @return the document element
	 */
	public DocumentElement UnwrapDocumentElement(
			simplenlg.xmlrealiser.wrapper.XmlDocumentElement wt) {
		DocumentElement t = factory.createDocument();

		if (wt.getCat() != null) {
			t.setCategory(Enum.valueOf(DocumentCategory.class, wt.getCat()
					.toString()));
		}
		if (wt.getTitle() != null) {
			t.setTitle(wt.getTitle());
		}

		for (simplenlg.xmlrealiser.wrapper.XmlNLGElement wp : wt.getChild()) {
			NLGElement p = UnwrapNLGElement(wp);
			t.addComponent(p);
		}

		return t;
	}

	/**
	 * Unwrap a <code>simplenlg.xmlrealiser.wrapper.NLGElement</code> and map it
	 * to a <code>simplenlg.framework.NLGElement</code>
	 * 
	 * @param wps
	 *            The wrapper object
	 * @return the NLGElement
	 */
	public simplenlg.framework.NLGElement UnwrapNLGElement(
			simplenlg.xmlrealiser.wrapper.XmlNLGElement wps) {

		if (wps == null) {
			return null;
		}

		if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlDocumentElement) {
			return (NLGElement) UnwrapDocumentElement((simplenlg.xmlrealiser.wrapper.XmlDocumentElement) wps);
		}

		// Handle coordinate phrase specs first, which will cause recursion.
		NLGElement cp = UnwrapCoordinatePhraseSpec(wps);
		if (cp != null) {
			return cp;
		}

		// Literal text.
		if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlStringElement) {
			simplenlg.xmlrealiser.wrapper.XmlStringElement wp = (simplenlg.xmlrealiser.wrapper.XmlStringElement) wps;
			NLGElement p = factory.createStringElement(wp.getVal());
			return p;
		}

		// WordElements (delegate to UnwrapWordElement) -- useful to have
		// because it is called by unWrapPhraseComponents, and pre/post mods
		// might be WordElements
		if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlWordElement) {
			return UnwrapWordElement((simplenlg.xmlrealiser.wrapper.XmlWordElement) wps);
		}

		// Sentence
		else if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlSPhraseSpec) {
			simplenlg.xmlrealiser.wrapper.XmlSPhraseSpec wp = (simplenlg.xmlrealiser.wrapper.XmlSPhraseSpec) wps;
			AbstractSPhraseSpec sp = factory.createClause();
			NLGElement vp = null;

			ArrayList<NLGElement> subjects = new ArrayList<NLGElement>();
			for (simplenlg.xmlrealiser.wrapper.XmlNLGElement p : wp.getSubj()) {
				NLGElement p1 = UnwrapNLGElement(p);
				checkFunction(simplenlg.features.DiscourseFunction.SUBJECT, p1);
				subjects.add(p1);
			}

			if (subjects.size() > 0) {
				sp.setFeature(InternalFeature.SUBJECTS, subjects);
			}

			if (wp.getVp() != null) {
				vp = UnwrapNLGElement(wp.getVp());
				sp.setVerbPhrase(vp);
			}

			if (wp.getCuePhrase() != null) {
				NLGElement cue = UnwrapNLGElement(wp.getCuePhrase());
				cue.setFeature(InternalFeature.DISCOURSE_FUNCTION,
						DiscourseFunction.CUE_PHRASE);
				sp.setFeature(Feature.CUE_PHRASE, cue);
			}

			if (wp.getCOMPLEMENTISER() != null) {
				sp.setFeature(Feature.COMPLEMENTISER, wp.getCOMPLEMENTISER());
			}

			setSFeatures(wp, sp, vp);

			// Common phrase components.
			UnwrapPhraseComponents(sp, wps);

			return sp;
		}

		// Phrases
		else if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlPhraseElement) {
			simplenlg.xmlrealiser.wrapper.XmlPhraseElement we = (simplenlg.xmlrealiser.wrapper.XmlPhraseElement) wps;
			PhraseElement hp = null;
			XmlWordElement w = we.getHead();
			NLGElement head = UnwrapWordElement(w);

			// NLGElement head;
			// simplenlg.xmlrealiser.wrapper.XmlNLGElement w =
			// we.getHeadstring();

			// check whether we have a stringelement or wordelement as head
			// if(w == null) {
			// w = we.getHeadword();
			// head = UnwrapWordElement((XmlWordElement) w);
			//				
			// } else {
			// head = factory.createStringElement(((XmlStringElement)
			// w).getVal());
			// }

			// Noun Phrase
			if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlNPPhraseSpec) {
				simplenlg.xmlrealiser.wrapper.XmlNPPhraseSpec wp = (simplenlg.xmlrealiser.wrapper.XmlNPPhraseSpec) wps;

				NPPhraseSpec p = factory.createNounPhrase(head);
				hp = p;

				if (wp.getSpec() != null) {
					// p.setSpecifier(UnwrapWordElement(wp.getSpec()));
					simplenlg.xmlrealiser.wrapper.XmlNLGElement spec = wp
							.getSpec();

					if (spec instanceof simplenlg.xmlrealiser.wrapper.XmlWordElement) {
						WordElement specifier = (WordElement) UnwrapWordElement((simplenlg.xmlrealiser.wrapper.XmlWordElement) spec);

						if (specifier != null) {
							p.setSpecifier(specifier);
						}

					} else {
						p.setSpecifier(UnwrapNLGElement(spec));
					}
				}

				setNPFeatures(wp, p);
			}

			// Adjective Phrase
			else if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlAdjPhraseSpec) {
				simplenlg.xmlrealiser.wrapper.XmlAdjPhraseSpec wp = (simplenlg.xmlrealiser.wrapper.XmlAdjPhraseSpec) wps;
				AdjPhraseSpec p = factory.createAdjectivePhrase(head);
				hp = p;

				p.setFeature(Feature.IS_COMPARATIVE, wp.isISCOMPARATIVE());
				p.setFeature(Feature.IS_SUPERLATIVE, wp.isISSUPERLATIVE());
			}

			// Prepositional Phrase
			else if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlPPPhraseSpec) {
				PPPhraseSpec p = factory.createPrepositionPhrase(head);
				hp = p;
			}

			// Adverb Phrase
			else if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlAdvPhraseSpec) {
				simplenlg.xmlrealiser.wrapper.XmlAdvPhraseSpec wp = (simplenlg.xmlrealiser.wrapper.XmlAdvPhraseSpec) wps;
				AdvPhraseSpec p = factory.createAdverbPhrase();
				p.setHead(head);
				hp = p;
				p.setFeature(Feature.IS_COMPARATIVE, wp.isISCOMPARATIVE());
				p.setFeature(Feature.IS_SUPERLATIVE, wp.isISSUPERLATIVE());
			}

			// Verb Phrase
			else if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlVPPhraseSpec) {
				simplenlg.xmlrealiser.wrapper.XmlVPPhraseSpec wp = (simplenlg.xmlrealiser.wrapper.XmlVPPhraseSpec) wps;
				VPPhraseSpec p = factory.createVerbPhrase(head);
				hp = p;
				setVPFeatures(wp, p);
			}

			// Common phrase components.
			UnwrapPhraseComponents(hp, wps);

			// set the discourse function, if defined
			if (we.getDiscourseFunction() != null) {
				hp.setFeature(InternalFeature.DISCOURSE_FUNCTION, Enum.valueOf(
						simplenlg.features.DiscourseFunction.class, we
								.getDiscourseFunction().toString()));
			}

			// check the appositive feature
			Boolean appositive = we.isAppositive();
			if (appositive != null) {
				hp.setFeature(Feature.APPOSITIVE, appositive);
			}

			return hp;
		}

		return null;
	}

	/**
	 * Unwrap the common phrase components (premodifiers, postmodifiers etc)
	 * that any <code>simplenlg.xmlrealiser.wrapper.NLGElement</code> can have,
	 * and map it to a <code>simplenlg.framework.NLGElement</code>
	 * 
	 * @param hp
	 *            the <code>simplenlg.framework.NLGElement</code> which is
	 *            cuurrently being constructed
	 * @param wps
	 *            The wrapper object
	 */
	public void UnwrapPhraseComponents(PhraseElement hp,
			simplenlg.xmlrealiser.wrapper.XmlNLGElement wps) {

		if (hp != null && wps != null) {
			simplenlg.xmlrealiser.wrapper.XmlPhraseElement wp = (simplenlg.xmlrealiser.wrapper.XmlPhraseElement) wps;

			for (simplenlg.xmlrealiser.wrapper.XmlNLGElement p : wp
					.getFrontMod()) {
				NLGElement p1 = UnwrapNLGElement(p);
				checkFunction(
						simplenlg.features.DiscourseFunction.FRONT_MODIFIER, p1);

				if (p1 != null) {
					hp.addFrontModifier(p1);
				}
			}

			for (simplenlg.xmlrealiser.wrapper.XmlNLGElement p : wp.getPreMod()) {
				NLGElement p1 = UnwrapNLGElement(p);
				checkFunction(
						simplenlg.features.DiscourseFunction.PRE_MODIFIER, p1);

				if (p1 != null) {
					hp.addPreModifier(p1);
				}
			}

			for (simplenlg.xmlrealiser.wrapper.XmlNLGElement p : wp
					.getPostMod()) {
				NLGElement p1 = UnwrapNLGElement(p);
				checkFunction(
						simplenlg.features.DiscourseFunction.POST_MODIFIER, p1);

				if (p1 != null) {
					hp.addPostModifier(p1);
				}
			}

			for (simplenlg.xmlrealiser.wrapper.XmlNLGElement p : wp.getCompl()) {
				NLGElement p1 = UnwrapNLGElement(p);

				// NB: set function to object by default, unless user set
				checkFunction(simplenlg.features.DiscourseFunction.OBJECT, p1);

				if (p1 != null) {
					hp.addComplement(p1);
				}
			}
		}
	}

	/**
	 * Unwraps a coordinate phrase.
	 * 
	 * @param wps
	 *            the <code>simplenlg.xmlrealiser.wrapper.NLGElement</code>
	 *            representing the phrase
	 * @return a <code>simplenlg.framework.CoordinatedPhraseElement</code> or
	 *         <code>null</code> if the wrapper element is not a coordinate
	 *         phrase.
	 */
	public NLGElement UnwrapCoordinatePhraseSpec(
			simplenlg.xmlrealiser.wrapper.XmlNLGElement wps) {
		NLGElement ret = null;

		// CoordinatedPhraseElement
		if (wps instanceof simplenlg.xmlrealiser.wrapper.XmlCoordinatedPhraseElement) {
			simplenlg.xmlrealiser.wrapper.XmlCoordinatedPhraseElement wp = (simplenlg.xmlrealiser.wrapper.XmlCoordinatedPhraseElement) wps;
			AbstractCoordinatedPhraseElement cp = new EnglishCoordinatedPhraseElement();
			ElementCategory cat = UnwrapCategory(wp.getCat());
			
			if (cat != null && cat instanceof PhraseCategory) {
				cp.setCategory(cat);
			}
			if (wp.getConj() != null) {
				String s = wp.getConj();
				if (s != null) {
					cp.setConjunction(s);
				}
			}
			
			setCoordinatedPhraseFeatures(wp, cp);

			for (simplenlg.xmlrealiser.wrapper.XmlNLGElement p : wp.getCoord()) {
				NLGElement p1 = UnwrapNLGElement(p);
				if (p1 != null) {
					cp.addCoordinate(p1);
				}

			}
			ret = cp;
		}

		return ret;
	}

	/**
	 * Unwrap word element.
	 * 
	 * @param wordElement
	 *            the word element
	 * @return the nLG element
	 */
	private NLGElement UnwrapWordElement(
			simplenlg.xmlrealiser.wrapper.XmlWordElement wordElement) {
		NLGElement word = null;
		
		if (wordElement != null) {
			
			if (Boolean.TRUE.equals(wordElement.isCanned())) {
				word = this.factory.createStringElement(wordElement.getBase());

			} else {

				LexicalCategory lexCat = LexicalCategory.ANY;
				ElementCategory cat = UnwrapCategory(wordElement.getCat());

				if (cat != null && cat instanceof LexicalCategory) {
					lexCat = (LexicalCategory) cat;
				}

				// String baseForm = getBaseWord(wordElement);
				String baseForm = wordElement.getBase();

				if (baseForm != null) {
					word = factory.createWord(baseForm, lexCat);

					if (word instanceof InflectedWordElement
							&& ((InflectedWordElement) word).getBaseWord()
									.getBaseForm().isEmpty()) {
						word = null; // cch TESTING

					} else if (word instanceof WordElement) {
						WordElement we = (WordElement) word;

						// Inflection
						if (wordElement.getVar() != null) {
							Inflection defaultInflection = Enum.valueOf(
									Inflection.class, wordElement.getVar()
											.toString());
							we.setDefaultInflectionalVariant(defaultInflection);
						}

						// Spelling variant may have been given as base form in
						// xml.
						// If so, use that variant.
						if (!baseForm.matches(we.getBaseForm())) {
							we.setDefaultSpellingVariant(baseForm);
						}
					}
				}
			}
		}
		
		return word;
	}

	// String getBaseWord(simplenlg.xmlrealiser.wrapper.WordElement lex) {
	// // List<String> c = lex.getContent();
	// // if (c.isEmpty())
	// // return "";
	// // else
	// // return (String) c.get(0);
	// return lex.getBase();
	//
	// }

	/**
	 * Unwrap category.
	 * 
	 * @param cat
	 *            the xml category object
	 * @return the element category
	 */
	private ElementCategory UnwrapCategory(Object cat) {
		if (cat == null) {
			return null;
		}
		if (cat.getClass().equals(
				simplenlg.xmlrealiser.wrapper.XmlLexicalCategory.class)) {
			return Enum.valueOf(LexicalCategory.class, cat.toString());
		} else if (cat.getClass().equals(
				simplenlg.xmlrealiser.wrapper.XmlPhraseCategory.class)) {
			return Enum.valueOf(PhraseCategory.class, cat.toString());
		} else if (cat.getClass().equals(
				simplenlg.xmlrealiser.wrapper.XmlDocumentCategory.class)) {
			return Enum.valueOf(DocumentCategory.class, cat.toString());
		} else {
			return null;
		}
	}

	/**
	 * Sets coordinated phrase features.
	 * 
	 * @param wp
	 *            The xml CoordinatedPhraseElement object.
	 * @param p
	 *            the internal CoordinatedPhraseElement object to get the features.
	 */
	private void setCoordinatedPhraseFeatures(
			simplenlg.xmlrealiser.wrapper.XmlCoordinatedPhraseElement wp,
			simplenlg.framework.AbstractCoordinatedPhraseElement p) {
		
		if (wp.getPERSON() != null) {
			p.setFeature(Feature.PERSON, wp.getPERSON());
		}

		if (wp.getTENSE() != null) {
			p.setFeature(Feature.TENSE, Enum.valueOf(Tense.class, wp.getTENSE()
					.toString()));
		}

		if (wp.getMODAL() != null) {
			p.setFeature(Feature.MODAL, wp.getMODAL());
		}
		
		if (wp.getNUMBER() != null) {
			// map number feature from wrapper ~NumberAgr to actual NumberAgr
			String numString = wp.getNUMBER().toString();
			simplenlg.features.NumberAgreement simplenlgNum = simplenlg.features.NumberAgreement
					.valueOf(numString);
			// p.setFeature(Feature.NUMBER, wp.getNUMBER());
			p.setFeature(Feature.NUMBER, simplenlgNum);
		}

		if (wp.getPERSON() != null) {
			// map person feature from wrapper Person to actual Person
			String perString = wp.getPERSON().toString();
			simplenlg.features.Person simplenlgPers = simplenlg.features.Person
					.valueOf(perString);
			p.setFeature(Feature.PERSON, simplenlgPers);
		}

		// boolean features.
		p.setFeature(Feature.APPOSITIVE, wp.isAPPOSITIVE());
		p.setFeature(Feature.NEGATED, wp.isNEGATED());
		p.setFeature(Feature.POSSESSIVE, wp.isPOSSESSIVE());
		p.setFeature(Feature.PROGRESSIVE, wp.isPROGRESSIVE());
		p.setFeature(Feature.RAISE_SPECIFIER, wp.isRAISESPECIFIER());
		p.setFeature(Feature.SUPRESSED_COMPLEMENTISER, wp.isSUPRESSEDCOMPLEMENTISER());
	}
	/**
	 * Sets the np features.
	 * 
	 * @param wp
	 *            The xml Noun Phrase object.
	 * @param p
	 *            the NPPhraseSpec object to get the features.
	 */
	private void setNPFeatures(
			simplenlg.xmlrealiser.wrapper.XmlNPPhraseSpec wp,
			simplenlg.phrasespec.NPPhraseSpec p) {

		if (wp.getNUMBER() != null) {
			// map number feature from wrapper ~NumberAgr to actual NumberAgr
			String numString = wp.getNUMBER().toString();
			simplenlg.features.NumberAgreement simplenlgNum = simplenlg.features.NumberAgreement
					.valueOf(numString);
			// p.setFeature(Feature.NUMBER, wp.getNUMBER());
			p.setFeature(Feature.NUMBER, simplenlgNum);
		}

		if (wp.getPERSON() != null) {
			// map person feature from wrapper Person to actual Person
			String perString = wp.getPERSON().toString();
			simplenlg.features.Person simplenlgPers = simplenlg.features.Person
					.valueOf(perString);
			p.setFeature(Feature.PERSON, simplenlgPers);
		}

		if (wp.getGENDER() != null) {
			// map gender feature from wrapper Gender to actual Gender
			String genString = wp.getGENDER().toString();
			simplenlg.features.Gender simplenlgGen = simplenlg.features.Gender
					.valueOf(genString);
			p.setFeature(LexicalFeature.GENDER, simplenlgGen);
		}

		p.setFeature(Feature.ELIDED, wp.isELIDED());
		p.setFeature(Feature.POSSESSIVE, wp.isPOSSESSIVE());
		p.setFeature(Feature.PRONOMINAL, wp.isPRONOMINAL());

	}

	/**
	 * Sets the vp features.
	 * 
	 * @param wp
	 *            The xml Verb Phrase object.
	 * @param p
	 *            the internal VP object to get features from xml object.
	 */
	private void setVPFeatures(
			simplenlg.xmlrealiser.wrapper.XmlVPPhraseSpec wp,
			simplenlg.phrasespec.VPPhraseSpec p) {
		if (wp.getFORM() != null) {
			p.setFeature(Feature.FORM, Enum.valueOf(Form.class, wp.getFORM()
					.toString()));
		}

		if (wp.getPERSON() != null) {
			p.setFeature(Feature.PERSON, wp.getPERSON());
		}

		if (wp.getTENSE() != null) {
			p.setFeature(Feature.TENSE, Enum.valueOf(Tense.class, wp.getTENSE()
					.toString()));
		}

		if (wp.getMODAL() != null) {
			p.setFeature(Feature.MODAL, wp.getMODAL());
		}

		p.setFeature(Feature.AGGREGATE_AUXILIARY, wp.isAGGREGATEAUXILIARY());
		p.setFeature(Feature.NEGATED, wp.isNEGATED());
		p.setFeature(Feature.PASSIVE, wp.isPASSIVE());
		p.setFeature(Feature.PERFECT, wp.isPERFECT());
		p.setFeature(Feature.PROGRESSIVE, wp.isPROGRESSIVE());
		p.setFeature(Feature.SUPPRESS_GENITIVE_IN_GERUND, wp.isSUPPRESSGENITIVEINGERUND());
		p.setFeature(Feature.SUPRESSED_COMPLEMENTISER, wp.isSUPRESSEDCOMPLEMENTISER());
	}

	/**
	 * ~Set the features for a sentence. This method also checks whether any
	 * features have been set on the VP, in which case, they are set if they
	 * haven't been set on the S
	 * 
	 * @param wp
	 *            the xml SPhraseSpec object
	 * @param sp
	 *            the sentence.
	 * @param vp
	 *            the verb phrase.
	 */
	private void setSFeatures(simplenlg.xmlrealiser.wrapper.XmlSPhraseSpec wp,
			simplenlg.phrasespec.AbstractSPhraseSpec sp,
			simplenlg.framework.NLGElement vp) {

		if (wp.getCLAUSESTATUS() != null) {
			sp.setFeature(InternalFeature.CLAUSE_STATUS, Enum.valueOf(
					ClauseStatus.class, wp.getCLAUSESTATUS().toString()));
		}

		if (wp.getPERSON() != null) {
			sp.setFeature(Feature.PERSON, Enum.valueOf(Person.class, wp
					.getPERSON().toString()));
		}

		if (wp.getFORM() != null) {
			sp.setFeature(Feature.FORM, Enum.valueOf(Form.class, wp.getFORM()
					.toString()));
		}

		if (wp.getTENSE() != null) {
			sp.setFeature(Feature.TENSE, Enum.valueOf(Tense.class, wp
					.getTENSE().toString()));

		} else if (vp != null && vp.hasFeature(Feature.TENSE)) {
			sp.setFeature(Feature.TENSE, vp.getFeature(Feature.TENSE));
		}

		// modal -- set on S or inherited from VP
		if (wp.getMODAL() != null) {
			sp.setFeature(Feature.MODAL, wp.getMODAL());
		} else if (vp != null && vp.hasFeature(Feature.MODAL)) {
			sp.setFeature(Feature.MODAL, vp.getFeature(Feature.MODAL));
		}

		// interrogative
		if (wp.getINTERROGATIVETYPE() != null) {
			sp.setFeature(Feature.INTERROGATIVE_TYPE, Enum.valueOf(
					simplenlg.features.english.EnglishInterrogativeType.class, wp
							.getINTERROGATIVETYPE().toString()));
		} else if (vp != null && vp.hasFeature(Feature.INTERROGATIVE_TYPE)) {
			sp.setFeature(Feature.INTERROGATIVE_TYPE, vp
					.getFeature(Feature.INTERROGATIVE_TYPE));
		}
		
		// set on clauses.
		boolean sAggregateAuxiliary = wp.isAGGREGATEAUXILIARY() == null ? false : wp.isAGGREGATEAUXILIARY();
		boolean vAggregateAuxiliary = vp == null ? false : vp.getFeatureAsBoolean(Feature.AGGREGATE_AUXILIARY).booleanValue();
		sp.setFeature(Feature.AGGREGATE_AUXILIARY, sAggregateAuxiliary || vAggregateAuxiliary);

		// passive: can be set on S or VP
		boolean sPass = wp.isPASSIVE() == null ? false : wp.isPASSIVE();
		boolean vPass = vp == null ? false : vp.getFeatureAsBoolean(
				Feature.PASSIVE).booleanValue();
		sp.setFeature(Feature.PASSIVE, sPass || vPass);

		// progressive: can be set on S or VP
		boolean sProg = wp.isPROGRESSIVE() == null ? false : wp.isPROGRESSIVE();
		boolean vProg = vp == null ? false : vp.getFeatureAsBoolean(
				Feature.PROGRESSIVE).booleanValue();
		sp.setFeature(Feature.PROGRESSIVE, sProg || vProg);

		// perfect: can be set on S or VP
		boolean sPerf = wp.isPERFECT() == null ? false : wp.isPERFECT();
		boolean vPerf = vp == null ? false : vp.getFeatureAsBoolean(
				Feature.PERFECT).booleanValue();
		sp.setFeature(Feature.PERFECT, sPerf || vPerf);

		// negation: can be set on S or VP
		boolean sNeg = wp.isNEGATED() == null ? false : wp.isNEGATED();
		boolean vNeg = vp == null ? false : vp.getFeatureAsBoolean(
				Feature.NEGATED).booleanValue();
		sp.setFeature(Feature.NEGATED, sNeg || vNeg);
		
		// set on clauses.
		boolean ssgg = wp.isSUPPRESSGENITIVEINGERUND() == null ? false : wp.isSUPPRESSGENITIVEINGERUND();
		boolean vsgg = vp == null ? false : vp.getFeatureAsBoolean(Feature.SUPPRESS_GENITIVE_IN_GERUND).booleanValue();
		sp.setFeature(Feature.SUPPRESS_GENITIVE_IN_GERUND, ssgg || vsgg);
		
		// set on clauses.
		boolean ssc = wp.isSUPRESSEDCOMPLEMENTISER() == null ? false : wp.isSUPRESSEDCOMPLEMENTISER();
		boolean vsc = vp == null ? false : vp.getFeatureAsBoolean(Feature.SUPRESSED_COMPLEMENTISER).booleanValue();
		sp.setFeature(Feature.SUPRESSED_COMPLEMENTISER, ssc || vsc);

	}

	/**
	 * Utility method to set the discourse function for phrase components,
	 * unless set by user
	 * 
	 * @param function
	 *            the function
	 * @param phrase
	 *            the phrase
	 */
	private void checkFunction(simplenlg.features.DiscourseFunction function,
			simplenlg.framework.NLGElement phrase) {
		if (!phrase.hasFeature(InternalFeature.DISCOURSE_FUNCTION)) {
			phrase.setFeature(InternalFeature.DISCOURSE_FUNCTION, function);
		}
	}
}
