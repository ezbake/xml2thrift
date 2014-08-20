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

import java.util.Map;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.Maps;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;
import ezbake.thrift.utils.xml2thrift.xsd.SchemaMapper;
import ezbake.thrift.utils.xml2thrift.xsd.SchemaTyper;

/**
 * A SAX based XSD handler. This class generates Thrift interface definition language (IDL) files based on the XML schema. <br />
 * On parsing an entry, it calls the callback {@link ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback#getOutput(Object)} output method. <br />
 * The {@link #initialize(IHandlerCallback) } method must be called to setup the handler. <br />
 * <p> {@code XSDHandler handler = new XSDHandler(); }<br />{@code IDLGenerator callback = new IDLGenerator(pathToThriftFile);}<br /><br />
 * {@code handler.initialize(callBack); }<br /><br />{@code GenericParser parser = new GenericParser(handler);}<br />
 * {@code parser.processFile(pathToXMLFile); }<br /></p>
 * Once the handler has completed parsing, it will save the Thrift file to the output location specified. 
 */
public class XSDHandler extends DefaultHandler {

	private int count = 0;
	private String structName = "";
	private IHandlerCallback<LinkedHashMap<String, Map<String, String>>> cb;
	
	private SchemaMapper mapper = new SchemaMapper();
	private SchemaTyper typer = new SchemaTyper();
	
	private static final Logger log = LoggerFactory.getLogger(XSDHandler.class);
	
	/**
    * Configures the XSD Handler.
    * @param callBackImpl an {@link IHandlerCallback} of type {@literal LinkedHashMap<String, Map<String, String>> }
    */
	public void initialize (IHandlerCallback<LinkedHashMap<String, Map<String, String>>> callBackImpl) {
		cb = callBackImpl;
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void startDocument() throws SAXException {
	    log.debug("Starting document parsing.");
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void startElement(String uri, String localName, String qName, Attributes attributes) { 
		if (!qName.isEmpty()) {
			typer.parseType(qName, attributes, mapper);
		}
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void characters(char[] ch, int start, int length)  { } 
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void endElement(String uri, String localName, String qName)  { 
		if (qName.toLowerCase().contains(":complextype")) {
			if (!mapper.getStack().isEmtpy()) { 
				addToMap();
				mapper.setName("");
				mapper.setType("");
			}
		} else if (qName.toLowerCase().contains(":include")) { 
			Map<String, String> map = Maps.newHashMap();
			
			if (mapper.getData().getData().size() > 0 ) {
				map = mapper.getData().getData().get("includes");
			}

			for (Map.Entry<String, String> entry : mapper.getMap().getValues().entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}
			mapper.getData().addData("includes", map);
			mapper.getMap().resetValues();
		}
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void endDocument() { 
	    log.debug("Completed document parsing.");
		cb.getOutput(mapper.getData().getData());
	}
	
	private boolean addToMap() { 
		String probableName = "";
		Map<String, String> newMap = Maps.newHashMap();
		
		newMap = mapper.getMap().getValues();
		mapper.getMap().setValues(mapper.getStack().pop());
		
		if (newMap.size() > 0) {
			count++;
			
			if (mapper.getValuesStack().size() > 0 ) { 
				structName = mapper.getValuesStack().pop(); 
			}
			
			// count is a hedge against loss due to name duplication
			probableName = String.valueOf(count) + ":" + structName; 
			mapper.getData().addData(probableName, newMap); 
		}
		return true;
	}

}
