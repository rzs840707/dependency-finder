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

import java.util.*;

import com.jeantessier.classreader.*;

public class MetricsEvent extends EventObject {
	private Classfile   classfile;
	private Method_info method;
	private Metrics     metrics;
	
	public MetricsEvent(Object source, Classfile classfile) {
		this(source, classfile, null, null);
	}
	
	public MetricsEvent(Object source, Classfile classfile, Metrics metrics) {
		this(source, classfile, null, metrics);
	}
		
	public MetricsEvent(Object source, Method_info method) {
		this(source, method.Classfile(), method, null);
	}
		
	public MetricsEvent(Object source, Method_info method, Metrics metrics) {
		this(source, method.Classfile(), method, metrics);
	}

	public MetricsEvent(Object source, Classfile classfile, Method_info method, Metrics metrics) {
		super(source);

		this.classfile = classfile;
		this.method    = method;
		this.metrics   = metrics;
	}

	public Classfile Classfile() {
		return classfile;
	}

	public Method_info Method() {
		return method;
	}

	public Metrics Metrics() {
		return metrics;
	}
}