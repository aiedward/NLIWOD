package org.aksw.hawk.nlp;

import java.io.IOException;
import java.util.List;

import org.aksw.autosparql.commons.qald.Question;
import org.aksw.autosparql.commons.qald.uri.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clearnlp.component.AbstractComponent;
import com.clearnlp.dependency.DEPTree;
import com.clearnlp.nlp.NLPGetter;
import com.clearnlp.nlp.NLPMode;
import com.clearnlp.reader.AbstractReader;
import com.clearnlp.tokenization.AbstractTokenizer;

/**
 * generates a dependency tree called predicate argument tree
 * 
 * @author r.usbeck
 * 
 */
public class ParseTree {
	// TODO find the point where the performance is lost here
	static Logger log = LoggerFactory.getLogger(ParseTree.class);

	final static String language = AbstractReader.LANG_EN;
	final static String modelType = "general-en";

	private static AbstractTokenizer tokenizer = NLPGetter.getTokenizer(language);
	private static AbstractComponent tagger;
	private static AbstractComponent parser;
	private static AbstractComponent identifier;
	private static AbstractComponent classifier;
	private static AbstractComponent labeler;

	private static AbstractComponent[] components;

	protected static synchronized void initialize() {
		if (components == null) {
			try {
				tagger = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
				parser = NLPGetter.getComponent(modelType, language, NLPMode.MODE_DEP);
				identifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_PRED);
				classifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_ROLE);
				labeler = NLPGetter.getComponent(modelType, language, NLPMode.MODE_SRL);
				components = new AbstractComponent[] { tagger, parser, identifier, classifier, labeler };
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public ParseTree() {
		initialize();
	}

	public DEPTree process(Question q) {
		return process(tokenizer, components, q);
	}

	private DEPTree process(AbstractTokenizer tokenizer, AbstractComponent[] components, Question q) {
		String sentence = q.languageToQuestion.get("en");
		if (!q.languageToNamedEntites.isEmpty()) {
			sentence = replaceLabelsByIdentifiedURIs(sentence, q.languageToNamedEntites.get("en"));
			log.debug(sentence);
		}

		DEPTree tree = NLPGetter.toDEPTree(tokenizer.getTokens(sentence));

		for (AbstractComponent component : components)
			component.process(tree);

		log.debug(TreeTraversal.inorderTraversal(tree.getFirstRoot(), 0, null));
		log.debug(tree.toStringSRL());

		return tree;
	}

	private String replaceLabelsByIdentifiedURIs(String sentence, List<Entity> list) {
		for (Entity entity : list) {
			if (!entity.label.equals("")) {
				// " " inserted so punctuation gets seperated correctly from URIs
				sentence = sentence.replace(entity.label, entity.uris.get(0).getURI() + " ").trim();
			} else {
				log.error("Entity has no label in sentence: " + sentence);
			}
		}
		return sentence;
	}

}