package jce.compiling;

import lombok.AllArgsConstructor;

import java.util.function.BiFunction;

/** <p>
 *  A program that Use {@link BiFunction} to generate commands.
 *  based on choose compileType and given {@link Pathify},
 *  return proper commands for compile that resource. </p>
 *  Note: This program is Written with Lombok.
 *  @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
@AllArgsConstructor
public enum CompileType {
    JAVAC(javac()),
    JAVA(java()),
    PYTHON3(python3()),
    NODEJS(nodejs()),
    JAVASCRIPT(nodejs()),
    PHP(php()),
    KOTLIN(kotlin()),
    CPPC(cppC());

    public BiFunction<Pathify,String,String[]> commands;


    private static BiFunction<Pathify,String,String[]> javac(){
        return (pathify, parameters) -> {

            String compileCmd = "javac ".concat(pathify.getFullPath());
            String execCmd = String.format("java -cp %s %s",pathify.getParentPath(),pathify.getFileName());
            if (parameters != null && !parameters.isEmpty()) execCmd = execCmd.concat(" ").concat(parameters);

            return new String[]{compileCmd,execCmd};
        };
    }

    // TODO: 7/11/2019 NOT WORKING
    private static BiFunction<Pathify,String,String[]> kotlin(){
        return (pathify, parameters) -> {
            String compileCmd = String.format("kotlinc %s -include-runtime -d %s.jar",pathify.getFullPath(), pathify.fullAddressWithoutExt());
            String execCmd = String.format("kotlin %s.jar", pathify.fullAddressWithoutExt());
            if (parameters != null && !parameters.isEmpty()) execCmd = execCmd.concat(" ").concat(parameters);

            return new String[]{compileCmd,execCmd};
        };
    }

    /**
     * this method is used for both Cpp and C resource files
     */
    private static BiFunction<Pathify,String,String[]> cppC(){
        return (pathify, parameters) -> {
            String compileCmd = "g++ ".concat(pathify.getFullPath());
            String execCmd = "a.exe";
            if (parameters != null && !parameters.isEmpty()) execCmd = execCmd.concat(" ").concat(parameters);

            return new String[]{compileCmd,execCmd};
        };
    }

    private static BiFunction<Pathify,String,String[]> java(){
        return (pathify, parameters) -> oneWordCommand("java", pathify.getFullPath(), parameters);
    }

    private static BiFunction<Pathify,String,String[]> python3(){
        return (pathify, parameters) -> oneWordCommand("python", pathify.getFullPath(), parameters);
    }

    private static BiFunction<Pathify,String,String[]> nodejs(){
        return (pathify, parameters) -> oneWordCommand("node",pathify.getFullPath(),parameters);
    }

    private static BiFunction<Pathify,String,String[]> php(){
        return (pathify, parameters) -> oneWordCommand("php", pathify.getFullPath(), parameters);
    }

    private static String[] oneWordCommand(String command, String fullPath, String parameters){
        String execCmd = String.format("%s %s",command, fullPath);
        if (parameters != null && !parameters.isEmpty()) execCmd = execCmd.concat(" ").concat(parameters);

        return new String[]{execCmd};
    }

}
