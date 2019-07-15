package jce.compiling;


import java.io.File;

/**
 * Cleanup is a program that implements
 * a self cleaning up system after compiling a file.
 * some of the files after compiling produce extra files such as jar file or etc.
 */
 class Cleanup {

    static void clean(Pathify filePathify, CompileType type){
        switch (type){
            case JAVAC:
                javac(filePathify);
                break;
            case KOTLIN:
                kotlin(filePathify);
                break;
            case CPPC:
                cppC();
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

    /**
     * this method is used for both Cpp and C resource files
     */
    private static void cppC(){
        File fil = new File("a.exe");
        if (fil.exists())//noinspection ResultOfMethodCallIgnored
            fil.delete();
    }
}
