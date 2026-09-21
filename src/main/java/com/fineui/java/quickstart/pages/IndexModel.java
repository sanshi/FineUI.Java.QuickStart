package com.fineui.java.quickstart.pages;

import com.fineui.java.core.EventArgs;
import com.fineui.java.core.FineUIPage;
import com.fineui.java.quickstart.PageBase;
import com.fineui.java.core.FineUIPageBase;

/**
 * 首页（框架页，路由 {@code index}，{@code GET /} 重定向到此）：左侧菜单树 + 顶部工具栏（主题按钮 /
 * 用户菜单）+ 中间选项卡工作区；点击左侧菜单在右侧以 IFrame 选项卡打开对应页面（树与选项卡的联动由
 * 客户端 {@code F.initTreeTabStrip} 完成）。本页无服务端数据逻辑。
 */
@FineUIPage("index")
public class IndexModel extends PageBase {

    public void Page_Load(Object sender, EventArgs e) {
    }
}
