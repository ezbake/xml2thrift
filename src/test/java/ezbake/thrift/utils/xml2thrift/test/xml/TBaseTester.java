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

import java.util.Map;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.thrift.TBase;

import com.google.common.collect.Maps;

import ezbake.thrift.utils.xml2thrift.sax.GenericParser;
import ezbake.thrift.utils.xml2thrift.sax.TBaseHandler;
import ezbake.thrift.utils.xml2thrift.test.planner.ClassMapper;

public class TBaseTester { 
	
	private static final Logger log = LoggerFactory.getLogger(TBaseTester.class);
	
	public void run(String element, String xmlFile, ClassMapper mapper) throws Exception { 
		
		Map<String, Class<? extends TBase<?,?>>> classes = Maps.newHashMap();
		classes = mapper.getMap();
		
		TBaseHandler handler = new TBaseHandler();
		handler.initialize(element, mapper.getTBaseCallback(), classes);
		handler.locateChildren(true);

		GenericParser parser = new GenericParser(handler);
		long startTime = System.currentTimeMillis();
	    parser.processFile(xmlFile); 
	    long endTime   = System.currentTimeMillis();
	    
	    long totalTime = endTime - startTime;
	    log.info("Total Time: " + totalTime + " milliseconds");
	}
	
	public void run(String element, InputStream inputStream, ClassMapper mapper) throws Exception { 
		
		Map<String, Class<? extends TBase<?,?>>> classes = Maps.newHashMap();
		classes = mapper.getMap();
		
		TBaseHandler handler = new TBaseHandler();
		handler.initialize(element, mapper.getTBaseCallback(), classes);
		handler.locateChildren(true);
		
		GenericParser parser = new GenericParser(handler);
		long startTime = System.currentTimeMillis();
	    parser.processFile(inputStream);
	    long endTime   = System.currentTimeMillis();
	    
	    long totalTime = endTime - startTime;
	    log.info("Total Time: " + totalTime + " milliseconds");
	}
}
