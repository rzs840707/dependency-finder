/*
 *  Copyright (c) 2001-2004, Jean Tessier
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
 *  	* Neither the name of Jean Tessier nor the names of his contributors
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

public class TestMeasurementDescriptor extends TestCase {
	private MeasurementDescriptor descriptor;
	
	protected void setUp() {
		descriptor = new MeasurementDescriptor();
	}
	
	public void testCreate() {
		assertNull("descriptor.ShortName() not initialized to null", descriptor.ShortName());
		assertNull("descriptor.LongName() not initialized to null", descriptor.LongName());
		assertNull("descriptor.Class() not initialized to null", descriptor.Class());
	}

	public void testShortName() {
		assertNull("descriptor.ShortName() not initialized to null", descriptor.ShortName());
		descriptor.ShortName("foo");
		assertEquals("descriptor.ShortName()", "foo", descriptor.ShortName());
	}
	
	public void testLongName() {
		assertNull("descriptor.LongName() not initialized to null", descriptor.LongName());
		descriptor.LongName("bar");
		assertEquals("descriptor.LongName()", "bar", descriptor.LongName());
	}

	public void testNonExistingClass() {
		assertNull("descriptor.Class() not initialized to null", descriptor.Class());
		try {
			descriptor.Class("nop such class");
			fail("set class to non-existing class");
		} catch (ClassNotFoundException ex) {
			// Do nothing
		}
	}

	public void testNullClass() {
		assertNull("descriptor.Class() not initialized to null", descriptor.Class());
		try {
			descriptor.Class((Class) null);
			fail("set class to null");
		} catch (IllegalArgumentException ex) {
			// Do nothing
		}
	}
	
	public void testClass() {
		assertNull("descriptor.Class() not initialized to null", descriptor.Class());
		descriptor.Class(com.jeantessier.metrics.CounterMeasurement.class);
		assertEquals("descriptor.Class()", com.jeantessier.metrics.CounterMeasurement.class, descriptor.Class());
	}

	public void testClassByName() throws ClassNotFoundException {
		descriptor.Class(com.jeantessier.metrics.CounterMeasurement.class.getName());
		assertEquals("descriptor.Class()", com.jeantessier.metrics.CounterMeasurement.class, descriptor.Class());
	}
}
