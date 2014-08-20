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

import org.xml.sax.Attributes;

public class SchemaSimpleType implements SchemaType {

	private String typeName = "simpletype";
	
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
			String name = attributes.getValue("name");
			if (name != null && !name.isEmpty()) {
				mapper.getSchemaTagInfo().setName(name);
			}
		} else { 
			if (mapper.getEnumValues().size() > 0) { 
				mapper.getSchemaTagInfo().setEnumValues(new LinkedList<String>(mapper.getEnumValues()));
				mapper.getEnumValues().clear();
				mapper.getSchemaTagInfo().setType("enum");
				mapper.getSchemaTagInfo().setNumber(0);
				SchemaHelper.addToLines(mapper);
				SchemaHelper.addToMap(mapper);
				
			}
		}
		
		return ""; //SchemaHelper.makeLine(mapper);
	}

}
