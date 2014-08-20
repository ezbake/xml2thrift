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

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.base.Preconditions;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;

/**
 * A SAX based String handler. On parsing an entry, it calls the callback {@link ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback#getOutput(Object)} output method. <br />
 * The {@link #initialize(String, IHandlerCallback)} method must be called to the setup handler. <br />
 * </p>Example: In the following example, the startElement could be "example".<br />
 * <pre>
 * {@literal <examples> } 
 *  {@literal <example> }
 *   {@literal <heading>Example</heading> }
 *   {@literal <body>This is sample XML Output.</body> }
 *  {@literal </example> }
 * {@literal </examples> }</pre>
 * <p>{@code String startElement = "example"; }<br />{@code CallbackImpl callback = new CallbackImpl();} <br />
 * {@code StringHandler handler = new StringHandler();} <br /><br />{@code handler.initialize(startElement, callbackMethod);} <br />
 * {@code GenericParser parser = new GenericParser(handler);} <br /> {@code parser.processFile(pathToXMLFile); } <br /><br />
 * Output: {@literal <example><heading>Example</heading><body>This is sample XML Output.</body></example> } <br /><br />
 */
public class StringHandler extends DefaultHandler {

	private String root = "";
	private IHandlerCallback<String> callBack;
	private boolean inRootItem = false, espace = false;
	private StringBuilder sbEntry = new StringBuilder();
	private static final Logger log = LoggerFactory.getLogger(StringHandler.class);
	
	/**
    * In order to run the String handler, the initialize method must be called.<br />
    * @param startElement the name of the main element to parse
    * @param callBackImpl an instance of {@link IHandlerCallback } 
    */
	public void initialize (String startElement, IHandlerCallback<String> callBackImpl) {
		this.root = startElement;
		this.callBack = callBackImpl;
	}
	
	public void escape(boolean escapeXML) {
		this.espace = escapeXML;
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void startDocument() throws SAXException { 
		Preconditions.checkNotNull(root, "Root element was null");
		Preconditions.checkArgument(!root.isEmpty(), "Root element not set");
		Preconditions.checkNotNull(this.callBack, "Callback was null");
		log.debug("Starting document parsing.");
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void startElement(String uri, String localName, String qName, Attributes attributes) { 
		
		if (qName.equalsIgnoreCase(root)) {
			sbEntry.setLength(0);
			inRootItem = true;
		}
		
		if (inRootItem) {
			sbEntry.append("<" + qName+ ">");
			
			/* Populate the attributes to the XML string */
			for (int i = 0; i < attributes.getLength(); i++) { 
				sbEntry.append("<" + attributes.getQName(i) + ">");
				sbEntry.append(attributes.getValue(i));
				sbEntry.append("</" + attributes.getQName(i) + ">");
			}
		}		
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void characters(char[] ch, int start, int length)  { 
		StringBuilder sb = new StringBuilder();
		sb.append(ch, start, length);
		String content = sb.toString();
		
		if (this.espace) {
			content = StringEscapeUtils.escapeXml(content);
		} 
		
		sbEntry.append(content);
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void endElement(String uri, String localName, String qName)  { 
		
		if (inRootItem) {
			sbEntry.append("</" + qName+ ">");
		}
		
		if (qName.equalsIgnoreCase(root)) {
			this.callBack.getOutput(sbEntry.toString());
			inRootItem = false;
		}
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void endDocument() { 
	    log.debug("Completed document parsing.");
	}
}
