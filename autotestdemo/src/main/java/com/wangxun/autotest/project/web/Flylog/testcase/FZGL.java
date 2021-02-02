package com.wangxun.autotest.project.web.Flylog.testcase;
import com.wangxun.autotest.project.web.Flylog.frame.Flylog;
import com.wangxun.util.StringUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
@Listeners
public class FZGL {
    Flylog Flylog= new Flylog();
    String changdu33=StringUtil.getRandomStr(33);
    String fenzumingcheng=StringUtil.getRandomStr(6);
    @BeforeClass
    public void setUp(){
        Flylog.initialTestData();
        Flylog.runChromeApp();
        Flylog.logClassInfo("分组管理");
    }
    @AfterClass
    public void tearDown() {
        Flylog.quit();
    }
    @Test
    public void test01_add(){
        Flylog.logTestDescription("新增分组");
        Flylog.get("http://10.4.149.222:9190/flylog-search-web/#/search");
        Flylog.waitDisplay("左侧菜单","分组管理");
        Flylog.clickElement("左侧菜单","分组管理");
        Flylog.clickElement("分组管理","创建分组");
        Flylog.waitDisplay("创建分组","分组名称");
        Flylog.sendKeys("创建分组","分组名称","UI1自动化_-"+fenzumingcheng);
        Flylog.clickElement("创建分组","保存");
        Flylog.assertContains(Flylog.getElementText("分组列表", "第一个分组名称"),"UI1自动化_-"+fenzumingcheng);
        Flylog.logSuccessMessage("分组增加成功");
    }
    @Test
    public void test02_search(){
        Flylog.logTestDescription("搜索分组");
        Flylog.get("http://10.4.149.222:9190/flylog-search-web/#/search");
        Flylog.waitDisplay("左侧菜单","分组管理");
        Flylog.clickElement("左侧菜单","分组管理");
        Flylog.waitDisplay("分组列表","搜索输入框");
        Flylog.sendKeys("分组列表","搜索输入框","UI1自动化_-"+fenzumingcheng);
        Flylog.clickElement("分组列表","搜索");
        Flylog.waitDisplay("分组列表","第一个分组名称");
        Flylog.assertContains(Flylog.getElementText("分组列表", "第一个分组名称"),"UI1自动化_-"+fenzumingcheng);
        Flylog.logSuccessMessage("搜索分组成功");
    }
    @Test
    public void test03_edit(){
        Flylog.logTestDescription("编辑分组");
        Flylog.get("http://10.4.149.222:9190/flylog-search-web/#/search");
        Flylog.waitDisplay("左侧菜单","分组管理");
        Flylog.clickElement("左侧菜单","分组管理");
        Flylog.waitDisplayAndclickElement("分组列表","第一个分组编辑");
        Flylog.waitDisplay("创建分组","分组名称");
        Flylog.clear("创建分组","分组名称");
        Flylog.sendKeys("创建分组","分组名称","222"+fenzumingcheng);
        Flylog.clickElement("创建分组","保存");
        Flylog.waitDisplay("分组列表","第一个分组名称");
        Flylog.assertContains(Flylog.getElementText("分组列表", "第一个分组名称"),"222"+fenzumingcheng);
        Flylog.logSuccessMessage("编辑成功");
    }
    @Test
    public void test07_xiangmuguanli(){
        Flylog.logTestDescription("项目管理");
        Flylog.get("http://10.4.149.222:9190/flylog-search-web/#/search");
        Flylog.waitDisplay("左侧菜单","分组管理");
        Flylog.clickElement("左侧菜单","分组管理");
        Flylog.waitDisplayAndclickElement("分组列表","第一个分组项目管理");
        Flylog.waitDisplay("项目管理","创建项目");
        Flylog.assertContains(Flylog.getElementText("项目管理", "创建项目"),"创建项目");
        Flylog.logSuccessMessage("");
    }
    @Test
    public void test04_delete(){
        Flylog.logTestDescription("删除分组");
        Flylog.get("http://10.4.149.222:9190/flylog-search-web/#/search");
        Flylog.waitDisplay("左侧菜单","分组管理");
        Flylog.clickElement("左侧菜单","分组管理");
        Flylog.waitDisplay("分组列表","第一个分组名称");
        String fenzu=Flylog.getElementAttribute("分组列表","第一个分组名称","value");
        String fenzu2=Flylog.getElementText("分组列表","第一个分组名称");
        String len=Flylog.getElementTextLen("分组列表","第一个分组名称");
        Flylog.waitDisplayAndclickElement("分组列表","第一个分组删除");
        Flylog.waitDisplayAndclickElement("分组删除对话框","确定");
        Flylog.waitDisplay("分组列表","第一个分组名称");
        Flylog.assertNotContains(Flylog.getElementText("分组列表", "第一个分组名称"),fenzu2);
        Flylog.logSuccessMessage("删除成功");
    }
    @Test
    public void test05_add(){
        Flylog.logTestDescription("新增分组特殊字符校验");
        Flylog.get("http://10.4.149.222:9190/flylog-search-web/#/search");
        Flylog.waitDisplay("左侧菜单","分组管理");
        Flylog.clickElement("左侧菜单","分组管理");
        Flylog.waitDisplayAndclickElement("分组管理","创建分组");
        Flylog.waitDisplay("创建分组","分组名称");
        Flylog.sendKeys("创建分组","分组名称","！@");
        Flylog.clickElement("创建分组","保存");
        Flylog.waitDisplay("创建分组","分组tooltip");
        Flylog.assertContains(Flylog.getElementText("创建分组", "分组tooltip"),"内容格式不符合要求");
        Flylog.clickElement("创建分组","关闭");
        Flylog.logSuccessMessage("校验成功");
    }
    @Test
    public void test06_bitian(){
        Flylog.logTestDescription("新增分组必填校验");
        Flylog.get("http://10.4.149.222:9190/flylog-search-web/#/search");
        Flylog.waitDisplay("左侧菜单","分组管理");
        Flylog.clickElement("左侧菜单","分组管理");
        Flylog.waitDisplayAndclickElement("分组管理","创建分组");
        Flylog.waitDisplay("创建分组","分组名称");
        Flylog.sendKeys("创建分组","分组名称","");
        Flylog.clickElement("创建分组","保存");
        Flylog.waitDisplay("创建分组","分组tooltip");
        Flylog.assertContains(Flylog.getElementText("创建分组", "分组tooltip"),"请填写此字段");
        Flylog.clickElement("创建分组","关闭");
        Flylog.logSuccessMessage("校验成功");
    }
    @Test
    public void test07_changdu(){
        Flylog.logTestDescription("新增分组长度校验");
        Flylog.get("http://10.4.149.222:9190/flylog-search-web/#/search");
        Flylog.waitDisplay("左侧菜单","分组管理");
        Flylog.clickElement("左侧菜单","分组管理");
        Flylog.waitDisplayAndclickElement("分组管理","创建分组");
        Flylog.waitDisplay("创建分组","分组名称");
        Flylog.sendKeys("创建分组","分组名称",changdu33);
        Flylog.clickElement("创建分组","保存");
        Flylog.waitDisplay("创建分组","分组tooltip");
        Flylog.assertContains(Flylog.getElementText("创建分组", "分组tooltip"),"最多输入32个字符");
        Flylog.clickElement("创建分组","关闭");
        Flylog.logSuccessMessage("校验成功");
    }
}
