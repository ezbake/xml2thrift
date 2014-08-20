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

package ezbake.thrift.utils.xml2thrift.test.planner;

import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ezbake.thrift.utils.xml2thrift.test.planner.Tester.Subject;
import ezbake.thrift.utils.xml2thrift.test.xml.GenericTester;
import ezbake.thrift.utils.xml2thrift.test.xml.SchemaTester;
import ezbake.thrift.utils.xml2thrift.test.xml.StringTester;
import ezbake.thrift.utils.xml2thrift.test.xml.TBaseTester;

public class TestPlanner {

	private ClassMapper mapper = null;
	private URL url = null;
	private String startElement = "", xmlFile = "", xsdFile = "", outputFile = "";
	private static final Logger log = LoggerFactory.getLogger(TestPlanner.class);
	
	public enum Type { 
	    FILETBASE, FILESTRING, STREAMTBASE, STREAMSTRING, XSD, GENERIC
	}
	
	public void initialize(Subject subject) {
		this.loadProperties(subject);
		this.mapper = new ClassMapper(subject);
	}
	
	public void runFileTBase() {
		this.run(Type.FILETBASE);
	}
	
	public void runFileString() {
		this.run(Type.FILESTRING);
	}
	
	public void runStreamTBase() {
		this.run(Type.STREAMTBASE);
	}
	
	public void runStreamString() {
		this.run(Type.STREAMSTRING);
	}
	
	public void runXSD() {
		this.run(Type.XSD);
	}
	
	public void runGeneric() {
		this.run(Type.GENERIC);
	}
	
	private void run(Type type) { 
		
		InputStream fis = null;
		
		try {
			
			switch(type) {
				case FILETBASE: 
					log.info("STARTING FILE BASED TBASE TEST");
					url = getClass().getClassLoader().getResource(xmlFile);
					TBaseTester tbaseTester = new TBaseTester();
					tbaseTester.run(startElement, url.getPath(), mapper); 
					log.info("END TBASE TEST" + System.getProperty("line.separator"));
					break;
				case FILESTRING: 
					log.info("STARTING FILE BASED STRING TEST");
					url = getClass().getClassLoader().getResource(xmlFile);
					StringTester stringTester = new StringTester();
					stringTester.run(startElement, url.getPath());
					log.info("END STRING TEST" + System.getProperty("line.separator"));
					break;
				case STREAMTBASE: 
					log.info("STARTING INPUTSTREAM BASED TBASE TEST");
					url = getClass().getClassLoader().getResource(xmlFile);
					TBaseTester streamTBaseTester = new TBaseTester();
					fis = new FileInputStream(url.getPath());
					streamTBaseTester.run(startElement, fis, mapper); 
					log.info("END INPUTSTREAM BASED TBASE TEST" + System.getProperty("line.separator"));
					break;
				case STREAMSTRING: 
					log.info("STARTING INPUTSTREAM BASED STRING TEST");
					url = getClass().getClassLoader().getResource(xmlFile);
					StringTester streamStringTester = new StringTester();
					fis = new FileInputStream(url.getPath());
					streamStringTester.run(startElement, fis);
					log.info("END INPUTSTREAM BASED STRING TEST" + System.getProperty("line.separator"));
					break;
				case XSD: 
					log.info("STARTING XSD TEST");
					url = getClass().getClassLoader().getResource(xsdFile);
					if (this.url == null) { 
						log.info("BYPASSING XSD TEST - no XSD file to parse.");
						log.info("END XSD TEST" + System.getProperty("line.separator"));
					} else {
						SchemaTester schemaTester = new SchemaTester();
						schemaTester.runXSDToXML(url.getPath(), outputFile);					
						schemaTester.runSchemaToXML(url.getPath(), outputFile, "ezbake.thrift.utils.xmlparser");
						log.info("END XSD TEST" + System.getProperty("line.separator"));
					}		
					break;
				case GENERIC: 
					log.info("STARTING GENERIC TEST");
					url = getClass().getClassLoader().getResource(xmlFile);
					GenericTester genericTester = new GenericTester();
					genericTester.run(startElement, url.getPath(), mapper);
					log.info("END GENERIC TEST" + System.getProperty("line.separator"));
					break;
			}
		} catch (Exception ex){
			log.error("TestPlaner.run - " + ex);
		}
	}
	
	/**
	*  Load properties and populate configuration <br />
	*  Loaded from <b>xmlparser.properties</b> properties file <br />
	*/
	private void loadProperties(Subject subject) { 
		Properties prop = new Properties();
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream(subject.getName()));
			
			startElement = prop.getProperty("startElement");
			xmlFile = prop.getProperty("xmlFile");
			xsdFile = prop.getProperty("xsdFile");
			outputFile = prop.getProperty("outputFile");
			
		} catch (IOException e) { 
			log.error("Error loading properties ", e);
		} catch (Exception ex) {
			log.error("Error loading properties ", ex);
		}
	}
}
