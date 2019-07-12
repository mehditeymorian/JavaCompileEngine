package jce.compiling;

import lombok.Getter;

@Getter
public class Pathify {
    private String name;
    private String extension;
    private String fullPath;
    private String parentPath; // return path of file folder


    private Pathify(String fullPath){
        this.fullPath = fullPath;
        this.name = buildName();
        this.extension = buildExtension();
        this.parentPath = buildParentPath();
    }

    public static Pathify create(String fullPath){
        return new Pathify(fullPath);
    }

    private String[] getSeparatedPath(){
        return fullPath.split("\\\\");
    }

    public String getNameExtension(){
        String[] separatedPath = getSeparatedPath();
        return separatedPath[separatedPath.length-1];
    }

    private String buildName(){
        return getNameExtension().split("\\.")[0];
    }

    private String buildExtension(){
        return getNameExtension().split("\\.")[1];
    }

    private String buildParentPath(){
        return fullPath.replace(getNameExtension(),"");
    }

    public String getFullAddressWithoutExt(){
        return getParentPath().concat(getName());
    }


}
