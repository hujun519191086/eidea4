package com.dsdl.eidea.devs;

import com.dsdl.eidea.core.entity.bo.TableMetaDataBo;
import com.dsdl.eidea.core.service.TableService;
import com.dsdl.eidea.devs.model.GenModelDto;
import com.dsdl.eidea.devs.model.GenSettings;
import com.dsdl.eidea.devs.service.CodeGenerationService;
import com.dsdl.eidea.devs.strategy.ControllerGenerateStrategy;
import com.dsdl.eidea.devs.strategy.PoGenerateStrategy;
import com.dsdl.eidea.devs.strategy.ServiceGenerateStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘大磊 on 2016/12/6 15:07.
 * 项目代码输出文件入口
 */
public class GenerateCodeMain implements CodeGenerationService {
    private TableService tableService;

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        GenerateCodeMain codeMain = new GenerateCodeMain();
        codeMain.tableService = applicationContext.getBean(TableService.class);

        GenModelDto genModelDto = new GenModelDto("core_report_settings", "ReportSettings", "core", "报表设置");
        genModelDto.setBasePackage("com.dsdl.eidea");
        List<GenModelDto> list = new ArrayList<>();
        list.add(genModelDto);

        GenSettings genSettings = new GenSettings();
        /**
         * 设置项目跟目录
         */
        genSettings.setRootPath("D:\\dsdl\\code\\eidea4\\");
        /**
         * 设置 controller 和 jsp文件输出项目位置
         */
        genSettings.setControllerPath(genSettings.getRootPath() + "eweb");
        /**
         * 设置业务逻辑代码输出项目位置
         */
        genSettings.setOutputPath(genSettings.getRootPath() + "ebase");
        genSettings.setGenModelDtoList(list);
        codeMain.generateCode(genSettings);
        System.out.println("执行完毕");
        System.exit(0);
    }


    public void generateCode( GenSettings genSettings) {

        for (GenModelDto genModelDto : genSettings.getGenModelDtoList()) {
            TableMetaDataBo tableMetaDataBo = tableService.getTableDescription(genModelDto.getTableName());
            /**
             * 生成PO代码
             */
            PoGenerateStrategy poGenerateStrategy = new PoGenerateStrategy(tableMetaDataBo, genModelDto);
            poGenerateStrategy.generateModel(genSettings.getOutputPath());
            System.out.println("生成Po类完成");

            ServiceGenerateStrategy serviceGenerateStrategy=new ServiceGenerateStrategy(genModelDto,tableMetaDataBo);
            serviceGenerateStrategy.generateInterface(genSettings.getOutputPath());
            serviceGenerateStrategy.generateServiceclass(genSettings.getOutputPath());
            /**
             * 生成Controller 部分代码
             */
            ControllerGenerateStrategy controllerGenerateStrategy=new ControllerGenerateStrategy(genModelDto,tableMetaDataBo);
            controllerGenerateStrategy.generateController(genSettings.getControllerPath());
        }

    }
}
