/*
 *  Copyright (c) 2001-2002, Jean Tessier
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  
 *  	* Redistributions of source code must retain the above copyright
 *  	  notice, this list of conditions and the following disclaimer.
 *  
 *  	* Redistributions in binary form must reproduce the above copyright
 *  	  notice, this list of conditions and the following disclaimer in the
 *  	  documentation and/or other materials provided with the distribution.
 *  
 *  	* Neither the name of the Jean Tessier nor the names of his contributors
 *  	  may be used to endorse or promote products derived from this software
 *  	  without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jeantessier.metrics;

import junit.framework.*;

import java.io.*;
import java.util.*;

import org.xml.sax.*;

import com.jeantessier.classreader.*;

public class TestMetricsGathererDependencies extends TestCase {
	public static final String TEST_CLASS    = "test.TestClass";
	public static final String TEST_DIRNAME  = "classes" + File.separator + "test";
	public static final String OTHER_DIRNAME = "classes" + File.separator + "other";

	private MetricsFactory factory;
	
	public TestMetricsGathererDependencies(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		factory = new MetricsFactory("test", new MetricsConfigurationLoader(Boolean.getBoolean("DEPENDENCYFINDER_TESTS_VALIDATE")).Load("etc" + File.separator + "MetricsConfig.xml"));

		DirectoryClassfileLoader loader = new DirectoryClassfileLoader(new AggregatingClassfileLoader());
		loader.Load(new DirectoryExplorer(TEST_DIRNAME));
		loader.Load(new DirectoryExplorer(OTHER_DIRNAME));

		loader.Classfile(TEST_CLASS).Accept(new MetricsGatherer("test", factory));
	}
	
	public void test_TestClass_TestMethod() {
		Collection dependencies;

		dependencies = ((AccumulatorMeasurement) factory.CreateMethodMetrics("test.TestClass.TestMethod(java.lang.String)").Measurement(Metrics.INBOUND_INTRA_CLASS_METHOD_DEPENDENCIES)).Values();
		assertEquals(Metrics.INBOUND_INTRA_CLASS_METHOD_DEPENDENCIES, 0, dependencies.size());

		dependencies = ((AccumulatorMeasurement) factory.CreateMethodMetrics("test.TestClass.TestMethod(java.lang.String)").Measurement(Metrics.INBOUND_INTRA_PACKAGE_METHOD_DEPENDENCIES)).Values();
		assertEquals(Metrics.INBOUND_INTRA_PACKAGE_METHOD_DEPENDENCIES, 0, dependencies.size());

		dependencies = ((AccumulatorMeasurement) factory.CreateMethodMetrics("test.TestClass.TestMethod(java.lang.String)").Measurement(Metrics.INBOUND_EXTRA_PACKAGE_METHOD_DEPENDENCIES)).Values();
		assertEquals(Metrics.INBOUND_EXTRA_PACKAGE_METHOD_DEPENDENCIES, 0, dependencies.size());

		dependencies = ((AccumulatorMeasurement) factory.CreateMethodMetrics("test.TestClass.TestMethod(java.lang.String)").Measurement(Metrics.OUTBOUND_INTRA_CLASS_FEATURE_DEPENDENCIES)).Values();
		assertEquals(Metrics.OUTBOUND_INTRA_CLASS_FEATURE_DEPENDENCIES, 0, dependencies.size());

		dependencies = ((AccumulatorMeasurement) factory.CreateMethodMetrics("test.TestClass.TestMethod(java.lang.String)").Measurement(Metrics.OUTBOUND_INTRA_PACKAGE_FEATURE_DEPENDENCIES)).Values();
		assertEquals(Metrics.OUTBOUND_INTRA_PACKAGE_FEATURE_DEPENDENCIES, 0, dependencies.size());

		dependencies = ((AccumulatorMeasurement) factory.CreateMethodMetrics("test.TestClass.TestMethod(java.lang.String)").Measurement(Metrics.OUTBOUND_INTRA_PACKAGE_CLASS_DEPENDENCIES)).Values();
		assertEquals(Metrics.OUTBOUND_INTRA_PACKAGE_CLASS_DEPENDENCIES, 0, dependencies.size());

		dependencies = ((AccumulatorMeasurement) factory.CreateMethodMetrics("test.TestClass.TestMethod(java.lang.String)").Measurement(Metrics.OUTBOUND_EXTRA_PACKAGE_FEATURE_DEPENDENCIES)).Values();
		assertEquals(Metrics.OUTBOUND_EXTRA_PACKAGE_FEATURE_DEPENDENCIES, 1, dependencies.size());
		assertTrue(Metrics.OUTBOUND_EXTRA_PACKAGE_FEATURE_DEPENDENCIES + " " + dependencies + "missing java.lang.Object.Object()", dependencies.contains("java.lang.Object.Object()"));

		dependencies = ((AccumulatorMeasurement) factory.CreateMethodMetrics("test.TestClass.TestMethod(java.lang.String)").Measurement(Metrics.OUTBOUND_EXTRA_PACKAGE_CLASS_DEPENDENCIES)).Values();
		assertEquals(Metrics.OUTBOUND_EXTRA_PACKAGE_CLASS_DEPENDENCIES, 0, dependencies.size());
	}
	
	public void test_TestClass() {
		Collection dependencies;

		dependencies = ((AccumulatorMeasurement) factory.CreateClassMetrics("test.TestClass").Measurement(Metrics.INBOUND_INTRA_PACKAGE_DEPENDENCIES)).Values();
		assertEquals(Metrics.INBOUND_INTRA_PACKAGE_DEPENDENCIES, 0, dependencies.size());
		
		dependencies = ((AccumulatorMeasurement) factory.CreateClassMetrics("test.TestClass").Measurement(Metrics.INBOUND_EXTRA_PACKAGE_DEPENDENCIES)).Values();
		assertEquals(Metrics.INBOUND_EXTRA_PACKAGE_DEPENDENCIES, 0, dependencies.size());
		
		dependencies = ((AccumulatorMeasurement) factory.CreateClassMetrics("test.TestClass").Measurement(Metrics.OUTBOUND_INTRA_PACKAGE_DEPENDENCIES)).Values();
		assertEquals(Metrics.OUTBOUND_INTRA_PACKAGE_DEPENDENCIES, 0, dependencies.size());
		
		dependencies = ((AccumulatorMeasurement) factory.CreateClassMetrics("test.TestClass").Measurement(Metrics.OUTBOUND_EXTRA_PACKAGE_DEPENDENCIES)).Values();
		assertEquals(Metrics.OUTBOUND_EXTRA_PACKAGE_DEPENDENCIES, 1, dependencies.size());
		assertTrue(Metrics.OUTBOUND_EXTRA_PACKAGE_DEPENDENCIES + " " + dependencies + "missing java.lang.Object", dependencies.contains("java.lang.Object"));
	}
}
