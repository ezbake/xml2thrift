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

/**
 * 
 */
package ezbake.thrift.utils.xml2thrift.sax;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import ezbake.thrift.utils.xml2thrift.handlerutil.TBaseMapper;
import ezbake.thrift.utils.xml2thrift.util.XMLUtil;

/**
 *
 */
public class ElementTyper { 
	
	private static final Logger log = LoggerFactory.getLogger(ElementTyper.class);
	
	public static boolean isRoot(String qName, TBaseMapper mapper) { 
		if (qName.equalsIgnoreCase(mapper.getStartElement())) { 
			mapper.setInStartTag(true);
			log.debug("Starting tag reached: " + qName);
			return true;
		}
		return false;
	}
	
	public static boolean isClass(String qName, TBaseMapper mapper) { 
		String sQName = qName.toLowerCase();	
		
		if (!XMLUtil.hasClass(mapper, sQName)) { 
			return false;
		}
		
		String className = XMLUtil.getClass(mapper, sQName).getSimpleName();

		if (className.equalsIgnoreCase(sQName)) { 
			
			Class<? extends TBase<?, ?>> clazz =  XMLUtil.getClass(mapper, sQName);
			
			/* To ensure we are referencing the same class - verify first level namespace 
			 * Get the class's namespace and match with the qName namespace */	
			if (mapper.getNS() == "") { 
				return true;
			}
			
			if (XMLUtil.compareNamespaces(mapper, clazz.getCanonicalName(), mapper.getNS())) { 
				return true;
			}
		}
		return false;
	}
	
	public static boolean inChild(String parentClassName, TBaseMapper mapper, String value) { 

		Class<? extends TBase<?, ?>> parentClazz = null;
		Field[] fields = null;
		
		if (mapper.getCurrentObj() == null) { 
			return false;
		}
		
		if (value.equalsIgnoreCase(parentClassName)) { 
			return true;
		}
		
		/* Ensure we have a parent class to set to... */
		if (!parentClassName.isEmpty()) { 
			parentClazz =  XMLUtil.getClass(mapper, parentClassName); 
			
			if (parentClazz == null) {
				return false;
			}
		}
		
		if (!value.isEmpty()) { 
			fields = mapper.getCurrentObj().getClass().getFields();
		} else { 
			ElementTyper.setChild(parentClassName, mapper, mapper.getCurrentObj(), parentClazz.getSimpleName());
			return true;
		}
		
		fields = parentClazz.getFields();

		for (Field field : fields) { 
			
			if (XMLUtil.isPrimitive(field)) {
				continue;
			}
			
			/* Besides primitives, we want to check these structures */
			if (field.getType() == Map.class) { 
				continue;
			} else if (field.getType() == List.class) { 
				continue;
			} 	 

			Object obj = XMLUtil.getNewInstanceFromClass(field.getType());
			Field childField = XMLUtil.getFieldFromObject(obj, value);
			
			if (field.getName().equalsIgnoreCase(parentClassName)) { 
				XMLUtil.setField(mapper.getCurrentObj(), field, obj);
				ElementTyper.inChild(value, mapper, "");
				return true;
			} else if (obj instanceof TBase) { 
				String name = obj.getClass().getSimpleName();
				if (mapper.getCallStack().add(name)) { 
					//TODO: wiki hack
					if (mapper.getCurrentObj().getClass() == parentClazz) { 
						return true;
					}
					ElementTyper.inChild(name, mapper, value);
				} else { 
					XMLUtil.setField(mapper.getCurrentObj(), field, obj);
				}				
			} else if (childField != null) { 
				XMLUtil.setField(obj, childField, mapper.getCurrentObj());
				return true;
			} 
 		}
		return false;
	}
	
	public static void setChild(String qName, TBaseMapper mapper, Object object, String targetClassName) { 
		Map<String, Object> children = Maps.newHashMap();
		
		if (object == null) {
			return;
		}
		
		for (Field field : object.getClass().getFields()) {
			
			String fieldName = field.getType().getSimpleName().toLowerCase();
			Class<?> clazz = XMLUtil.getClass(mapper, fieldName);
			
			if (clazz == null) {
				continue;
			}
			
			Object child = XMLUtil.getInstanceFromField(field, object);
			if (targetClassName.equalsIgnoreCase(clazz.getSimpleName())) { 
				XMLUtil.setObject(object, field, child);
				mapper.getData().put(mapper.currentObjName().toLowerCase(), mapper.getCurrentObj());
				mapper.setCurrentObj(child);
				return;
			}

			children.put(clazz.getSimpleName(), child);	    
		}
		
		for (Entry<String, Object> child : children.entrySet()) { 
			ElementTyper.setChild(child.getKey(), mapper, child.getValue(), targetClassName);
		}
	}
}
