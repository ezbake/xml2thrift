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

package ezbake.thrift.utils.xml2thrift.test.wikipedia;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;
import ezbake.thrift.utils.xml2thrift.test.wikipedia.Contributor;
import ezbake.thrift.utils.xml2thrift.test.wikipedia.Page;
import ezbake.thrift.utils.xml2thrift.test.wikipedia.Revision;

public class WikiTBaseCallback implements IHandlerCallback<Map<String, Object>> {

	private static final Logger log = LoggerFactory.getLogger(WikiTBaseCallback.class);
	
	@Override
	public void getOutput(Map<String, Object> result) {
		try { 
			
			Page p = (Page) result.get("page");
			log.info("Page - Title: " + p.getTitle());
			log.info("Page - NS: " + p.getNs());
			log.info("Page - ID: " + p.getId());
			
			assertEquals("Page - Title: ", "Barrister", p.getTitle());
			assertEquals("Page - NS: ", "0", p.getNs());
			assertEquals("Page - ID: ", "4848", p.getId());
			
			Revision r = p.getRevision();
			log.info("Revision - ID: " + r.getId());
			log.info("Revision - ParentID: " + r.getParentId());
			log.info("Revision - Timestamp: " + r.getTimestamp());
			log.info("Revision - Comment: " + r.getComment());
			log.info("Revision - Text: " + r.getText().substring(0, 200)); // Shortening to 200 chars
			log.info("Revision - SHA1: " + r.getSha1());
			log.info("Revision - Format: " + r.getFormat());
			
			assertEquals("Revision - ID: ", "562558019", r.getId());
			assertEquals("Revision - ParentID: ", "561807616",  r.getParentId());
			assertEquals("Revision - Comment: ", "2013-07-02T15:29:41Z",  r.getTimestamp());
			assertEquals("Revision - Timestamp: ", "/* Differences */clean up, replaced: century English → -century English using [[Project:AWB|AWB]]",  r.getComment());
			assertEquals("Revision - SHA1: ", "04nrsrqtrdxzw9l6qf3x955fu4ao5o1", r.getSha1());
			assertEquals("Revision - Format: ", "text/x-wiki", r.getFormat());

	 		Contributor c = r.getContributor(); 
	 		log.info("Contributor - ID: " + c.getId());
	 		log.info("Contributor - Username: " + c.getUsername());
	 		
	 		assertEquals("Contributor - ID: " , "16460235", c.getId());
	 		assertEquals("Contributor - Username: ", "Khazar2" , c.getUsername());
	 		
	 		log.info("TIMESTAMP: " + new java.util.Date().toString());
	 		
		} catch (Exception ex){
			log.error("getOutput - " + ex.getMessage());
		}
	}

}
