# Oyster-Card
Assignment is to model the following fare card system which is a limited version of London’s Oyster card system. At the end of the test, you should be able to 
demonstrate a user loading a card with £30, and taking the following trips, and then viewing the balance.
- Tube Holborn to Earl’s Court
- 328 bus from Earl’s Court to Chelsea 
- Tube Earl’s court to Hammersmith

# Check Out and Build from Source
1) Clone the repository from GitHub:
 $ git clone https://github.com/quitenatural/Oyster-Card.git

2) Navigate into the cloned repository directory:
 $ cd oyster

3) The project uses gradle to build:
 $ ./gradlew clean build

# Running from the Command Line 

 Launch the application from the command line:
 $ java -jar build/libs/oyster-0.0.1-SNAPSHOT.jar
 
 # Endpoints :
 1) Get all users: GET http://localhost:8080/api/v1/users
 2) Add a user: POST http://localhost:8080/api/v1/users
 3) Get user balance: GET http://localhost:8080/api/v1/users/{user_id}/balance
 4) Load user balance: PATCH http://localhost:8080/api/v1/users/{user_id}/topUp?money=30
 5) Start trip: POST http://localhost:8080/api/v1/travel/startTrip
 6) End Trip: PATCH http://localhost:8080/api/v1/travel/endTrip
 
 # Features:
1) The project is a restful web app following MVC design structure.
2) Inbuild H2 database has been used for storing data.
 
