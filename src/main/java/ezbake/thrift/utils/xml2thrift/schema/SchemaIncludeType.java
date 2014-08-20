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

class SchemaIncludeType implements SchemaType {

private String typeName = "include";
	
	public String getType() {
		return typeName;
	}
	
	public boolean compareType(String name) {
		return name.toLowerCase().contains(typeName);
	}

	@Override
	public String process(Attributes attributes, SchemaMapper mapper) {
		String schemaLoc = "";

		schemaLoc = attributes.getValue("schemaLocation");
		if (schemaLoc.isEmpty()) {
			return "";
		}
		
		String value = SchemaHelper.setInclude(schemaLoc, mapper);
		
		// Precaution
		mapper.getSchemaTagInfo().setNumber(0);
		return value;
	}
}
