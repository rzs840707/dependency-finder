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

package com.jeantessier.classreader;

import java.io.*;
import java.util.*;

public abstract class ClassfileLoaderDecorator extends ClassfileLoader {
	private ClassfileLoader loader;
	
	public ClassfileLoaderDecorator(ClassfileLoader loader) {
		this.loader = loader;
	}

	protected ClassfileLoader Loader() {
		return loader;
	}
	
	public Classfile Classfile(String name) {
		return Loader().Classfile(name);
	}

	public Collection Classfiles() {
		return Loader().Classfiles();
	}

	public Collection Classnames() {
		return Loader().Classnames();
	}

	public void addLoadListener(LoadListener listener) {
		Loader().addLoadListener(listener);
	}

	public void removeLoadListener(LoadListener listener) {
		Loader().removeLoadListener(listener);
	}

	protected void fireBeginSession() {
		Loader().fireBeginSession();
	}
	
	protected void fireBeginGroup(String group_name, int size) {
		Loader().fireBeginGroup(group_name, size);
	}
	
	protected void fireBeginFile(String filename) {
		Loader().fireBeginFile(filename);
	}
	
	protected void fireBeginClassfile(String filename) {
		Loader().fireBeginClassfile(filename);
	}
	
	protected void fireEndClassfile(String filename, Classfile classfile) {
		Loader().fireEndClassfile(filename, classfile);
	}
	
	protected void fireEndFile(String filename) {
		Loader().fireEndFile(filename);
	}
	
	protected void fireEndGroup(String group_name) {
		Loader().fireEndGroup(group_name);
	}

	protected void fireEndSession() {
		Loader().fireEndSession();
	}
	
	protected Classfile Load(DataInputStream in) throws IOException {
		return Loader().Load(in);
	}
}
