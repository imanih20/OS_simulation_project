package com.example.OsProject.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.OsProject.activities.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class FolderUtils implements Parcelable {
    private File dir;

    public FolderUtils(String userName) {
        this.dir = new File(App.HOME_DIR,userName);
        if (!dir.exists()){
            if (dir.mkdirs()){
                Log.i("file","created.");
            }

        }
    }


    protected FolderUtils(Parcel in) {
        dir = (File) in.readSerializable();
    }

    public static final Creator<FolderUtils> CREATOR = new Creator<FolderUtils>() {
        @Override
        public FolderUtils createFromParcel(Parcel in) {
            return new FolderUtils(in);
        }

        @Override
        public FolderUtils[] newArray(int size) {
            return new FolderUtils[size];
        }
    };

    public File[] getCurFolderContents(){
        return dir.listFiles();
    }

    public File[] getFolderContents(String path){
        File file = getValidFile(path);
        if (file == null) return null;
        if (file.exists() && file.isDirectory()){
            return file.listFiles();
        }
        return null;
    }

    public boolean changeDirTo(String dirName){
        File file;
        if (dirName.equals("/")){
            dir = App.ROOT_DIR;
            return true;
        }
        file = getValidFile(dirName);
        if (file == null) return false;
        if (file.exists() && file.isDirectory()){
            dir = file;
            return true;
        }
        return false;
    }

    public void changeToParent(){
        dir = dir.getParentFile();
    }

    public boolean makeDir(String path){
        File file = getValidFile(path);
        if (file == null)return false;
        if (file.exists()){
            file = getNumberFile(file);
        }
        return file.mkdirs();
    }

    public boolean makeFile(String path) throws IOException {
        File file = getValidFile(path);
        if (file == null) return false;
        File parent = file.getParentFile();
        if (parent!=null && !parent.exists()){
            if (!parent.mkdirs()){
                return false;
            }
        }
        if (file.exists()){
            file = getNumberFile(file);
        }
        return file.createNewFile();
    }

    public boolean delete(String name){
        File file = getValidFile(name);
        if (file==null) return false;
        if (file.exists()){
            if (file.isDirectory()){
                return deleteDir(file.getPath());
            }
            return file.delete();
        }
        return false;
    }

    public boolean deleteDir(String path){
        File file = new File(path);
        if (file.exists()){
            File[] children = file.listFiles();
            boolean success = false;
            if (children!=null && children.length>0){
                for (File ch : children){
                    if (ch.isDirectory()){
                        success = deleteDir(ch.getPath());
                    }else if (ch.delete()){
                        success = true;
                    }
                }
            }else {
                if (file.getPath().equals(dir.getPath()))
                    changeToParent();
            }
            if (file.delete()){
                success = true;
            }
            return success;
        }
        return false;
    }

    public boolean move(String source,String target)throws IOException{
        File sourceFile = getValidFile(source);
        if (sourceFile==null)return false;
        if (copy(source,target)){
            return delete(source);
        }
        return false;
    }

    public boolean copy(String source,String target) throws IOException{
        File sourceFile = getValidFile(source);
        File targetFile = getValidFile(target);
        if (sourceFile==null || targetFile==null) return false;
        if (sourceFile.exists()){
            if (sourceFile.isDirectory()){
                targetFile = new File(targetFile,sourceFile.getName());
                target += "/"+sourceFile.getName();
                if (!targetFile.exists() && !targetFile.mkdirs()){
                    throw new IOException("cannot create dir"+ targetFile.getAbsolutePath());
                }
                String[] children = sourceFile.list();
                if (children!=null && children.length>0){
                    for (String child : children){
                        copy(source+"/"+child, target+"/"+child);
                    }
                }
            }else {
                File file;
                if (targetFile.exists()) {
                    if (targetFile.isDirectory()) {
                        file = new File(targetFile, sourceFile.getName());
                    }else {
                        file = targetFile;
                    }
                }else {
                    File directory = targetFile.getParentFile();
                    if (directory == null) return false;
                    if (!directory.exists() && !directory.mkdirs()){
                        throw new IOException();
                    }
                    file = targetFile;

                }
                InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer))>0){
                    out.write(buffer,0,length);
                }
                in.close();
                out.close();
            }
            return true;
        }
        return false;
    }

    public boolean rename(String filePath,String newName){
        File file = getValidFile(filePath);
        if (file==null) return false;
        File newFile = new File(file.getParentFile(),newName);
        if (newFile.exists()){
            newFile = getNumberFile(newFile);
        }
        return file.renameTo(newFile);
    }

    public String showFileContent(String path) throws IOException{
        File file = getValidFile(path);
        if (file==null) return null;
        return getFileContent(file);
    }

    public String getFileContent(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine())!=null){
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    public boolean saveFileContent(String filePath, String content) throws IOException{
        File file = getValidFile(filePath);
        if (file==null) return false;
        if (!file.exists())return false;
        if (file.isDirectory()) return false;
        PrintWriter writer = new PrintWriter(file);
        writer.append(content);
        writer.flush();
        writer.close();
        return true;
    }

    public String getCurrentDir(){
        return dir.getPath();
    }

    public String convertToDirectAddress(String address){
        String[] files = address.split("/");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i<files.length;i++){
            if (i==0)continue;
            if (files[i].equals(".")) continue;
            if (files[i].equals("..")){
                if (i>1) {
                    builder.delete(builder.lastIndexOf(files[i - 1]), builder.length() - 1);
                    continue;
                }else {
                    return null;
                }
            }
            builder.append(files[i]).append("/");
        }
        if (builder.length()>1){
            builder.deleteCharAt(builder.length()-1);
        }
        return builder.toString();
    }
    public File getValidFile(String path){
        if (path.startsWith("/")){
            path = path.substring(1);
        }
        if (path.startsWith(App.HOME_DIR.getName())){
            String adr;
            if (path.equals(App.HOME_DIR.getName())){
                path = App.HOME_DIR.getPath();
            }else {
                adr = convertToDirectAddress(path);
                if (adr == null) return null;
                path = App.HOME_DIR.getPath() + "/" + adr;
            }
        }else {
            path = dir.getPath()+"/"+path;
        }
        return new File(path);
    }
    public File getNumberFile(File file){
        File parent = file.getParentFile();
        String name = file.getName();
        StringBuilder builder = new StringBuilder();
        int number = TextUtils.getNumber(name)+1;
        char[] chars = name.toCharArray();
        for (int i=0;i<chars.length;i++){
            if (i==name.lastIndexOf(".")){
                builder.append(number==0?"":number);
            }
            builder.append(chars[i]);
        }
        return new File(parent,builder.toString());
    }

    public boolean isForbidFile(String path){
        File file = getValidFile(path);
        if (file== null) return false;
        if (file.getPath().equals(App.HOME_DIR.getPath())) return true;
        File parent = file.getParentFile();
        return parent != null && parent.getPath().equals(App.HOME_DIR.getPath());
    }

    public boolean isOwner(String path, String userName){
        File file = getValidFile(path);
        if (file == null) return false;
        if (file.getParentFile() == null) return false;
        if(file.getParentFile().getPath().equals(App.ROOT_DIR.getPath())&&userName.equals("U1001")) {
            return true;
        }
        if (file.getPath().contains(App.HOME_DIR.getPath()+"/"+userName)){
            return true;
        }
        if (!file.getPath().contains(App.HOME_DIR.getPath())&&userName.equals("U1001")){
            return true;
        }
        return file.getPath().contains(App.PUBLIC_DIR.getPath());
    }

    public boolean isValidDirectory(String path){
        File file = getValidFile(path);
        if (file!=null && file.exists()){
            return file.isDirectory();
        }
        return false;
    }

    public boolean isValidTarget(String path, String userName) {
        File file = getValidFile(path);
        if (file == null) return true;
        if (file.getPath().contains(App.PUBLIC_DIR.getPath())){
            return false;
        }
        return !file.getPath().contains(App.HOME_DIR.getPath() + "/" + userName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(dir);
    }
}
