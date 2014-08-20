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

import org.junit.Test;

public class Tester {

	public enum Subject {
	    WIKIPEDIA("wikipediaTest.properties"), BOOKS("booksTest.properties"), SCHEMA("schemaTest.properties");
	    
	    private String subject;

	    Subject(String subject) {
	        /* Change "Subject" to run a specific test */
	        this.subject = subject;
	    }

	    public String getName() {
	        return subject;
	    }
	}
	
	@Test
	public void runWIKIPEDIA() {
		TestPlanner planner = new TestPlanner();
		planner.initialize(Subject.WIKIPEDIA);
		planner.runGeneric();
		planner.runXSD();
		planner.runFileString();
		planner.runFileTBase();
		planner.runStreamString();
		planner.runStreamTBase();
	}
	
	@Test
	public void runBOOKS() {
		TestPlanner planner = new TestPlanner();
		planner.initialize(Subject.BOOKS);
		planner.runGeneric();
		planner.runXSD();
		planner.runFileString();
		planner.runFileTBase();
		planner.runStreamString();
		planner.runStreamTBase();
	}
	
	@Test
    public void runSCHEMA() {
        TestPlanner planner = new TestPlanner();
        planner.initialize(Subject.SCHEMA);
        planner.runXSD();
    }
}
