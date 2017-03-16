/*===============================================================================
 * Copyright (c) 2010-2012 University of Massachusetts.  All Rights Reserved.
 *
 * Use of the RankLib package is subject to the terms of the software license set 
 * forth in the LICENSE file included with this software, and also available at
 * http://people.cs.umass.edu/~vdang/ranklib_license.html
 *===============================================================================
 */

package com.horsehour.ml.rank.ranklib;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.horsehour.ml.rank.ranklib.RankList;

/**
 * @author vdang
 * 
 *         This class implements the generic Ranker interface. Each ranking
 *         algorithm implemented has to extend this class.
 */
public class Ranker {
	public static boolean verbose = true;

	protected List<RankList> samples = new ArrayList<RankList>();// training
																 // samples
	protected int[] features = null;// all the features used for training
	protected MetricScorer scorer = null;
	protected double scoreOnTrainingData = 0.0;
	protected double bestScoreOnValidationData = 0.0;

	protected List<RankList> validationSamples = null;
	protected List<RankList> testSamples = null;

	public Ranker() {

	}

	public Ranker(List<RankList> samples, int[] features) {
		this.samples = samples;
		this.features = features;
	}

	// Utility functions
	public void set(List<RankList> samples, int[] features) {
		this.samples = samples;
		this.features = features;
	}

	public void setValidationSet(List<RankList> samples) {
		this.validationSamples = samples;
	}

	public void setTestSet(List<RankList> samples) {
		this.testSamples = samples;
	}

	public void set(MetricScorer scorer) {
		this.scorer = scorer;
	}

	public double getScoreOnTrainingData() {
		return scoreOnTrainingData;
	}

	public double getScoreOnValidationData() {
		return bestScoreOnValidationData;
	}

	public int[] getFeatures() {
		return features;
	}

	/**
	 * 预测分值的同时对数据集重新排列
	 * 
	 * @param rl
	 * @return
	 */
	public RankList rank(RankList rl) {
		double[] scores = new double[rl.size()];
		for (int i = 0; i < rl.size(); i++)
			scores[i] = eval(rl.get(i));// 预测样本分值
		int[] idx = MergeSorter.sort(scores, false);
		return new RankList(rl, idx);// 对样本按照分值降序排列
	}

	public List<RankList> rank(List<RankList> l) {
		List<RankList> ll = new ArrayList<RankList>();
		for (int i = 0; i < l.size(); i++)
			ll.add(rank(l.get(i)));
		return ll;
	}

	/**
	 * 保存模型到本地文件
	 * 
	 * @param modelFile
	 */
	public void save(String modelFile) {
		FileUtils.write(modelFile, "ASCII", model());
	}

	public void PRINT(String msg) {
		if (verbose)
			System.out.print(msg);
	}

	public void PRINTLN(String msg) {
		if (verbose)
			System.out.println(msg);
	}

	public void PRINT(int[] len, String[] msgs) {
		if (verbose) {
			for (int i = 0; i < msgs.length; i++) {
				String msg = msgs[i];
				if (msg.length() > len[i])
					msg = msg.substring(0, len[i]);
				else
					while (msg.length() < len[i])
						msg += " ";
				System.out.print(msg + " | ");
			}
		}
	}

	public void PRINTLN(int[] len, String[] msgs) {
		PRINT(len, msgs);
		PRINTLN("");
	}

	public void PRINTTIME() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
	}

	public void PRINT_MEMORY_USAGE() {
		System.out.println("***** " + Runtime.getRuntime().freeMemory() + " / "
		        + Runtime.getRuntime().maxMemory());
	}

	protected void copy(double[] source, double[] target) {
		for (int j = 0; j < source.length; j++)
			target[j] = source[j];
	}

	/**
	 * HAVE TO BE OVER-RIDDEN IN SUB-CLASSES
	 */
	/***
	 * 初始化
	 */
	public void init() {
	}

	/**
	 * 学习
	 */
	public void learn() {
	}

	/**
	 * 模型评估数据点
	 * 
	 * @param p
	 * @return
	 */
	public double eval(DataPoint p) {
		return -1.0;
	}

	public Ranker clone() {
		return null;
	}

	public String toString() {
		return "[Not yet implemented]";
	}

	public String model() {
		return "[Not yet implemented]";
	}

	public void load(String fn) {
	}

	public void printParameters() {
	}

	public String name() {
		return "";
	}
}
