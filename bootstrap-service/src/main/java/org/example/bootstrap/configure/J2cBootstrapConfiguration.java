package org.example.bootstrap.configure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 添加j2c配置源
 *
 * @author VNElinpe
 * @since 2023/4/3
 **/
public class J2cBootstrapConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(J2cBootstrapConfiguration.class);
    private static final String SAVE_PATH = "D:/Workspace/Temp/config.properties";
    private static final String J2C_MOCK_PATTERN = "ENC(J2C_MOCK)";
    private static final String J2C_PROPERTY_SOURCE_NAME = "j2cProperties";
    private static final String ENABLE_J2C_PROXY_PLACEHOLDER = "${j2c.proxy.enable:true}";

    private static final HashSet<String> PROPERTY_SOURCE_FILTER = new HashSet<>();

    static  {
        Path dir = Paths.get(SAVE_PATH).getParent();
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                LOGGER.error("create dir for j2c properties fail! {}", e);
            }
        }
        // 配置源过滤列表
        PROPERTY_SOURCE_FILTER.add("configurationProperties");
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        if (Boolean.FALSE.toString().equalsIgnoreCase(environment.resolvePlaceholders(ENABLE_J2C_PROXY_PLACEHOLDER))) {
            return;
        }
        // 获取j2c配置
        Map<String, String> propertiesMap = Collections.unmodifiableMap(loadProperties());
        MutablePropertySources propertySources = environment.getPropertySources();
        // 创建代理
        propertySources.stream().filter(item -> !PROPERTY_SOURCE_FILTER.contains(item.getName()))
                .filter(item -> !(item instanceof J2cPropertySourceProxy))
                .collect(Collectors.toList())
                .forEach(item -> {
                    String name = item.getName();
                    propertySources.replace(name, new J2cPropertySourceProxy(name, propertiesMap, item));
                });
        // 添加j2c配置源
        propertySources.addLast(new J2cPropertySourceProxy(J2C_PROPERTY_SOURCE_NAME, propertiesMap, null));
    }

    /**
     * 获取j2c配置，先查本地，如果没有就从配置中心获取
     * @return 配置信息(Map)
     */
    private Map<String, String> loadProperties() {
        Properties properties = new Properties();
        // 从本地加载
        Path propertiesPath = Paths.get(SAVE_PATH);
        if (Files.exists(propertiesPath)) {
            try (BufferedReader reader = Files.newBufferedReader(propertiesPath, StandardCharsets.UTF_8)) {
                properties.load(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (properties.isEmpty()) {
            // 读取j2c配置
            properties.put("spring.application.name", "vnelinpe");
            // 保存
            try (BufferedWriter writer = Files.newBufferedWriter(propertiesPath, StandardCharsets.UTF_8)) {
                properties.store(writer, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return (Map) properties;
    }

    private class J2cPropertySourceProxy extends PropertySource<Map<String, String>> {
        private final PropertySource delegate;

        public J2cPropertySourceProxy(String name, Map<String, String> source, PropertySource delegate) {
            super(name, source);
            this.delegate = delegate;
        }

        @Override
        public Object getProperty(String name) {
            if (delegate == null || delegate instanceof J2cPropertySourceProxy) {
                return source.get(name);
            }
            Object value = delegate.getProperty(name);
            if (value instanceof String && ((String) value).equalsIgnoreCase(J2C_MOCK_PATTERN)) {
                value = source.get(name);
            }
            return value;
        }
    }
}
