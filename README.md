BANKING APPLICATION
========================
### OOP Project Using JAVA with MYSQL Database

#### Members:
1. Mehul Agarwal (Project Lead)
2. Kushalkumar Dipakkumar Gajjar
3. Harshit Chauhan
4. Virendra Yadav
5. Vinay Yadam

#### How to Run:
*Use jdbc driver present in the lib folder and add its path in the classpath while running the Main.java file to run the commandline app.*

#### Command Line Arguments
* --add                                                                                                                                                                                                                                                       `adds a single account to the database`
* --u [acc_no] [password] loanpay                                                                                                                                                                                                    `pays your monthly installment of loan`
* --u [acc_no] [password] loanview                                                                                                                                                                                                  `view the status of your current loans`
* --a [admin password] loantype [loantype=(HOME, EDUCATION, PERSONAL, CAR)]                                                                                                  `get the details of all loans registered with a specific loan type`
* --a [admin_password] fdpay                                                                                                                                                                                                           `pay the monthly installment of all fixed depposits`
* --a [admin_password] deleteloan [loan_id]                                                                                                                                                                               `deletes the current loan from the database`
* --u [acc_no] [password] addfd [amount in INR] [duartion(in year as integer)]                                                                                                               `add a fixed deposit with a user defined amount and time`
* --querybystatus [status=(resolved, unresolved)]                                                                                                                                                                    `display all queries sorted by whether they are resolved or not`
* --querysolve [query id]                                                                                                                                                                                                                     `solve an admin defined query by query id`
* --querydisplay [query id]                                                                                                                                                                                                                  `display a specific query pertaining to its query id`
* --querydelete [query id]                                                                                                                                                                                                                   `delete a specific query with a query id`
* --viewfeedback                                                                                                                                                                                                                                    `lists all feedback stored in the database`
* --addfeedback [name] [email_id] [feedback text] [rating = (POOR, AVERAGE, GOOD)]                                                                                             `submits a feedback to the system`
* --addcsv [table_name(accounts, transactions, fd, feedback, insurance)] [file_path = (ex-/home/mehul/Deesktop/filename.csv)]             `add data from a csv file into the database`