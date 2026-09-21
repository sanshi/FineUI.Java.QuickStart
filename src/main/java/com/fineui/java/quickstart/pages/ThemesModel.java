package com.fineui.java.quickstart.pages;

import com.fineui.java.core.EventArgs;
import com.fineui.java.core.FineUIPage;
import com.fineui.java.quickstart.PageBase;
import com.fineui.java.core.FineUIPageBase;

/**
 * 主题选择器（路由 {@code themes}）：以 IFrame 内嵌在首页的窗口里，点击主题缩略图把主题名写入
 * {@code Theme} cookie 并整页刷新；刷新后由 {@code AppPageManagerInitializer} 读该 cookie 切换主题 CSS。
 * 本页纯前端交互，无服务端逻辑。
 */
@FineUIPage("themes")
public class ThemesModel extends PageBase {

    public void Page_Load(Object sender, EventArgs e) {
    }
}
