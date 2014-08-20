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

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import ezbake.thrift.utils.xml2thrift.util.XMLUtil;

public class TBaseMapper {

    private Object currentClassObj = null;
	private String startElement = "", ns = "", className = "";
	private java.lang.reflect.Field[] jFields = null;
	private boolean inStartTag = false, loadAttributesToMap = false, locateChildren = false, recurse = false;

	private Set<String> callStack = Sets.newHashSet();
	private Map<String, String> comparables = Maps.newHashMap();
	private LinkedList<String> llStack = new LinkedList<String>();
	private Multimap<String, Object> data = ArrayListMultimap.create();
	private ListMultimap<String, String> namespaces = ArrayListMultimap.create();
	private Map<String, Class<? extends TBase<?,?>>> classes = Maps.newHashMap();

	private static final Logger log = LoggerFactory.getLogger(TBaseMapper.class);
	
	public String getStartElement() { 
		return startElement;
	}

	public void setStartElement(String startElement) { 
		this.startElement = startElement;
	}

	public String getNS() {
		return ns;
	}

	/**
    * Derives the namespace by parsing input string (based on colon character), lowercases and sets to mapper.
    * 
    * @param ns The String to parse
    */
	public String setNS(String ns) { 
		int endPos = ns.lastIndexOf(":");
		
		if (endPos != -1 && endPos > 1) { 
			ns = ns.substring(0, endPos);
			this.ns = ns.toLowerCase();
		} else { 
			this.ns = "";
		}
		return ns;
	}

	public ListMultimap<String, String> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(ListMultimap<String, String> namespaces) {
		this.namespaces = namespaces;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean inStartTag() { 
		return inStartTag;
	}

	public void setInStartTag(boolean startTag) { 
		this.inStartTag = startTag;
	}

	public boolean loadAttributesToMap() {
		return loadAttributesToMap;
	}

	public void setLoadAttributesToMap(boolean loadAttributesToMap) {
		this.loadAttributesToMap = loadAttributesToMap;
	}
	
	public boolean locateChildren() {
		return locateChildren;
	}

	public void setLocateChildren(boolean locateChildren) {
		this.locateChildren = locateChildren;
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
	
	public String currentObjName() { 
		if (currentClassObj != null) {
			return currentClassObj.getClass().getSimpleName().toLowerCase();
		} else { 
		    log.debug("CurrentObject is null");
			return "";
		}
		
	}

	public LinkedList<String> getStack() {
		return llStack;
	}
	
	public Map<String, Class<? extends TBase<?, ?>>> getClasses() { 
		return classes;
	}
	
	public void setClasses(Map<String, Class<? extends TBase<?, ?>>> map) { 
		classes = map;
	}
	
	public Multimap<String, Object> getData() { 
		return data;
	}
	
	public Map<String, String> getComparables() { 
		if (comparables == null) { 
			comparables = Maps.newHashMap();
		}
		return comparables;
	}

	public void setComparables(Map<String, String> comparables) {
		this.comparables = comparables;
	}

	public boolean resetAll() {
		this.data.clear();
		this.llStack.clear();
		this.currentClassObj = null;
		this.inStartTag = false;
		return true;
	}

	public Set<String> getCallStack() {
		return callStack;
	}

	public void setCallStack(Set<String> callStack) {
		this.callStack = callStack;
	}

	public boolean inRecurse() {
		return recurse;
	}

	public void setRecurse(boolean inRecurse) {
		this.recurse = inRecurse;
	}
	
	/* *************************** JAVA REFLECT *************************** */
	public void setJFields(java.lang.reflect.Field[] javaFields) {
		jFields = javaFields;
	}
	
	public java.lang.reflect.Field[] getJFields() {
		return jFields;
	}
	
	public boolean isJFieldsNull() {
		if (jFields == null) {
			return true;
		}
		return false;
	}
	
	// Need to think about cleaning this up a bit....
	public boolean setClassInstance(String className) { 
		
		Class cls = XMLUtil.getClass(this, className);
		
		try {
			
			if (cls == null) { 
				cls = this.getClasses().get(this.className); 
				
				if (cls == null) {
				    log.debug("Class is null: " + className);
					return false;				
				}			
			}
			
			if (!cls.isEnum()) { 
				this.setCurrentObj(cls.newInstance());
				this.setJFields(cls.getFields());
				this.className = "";
				return true;
			} else { 
			    log.debug("Class is enum: " + className);
			}
			
		} catch (InstantiationException e) {
		    log.error("setClassInstance(" + className + "): " +  e.getMessage());
		} catch (IllegalAccessException e) {
		    log.error("setClassInstance(" + className + "): " + e.getMessage());
		}
		return false;
	}
	
	public boolean setFields(String className) { 
		Class cls = XMLUtil.getClass(this, className);
		try {
			this.setJFields(cls.getFields());
			return true;
		} catch (Exception e) {
		    log.error("setFields(" + className + "): " + e.getMessage());
		}
		return false;
	}

}
