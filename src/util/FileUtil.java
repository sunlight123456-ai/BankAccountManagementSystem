package src.util;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * 文件操作工具类
 */
public class FileUtil {
    /**
     * 从文件读取数据
     */
    public static List<String> readFromFile(String filename){
        List<String> lines = new ArrayList<>();
        File file = new File(filename);
        if(!file.exists()){
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }catch (IOException e){
                System.out.println("创建文件失败： "+e.getMessage());
            }
            return lines;
        }
        try(BufferedReader reader=new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                if(!line.trim().isEmpty()){
                    lines.add(line.trim());
                }
            }
        }catch (IOException e){
            System.out.println("读取文件失败："+e.getMessage());
        }
        return lines;
    }

    /**
     * 写入数据到文件
     */
    public static void writeToFile(String filename, List<String> lines){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for(String line:lines){
                writer.write(line);
                writer.newLine();
            }
        }catch (IOException e){
            System.out.println("写入文件失败： "+ e.getMessage());
        }
    }

}
