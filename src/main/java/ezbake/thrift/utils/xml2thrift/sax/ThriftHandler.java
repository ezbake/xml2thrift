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
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.thrift.TBase;

import com.google.common.base.Preconditions;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;
import ezbake.thrift.utils.xml2thrift.handlerutil.ThriftMapper;
import ezbake.thrift.utils.xml2thrift.util.XMLUtil;

/**
 * A SAX based TBase / Thrift Class handler. <br />
 * The {@link #initialize(String, IHandlerCallback, Map) } method must be called to setup handler. <br />
 * 
 * @deprecated use {@link TBaseHandler} instead.  
 */
@Deprecated 
public class ThriftHandler extends DefaultHandler { 
	
	private IHandlerCallback<Map<String, Object>> cb;

	private String root = "";
	private StringBuilder sbEntry = new StringBuilder();

	private ThriftMapper mapper = new ThriftMapper();
	
	private static final Logger log = LoggerFactory.getLogger(ThriftHandler.class);
	
	public ThriftHandler() { }
	
	public ThriftHandler(String startElement, IHandlerCallback<Map<String, Object>> callBackImpl, Map<String, Class<? extends TBase<?,?>>> classMap) { 
		this.root = startElement;
		this.cb = callBackImpl;
		this.mapper.getClasses().setClassMap(classMap);
	}
	
	/**
    * In order to run the Thrift handler, the initialize method must be called.
    *
    * @param	startElement	the name of the main element to parse
    * @param	callback		an {@link IHandlerCallback }
    * @param	classMap		{@literal Map<String, Class<? extends TBase<?,?>>> } Thrift classes
    */
	public void initialize (String startElement, IHandlerCallback<Map<String, Object>> callback, Map<String, Class<? extends TBase<?,?>>> classMap) {
		this.root = startElement;
		this.cb = callback;
		this.mapper.getClasses().setClassMap(classMap);
	}	
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void startDocument() { 
		Preconditions.checkNotNull(root, "Root element was null");
		Preconditions.checkArgument(!root.isEmpty(), "Root element not set");
		Preconditions.checkNotNull(cb, "Callback was null");
		Preconditions.checkNotNull(mapper.getClasses().getClassMap(), "Class map was null");
		log.debug("Starting document parsing.");
    }
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void startElement(String uri, String localName, String qName, Attributes attributes) { 
		
		/* New element - reset StringBuilder */
		sbEntry.setLength(0);
		
		if (qName.equalsIgnoreCase(root)) {
			mapper.setInRootItem(true);
		}
		
		/* 1. If it's a list, we need to add to list instead of directly to map
		 * 2. If they match, we are at the end of the current class */
		if (qName.toLowerCase().contains("list") && mapper.inRootItem()) {
			mapper.setIsListItem(true);
			mapper.getObjects().clear(); 
		} else if (mapper.getClasses().contains(qName.toLowerCase())) { 
				
			/* In a new location, add currently processed object to map */
			if (!mapper.isCurrentObjNull()) { 
				mapper.getData().addData(mapper.getCurrentObj().getClass().getSimpleName().toLowerCase(), mapper.getCurrentObj());
			}
				
			/* Add last class to stack to handle nesting */
			mapper.getStack().push(qName); 
			
			/* Reset current class to new class location....NEEDS To be here! */
			mapper.setClassInstance(qName);

		}
    }
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void characters(char[] ch, int start, int length)  { 
		sbEntry.append(ch, start, length);	
    }
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void endElement(String uri, String localName, String qName)  { 
		
		/* Iterate through fields and add text to object on match */
		if (mapper.isJFieldsNotNull()) {
			for (java.lang.reflect.Field f : mapper.getJFields()) {
				if (f.getName().equalsIgnoreCase(qName)) {
					XMLUtil.setField(mapper.getCurrentObj(), f, sbEntry.toString());
					break;
				}
			}
		}
		
		/* 1. If it's a list, we need to add list to map
		 * 2. If in classes, we are at the end of the current class */
		if (qName.toLowerCase().contains("list")) {
			List<Object> tempObjList = new ArrayList<Object>(mapper.getObjects().getList()); 
			mapper.getData().addData(qName, tempObjList); 
			mapper.resetList();
		} else if (mapper.getClasses().contains(qName.toLowerCase())) { 
		    log.debug(qName + " done");

			if (!mapper.isCurrentObjNull()) {
				
				if (mapper.isListItem()) {
					mapper.getObjects().add(mapper.getCurrentObj()); 
					mapper.setCurrentObj(null);
				} else {
					mapper.getData().addData(mapper.currentObjName(), mapper.getCurrentObj());
					mapper.setCurrentObj(null);
					mapper.getStack().pop(); 
					
					if (mapper.getStack().getSize() > 0) { 
						/* We DO NOT want a new instance here 
						 * We need to return to parent/state prior to parsing child object */
						String sElement = mapper.getStack().element();
						mapper.setCurrentObj(mapper.getData().getObject(sElement)); 
						mapper.setFields(sElement); 
					}
				}
			}
		}
		
		/* If end element equals root element, we have reached end of this entry 
		 * return parsed objects to caller */
		if (qName.toLowerCase().equals(root.toLowerCase())) {
 			cb.getOutput(mapper.getData().getMap()); 
 			mapper.resetAll();
		}
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void endDocument() { 
	    log.debug("Completed document parsing.");
	}

}
