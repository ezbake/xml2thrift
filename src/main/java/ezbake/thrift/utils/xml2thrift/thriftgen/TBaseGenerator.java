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

package ezbake.thrift.utils.xml2thrift.thriftgen;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NOT IMPLEMENTED
 */
public class TBaseGenerator {

	private String thriftFile = "", outDir = "", thriftPath = "", line = "";
	private StringBuilder sb = new StringBuilder();
	private static final Logger log = LoggerFactory.getLogger(TBaseGenerator.class);
	
	public TBaseGenerator(String fileNamePath, String outputDir, String thriftAppPath) {
		thriftFile = fileNamePath;
		outDir = outputDir;
		thriftPath = thriftAppPath;
	}
	
	@SuppressWarnings("unused")
	private boolean run() {
		try {
			File file = new File(thriftFile);
			
			List<String> command = new ArrayList<String>();
	        command.add(thriftPath);
	        command.add(" -out ");
	        command.add(outDir);
	        command.add(" --gen ");
	        command.add("java ");
	        command.add(file.getAbsolutePath());

	        ProcessBuilder builder = new ProcessBuilder(command);
	        builder.redirectErrorStream(true);
			
			if (file.exists()) {
			    log.info("CMD: \n"+ command.get(0) + command.get(1) +  command.get(2) + command.get(3) + command.get(4) + command.get(5) + "\n");
				Process proc = builder.start(); 

				InputStream stdout = proc.getInputStream ();
				BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));

				while ((line = reader.readLine ()) != null) {
				    sb.append(line + "\n");
				}
				log.info("STD OUT: \n "+ sb.toString() +"\n");
				log.info("Done...");
			}
		} catch (IOException e) { 
		    log.error("Error generating thrift file: " + e);
		}
		return true;
	}
}
