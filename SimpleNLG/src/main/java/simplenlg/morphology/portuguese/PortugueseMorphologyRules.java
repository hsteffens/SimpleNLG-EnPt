package simplenlg.morphology.portuguese;

import java.util.ArrayList;
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.Gender;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.StringElement;
import simplenlg.framework.WordElement;
import simplenlg.morphology.AbstractMorphologyRules;
import simplenlg.phrasespec.NPPhraseSpec;

/**
 * Classe contendo os processos  morfologicos em Português
 * 
 * @author Hélinton P. Steffens
 * @Jul 13, 2017
 */
public class PortugueseMorphologyRules extends AbstractMorphologyRules {

	/**
	 * A triple array of Pronouns organised by singular/plural,
	 * possessive/reflexive/subjective/objective and by gender/person.
	 */
	@SuppressWarnings("nls")
	private static final String[][][] PRONOUNS = {
													{
														{"eu", "tu", "ele", "ela", "você"},
														{"me", "te", "lhe", "lhe", "se"},
														{"me", "te", "se", "se", "se"},
														{"meu", "teu", "dele", "dela", "dele"},
														{"meu", "teu", "dele", "dela", "dele"}
													},
													{
														{"nós", "vocês", "eles", "elas", "eles"},
														{"nos", "vos", "lhes", "lhes", "lhes"},
														{"nos", "vossa", "vos", "se", "se"},
														{"nosso", "vosso", "deles", "delas", "deles"},
														{"nosso", "vosso", "deles", "deles", "deles"}
													}
												};

	private static final String[] WH_PRONOUNS = {"quem", "que", "qual", "onde", "por que", "como", "quanto"};
	
	private static final List<String> AUX_GERUNDIO = new ArrayList<String>(1);
	
	private static final List<String> NOT_AUX_PARTICIPIO = new ArrayList<String>(2);
	
	static{
		AUX_GERUNDIO.add("estar");
		NOT_AUX_PARTICIPIO.add("querer");
		NOT_AUX_PARTICIPIO.add("precisar");
		NOT_AUX_PARTICIPIO.add("necessitar");
	}

	@Override
	public String[][][] getPronouns() {
		return PRONOUNS;
	}

	@Override
	public String[] getWHPronouns() {
		return WH_PRONOUNS;
	}
	
	@Override
	public void initialise() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NLGElement realise(NLGElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NLGElement> realise(List<NLGElement> elements) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * This method performs the morphology for verbs.
	 *
	 * @param element
	 *            the <code>InflectedWordElement</code>.
	 * @param baseWord
	 *            the <code>WordElement</code> as created from the lexicon
	 *            entry.
	 * @return a <code>StringElement</code> representing the word after
	 *         inflection.
	 */
	@Override
	public NLGElement doVerbMorphology(InflectedWordElement element, WordElement baseWord) {

		String realised = null;
		Object numberValue = element.getFeature(Feature.NUMBER);
		Object personValue = element.getFeature(Feature.PERSON);
		Object tense = element.getFeature(Feature.TENSE);
		Tense tenseValue;

		// AG: change to avoid deprecated getTense
		// if tense value is Tense, cast it, else default to present
		if(tense instanceof Tense) {
			tenseValue = (Tense) tense;
		} else {
			tenseValue = Tense.PRESENT;
		}

		Object formValue = element.getFeature(Feature.FORM);
		Object patternValue = element.getFeature(LexicalFeature.DEFAULT_INFL);
		DiscourseFunction discourseFunction = (DiscourseFunction) element.getFeature(InternalFeature.DISCOURSE_FUNCTION);
		
		// base form from baseWord if it exists, otherwise from element
		String baseForm = getBaseForm(element, baseWord);

		if(Form.BARE_INFINITIVE.equals(formValue)) {
			realised = baseForm;
		} else if(Form.GERUND.equals(formValue)){
			realised = buildGerundVerb(element, baseWord, patternValue, baseForm);
		} else if(Form.PRESENT_PARTICIPLE.equals(formValue)) {
			realised = buildParticiplePresentVerb(element, baseWord, patternValue, baseForm);
		}else if(Form.PAST_PARTICIPLE.equals(formValue)){
			realised = buildParticiplePastVerb(element, baseWord, baseForm, numberValue, personValue, patternValue);
		} else if(Tense.PAST.equals(tenseValue)) {
			realised = buildPastVerb(element, baseWord, baseForm, numberValue, personValue, patternValue);
		} else if(tenseValue != null && Tense.PRESENT.equals(tenseValue)) {
			realised = buildPresentVerb(element, baseWord, baseForm, numberValue, personValue, discourseFunction);
		} else if (tenseValue == null || Tense.FUTURE.equals(tenseValue)) {
			realised = buildFutureVerb(element, baseWord, baseForm, numberValue, personValue, discourseFunction);
		} else {
			realised = defaultVerbFormat(numberValue, personValue, baseForm);
		}
		StringElement realisedElement = new StringElement(realised);
		realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
		                           element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
		return realisedElement;
	}
	
	@Override
	public String defaultVerbFormat(Object numberValue, Object personValue, String baseForm) {
		String realised;
		if("ser".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
			if(Person.FIRST.equals(personValue) && (NumberAgreement.SINGULAR.equals(numberValue)
			                                        || numberValue == null)) {
				realised = "sou"; //$NON-NLS-1$
			} else {
				realised = "é"; //$NON-NLS-1$
			}
		} else {
			realised = baseForm;
		}
		return realised;
	}
	
	/**
	 * # Flexão simples de número
	 *    	   O      >       S                  # tempo > tempos
			   A      >       S                  # turma > turmas
			   E      >       S                  # quantidade > quantidades
			   O      >       S                  # o > os
			   U      >       S                  # tatu > tatus
			   É      >       S                  # pontapé > pontapés
			   I      >       S                  # colibri > colibris
			   Á      >       S                  # gambá > gambás
			   Ó      >       S                  # nó > nós
			   Ê      >       S                  # turnê > turnês
			   Ú      >       S                  # baú > baús
			   Ô      >       S                  # robô > robôs
			   Ã      >       S                  # clã > clãs
			   N      >       S                  # nêutron > nêutrons
			   [^I] L >       -L,IS              # audível > audíveis
			   I L    >       -L,S               # fuzil > fuzis
			   R      >       ES                 # cadáver > cadáveres
			   Z      >       ES                 # verniz > vernizes
			   S      >       ES                 # país > países
			   M      >       -M,NS	             # colagem > colagens
			   ÃO     >       -ÃO,ÕES	         # feijão > feijões
	 * 
	 * 
	 * @param word
	 * @param feature
	 * @return
	 */
	@Override
	public String buildRegularPlural(NLGElement element, String word) {
		Object obj = element.getFeature(LexicalFeature.PLURAL);
		if (obj != null) {
			return obj.toString();
		}
		
		if (word.equals("estes") || word.equals("estas") || word.equals("aquelas") || word.equals("aqueles") || word.equals("um")) {
			return word;
		}
		
		if (word.endsWith("x")){
			return word;
		} else if (word.endsWith("il")){
			return word.substring(0, word.length() - 1) + "l";
		} else if (word.endsWith("l")){
			return word.substring(0, word.length() - 1) + "is";
		} else if (word.endsWith("r")){
			return word + "es";
		} else if (word.endsWith("z")){
			return word + "es";
		} else if (word.endsWith("s")){
			return word + "es";
		}  else if (word.endsWith("m")){
			return word.substring(0, word.length() - 1) + "ns";
		} else if (word.endsWith("ão")){
			return word.substring(0, word.length() - 2) + "ões";
		}else if (word.endsWith("o") || word.endsWith("a") || 
				word.endsWith("e") || word.endsWith("o") ||
				word.endsWith("u") || word.endsWith("é") ||
				word.endsWith("i") || word.endsWith("á") ||
				word.endsWith("ó") || word.endsWith("ê") ||
				word.endsWith("ú") || word.endsWith("ô") ||
				word.endsWith("ã") || word.endsWith("n")) {
			return word + "s";
		} else {
			try{
				Integer.parseInt(word);
				return word;
			}catch (Exception e) {}
		} 

		return word + "s";
	}
	
	@Override
	public String buildPastVerb(InflectedWordElement element, WordElement baseWord, String baseForm, Object numberValue,
			Object personValue, Object patternValue) {
		String realised = element.getFeatureAsString(LexicalFeature.PAST);

		if(realised == null && baseWord != null && !NumberAgreement.PLURAL.equals(numberValue)) {
			if (Person.FIRST.equals(personValue)) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PAST1S);
			}else if (realised == null && Person.SECOND.equals(personValue)) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PAST2S);
			}else if (realised == null && Person.THIRD.equals(personValue)) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PAST3S);
			}
		} else if(realised == null && baseWord != null && NumberAgreement.PLURAL.equals(numberValue)) {
			if (Person.FIRST.equals(personValue)) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PAST1P);
			}else if (realised == null && Person.SECOND.equals(personValue)) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PAST2P);
			}else if (realised == null && Person.THIRD.equals(personValue)) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PAST3P);
			}
		}

		if(realised == null) {
			realised = buildRegularPastVerb(baseForm, numberValue, personValue);
		}
		return realised;
	}
	
	/**
	 *  # Conjugações regulares no perfeito do indicativo
		#
		# 3. Pretérito perfeito do indicativo. Paradigma:
		#
		# cantei cantaste cantou cantamos cantastes cantaram
		# vendi  vendeste vendeu vendemos vendestes venderam
		# parti  partiste partiu partimos partistes partiram
		# pus    puseste  pôs    pusemos  pusestes  puseram
		#
		# cantamos, vendemos e partimos já constam no presente do indicativo
		#
		    A R         >       -AR,EI          # cantei
		    [AEI]R      >       -R,STE          # cantaste, vendeste, partiste
		    A R         >       -AR,OU          # cantou
		    [AEI]R      >       -R,MOS          # passeamos, conhecemos, ferimos
		    [AEI]R      >       -R,STES         # cantastes, vendestes, partistes
		    [AEI]R      >       -R,RAM          # cantaram, venderam, partiram
		    E R         >       -ER,I           # vendi
		    [EI]R       >       -R,U            # vendeu, partiu
		    I R         >       -IR,I           # parti
		    O R         >       -OR,US          # pus
		    O R         >       -OR,USESTE      # puseste
		    O R         >       -OR,ÔS          # pôs
		    O R         >       -OR,USEMOS      # pusemos
		    O R         >       -OR,USESTES     # pusestes
		    O R         >       -OR,USERAM      # puseram
	 * @param baseForm
	 * @param number
	 * @param person
	 * @return
	 */
	private String buildRegularPastVerb(String baseForm, Object number, Object person) {
		String morphology = null;
		if(baseForm != null) {
			if(baseForm.equalsIgnoreCase("ser")) { //$NON-NLS-1$
				if(!NumberAgreement.PLURAL.equals(number)) {
					if(Person.SECOND.equals(person)) {
						morphology = "foste"; //$NON-NLS-1$
					} else if(Person.THIRD.equals(person)) {
						morphology = "foi"; //$NON-NLS-1$
					}else {
						morphology = "fui";
					}
				}else {
					if(Person.SECOND.equals(person)) {
						morphology = "fostes"; //$NON-NLS-1$
					} else if(Person.THIRD.equals(person)) {
						morphology = "foram"; //$NON-NLS-1$
					}else {
						morphology = "fomos";
					}
				}

			} else if(!NumberAgreement.PLURAL.equals(number)) {
				if(Person.FIRST.equals(person)) {
					if (baseForm.endsWith("ar")){
						return baseForm.substring(0, baseForm.length() - 2) + "ei";
					} else if (baseForm.endsWith("er")) {
						return baseForm.substring(0, baseForm.length() - 2) + "i";
					} else if (baseForm.endsWith("ir")) {
						return baseForm.substring(0, baseForm.length() - 2) + "i";
					} else if (baseForm.endsWith("or")) {
						return baseForm.substring(0, baseForm.length() - 2) + "us";
					} 
				}  else if (Person.SECOND.equals(person)) {
					if (baseForm.endsWith("or")) {
						return baseForm.substring(0, baseForm.length() - 1) + "useste";
					} else if (baseForm.endsWith("r")) {
						return baseForm.substring(0, baseForm.length() - 1) + "ste";
					}
				}  else if (Person.THIRD.equals(person)) {
					if (baseForm.endsWith("ar") && Person.THIRD.equals(person)) {
						return baseForm.substring(0, baseForm.length() - 2) + "ou";
					} else if (baseForm.endsWith("or") && Person.THIRD.equals(person)) {
						return baseForm.substring(0, baseForm.length() - 2) + "ôs";
					} else if (baseForm.endsWith("r") && Person.THIRD.equals(person)) {
						return baseForm.substring(0, baseForm.length() - 1) + "u";
					}
				}
			}else {
				if(Person.FIRST.equals(person)) {
					if (baseForm.endsWith("or")) {
						return baseForm.substring(0, baseForm.length() - 1) + "usemos";
					}
					if (baseForm.endsWith("r")) {
						return baseForm.substring(0, baseForm.length() - 1) + "mos";
					}
				} else if (Person.SECOND.equals(person)){
					if (baseForm.endsWith("or")) {
						return baseForm.substring(0, baseForm.length() - 1) + "usestes";
					}
					if (baseForm.endsWith("r")) {
						return baseForm.substring(0, baseForm.length() - 1) + "stes";
					}
				}else if(Person.THIRD.equals(person)) {
					if (baseForm.endsWith("or")) {
						return baseForm.substring(0, baseForm.length() - 1) + "usestes";
					}
					if (baseForm.endsWith("r")) {
						return baseForm.substring(0, baseForm.length() - 1) + "ram";
					}
				}
			}
			
		}
		return morphology;
	}

	/**
	 *  # Conjugações regulares no presente do indicativo
		#
		# 1. Presente do indicativo. Paradigma:
		#
		# canto cantas canta cantamos cantais cantam
		# vendo vendes vende vendemos vendeis vendem
		# parto partes parte partimos partis  partem
		# ponho pões   põe   pomos    pondes  põem  
		#
		    A R         >       -AR,O           # canto
		    [AEI]R      >       -R,S            # cantas, vendes, partis
		    A R         >       -AR,A           # canta
		    [AEI]R      >       -R,MOS          # cantamos, vendemos, partimos
		    [AE]R       >       -R,IS           # cantais, vendeis
		    [AE]R       >       -R,M            # cantam, vendem
		    E R         >       -ER,O           # vendo
		    E R         >       -ER,E           # vende
		    I R         >       -IR,O           # parto
		    I R         >       -IR,ES          # partes
		    I R         >       -IR,E           # parte
		    I R         >       -IR,EM          # partem
		    O R         >       -R,NHO          # ponho
		    O R         >       -OR,ÕES         # pões
		    O R         >       -OR,ÕE          # põe
		    O R         >       -R,MOS          # pomos
		    O R         >       -R,NDES         # pondes
		    O R         >       -OR,ÕEM         # põem

	 */
	@Override
	public String buildPresentVerb(InflectedWordElement element, WordElement baseWord, String baseForm,
			Object number, Object person, DiscourseFunction discourseFunction) {
		
		String realised = null;
		
		if ((discourseFunction == null || (!discourseFunction.equals(DiscourseFunction.OBJECT) && !discourseFunction.equals(DiscourseFunction.COMPLEMENT)))) {
			if (number == null || NumberAgreement.SINGULAR.equals(number)) {
				realised = buildSingularPresentVerb(element, baseWord, baseForm, person, realised);
			} else if (number == null || NumberAgreement.PLURAL.equals(number)) {
				realised = buildPluralPresentVerb(element, baseWord, baseForm, person, realised);
			}
		}
		
		if (realised == null) {
			 realised = baseForm;
		}
		
		return realised;
	}

	private String buildPluralPresentVerb(InflectedWordElement element, WordElement baseWord, String baseForm, Object person, String realised) {
		if(person == null || Person.THIRD.equals(person)) {
			realised = element.getFeatureAsString(LexicalFeature.PRESENT3P);
			
			if (realised == null) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT3P);
			}
			
			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "são";
				} else if (baseForm.endsWith("or")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "õem";
				} else if (baseForm.endsWith("ir")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "em";
				} else if (baseForm.endsWith("r")) {
					realised = baseForm.substring(0, baseForm.length() - 1) + "m";
				} 
			}

		} else if(person == null || Person.SECOND.equals(person)) {
			realised = element.getFeatureAsString(LexicalFeature.PRESENT2P);
			
			if (realised == null) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT2P);
			}
			
			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "sois";
				} else if (baseForm.endsWith("or")) {
					realised = baseForm.substring(0, baseForm.length() - 1) + "ndes";
				} else if (baseForm.endsWith("r")) {
					realised = baseForm.substring(0, baseForm.length() - 1) + "is";
				} 
			}

		}  else if(person == null || Person.FIRST.equals(person)) {
			realised = element.getFeatureAsString(LexicalFeature.PRESENT1P);
			
			if (realised == null) {
				realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT1P);
			}
			
			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "somos";
				} else if (baseForm.endsWith("or")) {
					realised = baseForm.substring(0, baseForm.length() - 1) + "mos";
				} else if (baseForm.endsWith("r")) {
					realised = baseForm.substring(0, baseForm.length() - 1) + "mos";
				}
			}

		}
		return realised;
	}

	private String buildSingularPresentVerb(InflectedWordElement element, WordElement baseWord, String baseForm,
			Object person, String realised) {
		if(person == null || Person.THIRD.equals(person)) {
			realised = element.getFeatureAsString(LexicalFeature.PRESENT3S);

			if(realised == null && baseWord != null && !"ser".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT3S);
			}
			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "é";
				} else if (baseForm.endsWith("ar")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "a";
				} else if (baseForm.endsWith("er")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "e";
				} else if (baseForm.endsWith("ir")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "e";
				} else if (baseForm.endsWith("or")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "õe";
				}
			}

		} else if(person == null || Person.SECOND.equals(person)) {
			realised = element.getFeatureAsString(LexicalFeature.PRESENT2S);

			if(realised == null && baseWord != null && !"ser".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT2S);
			}
			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "és";
				} else if (baseForm.endsWith("ir")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "es";
				} else if (baseForm.endsWith("or")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "ões";
				} else if (baseForm.endsWith("r")) {
					realised = baseForm.substring(0, baseForm.length() - 1) + "s";
				}
			}

		}  else if(person == null || Person.FIRST.equals(person)) {
			realised = element.getFeatureAsString(LexicalFeature.PRESENT1S);

			if(realised == null && baseWord != null && !"ser".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT1S);
			}

			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "sou";
				} else if (baseForm.endsWith("ar")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "o";
				} else if (baseForm.endsWith("er")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "o";
				} else if (baseForm.endsWith("ir")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "o";
				} else if (baseForm.endsWith("or")) {
					realised = baseForm.substring(0, baseForm.length() - 2) + "nho";
				}
			}

		}
		return realised;
	}

	/**
	 *
		# 5. Futuro do presente do indicativo. Paradigma:
		#
		# cantarei cantarás cantará cantaremos cantareis cantarão
		# venderei venderás venderá venderemos vendereis venderão
		# partirei partirás partirá partiremos partireis partirão
		# porei    porás    porá    poremos    poreis    porão
		#
		    [AEIO]R     >       EI              # cantarei, venderei, partirei, porei
		    [AEIO]R     >       ÁS              # cantarás, etc
		    [AEIO]R     >       Á               # cantará, etc
		    [AEIO]R     >       EMOS            # cantaremos, etc
		    [AEIO]R     >       EIS             # cantareis, etc
		    [AEIO]R     >       ÃO              # cantarão, etc
	 */
	@Override
	public String buildFutureVerb(InflectedWordElement element, WordElement baseWord, String baseForm,
			Object numberValue, Object personValue, DiscourseFunction discourseFunction) {
		String realised = null;
		
		if ((discourseFunction == null || (!discourseFunction.equals(DiscourseFunction.OBJECT) && !discourseFunction.equals(DiscourseFunction.COMPLEMENT)))) {
			if (numberValue == null || NumberAgreement.SINGULAR.equals(numberValue)) {
				realised = buildSingularFutureVeb(element, baseWord, baseForm, numberValue, personValue, realised);
			} else if (numberValue == null || NumberAgreement.PLURAL.equals(numberValue)) {
				realised = buildPluralFutureVeb(element, baseForm, numberValue, personValue, realised);
			}
		}
		
		if (realised == null) {
			 realised = baseForm;
		}
		
		
		return realised;
	}

	private String buildPluralFutureVeb(InflectedWordElement element, String baseForm, Object numberValue, Object personValue, String realised) {
		if (personValue == null || Person.FIRST.equals(personValue)) {
			realised = element.getFeatureAsString(LexicalFeature.FUTURE1P);
			
			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "seremos";
				} else if (baseForm.endsWith("r")) {
					realised = baseForm + "emos";
				} 
			}
		} else if (personValue == null || Person.SECOND.equals(personValue)) {
			realised = element.getFeatureAsString(LexicalFeature.FUTURE2P);

			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "sereis";
				} else if (baseForm.endsWith("r")) {
					realised = baseForm + "eis";
				}  
			}
		} else if (personValue == null || Person.THIRD.equals(personValue)) {
			realised = element.getFeatureAsString(LexicalFeature.FUTURE3P);

			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "serão";
				} else if (baseForm.endsWith("r")) {
					realised = baseForm + "ão";
				}
			}
		}
		return realised;
	}

	private String buildSingularFutureVeb(InflectedWordElement element, WordElement baseWord, String baseForm,
			Object numberValue, Object personValue, String realised) {
		if (personValue == null || Person.FIRST.equals(personValue)) {
			realised = element.getFeatureAsString(LexicalFeature.FUTURE1S);

			if(realised == null && baseWord != null && !"ser".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.FUTURE1S);
			}

			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "serei";
				} else if (baseForm.endsWith("r")) {
					realised = baseForm + "ei";
				} else {
					realised = defaultVerbFormat(numberValue, personValue, baseForm);
				}
			}
		} else if (personValue == null || Person.SECOND.equals(personValue)) {
			realised = element.getFeatureAsString(LexicalFeature.FUTURE2S);

			if(realised == null && baseWord != null && !"ser".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.FUTURE2S);
			}

			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "serás";
				} else if (baseForm.endsWith("r")) {
					realised = baseForm + "ás";
				} else {
					realised = defaultVerbFormat(numberValue, personValue, baseForm);
				}
			}
		} else if (personValue == null || Person.THIRD.equals(personValue)) {
			realised = element.getFeatureAsString(LexicalFeature.FUTURE3S);

			if(realised == null && baseWord != null && !"ser".equalsIgnoreCase(baseForm)) { //$NON-NLS-1$
				realised = baseWord.getFeatureAsString(LexicalFeature.FUTURE3S);
			}

			if(realised == null) {
				if (baseForm.equalsIgnoreCase("ser")) {
					realised = "será";
				} else if (baseForm.endsWith("r")) {
					realised = baseForm + "á";
				} else {
					realised = defaultVerbFormat(numberValue, personValue, baseForm);
				}
			}
		}
		return realised;
	}

	/**
		# 10. Particípio. Paradigma:
		#
		# cantado
		# vendido
		# partido
		# posto
		#
		    [AI]R       >       -R,DO           # cantado, partido
		    E R         >       -ER,IDO         # vendido
		    O R         >       -R,STO          # posto
	 */
	@Override
	public String buildParticiplePastVerb(InflectedWordElement element, WordElement baseWord, String baseForm,
			Object number, Object person, Object patternValue) {
		String realised = element.getFeatureAsString(LexicalFeature.PAST_PARTICIPLE);

		if(realised == null && baseWord != null) {
			realised = baseWord.getFeatureAsString(LexicalFeature.PAST_PARTICIPLE);
		}
		
		if(realised == null) {
			if (baseForm.endsWith("er")) {
				realised = baseForm.substring(0, baseForm.length() - 2) + "ido";
			} else if (baseForm.endsWith("or")) {
				realised = baseForm.substring(0, baseForm.length() - 1) + "sto";
			} else if (baseForm.endsWith("r")) {
				realised = baseForm.substring(0, baseForm.length() - 1) + "do";
			}
			
		}
		
		if(realised == null) {
			realised = baseForm;
		}
		
		if (number != null && NumberAgreement.PLURAL.equals(number)) {
			realised = buildRegularPlural((NLGElement) baseWord, realised);
		}
		
		return realised;
	}

	/**
	 *  terminações -ante, -ente e -inte
	 *   AR     >       -R, NTE          # viajante
	 *   ER     >       -R, NTE          # temente
	 *   IR     >       -R, NTE          # pedinte
	 *   
	 */
	@Override
	public String buildParticiplePresentVerb(InflectedWordElement element, WordElement baseWord, Object patternValue,
			String baseForm) {
		String realised;
		realised = element.getFeatureAsString(LexicalFeature.PRESENT_PARTICIPLE);

		if(realised == null && baseWord != null) {
			realised = baseWord.getFeatureAsString(LexicalFeature.PRESENT_PARTICIPLE);
		}

		if(realised == null) {
//			if (baseForm.endsWith("r")) {
//				realised = baseForm.substring(0, baseForm.length() - 2) + "nte";
//			}
			realised = buildGerundVerb(element, baseWord, patternValue, baseForm);
		}
		return realised;
	}

	/**
	 * # 11. Gerúndio. Paradigma:
		#
		# cantando
		# vendendo
		# partindo
		# pondo
		#
		    [AEIO]R     >       -R,NDO          # cantando, vendendo, partindo, pondo
	 */
	@Override
	public String buildGerundVerb(InflectedWordElement element, WordElement baseWord, Object patternValue,
			String baseForm) {
		String realised = null;

		if (baseForm.endsWith("r")) {
			realised = baseForm.substring(0, baseForm.length() - 1) + "ndo";
		}
		
		if(realised == null) {
			realised  = baseForm;
		}
		return realised;
	}

	@Override
	public List<String> getAuxGerundio() {
		return AUX_GERUNDIO;
	}

	@Override
	public List<String> getNotAuxParticiple() {
		return NOT_AUX_PARTICIPIO;
	}

	@Override
	public void checkPossessive(InflectedWordElement element, StringBuffer realised) {
		if(element.getFeatureAsBoolean(Feature.POSSESSIVE).booleanValue()) {
			Object genderValue = element.getFeature(LexicalFeature.GENDER);
			Gender gender = Gender.getInstace(genderValue.toString());
			DiscourseFunction discourseFunction = (DiscourseFunction) element.getFeature(InternalFeature.DISCOURSE_FUNCTION);
			
			if (Gender.FEMININE.equals(gender) || realised.toString().endsWith("a") ||
					realised.toString().endsWith("ã")) {
				if (DiscourseFunction.SUBJECT.equals(discourseFunction)) {
					realised.insert(0, "sua ");
				}else{
					realised.insert(0, "da ");
				}
			} else {
				if (DiscourseFunction.SUBJECT.equals(discourseFunction)) {
					realised.insert(0, "seu ");
				}else{
					realised.insert(0, "do ");
				}
			}
		}
	}

	@Override
	public NLGElement doDeterminerMorphology(InflectedWordElement element) {
		if (element.getParent() != null) {
			doDeterminerMorphology(element, element.getParent(), "");
		}
		NLGElement realisedElement = null;
		Object number = element.getFeature(Feature.NUMBER);
		String baseForm = element.getBaseForm();
		if (element.getRealisation() != null && !element.getRealisation().equals("")) {
			baseForm = element.getRealisation();
		}
		
		if (number != null && NumberAgreement.PLURAL.equals(number)) {
			String pluralWord = buildRegularPlural((NLGElement) element, baseForm);
			realisedElement = new StringElement(pluralWord);
		}else{
			realisedElement = new StringElement(baseForm);
		}
		realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
				element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
		
		return realisedElement;
	}

	@Override
	public NLGElement doAdjectiveMorphology(InflectedWordElement element, WordElement baseWord) {
		NLGElement parent = baseWord.getParent();
		if (parent != null) {
			NLGElement subject = parent.getParent();
			if (subject != null) {
				if (isRegularFeminineWord(subject, subject.getRealisation())) {
					String feminineWord = buildRegularFeminineWord(baseWord.getBaseForm());
					element.setFeature(LexicalFeature.BASE_FORM, feminineWord);
				}
			}
		}
		
		return doDafaultMorphology(element, baseWord);
	}
	
	/**
	 * 
	 * # Flexão simples de genero
	 *  
	 * O      >       -O,A               # menino > menina
	 * R      >       A                  # cantor > cantora
	 * @return
	 */
	private String buildRegularFeminineWord(String word){
		if (word.endsWith("o")){
			return word.substring(0, word.length() - 1) + "a";
		} else if (word.endsWith("r")){
			return word + "a";
		}
		
		return word;
	}

	private NLGElement doDafaultMorphology(InflectedWordElement element, WordElement baseWord) {
		String realised = null;

		// base form from baseWord if it exists, otherwise from element
		String baseForm = getBaseForm(element, baseWord);
		
		Object number = element.getFeature(Feature.NUMBER);
		if (number == null) {
			NLGElement parent = ((NLGElement) baseWord).getParent();
			do {
				if (parent != null && number == null) {
					number = parent.getFeature(Feature.NUMBER);
					
					if (number != null) {
						break;
					}
				}else{
					break;
				}
				parent = parent.getParent();
				
			} while (parent != null);
		}
		if (number != null && NumberAgreement.PLURAL.equals(number)) {
			realised = buildRegularPlural((NLGElement) baseWord, baseForm);
		}else{
			realised = baseForm;
		}

		StringElement realisedElement = new StringElement(realised);
		realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
		                           element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
		return realisedElement;
	}

	@Override
	public NLGElement doAdverbMorphology(InflectedWordElement element, WordElement baseWord) {
		String realised = null;

		// base form from baseWord if it exists, otherwise from element
		String baseForm = getBaseForm(element, baseWord);

		realised = baseForm;

		StringElement realisedElement = new StringElement(realised);
		realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
		                           element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
		return realisedElement;
	}

	@Override
	public NLGElement doPrepositionMorphology(InflectedWordElement element, WordElement baseWord) {
		return doDafaultMorphology(element, baseWord);
	}
	
	/**
	 * This method performs the morphology for determiners.
	 *
	 * @param determiner
	 *            the <code>InflectedWordElement</code>.
	 * @param realisation
	 *            the current realisation of the determiner.
	 */
	@Override
	public void doDeterminerMorphology(NLGElement determiner, NLGElement postElement, String realisation) {
		if (determiner instanceof ListElement) {
			for (NLGElement element : determiner.getChildren()) {
				doDeterminerMorphology(element, postElement, realisation);
			}
		}
		
		if(realisation != null) {
			if((determiner.getRealisation().isEmpty() && 
					determiner instanceof InflectedWordElement &&
					((InflectedWordElement)determiner).getBaseWord().getBaseForm().equals("a")) ||
					determiner.getRealisation().equals("a") || determiner.getRealisation().equals("as")) {
				if (!isRegularFeminineWord(postElement, realisation)) {
					if(determiner.isPlural()) {
						determiner.setRealisation("os");
					}else{
						determiner.setRealisation("o");
					}
				}
			} else if((determiner.getRealisation().isEmpty() && 
					determiner instanceof InflectedWordElement &&
					((InflectedWordElement)determiner).getBaseWord().getBaseForm().equals("o")) ||
					determiner.getRealisation().equals("o") || determiner.getRealisation().equals("os")) {
				if (isRegularFeminineWord(postElement, realisation)) {
					if(determiner.isPlural()) {
						determiner.setRealisation("as");
					}else{
						determiner.setRealisation("a");
					}
				}
			} else if(determiner.getRealisation().equals("uma")) {
				if (!isRegularFeminineWord(postElement, realisation)) {
					if(determiner.isPlural()) {
						determiner.setRealisation("alguns");
					}else{
						determiner.setRealisation("um");
					}
				}else {
					if(determiner.isPlural()) {
						determiner.setRealisation("algumas");
					}
				}
			} else if(determiner.getRealisation().equals("um")) {
				if (isRegularFeminineWord(postElement, realisation)) {
					if(determiner.isPlural()) {
						determiner.setRealisation("algumas");
					}else{
						determiner.setRealisation("uma");
					}
				}else{
					if(determiner.isPlural()) {
						determiner.setRealisation("alguns");
					}
				}
			} else if(determiner.getRealisation().equals("este")) {
				if (isRegularFeminineWord(postElement, realisation)) {
					if(determiner.isPlural()) {
						determiner.setRealisation("estas");
					}else{
						determiner.setRealisation("esta");
					}
				}else{
					if(determiner.isPlural()) {
						determiner.setRealisation("estes");
					}
				}
			} else if(determiner.getRealisation().equals("aquele")) {
				if (isRegularFeminineWord(postElement, realisation)) {
					if(determiner.isPlural()) {
						determiner.setRealisation("aquelas");
					}else{
						determiner.setRealisation("aquela");
					}
				}else if(determiner.isPlural()) {
					determiner.setRealisation("aqueles");
				}
			} else if("aqueles".equals(determiner.getRealisation())) {
				if (isRegularFeminineWord(postElement, realisation)) {
					if (determiner.isPlural()) {
						determiner.setRealisation("aquelas");
					}else{
						determiner.setRealisation("aquela");
					}
				} else if (!determiner.isPlural()) {
					determiner.setRealisation("aquele");
				}
			} else if("estes".equals(determiner.getRealisation())) {
				if (isRegularFeminineWord(postElement, realisation)) {
					if (determiner.isPlural()) {
						determiner.setRealisation("estas");
					}else{
						determiner.setRealisation("esta");
					}
				} else if (!determiner.isPlural()) {
					determiner.setRealisation("este");
				}
			}else if("aquelas".equals(determiner.getRealisation())) {
				if (!isRegularFeminineWord(postElement, realisation)) {
					if (determiner.isPlural()) {
						determiner.setRealisation("aqueles");
					}else{
						determiner.setRealisation("aquele");
					}
				} else if (!determiner.isPlural()) {
					determiner.setRealisation("aquela");
				}
			} else if("estas".equals(determiner.getRealisation())) {
				if (!isRegularFeminineWord(postElement, realisation)) {
					if (determiner.isPlural()) {
						determiner.setRealisation("estes");
					}else{
						determiner.setRealisation("este");
					}
				} else if (!determiner.isPlural()) {
					determiner.setRealisation("esta");
				}
			} else if(determiner.getRealisation().equals("uma")) {
				if (!isRegularFeminineWord(postElement, realisation)) {
					if(determiner.isPlural()) {
						determiner.setRealisation("alguns");
					}else{
						determiner.setRealisation("um");
					}
				}else {
					if(determiner.isPlural()) {
						determiner.setRealisation("algumas");
					}
				}
			}else if(determiner.getRealisation().equals("no")) {
				if (isRegularFeminineWord(postElement, realisation)) {
					if(determiner.isPlural()) {
						determiner.setRealisation("nas");
					}else{
						determiner.setRealisation("na");
					}
				}else {
					if(determiner.isPlural()) {
						determiner.setRealisation("nos");
					}
				}
			}else if(determiner.getRealisation().equals("na")) {
				if (!isRegularFeminineWord(postElement, realisation)) {
					if(determiner.isPlural()) {
						determiner.setRealisation("nos");
					}else{
						determiner.setRealisation("no");
					}
				}else {
					if(determiner.isPlural()) {
						determiner.setRealisation("nas");
					}
				}
			} else if (!determiner.getRealisation().equals("de")) {
				try{
					Integer.parseInt(realisation);
					if(determiner.isPlural()) {
						determiner.setRealisation("os");
					}else{
						determiner.setRealisation("o");
					}
				}catch (Exception e) {}
			}
		}
	}
	
	public static boolean isRegularFeminineWord(NLGElement element, String realisation){
		Object obj = element.getFeature(LexicalFeature.GENDER);
		if (obj != null) {
			Gender gender = Gender.getInstace(obj.toString());
			if (Gender.FEMININE.equals(gender)) {
				return true;
			} else if (Gender.MASCULINE.equals(gender)) {
				return false;
			}
		}
		
		if (realisation.equals("") && element instanceof NPPhraseSpec) {
			Object head = element.getFeature(InternalFeature.HEAD);
			if (head instanceof WordElement) {
				realisation = ((WordElement) head).getBaseForm();
			}
		}
		
		return realisation.endsWith("a") || 
				realisation.endsWith("ã") || 
				realisation.endsWith("oa") || 
				realisation.endsWith("ona") || 
				realisation.endsWith("eira") || 
				realisation.endsWith("esa") || 
				realisation.endsWith("essa") || 
				realisation.endsWith("isa");
		
	}

}
