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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;
import ezbake.thrift.utils.xml2thrift.util.TStructDescriptor;

/**
 * A SAX based generic handler. On parsing an entry, it calls the callback {@link ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback#getOutput(Object)} output method. 
 * Returns each XML entry as a Map of fields of type TStructDescriptor.Field <br/><br/>
 * The {@link GenericHandler#initialize(String, Map, IHandlerCallback)} method must be called to setup handler <br/>
 */
public class GenericHandler extends DefaultHandler { 
	
	private String root = "", location = "";
	private IHandlerCallback<Map<String, ArrayList<Map<String, String>>>> cb;
	private StringBuilder sbEntry = new StringBuilder();
	
	/* Need to think about optimizing now */
	private Map<String, List<TStructDescriptor.Field>> fieldsMap = Maps.newHashMap();
	private Set<String> setClassNames = new HashSet<String>();
	
	private boolean isDone = false;
	private Map<String, ArrayList<Map<String, String>>> data = Maps.newHashMap();
	private ArrayList<Map<String, String>> childList = new ArrayList<Map<String, String>>();
	private Map<String, String> childData = Maps.newHashMap();

	private static final Logger log = LoggerFactory.getLogger(GenericHandler.class);
	
	public GenericHandler() { }
	
	/**
    * In order to run the Generic handler, the initialize method must be called.
    *
    * @param startElement the name of the main element to parse
    * @param fieldMap Thrift fields of type Map<String, List<TStructDescriptor.Field>
    * @param callback an IHandlerCallback of type Map<String, ArrayList<Map<String, String>>>
    */
	public void initialize (String startElement, Map<String, List<TStructDescriptor.Field>> fieldMap, IHandlerCallback<Map<String, ArrayList<Map<String, String>>>> callback) {
		root = startElement;
		fieldsMap = fieldMap;
		cb = callback;
		this.getClassNames();
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void startDocument() throws SAXException { 
		Preconditions.checkNotNull(root, "Root element was null");
		Preconditions.checkArgument(!root.isEmpty(), "Root element not set");
		Preconditions.checkNotNull(cb, "Callback was null");
		Preconditions.checkNotNull(fieldsMap, "Fields map was null");
        log.debug("Starting document parsing.");
    }
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
		if (setClassNames.contains(qName.toLowerCase())) {
			if (!location.isEmpty()) {
				
				/* Store data: 
				 * Add to ArrayList - there could be multiple entries
				 * If location and qName don't match, we can assume new entries
				 * therefore, store the elements in our main map 
				 * and reset, proceed */
				childList.add(childData);
				if (!location.toLowerCase().equals(qName)) {
					data.put(location, (ArrayList<Map<String, String>>) childList);
					childList = new ArrayList<Map<String, String>>();
				}
				
				/* Reset all variables */
				childData = Maps.newHashMap();
			}
			/* New class - reset location */
			location = qName.toLowerCase();
			
			/* Populate the attributes to the Map */
			for (int i = 0; i < attributes.getLength(); i++) { 
				childData.put(attributes.getQName(i), attributes.getValue(i));
			}
		}
		
		/* New element - reset StringBuilder */
		sbEntry.setLength(0);	
    }
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void characters(char[] ch, int start, int length) throws SAXException
    {
		sbEntry.append(ch, start, length);
    }
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void endElement(String uri, String localName, String qName) throws SAXException
	{	

		if (qName.toLowerCase().equals(root.toLowerCase())) {
			if (data.size() > 0) { 
				if (!childData.isEmpty()) { 
					childList.add(childData);
					data.put(location, childList);
				}
				cb.getOutput(data);
				data.clear();
			}
		}
		
		log.debug("Key = " + qName + " | Value: " + sbEntry.toString());
		
		String[] value = searchFields(qName);
		if (value != null) { 
			childData.put(value[0], value[1]);
		}	
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void endDocument() throws SAXException {
	    log.debug("Completed document parsing.");
	}
	
	private String[] searchFields(String qName) { 
		String node = "";
		String[] value = new String[2];
		
		for (Entry<String, List<TStructDescriptor.Field>>  map : fieldsMap.entrySet()) {
			node = map.getKey().substring(map.getKey().lastIndexOf(".") + 1).toLowerCase();
			
			if (node.trim().equals(location.trim())) {
				value = getValue(map.getValue(), qName);
				break;
			} else {
				continue;
			}
	    }
		return value;
	}
	
	private String[] getValue(List<TStructDescriptor.Field> list, String keyName) { 
		String[] retVal = new String[2];
		
		for (TStructDescriptor.Field f : list) {		
			if (f.getName().toLowerCase().equals(keyName.toLowerCase())) {
				retVal[0] = f.getName();
				retVal[1] = sbEntry.toString();
				return retVal;
			}
		}
		return null;	
	}
	
	private void getClassNames() { 
		for (String  key : fieldsMap.keySet()) {
			setClassNames.add(key.substring(key.lastIndexOf(".") + 1).toLowerCase());
		}
	}
	
}
