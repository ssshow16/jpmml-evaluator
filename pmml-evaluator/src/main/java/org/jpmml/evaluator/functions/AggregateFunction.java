/*
 * Copyright (c) 2014 Villu Ruusmann
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
package org.jpmml.evaluator.functions;

import java.util.*;

import org.jpmml.evaluator.*;
import org.jpmml.evaluator.FieldValue;

import org.apache.commons.math3.stat.descriptive.*;

import org.dmg.pmml.*;

abstract
public class AggregateFunction extends AbstractFunction {

	abstract
	public StorelessUnivariateStatistic createStatistic();

	public DataType getResultType(DataType dataType){
		return dataType;
	}

	@Override
	public FieldValue evaluate(List<FieldValue> values){
		StorelessUnivariateStatistic statistic = createStatistic();

		DataType dataType = null;

		for(FieldValue value : values){

			// "Missing values in the input to an aggregate function are simply ignored"
			if(value == null){
				continue;
			}

			statistic.increment((value.asNumber()).doubleValue());

			if(dataType != null){
				dataType = TypeUtil.getResultDataType(dataType, value.getDataType());
			} else

			{
				dataType = value.getDataType();
			}
		}

		if(statistic.getN() == 0){
			throw new MissingResultException(null);
		}

		Object result = cast(getResultType(dataType), statistic.getResult());

		return FieldValueUtil.create(result);
	}
}