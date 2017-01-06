# This project hosts the MyCareer API.

##Prerequisites 
1. [JDK 1.8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. [Gradle 3.1+](https://gradle.org/gradle-download/)

##Getting Started Locally

1. Clone the repository using git clone 
2. 'cd mycareer-web-api'
3. 'gradle bootRun' (to run the project)
4. 'gradle eclipse' (to syncronise eclipse with the libraries and remove the errors, this method also calls "gradle build")
5. 'gradle build' (to download dependencies and get the project set up)
6. The API is accessible at [localhost:8080](http://localhost:8080/)

##Notes:
1. Only UAT, Dev and Live servers have the certificate to use ews services. 
   To work on email/contact tasks you need to change the Constants file and comment out these lines:
  		public static final String MAIL_USERNAME="FbackUK";
		public static final String MAIL_PASSWORD="********";
		public static final String MAIL_EXCHANGE_URI="https://mailbox.corp.sopra/ews/exchange.asmx";
		
	and uncomment the following ones as well as inserting your own email address and your current SopraSteria password
		//public static final String MAIL_USERNAME="michael.piccoli@soprasteria.com";
		//public static final String MAIL_PASSWORD="****************";
		//public static final String MAIL_EXCHANGE_URI="https://outlook.office365.com/ews/exchange.asmx";
		
2. Before starting the back-end you might want to start the MongoDB replica sets, following the guide in the "MyCareer Environment"