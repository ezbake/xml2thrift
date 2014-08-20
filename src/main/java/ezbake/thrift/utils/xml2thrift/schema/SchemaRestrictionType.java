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

public class SchemaRestrictionType implements SchemaType {

private String typeName = "restriction";
	
	public String getType() {
		return typeName;
	}
	
	public boolean compareType(String name) {
		return name.toLowerCase().contains(typeName);
	}

	@Override
	public String process(Attributes attributes, SchemaMapper mapper) {
		String sType = "";
		
		String sTemp = attributes.getValue("base");
		sType = sTemp;
		
		if (sType != null && sType.isEmpty()) { 
			return "";
		} else { 
			sType = SchemaHelper.getType(sType);
			mapper.getSchemaTagInfo().setType(sType);
			
			String name = mapper.getSchemaTagInfo().getName();
			
			if (name != null & !name.isEmpty()) { 
				SchemaHelper.addToMap(mapper);
			}
		}
		
		return sType;
	}
}
