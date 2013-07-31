package com.lebo.event;

import com.google.common.eventbus.EventBus;
import com.lebo.event.listener.FavoritesCountRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;

/**
 * 全局EventBus。
 *
 * 应用启动时，Spring会调用由@PostConstruct指定的方法，注册事件处理器。
 *
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: AM10:50
 */
@Component
public class ApplicationEventBus extends EventBus implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private Logger logger = LoggerFactory.getLogger(ApplicationEventBus.class);

    /**
     * 遍历listener包里的每个类，注册到EventBus。
     */
    @PostConstruct
    private void registerHandlers() {
        //用class获得包名，是为了获得编译时检查
        registerHandlers(FavoritesCountRecorder.class.getPackage().getName());
        //如果有子package，写在下面..
    }

    private void registerHandlers(String packageName) {
        // Prepare.
        URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));

        // Filter .class files.
        File[] files = new File(root.getFile()).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });

        // Find classes implementing ICommand.
        for (File file : files) {
            String className = file.getName().replaceAll(".class$", "");
            try {
                Class<?> cls = Class.forName(packageName + "." + className);
                if (cls.isAnnotationPresent(Component.class)) {
                    this.register(applicationContext.getBean(cls));
                }
            } catch (ClassNotFoundException e) {
                logger.warn("注册事件处理器出错", e);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}