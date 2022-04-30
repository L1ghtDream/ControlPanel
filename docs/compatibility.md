# General Information
The application was designed by the ground up to run on Linux as its primary platform.
The application heavily depends on the following:

| Library / App | Linux compatibility        | Windows Comaptibility                                                                                                                                                                                                                    |   |   |
|---------------|----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---|---|
| Java 8        | apt install openjdk-8-jdk  | Java Official Website.<br/>Configuration of the panel to use them instead of the linux java paths                                                                                                                                        |   |   |
| Java 11       | apt install openjdk-11-jdk | Java Official Website.<br/>Configuration of the panel to use them instead of the linux java paths                                                                                                                                        |   |   |
| Java 16       | apt install openjdk-16-jdk | Java Official Website.<br/>Configuration of the panel to use them instead of the linux java paths                                                                                                                                        |   |   |
| Java 17       | apt install openjdk-17-jdk | Java Official Website.<br/>Configuration of the panel to use them instead of the linux java paths                                                                                                                                        |   |   |
| Screen        | apt install screen         | This is linux-only and there were no options (at the time of writing) to get linux screens onto windows. If you can find one, you would need to also edit the config file to make the panel use that one instead of the linux counterpart |   |   |
| lsof          | apt install lsof           | This is linux-only but there are some alternatives like PowerShell's Get-Process that you can add in the config to make it work                                                                                                          |   |   |

# Windows Compatibility - Easy way
You can easily install a virtual machine on your linux computer / server using software like [VirtualBox](https://www.virtualbox.org/wiki/Downloads) and Windows [Hyper-V](https://docs.microsoft.com/en-us/virtualization/hyper-v-on-windows/quick-start/enable-hyper-v]), but this is going to impact heavily the performance of your machine.
<br>
A better solution would be to install [WSL](https://docs.microsoft.com/en-us/windows/wsl/install) (Windows Subsystem for Linux) on your machine and run the application natively on linux.

# Linux Compatibility
The application is written for Java 11 and is natively compatible with Linux.
<br>
All the testing for the application has been done on Ubuntu 20.04 LTS using a MariaDB for the database engine. 