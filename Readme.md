# Money Transfer Application:

This is a simple application using Gradle, Java 17, Spring Boot that provide transfer of money between accounts.

## Task

We are providing you with a simple REST service with some very basic functionality - to add and read an
account.
It is a standard project, running on Spring Boot. It uses Lombok and if you've not come across it
before you'll need to configure your IDE to use it (otherwise code will not compile).
Your task is to add functionality for a transfer of money between accounts. Transfers should be
specified by providing:
accountFrom id
accountTo id
amount to transfer between accounts

The amount to transfer should always be a positive number. It should not be possible for an account to end
up with a negative balance (we do not support overdrafts!)
Whenever a transfer is made, a notification should be sent to both account holders, with a message
containing the id of the other account and the amount transferred.

**Multithreading:**
This feature should be implemented in a thread-safe manner. Your solution should never deadlock, should
never result in corrupted account state, and should work efficiently for multiple transfers happening at the
same time.

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
