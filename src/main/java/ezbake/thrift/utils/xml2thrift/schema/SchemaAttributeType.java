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

class SchemaAttributeType implements SchemaType {

	private String typeName = "attribute";
	
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
		//String name = SchemaHelper.getName(attributes);
		String name = attributes.getValue("name");
		String ref = attributes.getValue("ref");
		String use = attributes.getValue("use");
		String type = attributes.getValue("type");
		String fixed = attributes.getValue("fixed");
		
		// If it's a name, then we adding a new attribute
		if (name != null) { 
			mapper.getSchemaTagInfo().setName(name);
			mapper.getSchemaTagInfo().setUse(use, mapper);
			mapper.getSchemaTagInfo().incrementNumber();
			
			if (fixed != null) {
				mapper.getSchemaTagInfo().setFixed(fixed);
			}
			
			if (type != null) { 
				mapper.getSchemaTagInfo().setType(SchemaHelper.getType(type));
				SchemaHelper.addToLines(mapper);
			}
			
			// Don't want dupes for types
			if (mapper.getData().containsKey(name.toLowerCase())) {
				mapper.getData().remove(name.toLowerCase());
			}
			
			// Add back
			SchemaHelper.addToMap(mapper);
		}

		// If it's a ref, then we are referencing an already defined attributeGroup
		if (ref != null) { 
			mapper.getSchemaTagInfo().setName(ref);
			mapper.getSchemaTagInfo().setUse(use, mapper);
			mapper.getSchemaTagInfo().setType(SchemaHelper.getType(type));
			mapper.getSchemaTagInfo().incrementNumber();
			mapper.getSchemaTagInfo().setFixed(fixed);
			SchemaHelper.addToLines(mapper);	
		}
		
		return ""; //SchemaHelper.makeLine(mapper);
	}
}
