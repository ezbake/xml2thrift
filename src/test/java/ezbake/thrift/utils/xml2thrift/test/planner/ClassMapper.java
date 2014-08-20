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

import java.util.Map;

import org.apache.thrift.TBase;

import com.google.common.collect.Maps;

import ezbake.thrift.utils.xml2thrift.callback.IHandlerCallback;
import ezbake.thrift.utils.xml2thrift.test.books.Book;
import ezbake.thrift.utils.xml2thrift.test.books.BooksTBaseCallback;
import ezbake.thrift.utils.xml2thrift.test.books.Catalog;
import ezbake.thrift.utils.xml2thrift.test.planner.Tester.Subject;
import ezbake.thrift.utils.xml2thrift.test.wikipedia.Contributor;
import ezbake.thrift.utils.xml2thrift.test.wikipedia.Page;
import ezbake.thrift.utils.xml2thrift.test.wikipedia.Revision;
import ezbake.thrift.utils.xml2thrift.test.wikipedia.WikiTBaseCallback;

public class ClassMapper { 

	private IHandlerCallback<Map<String, Object>> callback;
	private Map<String, Class<? extends TBase<?,?>>> classes = Maps.newHashMap();
	
	public ClassMapper(Subject subject) { 
		this.loadMap(subject);
		this.loadCallback(subject);
	}
	
	public Map<String, Class<? extends TBase<?,?>>> getMap() { 
		return classes;
	}
	
	public IHandlerCallback<Map<String, Object>> getTBaseCallback() {
		return callback;
	}
	
	private void loadMap(Subject subject) { 

		switch (subject) {
			case WIKIPEDIA:
				this.getWikipedia();
				break;
			case BOOKS:
				this.getBooks();
			default:
				break;
		}
	}
	
	private void getWikipedia() { 
		classes.clear();
		classes.put("page", Page.class);
		classes.put("revision", Revision.class);
		classes.put("contributor", Contributor.class);
	}
	
	private void getBooks() {
		classes.clear();
		classes.put("book", Book.class);
		classes.put("catalog", Catalog.class);
	}

	private void loadCallback(Subject subject) { 

		switch (subject) {
			case WIKIPEDIA:
				this.getWikipedia();
				callback = new WikiTBaseCallback();
				break;
			case BOOKS:
				this.getBooks();
				callback = new BooksTBaseCallback();
			default:
				break;
		}
	}

}
