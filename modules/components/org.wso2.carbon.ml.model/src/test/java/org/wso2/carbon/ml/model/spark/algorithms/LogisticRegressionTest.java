/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.ml.model.spark.algorithms;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.carbon.ml.model.spark.dto.ProbabilisticClassificationModelSummary;
import org.wso2.carbon.ml.model.spark.transformations.HeaderFilter;
import org.wso2.carbon.ml.model.spark.transformations.LineToTokens;
import org.wso2.carbon.ml.model.spark.transformations.MeanImputation;
import org.wso2.carbon.ml.model.spark.transformations.DoubleArrayToLabeledPoint;

import java.util.HashMap;
import java.util.regex.Pattern;

public class LogisticRegressionTest {

    @Test
    public void testModelTrainingAndTesting() throws Exception {
        SparkConf conf = new SparkConf().setAppName("logisticRegressionTest").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile("src/test/resources/pIndiansDiabetes.csv");
        Pattern pattern = Pattern.compile(",");
        LineToTokens lineToTokens = new LineToTokens(pattern);
        String headerRow = lines.take(1).get(0);
        HeaderFilter headerFilter = new HeaderFilter(headerRow);
        JavaRDD<String> data = lines.filter(headerFilter);
        JavaRDD<String[]> tokens = data.map(lineToTokens);
        DoubleArrayToLabeledPoint doubleArrayToLabeledPoint = new DoubleArrayToLabeledPoint(8);
        MeanImputation meanImputation = new MeanImputation(new HashMap<Integer, Double>());
        JavaRDD<LabeledPoint> labeledPoints = tokens.map(meanImputation).map(doubleArrayToLabeledPoint);
        JavaRDD<LabeledPoint> trainingData = labeledPoints.sample(false, 0.7, 11L);
        JavaRDD<LabeledPoint> testingData = labeledPoints.subtract(trainingData);
        LogisticRegression logisticRegression = new LogisticRegression();
        LogisticRegressionModel model = logisticRegression.trainWithSGD(trainingData, 0.01, 100,
                "L1", 0.001, 1.0);
        model.clearThreshold();
        ProbabilisticClassificationModelSummary modelSummary = SparkModelUtils
                .generateProbabilisticClassificationModelSummary(logisticRegression.test(model, testingData));
        Assert.assertEquals(modelSummary.getAuc(), 0.54, 0.01);
        sc.stop();
    }
}
