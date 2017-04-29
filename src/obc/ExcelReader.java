package obc;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

public class ExcelReader 
{
	
	private static String path = System.getProperty("user.dir")+"\\TestData\\BankMasterOBC.xls";	
	private HSSFSheet ExcelWSheet;	 
	private HSSFWorkbook ExcelWBook;

	public ExcelReader()  
	{	
			try 
			{	
			
			FileInputStream ExcelFile = new FileInputStream(path);
			ExcelWBook = new HSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet("Sheet1");
	
			} catch (FileNotFoundException fnfEx) {
	            System.out.println("File is not Found. please check the file name.");
	            System.exit(0);
	        } catch (IOException ioEx) {
	            System.out.println(" Please check the path.");
	        } catch (Exception ex) {
	            System.out.println("There is error reading/loading xls file, due to " + ex);
	        }
	
	}
	
	public String getCellData(int RowNum, int ColNum) 
	{
		 try
		 {				 
			 if (ExcelWSheet.getRow(RowNum).getCell(ColNum).getCellTypeEnum()==CellType.NUMERIC)
			 {
				 if(HSSFDateUtil.isCellDateFormatted(ExcelWSheet.getRow(RowNum).getCell(ColNum)))
				 {	
					 return new SimpleDateFormat("dd/MM/YYYY").format(ExcelWSheet.getRow(RowNum).getCell(ColNum).getDateCellValue()).trim();
				 }
				 else
				 {					
					 return new Long((long)ExcelWSheet.getRow(RowNum).getCell(ColNum).getNumericCellValue()).toString().trim();
				 }
			 }			 
			 else
			 {				
				 return "";				 
			 }			
		 }
		 catch (Exception e)
		 {
			 e.printStackTrace();
			 return ""  ;
		 }		
	}
	
	public long getNumData(int RowNum, int ColNum)
	{
		return (long)ExcelWSheet.getRow(RowNum).getCell(ColNum).getNumericCellValue();
	}

	public int getRows()
	{
		return ExcelWSheet.getPhysicalNumberOfRows();
	}
	
	public int getCells(int row)
	{
		return ExcelWSheet.getRow(row).getPhysicalNumberOfCells();		
	}	
	

}

