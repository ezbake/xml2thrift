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

package ezbake.thrift.utils.xml2thrift.test.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ezbake.thrift.utils.xml2thrift.sax.GenericParser;
import ezbake.thrift.utils.xml2thrift.sax.SchemaHandler;
import ezbake.thrift.utils.xml2thrift.sax.XSDHandler;
import ezbake.thrift.utils.xml2thrift.thriftgen.IDLGenerator;

public class SchemaTester { 

	private static final Logger LOGGER = LoggerFactory.getLogger(SchemaTester.class);
	
	public void runXSDToXML(String xsdFile, String outputFile) throws Exception { 
		
		IDLGenerator tester = new IDLGenerator(outputFile, "ezbake.thrift.utils.");
		XSDHandler handler = new XSDHandler();
		handler.initialize(tester); 
		
	    GenericParser parser = new GenericParser(handler);
	    
	    long startTime = System.currentTimeMillis();
	    parser.processFile(xsdFile); 
	    long endTime   = System.currentTimeMillis();
	    
	    long totalTime = endTime - startTime;
	    LOGGER.info("Total Time: " + totalTime + " milliseconds");
	}
	
	public void runSchemaToXML(String xsdFile, String outputFile, String namespace) throws Exception { 
        
        IDLGenerator tester = new IDLGenerator(outputFile, "ezbake.thrift.utils.");
        SchemaHandler handler = new SchemaHandler();
        handler.initialize(outputFile, namespace); 
        
        GenericParser parser = new GenericParser(handler);
        
        long startTime = System.currentTimeMillis();
        parser.processFile(xsdFile); 
        long endTime   = System.currentTimeMillis();
        
        long totalTime = endTime - startTime;
        LOGGER.info("Total Time: " + totalTime + " milliseconds");
    }

}
