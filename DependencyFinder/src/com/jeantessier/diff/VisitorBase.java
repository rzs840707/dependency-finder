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

package com.jeantessier.diff;

public abstract class VisitorBase implements Visitor {
	private int deprecatable_level = 0;
	private int documentable_level = 0;
	
	private boolean deprecated[]   = new boolean[4];
	private boolean undeprecated[] = new boolean[4];
	private boolean documented[]   = new boolean[4];
	private boolean undocumented[] = new boolean[4];

	private void RaiseDeprecatableLevel() {
		deprecatable_level++;
	}

	private void LowerDeprecatableLevel() {
		deprecatable_level--;
	}

	private void RaiseDocumentableLevel() {
		documentable_level++;
	}

	private void LowerDocumentableLevel() {
		documentable_level--;
	}
	
	public boolean Deprecated() {
		return deprecated[deprecatable_level];
	}

	public void Deprecated(boolean deprecated) {
		this.deprecated[deprecatable_level] = deprecated;
	}
	
	public boolean Undeprecated() {
		return undeprecated[deprecatable_level];
	}

	public void Undeprecated(boolean undeprecated) {
		this.undeprecated[deprecatable_level] = undeprecated;
	}

	public boolean Documented() {
		return documented[documentable_level];
	}

	public void Documented(boolean documented) {
		this.documented[documentable_level] = documented;
	}
	
	public boolean Undocumented() {
		return undocumented[documentable_level];
	}

	public void Undocumented(boolean undocumented) {
		this.undocumented[documentable_level] = undocumented;
	}
	
	public void VisitJarDifferences(JarDifferences differences) {
		// Do nothing
	}
		
	public void VisitPackageDifferences(PackageDifferences differences) {
		// Do nothing
	}

	public void VisitFieldDifferences(FieldDifferences differences) {
		// Do nothing
	}
	
	public void VisitConstructorDifferences(ConstructorDifferences differences) {
		// Do nothing
	}

	public void VisitMethodDifferences(MethodDifferences differences) {
		// Do nothing
	}

	public void VisitDeprecatableDifferences(DeprecatableDifferences differences) {
		RaiseDeprecatableLevel();
		
		Deprecated(differences.NewDeprecation());
		Undeprecated(differences.RemovedDeprecation());

		differences.Component().Accept(this);
		
		LowerDeprecatableLevel();
	}
	
	public void VisitDocumentableDifferences(DocumentableDifferences differences) {
		RaiseDocumentableLevel();
		
		Documented(differences.NewDocumentation());
		Undocumented(differences.RemovedDocumentation());

		differences.Component().Accept(this);
		
		LowerDocumentableLevel();
	}
}
