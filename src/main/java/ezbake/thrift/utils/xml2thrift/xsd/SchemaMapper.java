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

package ezbake.thrift.utils.xml2thrift.xsd;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.common.collect.Maps;

public class SchemaMapper {
	
	private String root = "", name = "", type = ""; 
	private MapperStack stack = new MapperStack();
	private MapperData data = new MapperData();
	private MapperValues tempMap = new MapperValues();
	private MapperValuesStack valuesStack = new MapperValuesStack();
	
	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MapperStack getStack() { 
		return stack;
	}
	
	public MapperData getData() { 
		return data;
	}
	
	public MapperValues getMap() { 
		return tempMap;
	}
	
	public MapperValuesStack getValuesStack() { 
		return valuesStack;
	}

	/* Values */
	public static class MapperValues { 
		Map<String, String> values = Maps.newHashMap();
		
		public Map<String, String> getValues() {
			return values;
		}
		
		public void resetValues() {
			values = Maps.newHashMap();
		}
		
		public void setValues(Map<String, String> map) {
			values = map;
		}
		
		public void addValues(String key, String value) { 
			values.put(key, value);
		}
		
		public boolean clear() {
			values.clear();
			return true;
		}

	}
	
	/* Values Stack */
	public static class MapperValuesStack { 
		LinkedList<String> llValuesStack = new LinkedList<String>();
		
		public LinkedList<String> getStack() {
			return llValuesStack;
		}
		
		public String pop() {
			return llValuesStack.pop();
		}
		
		public void push(String data) {
			 llValuesStack.push(data);
		}
		
		public int size() {
			return llValuesStack.size();
		}
		
		public boolean contains(String item) {
			return llValuesStack.contains(item);
		}
	}
	
	/* Main Stack */
	public static class MapperStack { 
		LinkedList<Map<String, String>> llStack = new LinkedList<Map<String, String>>();
		
		public LinkedList<Map<String, String>>  getStack() {
			return llStack;
		}
		
		public int getSize() {
			return llStack.size();
		}
		
		public boolean isEmtpy() {
			return llStack.isEmpty();
		}
		
		public Map<String, String> pop() {
			return llStack.pop();
		}
		
		public void push(Map<String, String> data) {
			llStack.push(data);
		}
	}
	
	/* Final Data */
	public static class MapperData { 
		LinkedHashMap<String, Map<String, String>> lHashMap = new LinkedHashMap<String, Map<String, String>>();
		
		public LinkedHashMap<String, Map<String, String>> getData() {
			return lHashMap;
		}
		
		public void addData(String name, Map<String, String> data) {
			lHashMap.put(name, data);
		}
	}

}
