# MS3CodingChallenge
## Purpose
This repository is created as a part of the MS3 recruitment process. This repository contains the Eclipse Workspace of the project and also the executable JAR File. This project's Function is to select a csv file, and parse the data which is saved to the outputDB.db SQLite Database in the Output Folder. The Unsuccessful or incomplete datas are saved in the bad csv file which has format name of "MMdd_HHmm-bad.csv", also under the the output Folder. Lastly the project will also produce a Log file which contains the statistic datas of the parsed file, which indicates the # of records received, successfully saved to SQLite Database and the Failed datas.

## How to run the application
* **Run the Executable (1st Option)**
  1. Download/Clone the repository
  2. Locate and Open Executable JAR File (MS3 > MS3CodingChallenge > ExecutableVer.jar)
  3. Click "Select CSV File"
  4. Choose CSV File
  5. Click Parse CSV
  6. When Left Panel turns green, it means it finished parsing, check the Output Folder for the bad csv file, Log file and outputDB.db file.
* **Run through Eclipse (2nd Option)**
  1. Make sure you have eclipse IDE (I used Version: 2020-03 (4.15.0) Build id: 20200313-1211)
  2. Download/Clone the repository
  3. Open Eclipse and switch workspace to the downloaded repository
  4. Click "Run Main"
  5. Click "Select CSV File"
  6. Choose CSV File
  7. Click Parse CSV
  8. When Left Panel turns green, it means it finished parsing the file, check the Output Folder for the bad csv file, Log file and outputDB.db file.
  
## Project Overview (Approaches, Design Choices, Assumptions)
  * I chose to use Eclipse IDE with this project. Using Java language.
  * **Execution Environment JRE version:** JavaSE-1.8
  * I created a class extending JFrame as Main. I created my GUI through WindowBuilder > Swing Designer
  * **Used sqlite-jdbc-3.30.1.jar:** https://bitbucket.org/xerial/sqlite-jdbc/downloads/
  * **Used OpenCSV library:** https://sourceforge.net/projects/opencsv/files/opencsv/
  * **Used Apache Commons Lang 3.10:** https://mvnrepository.com/artifact/org.apache.commons/commons-lang3/3.10
  * First at main, I created 2 buttons. 
    1. **First Button is *btn_SeletCSV***
        * Enabled by default
        * **Functions:** When clicked, It shows Open Dialog Box, which User needs to select the csv file to be fed to the program. When the user selects the file, the path of the selected file will be shown by *lbl_Path* which is a JLabel and the 2nd Button will be enabled.
    2. **Second Button is *btn_Parse***
        * Disabled by default (Enabled when the user selected a csv file)
        * **Functions:** When Clicked, CSVReader is initialized with the selected file of the user from the open dialog box. CSVWriter is also intialized for writing the "bad" datas to "MMdd_HHmm-bad" format csv file in the output folder. After parsing, the program application saves the "good" datas to the outputDB.db SQLite Database in the output folder which in the same writes the statistics to the "MMdd_HHmm" format also of the log file in the output folder.
      
### Application General Approach
  * First when user selects file, the selected file location stored at selectedFile variable
  * When Parse CSV is clicked, CSVReader and CSVWriter are initialized
  * Main.java LN 153: I skipped column header, so it won't be included to the process.
  * Main.java LN 155: Iterates through the CSV with the `readNext()` function, checking if the line is null or not
  * each line is processed by: 
    1. Check if row length is equal to 10. 
        * If equal to 10: Foreach looped is done to check if there is an empty element
        * If no empty elements: Add row data to ArrayList variable `dataSuccess`
                               : increment `countSuccessful`
           If there are empty elements: Add row data to ArrayList variable `dataFailed`
                               : increment `countFailed`
    2. If row length not equal to 10: Add row data to ArrayList variable `dataFailed`
                                    : increment `countFailed`
    3. increment `countReceived`
  * After CSV is processed, ArrayList variable `dataSuccess` is inserted to the SQLiteDatabase. `insertAll` function of the DatabaseFunc.java is called at Main.java LN 184
    1. `insertAll` function in DatabaseFunc.java accepts a paramater of ArrayList of Array of Strings; Data is inserted to the SQLite Database using Prepared statement and commit as batch, steps are:
        * Establish connection to the database
        * Turn autocommit off (as I am trying to commit as a batch for faster processing)
        * The passed data to the function is an ArrayList, and to process each Array of string, I used foreach loop. Each data are then added to sql preparedstatement format statement.
        * Each statement are added to the batch
        * After all datas processed, Batch is then executed and commited to the SQLite Database
      
### Assumptions
  * I did not include in parsing the CSV datas the column header in the first row, as I think the desired datas to be commited to the database are only the following datas
  * bad csv and log outputs are in format "MMdd_HHmm", which corresponds to the executed time of parsing. I assume that the client wants statistics on every execution. 
