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

package ezbake.thrift.utils.xml2thrift.sax;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ezbake.thrift.utils.xml2thrift.schema.SchemaHelper;
import ezbake.thrift.utils.xml2thrift.schema.SchemaMapper;
import ezbake.thrift.utils.xml2thrift.schema.SchemaTagInfo;
import ezbake.thrift.utils.xml2thrift.schema.SchemaTyper;
import ezbake.thrift.utils.xml2thrift.util.XMLUtil;

/* Valid output > time performance */

public class SchemaHandler extends DefaultHandler {

	private SchemaMapper mapper = new SchemaMapper();
	private SchemaTyper typer = new SchemaTyper();
	private static final Logger log = LoggerFactory.getLogger(SchemaHandler.class);
	
	public void initialize(String outputFilePath, String namespace) { 
		mapper.setOutputFile(outputFilePath);
		mapper.setNamespace(namespace);
	}
	
	/**
	* This is an inherited SAX method. The handler extends it to parse XML.
	*/
	public void startDocument() throws SAXException {
	    log.debug("Starting document parsing.");
		
		File file = new File(mapper.getOutputFile());
		
		if (file.exists()) {
			file.delete();
		}
		
		XMLUtil.appendFile(mapper.getThriftNamespace(), mapper.getOutputFile());
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void startElement(String uri, String localName, String qName, Attributes attributes) { 
	    log.debug("startElement: " + qName);
		
		mapper.getStack().push(qName);
		
		if (!qName.isEmpty()) {
			typer.parseType(qName, attributes, mapper);
		}
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void characters(char[] ch, int start, int length)  { } 
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void endElement(String uri, String localName, String qName)  { 
	    log.debug("endElement: " + qName);
		
		mapper.getStack().pop();
		
		// to insert struct end
		if (qName.toLowerCase().contains("attributegroup")) { 
			typer.parseType(qName, null, mapper);
			mapper.getSchemaTagInfo().setNumber(0);
		} else if (qName.toLowerCase().contains("attribute")) { 
		} else if (qName.toLowerCase().contains("all")) { 
			typer.parseType(qName, null, mapper);
		} else if (qName.toLowerCase().contains("choice")) { 
			typer.parseType(qName, null, mapper);
		} else if (qName.toLowerCase().contains("enumeration")) { 
			typer.parseType(qName, null, mapper);
		} else if (qName.toLowerCase().contains("simpletype")) { 
			typer.parseType(qName, null, mapper);
		} else if (qName.toLowerCase().contains("sequence")) { 
			typer.parseType(qName, null, mapper);
		} 
	}
	
	/**
    * This is an inherited SAX method. The handler extends it to parse XML.
    */
	public void endDocument() { 
		for (SchemaTagInfo line : mapper.getLines()) { 
			String out = "";
			
			if (line.getType().contains("include")) { 
				SchemaHelper.appendFile(line.getName() + System.getProperty("line.separator"), mapper.getOutputFile());
				log.info(line.getName());
				continue;
			}
			
			if (line.getType().equalsIgnoreCase("struct")) { 
				out = "struct " + line.getName() + " { ";
				SchemaHelper.appendFile(out, mapper.getOutputFile());
				log.info(out);
				continue;
			}
			
			if (line.getType().equalsIgnoreCase("endstruct")) { 
				out = line.getName() + System.getProperty("line.separator");
				SchemaHelper.appendFile(out, mapper.getOutputFile());
				log.info(out);
				continue;
			}
			
			if (line.getType().equalsIgnoreCase("enum")) { 
				out = "enum " + line.getName() + " { ";
				SchemaHelper.appendFile(out, mapper.getOutputFile());
				log.info(out);
				
				SchemaTagInfo enumAttr = (SchemaTagInfo) mapper.getData().get(line.getName().toLowerCase());
				
				int num = enumAttr.getEnumValues().size();
				for (int i = 0; i < num; i++) { 
					String val = enumAttr.getEnumValues().poll();
					if (i == num - 1) {
						SchemaHelper.appendFile(" " + val.toUpperCase(), mapper.getOutputFile());
					} else {
						SchemaHelper.appendFile(" " + val.toUpperCase() + ",", mapper.getOutputFile());
					}
				}
				
				out = "} " + System.getProperty("line.separator");
				SchemaHelper.appendFile(out, mapper.getOutputFile());
				log.info(out);
				continue;
			}
			
			SchemaTagInfo attr = (SchemaTagInfo) mapper.getData().get(line.getName().toLowerCase());
			if (attr != null) { 
				if (attr.getFixed() != null && !attr.getFixed().isEmpty()) { 
					out = " " + line.getNumber() + ": " + line.getUse() + " " + attr.getType() + " " + attr.getName() + " = " + attr.getFixed() + ";";
				} else {
					out = " " + line.getNumber() + ": " + line.getUse() + " " + attr.getType() + " " + attr.getName() + ";";
				}
				
				SchemaHelper.appendFile(out, mapper.getOutputFile());
				log.info(out);
				continue;
			}
			
			if (line.getName() != null && !line.getName().isEmpty()) {
				out = " " + line.getNumber() + ": " + line.getUse() + " " + line.getType() + " " + line.getName() + ";";
				SchemaHelper.appendFile(out, mapper.getOutputFile());
				log.info(out);
				continue;
			}
			
		}
		
		log.debug("Completed document parsing.");
	}
}
