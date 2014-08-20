/*   Copyright (C) 2013-2014 Computer Sciences Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */

package ezbake.thrift.utils.xml2thrift.sax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;

import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.google.common.base.Preconditions;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;

/**
 * A class to configure and run SAX based XML Parsers. <br />
 * The constructor {@link #GenericParser(ContentHandler) } requires a content handler. The content handler must be of the following types: 
 * <ul><li>StringHandler: String</li>
 * <li>TBaseHandler: {@literal Map<String, Object>}</li>
 * <li>XSDHandler: {@literal LinkedHashMap<String, Map<String, String>>}</li>
 * <li>GenericHandler: {@literal Map<String, ArrayList<Map<String, String>>}</li>
 * </ul><br />
 * The {@link #initialize(String, IHandlerCallback, Map) } method must be called to setup the handler. <br />
 * If needed, set {@link #setLogWarnings(boolean) } to true to log SAX warnings. <br />
 * Call {@link #processFile(String) } with the location of the XML file to parse.
 */
public class GenericParser {

    private String root = "";
    private ContentHandler handler;
    private boolean logWarnings = false;
	private IHandlerCallback<Map<String, Object>> cb;
	private Map<String, Class<? extends TBase<?,?>>> classMap = null;
	private static final Logger log = LoggerFactory.getLogger(GenericParser.class);
	
	/**
    * If true, sets the error handler to log SAX warnings. <br />
    * @param handlerClass indicates whether to log SAX warnings
    */
	public GenericParser (ContentHandler handlerClass) { 
		handler = handlerClass;
	}
	
	/**
    * If true, sets the error handler to log SAX warnings. <br />
    * @param isLogged indicates whether to log SAX warnings
    */
	public void setLogWarnings(boolean isLogged) { 
		logWarnings = isLogged;
	}
	
	/**
    * In order to run the Generic Parser, the initialize method must be called. <br />
    * @param startElement the name of the main element to parse
    * @param callback an {@link IHandlerCallback }
    * @param classMapObj Map of Thrift classes
    */
	public void initialize(String startElement, IHandlerCallback<Map<String, Object>> callback, Map<String, Class<? extends TBase<?,?>>> classMapObj) {
		this.root = startElement;
		this.cb = callback;
		classMap = classMapObj;
	}
	
	/**
    * Configures the handler and starts parsing the XML file. <br />
    * @param xmlFilePath the location of the XML file to parse
    */
	public void processFile(String xmlFilePath) throws IOException, SAXException, Exception { 
		XMLReader xmlReader = null;
		
		Preconditions.checkNotNull(xmlFilePath, "XML File path was null");
		Preconditions.checkArgument(!xmlFilePath.isEmpty(), "XML File path was not set");
		
		log.debug("Loading XML File");
		InputSource source = new InputSource(xmlFilePath);
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);						
		SAXParser parser = factory.newSAXParser();

		DefaultErrorHandler errHandler = new DefaultErrorHandler();
		errHandler.setLogWarnings(logWarnings);
		
		log.debug("Loading XML Reader");
		xmlReader = parser.getXMLReader();
		xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
		xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
		xmlReader.setErrorHandler(errHandler);
		Preconditions.checkNotNull(handler, "Handler was null");
		xmlReader.setContentHandler(handler);
		
		log.debug("Loading XML Parser");
		xmlReader.parse(source);		
	}
	
	public void processFile(InputStream inputStream) throws IOException, SAXException, Exception { 
		XMLReader xmlReader = null;
		
		Preconditions.checkNotNull(inputStream, "XML input stream was null");
		
		log.debug("Loading Input Stream");
		InputSource source = new InputSource(inputStream);
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);						
		SAXParser parser = factory.newSAXParser();

		DefaultErrorHandler errHandler = new DefaultErrorHandler();
		errHandler.setLogWarnings(logWarnings);
		
		log.debug("Loading XML Reader");
		xmlReader = parser.getXMLReader();
		xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
		xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
		xmlReader.setErrorHandler(errHandler);
		Preconditions.checkNotNull(handler, "Handler was null");
		xmlReader.setContentHandler(handler);
		
		log.debug("Loading XML Parser");
		xmlReader.parse(source);		
	}

	@SuppressWarnings("unused")
	private void processFileWithSchema(String xmlFilePath, String xsdFilePath) throws ParserConfigurationException, SAXException, IOException {
		XMLReader xmlReader = null;
		
		Preconditions.checkNotNull(xmlFilePath, "XML File path was null");
		Preconditions.checkArgument(!xmlFilePath.isEmpty(), "XML File path was not set");
		
		log.debug("Loading XML File");
		InputSource source = new InputSource(xmlFilePath);
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);						
		SAXParser parser = factory.newSAXParser();

		DefaultErrorHandler errHandler = new DefaultErrorHandler();
		errHandler.setLogWarnings(logWarnings);
	
		File file = new File(xsdFilePath);
		System.setProperty("javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema","com.saxonica.jaxp.SchemaFactoryImpl");
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        schemaFactory.setErrorHandler(errHandler);
		Schema schemaGrammar = schemaFactory.newSchema(file);
		ValidatorHandler schemaValidator = schemaGrammar.newValidatorHandler();
		
		TBaseHandler handler = new TBaseHandler();
		handler.initialize(root, cb, classMap);
		schemaValidator.setContentHandler(handler);
		
		log.debug("Loading XML Reader");
		xmlReader = parser.getXMLReader();
		xmlReader.setErrorHandler(errHandler);
		Preconditions.checkNotNull(handler, "Handler was null");
		xmlReader.setContentHandler(schemaValidator);
		
		log.debug("Loading XML Parser");
		xmlReader.parse(source);
	}
}
