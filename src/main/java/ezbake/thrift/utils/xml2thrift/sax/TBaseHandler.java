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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.apache.thrift.TBase;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ListMultimap;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;
import ezbake.thrift.utils.xml2thrift.handlerutil.TBaseMapper;
import ezbake.thrift.utils.xml2thrift.util.XMLUtil;

/**
 * A SAX based TBase handler. On parsing an entry, it calls the callback {@link ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback#getOutput(Object)} output method. 
 * The {@link #initialize(String, IHandlerCallback, Map)} method must be called to setup handler. Returns each XML entry as a Map of Thrift objects <br />
 * </p>Example: In the following example, the startElement could be "example".<br />
 * <pre>
 * {@literal <examples> } 
 *  {@literal <example> }
 *   {@literal <heading>Example</heading> }
 *   {@literal <body>This is sample XML Output.</body> }
 *  {@literal </example> }
 * {@literal </examples> }</pre>
 * An example implementation is: <br />
 * <p> {@code String startElement = "example"; }<br />{@code CallbackImpl callback = new CallbackImpl();}<br />
 * {@code Map<String, Class<? extends TBase<?,?>>> classes = Maps.newHashMap(); }<br /><br />{@code classes.put("example", Example.class) } <br /><br />
 * {@code TBaseHandler handler = new TBaseHandler(); }<br />{@code handler.initialize(startElement, callBack, classes); } <br />
 * {@code GenericParser parser = new GenericParser(handler);}<br />{@code parser.processFile(pathToXMLFile); } <br /></p>
 * Output: {@literal Map<String, Object> } of class names and Thrift objects <br /><br />
 */
public class TBaseHandler extends DefaultHandler { 
	
	private TBaseMapper mapper = new TBaseMapper();
	private StringBuilder sbEntry = new StringBuilder();
	private IHandlerCallback<Map<String, Object>> callBack;
	private static final Logger log = LoggerFactory.getLogger(TBaseHandler.class);

	/**
	 * In order to run the TBase handler, the initialize method must be called. <br />
	 * @param startElement the name of the main element to parse
	 * @param callBackImpl an {@link IHandlerCallback} of type String
	 * @param classMap Map of Thrift classes
	 */
	public void initialize (String startElement, IHandlerCallback<Map<String, Object>> callBackImpl, Map<String, Class<? extends TBase<?,?>>> classMap) {
		mapper.setStartElement(startElement);
		this.callBack = callBackImpl;
		this.mapper.setClasses(classMap);
	}	

	public void setComparables(Map<String, String> comparables) { 
		mapper.setComparables(comparables);
	}

	/**
	 * In order to run the TBase handler, the initialize method must be called. <br />
	 * Loads all attributes to {@literal Map<String, String>}. The map must be call 'attributes'. <br />
	 * Each Thrift object must have the 'attributes' map object. <br />
	 * Default is false. <br />
	 * @param loadAttributes Load attributes to Thrift classes with {@literal Map<String, String>} named 'attributes'
	 */
	public void loadAttributesToMap(boolean loadAttributes) { 
		mapper.setLoadAttributesToMap(loadAttributes);
	}

	/**
	 * In order to run the TBase handler, the initialize method must be called. <br />
	 * Performs search of child structs for fields matching tag. <br />
	 * Default is false. <br />
	 * @param locate Search classMap and attempt to load past firt level nesting
	 */
	public void locateChildren(boolean locate) { 
		mapper.setLocateChildren(locate);
	}
	
	/**
     * In order to run the TBase handler, the initialize method must be called. <br />
     * Add the URI as key and prefix as value for namespaces in Java Thrift. <br />
     * @param namespaces Map of URIs with associated List of namespace folders
     */
	public void setNameSpaces(ListMultimap<String, String> namespaces) {
		this.mapper.setNamespaces(namespaces);
	}
	
	public void startPrefixMapping(String prefix, String uri) throws SAXException { 
		log.debug("{} : {} ", prefix, uri);
		if (!this.mapper.getNamespaces().containsValue(prefix)) { 
		    this.mapper.getNamespaces().put(uri, prefix);
		}	
	}

	/**
	 * This is an inherited SAX method. The handler extends it to parse XML.
	 */
	public void startDocument() { 
		Preconditions.checkNotNull(mapper.getStartElement(), "Start element was null");
		Preconditions.checkArgument(!mapper.getStartElement().isEmpty(), "Start element not set");
		Preconditions.checkNotNull(callBack, "Callback was null");
		Preconditions.checkNotNull(mapper.getClasses(), "Class map was null");
		log.debug("Starting document parsing.");
	}

	/**
	 * This is an inherited SAX method. The handler extends it to parse XML.
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) { 

		/* Set namespace and then remove namespace from qName.
		 * Determine if comparable class should be used */
		String tag = TagStart.getQName(qName, mapper);
		boolean isSet = false;

		/* Check if in start/root element already.
		 * OR Check via qName, set inRootItem if true, and return true */
		if (mapper.inStartTag() || ElementTyper.isRoot(tag, mapper)) { 

			/* Iterate through fields and add text to object on match 
			 * This is for cases where tag(s) exists between current tag text */
			java.lang.reflect.Field field = XMLUtil.getFieldFromObject(mapper.getCurrentObj(), mapper.getStack().peek());
			if (field != null) { 	
				XMLUtil.setField(mapper.getCurrentObj(), field, sbEntry.toString());
			}

			if (ElementTyper.isClass(tag, mapper)) { 
				isSet = TagStart.setClass(tag, mapper);
			} 

			if (!isSet && mapper.locateChildren()) { 
				mapper.getCallStack().clear();
				ElementTyper.inChild(mapper.currentObjName(), mapper, tag);	
			}

			TagStart.setAttributes(attributes, mapper);
		}

		/* New element - reset StringBuilder */
		sbEntry.setLength(0);
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

		String tag = TagEnd.getQName(qName, mapper);

		if (mapper.inStartTag()) { 

			/* Iterate through fields and add text to object on match */
			java.lang.reflect.Field field = XMLUtil.getFieldFromObject(mapper.getCurrentObj(), tag);
			if (field != null) { 
				XMLUtil.setField(mapper.getCurrentObj(), field, sbEntry.toString());
			} 

			if (TagEnd.hasParent(tag, mapper)) { 
				TagEnd.makeNest(tag, mapper);
				TagEnd.reset(tag, mapper);
			}

			if (ElementTyper.isClass(tag, mapper)) { 			
				TagEnd.setClass(tag, mapper);
			}

			sbEntry.setLength(0);
		}

		/* If element equals start element, we have reached end of this entry 
		 * return parsed objects to caller */
		if (ElementTyper.isRoot(tag, mapper)) { 
			TagEnd.Finish(mapper, callBack);
		}
	}

	/**
	 * This is an inherited SAX method. The handler extends it to parse XML.
	 */
	public void endDocument() { 
		log.debug("Completed document parsing.");
	}

}
