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

package ezbake.thrift.utils.xml2thrift.handlerutil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class ThriftMapper { 
	
	java.lang.reflect.Field[] jFields = null;
	private Object currentClassObj = null;
	private boolean isListItem = false, inRootItem = false;
	
	private MapperStack stack = new MapperStack();
	private MapperClasses classes = new MapperClasses();
	private MapperList objList = new MapperList();
	private MapperData  data = new MapperData();

	private static final Logger LOGGER = LoggerFactory.getLogger(ThriftMapper.class);
	
	public boolean inRootItem() {
		return inRootItem;
	}

	public void setInRootItem(boolean inRootItem) {
		this.inRootItem = inRootItem;
	}

	public boolean isListItem() {
		return isListItem;
	}

	public void setIsListItem(boolean isListItem) {
		this.isListItem = isListItem;
	}

	public Object getCurrentObj() {
		return currentClassObj;
	}

	public void setCurrentObj(Object currentClassObj) {
		this.currentClassObj = currentClassObj;
	}
	
	public boolean isCurrentObjNull() {
		if (currentClassObj == null) {
			return true;
		}
		return false;
	}
	
	public void setJFields(java.lang.reflect.Field[] javaFields) {
		jFields = javaFields;
	}
	
	public java.lang.reflect.Field[] getJFields() {
		return jFields;
	}
	
	public boolean isJFieldsNotNull() {
		if (jFields != null) {
			return true;
		}
		return false;
	}
	
	public String currentObjName() {
		return currentClassObj.getClass().getSimpleName().toLowerCase();
	}

	public MapperStack getStack() { 
		return stack;
	}
	
	public MapperClasses getClasses() { 
		return classes;
	}
	
	public MapperList getObjects() { 
		return objList;
	}
	
	public MapperData getData() {
		return data;
	}
	
	public boolean resetAll() {
		this.objList.clear();
		this.stack.clear();
		this.data.clear();
		this.currentClassObj = null;
		this.inRootItem = false;
		this.jFields =  null;
		this.isListItem = false;
		return true;
	}
	
	public boolean resetList() {
		this.isListItem = false;
		this.objList.clear(); 
		this.currentClassObj = null;
		if (this.stack.getSize() > 0) {  
			this.stack.pop(); 
		}
		return true;
	}
	
	// Need to think about cleaning this up a bit....
	public boolean setClassInstance(String className) {
		Class cls = this.getClasses().getClass(className.toLowerCase()); 
		try {
			this.setCurrentObj(cls.newInstance());
			this.setJFields(cls.getFields()); //= cls.getFields();
			return true;
		} catch (InstantiationException e) {
			LOGGER.error("setClassInstance(" + className + "): " +  e.getMessage());
		} catch (IllegalAccessException e) {
			LOGGER.error("setClassInstance(" + className + "): " + e.getMessage());
		}
		return false;
	}
	
	public boolean setFields(String className) { 
		Class cls = this.getClasses().getClass(className.toLowerCase()); 
		try {
			this.setJFields(cls.getFields()); //= cls.getFields();
			return true;
		} catch (Exception e) {
			LOGGER.error("setFields(" + className + "): " + e.getMessage());
		}
		return false;
	}
	
	public static class MapperClasses {
		Map<String, Class<? extends TBase<?,?>>> classes = Maps.newHashMap();
		
		public Map<String, Class<? extends TBase<?,?>>> getClassMap() {
			return classes;
		}
		
		public void resetClassMap() {
			classes = Maps.newHashMap();
		}
		
		public void setClassMap(Map<String, Class<? extends TBase<?,?>>> map) {
			classes = map;
		}
		
		public void addClass(String key, Class<? extends TBase<?,?>> value) { 
			classes.put(key, value);
		}
		
		public Class getClass(String key) { 
			return classes.get(key);
		}
		
		public boolean contains(String key) {
			return classes.containsKey(key);
		}
		
		public boolean clear() {
			classes.clear();
			return true;
		}
	}
	
	public static class MapperStack { 
		LinkedList<String> llStack = new LinkedList<String>();
		
		public LinkedList<String>  getStack() {
			return llStack;
		}
		
		public int getSize() {
			return llStack.size();
		}
		
		public boolean isEmtpy() {
			return llStack.isEmpty();
		}
		
		public String pop() {
			return llStack.pop();
		}
		
		public String element() {
			return llStack.element();
		}
		
		public void push(String data) {
			llStack.push(data);
		}
		
		public void clear() {
			llStack.clear();
		}
	}

	public static class MapperList { 
		List<Object> objectList = new ArrayList<Object>();
		
		public List<Object> getList() {
			return objectList;
		}
		
		public void add(Object data) {
			objectList.add(data);
		}
		
		public void remove(Object data) {
			objectList.remove(data);
		}
		
		public Object get(int i) {
			return objectList.get(i);
		}
		
		public int size() {
			return objectList.size();
		}
		
		public void clear() {
			objectList.clear();
		}
		
		public void reset() {
			objectList = new ArrayList<Object>();
		}
		
		public boolean contains(Object item) {
			return objectList.contains(item);
		}
	}
	
	public static class MapperData { 
		private Map<String, Object> returnObj = Maps.newHashMap();
		
		public Map<String, Object> getClassMap() {
			return returnObj;
		}
		
		public void resetDataMap() {
			returnObj = Maps.newHashMap();
		}
		
		public void setDataMap(Map<String, Object> map) {
			returnObj = map;
		}
		
		public void addData(String key, Object value) { 
			returnObj.put(key, value);
		}
		
		public Object getObject(String key) { 
			return returnObj.get(key);
		}
		
		public void removeObject(String key) { 
			returnObj.remove(key);
		}
		
		public Map<String, Object> getMap() {
			return returnObj;
		}
		
		public void clear() {
			returnObj.clear();
		}
	}

}
