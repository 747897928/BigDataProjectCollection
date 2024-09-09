package sparkMLHomeWork

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.{HashingTF, IndexToString, PCA, PCAModel, StringIndexer, Tokenizer, VectorIndexer}
import org.apache.spark.sql.Row
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.classification.{BinaryLogisticRegressionSummary, LogisticRegression}
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder}
import org.apache.spark.sql.functions
import org.apache.spark.sql.SparkSession
import org.junit.Test;
/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/7/12 19:42
 */
class SparkMLHomeWork{
  Logger.getLogger("org").setLevel(Level.ERROR)
  val spark=SparkSession.builder().master("local").appName("test").getOrCreate();
  val sc=spark.sparkContext;
  import spark.implicits._
  val dataFrame=sc.textFile("adult/adult.data").map(_.split(","))
    .map(x =>{
      Adult(Vectors.dense(
        x(0).toDouble,
        x(2).toDouble,
        x(4).toDouble,
        x(10).toDouble,
        x(11).toDouble,
        x(12).toDouble),
        x(14).toString())
    }
    ).toDF();

  val testDataFrame=sc.textFile("adult/adult.test")
    .map(_.split(","))
    .map(x =>{
      Adult(Vectors.dense(
        x(0).toDouble,
        x(2).toDouble,
        x(4).toDouble,
        x(10).toDouble,
        x(11).toDouble,
        x(12).toDouble),
        x(14).toString().dropRight(1)
      )
    })
    .toDF();
  val pca = new PCA().
    setInputCol("features").
    setOutputCol("pcaFeatures").
    setK(3).fit(dataFrame)
  val result = pca.transform(dataFrame)
  val testdata = pca.transform(testDataFrame)
  @Test
  def test01(): Unit ={
    println(dataFrame);
    println(testDataFrame);
    testDataFrame.show(false)
  }
  @Test
  def test02(): Unit ={
    result.show(false)
    println("*******************")
    testdata.show(false)
  }
  @Test
  def test03(): Unit ={
    val labelIndexer = new
        StringIndexer().setInputCol("label").setOutputCol("indexedLabel").fit(result)
    labelIndexer.labels.foreach(println)
    val featureIndexer = new
        VectorIndexer().setInputCol("pcaFeatures").setOutputCol("indexedFeatures").fit(result)
    println(featureIndexer.numFeatures)
    val labelConverter = new
        IndexToString().setInputCol("prediction").setOutputCol("predictedLabel").setLabels(labelIndexer.
      labels)
    val lr = new LogisticRegression().setLabelCol("indexedLabel").setFeaturesCol("indexedFeatures").setMaxIter(100)
    val lrPipeline = new Pipeline().setStages(Array(labelIndexer, featureIndexer, lr,
      labelConverter))
    val lrPipelineModel = lrPipeline.fit(result)
    val lrModel = lrPipelineModel.stages(2).asInstanceOf[LogisticRegressionModel]
    println("Coefficients: " + lrModel.coefficientMatrix+"Intercept:"+lrModel.interceptVector+"numClasses: "+lrModel.numClasses+"numFeatures:"+lrModel.numFeatures)
    val lrPredictions = lrPipelineModel.transform(testdata)
    val evaluator = new
        MulticlassClassificationEvaluator().setLabelCol("indexedLabel").setPredictionCol("prediction")
    val lrAccuracy = evaluator.evaluate(lrPredictions)
    println("Test Error = " + (1.0 - lrAccuracy))
  }
  @Test
  def test04(): Unit ={
    val pca = new PCA().setInputCol("features").setOutputCol("pcaFeatures");
    val labelIndexer = new StringIndexer().setInputCol("label")
      .setOutputCol("indexedLabel").fit(dataFrame);
    val featureIndexer = new
        VectorIndexer().setInputCol("pcaFeatures").setOutputCol("indexedFeatures")
    val labelConverter = new IndexToString().setInputCol("prediction").
      setOutputCol("predictedLabel").setLabels(labelIndexer.labels);
    val lr = new LogisticRegression().setLabelCol("indexedLabel")
      .setFeaturesCol("indexedFeatures").setMaxIter(100);
    val lrPipeline = new Pipeline().setStages(Array(pca,
      labelIndexer, featureIndexer, lr, labelConverter))
    val paramGrid = new ParamGridBuilder().addGrid(pca.k,
      Array(1,2,3,4,5,6)).addGrid(lr.elasticNetParam, Array(0.2,0.8)).addGrid(lr.regParam, Array(0.01,
      0.1, 0.5)).build()
    val cv = new CrossValidator().setEstimator(lrPipeline).setEvaluator(new
        MulticlassClassificationEvaluator().setLabelCol("indexedLabel").
      setPredictionCol("prediction")).setEstimatorParamMaps(paramGrid).setNumFolds(3)
    val cvModel = cv.fit(dataFrame);
    val lrPredictions=cvModel.transform(testDataFrame);
    val evaluator = new MulticlassClassificationEvaluator().
      setLabelCol("indexedLabel").setPredictionCol("prediction");
    val lrAccuracy = evaluator.evaluate(lrPredictions);
    println("准确率为"+lrAccuracy);
    val bestModel= cvModel.bestModel.asInstanceOf[PipelineModel];
    val lrModel = bestModel.stages(3).asInstanceOf[LogisticRegressionModel];
    println("Coefficients: " + lrModel.coefficientMatrix + "Intercept:"+lrModel.interceptVector+ "numClasses: "+lrModel.numClasses+"numFeatures:"+lrModel.numFeatures)
    val pcaModel = bestModel.stages(0).asInstanceOf[PCAModel]
    println("Primary Component: " + pcaModel.pc)
  }
}
