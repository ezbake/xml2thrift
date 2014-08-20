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

package ezbake.thrift.utils.xml2thrift.thriftgen;

import java.io.File;
import java.util.Map;
import java.io.Writer;
import java.io.IOException;
import java.util.Map.Entry;
import java.io.BufferedWriter;
import java.util.LinkedHashMap;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;

/**
 * Takes output from the XSD Handler to generate the Thrift interface definition language (IDL) file.
 */
public class IDLGenerator implements IHandlerCallback<LinkedHashMap<String, Map<String, String>>> {
	
	private String filePath = "", ns = "";
	private StringBuilder sb = new StringBuilder();
	private static final Logger log = LoggerFactory.getLogger(IDLGenerator.class);
	
	/**
    * Initializes the Thrift interface definition language (IDL) Handler with the location for the .Thrift file.
    * @param outputFilePath the full path (include file name) for the output (.thrift) file
    * @param namespace the namespace to use
    */
	public IDLGenerator (String outputFilePath, String namespace) { 
		filePath = outputFilePath;
		ns = namespace;
	}
	
	/**
    * Creates the Thrift interface definition language (IDL) for the XSD supplied to the XSD Handler.
    * @param schemaMap of type {@literal LinkedHashMap<String, Map<String, String>> }
    */
	@Override
	public void getOutput(LinkedHashMap<String, Map<String, String>> schemaMap) {
		String structName = "", strKey = "";
		
		setIncludes(schemaMap);
		
		sb.append("namespace java " + ns + filePath.substring(filePath.lastIndexOf("/") + 1) + System.lineSeparator() + System.lineSeparator());
		
		for (Entry<String, Map<String, String>> item : schemaMap.entrySet()) {
			int size = item.getValue().size();
			
			log.debug(item.getKey() + " " + item.getValue());
			
			if (size > 0) {
				strKey = item.getKey();
				structName = strKey.substring(strKey.indexOf(":") + 1);
				
				// TODO: Check for dups here
				
				sb.append("struct " + structName + " { " + System.lineSeparator());
				
				int num = 1;
				for (Map.Entry<String, String> elem : item.getValue().entrySet()) {
					this.makeLine(elem, num);
					num++;
					
					if (num == size + 1) {
						sb.append("} " + System.lineSeparator() + System.lineSeparator());
					} 
				}
			}
			
		}
		String sThrift = sb.toString();
		log.debug(sThrift);
		this.writeThiftFile(sThrift, filePath);
	}
	
	private boolean setIncludes(LinkedHashMap<String, Map<String, String>> schemaMap) {
		String fileName = "";
		int endIndex = 0;
		
		Map<String, String> includes = schemaMap.get("includes");
		
		if (includes != null) {
			schemaMap.remove("includes");
			
			for (Entry<String, String> item : includes.entrySet()) { 
				if (item.getKey() != null) {
					fileName = new File(item.getValue()).getName();
				}
				
				if (!fileName.isEmpty()) {
					endIndex = fileName.indexOf(".");
					if (endIndex > 0) {
						fileName = fileName.substring(0, endIndex);
					}
					
					sb.append("include \"" + fileName + ".thrift" + System.lineSeparator());
				}
				
			}
		}
		return true;
	}
	
	
	private boolean makeLine(Map.Entry<String, String> elem, int num) { 
		String name = "", type = "";
		
		if (elem.getKey() == null) {
			return false;
		}
		
		name = elem.getKey();
		int index = elem.getValue().indexOf(":") + 1;
		type = elem.getValue().substring(index);
		
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
		} 
		if (name == null || name.isEmpty() || name.equalsIgnoreCase("null")) {
			return false;
		}
		sb.append(" " + num + ": " + type + " " + name + ";\n");
		
		return true;
	}

	private boolean writeThiftFile(String strThrift, String filePath) {
		File file = new File(filePath);
		
		if (!file.exists()) {
		    log.warn("Overwriting thrift file");
		}
		
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
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

}
