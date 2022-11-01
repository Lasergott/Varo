package net.howtobedwars.varo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.howtobedwars.varo.config.*;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class VaroFiles {

    @Getter
    private final File folder;

    @Getter
    private final File timeOverConfigFile;

    @Getter
    private TimeOverConfig timeOverConfig;

    @Getter
    private final File mainConfigFile;

    @Getter
    private MainConfig mainConfig;

    @Getter
    private final File teamsConfigFile;

    @Getter
    private TeamsConfig teamsConfig;

    @Getter
    private final File spawnsConfigFile;

    @Getter
    private SpawnsConfig spawnsConfig;

    @Getter
    private final File deadUsersConfigFile;

    @Getter
    private DeadUsersConfig deadUsersConfig;

    public VaroFiles() {
        this.folder = new File("plugins/Varo");
        this.timeOverConfigFile = new File(folder.getPath(), "timeOver.json");
        this.mainConfigFile = new File(folder.getPath(), "config.json");
        this.teamsConfigFile = new File(folder.getPath(), "teams.json");
        this.spawnsConfigFile = new File(folder.getPath(), "spawns.json");
        this.deadUsersConfigFile = new File(folder.getPath(), "deadUsers.json");
        if (!folder.exists()) {
            if(!folder.mkdirs()) {
                Bukkit.getLogger().warning("Folder has not been created for unknown reasons.");
                return;
            }
        }
        try {
            if(Objects.requireNonNull(folder.listFiles()).length == 0) {
                if(!createConfigs()) {
                    Bukkit.getLogger().warning("Configs have not been created for unknown reasons.");
                }
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.timeOverConfig = (TimeOverConfig) readConfig(timeOverConfigFile, TimeOverConfig.class);
        this.mainConfig = (MainConfig) readConfig(mainConfigFile, MainConfig.class);
        this.teamsConfig = (TeamsConfig) readConfig(teamsConfigFile, TeamsConfig.class);
        this.spawnsConfig = (SpawnsConfig) readConfig(spawnsConfigFile, SpawnsConfig.class);
        this.deadUsersConfig = (DeadUsersConfig) readConfig(deadUsersConfigFile, DeadUsersConfig.class);
    }

    private boolean createConfigs() throws IOException {
        boolean created;
        created = mainConfigFile.createNewFile();
        this.mainConfig = MainConfig.create(mainConfigFile);
        saveConfig(mainConfig);

        created = timeOverConfigFile.createNewFile();
        this.timeOverConfig = TimeOverConfig.create(timeOverConfigFile);
        saveConfig(timeOverConfig);

        created = teamsConfigFile.createNewFile();
        this.teamsConfig = TeamsConfig.create(teamsConfigFile);
        saveConfig(teamsConfig);

        created = spawnsConfigFile.createNewFile();
        this.spawnsConfig = SpawnsConfig.create(spawnsConfigFile);
        saveConfig(spawnsConfig);

        created = deadUsersConfigFile.createNewFile();
        this.deadUsersConfig = DeadUsersConfig.create(deadUsersConfigFile);
        saveConfig(deadUsersConfig);
        return created;
    }

    public void saveConfig(Config config) {
        Optional<File> optionalConfigFile = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter(file -> file.getName().equals(config.getFileName()))
                .findFirst();
        if (!optionalConfigFile.isPresent()) {
            Bukkit.getLogger().warning("Config has not been saved because file has not been found.");
            return;
        }
        File configFile = optionalConfigFile.get();
        try (FileWriter fileWriter = new FileWriter(configFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            fileWriter.write(gson.toJson(config));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Config readConfig(File file, Class<? extends Config> clazz) {
        try(FileReader fileReader = new FileReader(file)) {
            return new Gson().fromJson(fileReader, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
