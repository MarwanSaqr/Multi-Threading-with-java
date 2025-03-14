## Multi-Threading with Java  

### What is this project about?  
This project demonstrates how to efficiently compare words from a file (`sample.txt` located in the `src` folder) using **multi-threading** in Java. By leveraging multiple threads, we aim to improve performance and reduce execution time compared to a single-threaded approach.  

### What is Multi-Threading?  
Multi-threading is a programming technique that allows a process to be divided into multiple threads, which execute concurrently. This enhances performance by utilizing system resources more efficiently, especially in CPU-intensive tasks.  

## Technologies Used
| **Technology**       | **Purpose**                               |
|----------------------|-------------------------------------------|
| **Java**            | Main programming language                  |
| **Multi-threading** | For fast comparing words from file         |
| **Java (JDK 17)**   | ore application logic                      |
| **Maven**           | Dependency management                      |
| **GitHub**          | Version control                            |

## Project Structure

```
LogMonitorSystem/
├── src/
|    ├── sample.txt                              #Words that was compared
|    └── java
|          ├── sample.txt                        #Another file for  words that was compared
|          └── com/mycompany/multithreadingtest/
│             ├──WordCountComparison.java        # Main class to compare words
│             └── MultithreadingTest.java        # Multi-threaded compare processing
└── README.md
```


### Performance Comparison  
Below are some sample results comparing single-threaded and multi-threaded execution:  

```
Single-threaded execution time: 32 ms
Single-threaded total words: 477

Multi-threaded execution time: 7 ms
Multi-threaded total words: 477
```
### If you have a problem in code and how to run it 
Clone our repository and open it in any Java IDE. Then, navigate to the project folder and update the Maven configuration to match your Java version. I am using Maven with Java 17.
