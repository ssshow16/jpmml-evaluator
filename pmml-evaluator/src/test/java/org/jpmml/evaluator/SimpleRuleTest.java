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

import org.dmg.pmml.*;

import org.junit.*;

import static org.junit.Assert.*;

public class SimpleRuleTest extends RuleSelectionMethodTest {

	@Test
	public void evaluate() throws Exception {
		assertEquals("RULE1", getRuleId(RuleSelectionMethod.Criterion.FIRST_HIT));
		assertEquals("RULE2", getRuleId(RuleSelectionMethod.Criterion.WEIGHTED_SUM));
		assertEquals("RULE1", getRuleId(RuleSelectionMethod.Criterion.WEIGHTED_MAX));
	}
}