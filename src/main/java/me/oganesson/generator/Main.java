package me.oganesson.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moandjiezana.toml.Toml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //改成你的Prism Pack Metadata路径
        File file = new File("D:\\examples");
        file.mkdir();
        //输出路径
        File export = new File("D:\\exports");
        export.mkdir();

        if (file.isDirectory()) {
            var files = file.listFiles((dir, name) -> name.endsWith("pw.toml"));
            for (File toml : files) {
                Toml toml1 = new Toml().read(toml);

                var download = toml1.getTable("download");
                if (download.getString("mode").equals("metadata:curseforge")) {
                    var prop = toml1.getTable("update.curseforge");
                    var prjID = prop.getLong("project-id");
                    var fileID = prop.getLong("file-id");

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    File json = new File(export, toml.getName().replace("pw.toml", "curse.json"));
                    json.createNewFile();
                    try (FileWriter writer = new FileWriter(json)) {
                        // 将对象转换为JSON并写入文件
                        gson.toJson(new JSON(prjID, fileID), writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public record JSON(long addonId, long fileId) { }
}