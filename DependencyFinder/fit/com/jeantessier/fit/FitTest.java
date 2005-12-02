/*
 *  Copyright (c) 2001-2005, Jean Tessier
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of Jean Tessier nor the names of his contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
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

package com.jeantessier.fit;

import java.io.*;
import java.util.*;

import junit.framework.*;

import fit.*;

public class FitTest extends TestCase {
    private File inFile;
    private File outFile;

    public FitTest(File inFile, File outFile) {
        super(inFile.getName());

        this.inFile = inFile;
        this.outFile = outFile;
    }

    protected void runTest() throws Throwable {
        Fixture fixture = new Fixture();
        fixture.summary.put("input file", inFile.getAbsolutePath());
        fixture.summary.put("input update", new Date(inFile.lastModified()));
        fixture.summary.put("output file", outFile.getAbsolutePath());
        String input = read(inFile);
        PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
        Parse tables;
        if (input.indexOf("<wiki>") >= 0) {
            tables = new Parse(input, new String[]{"wiki", "table", "tr", "td"});
            fixture.doTables(tables.parts);
        } else {
            tables = new Parse(input, new String[]{"table", "tr", "td"});
            fixture.doTables(tables);
        }
        tables.print(output);
        output.close();

        assertEquals("count wrongs(" + fixture.counts.wrong + ") + exceptions(" + fixture.counts.exceptions + ")", 0, fixture.counts.wrong + fixture.counts.exceptions);
    }

    protected String read(File input) throws IOException {
        char chars[] = new char[(int) (input.length())];
        FileReader in = new FileReader(input);
        in.read(chars);
        in.close();
        return new String(chars);
    }
}
