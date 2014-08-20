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

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;
import ezbake.thrift.utils.xml2thrift.sax.GenericParser;
import ezbake.thrift.utils.xml2thrift.sax.StringHandler;

public class StringTester implements IHandlerCallback<String> { 

	private static final Logger LOGGER = LoggerFactory.getLogger(StringTester.class);
	
	public void run(String element, String xmlFile) throws Exception { 
		
		StringHandler handler = new StringHandler();
		handler.initialize(element, this);
		
		GenericParser parser = new GenericParser(handler);
		
	    long startTime = System.currentTimeMillis(); 
	    parser.processFile(xmlFile); 
	    long endTime   = System.currentTimeMillis();
	    
	    long totalTime = endTime - startTime;
	    LOGGER.info("Total Time: " + totalTime + " milliseconds");	
	}
	
	public void run(String element, InputStream inputStream) throws Exception { 
		
		StringHandler handler = new StringHandler();
		handler.initialize(element, this);
		
		GenericParser parser = new GenericParser(handler);
		
	    long startTime = System.currentTimeMillis(); 
	    parser.processFile(inputStream); 
	    long endTime   = System.currentTimeMillis();
	    
	    long totalTime = endTime - startTime;
	    LOGGER.info("Total Time: " + totalTime + " milliseconds");
	}
	
	@Override
	public void getOutput(String result) { 
		LOGGER.info(result);
	}
}
