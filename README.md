# JavaCompileEngine
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bc5268d7ccb74ede9bf16b482ebfea79)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mehditeymorian/JavaCompileEngine&amp;utm_campaign=Badge_Grade) | [View JCE's Documentation](https://github.com/mehditeymorian/JavaCompileEngine/wiki)

JavaCompileEngine is an efficient open source Compiling Library, usable in Java frameworks for compiling programming languages files and Processing Commands with instant feedback. I wrote this project out of curiosity but it's useful in many situations e.g. Programming Contests Sites, Online Compilers, Online Code Learning and etc.  

![JavaCompileEngine](https://github.com/mehditeymorian/JavaCompileEngine/blob/master/images/javaCompileEngineLandScape.png)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Downloading
You can download a **jar version** from GitHub's [releases page](https://github.com/mehditeymorian/JavaCompileEngine/releases) for using as a library.  
Or you can Clone the project

Clone Over HTTPS
~~~git
 $ git clone https://github.com/mehditeymorian/JavaCompileEngine.git
~~~
Clone Over SSH
~~~git
 $ git clone git@github.com:mehditeymorian/JavaCompileEngine.git
~~~
**Note**: This Project used Lombok. check [Lombok Install](https://projectlombok.org/) for different environment setups if you want to develope to project otherwise, it's not neccessary.   
**Note**: For working properly, you need to add [Gson](https://github.com/google/gson) to your project.

## How Do I Use JavaCompileEngine?
~~~java
        CompileResult compileResult =  Compiler.builder()
                .fileAddress("H:\\m.java")
                .fileLanguage(Compiler.JAVA)
                .withExceedTime(true)
                .exceedTimeInMillis(200)
                .onEachProcessListener(new OnEachProcessListener() {
                    @Override
                    public void command(String command, int index) {
                        System.out.printf("Command%d: %s\n",index+1,command);
                    }

                    @Override
                    public void processResult(String result, int index) {
                        System.out.printf("Result%d: %s\n",index+1,result);
                    }
                }).build().compile();

        System.out.println("Compiling Finished!");

        System.out.println(Arrays.toString(compileResult.getCommands()));
        System.out.println(compileResult.getCompileState().name());
        System.out.println(compileResult.getDuration());
        System.out.println(compileResult.isWithExceedTime());
~~~
See [Documentation](https://github.com/mehditeymorian/JavaCompileEngine/wiki) for more Information.

## Contributing

Please read [CONTRIBUTING.md](https://github.com/mehditeymorian/JavaCompileEngine/blob/master/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Getting Help
To report a specific problem or feature request, [open a new issue on Github](https://github.com/mehditeymorian/JavaCompileEngine/blob/master/CONTRIBUTING.md). For questions, suggestions, or anything else, [Email](mailto:mehditeymorian322@gmail.com) me


## General Information
- Built With
  -  [Gradle](https://maven.apache.org/) - Automation System
  -  [Intellij IDEA](https://www.jetbrains.com/idea/) - Developing Environment
- Versioning
  -  We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [JavaCompileEngine Tags](https://github.com/mehditeymorian/JavaCompileEngine/tags). 
- Authors
  -  **Mehdi Teymorian** [Github Page](https://github.com/mehditeymorian) 
  - [contributors](https://github.com/mehditeymorian/JavaCompileEngine/graphs/contributors) who participated in this project.
  
## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/mehditeymorian/JavaCompileEngine/blob/master/LICENSE) file for details
