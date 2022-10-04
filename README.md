# CMU-95-702-Distributed-Systems-Project2
**Project 2 for CMU course 95-702 (Distributed Systems) Spring 2022**

**Project Objective:**
1. Understand client side and server side UDP and TCP sockets.
2. Simulate a man-in-the-middle attack and understand the indicators and implications of this type of attack.
3. Understand TCP and UDP through first-hand experience.
4. Familiarize with the abstraction provided by Remote Procedure Calls (RPCs) and implement digital signatures in RSA. 

**Tasks:** 

0. Modify Server & Client code from in-class Lab to print certain messages and respond to given commands.
1. Simulate a man-in-the-middle attack
2. Create a basic addition method on the server that is called by the client, server responds with a running total. Server keeps track of total as long as it's running (client can exit / return and see the same total).
3. Same principle as Task 2, but adds functionality to track multiple client IDs, subtract from the total, and get the current total for any of the IDs tracked.
4. Repeat Task 3 using TCP instead of UDP.
5. Implement RSA & digitally sign messages (modeled after ETH signatures).

**Topics/Skills covered:**
- Client/Server interactions using both TCP & UDP
- Eavesdropper / MITM attacks
- Digital Signatures 
- RSA public / private key encryption

**Demonstration of completed tasks:**

**Task 0:**
Client console output - "Halt!" command shuts down both server / client
![Picture1](https://user-images.githubusercontent.com/114946651/193726067-49514126-9b33-49c6-846b-adee03b062ee.png)

Server console output - echoing messages received from client
![Picture2](https://user-images.githubusercontent.com/114946651/193726198-50df2d3c-ddb2-4e13-80a2-436cda4cb378.png)

**Task 1:**
Created an eavsdropper that intercepts messages from spoofed port (in this case, 6798), and relays the intercepted message to another port (in this case, 6789). The idea is just to demostrate what it may look like on the client side if a message is being intercepted (shows that the client doesn't know any better)
![task1](https://user-images.githubusercontent.com/114946651/193727116-87f0b6e7-d90e-44a4-a6f0-fc6990157e85.png)

**Task 2:**
Client-side console output. The client enters a number and the server adds that number to the running total, then returns the running total to the client. Ignore the spelling mistake.
![Picture1](https://user-images.githubusercontent.com/114946651/193728474-e40a94f3-bec1-42a4-af3b-b715b56c705f.jpg)

Server-side console output. Shows the addition operation and the result, and what is being returned to the client.
![Picture1](https://user-images.githubusercontent.com/114946651/193728826-03003b29-6536-4e90-9c3a-05349c3b83f8.jpg)

**Task 3:**
Expanded upon task 2, added multiple users, subtraction, and GET functionality. Client-side console output:
![Picture1](https://user-images.githubusercontent.com/114946651/193733352-2ea15924-a172-41d9-8d1a-23a17598ca6f.jpg)

Server-side console output:
![Picture1](https://user-images.githubusercontent.com/114946651/193733658-29cdf834-8cdd-45b6-9a29-6b232615df73.jpg)

**Task 4:**
Functionality is identical to Task 3, it just uses TCP instead of UDP. Client-side console output:
![Picture1](https://user-images.githubusercontent.com/114946651/193733906-458c699e-7740-45e7-9a8e-0f51acfac7f2.jpg)

Server-side console output:
![Picture2](https://user-images.githubusercontent.com/114946651/193734116-e5048bc4-747c-42c4-a357-5758a3b55ba9.jpg)

**Task 5:**
Task 4 functionality with RSA & digital signatures added. Client-side console output:
![Picture1](https://user-images.githubusercontent.com/114946651/193734316-f2561e47-2e5d-46c8-b553-b4eeabf6778d.jpg)

Server-side console output:
![Picture2](https://user-images.githubusercontent.com/114946651/193734336-b3e9b70e-85ab-4768-85e0-fda6de42b0f3.jpg)

