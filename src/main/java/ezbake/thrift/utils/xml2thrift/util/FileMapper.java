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

package ezbake.thrift.utils.xml2thrift.util;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.thrift.TBase;
import com.google.common.collect.Maps;

/**
 *
 */
public class FileMapper { 
	
	private static Map<String, Class<? extends TBase<?,?>>> classMap = Maps.newHashMap();
	private static final Logger log = LoggerFactory.getLogger(FileMapper.class);
 
	public static Map<String, Class<? extends TBase<?,?>>> getFiles(String path, String directory, String packageName, boolean addNamespace) throws ClassNotFoundException { 
    	
    	File dir = new File(path);
    	String parent = "", dirName = directory, className = "";

    	File[] files = dir.listFiles();
    	log.debug("Directory: Found {} files in [{}] directory.", files.length, dir.getName());
    	
    	for (File file : files) { 
    		if (file.isDirectory()) { 
    			dirName = file.getName();
    		    parent = getParentDir(file);
    			
    			if (!parent.equalsIgnoreCase("thrift")) { 
    				dirName = parent + "." + dirName;
    			} 
    			getFiles(file.getAbsolutePath(), dirName, packageName, addNamespace);
    			dirName = "";
    		} else if (file.isFile() && file.getName().endsWith(".java")) {
    			log.debug("FileName: " + file.getName());
    			
    			parent = getParentDir(file);
    			int pos = dirName.lastIndexOf(".") + 1; 
    			if (pos > 0) {
    				String name = dirName.substring(pos, dirName.length());
        			if (!parent.equalsIgnoreCase(name)) {
        				dirName = dirName.substring(0, dirName.lastIndexOf("."));
        			}
    			}
    			
    			String nsName = dirName;
    			if (!dirName.isEmpty()) {
    				nsName = "." + dirName;
    			}

    			className = packageName + nsName.trim() + "." + file.getName().substring(0, file.getName().lastIndexOf("."));
    			Class<? extends TBase<?, ?>> clazz = (Class<? extends TBase<?, ?>>) Class.forName(className);
    			String id = file.getName().toLowerCase().substring(0, file.getName().toLowerCase().lastIndexOf("."));
    			
    			// For duplicates 
    			if (addNamespace) {
    				id = dirName.trim() + ":" + id;
    			}
    			
    			classMap.put(id, clazz);	
    		}
    	}	
    	log.info("Loaded: {} classes to classmap from directory [{}]", files.length, dir.getName()); 
    	return classMap;
    }
    
    public static String getParentDir(File file) {
    	int pos = file.getParentFile().toString().toLowerCase().lastIndexOf(File.separator) + 1;
		int len = (int) file.getParentFile().toString().length();
		return file.getParentFile().toString().toLowerCase().substring(pos, len);
    }

}
