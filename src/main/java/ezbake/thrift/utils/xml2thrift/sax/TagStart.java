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
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import com.google.common.collect.Maps;

import ezbake.thrift.utils.xml2thrift.handlerutil.TBaseMapper;
import ezbake.thrift.utils.xml2thrift.util.XMLUtil;

/**
 * Processes related to SAX startElement method
 */
public class TagStart { 

	private static final Logger log = LoggerFactory.getLogger(TagStart.class);

	/**
	 * Determines if qName should use a comparable class.
	 * If so, sets to comparable class name and lowercases result.
	 * @param qName The qName to parse XML namespace from and determine comparables
	 * @param mapper The current mapper object
	 */
	public static String getQName(String qName, TBaseMapper mapper) { 
		String newQName = "";

		/* Set the namespace and then parse qName from namespace */
		mapper.setNS(qName);
		newQName = qName.substring(qName.lastIndexOf(":") + 1, qName.length());
		newQName = newQName.toLowerCase();

		/* Look for qName in comparables and set to comparable name if so.*/
		if (mapper.getComparables().containsKey(newQName)) {  
			newQName = mapper.getComparables().get(newQName);	
		}

		return newQName;
	}

	/**
	 * If exists, adds previous object to datamap. 
	 * Sets current object to qName and pushs new classname onto stack.
	 * @param qName The qName to parse XML namespace from and determine comparables
	 * @param mapper The current mapper
	 * @return true if class is set
	 * 
	 */
	public static boolean setClass(String qName, TBaseMapper mapper)  { 
		boolean result = false;
		
		if (!mapper.isCurrentObjNull() && qName.equalsIgnoreCase(mapper.getCurrentObj().getClass().getSimpleName())) {
			return true;
		}

		/* In a new location, add currently processed object to map */
		if (!mapper.isCurrentObjNull()) {
			mapper.getData().put(mapper.currentObjName().toLowerCase(), mapper.getCurrentObj());
		}

		/* Reset current class to new class location....NEEDS To be here! */
		result = mapper.setClassInstance(qName);

		/* Add last class to stack to handle nesting */
		mapper.getStack().push(qName); 

		return result;
	}

	/**
	 * Set attributes to class fields. 
	 * If loadAttributesToMap is true, load to map.
	 * @param attributes The attributes to parse
	 * @param mapper The current mapper object
	 */
	public static void setAttributes(Attributes attributes, TBaseMapper mapper) { 
		/* Populate the attributes to the Thrift object */
		Map<String, String> attr = Maps.newHashMap();
		
		for (int i = 0; i < attributes.getLength(); i++) { 
			log.debug(attributes.getQName(i) + " - " + attributes.getValue(i));

			attr.put(attributes.getQName(i), attributes.getValue(i));

			/* String attribute = attributes.getQName(i).substring(attributes.getQName(i).lastIndexOf(":") + 1, attributes.getQName(i).length());

			If current attribute is a class, set current object to attribute 
			if (ElementTyper.isClass(attribute, mapper)) { 
				//TagStart.setClass(attribute, mapper);
			} */

			XMLUtil.setFieldOnMatch(mapper, attributes.getQName(i), attributes.getValue(i));
		}

		if (mapper.loadAttributesToMap()) { 
			Field field = XMLUtil.getFieldFromObject(mapper.getCurrentObj(), "attributes");

			if (field != null && !attr.isEmpty()) {
				XMLUtil.setField(mapper.getCurrentObj(), field, attr);
			}
		}
	}

}
