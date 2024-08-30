- Was it easy to complete the task using AI?


- How long did task take you to complete? (Please be honest, we need it to gather anonymized statistics)


- Was the code ready to run after generation? What did you have to change to make it usable? 
  - I had to modify the tests for the GlobaExceptionHandler class, because the generated code was not correct.The status was not "Not found" or "Internal server error"
  
    but in both cases 200 because the response was anyway set to return a page even if it was not found page or generic error page.
 

- Which challenges did you face during completion of the task?
 

- Which specific prompts you learned as a good practice to complete the task?


- How to run it
   - run docker-compose up -d  to start the database and the client phpMyAdmin
   - Access the client phpMyAdmin on http://localhost:8085, you enter directly with the root credentials
   - 
 


