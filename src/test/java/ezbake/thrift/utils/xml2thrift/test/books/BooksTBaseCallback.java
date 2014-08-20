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

package ezbake.thrift.utils.xml2thrift.test.books;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;

public class BooksTBaseCallback implements IHandlerCallback<Map<String, Object>> {

	private static final Logger log = LoggerFactory.getLogger(BooksTBaseCallback.class);
	
	@Override
	public void getOutput(Map<String, Object> result) {
		
		Book book = (Book) result.get("book");
		
		log.info("Book - ID: " + book.getId());
		log.info("Book - Number: " + book.getNumber());
		log.info("Book - Author: " + book.getAuthor());
		log.info("Book - Title: " + book.getTitle());
		log.info("Book - Genre: " + book.getGenre());
		log.info("Book - Price: " + book.getPrice());
		log.info("Book - Publish_date: " + book.getPublish_date());
		log.info("Book - Description: " + book.getDescription());
		
		assertEquals("Book - ID: ", "bk101", book.getId());
		assertEquals("Book - Number: ", "1", book.getNumber());
		assertEquals("Book - Author: ", "Gambardella, Matthew", book.getAuthor());
		assertEquals("Book - Title: ", "XML Developer's Guide", book.getTitle());
		assertEquals("Book - Genre: ", "Computer", book.getGenre());
		assertEquals("Book - Price: ", "44.95", book.getPrice());
		assertEquals("Book - Publish_date: ", "2000-10-01", book.getPublish_date());
		assertEquals("Book - Description: ", "An in-depth look at creating applications with XML.", book.getDescription());	
		
		log.info("TIMESTAMP: " + new java.util.Date().toString());
	}

}
