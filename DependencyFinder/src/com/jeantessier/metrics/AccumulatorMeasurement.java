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

import java.io.*;
import java.util.*;

import org.apache.log4j.*;

/**
 *  <p>Accumulates entries, filtering with regular
 *  expressions.  If no regular expressions are given, matches
 *  everything for the given measurement, which must implement
 *  the <code>CollectionMeasurement</code> interface.  Regular
 *  expressions matching using <code>Perl5Util</code> from
 *  Jakarta-ORO.  This measurement will use
 *  <code>Perl5Util.group(1)</code> if not null, or else the
 *  full string.</p>
 *
 *  <p>This is the syntax for initializing this type of
 *  measurement:</p>
 *  
 *  <pre>
 *  &lt;init&gt;
 *      measurement name [perl regular expression]
 *      ...
 *  &lt;/init&gt;
 *  </pre>
 */
public abstract class AccumulatorMeasurement extends MeasurementBase implements CollectionMeasurement {
	private Map        terms  = new HashMap();
	private Collection values = new TreeSet();

	public AccumulatorMeasurement(MeasurementDescriptor descriptor, Metrics context, String init_text) {
		super(descriptor, context, init_text);

		if (init_text != null) {
			try {
				BufferedReader in   = new BufferedReader(new StringReader(init_text));
				String         line;
				
				while ((line = in.readLine()) != null) {
					synchronized (Perl()) {
						if (Perl().match("/^\\s*(\\S+)\\s*(.*)/", line)) {
							String name = Perl().group(1);
							String re   = Perl().group(2);

							Collection res = (Collection) terms.get(name);
							if (res == null) {
								res = new ArrayList();
								terms.put(name, res);
							}

							if (re != null && re.length() > 0) {
								res.add(re);
							}
						}
					}
				}
				
				in.close();
			} catch (Exception ex) {
				Logger.getLogger(getClass()).debug("Cannot initialize with \"" + init_text + "\"", ex);
				terms.clear();
			}
		}

		LogTerms(init_text);
	}

	private void LogTerms(String init_text) {
		Logger.getLogger(getClass()).debug("Initialize with\n" + init_text);
		Logger.getLogger(getClass()).debug("Terms:");

		Iterator i = terms.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			Logger.getLogger(getClass()).debug("\t" + entry.getKey());

			Iterator j = ((Collection) entry.getValue()).iterator();
			while (j.hasNext()) {
				Logger.getLogger(getClass()).debug("\t\t" + j.next());
			}
		}
	}

	public Number Value() {
		return new Integer(Values().size());
	}

	public boolean Empty() {
		return Values().isEmpty();
	}
	
	protected double Compute() {
		return Values().size();
	}

	public Collection Values() {
		if (!Cached()) {
			values.clear();
			
			PopulateValues();

			Cached(true);
		}
		
		return Collections.unmodifiableCollection(values);
	}

	protected abstract void PopulateValues();

	protected void FilterMetrics(Metrics metrics) {
		Iterator i = terms.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry  entry = (Map.Entry)  i.next();
			String     name  = (String)     entry.getKey();
			Collection res   = (Collection) entry.getValue();
		
			Measurement measurement = metrics.Measurement(name);
			if (measurement instanceof CollectionMeasurement) {
				FilterMeasurement((CollectionMeasurement) measurement, res);
			}
		}
	}
	
	private void FilterMeasurement(CollectionMeasurement measurement, Collection res) {
		if (res.isEmpty()) {
			values.addAll(measurement.Values());
		} else {
			Iterator i = measurement.Values().iterator();
			while (i.hasNext()) {
				FilterElement((String) i.next(), res);
			}
		}
	}
	
	private void FilterElement(String element, Collection res) {
		boolean found = false;
		Iterator i = res.iterator();
		while (!found && i.hasNext()) {
			found = EvaluateRE((String) i.next(), element);
		}
	}
	
	private boolean EvaluateRE(String re, String element) {
		boolean result = false;

		synchronized (Perl()) {
			if (Perl().match(re, element)) {
				result = true;
				if (Perl().group(1) != null) {
					values.add(Perl().group(1));
				} else {
					values.add(element);
				}
			}
		}

		return result;
	}
}
