/*
 * Copyright (c) 2013 Villu Ruusmann
 *
 * This file is part of JPMML-Evaluator
 *
 * JPMML-Evaluator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JPMML-Evaluator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with JPMML-Evaluator.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jpmml.manager;

import org.dmg.pmml.*;

import com.sun.xml.bind.*;

import org.xml.sax.*;

abstract
public class PMMLException extends RuntimeException {

	private PMMLObject context = null;


	public PMMLException(){
		super();
	}

	public PMMLException(String message){
		super(message);
	}

	public PMMLException(PMMLObject context){
		super();

		setContext(context);
	}

	public PMMLException(String message, PMMLObject context){
		super(message);

		setContext(context);
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());

		Locatable locatable = getContext();
		if(locatable != null){
			int lineNumber = -1;

			Locator locator = locatable.sourceLocation();
			if(locator != null){
				lineNumber = locator.getLineNumber();
			}

			if(lineNumber != -1){
				sb.append(" ").append("(at or around line ").append(lineNumber).append(")");
			}
		}

		String message = getLocalizedMessage();
		if(message != null){
			sb.append(":");

			sb.append(" ").append(message);
		}

		return sb.toString();
	}

	public PMMLObject getContext(){
		return this.context;
	}

	private void setContext(PMMLObject context){
		this.context = context;
	}
}