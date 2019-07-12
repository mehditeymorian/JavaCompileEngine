package jce.compiling;

import lombok.AllArgsConstructor;

import java.util.function.BiFunction;

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
            String execCmd = String.format("java -cp %s %s",pathify.getParentPath(),pathify.getName());
            if (parameters != null && !parameters.isEmpty()) execCmd = execCmd.concat(" ").concat(parameters);

            return new String[]{compileCmd,execCmd};
        };
    }

    // TODO: 7/11/2019 NOT WORKING
    private static BiFunction<Pathify,String,String[]> kotlin(){
        return (pathify, parameters) -> {
            String compileCmd = String.format("kotlinc %s -include-runtime -d %s.jar",pathify.getFullPath(), pathify.getFullAddressWithoutExt());
            String execCmd = String.format("kotlin %s.jar", pathify.getFullAddressWithoutExt());
            if (parameters != null && !parameters.isEmpty()) execCmd = execCmd.concat(" ").concat(parameters);

            return new String[]{compileCmd,execCmd};
        };
    }


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
