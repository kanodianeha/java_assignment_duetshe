Assumptions: 

1. Thousands of trades are flowing, but not concurrently. This code can be improvised to serve concurrency later. 


Improvements: 
1. Add a Logger library
2. Interfaces driven if business requirement was like that 
3. Exception handling and error messages to be taken care of.
4. Use RxJava to for upsert method
5. Use Mongoose style to convert DB document into Java objects 
6. There are no validations on the input data, can be added 
7. Add a Scheduler to look for all expired ones and update them 

Doubts/Questions: 
1. Shouldn't all the ids be of type long? Why String!? 
2. Confusion related to relationship between tradeId and cp-Id 
3. How about insert? Should the Trade be not inserted, if it is not present? 
