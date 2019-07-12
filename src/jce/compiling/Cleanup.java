package jce.compiling;


import java.io.File;

public class Cleanup {

    public static void clean(Pathify filePathify, CompileType type){
        switch (type){
            case JAVAC:
                javac(filePathify);
                break;
            case KOTLIN:
                kotlin(filePathify);
                break;
            case CPPC:
                cppC(filePathify);
                break;
                default:
                    throw new IllegalArgumentException(String.format("No Cleanup need for a %s program ",type.name()));
        }
    }

    private static void javac(Pathify filePathify){
        File file = new File(filePathify.getFullAddressWithoutExt().concat(".class"));
        if (file.exists()) //noinspection ResultOfMethodCallIgnored
            file.delete();
    }

    private static void kotlin(Pathify filePathify){
        File file = new File(filePathify.getFullAddressWithoutExt().concat(".jar"));
        if (file.exists()) //noinspection ResultOfMethodCallIgnored
            file.delete();
    }

    private static void cppC(Pathify filePathify){
        // TODO: 7/12/2019 COMPLETE THIS
    }
}
