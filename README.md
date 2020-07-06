# kata-bank-ing
This project is a Bank account Kata. The MVP consists of 4 user stories.

### MVP

#### User Story 1
> As a bank, deposit money from a customer to his account, is allowed when superior to €0.01

#### User Story 2
> As a bank, withdraw money from a customer account, is allowed when no overdraft used

#### User Story 3
> As a bank, a customer can display its account balance

#### User Story 4
> As a bank, a customer can display its account transactions history

Each feature is implemented using TDD.

### Technical Specification :
* JAVA 11
* Spring Boot 2.3.1.RELEASE
* JUnit 5
* OpenApi v3

### Technical Architecture :
The Kata contains a Business layer connected to an embedded inMemory database and exposed as a Restful API. the later is documented using openApi v3 specification.

### Running the project :
To run the project locally, simply execute the maven goal "spring-boot:run" and make sure that your port 8087 is not used by anoher process or change it in the yml file.

### Features :
For test purposed, we have fed the database with one account and one customer (details can be found in resource file).

#### Deposit/Withdraw :
Two transactions are possible : deposit and withdraw.

A test case is performed with openApi to demonstrate a deposit operation :


![deposit example] (https://i.imgur.com/ofhte0V.jpg)

#### Consult balance :
It is possible to consult the balance for a given account, as shown in the screenshot below :

![consult balance] (https://i.imgur.com/xstxeUh.jpg)

#### Consult Statements (Or transactions history) :
When a transaction is executed, a statement is generated and stored in order to keep trace of all the operations occuring on the account.

It is possible to get the transactions hisory for a given account as shown in the following test case :
[consult statements] (https://i.imgur.com/QfID7zg.jpg)

