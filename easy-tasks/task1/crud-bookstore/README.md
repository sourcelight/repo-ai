- Was it easy to complete the task using AI?



- How long did task take you to complete? (Please be honest, we need it to gather anonymized statistics)


- Was the code ready to run after generation? What did you have to change to make it usable? 
  - The code was not ready to run, I had to change the package name from javax to jakarta
  - I had to add the @Data lombok annotation to the class Book in order to generate the getters and setters used in the BookService class
  - I had to add the methods like findByTitleContainingIgnoreCase in the BookRepository class, I've used copilot to generate the method signature and the implementation
  - I had to change the lombok version from 1.18.22 in the pom.xml file to 1.18.34 in order to make it compatible with the jdk 21 version


- Which challenges did you face during completion of the task?


- Which specific prompts you learned as a good practice to complete the task?


