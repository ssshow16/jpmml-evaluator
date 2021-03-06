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
package org.jpmml.evaluator;

import java.util.*;

import org.dmg.pmml.*;

import org.junit.*;

import static org.junit.Assert.*;

public class ContrastMatrixTest extends GeneralRegressionModelEvaluatorTest {

	@Test
	public void evaluate() throws Exception {
		GeneralRegressionModelEvaluator evaluator = createEvaluator();

		Map<FieldName, ?> arguments = createArguments("gender", "f", "educ", 19d, "jobcat", "3", "salbegin", 45000d);

		Map<FieldName, ?> result = evaluator.evaluate(arguments);

		Number probabilityLow = (Number)result.get(new FieldName("Probability_Low"));
		Number probabilityHigh = (Number)result.get(new FieldName("Probability_High"));

		// Expected values have been calculated by hand
		assertTrue(VerificationUtil.acceptable(0.81956470d, probabilityLow));
		assertTrue(VerificationUtil.acceptable(0.18043530d, probabilityHigh));
	}
}