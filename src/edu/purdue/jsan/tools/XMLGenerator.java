package edu.purdue.jsan.tools;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLGenerator {
	private String mainDir;
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private TransformerFactory transformerFactory;
	private Transformer transformer;
	private DOMSource source;
	private int num;

	public XMLGenerator() {
		mainDir = "jsan_resources/our_problem_set/";
		docFactory = DocumentBuilderFactory.newInstance();
		transformerFactory = TransformerFactory.newInstance();
		num = 0;

	}

	public void generateXML(String[] authorNames, String folderName,
			int numOfSamples) {
		try {
			num++;
			docBuilder = docFactory.newDocumentBuilder();
			transformer = transformerFactory.newTransformer();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("problem-set");
			doc.appendChild(rootElement);

			// staff elements
			Element trainingSet = doc.createElement("training");
			rootElement.appendChild(trainingSet);

			// set attribute to staff element
			Attr attr = doc.createAttribute("name");
			attr.setValue("author_detect_" + numOfSamples + "_" + num);
			trainingSet.setAttributeNode(attr);

			for (int i = 0; i < authorNames.length; i++) {
				String authorName = authorNames[i];
				Element author = doc.createElement("author");
				Attr attr2 = doc.createAttribute("name");
				attr2.setValue(authorName); // put the name of the author
				author.setAttributeNode(attr2);
				trainingSet.appendChild(author);

				File authorFolder = new File("jsan_resources/corpora/amt/"
						+ authorName);
				String texts[] = authorFolder.list();
				for (int k = 0; k < texts.length; k++) {
					String fileName = texts[k];
					if(fileName.length()>13) continue;
					Element document = doc.createElement("document");
					document.appendChild(doc
							.createTextNode("jsan_resources/corpora/amt/"
									+ authorName + "/" + fileName));
					author.appendChild(document);

					Attr attr3 = doc.createAttribute("title");
					attr3.setValue(fileName.replace(".txt", ""));
					document.setAttributeNode(attr3);
				}

			}

			// write the content into xml file
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(new File(mainDir
					+ folderName + "/author_detect_" + numOfSamples + "_" + num
					+ ".xml"));
			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

}