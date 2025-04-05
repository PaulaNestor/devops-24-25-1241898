# CA2 - Technical Report  

## Table Of Contents  

- [CA2 - Part1: Virtualization with Vagrant - Technical Report](#ca2---part1-virtualization-with-vagrant---technical-report)
- [Part 1 - Introduction](#part-1---introduction)
- [Part 1 - Setup and configuration](#part-1---setup-and-configuration)
- [Part 1 - Install project dependencies](#part-1---install-project-dependencies)
- [Part 1 - Clone the individual repository inside the VM](#part-1---clone-the-individual-repository-inside-the-vm)
- [Part 1  - Possible issues running the projects in the VM](#part-1---possible-issues-running-the-projects-in-the-vm)
- [Part 1 - Maven-based Spring Boot Tutorial Basic Project](#part-1---maven-based-spring-boot-tutorial-basic-project)
- [Part 1 - Gradle-based Spring Boot Tutorial Basic Project](#part-1---gradle-based-spring-boot-tutorial-basic-project)
- [Part 1 - Conclusion](#conclusion)

# CA2 - Part1: Virtualization with Vagrant - Technical Report

Author: Ana Paula Lopes Nestor

Programme: SWitCH DEV

Course: DevOps

## Part 1 - Introduction

This part of the report covers the work done for **CA2 - Part1**, which involved using **VirtualBox** to explore virtualization in a DevOps context. The objective was to recreate a development environment inside a virtual machine and use it to run projects built earlier in the course.
Instead of using a local IDE as in previous assignments, this time all development and execution were done inside a VM. This shift allowed for a closer simulation of real-world DevOps environments, where code often runs in isolated, reproducible systems.  

## Part 1 - Setup and configuration

Since I had previously installed and configured a VirtualBox Virtual Machine (VM) in the last semester for the SCOMRED course, I reused that setup for this assignment. As a result, the base VM installation and initial configuration steps were already completed.

However, the process to set up the VM from scratch, would involve the following steps:

1. **Download and install VirtualBox** from [virtualbox.org](https://www.virtualbox.org/wiki/Downloads).
2. **Create a new VM**, selecting Linux (Ubuntu 64-bit) as the OS and allocating 2048 MB of RAM.
3. **Mount the Ubuntu 18.04 Minimal ISO** in the VM's storage settings to begin the OS installation.
4. **Start the VM** and follow the installation steps to complete the Ubuntu setup.
5. **Install VirtualBox Guest Additions** for better integration with the host.
6. **Configure network settings**:
    - Adapter 1: set to **NAT** for internet access.
    - Adapter 2: set to **Host-only Adapter** (vboxnet0) for communication with the host machine.

To configure the Host-only Adapter the following steps should be taken:

1. Go to **File > Host Network Manager** in VirtualBox.
2. Click **Create** to add a new Host-only network (e.g., `vboxnet0`).
3. Check the IP range of the created network — in this case, it was `192.168.56.1/24`.
4. Assign a static IP within that range to the second adapter of the VM, in this case `192.168.56.5`.  

After starting the virtual machine, I only needed to run step 1 to ensure the package repositories were up to date, and step 6, as the rest of the configuration had already been completed for the SCOMRED course. However, the following steps are essential for setting up network access and remote connectivity in a new environment.

1. **Update package repositories**  
   Run the following command to ensure all package lists are up to date:
   ```
   sudo apt update
   ```
2. **Install network tools**  
   To facilitate network configuration, install the necessary tools:
    ```
    sudo apt install net-tools
    ```  
3. **Edit the Netplan configuration to assign a static IP**  
   Open the network configuration file with the following command:
    ```
    sudo nano /etc/netplan/50-cloud-init.yaml
    ```  
   And ensure the file contains the following configuration:
    ```
    network:
        ethernets:
          enp0s3:
            dhcp4: yes
          enp0s8:
            addresses:
              - 192.168.56.5/24
        version: 2
    ```  
4. **Install OpenSSH server**  
   To enable remote access via SSH, the OpenSSH server needs to be installed:
    ```
    sudo apt install openssh-server
    ```  
5. **Enable password authentication for SSH**  
   Edit the SSH configuration file:
    ```
    sudo nano /etc/ssh/sshd_config
    ```  
   By locating and uncommenting the following line:
    ```
    PasswordAuthentication yes
    ```  
   Then, the SSH service should be restarted to apply the changes:
    ```
    sudo service ssh restart
    ``` 
6. **Install an FTP server**  
    To enable the FTP protocol for file transfers to and from the VM, the next step is to install the FTP server. In this case, I installed `vsftpd` by running the following command:
    ```
    sudo apt install vsftpd
    ```
    In order to enable write access for `vsftpd`, I edited the configuration file:
    ```
    sudo nano /etc/vsftpd.conf
    ```  
   And uncommented the following line to allow write access:
    ```
    write_enable=YES
    ```  
   Afterward, I restarted the FTP service to apply the changes:
    ```
    sudo service vsftpd restart
    ```  

## Part 1 - Install project dependencies

Before building and running the Java-based projects I worked on in CA1 (`springboot_basic_tutorial` and `gradle_basic_demo`), it was necessary to install several dependencies in the virtual machine.  
1. **Install Git**: The first dependency I installed was Git, required to **clone repositories** and manage version control:
```
sudo apt install git
 ``` 
2. **Install Java**: Then, I installed Java. Java is necessary to **compile and run** the Spring Boot and Gradle-based projects. I installed OpenJDK 17 using the headless version (the headless version of the JDK does not include graphical user interface (GUI) components. This makes it lighter and more suitable for environments like servers or virtual machines where no graphical interface is needed):
```
sudo apt install openjdk-17-jdk-headless
 ``` 
3. **Install Maven**: Maven is used to manage dependencies and build Java projects, and was the next dependency I installed:
```
sudo apt install maven
 ``` 
4. **Install Gradle**: Gradle is used to build the gradle_basic_demo project. Although Gradle was already installed in my VM from a previous tutorial here are the steps to install gradle manually:
```
wget https://services.gradle.org/distributions/gradle-8.12-bin.zip
sudo mkdir /opt/gradle
sudo unzip -d /opt/gradle gradle-8.12-bin.zip
 ``` 
Finally, I checked that all tools were correctly installed by verifying their versions:
```
git --version
java --version
mvn --version
gradle --version
 ``` 
At this point, the virtual machine was ready to build and execute the projects developed in previous assignments.

## Part 1 - Clone the individual repository inside the VM

To clone my individual repository inside the virtual machine, I first needed to set up secure SSH access between the VM and my GitHub repository.
The first step was to generate a new SSH key pair. I created a new SSH key pair in the VM to ensure secure communication with GitHub. I ran the following command to generate the key:
```
ssh-keygen -t ed25519 -C "1241898@isep.ipp.pt"
```  
After generating the key, I verified that it was created by listing the contents of the .ssh directory:
```
ls ~/.ssh/
```  
To add the newly created SSH key to my GitHub account, I accessed the key content by displaying it in the terminal:
```
cat ~/.ssh/id_ed25519.pub
```  
Next, I logged into my GitHub account, navigated to **Settings -> SSH and GPG keys**, and clicked on **New SSH key**. I pasted the key into the field provided and saved it, allowing my **VM to authenticate securely with GitHub**.  
Finally, with SSH configured, I **cloned my repository** into the desired directory within the VM using the following command:
```
git clone git@github.com:PaulaNestor/devops-24-25-1241898.git
```  

## Part 1  - Possible issues running the projects in the VM

Since the virtual machine is based on **Ubuntu Server**, it does **not include a graphical user interface (GUI)**. This means that any project or task that requires launching windows or interacting with a desktop environment **will not work directly within the VM**.
In this assignment, some of the projects developed previously require GUI interaction:

- The **Spring Boot project, Maven-based,** and the **Gradle version of the same project** provide a web interface that needs to be accessed via a web browser. Since the VM does not have a browser, these projects must be accessed through a browser running on the **host machine**. For that to work, proper networking (e.g., host-only adapter) must be set up and the correct IP address of the VM used.
- The **server-side of the chat project** can run fully within the VM. However, the **client-side interface**, which launches windows for the user to interact with, **requires a desktop environment**. Therefore, the server can be started inside the VM, but the clients must be run from the **host machine**, or another machine with GUI capabilities.

These restrictions are normal when using minimal server environments, and understanding how to split responsibilities between VM and host is an important part of DevOps and system administration practices.  

## Part 1 - Maven-based Spring Boot Tutorial Basic Project

In this section, I executed the **Spring Boot tutorial basic project**, which is **Maven-based** and was part of the prerequisites from CA1. The goal was to **build and run** the project within the virtual machine environment set up previously.
First, I navigated to the directory where the project files are located. This directory contains the setup for the Spring Boot application.

When attempting to run the application, I encountered an **error** indicating that I did not have permission to execute the **mvnw** script. To resolve this, I executed the following command to grant **execute permissions** to the script:
```
chmod +x ./mvnw
```  
After this, I was **able to run** the application using the following command within the project directory:
```
./mvnw spring-boot:run
```  
As explained previously, the **Ubuntu Server VM** does not have a **graphical user interface (GUI)**, and the Spring Boot application provides a web interface that can only be accessed through a browser. Therefore, **the browser must be opened on the host machine, not on the VM**.
To ensure the application was accessible from the host machine or other devices on the same network, I used the **IP address of the VM**. Here’s the URL I used to access the application:  
```
http://192.168.56.5:8080/
```  
The application loaded successfully, displaying the expected content. This confirms that the backend was functioning correctly and the Spring Boot framework was properly serving the content. The image bellows shows the result:  
![Spring Boot Maven](images/SpringBootMaven.png)  

## Part 1 - Gradle_basic_demo Project - Chat Server and Client

In this section, I describe the process of building and running the **gradle_basic_demo project**, which involved executing the **server** inside the **VM** and the **client** on the **host machine**. This setup was necessary due to the nature of the virtual machine and its **lack of a graphical user interface (GUI)**, as discussed previously.
I first navigated to the **CA1 part2** directory within the virtual machine and encountered a permission issue when attempting to execute the **gradlew** script. To resolve this, I executed the following command to grant execute permissions:
```
chmod +x ./gradlew
```  
After ensuring the script had the correct permissions, I attempted to run the build command:
```
./gradlew build
```  
However, I encountered an error related to the **Gradle Wrapper**, which prevented the build from running. The error message was the following:
![Gradle Wrapper](images/gradleWrapper.png)

To resolve this, I ran the following command to regenerate the gradle wrapper:  
```
./gradle wrapper
```  
This command allowed me to run the build successfully. After that, I was able to execute the **./gradlew build** command. This command **successfully** compiled the project and generated the necessary files.  

Once the project was built, I proceeded to **run the chat server inside the VM**. I executed the following command:  
```
./gradlew runServer
```  
This started the server, and it began listening for connections on port 59001:  
![Run Server](images/runServer.png)  

Since the **Ubuntu Server VM does not have a GUI**, running the client application (which requires a window interface for user interaction) directly inside the VM was not feasible. Therefore, I **ran the client on the host machine**. This separation of responsibilities (server inside the VM, client on the host) is crucial for projects that involve GUI-based interactions but are running in a headless environment.
To connect the client to the server running inside the VM, I executed the following command on the host machine, ensuring it was able to **connect to the VM’s IP address** (in this case, 192.168.56.5) and port 59001:
```
gradlew runClient --args="192.168.56.5 59001"
```  
This allowed the client on the host machine to communicate with the server running in the VM, facilitating the two-way chat functionality, as seen in the images bellow:
![Run Client](images/runClient1.png)  
![Run Client](images/runClient2.png)  

## Part 1 - Gradle-based Spring Boot Tutorial Basic Project

The next part of the assignment focused on running the **Gradle-based version** of the Spring Boot application that had been executed previously using **Maven**.
The steps were quite similar, with the key difference being the use of **Gradle** as the build and execution tool.

After navigating to the appropriate directory, I built the project by executing the following command:
```
./gradlew build
```  
With the build successful, I launched the Spring Boot server using the following command:  
```
./gradlew bootRun
```  
This started the web application, making it accessible on port 8080. To verify that the server was up and running, I opened a browser on the host machine and entered the following URL:  
```
http://192.168.56.5:8080/
```  
This loaded the application’s landing page successfully, confirming that the server was running inside the VM, and that it was reachable from the host machine, as seen in the image bellow:  
![Spring Boot Gradle](images/SpringBootGradle.png)  

## Conclusion

This report describes the process of preparing and working within a **virtualized development environment** for **CA2 Part 1**. The tasks involved included **setting up a virtual machine** using VirtualBox, **installing** essential development tools (such as Git, Java, Maven, and Gradle), and successfully **building and running** Java-based projects.
By executing both Maven and Gradle versions of the Spring Boot tutorial project, as well as the chat server application, it was possible to gain hands-on experience in **managing and testing software within a headless Linux environment**. This also required solving practical challenges, such as **executing GUI-dependent components from the host machine**, and **ensuring communication between the VM and host**. This part of the assignment emphasized the importance of configuring reliable and functional development environments — a critical step in any DevOps workflow. Understanding how to replicate real-world conditions within a controlled setup helps build the skills needed for system setup, testing, and maintenance.
Throughout the process, I faced and resolved practical issues such as setting execution permissions, configuring Gradle properly, and ensuring that the VM and host machine could communicate effectively. These challenges reinforced the importance of understanding the environment in which applications are executed and prepared me for managing similar setups in future development tasks.  
This part of the assignment contributed to building a stronger foundation for working in real-world DevOps contexts.

