- Was it easy to complete the task using AI?



- How long did task take you to complete? (Please be honest, we need it to gather anonymized statistics)


- Was the code ready to run after generation? What did you have to change to make it usable? 
  - The code was not ready to run, I had to change the package name from javax to jakarta
  - I had to add the @Data lombok annotation to the class Book in order to generate the getters and setters used in the BookService class
  - I had to add the methods like findByTitleContainingIgnoreCase in the BookRepository class, I've used copilot to generate the method signature and the implementation
  - I had to change the lombok version from 1.18.22 in the pom.xml file to 1.18.34 in order to make it compatible with the jdk 21 version
  - I changed the pom configuration to use the jdk 21 version and set the JAVA_HOME environment variable to the jdk 21 path(used by maven) in order to run the application with the command: "mvn spring-boot:run"
  - I had to change the application.properties file to use the jdk 21 version
  - I had to add application-test.yml file to the resources folder to make the tests run adding also @Profile("test") to the test classes
  - When I added the Hateos support, I had to review all tests(particular all the JsonPath expressions) to make them work again
  - When I added the swagger support I ahd to address openapi to use springdoc-openapi-ui and find the correct artifcatId and version compatible with spring boot version 3.2.0


- Which challenges did you face during completion of the task?
  - The generated code is not immediately ready to run, I had to make some changes to make it work
  - To create a ready to run project required some additional tuning like changing the sdk and the local maven configuration
  - 


- Which specific prompts you learned as a good practice to complete the task?
  -It's a good idea to require to generate the tasks necessary to accomplish the project with the related prompts to make the task easier to complete 


