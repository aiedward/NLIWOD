package org.aksw.qa.commons.load;

import java.io.InputStream;
import java.util.List;

import org.aksw.qa.commons.datastructure.Question;
import org.junit.Assert;
import org.junit.Test;

public class LoadTest {

	@Test
	@SuppressWarnings("static-access")
	public void loadQALD5Test() {
		ClassLoader.getSystemClassLoader();
		InputStream file = ClassLoader.getSystemResourceAsStream("QALD-5/qald-5_test.xml");
		QALD_Loader ql = new QALD_Loader();
		List<Question> load = QALD_Loader.load(file);
		Assert.assertTrue(load.size() == 59);
		for (Question q : load) {
			Assert.assertTrue(q.id > 0);
			Assert.assertNotNull(q.answerType);
			Assert.assertTrue(q.pseudoSparqlQuery != null || q.sparqlQuery != null);
			Assert.assertNotNull(q.languageToQuestion);
			Assert.assertNotNull(q.languageToKeywords);
			Assert.assertTrue(q.goldenAnswers != null && q.pseudoSparqlQuery != null);
		}
	}
}
