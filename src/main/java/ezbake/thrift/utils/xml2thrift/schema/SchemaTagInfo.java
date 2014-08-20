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

import java.util.LinkedList;
import java.util.Queue;

public class SchemaTagInfo { 
	
	private int minOccurs = 0, number = 1;
	private String name = "", type = "", use = "optional", fixed = "";
	private Queue<String> enumValues = new LinkedList<String>();

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

	public int getMinOccurs() {
		return minOccurs;
	}

	public void setMinOccurs(int minOccurs) {
		this.minOccurs = minOccurs;
	}
	
	public int getNumber() { 
		return number;
	}

	public void setNumber(int itemNumber) { 
		this.number = itemNumber;
	}
	
	public void incrementNumber() { 
		this.number = number + 1;
	}
	
	public String getUse() { 
		return use;
	}

	public void setUse(String use) { 
		this.use = use;
	}
	
	/** 
	 * Required: {@literal "minOccurs" > 0} </br >
	 * Required: {@literal "all" } */
	public void setUse(String use, SchemaMapper mapper) { 
		if (use == null || use.isEmpty()) {
			use = "optional";
			
			if (mapper.getStack().contains(mapper.getXSDPrefix() + ":all")) {
				use = "required";
			} else if (mapper.getSchemaTagInfo().getMinOccurs() > 0) {
				use = "required";
			}
		}
		this.use = use;
	}

	public Queue<String> getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(Queue<String> enumValues) {
		this.enumValues = enumValues;
	}

	public String getFixed() {
		return fixed;
	}

	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

}
