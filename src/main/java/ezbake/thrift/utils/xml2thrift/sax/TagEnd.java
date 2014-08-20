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
import java.lang.reflect.Field;
import java.lang.reflect.Array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.thrift.TBase;

import com.google.common.collect.Maps;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;
import ezbake.thrift.utils.xml2thrift.handlerutil.TBaseMapper;
import ezbake.thrift.utils.xml2thrift.util.XMLUtil;

/**
 * Processes related to SAX endElement method
 */
public class TagEnd { 

	private static final Logger log = LoggerFactory.getLogger(TagEnd.class);

	/**
	 * Determines if qName should use a comparable class.
	 * If so, sets to comparable class name and lowercases result.
	 * @param qName The qName to parse XML namespace from and determine comparables
	 * @param mapper The current mapper object
	 */
	public static String getQName(String qName, TBaseMapper mapper) { 
		String newQName = "";

		mapper.setNS(qName);
		newQName = qName.substring(qName.lastIndexOf(":") + 1, qName.length());
		newQName = newQName.toLowerCase();

		if (mapper.getComparables().containsKey(newQName)) {  
			newQName = mapper.getComparables().get(newQName);	
		}

		return newQName;
	}

	/**
	 * If exists, add previous object to datamap and pop stack. 
	 * Get object from datamap with stack element as key, set as current object and remove object from datamap.
	 * @param mapper The current mapper object
	 * @return true if class is set
	 */
	public static boolean setClass (String tag, TBaseMapper mapper) { 
		if (!mapper.isCurrentObjNull()) {
			mapper.getData().put(mapper.currentObjName().toLowerCase(), mapper.getCurrentObj());
			mapper.setCurrentObj(null);
			if (!mapper.getStack().isEmpty() && tag.equalsIgnoreCase(mapper.getStack().element())) {
				mapper.getStack().pop();
			}
		}

		if (mapper.getStack().isEmpty()) { 
			return false;
		} 

		/* We DO NOT want a new instance here 
		 * We need to return to parent/state prior to parsing child object */
		String sElement = mapper.getStack().element();
		List<Object> value = (List<Object>) mapper.getData().get(sElement.toLowerCase());

		if (value.isEmpty()) {
			return false;
		}

		mapper.setCurrentObj(value.get(0));
		mapper.getData().remove(sElement.toLowerCase(), value.get(0));

		if (!mapper.setFields(sElement.toLowerCase())) {
			log.warn("Fields were not reset to parent class");
		}

		return true;
	}

	/**
	 * Retreive object from datamap. If field is list type, returned object is of list type.
	 * @param qName The qName 
	 * @param field The target field
	 * @param mapper The current mapper object
	 * @return True if set
	 */
	public static Object getObject(String qName, Field field, TBaseMapper mapper) { 
		try { 
			if (mapper.getCurrentObj() == null) { 
				Object object = XMLUtil.getObjectFromList(mapper.getData().get(qName.toLowerCase()), 0);
				return object;
			}

			if (field.getType() == List.class) {
				List<Object> listObj = new ArrayList<Object>();
				listObj.add(mapper.getCurrentObj());
				return listObj;
			}
		} catch (IndexOutOfBoundsException e) { 
			log.error("TagEnd.getObject: ", e);
			return false;
		} catch (Exception e) {
			log.error("TagEnd.getObject: ", e);
			return false;
		}
		return mapper.getCurrentObj();
	}

	public static boolean setObject(String name, Object child, TBaseMapper mapper) { 
		Object main  = null;
		boolean isSet = false;

		try { 
			if (mapper.getCurrentObj() == null) { 
				return false;
			}

			for (Field f : mapper.getCurrentObj().getClass().getFields()) {
				f.setAccessible(true);

				if (f.get(mapper.getCurrentObj()) == null) { 
					// 
					if (f.getName().equalsIgnoreCase(name)) { 
						Class<?> clazz = XMLUtil.getClass(mapper, name);
						main = clazz.newInstance();
						isSet = XMLUtil.setField(mapper.getCurrentObj(), f, main);
						break;
					}		 
				} else { 
					// Need to ensure we are pulling the same field
					if (f.getName().equalsIgnoreCase(name)) {
						main = f.get(mapper.getCurrentObj());
						break;
					}
				}  
			}

			if (main == null) { 
				log.warn("TagEnd.setObject: Field not set: " + name);
				return false;
			}

			for (Field field : main.getClass().getFields()) { 
				String childName = child.getClass().getSimpleName();

				if (field.getName().equalsIgnoreCase(childName)) { 

					if (field.getType() == List.class) { 
						List<Object> temp = new ArrayList<Object>();
						temp.add(child);
						isSet = XMLUtil.setField(main, field, temp);
					} else { 
						isSet = XMLUtil.setField(main, field, child);
					}

					if (isSet) {
						mapper.getData().remove(childName.toLowerCase(), child);
					}

					return isSet;
				}
			}
		} catch (IllegalArgumentException e) {
			log.error("TagEnd.setObject: " + e.getMessage());
			log.debug("TagEnd.setObject: ", e);
			return false;
		} catch (SecurityException e) { 
			log.error("TagEnd.setObject: " + e);
			return false;
		} catch (IllegalAccessException e) { 
			log.error("TagEnd.setObject: " + e);
			return false;
		} catch (InstantiationException e) { 
			log.error("TagEnd.setObject: " + e);
			return false;
		}
		return isSet;
	}

	/**
	 * Retrieves class from classmap based on previous stack object.
	 * @param mapper The current mapper object
	 * @return The class or null
	 */
	public static Class<?> getParentClass(TBaseMapper mapper) { 
		/* At least two items in stack for parent-child nesting */
		if (mapper.getStack().size() < 2) { 
			return null;
		}

		Class<?> clazz = null;

		/* Need second item in stack - the parent  */
		String parent = mapper.getStack().get(1);

		/* Need to load class to iterate fields */
		clazz = XMLUtil.getClass(mapper, parent);

		return clazz;
	}

	/**
	 * Retrieves object from datamap based on previous stack object.
	 * @param mapper The current mapper object
	 * @return The object or null
	 */
	public static Object getParentObject(TBaseMapper mapper) { 
		/* At least two items in stack for parent-child nesting */
		if (mapper.getStack().size() < 2) { 
			return null;
		}

		/* Need second to last item in stack - the parent */
		String parent = mapper.getStack().get(1);

		/* Need to get the class from parsed data */		
		Object parentObject = XMLUtil.getObjectFromList(mapper.getData().get(parent.toLowerCase()), 0);

		return parentObject;
	}

	/**
	 * Retrieves parent class based on previous stack object.
	 * Determines if parent class or comparables has field that matches qName.
	 * @param qName The qName
	 * @param mapper The current mapper object
	 * @return True if parent class has field that matches
	 */
	public static boolean hasParent(String qName, TBaseMapper mapper) { 

		Class<?> clazz = getParentClass(mapper);

		if (clazz == null) { 
			return false;
		}

		/* If the qName matches, then the current object is a child */
		for (Field field : clazz.getFields()) {
			if (field.getName().equalsIgnoreCase(qName)) { 			
				return true;
			}		

			if (mapper.getComparables().containsKey(qName.toLowerCase())) {  
				String name = mapper.getComparables().get(qName.toLowerCase());	

				if (field.getName().equalsIgnoreCase(name)) { 			
					return true;
				}	
			}
		}
		return false;
	}

	/**
	 * 
	 * @param qName The qName 
	 * @param mapper The current mapper object 
	 * @return True if set 
	 */
	public static boolean makeNest(String qName, TBaseMapper mapper) { 

		Class<?> clazz = getParentClass(mapper);
		Object parentObject = getParentObject(mapper);

		if (parentObject == null) { 
			log.warn("Missed field: " + qName);
			return false;
		}

		/* If the qName matches, then the current object is a child */
		for (Field field : clazz.getFields()) { 

			Object childObject = getObject(qName, field, mapper);

			/* To counter act primitive's with the same name as parent classes 
			 * Need to find an optimal place... */
			if (XMLUtil.isPrimitive(field)) {
				continue;
			}

			if (XMLUtil.compareTypes(qName, field, childObject)) { 

				// Need to check children
				mapper.getCallStack().clear();
				mapper.getCallStack().add(qName);
				hasChildren(qName, mapper, childObject);

				// Set parent-child nesting - after child's children are set
				if (XMLUtil.setObject(parentObject, field, childObject)) {
					/* Remove from map after it's been set 
					 * if not in map, look in comparables and remove */
					mapper.getData().remove(qName.toLowerCase(), childObject);
					if (mapper.getComparables().containsKey(qName.toLowerCase())) {  
						String name = mapper.getComparables().get(qName.toLowerCase());
						mapper.getData().remove(name.toLowerCase(), childObject);
					}
				}

				mapper.setRecurse(false);
				return true;
			} else if (qName.equalsIgnoreCase(field.getName())) { 
				try {

					TBase<?, ?> temp = (TBase<?, ?>) XMLUtil.getObjectFromList(mapper.getData().get(qName), 0); 
					if (temp instanceof TBase) {
						childObject = temp.deepCopy();
					}

					if (XMLUtil.setObject(parentObject, field, childObject)) { 
						return true;
					}
				} catch (Exception e) {
					log.error("TagEnd.makeNest: ", e);
				}
			}
		}

		return false;
	}

	public static boolean hasChildren(String parentName, TBaseMapper mapper, Object parentObj) { 
		try {   
			/* Need to load class to iterate fields */
			Class<?> clazz = XMLUtil.getClass(mapper, parentName);

			if (clazz == null) { 
				return false;
			}

			/* If the qName matches, then the current object is a child */
			for (Field field : clazz.getFields()) { 

				String childName = field.getName().toLowerCase();

				if (mapper.getStack().contains(childName)) {
					continue;
				}

				if (parentName.equalsIgnoreCase(childName)) { 
					continue;
				}

				if (XMLUtil.isPrimitive(field)) {
					continue;
				}

				List<Object> child = (List<Object>) mapper.getData().get(childName.toLowerCase());

				if (mapper.locateChildren() && !mapper.getStack().contains(parentObj.getClass().getSimpleName())) {
					nestChildren(mapper, clazz, parentObj, field);
				}

				if (child != null && !child.isEmpty()) { 
					Object childObj = XMLUtil.getObjectFromList(child, 0);

					if (setObject(parentName, childObj, mapper) == false) {
						log.debug("");
					}
				} else { 
					/* Recursively add children 
					 * Keep track of recursive stack */

					if (mapper.getCallStack().size() == 2 && mapper.getCallStack().contains(parentName)) { 
						mapper.setRecurse(true);
					} else {
						mapper.getCallStack().add(childName);
						hasChildren(childName, mapper, parentObj);
						mapper.getCallStack().remove(childName);
					}

					if (mapper.inRecurse() && mapper.getCallStack().size() == 5) {
						return false;
					}
				}		
			}	
		} catch (Exception e) {
			log.error("TagEnd.hasChildren: ", e);
			return false;
		}
		return false;
	}

	public static boolean nestChildren(TBaseMapper mapper, Class<?> clsParent, Object objParent, Field field) { 
		Object temp = null;
		Object parent = null;

		if (objParent instanceof List || objParent instanceof ArrayList) {
			temp = XMLUtil.getObjectFromList(objParent, 0);
		} else if (clsParent == objParent.getClass())  {
			temp = objParent;
		} else {
			temp = XMLUtil.getNewInstanceFromClass(clsParent);
		}

		Object child = XMLUtil.getInstanceFromField(field, temp);

		if (child == null) { 
			parent = XMLUtil.getNewInstanceFromClass(clsParent);
			child = XMLUtil.getInstanceFromField(field, parent);
		}

		if (child == null) {
			return false;
		}

		/* Ensure we are setting to the right child by matching with call stack 
		 * For cases where two or more classes have the same types */
		Object[] callStackArray = mapper.getCallStack().toArray();
		if (callStackArray.length > 0) {
			String callStackParent = (String) Array.get(callStackArray, callStackArray.length - 1);
			if (!child.getClass().getSimpleName().equalsIgnoreCase(callStackParent)) {
				return false;
			}
		}

		for (Field targetField : child.getClass().getFields()) {

			if (targetField == null) {
				return false;
			}

			String fieldName = targetField.getName().toLowerCase();

			if (mapper.getStack().contains(fieldName)) {
				return false;
			}

			if (mapper.getData().containsKey(fieldName)) { 
				Object o = XMLUtil.getObjectFromList(mapper.getData().get(fieldName), 0);

				if (XMLUtil.isPrimitive(targetField)) {
					break;
				}

				if (XMLUtil.setField(child, targetField, o)) { 
					mapper.getData().remove(fieldName, o);
					if (parent != null) { 
						XMLUtil.setFieldOnMatch(temp, parent);
					} else {
						XMLUtil.setFieldOnMatch(temp, objParent);
					}
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Reset the stack, current object, and datamap.
	 * @param mapper The current mapper object
	 * @param tag the qName tag
	 */
	public static boolean reset(String tag, TBaseMapper mapper) { 

		if (mapper.getStack().isEmpty()) {
			return false;
		}

		if (tag.equalsIgnoreCase(mapper.getStack().element())) { 

			/* Try removing object from data map
			if it isn't removed from map, it might be a list item 
			try getting item from and removing */
			if (!mapper.getData().remove(tag, mapper.getCurrentObj())) { 
				Object genericObj = mapper.getData().get(tag);
				Object obj = XMLUtil.getObjectFromList(genericObj, 0);
				mapper.getData().remove(tag, obj);
			}
			mapper.getStack().pop();
			mapper.setCurrentObj(null);
			return true;
		}
		return false; 
	}

	/**
	 * Reached end element, return output to callback class.
	 * @param mapper The current mapper object
	 * @param callback IHandlerCallback
	 */
	public static void Finish(TBaseMapper mapper, IHandlerCallback<Map<String, Object>> callBack) { 
		try {		
			if (!mapper.isCurrentObjNull()) {
				mapper.getData().put(mapper.currentObjName().toLowerCase(), mapper.getCurrentObj());
				mapper.setCurrentObj(null);
			}

			Map<String, Object> map = Maps.newHashMap();

			for (Object key : mapper.getData().keySet()) { 
				List<Object> value = (List<Object>) mapper.getData().get(key.toString());

				if (value.size() > 1) {
					map.put(key.toString(), value);
				} else {
					map.put(key.toString(), value.get(0));
				}
				log.debug(key.toString());
			}

			callBack.getOutput(map); 
			mapper.resetAll();
		} catch (Exception e) {
			log.error("TagEnd.finish: ", e);
		}
	}

}
