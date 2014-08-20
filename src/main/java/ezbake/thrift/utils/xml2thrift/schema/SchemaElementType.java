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

import org.xml.sax.Attributes;

class SchemaElementType implements SchemaType {

	private String typeName = "element";
	
	@Override
	public String getType() {
		return typeName;
	}
	
	@Override
	public boolean compareType(String name) {
		return name.toLowerCase().contains(typeName);
	}

	@Override
	public String process(Attributes attributes, SchemaMapper mapper) { 
		String name = attributes.getValue("name");
		String ref = attributes.getValue("ref");
		String use = attributes.getValue("use");
		String type = attributes.getValue("type");
		String min = attributes.getValue("minOccurs");
		
		if (min != null) {
			mapper.getSchemaTagInfo().setMinOccurs(Integer.parseInt(min));
		}
		
		// If it's a name, then we adding a new attribute
		if (name != null) { 
			mapper.getSchemaTagInfo().setName(SchemaHelper.parseValueFromNS(name));
			mapper.getSchemaTagInfo().setUse(use, mapper);
			mapper.getSchemaTagInfo().incrementNumber();
			
			if (type != null) { 
				mapper.getSchemaTagInfo().setType(SchemaHelper.getType(type));
			} else if (type == null) {
				mapper.getSchemaTagInfo().setType("struct");
				mapper.getSchemaTagInfo().setNumber(0);
				SchemaHelper.addToLines(mapper);
				return ""; 
			}
			
			// Don't want dupes for types
			if (mapper.getData().containsKey(name.toLowerCase())) {
				mapper.getData().remove(name.toLowerCase());
			}
			
			SchemaHelper.addToLines(mapper);
			// Add back
			SchemaHelper.addToMap(mapper);
		}

		// If it's a ref, then we are referencing an already defined attributeGroup
		if (ref != null) { 
			mapper.getSchemaTagInfo().setName(SchemaHelper.parseValueFromNS(ref));
			
			if (type != null && mapper.getSchemaTagInfo().getName().equalsIgnoreCase(ref)) { 
				mapper.getSchemaTagInfo().setType(SchemaHelper.getType(type));		
			} else {
				mapper.getSchemaTagInfo().setType(SchemaHelper.parseValueFromNS(ref));
			}
			
			mapper.getSchemaTagInfo().setUse(use, mapper);
			mapper.getSchemaTagInfo().incrementNumber();
			SchemaHelper.addToLines(mapper);	
		}
		
		return ""; 
	}
}
