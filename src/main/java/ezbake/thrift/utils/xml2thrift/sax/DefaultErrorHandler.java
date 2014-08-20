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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The default error handler for SAX handlers.
 */
class DefaultErrorHandler implements ErrorHandler {
	
	private boolean logWarning = false;
	private String msg  = "";
	private static final Logger log = LoggerFactory.getLogger(DefaultErrorHandler.class);
	
	public void setLogWarnings(boolean isLogged) {
		logWarning = isLogged;
	}
	
	public void warning(SAXParseException exception) { 
		if (logWarning) { 
			msg = this.makeErrorMessage(exception);
			log.warn("SAX Warning " + msg);
		}
	}

	public void error(SAXParseException exception) {
		msg = this.makeErrorMessage(exception);
		log.error("SAX Error " + msg);
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		msg = this.makeErrorMessage(exception);
		log.error("SAX Fatal Error " + msg);
	}
	
	private String makeErrorMessage(SAXParseException exception) {
		StringBuilder message = new StringBuilder();
		message.append("\nMessage:\t" + exception.getMessage());
		message.append("\nLine number:\t" + exception.getLineNumber());
		message.append("\nColumn number:\t" + exception.getColumnNumber());
		return message.toString();
	}
}