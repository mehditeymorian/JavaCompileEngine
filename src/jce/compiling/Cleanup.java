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
        }
    }

    private static void javac(Pathify filePathify){
        File file = new File(filePathify.fullAddressWithoutExt().concat(".class"));
        if (file.exists()) //noinspection ResultOfMethodCallIgnored
            file.delete();
    }

    private static void kotlin(Pathify filePathify){
        File file = new File(filePathify.fullAddressWithoutExt().concat(".jar"));
        if (file.exists()) //noinspection ResultOfMethodCallIgnored
            file.delete();
    }

    private static void cppC(Pathify filePathify){
        File fil = new File("a.exe");
        if (fil.exists())//noinspection ResultOfMethodCallIgnored
            fil.delete();
    }
}
