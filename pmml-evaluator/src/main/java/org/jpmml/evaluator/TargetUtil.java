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

import org.jpmml.manager.*;

import org.dmg.pmml.*;

import com.google.common.collect.*;

public class TargetUtil {

	private TargetUtil(){
	}

	static
	public Map<FieldName, ? extends Number> evaluateRegression(Double value, ModelEvaluationContext context){
		ModelManager<?> modelManager = context.getModelManager();

		return evaluateRegression(Collections.singletonMap(modelManager.getTargetField(), value), context);
	}

	/**
	 * Evaluates the {@link Targets} element for {@link MiningFunctionType#REGRESSION regression} models.
	 */
	static
	public Map<FieldName, ? extends Number> evaluateRegression(Map<FieldName, ? extends Number> predictions, ModelEvaluationContext context){
		ModelManager<?> modelManager = context.getModelManager();

		Targets targets = modelManager.getTargets();
		if(targets == null || Iterables.isEmpty(targets)){
			return predictions;
		}

		Map<FieldName, Number> result = Maps.newLinkedHashMap();

		Collection<? extends Map.Entry<FieldName, ? extends Number>> entries = predictions.entrySet();
		for(Map.Entry<FieldName, ? extends Number> entry : entries){
			FieldName key = entry.getKey();
			Number value = entry.getValue();

			Target target = modelManager.getTarget(key);
			if(target != null){

				if(value != null){
					value = process(target, entry.getValue());
				} else

				{
					value = getDefaultValue(target);
				}
			}

			result.put(key, value);
		}

		return result;
	}

	static
	public Map<FieldName, ? extends ClassificationMap<?>> evaluateClassification(ClassificationMap<?> value, ModelEvaluationContext context){
		ModelManager<?> modelManager = context.getModelManager();

		return evaluateClassification(Collections.singletonMap(modelManager.getTargetField(), value), context);
	}

	/**
	 * Evaluates the {@link Targets} element for {@link MiningFunctionType#CLASSIFICATION classification} models.
	 */
	static
	public Map<FieldName, ? extends ClassificationMap<?>> evaluateClassification(Map<FieldName, ? extends ClassificationMap<?>> predictions, ModelEvaluationContext context){
		ModelManager<?> modelManager = context.getModelManager();

		Targets targets = modelManager.getTargets();
		if(targets == null || Iterables.isEmpty(targets)){
			return predictions;
		}

		Map<FieldName, ClassificationMap<?>> result = Maps.newLinkedHashMap();

		Collection<? extends Map.Entry<FieldName, ? extends ClassificationMap<?>>> entries = predictions.entrySet();
		for(Map.Entry<FieldName, ? extends ClassificationMap<?>> entry : entries){
			FieldName key = entry.getKey();
			ClassificationMap<?> value = entry.getValue();

			Target target = modelManager.getTarget(key);
			if(target != null){

				if(value == null){
					value = getPriorProbabilities(target);
				}
			}

			result.put(key, value);
		}

		return result;
	}

	static
	public Number process(Target target, Number value){
		double result = value.doubleValue();

		Double min = target.getMin();
		if(min != null){
			result = Math.max(result, min.doubleValue());
		}

		Double max = target.getMax();
		if(max != null){
			result = Math.min(result, max.doubleValue());
		}

		result = (result * target.getRescaleFactor() + target.getRescaleConstant());

		Target.CastInteger castInteger = target.getCastInteger();
		if(castInteger == null){
			return result;
		}

		switch(castInteger){
			case ROUND:
				return (int)Math.round(result);
			case CEILING:
				return (int)Math.ceil(result);
			case FLOOR:
				return (int)Math.floor(result);
			default:
				throw new UnsupportedFeatureException(target, castInteger);
		}
	}

	static
	public TargetValue getTargetValue(Target target, Object value){
		DataType dataType = TypeUtil.getDataType(value);

		List<TargetValue> targetValues = target.getTargetValues();
		for(TargetValue targetValue : targetValues){

			if(TypeUtil.equals(dataType, value, TypeUtil.parseOrCast(dataType, targetValue.getValue()))){
				return targetValue;
			}
		}

		return null;
	}

	static
	private Double getDefaultValue(Target target){
		List<TargetValue> values = target.getTargetValues();
		if(values.size() != 1){
			throw new InvalidFeatureException(target);
		}

		TargetValue value = values.get(0);
		if(value.getValue() != null || value.getPriorProbability() != null){
			throw new InvalidFeatureException(value);
		}

		return value.getDefaultValue();
	}

	static
	private DefaultClassificationMap<String> getPriorProbabilities(Target target){
		DefaultClassificationMap<String> result = new DefaultClassificationMap<String>();

		List<TargetValue> values = target.getTargetValues();
		for(TargetValue value : values){

			if(value.getDefaultValue() != null){
				throw new InvalidFeatureException(value);
			}

			result.put(value.getValue(), value.getPriorProbability());
		}

		return result;
	}
}