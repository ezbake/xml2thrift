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
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.thrift.TBase;

import com.google.common.collect.Maps;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;
import ezbake.thrift.utils.xml2thrift.sax.GenericHandler;
import ezbake.thrift.utils.xml2thrift.sax.GenericParser;
import ezbake.thrift.utils.xml2thrift.test.planner.ClassMapper;
import ezbake.thrift.utils.xml2thrift.util.TStructDescriptor;
import ezbake.thrift.utils.xml2thrift.util.TStructDescriptor.Field;

public class GenericTester implements IHandlerCallback<Map<String, ArrayList<Map<String, String>>>> { 

	private static final Logger log = LoggerFactory.getLogger(GenericTester.class);
	
	public void run(String element, String xmlFile, ClassMapper mapper) throws Exception { 
		
		Map<String, List<TStructDescriptor.Field>> fieldMap =  Maps.newHashMap();
		Map<String, Class<? extends TBase<?,?>>> classes = Maps.newHashMap();
		classes = mapper.getMap();
		
		TStructDescriptor desc = null;
		List<Field> fieldList = new ArrayList<Field>();
		
		for (Class<? extends TBase<?, ?>> value : classes.values()) {
			desc = TStructDescriptor.getInstance(value);
			fieldList = desc.getFields();
			fieldMap.put(desc.getThriftClass().getName(), fieldList);
		}
		
		GenericHandler handler = new GenericHandler();
		
		handler.initialize(element, fieldMap, this);
		
		GenericParser parser = new GenericParser(handler);
		
		long startTime = System.currentTimeMillis();
	    parser.processFile(xmlFile); 
	    long endTime   = System.currentTimeMillis();
	    
	    long totalTime = endTime - startTime;
	    log.info("Total Time: " + totalTime + " milliseconds");
	}

	@Override
	public void getOutput(Map<String, ArrayList<Map<String, String>>> result) {
		for (Entry<String, ArrayList<Map<String, String>>> innerMap : result.entrySet()) {
			String className = innerMap.getKey();
			ArrayList<Map<String, String>> list = innerMap.getValue();
			
			for (Map<String, String> elem : list) { 
			    log.info("Class = " + className);
				for (Map.Entry<String, String> item : elem.entrySet()) {
					/* Populate thrift object here or other processing */
					if (item.getKey() != null && !item.getValue().isEmpty()) {
					    log.info("Key = " + item.getKey() + ", Value = " + item.getValue()); 
					} 
				}
			}
		}
		
	}
}
