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

package ezbake.thrift.utils.xml2thrift.callback;

import java.util.Map;

/** This is a convenience class. It is also possible to implement {@link IHandlerCallback } directly. <br />
 * This class can be extended as a callback for {@link ezbake.thrift.utils.xml2thrift.sax.TBaseHandler } and {@link ezbake.thrift.utils.xml2thrift.sax.ThriftHandler }. <br />
 * Returns each XML entry from parser in {@link #getOutput(Map) } method
 */
public abstract class TBaseCallback implements IHandlerCallback<Map<String, Object>> {

	/**
	* @param result {@literal Map<String, Object>}
	*/
	@Override
	public abstract void getOutput(Map<String, Object> result);
}
