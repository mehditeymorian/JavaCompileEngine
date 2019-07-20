# JavaCompileEngine
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bc5268d7ccb74ede9bf16b482ebfea79)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=MohammadNik/JavaCompileEngine&amp;utm_campaign=Badge_Grade)

JavaCompileEngine is an efficient open source Compiling Library, usable in Java frameworks for compiling programming languages files and Processing Commands with realtime feedback. I wrote this project out of curiosity but it's useful in many situations e.g. Programming Contests Sites, Online Compilers, Online Code Learning and etc.  

![JavaCompileEngine](https://github.com/MohammadNik/JavaCompileEngine/blob/master/images/javaCompileEngineLandScape.png)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Install Compilers or Interpreters that you need to compile their files.  
After, Add Compiler or Interpreter Main Program parent folder to *Environment Variables*.  
~~~Text
[DRIVE-NAME]:\[ADDRESS-TO-FOLDER]\Python37
[DRIVE-NAME]:\[ADDRESS-TO-FOLDER]\jdk-12.0.1\bin
~~~
For supported Languages in **JavaCompileEngine** check [Supported Languages List](https://github.com/MohammadNik/JavaCompileEngine/blob/master/SupportedLanguages). 

### Downloading
You can download a **jar version** from GitHub's [releases page](https://github.com/MohammadNik/JavaCompileEngine/releases).  
Or you can Clone the project

Clone Over HTTPS
~~~git
 $ git clone https://github.com/MohammadNik/JavaCompileEngine.git
~~~
Clone Over SSH
~~~git
 $ git clone git@github.com:MohammadNik/JavaCompileEngine.git
~~~
Note: This Project used Lombok. check [Lombok Install](https://projectlombok.org/) for different environment setups.

## How Do I Use JavaCompileEngine?
~~~java
        CompileResult compileResult = Compiler.builder()
                .fileAddress("H:\\fibonacci.java")
                .compileType(CompileType.JAVA)
                .parameters("100")
                .withExceedTime(true)
                .exceedTimeInMillis(3000)
                .onEachProcessListener(new OnEachProcessListener() {
                    @Override
                    public void command(String command, int index) {
                        System.out.printf("Command %d: %s\n",index, command);
                    }

                    @Override
                    public void processResult(String result, int index) {
                        System.out.printf("Command %d: %s\n",index, result);
                    }
                }).build()
                .compile();

        System.out.println("Compiling Finished!");

        System.out.println(Arrays.toString(compileResult.getCommands()));
        System.out.println(compileResult.getCompileState().name());
        System.out.println(compileResult.getDuration());
        System.out.println(compileResult.isWithExceedTime());
~~~

## Contributing

Please read [CONTRIBUTING.md](https://github.com/MohammadNik/JavaCompileEngine/blob/master/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Getting Help
To report a specific problem or feature request, [open a new issue on Github](https://github.com/MohammadNik/JavaCompileEngine/blob/master/CONTRIBUTING.md). For questions, suggestions, or anything else, [Email](mailto:mehditeymorian322@gmail.com) me or Direct me through [Instagram](https://www.instagram.com/nik_teymorian/)


## General Information
- Built With
  -  [Gradle](https://maven.apache.org/) - Automation System
  -  [Intellij IDEA](https://www.jetbrains.com/idea/) - Developing Environment
- Versioning
  -  We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [JavaCompileEngine Tags](https://github.com/MohammadNik/JavaCompileEngine/tags). 
- Authors
  -  **Mehdi Teymorian** [Github Page](https://github.com/MohammadNik) 
  - [contributors](https://github.com/MohammadNik/JavaCompileEngine/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/MohammadNik/JavaCompileEngine/blob/master/LICENSE) file for details
