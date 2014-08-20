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

public class SchemaTyper {

	public void parseType(String qName, Attributes attributes, SchemaMapper mapper) {
		
		@SuppressWarnings("unused")
		int i = -1;
		@SuppressWarnings("unused")
		String temp = "";
		SchemaTypeIdentifier identifier = new SchemaTypeIdentifier();
		SchemaTypeIdentifier.Type type = identifier.getType(qName);
		
		switch(type) { 
			case ALL:
				if (mapper.getStack().peek().contains("all")) {
					mapper.getSchemaTagInfo().setUse("required", mapper);
				} else { 
					mapper.getSchemaTagInfo().setUse("optional", mapper);
				}				
				break;
			case ANY:
				//SchemaAnyType any = new SchemaAnyType();
				//any.process(attributes, mapper);
				break;
			case ATTRIBUTE:
				SchemaAttributeType attribute = new SchemaAttributeType();
				temp = attribute.process(attributes, mapper);
				break;
			case ATTRIBUTEGROUP: 
				SchemaAttributeGroupType attributeGroup = new SchemaAttributeGroupType();
				temp = attributeGroup.process(attributes, mapper);
				break;
			case CHOICE: 
				SchemaChoiceType choice = new SchemaChoiceType();
				temp = choice.process(attributes, mapper);
				//mapper.getLines().add(mapper.getSchemaTTypeInfo());
				break;
			case COMPLEXTYPE:
				SchemaComplexType complex = new SchemaComplexType();
				complex.process(attributes, mapper);
				break;
			case ELEMENT:
				SchemaElementType element = new SchemaElementType();
				temp = element.process(attributes, mapper);
				break;
			case ENUMERATION:
				SchemaEnumerationType enumeration = new SchemaEnumerationType();
				temp = enumeration.process(attributes, mapper);
				break;
			case GROUP:
				SchemaGroupType group = new SchemaGroupType();
				temp = group.process(attributes, mapper);
				break;
			case INCLUDE:
				SchemaIncludeType include = new SchemaIncludeType();
				temp = include.process(attributes, mapper);
				break;
			case IMPORT:
				SchemaImportType imprt = new SchemaImportType();
				temp = imprt.process(attributes, mapper);
				break;
			case RESTRICTION: 			
				SchemaRestrictionType restriction = new SchemaRestrictionType();
				temp = restriction.process(attributes, mapper);
				break;
			case SCHEMA: 
				SchemaBaseType schema = new SchemaBaseType();
				schema.process(attributes, mapper);
				break;
			case SEQUENCE: 
				SchemaSequenceType sequence = new SchemaSequenceType();
				sequence.process(attributes, mapper);
				break;
			case SIMPLETYPE: 
				SchemaSimpleType simpleType = new SchemaSimpleType();
				simpleType.process(attributes, mapper);
				break;
			default:
				break;
		}
	}
}
