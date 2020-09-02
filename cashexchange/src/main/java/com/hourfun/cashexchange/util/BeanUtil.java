package com.hourfun.cashexchange.util;


import org.springframework.context.ApplicationContext;

import com.hourfun.cashexchange.config.ApplicationContextProvider;

public class BeanUtil {

    public static Object getBean(Class clazz) {

        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

        if (applicationContext == null) {
            throw new NullPointerException("Spring is not initialized");
        }

        /*
        String[] names = applicationContext.getBeanDefinitionNames();
        for (int i=0; i<names.length; i++) {
            System.out.println(names[i]);
        }
        */

        return applicationContext.getBean(clazz);
    }

}
