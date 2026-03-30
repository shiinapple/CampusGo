package site.shiinapple.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web 配置类，用于映射静态资源（如上传的图片）
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path:./data/upload/}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取绝对路径，确保映射正确
        File dir = new File(uploadPath);
        String absolutePath = dir.getAbsolutePath();
        
        // Windows 下路径需要处理，这里假设是 Linux/Mac
        String path = "file:" + absolutePath + "/";
        
        // 将 /api/upload/** 的请求映射到物理磁盘目录
        registry.addResourceHandler("/api/upload/**")
                .addResourceLocations(path);
    }
}
