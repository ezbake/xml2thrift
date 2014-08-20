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

public class SchemaEnumerationType implements SchemaType { 

	private String typeName = "enumeration";
	
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
		if (attributes != null) {
			String value = attributes.getValue("value");
			mapper.getEnumValues().add(value);
			return value;
		} 
		
		/*else { 
			StringBuilder sb = new StringBuilder();
			sb.append("enum NAME { " + SchemaHelper.newLine());
			
			for (String value : mapper.getEnumValues()) { 
				sb.append(" " + value + SchemaHelper.newLine());
			}
			
			sb.append("} " + SchemaHelper.newLine());
			
			TTypeInfo info = new TTypeInfo();
			info.setName(sb.toString());
			info.setType("enum");
			info.setNumber(0);
			info.setUse("");
			mapper.getLines().add(info);
			mapper.getEnumValues().clear();
		}
		 */
		return "";
	}
}
