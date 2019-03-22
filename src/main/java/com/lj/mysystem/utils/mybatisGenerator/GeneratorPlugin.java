package com.lj.mysystem.utils.mybatisGenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorPlugin extends PluginAdapter {

    private ShellCallback shellCallback = null;
    private String prefix = "com.lj.mysystem.";
    private boolean overwrite = false;
    private String baseEntity = prefix + "entity.base.BaseEntity";
    private String baseMapper = prefix + "dao.base.BaseMapper";
    private String baseService = prefix + "service.base.BaseService";
    private String baseServiceImpl = prefix + "impl.base.BaseServiceImpl";
    private String baseController = prefix + "controller.base.BaseController";
    private String pageEntity = prefix + "entity.base.PageEntity";
    private String serviceProject;
    private String serviceTarget;
    private String controllerTarget;
    private String controllerProject;
    private String serviceImplProject;
    private String serviceImplTarget;
    private Map map = new HashMap();

    public GeneratorPlugin() {
        shellCallback = new DefaultShellCallback(overwrite);
    }

    @Override
    public boolean validate(List<String> list) {
        this.serviceTarget = properties.getProperty("serviceTarget");
        this.serviceProject = properties.getProperty("serviceProject");
        this.serviceImplTarget = properties.getProperty("serviceImplTarget");
        this.serviceImplProject = properties.getProperty("serviceImplProject");
        this.controllerTarget = properties.getProperty("controllerTarget");
        this.controllerProject = properties.getProperty("controllerProject");
        if (properties.getProperty("overwrite").equals("true")) {
            this.overwrite = true;
        }
        return (!StringUtils.isEmpty(this.serviceProject))
                && (!StringUtils.isEmpty(this.serviceTarget))
                && (!StringUtils.isEmpty(this.controllerProject))
                && (!StringUtils.isEmpty(this.controllerTarget))
                && (!StringUtils.isEmpty(this.serviceImplProject))
                && (!StringUtils.isEmpty(this.serviceImplTarget));
    }

    //避免重复生成代码
    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        try {
            java.lang.reflect.Field field = sqlMap.getClass().getDeclaredField("isMergeable");
            field.setAccessible(true);
            field.setBoolean(sqlMap, false);
        } catch (Exception e) {

        }
        return true;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        IntrospectedColumn primaryKeyCloumn = introspectedTable.getPrimaryKeyColumns().get(0);
        String primaryKey = primaryKeyCloumn.getActualColumnName();
        element.getElements().clear();
        element.getAttributes().clear();
        element.addAttribute(new Attribute("id","selectByPrimaryKey"));
        element.addAttribute(new Attribute("resultType",introspectedTable.getBaseRecordType()));
        element.addAttribute(new Attribute("parameterType",primaryKeyCloumn.getFullyQualifiedJavaType().toString()));

        element.addElement(new TextElement("select * from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        element.addElement(new TextElement("WHERE"));
        element.addElement(new TextElement(primaryKey + " = #{" + primaryKey + ",jdbcType=" + primaryKeyCloumn.getJdbcTypeName() + "}"));
        return super.sqlMapSelectByPrimaryKeyElementGenerated(element,introspectedTable);
    }

    // 方法给sqlmap增加sql
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();

        XmlElement select = new XmlElement("select");
        select.addAttribute(new Attribute("id", "selectAll"));
        select.addAttribute(new Attribute("resultType", introspectedTable.getBaseRecordType()));
        select.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        select.addElement(new TextElement("select * from "+ introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        // 增加空行
        parentElement.addElement(new TextElement(""));
        // 增加注释
        parentElement.addElement(new TextElement("<!--查询全部-->"));
        parentElement.addElement(select);

        // 增加批量删除
        XmlElement batchDel = new XmlElement("delete");
        batchDel.addAttribute(new Attribute("id","batchDeleteByPrimaryKey"));
        batchDel.addElement(new TextElement("DELETE FROM "+introspectedTable.getFullyQualifiedTableNameAtRuntime()+" WHERE "
                + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()+ " IN "));
        batchDel.addElement(new TextElement("<foreach close=\")\" collection=\"array\" index=\"index\" item=\"item\" open=\"(\" separator=\",\">"));
        batchDel.addElement(new TextElement("  #{item}"));
        batchDel.addElement(new TextElement("</foreach>"));
        // 增加空行
        parentElement.addElement(new TextElement(""));
        // 增加注释
        parentElement.addElement(new TextElement("<!--批量删除-->"));
        parentElement.addElement(batchDel);

        // 分页
        // 增加空行
        parentElement.addElement(new TextElement(""));
        // 增加注释
        parentElement.addElement(new TextElement("<!--分页-->"));
        XmlElement findPageList = new XmlElement("select");
        findPageList.addAttribute(new Attribute("id","findPageList"));
        findPageList.addAttribute(new Attribute("parameterType",this.pageEntity));
        findPageList.addAttribute(new Attribute("resultType",introspectedTable.getBaseRecordType()));
        findPageList.addElement(new TextElement("SELECT * \n" +
                "    FROM " +introspectedTable.getFullyQualifiedTableNameAtRuntime() ));
        findPageList.addElement(new TextElement("<where>"));
        findPageList.addElement(new TextElement("  <if test=\"cons.search_str1!=null and cons.search_str1 !=''\">"));
        findPageList.addElement(new TextElement("    "+introspectedTable.getBaseColumns().get(0).getActualColumnName()+" LIKE concat('%',concat(#{cons.search_str1},'%'))"));
        findPageList.addElement(new TextElement("  </if>"));
        findPageList.addElement(new TextElement("</where>"));
        findPageList.addElement(new TextElement("ORDER BY " + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()));
        findPageList.addElement(new TextElement("limit #{start},#{pageSize} " ));
        parentElement.addElement(findPageList);

        // 分页统计
        parentElement.addElement(new TextElement(""));
        parentElement.addElement(new TextElement("<!--分页统计-->"));
        XmlElement findTotalCount = new XmlElement("select");
        findTotalCount.addAttribute(new Attribute("id","findTotalCount"));
        findTotalCount.addAttribute(new Attribute("parameterType",this.pageEntity));
        findTotalCount.addAttribute(new Attribute("resultType","java.lang.Integer"));
        findTotalCount.addElement(new TextElement("SELECT\n" +
                "    count(1)\n" +
                "    FROM "+ introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        findTotalCount.addElement(new TextElement("<where>\n" +
                "      <if test=\"cons.search_str1!=null and cons.search_str1 !=''\">"));
        findTotalCount.addElement(new TextElement("   "+introspectedTable.getBaseColumns().get(0).getActualColumnName()+" LIKE concat('%',concat(#{cons.search_str1},'%'))"));
        findTotalCount.addElement(new TextElement("  </if>\n" +
                "    </where>"));
        parentElement.addElement(findTotalCount);

        // 根据非主键条件查询单个
        parentElement.addElement(new TextElement(""));
        parentElement.addElement(new TextElement("<!--根据非主键条件查询单个-->"));
        XmlElement findByMap = new XmlElement("select");
        findByMap.addAttribute(new Attribute("id","findByMap"));
        findByMap.addAttribute(new Attribute("parameterType","java.util.Map"));
        findByMap.addAttribute(new Attribute("resultType",introspectedTable.getBaseRecordType()));
        findByMap.addElement(new TextElement("SELECT * \n" +
                "    FROM "+introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        findByMap.addElement(new TextElement("<where>\n" +
                "      <if test=\""+introspectedTable.getBaseColumns().get(0).getActualColumnName()+"!=null and "+introspectedTable.getBaseColumns().get(0).getActualColumnName()+"!=''\">"));
        findByMap.addElement(new TextElement("      "+introspectedTable.getBaseColumns().get(0).getActualColumnName()+"=#{"+introspectedTable.getBaseColumns().get(0).getActualColumnName()+"}"));
        findByMap.addElement(new TextElement("  </if>\n" +
                "    </where>"));
        parentElement.addElement(findByMap);

        // 根据非主键条件查询集合
        parentElement.addElement(new TextElement(""));
        parentElement.addElement(new TextElement("<!--根据非主键条件查询集合-->"));
        XmlElement findListByMap = new XmlElement("select");
        findListByMap.addAttribute(new Attribute("id","findListByMap"));
        findListByMap.addAttribute(new Attribute("parameterType","java.util.Map"));
        findListByMap.addAttribute(new Attribute("resultType",introspectedTable.getBaseRecordType()));
        findListByMap.addElement(new TextElement("SELECT * \n" +
                "    FROM "+introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        findListByMap.addElement(new TextElement("<where>\n" +
                "      <if test=\""+introspectedTable.getBaseColumns().get(0).getActualColumnName()+"!=null and "+introspectedTable.getBaseColumns().get(0).getActualColumnName()+"!=''\">"));
        findListByMap.addElement(new TextElement("      "+introspectedTable.getBaseColumns().get(0).getActualColumnName()+"=#{"+introspectedTable.getBaseColumns().get(0).getActualColumnName()+"}"));
        findListByMap.addElement(new TextElement("  </if>\n" +
                "    </where>"));
        findListByMap.addElement(new TextElement("ORDER BY "+introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()));
        parentElement.addElement(findListByMap);

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        IntrospectedColumn primarykey = introspectedTable.getPrimaryKeyColumns().get(0);
        System.out.println(primarykey.getActualColumnName());
    }


    // 可以生成额外的文件
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        for (GeneratedJavaFile javaFile : introspectedTable.getGeneratedJavaFiles()) {
            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType baseModelJavaType = unit.getType();
            String shortName = baseModelJavaType.getShortName();
            if (!shortName.endsWith("Mapper")){
                Configuration cfg = new Configuration(Configuration.getVersion());
                try {
                    cfg.setDirectoryForTemplateLoading(new File("src/main/resources/ftl"));
                    // 生成必要的参数
                    getMap( introspectedTable, baseModelJavaType,  shortName);
                    // 生成service接口
                    generateService(this.map, cfg);
                    generateImplService(this.map, cfg);
                    generateController(this.map, cfg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TemplateException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private Map getMap(IntrospectedTable introspectedTable,FullyQualifiedJavaType baseModelJavaType, String shortName){
        this.map.put("packageName",this.serviceTarget);

        int i = this.serviceTarget.lastIndexOf('.');
        this.map.put("implPackageName",this.serviceImplTarget);
        this.map.put("domainImport",baseModelJavaType.getFullyQualifiedName());
        this.map.put("mapperImport",introspectedTable.getContext().getJavaClientGeneratorConfiguration().getTargetPackage()+"."+shortName+"Mapper");
        // 实体类
        this.map.put("domainName",shortName);
        // 实体类首字母小写
        this.map.put("sdomainName",(new StringBuilder()).append(Character.toLowerCase(shortName.charAt(0))).append(shortName.substring(1)).toString());
        this.map.put("lowerdomainName",shortName.toLowerCase());
        // 基本类引入
        this.map.put("baseServiceImport",this.baseService);
        this.map.put("baseMapperImport",this.baseMapper);
        this.map.put("abStractServiceImport",this.baseServiceImpl);
        // 服务类引入
        this.map.put("serviceImport",this.serviceTarget+"."+shortName+"Service");
        // 主键类型
        this.map.put("pkType",introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().getShortName());

        String pk = introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName();
        this.map.put("primaryKey",pk);
        this.map.put("UprimaryKey",(new StringBuilder()).append(Character.toUpperCase(pk.charAt(0))).append(pk.substring(1)).toString());

        this.map.put("prefix",this.prefix);
        //controller
        this.map.put("controllerPackage",this.controllerTarget);
        // 基本controller类
        this.map.put("baseControllerImport",this.baseController);

        System.out.println(this.map);
        return this.map;
    }

    // 生成service接口
    private void generateService(Map map, Configuration cfg) throws IOException, TemplateException {
        Template t = cfg.getTemplate("service.ftl");
        System.out.println(this.serviceProject+getpath(this.serviceTarget));
        System.out.println(getpath(this.serviceTarget));
        File dir = new File(this.serviceProject+getpath(this.serviceTarget));
        if (!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir+"/"+map.get("domainName")+"Service.java");
        if (file.exists() && !overwrite){
            System.err.println(map.get("domainName")+"Service.java"+"已经存在，停止覆盖！");
            return;
        }
        OutputStream out = new FileOutputStream(file);
        t.process(map, new OutputStreamWriter(out));
        out.close();
    }

    // 生成serviceImpl接口
    private void generateImplService(Map map, Configuration cfg) throws IOException, TemplateException {
        Template t = cfg.getTemplate("serviceImpl.ftl");
        System.out.println(this.serviceImplProject+getpath(this.serviceImplTarget));
        System.out.println(getpath(this.serviceImplTarget));
        File dir = new File(this.serviceImplProject+getpath(this.serviceImplTarget));
        if (!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir+"/"+map.get("domainName")+"ServiceImpl.java");
        if (file.exists() && !overwrite){
            System.err.println(map.get("domainName")+"ServiceImpl.java"+"已经存在，停止覆盖！");
            return;
        }
        OutputStream out = new FileOutputStream(file);
        t.process(map, new OutputStreamWriter(out));
        out.close();
    }

    // 生成serviceController
    private void generateController(Map map, Configuration cfg) throws IOException, TemplateException {
        Template t = cfg.getTemplate("controller.ftl");
        File dir = new File(this.controllerProject+getpath(this.controllerTarget));
        if (!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir+"/"+map.get("domainName")+"Controller.java");
        if (file.exists() && !overwrite){
            System.err.println(map.get("domainName")+"Controller.java"+"已经存在，停止覆盖！");
            return;
        }
        OutputStream out = new FileOutputStream(file);
        t.process(map, new OutputStreamWriter(out));
        out.close();
    }

    // 通过包名获取了路径
    private String getpath(String a){
        String[] arr = a.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String s:arr){
            sb.append("/"+s);
        }
        return sb.toString();
    }

    // 修改生成的实体类
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(baseEntity);
        topLevelClass.setSuperClass(baseEntity);
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    // 修改mapper文件生成
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String model = introspectedTable.getBaseRecordType();
        String idType = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().getShortName();
        interfaze.addImportedType(new FullyQualifiedJavaType(baseMapper) );
        interfaze.addImportedType(new FullyQualifiedJavaType(model));
        interfaze.addSuperInterface(new FullyQualifiedJavaType(baseMapper+"<"+model+","+idType+">"));
        return true;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }
}
