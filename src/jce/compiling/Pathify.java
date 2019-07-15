package jce.compiling;

import lombok.Getter;

/**
 * <p>
 *  A program that implements some method to
 *  process the path of give address for compiling.</p>
 *  Note: This program is Written with Lombok.
 *  @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
@Getter
public class Pathify {
    private String fileName;
    private String extension;
    private String fullPath;
    private String parentPath; // return path of file folder


    private Pathify(String fullPath){
        this.fullPath = fullPath;
        this.fileName = fileName();
        this.extension = extension();
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
        String[] separatedPath = getSeparatedPath();
        return separatedPath[separatedPath.length-1];
    }

    private String fileName(){
        return fileNameExtension().split("\\.")[0];
    }

    private String extension(){
        return fileNameExtension().split("\\.")[1];
    }

    private String parentPath(){
        return fullPath.replace(fileNameExtension(),"");
    }

    public String fullAddressWithoutExt(){
        return getParentPath().concat(getFileName());
}


}
