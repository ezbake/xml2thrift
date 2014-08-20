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

/**
 * The base interface for all Handler callback implementations.<br />
 * This interface should be implemented to retrieve output from the Handler. It returns each XML entry from Handler in {@link #getOutput(Object) } method. <br /> <br />
 */
public interface IHandlerCallback<T> { 
	
	/**
	* Acceptable types of parameters: T
	* <ul><li>StringHandler: String</li>
	* <li>TBaseHandler: {@literal Map<String, Object>}</li>
	* <li>XSDHandler: {@literal LinkedHashMap<String, Map<String, String>>}</li>
	* <li>GenericHandler: {@literal Map<String, ArrayList<Map<String, String>>}</li>
	* </ul><br />
	* @param result Based on handler type. It will be one of types above.
	*/
	public void getOutput(T result);
}
