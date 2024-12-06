package com.liu.mall.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Scanner;

/**
 * @auther liu
 * @description MyBatisPlus代码生成器
 * @date 2020/8/20
 * @github https://github.com/liuzheng
 */
public class MyBatisPlusGenerator {



    public static void main(String[] args) {

        String module=scanner("代码存放模块名");
        String projectPath = System.getProperty("user.dir") +"/" + module;
        //String moduleName = scanner("表模块名");//即com.liu.mall.*中的*
        String[] tableNames = scanner("表名，多个英文逗号分割,输入*可生成多个带该前缀").split(",");
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator(initDataSourceConfig());
        autoGenerator.global(initGlobalConfig(projectPath));
        autoGenerator.packageInfo(initPackageConfig(projectPath));
        autoGenerator.injection(initInjectionConfig(projectPath));
        autoGenerator.template(initTemplateConfig());
        autoGenerator.strategy(initStrategyConfig(tableNames));
        autoGenerator.execute(new VelocityTemplateEngine());
    }

    /**
     * 读取控制台内容信息
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String next = scanner.next();
            if (StrUtil.isNotEmpty(next)) {
                return next;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 初始化全局配置
     */
    private static GlobalConfig initGlobalConfig(String projectPath) {
        return new GlobalConfig.Builder()
                .outputDir(projectPath + "/src/main/java")
                .author("liu")
                .disableOpenDir()
                .enableSwagger()
                .dateType(DateType.ONLY_DATE)
                .build();
    }

    /**
     * 初始化数据源配置
     */
    private static DataSourceConfig initDataSourceConfig() {
        Props props = new Props("generator.properties");
        String url = props.getStr("dataSource.url");
        String username = props.getStr("dataSource.username");
        String password = props.getStr("dataSource.password");
        return new DataSourceConfig.Builder(url,username,password)
                .dbQuery(new MySqlQuery())
                .build();
    }

    /**
     * 初始化包配置
     */
    private static PackageConfig initPackageConfig(String projectPath) {
        Props props = new Props("generator.properties");
        return new PackageConfig.Builder()
                .parent(props.getStr("package.base"))
                .entity("model")
                .build();
    }

    /**
     * 初始化模板配置
     */
    private static TemplateConfig initTemplateConfig() {
        //可以对controller、service、entity模板进行配置
        return new TemplateConfig.Builder().build();
    }

    /**
     * 初始化策略配置
     */
    private static StrategyConfig initStrategyConfig(String[] tableNames) {
        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder.entityBuilder()
                .enableFileOverride()
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .enableLombok()
                .formatFileName("%s")
                .mapperBuilder()
                .enableFileOverride()
                .enableBaseResultMap()
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper")
                .serviceBuilder()
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl")
                .controllerBuilder()
                .enableRestStyle()
                .formatFileName("%sController");
        //当表名中带*号时可以启用通配符模式
        if (tableNames.length == 1 && tableNames[0].contains("*")) {
            String[] likeStr = tableNames[0].split("_");
            String likePrefix = "^"+likeStr[0] +"_.*";
            builder.addInclude(likePrefix);
        } else {
            builder.addInclude(tableNames);
        }
        return builder.build();
    }

    /**
     * 初始化自定义配置
     */
    private static InjectionConfig initInjectionConfig(String projectPath) {
        // 自定义配置
        return new InjectionConfig.Builder().build();
    }

}
