package edu.uic.ids.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.myfaces.custom.fileupload.UploadedFile;

@ManagedBean
@SessionScoped

public class DatabaseImport {

	private DatabaseAccess databaseAccess;
	private String context;
	private String fileContentType;
	private int numberRows;
	private int numberColumns;
	private String uploadedFileContents;
	private boolean fileImport;
	private boolean fileImportError;
	private UploadedFile uploadedFile;
	private String fileLabel;
	private String fileName;
	private long fileSize;
	private String filePath;
	private String tempFileName;
	private FacesContext facesContext;
	private String fileType;
	private String fileFormat1;
	private String headerRow;
	public File f;

	public String getHeaderRow() {
		return headerRow;
	}

	public void setHeaderRow(String headerRow) {
		this.headerRow = headerRow;
	}

	public String getFileFormat1() {
		return fileFormat1;
	}

	public void setFileFormat1(String fileFormat) {
		this.fileFormat1 = fileFormat;
	}

	List<String> header = null;
	String dataRow[] = null;
	List<String[]> dataList = new ArrayList<String[]>();

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public DatabaseAccess getDatabaseAccess() {
		return databaseAccess;
	}

	public void setDatabaseAccess(DatabaseAccess databaseAccess) {
		this.databaseAccess = databaseAccess;
	}

	public String getContextPath() {
		return context;
	}

	public void setContextPath(String contextPath) {
		this.context = contextPath;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public int getNumberRows() {
		return numberRows;
	}

	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	public int getNumberColumns() {
		return numberColumns;
	}

	public void setNumberColumns(int numberColumns) {
		this.numberColumns = numberColumns;
	}

	public String getUploadedFileContents() {
		return uploadedFileContents;
	}

	public void setUploadedFileContents(String uploadedFileContents) {
		this.uploadedFileContents = uploadedFileContents;
	}

	public boolean isFileImport() {
		return fileImport;
	}

	public void setFileImport(boolean fileImport) {
		this.fileImport = fileImport;
	}

	public boolean isFileImportError() {
		return fileImportError;
	}

	public void setFileImportError(boolean fileImportError) {
		this.fileImportError = fileImportError;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTempFileName() {
		return tempFileName;
	}

	public void setTempFileName(String tempFileName) {
		this.tempFileName = tempFileName;
	}

	public FacesContext getFacesContext() {
		return facesContext;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	public String processFileUpload() {
		uploadedFileContents = null;
		facesContext = FacesContext.getCurrentInstance();
		filePath = null;
		System.out.println(filePath + " 1");
		filePath = facesContext.getExternalContext().getRealPath("/") + "temp";
		System.out.println(filePath + " 2");

		File tempFile = null;
		FileOutputStream fos = null;
		int n = 0;
		fileImport = false;
		fileImportError = true;
		try {
			fileName = uploadedFile.getName();
			fileName = fileName.substring((fileName.lastIndexOf("\\") + 1), fileName.length());
			fileSize = uploadedFile.getSize();
			fileContentType = uploadedFile.getContentType();
			System.out.println(filePath + " 2");
			System.out.println(fileName + " 2");
			// next line if want upload in String for memory processing
			// uploadedFileContents = new String(uploadedFile.getBytes());
			tempFileName = fileName;
			tempFile = new File(filePath + "/" + fileName);
			fos = new FileOutputStream(tempFile);
			// next line if want file uploaded to disk
			fos.write(uploadedFile.getBytes());
			fos.close();
			Scanner s = new Scanner(tempFile);
			String input;
			while (s.hasNext()) {
				input = s.nextLine();
				// do something using input line at a time
				n++;
			}
			s.close();
			f = tempFile;
			numberRows = n;
			fileImport = true;
		} catch (FileNotFoundException e) {
			System.out.println(filePath + " 2");
			System.out.println(fileName + " 2");
			e.printStackTrace();
			fileImportError = true;
		} catch (IOException e) {
			System.out.println(filePath + " 2");
			System.out.println(fileName + " 2");
			e.printStackTrace();
			fileImportError = true;
		}
		return "SUCCESS";
	}

	@PostConstruct
	public void init() {
		facesContext = FacesContext.getCurrentInstance();
		Map<String, Object> m = facesContext.getExternalContext().getSessionMap();
		databaseAccess = (DatabaseAccess) m.get("databaseAccess");

	}

	public String processFileUpload1() {
		String status = processFileUpload();

		if (!status.equalsIgnoreCase("SUCCESS"))
			return "FAIL";
		switch (fileType.toLowerCase()) {
		case "metadata": // metadata file import
			status = processMetaDataFile();
			break;
		case "data": // data file import
			status = processFile1();
			break;
		default: // general file import
			status = processFile1();
			// processF();
			break;
		}
		return status;
	}

	/*
	 * private String processFile() { List<String> header1 = null; String
	 * dataRow [] = null; List<String[]> dataList = new ArrayList<String []>();
	 * int nc = 0; int row = 0; // includes header row Reader in; int n = 0;
	 * String status = "FAIL"; try { in = new FileReader(f); Iterable<CSVRecord>
	 * records = CSVFormat.DEFAULT.parse(in); for (CSVRecord record : records) {
	 * nc = record.size(); // allocate exact size for data and header // based
	 * on first row if(row++ == 0) { header = new ArrayList<String>(nc); for
	 * (String field : record) { header.add(field); } continue; } // data row
	 * needs new array for each data line // if(row > 0) – depends if header row
	 * or not dataRow = new String [nc]; dataList.add(dataRow); int col = 0; for
	 * (String field : record) { dataRow[col++] = field; }
	 * System.out.println("sdfwdf"); status = "SUCCESS"; } } catch
	 * (FileNotFoundException e) { System.out.println("s2");
	 * e.printStackTrace(); // . . . } catch (IOException e) {
	 * System.out.println("s3"); e.printStackTrace(); // . . . } numberRows =
	 * row; return status;
	 * 
	 * }
	 */

	public List<String[]> getDataList() {
		return dataList;
	}

	public void setDataList(List<String[]> dataList) {
		this.dataList = dataList;
	}

	public String processFile1() {
		String splitt;
		switch (fileFormat1.toLowerCase()) {
		case "exceltab":
			splitt = "\t";
			break;
		case "excelcsv":
			splitt = ",";
			break;
		default:
			splitt = ",";

			break;
		}

		List<String> header1 = null;
		BufferedReader br = null;
		FileReader fr = null;

		dataList.clear();
		try {

			// fr = new FileReader(f);
			// br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(f));

			// br.readLine();
			int i = 0, m = 0;
			String a = null;

			if (headerRow.toLowerCase().equals("yes")) {

				while ((sCurrentLine = br.readLine()) != null) {

					if (i == 0) {
						String[] strt = sCurrentLine.split(splitt);
						header1 = new ArrayList<String>(strt.length);
						System.out.println("head" + sCurrentLine + "strtln" + strt.length);
						for (int k = 0; k < strt.length; k++) {
							header1.add(strt[k]);
							System.out.println(header1.get(k));
						}

						setHeader(header1);
						i++;
					} else {
						String[] strt1 = sCurrentLine.split(splitt);
						System.out.println(sCurrentLine);

						dataList.add(strt1);
						i++;
					}
				}
			} else {
				while ((sCurrentLine = br.readLine()) != null) {

					String[] strt1 = sCurrentLine.split(splitt);
					System.out.println(sCurrentLine);

					dataList.add(strt1);
					if (m == 0) {
						header1 = new ArrayList<String>(strt1.length);
						System.out.println("head" + sCurrentLine + "strtln" + strt1.length);
						for (int k = 0; k < strt1.length; k++) {
							header1.add("Header" + k);
							System.out.println(header1.get(k));
						}

						setHeader(header1);

						m++;
					}
				}
				
				for (int l = 0; l < dataList.size(); l++) {
					String sqlQuery = "Insert into " + fileLabel + " values (";
					for (int b = 0; b < dataList.get(l).length; b++) {
						if (b < dataList.get(l).length - 1) {
							sqlQuery += dataList.get(l)[b] + ",";
						} else {
							sqlQuery += dataList.get(l)[b] + ")";
						}
					}
					System.out.println(sqlQuery + " query");
					databaseAccess.execute(sqlQuery);
				}
				
				

			}

			System.out.println(i + m);
			setHeader(header1);
			return "SUCCESS";

		} catch (IOException e) {

			e.printStackTrace();
			return "FAIL";

		} finally {
			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {
				ex.printStackTrace();
				return "FAIL";
			}
		}
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	private String processDataFile() {
		// TODO Auto-generated method stub
		return "SUCCESS";
	}

	private String processMetaDataFile() {
		String splitt;
		switch (fileFormat1.toLowerCase()) {
		case "exceltab":
			splitt = "\t";
			break;
		case "excelcsv":
			splitt = ",";
			break;
		default:
			splitt = ",";

			break;
		}

		List<String> header1 = null;
		BufferedReader br = null;
		FileReader fr = null;
		dataList.clear();
		try {

			// fr = new FileReader(f);
			// br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(f));

			// br.readLine();
			int i = 0, m = 0;
			String a = null;

			if (headerRow.toLowerCase().equals("yes")) {

				while ((sCurrentLine = br.readLine()) != null) {

					if (i == 0) {
						String[] strt = sCurrentLine.split(splitt);
						header1 = new ArrayList<String>(strt.length);
						System.out.println("head" + sCurrentLine + "strtln" + strt.length);
						for (int k = 0; k < strt.length; k++) {
							header1.add(strt[k]);
							System.out.println(header1.get(k));
						}

						setHeader(header1);
						i++;
					} else {
						String[] strt1 = sCurrentLine.split(splitt);
						System.out.println(sCurrentLine);

						dataList.add(strt1);
						i++;
					}
				}
			} else {
				while ((sCurrentLine = br.readLine()) != null) {

					String[] strt1 = sCurrentLine.split(splitt);
					System.out.println(sCurrentLine);

					dataList.add(strt1);
					if (m == 0) {
						header1 = new ArrayList<String>(strt1.length);
						System.out.println("head" + sCurrentLine + "strtln" + strt1.length);
						for (int k = 0; k < strt1.length; k++) {
							header1.add("Header" + k);
							System.out.println(header1.get(k));
						}

						setHeader(header1);

						m++;
					}
				}

			}
			for(int i1 =0;i1<dataList.size();i1++){
				for(int j =0;j<dataList.get(i1).length;j++)
				{
					System.out.println(dataList.get(j));
				}
			}
			String sqlQuery = "Create table " + fileLabel + "(";
			for (int l = 0; l < dataList.size(); l++) {
				if (l < dataList.size() - 1) {
					sqlQuery += dataList.get(l)[0] + " " + dataList.get(l)[1] + ",";
				} else {
					sqlQuery += dataList.get(l)[0] + " " + dataList.get(l)[1] + ")";
				}
			}

			System.out.println(sqlQuery + " query");
			databaseAccess.execute(sqlQuery);
			System.out.println(i + m);
			setHeader(header1);
			return "SUCCESS";

		} catch (IOException e) {

			e.printStackTrace();
			return "FAIL";

		} finally {
			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {
				ex.printStackTrace();
				return "FAIL";
			}
		}
	}
}
