package ${package}.web.pasm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018/4/10/010.
 *  * Update by whx
 */
@Configuration
@ConfigurationProperties(prefix = "")
public class Config {


    private Integer province;

    private Integer region;

    private Integer district;

    public Integer getProvince() {
        return province;
    }

    public Integer getRegion() {
        return region;
    }

    public Integer getDistrict() {
        return district;
    }

    @Autowired
    private Environment env;


    public String getProperty(String key) {
        return env.getProperty(key);
    }

    public String getProperty(String key, String defValue) {
        return env.getProperty(key, defValue);
    }


    public <T> T getProperty(String key, Class<T> classz) {
        return env.getProperty(key, classz);
    }

    public <T> T getProperty(String key, Class<T> classz, T defValue) {
        return env.getProperty(key, classz, defValue);
    }

    /**
     * 获取临时文件路径
     *
     * @return
     */
    public String getTempFolderPath() {
        return System.getProperty("user.dir") + File.separatorChar + "tempfiles";
    }

    /**
     * 获取永久性存储文件路径
     *
     * @return
     */
    public String getFileFolderPath() {
        return env.getProperty("fileFolderPath", System.getProperty("user.dir") + File.separatorChar + "FileFolder");
    }
}
