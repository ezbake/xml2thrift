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
package ezbake.thrift.utils.xml2thrift.schema;

import org.xml.sax.Attributes;

public class SchemaSequenceType implements SchemaType {

	private String typeName = "sequence";
	
	@Override
	public String getType() {
		return typeName;
	}
	
	@Override
	public boolean compareType(String name) {
		return name.toLowerCase().contains(typeName);
	}

	@SuppressWarnings("unused")
	@Override
	public String process(Attributes attributes, SchemaMapper mapper) { 
		
		if (attributes != null) { 
			String min = attributes.getValue("minOccurs");
			
			if (min != null) {
				mapper.getSchemaTagInfo().setMinOccurs(Integer.parseInt(min));
			}
		} else { 
			mapper.getSchemaTagInfo().setMinOccurs(0);
			
			if (!mapper.getStack().contains("all")) {
				mapper.getSchemaTagInfo().setUse("optional");
			}	
		}
		
		return "";
	}
}
