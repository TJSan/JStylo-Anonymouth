package edu.purdue.jsan.tools;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import weka.classifiers.Evaluation;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelWriter {

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputFile;
	private File file;
	private WritableWorkbook workbook;
	private WorkbookSettings wbSettings;
	private Evaluation resArr[][];


	public ExcelWriter(String inputFile){
		try {
			setOutputFile(inputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setOutputFile(String inputFile) throws IOException {
		this.inputFile = inputFile;
		this.file = new File(inputFile);
		this.wbSettings = new WorkbookSettings();
		this.wbSettings.setLocale(new Locale("en", "EN"));
		this.workbook = Workbook.createWorkbook(file, wbSettings);
	}

	public void write(String nameOfSheet, Evaluation [][]arr) throws Exception {
		resArr=arr;
		workbook.createSheet(nameOfSheet, 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createLabel(excelSheet);
		createContent(excelSheet);
		workbook.write();
		workbook.close();
	}

	private void createLabel(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);

		// Write a few headers
		addCaption(sheet, 0, 0, "Authors quantaty");
		addCaption(sheet, 1, 0, "No");
		addCaption(sheet, 2, 0, "numInstances");
		addCaption(sheet, 3, 0, "correct");
		addCaption(sheet, 4, 0, "incorrect");
		addCaption(sheet, 5, 0, "errorRate");
		addCaption(sheet, 6, 0, "Mean absolute error");
		addCaption(sheet, 7, 0, "Root mean squared error ");
		addCaption(sheet, 8, 0, "Relative absolute error ");
		addCaption(sheet, 9, 0, "Root relative squared error  ");
		addCaption(sheet, 11, 0, "(Weighted Avg) Tp Rate");
		addCaption(sheet, 12, 0, "(Weighted Avg) FP Rate");
		addCaption(sheet, 13, 0, "(Weighted Avg) Precision");
		addCaption(sheet, 14, 0, "(Weighted Avg) Recall");
		addCaption(sheet, 15, 0, "(Weighted Avg) F-measure");
		addCaption(sheet, 16, 0, "(Weighted Avg) ROC Area");
		

	}

	private void createContent(WritableSheet sheet) throws Exception {
		for(int row=0;row<resArr.length;row++){
			for(int col=0;col<resArr[row].length;col++){
				int rowNum=col*8+(row+1);
				//if (rowNum>2) break;
				//int rowNum=col+1;
				addNumber(sheet, 0, rowNum, (row+1)*5);//Authors quantaty
				addNumber(sheet, 1, rowNum, rowNum);
				addLabel(sheet, 2, rowNum, Double.toString(resArr[row][col].numInstances()));
				addLabel(sheet, 3, rowNum, Double.toString(resArr[row][col].correct()));
				addLabel(sheet, 4, rowNum, Double.toString(resArr[row][col].incorrect()));
				addLabel(sheet, 5, rowNum, Double.toString(resArr[row][col].errorRate()));
				addLabel(sheet, 6, rowNum, Double.toString(resArr[row][col].meanAbsoluteError()));
				addLabel(sheet, 7, rowNum, Double.toString(resArr[row][col].rootMeanSquaredError()));
				addLabel(sheet, 8, rowNum, Double.toString(resArr[row][col].relativeAbsoluteError()));
				addLabel(sheet, 9, rowNum, Double.toString(resArr[row][col].rootRelativeSquaredError()));
				
				addLabel(sheet, 11, rowNum, Double.toString(resArr[row][col].weightedTruePositiveRate()));
				addLabel(sheet, 12, rowNum, Double.toString(resArr[row][col].weightedFalsePositiveRate()));
				addLabel(sheet, 13, rowNum, Double.toString(resArr[row][col].weightedPrecision()));
				addLabel(sheet, 14, rowNum, Double.toString(resArr[row][col].weightedRecall()));
				addLabel(sheet, 15, rowNum, Double.toString(resArr[row][col].weightedFMeasure()));
				addLabel(sheet, 16, rowNum, Double.toString(resArr[row][col].weightedAreaUnderROC()));
			}
		}
		/*
		// Lets calculate the sum of it
		StringBuffer buf = new StringBuffer();
		buf.append("SUM(A2:A10)");
		Formula f = new Formula(0, 10, buf.toString());
		sheet.addCell(f);
		buf = new StringBuffer();
		buf.append("SUM(B2:B10)");
		f = new Formula(1, 10, buf.toString());
		sheet.addCell(f);

		// Now a bit of text
		for (int i = 12; i < 20; i++) {
			// First column
			addLabel(sheet, 0, i, "Boring text " + i);
			// Second column
			addLabel(sheet, 1, i, "Another text");
		}
		*/
	}

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
	}

	private void addNumber(WritableSheet sheet, int column, int row,
			Integer integer) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer, times);
		sheet.addCell(number);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}
}
