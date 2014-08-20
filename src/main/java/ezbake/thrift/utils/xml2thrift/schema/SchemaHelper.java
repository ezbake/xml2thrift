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

import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ezbake.thrift.utils.xml2thrift.schema.SchemaMapper;

public class SchemaHelper {

	private static final Logger log = LoggerFactory.getLogger(SchemaHelper.class);
	
	static String newLine() { 
		return System.getProperty("line.separator");
	}
	
	static void addToLines(SchemaMapper mapper) { 
		SchemaTagInfo info = new SchemaTagInfo();
		String type = mapper.getSchemaTagInfo().getType();
		
		info.setName(mapper.getSchemaTagInfo().getName());
		info.setNumber(mapper.getSchemaTagInfo().getNumber());
		info.setUse(mapper.getSchemaTagInfo().getUse(), mapper);
		info.setType(getType(type));
		
		mapper.getLines().add(info);
	}	
	
	static void addToMap(SchemaMapper mapper) { 
		SchemaTagInfo info = new SchemaTagInfo();
		
		if (mapper.getSchemaTagInfo().getName() != null && !mapper.getSchemaTagInfo().getName().isEmpty()) {
			String type = mapper.getSchemaTagInfo().getType();
			
			info.setNumber(mapper.getSchemaTagInfo().getNumber());
			info.setName(mapper.getSchemaTagInfo().getName());
			info.setUse(mapper.getSchemaTagInfo().getUse());
			info.setType(getType(type));
			info.setEnumValues(mapper.getSchemaTagInfo().getEnumValues());
			
			// Don't want dupes for types
			if (mapper.getData().containsKey(info.getName().toLowerCase())) {
				mapper.getData().remove(info.getName().toLowerCase());
			}
			
			mapper.getData().put(info.getName().toLowerCase(), info);
		}
	}
	
	/* TRANSFORM */
	
	static String start(SchemaMapper mapper, String type) { 
		StringBuilder retVal = new StringBuilder();
				
		mapper.getSchemaTagInfo().setNumber(1);
		retVal.append(type + " " + mapper.getSchemaTagInfo().getName() + " { ");
		
		return retVal.toString(); 
	}
	
	static String end(SchemaMapper mapper) { 
		mapper.getSchemaTagInfo().setNumber(1);
		String temp = "} " + System.getProperty("line.separator");
		return temp;
	}
	
	static String makeLine(SchemaMapper mapper) { 		
		String type = "unknown";
		StringBuilder sb = new StringBuilder();
		
		/* NUMBER */
		mapper.getSchemaTagInfo().setNumber(mapper.getSchemaTagInfo().getNumber() + 1);
		
		/* USE */
		if (mapper.getSchemaTagInfo().getUse() == null || mapper.getSchemaTagInfo().getUse().isEmpty()) { 
			mapper.getSchemaTagInfo().setUse("optional", mapper);
		}
		
		/* NAME */
		if (mapper.getSchemaTagInfo().getName() == null || mapper.getSchemaTagInfo().getName().isEmpty() || mapper.getSchemaTagInfo().getName().equalsIgnoreCase("null")) { 
			return "makeLine:ERROR - name was not set";
		}
		
		/* TYPE */
		if (mapper.getSchemaTagInfo().getType() != null) { 
			type = getType(mapper.getSchemaTagInfo().getType());
			mapper.getSchemaTagInfo().setType(type);
		}
		
		return sb.append(" " + mapper.getSchemaTagInfo().getNumber() + ": " + mapper.getSchemaTagInfo().getUse() + " " + mapper.getSchemaTagInfo().getType() + " " + mapper.getSchemaTagInfo().getName() + ";").toString();
	}
	
	static String setInclude(String name, SchemaMapper mapper) { 
		String inputName = name.substring(name.lastIndexOf("/") + 1);
		String thriftName = inputName.replace(".xsd", ".thrift");
		
		String value = "include " + thriftName;
		
		mapper.getSchemaTagInfo().setName(value);
		
		mapper.getSchemaTagInfo().setType("include");
		
		addToLines(mapper);
		addToMap(mapper);
		
		mapper.setSchemaTagInfo(new SchemaTagInfo());
		return value;	
	}
	
	/* PARSING */
	
	static String getType(String type) { 
		
		type = parseValueFromNS(type);
		
		if (type.equalsIgnoreCase("int")) {
			type = "i32";
		} else if (type.equalsIgnoreCase("positiveInteger")) {
			type = "i32";
		} else if (type.equalsIgnoreCase("boolean")) {
			type = "bool";
		} else if (type.equalsIgnoreCase("float")) {
			type = "double";
		} else if (type.equalsIgnoreCase("decimal")) {
			type = "double";
		} else if (type.equalsIgnoreCase("date")) {
			type = "string";
		} else if (type.equalsIgnoreCase("nmtoken")) {
			type = "string";
		} else if (type.equalsIgnoreCase("ncname")) {
            type = "string";
		} else if (type.equalsIgnoreCase("anyURI")) {
			type = "string";
		} 
		return type;
	}
	
	static String parseValueFromNS(String value) { 
		int index = -1;
		
		if (value != null && !value.isEmpty()) {  
			index = value.indexOf(":") + 1;
		} else {
			return "";
		}
	
		return value.substring(index);
	}	
	
	/* FILE IO */
	
	static boolean writeThiftFile(String strThrift, String filePath) {
		
		File f = new File(filePath);
		
		if (f.exists()) {
			log.warn("Overwriting thrift file");
		}
		
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
		    writer.write(strThrift);
		} catch (IOException ex){
			return false; 
		} finally {
		   try {
			   writer.close();
			   } catch (Exception ex) { 
				   return true; 
			}
		}
		return true;
	}
	
	public static boolean appendFile(String strThrift, String filePath) {
		 PrintWriter out = null;
		try {
		    out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
		    out.println(strThrift);
		    out.flush();
		    out.close();
		} catch (IOException e) { 
		    return false;
		}
		return true;

	}
	
}
