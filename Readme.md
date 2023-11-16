# Money Transfer Application:

This is a simple application using Gradle, Java 17, Spring Boot that provide transfer of money between accounts.


## Functionality:
- Money transfer between existing accounts (accountFrom,accountTo)
- Validation (positive amount of balance and positive amount of money transfer)
- Creating of account also present

## How to run

- git clone 
- gradlew clean build
- gradlew bootRun


## Controllers:
- Create account (POST http://localhost:18080/v1/accounts {
  "accountId": "Id-321",
  "balance": "10"
  } )
- Get account by id (GET http://localhost:18080/v1/accounts/accountId)
- Transfer money from one account to another (POST http://localhost:18080/v1/accounts/transfer 
  {
  "accountFromId": "Id-321",
  "accountToId": "Id-123",
  "amount" : 0
  }