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

package ezbake.thrift.utils.xml2thrift.util;

import java.util.Map;
import java.util.List;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.thrift.TBase;
import org.apache.thrift.meta_data.MapMetaData;

import com.google.common.collect.Lists;

import ezbake.thrift.utils.xml2thrift.handlerutil.TBaseMapper;

public class XMLUtil {

	private static final Logger log = LoggerFactory.getLogger(XMLUtil.class);

	/**
	 * Compare package name of a class and namespace.
	 * @param packageName The package name of the class
	 * @param namespace The namespace to compare to
	 * @return true if namespaces matches
	 */
	public static boolean compareNamespaces(TBaseMapper mapper, String packageName, String namespace) { 

		String ns = XMLUtil.parseNSFromPackageName(packageName);

		if (ns.equalsIgnoreCase(namespace)) { 
			return true;
		}
		
        for (Entry<String, String> entry : mapper.getNamespaces().entries() ) { 
            List<String> prefixes = mapper.getNamespaces().get(entry.getKey());
            if (prefixes.contains(ns) && prefixes.contains(namespace)) {
                return true;
            }  
        }
		log.warn("Namespace " + namespace + " did not match for " + packageName);
		return false;
	}
	
	/**
	 * Parse namespace from package name of a class.
	 * @param packageName The package name of the class
	 * @return The parsed namespace
	 */
	public static String parseNSFromPackageName(String packageName) { 
		String parsedNS = "";
		int beginPos = 0, endPos = 0;

		endPos = packageName.lastIndexOf(".");
		if (endPos <= 0 ) {
			return "";
		}
		packageName = packageName.substring(0, endPos);

		beginPos = packageName.lastIndexOf(".") + 1;
		parsedNS = packageName.substring(beginPos, packageName.length());
		return parsedNS;
	}
	
	public static Class<? extends TBase<?, ?>> getClass(TBaseMapper mapper, String classname) {
		String fqName = "";
		Class<? extends TBase<?, ?>> clazz = null;
		
		clazz = mapper.getClasses().get(classname.toLowerCase());
		
		if (clazz == null) { 
			fqName = mapper.getNS() + ":" + classname;
			clazz = mapper.getClasses().get(fqName);
		}
		
		if (clazz == null) { 
		    for (Entry<String, String> entry : mapper.getNamespaces().entries() ) { 
	            List<String> prefixes = mapper.getNamespaces().get(entry.getKey());
	            
	            for (String prefix : prefixes) {
	                fqName = prefix + ":" + classname;
	                
	                clazz = mapper.getClasses().get(fqName);
	                
	                if (clazz != null) {
	                    return clazz;
	                }
	            }
		    }
		}
		 
		return clazz;
	}
	
	public static boolean hasClass(TBaseMapper mapper, String classname) { 
		
		if (mapper.getClasses().containsKey(classname)) {
			return true;
		}
		
 		String fqName = mapper.getNS() + ":" + classname;
 		
 		if (mapper.getClasses().containsKey(fqName)) {
 			return true;
 		}
 		return false;
	}

	/**
	 * Attempts to instantiate a new instance of the class.
	 * @param clazz The class to get new instance of
	 * @return The object or null
	 * @exception null on InstantiationException and IllegalAccessException
	 */
	public static Object getNewInstanceFromClass(Class<?> clazz) { 
		Object obj = null;

		try {
			if (clazz != null && !clazz.isPrimitive()) { 
				obj = clazz.newInstance();
			}
		} catch (InstantiationException e) { 
			log.debug("XMLUtil.getNewInstanceFromClass: ", e);
			return null;
		} catch (IllegalAccessException e) { 
			log.debug("XMLUtil.getNewInstanceFromClass: ", e);
			return null;
		}
		return obj;
	}
	
	/**
	 * Reuturns instance of object.
	 * @param field The field to set
	 * @param parent The object that contains the field
	 */
	public static Object getInstanceFromField(Field field, Object parent) { 
		Object child = null;	

		try { 
			if (field != null) { 
			    if (field.getType() == MapMetaData.class) {
                    return null;
			    } else if (field.getType() == Map.class) {
					return null;
				} else if (field.getType() == List.class) {
					return null;
				} else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
					return null;
				} else {
					child = field.get(parent);
				}
				
				if (child == null) { 
					child = getNewInstanceFromClass(field.getType());
					field.set(parent, child);
				}
			}
		} catch (IllegalArgumentException e) { 
			log.debug("XMLUtil.getInstanceFromField: ", e);
			return null;
		} catch (IllegalAccessException e) { 
			log.debug("XMLUtil.getInstanceFromField: ", e);
			return null;
		}
		return child;
	}
	
	/**
	 * Look for field in object and return the field.
	 * @param object The object in which to search for field
	 * @param fieldName The target field's name
	 * @return The field or null if not found
	 * @exception null on SecurityException and NoSuchFieldException
	 */
	public static Field getFieldFromObject(Object object, String fieldName) { 
		try { 

			if (object == null || fieldName.isEmpty()) {
				return null;
			}
			
			/* Java's getFields is case sensitive. The search should ignore case */
			for (Field field : object.getClass().getDeclaredFields()) {
				if (field.getName().equalsIgnoreCase(fieldName)) {
					return field;
				}
			}

		} catch (SecurityException e) { 
			log.error("XMLUtil.getFieldFromObject: ", e);
			return null;
		} catch (Exception e) { 
			log.debug("XMLUtil.getFieldFromObject: " + e);
			return null;
		}
		return null;
	}
	
	/**
	 * Determines if the object is an instance of List class.
	 * If it is a list and is not empty, return item at index.
	 * @param object The object to parse
	 * @param index The index to retrieve from list
	 * @return The object at the index or null
	 * @exception null on IndexOutOfBoundsException and ClassCastException
	 */
	public static Object getObjectFromList(Object object, int index) { 
		try { 
			if (object instanceof List) {
				List<Object> listObj = (List<Object>) object;

				if (!listObj.isEmpty()) {
					return listObj.get(index);
				}	
			}
		} catch (IndexOutOfBoundsException e) { 
			log.debug("XMLUtil.getFromList: ", e);
			return null;
		} catch (ClassCastException e) { 
			log.debug("XMLUtil.getFromList: ", e);
			return null;
		}

		return null;
	}
	
	/**
	 * Compare types 
	 * @param qName The qName
	 * @param field The target field name
	 * @param object The value to compare target field to
	 * @return true on match
	 */
	public static boolean compareTypes(String qName, Field field, Object object) { 

		Object tempObject = null;
		Class<?> fieldArgClass = null;
		Type genericFieldType = field.getGenericType();
		
		if (object == null) {
			return false;
		}
		
		if (genericFieldType instanceof ParameterizedType){ 
			
		    ParameterizedType aType = (ParameterizedType) genericFieldType;
		    Type[] fieldArgTypes = aType.getActualTypeArguments();
		    
		    for (Type fieldArgType : fieldArgTypes) { 
		        fieldArgClass = (Class<?>) fieldArgType;
		        
		        if (fieldArgClass.equals(object.getClass())) { 
		        	return true;
		        }
		    }
		}
		
		tempObject = XMLUtil.getObjectFromList(object, 0);

		if (genericFieldType.equals(object.getClass())) {
			return true;
		}
		
		if (tempObject != null && tempObject.getClass().equals(fieldArgClass)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Set child object to appropriate parent object's field. Accounts for list types.
	 * @param field The field to set
	 * @param parent The class which contains the field
	 * @param child The value to set to target field
	 * @return true if field is set
	 * @exception false on NoSuchFieldException, ClassCastException, IllegalArgumentException, SecurityException and IllegalAccessException
	 */
	public static boolean setObject(Object parent, java.lang.reflect.Field field, Object child) {
		try {
			Object children = field.get(parent);
			field.setAccessible(true);

			if (children == null) { 
				/* The field hasn't be set and we are adding the first item */
				if (field.getType() == List.class) { 
					if (child instanceof List) { 
						XMLUtil.setField(parent, field, child);
						return true;
					} else { 
						List<Object> parentList = Lists.newArrayList();
						parentList.add(child);
						XMLUtil.setField(parent, field, parentList);
						return true;
					}
				} else { 
					XMLUtil.setField(parent, field, child);
					return true;
				}
			} else if (!children.equals(child)) { 
				//TODO: Fix RogerRangers dup "TBody Row 1" bug
				if (children instanceof List) { 
					List<Object> childList = (List<Object>) children;
					if (child instanceof List) {  
						if (!isLinked(child, childList)) {
							childList.add(((List<?>) child).get(0));
						}
						return true;
					} else { 
						if (!isLinked(child, childList)) {
							childList.add(child);
						}
						return true;
					}
				} else { 
					XMLUtil.setField(parent, field, child);
					return true;
				}
			}
		} catch (ClassCastException e) { 
			log.error("XMLUtil.setObject: ", e);
			return false;
		} catch (IllegalArgumentException e) { 
			log.error("XMLUtil.setObject: " + e.getMessage());
			log.debug("XMLUtil.setObject: ", e);
			return false;
		} catch (IllegalAccessException e) { 
			log.error("XMLUtil.setObject: ", e);
			return false;
		} catch (SecurityException e) {
			log.error("XMLUtil.setObject: ", e);
			return false;
		}
		return false;
	}
	
	public static boolean isLinked(Object child, List<Object> childList) { 
		/* Do not add to list if child is already referenced in list -- avoid dups */
		for (Object obj : childList) {
			if (obj.equals(child)) { 
				return true;
			} else if (child instanceof List) { 
				/* child is a list - our first object eqivalancy will not work 
				 * Iterate through items since child is a list and check */
				for (Object obj2 : (List<Object>) child) {
					if (obj.equals(obj2)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * If current object has the target field, set value to field. 
	 * @param mapper The current mapper object
	 * @param key The target field's name
	 * @param value The value to set to target field
	 */
	public static void setFieldOnMatch(TBaseMapper mapper, String key, String value) { 

		if (mapper.getCurrentObj() == null) {
			return;
		}

		for (java.lang.reflect.Field field : mapper.getCurrentObj().getClass().getDeclaredFields()) {
			if (field.getName().equalsIgnoreCase(key)) { 

				setField(mapper.getCurrentObj(), field, value);

				// For enum types
				if (mapper.getStack().peek().equalsIgnoreCase(key)) { 
					mapper.getStack().pop();
				}
				break;
			}
		}
	}
	
	/**
	 * If current object has the target field, set child object to field. 
	 * @param parent The parent object
	 * @param child The child object
	 */
	public static boolean setFieldOnMatch(Object parent, Object child) { 

		if (parent == null) {
			return false;
		}
		
		Object target = XMLUtil.getObjectFromList(parent, 0);
		
		if (target == null) {
			target = parent;
		}

		for (java.lang.reflect.Field field : target.getClass().getFields()) {
			if (field.getName().equalsIgnoreCase(child.getClass().getSimpleName())) { 

				setField(parent, field, child);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If current object has the target field, set value to field. 
	 * @param parent The current mapper object
	 * @param jField The target field's name
	 * @param value The value to set to target field
	 * @return true if set
	 * @exception false on NullPointerException, IllegalArgumentException and IllegalAccessException
	 */
	public static boolean setField(Object parent, java.lang.reflect.Field field, Object value) { 
		
		if (parent == null) { 
			log.debug("Object is null: " + field.getName());
			return false;
		}
		
		Class<?> type = field.getType();
		
		try { 		
			if (type.isEnum()) { 
				Object[] enums = type.getEnumConstants();
						
				for (Object e : enums) { 
					
					log.debug(e.toString());
					String enumName = e.toString().replace(" ", "_");
					
					if (((String) value).equalsIgnoreCase(enumName)) {
						field.set(parent, e);
						return true;
					}
				}
			} else if (type == String.class) { 
				String prior = (String) field.get(parent);
				String text = "";
				
				// Ensure prior text doesn't get overridden
				if (prior != null && !prior.isEmpty()) {
					text = prior + value;
				} else {
					text = (String) value;
				}	
				field.set(parent, text);
				return true;
			} else if (type == Integer.class) {
				int intValue = Integer.parseInt((String) value);
				field.setInt(parent, intValue);
				return true;
			} else if (type == int.class) {
				int intValue = Integer.parseInt((String) value);
				field.setInt(parent, intValue);
				return true;
			} else if (type == boolean.class) {
				boolean bValue = Boolean.parseBoolean((String) value);
				field.setBoolean(parent, bValue);
				return true;
			} else if (type == Map.class) {
				field.set(parent, value);
				return true;
			} else if (type == List.class) { 
				if (value.equals(type)) {
					field.set(parent, value);
					return true;
				} else if (value instanceof ArrayList) { 
					field.set(parent, value);
					return true;		
				} else {
					List<Object> child = Lists.newArrayList();
					child.add(value);
					field.set(parent, (List<Object>) child);
					return true;
				} 
			} else { 		
				field.set(parent, value);
				return true;
			}
		} catch (NullPointerException e) {
			log.error("XMLUtil.setField: ", e);
			return false;
		} catch (IllegalArgumentException e) {
			log.error("XMLUtil.setField: " + e.getMessage());
			log.debug("XMLUtil.setField: ", e);
			return false;
		} catch (IllegalAccessException e) {
			log.error("XMLUtil.setField: ", e);
			return false;
		} 	
		return false;		
	}
	
	/**
	 * Determines if the field is an instance of a primitive.
	 * <ul><li>String.class</li>
	 * <li>Boolean.class <b>OR</b> boolean.class</li>
	 * <li>Integer.class <b>or</b> int.class</li>
	 * <li>Double.class <b>or</b> double.class</li>
	 * <li>Float.class <b>or</b> float.class</li></ul>
	 * @param field The field to verify
	 * @return The true if primitive
	 */
	public static boolean isPrimitive(Field field) { 

		if (field == null) { 
			return false;
		}

		if (field.getType() == String.class) { 
			return true;
		} else if (field.getType() == Boolean.class || field.getType() == boolean.class) { 
            return true;
		} else if (field.getType() == Integer.class || field.getType() == int.class) { 
			return true;
		} else if (field.getType() == Double.class || field.getType() == double.class) { 
			return true;
		} else if (field.getType() == Float.class || field.getType() == float.class) { 
			return true;
		}
		return false;
	}
	
	public static boolean appendFile(String strThrift, String filePath) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
			out.println(strThrift);
			out.flush();
			out.close();
		} catch (IOException e) { 
			return false;
		}
		return true;

	}

}
