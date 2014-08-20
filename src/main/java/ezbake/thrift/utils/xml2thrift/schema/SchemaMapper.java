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

package ezbake.thrift.utils.xml2thrift.schema;

import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;

import com.google.common.collect.Maps;

public class SchemaMapper { 
	
	public enum ThriftType { STRUCT, ENUM };
	private ThriftType thriftType =  ThriftType.STRUCT;
	private SchemaTagInfo schemaTagInfo = new SchemaTagInfo();
	
	private Stack<String> stack = new Stack<String>();
	private Queue<SchemaTagInfo> lines = new LinkedList<SchemaTagInfo>();
	private Queue<String> enumValues = new LinkedList<String>();
	private Map<String, Object> data = Maps.newHashMap();
	private Map<String, Object> temp = Maps.newHashMap();
	private String xsdPrefix = "xs", outputFile = "schemaOut.thrift", thriftNamespace = "namespace java ezbake.thrift.utils.xmlparser.nsunknown "; 
	
	private Map<String, String> namespaces = Maps.newHashMap();
	
	public SchemaTagInfo getSchemaTagInfo() {
		return schemaTagInfo;
	}

	public void setSchemaTagInfo(SchemaTagInfo attributes) {
		this.schemaTagInfo = attributes;
	}

	public String getXSDPrefix() {
		return xsdPrefix;
	}

	public void setXSDPrefix(String xsdPrefix) {
		this.xsdPrefix = xsdPrefix;
	}

	public Queue<SchemaTagInfo> getLines() {
		return lines;
	}

	public void setLines(Queue<SchemaTagInfo> lines) {
		this.lines = lines;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public String getThriftNamespace() {
		return thriftNamespace;
	}

	public void setNamespace(String namespace) {
		this.thriftNamespace = namespace;
	}
	
	public Map<String, String> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(String key, String value) {
		this.namespaces.put(key, value);
	}

	public Stack<String> getStack() { 
		return stack;
	}
	
	public Map<String, Object> getData() { 
		return data;
	}

	public Queue<String> getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(Queue<String> enumValues) {
		this.enumValues = enumValues;
	}

	public Map<String, Object> getTemp() {
		return temp;
	}

	public void setTemp(Map<String, Object> temp) {
		this.temp = temp;
	}

	public ThriftType getThriftType() {
		return thriftType;
	}

	public void setThriftType(ThriftType thriftType) {
		this.thriftType = thriftType;
	}
}
