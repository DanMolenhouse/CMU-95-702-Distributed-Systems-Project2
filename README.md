# CMU-95-702-Distributed-Systems-Project2
Project 2 for CMU course 95-702 (Distributed Systems) Spring 2022

Project Objective:
1. Understand client side and server side UDP and TCP sockets.
2. Simulate a man-in-the-middle attack and understand the indicators and implications of this type of attack.
3. Understand TCP and UDP protocols through first-hand experience.
4. Familiarize with the abstraction provided by Remote Procedure Calls (RPCs) and implement digital signatures in RSA. 

Tasks: 

0. Modify Server & Client code from in-class Lab to print certain messages and respond to given commands.
1. Simulate a man-in-the-middle attack
2. Create a basic addition method on the server that is called by the client, server responds with a running total. Server keeps track of total as long as it's running (client can exit / return and see the same total).
3. Same principle as Task 2, but adds a method for basic subtraction.
4. Repeat Task 3 using TCP instead of UDP.
5. Implement RSA & digitally sign messages (modeled after ETH signatures).

Topics/Skills covered:
- UDP / TCP interactions
- 

Demonstration of completed tasks:

Task 0:
Client console output - "Halt!" command shuts down both server / client
![Picture1](https://user-images.githubusercontent.com/114946651/193726067-49514126-9b33-49c6-846b-adee03b062ee.png)

Server console output - echoing messages received from client
![Picture2](https://user-images.githubusercontent.com/114946651/193726198-50df2d3c-ddb2-4e13-80a2-436cda4cb378.png)

Task 1:
Created an eavsdropper that intercepts messages from spoofed port (in this case, 6798), and relays the intercepted message to another port (in this case, 6789). The idea is just to demostrate what it may look like on the client side if a message is being intercepted (shows that the client doesn't know any better)
![task1](https://user-images.githubusercontent.com/114946651/193727116-87f0b6e7-d90e-44a4-a6f0-fc6990157e85.png)

Task 2:



