//package com.dianping.cat.registry;
//
//import com.dianping.cat.anno.EnableUrlMonitor;
//import com.dianping.cat.servlet.CatFilter;
//import org.springframework.beans.factory.support.BeanDefinitionBuilder;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.beans.factory.support.GenericBeanDefinition;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
//import org.springframework.core.type.AnnotationMetadata;
//
///**
// * @author 空途
// * @Title: CatDymaticBeanSelectorImport
// * @Descripition: 如 @EnableUrlMonitor 注解存在，动态注册 FilterRegistrationBean
// * @date 2020/7/2
// */
//public class CatDymaticBeanSelectorImport implements ImportBeanDefinitionRegistrar {
//
//    @Override
//    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        if (importingClassMetadata.hasAnnotation(EnableUrlMonitor.class.getName())) {
//            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(FilterRegistrationBean.class);
//            beanDefinitionBuilder.addPropertyValue("filter", new CatFilter())
//                                 .addPropertyValue("urlPatterns", "/*")
//                                 .addPropertyValue("name", "cat-filter")
//                                 .addPropertyValue("order", 1);
//            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionBuilder.getBeanDefinition();
//            registry.registerBeanDefinition(FilterRegistrationBean.class.getName(), beanDefinition);
//        }
//    }
//}
