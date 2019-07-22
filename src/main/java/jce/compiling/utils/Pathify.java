package main.java.jce.compiling.utils;

import lombok.Getter;

/**
 * <p>
 *  A program that implements some method to
 *  process the path of give address for compiling.</p>
 *  Note: This program is Written with Lombok.
 *  @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
@SuppressWarnings("WeakerAccess")
@Getter
public class Pathify {
    private String fileName;
    private String extension;
    private String fullPath;
    private String parentPath; // return path of file folder


    private Pathify(String fullPath){
        this.fullPath = fullPath;
        String[] separatedPath = getSeparatedPath();
        this.fileName = fileName(separatedPath);
        this.extension = extension(separatedPath);
        this.parentPath = parentPath();
    }

    public static Pathify of(String fullPath){
        return new Pathify(fullPath);
    }

    private String[] getSeparatedPath(){
        return fullPath.split("\\\\");
    }

    /**
     * @return file name with extension in format of [FILE-NAME].[EXTENSION] . for instance Main.java
     */
    public String fileNameExtension(){
        return String.format("%s.%s",getFileName(),getExtension());
    }

    private String fileName(String[] separatedPath){
        return separatedPath[separatedPath.length-1].split("\\.")[0];
    }

    private String extension(String[] separatedPath){
        return separatedPath[separatedPath.length-1].split("\\.")[1];
    }

    private String parentPath(){
        return fullPath.replace(fileNameExtension(),"");
    }

    public String fullAddressWithoutExt(){
        return getParentPath().concat(getFileName());
}

}
