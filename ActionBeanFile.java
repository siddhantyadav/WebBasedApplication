package edu.uic.ids.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import com.mysql.jdbc.PreparedStatement;

public class ActionBeanFile {
	
	private UploadedFile uploadedFile;
	private String fileLabel;
	private String fileName;
	private long fileSize;
	private String fileContentType;
	private int numberRows;
	private int numberColumns;
	private String uploadedFileContents;
	private boolean fileImport;
	private boolean fileImportError;
	private String filePath;
	private String tempFileName;
	private String tempFile;
	private String header;
	private String fileType;
	private String fileFormat;
	private FacesContext facesContext;
	private DatabaseAccess databaseAccess;
	private Connection connection;
	private PreparedStatement preparedStatement;

	@PostConstruct
	public void init() {
		Map<String, Object> m = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		databaseAccess = (DatabaseAccess) m.get("databaseAccess");
		System.out.println("databaseAccess.getDatabases(): " + databaseAccess);
	}

	public String processFileUpload() {
		System.out.println("Entered PRocess file upload");
		uploadedFileContents = null;
		// messageBean.setErrorMessage("");
		facesContext = FacesContext.getCurrentInstance();
		filePath = facesContext.getExternalContext().getRealPath("/temp");
		File tempFile = null;
		FileOutputStream fos = null;
		int n = 0;
		fileImport = false;
		fileImportError = true;
		try {

			if(uploadedFile==null){
				System.out.println("File upload is null");
				return "FAIL";
			}
			fileName = uploadedFile.getName();
			String baseName = FilenameUtils.getBaseName(fileName);
			fileSize = uploadedFile.getSize();
			fileContentType = uploadedFile.getContentType();
			// next line if want upload in String for memory processing
			uploadedFileContents = new String(uploadedFile.getBytes());
			tempFileName = filePath + "/" + baseName;
			System.out.println("Temp file name: "+tempFileName);
			tempFile = new File(tempFileName);
			fos = new FileOutputStream(tempFile);
			// next line if want file uploaded to disk
			fos.write(uploadedFile.getBytes());
			fos.close();
			Scanner s = new Scanner(tempFile);
			String input = "";
			String[] stInput = new String[100];
			ArrayList<String[]> as = new ArrayList<String[]>();
			int numberOfColumns = 0;
			while (s.hasNext()) {
				input = s.nextLine();
				System.out.println("Input: " + input);
				System.out.println("File format: " + fileFormat);
				if (fileFormat.equals("csv")) {
					System.out.println("Entered");
					stInput = input.split(",");
				} else if (fileFormat.equals("tab") || fileFormat.equals("text")) {
					stInput = input.split("\t");
				}
				System.out.println("Position 0: " + stInput[0]);
				System.out.println("Position 1: " + stInput[1]);
				System.out.println("Position 2: " + stInput[2]);
				System.out.println("Array data: " + Arrays.toString(stInput));
				as.add(stInput);
				// do something using input line at a time
				n++;
				System.out.println(n);
			}
			if (fileType.equals("m")) {
				createTable(fileLabel, as);
			} else if (fileType.equals("d")) {
				insertIntoTable(fileLabel, as);
			}
			{
				numberRows = n;
				fileImport = true;
				s.close();
			}
		} catch (IOException e) {
			e.getMessage();
			return "FAIL";
		}
		System.out.println("Process Completed at this layer");
		return "SUCCESS";
	}

	public boolean createTable(String fileLabel01, ArrayList<String[]> test) {
		StringBuilder sb = new StringBuilder();

		if (header.equals("n")) {

			System.out.println("ArrayList size: " + test.size());
			for (int i = 0; i < test.size(); i++) {
				String[] stInput1 = test.get(i);
				sb.append(stInput1[0]).append(" ").append(stInput1[1]);
				if (i < (test.size() - 1)) {
					sb.append(",");
				}
			}
		} else if (header.equals("y")) {

			System.out.println("ArrayList size: " + test.size());
			for (int i = 1; i < test.size(); i++) {
				String[] stInput1 = test.get(i);
				sb.append(stInput1[0]).append(" ").append(stInput1[1]);
				if (i < (test.size() - 1)) {
					sb.append(",");
				}

			}

		}

		String createTbl = "CREATE TABLE " + "s17g209_" + fileLabel01 + "(" + sb.toString() + ")";
		System.out.println("Query: " + createTbl);
		try {
			System.out.println("Entered try block");
			databaseAccess.processQuery(createTbl);
			System.out.println("After connection");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	public boolean insertIntoTable(String fileLabel01,
			ArrayList<String[]> test) {/*
										 * 
										 * StringBuilder sb = new
										 * StringBuilder();
										 * 
										 * 
										 * if(header.equals("n")){
										 * 
										 * System.out.println("ArrayList size: "
										 * +test.size()); for(int
										 * i=0;i<test.size();i++){ String[]
										 * stInput1 = test.get(i);
										 * sb.append(stInput1[0]).append(" ").
										 * append(stInput1[1]);
										 * if(i<(test.size()-1)){
										 * sb.append(","); } } } else
										 * if(header.equals("y")){
										 * 
										 * System.out.println("ArrayList size: "
										 * +test.size()); for(int
										 * i=1;i<test.size();i++){ String[]
										 * stInput1 = test.get(i);
										 * sb.append(stInput1[0]).append(" ").
										 * append(stInput1[1]);
										 * if(i<(test.size()-1)){
										 * sb.append(","); }
										 * 
										 * }
										 * 
										 * }
										 * 
										 * String createTbl = "INSERT INTO "
										 * +fileLabel01+" VALUES ("+sb.toString(
										 * )+")"; System.out.println("Query: "
										 * +createTbl); return true; /*String
										 * createTableSQL =
										 * "CREATE TABLE "+fileLabel01 +
										 * "(USER_ID NUMBER(5) NOT NULL, " +
										 * "USERNAME VARCHAR(20) NOT NULL, " +
										 * "CREATED_BY VARCHAR(20) NOT NULL, " +
										 * "CREATED_DATE DATE NOT NULL, " +
										 * "PRIMARY KEY (USER_ID) " + ")";
										 */

		StringBuilder sb = new StringBuilder();

		if (header.equals("n")) {

			System.out.println("ArrayList size: " + test.size());

			for (int i = 0; i < test.size(); i++) {
				sb.append("(");
				String[] stInput1 = test.get(i);
				// Step 2
				int count = 0;
				for (String j : stInput1) {
					if (j != null) {
						count++;
					}
				}

				// Step 3
				String[] newArray = new String[count];

				// Step 4
				int index = 0;
				for (String j : stInput1) {
					if (j != null) {
						newArray[index++] = j;
					}
				}
				for (int k = 0; k < newArray.length; k++) {
					sb.append(newArray[k]);
					if (k < newArray.length - 1) {
						sb.append(",");
					}
				}
				sb.append(")");
				if (i < (test.size() - 1)) {
					sb.append(",");
				}
			}
			System.out.println("StringBuilder: " + sb.toString());
		} else if (header.equals("y")) {
			for (int i = 0; i < test.size(); i++) {
				sb.append("(");
				String[] stInput1 = test.get(i);
				// Step 2
				int count = 0;
				for (String j : stInput1) {
					if (j != null) {
						count++;
					}
				}

				// Step 3
				String[] newArray = new String[count];

				// Step 4
				int index = 0;
				for (String j : stInput1) {
					if (j != null) {
						newArray[index++] = j;
					}
				}
				for (int k = 0; k < newArray.length; k++) {
					sb.append(newArray[k]);
					if (k < newArray.length - 1) {
						sb.append(",");
					}
				}
				sb.append(")");
				if (i < (test.size() - 1)) {
					sb.append(",");
				}

			}
		}

		String insertQuery = "INSERT INTO " + "s17g209_" + fileLabel01 + " VALUES " + sb.toString();
		System.out.println("Insert Query: " + insertQuery);
		try {
			System.out.println("Entered try block");
			databaseAccess.processQuery(insertQuery);
			System.out.println("After connection");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	public String processFile() {
		List<String> header = null;
		String dataRow[] = null;
		List<String[]> dataList = new ArrayList<String[]>();
		int nc = 0;
		int row = 0; // includes header row
		Reader in;
		int n = 0;
		String status = "FAIL";
		try {
			in = new FileReader(tempFile);
			Iterable<CSVRecord> records = CSVFormat.TDF.parse(in);
			for (CSVRecord record : records) {
				System.out.println("Record: " + record);
				nc = record.size();
				System.out.println(nc);
				// allocate exact size for data and header
				// based on first row
				if (row++ == 0) {
					header = new ArrayList<String>(nc);
					for (String field : record) {
						System.out.println("Field: " + field);
						header.add(field);
					}
					continue;
				}
				// data row needs new array for each data line
				// if(row > 0) – depends if header row or not
				dataRow = new String[nc];
				System.out.println("DataRow: " + nc);
				dataList.add(dataRow);
				int col = 0;
				for (String field : record) {
					System.out.println("Field: " + field);
					dataRow[col++] = field;
				}
				status = "SUCCESS";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// . . .
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// . . .
		}
		numberRows = row;
		return status;
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

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

}
