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

class SchemaTypeIdentifier {

	public enum Type { 
		ALL,
		ANNOTATION,
		ANY,
		ATTRIBUTE,
		ATTRIBUTEGROUP,
		CHOICE,
	    COMPLEXTYPE, 
	    DOCUMENTATION,
	    ELEMENT,
	    ENUMERATION,
	    GROUP,
	    INCLUDE, 
	    IMPORT,
	    RESTRICTION,
	    SCHEMA,
	    SEQUENCE,
	    SIMPLETYPE,
	    UNKNOWN;
	}
	
	public Type getType(String name) { 
		if (name.toLowerCase().contains("element")) { 
			return Type.ELEMENT;		
		} else if (name.toLowerCase().contains("complextype")) {
			return Type.COMPLEXTYPE;
		} else if (name.toLowerCase().contains("simpletype")) {
			return Type.SIMPLETYPE;
		} else if (name.toLowerCase().contains("attributegroup")) { 
			return Type.ATTRIBUTEGROUP;
		} else if (name.toLowerCase().contains("attribute")) { 
			return Type.ATTRIBUTE;
		} else if (name.toLowerCase().contains("sequence")) { 
			return Type.SEQUENCE;
		} else if (name.toLowerCase().contains("choice")) { 
			return Type.CHOICE;
		} else if (name.toLowerCase().contains("enumeration")) {
			return Type.ENUMERATION;
		} else if (name.toLowerCase().contains("restriction")) { 
			return Type.RESTRICTION;
		} else if (name.toLowerCase().contains("include")) { 
			return Type.INCLUDE;
		} else if (name.toLowerCase().contains("import")) { 
			return Type.IMPORT;
		} else if (name.toLowerCase().contains("annotation")) { 
			return Type.ANNOTATION;
		} else if (name.toLowerCase().contains("documentation")) { 
			return Type.DOCUMENTATION;
		} else if (name.toLowerCase().contains("all")) { 
			return Type.ALL;
		} else if (name.toLowerCase().contains("schema")) { 
			return Type.SCHEMA;
		} else if (name.toLowerCase().contains("group")) { 
			return Type.GROUP;
		} else if (name.toLowerCase().contains("any")) { 
			return Type.ANY;
			/* O.o how to handle this?! 
			Should be a generic class like so: items.put("Clazz", "Class<?>"); 
			but Thrift can't handle that 
			maybe create a generic class to ref */
		}
		return Type.UNKNOWN;
	}
}
