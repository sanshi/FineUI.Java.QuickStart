package com.fineui.java.quickstart.pages;

import com.fineui.java.core.EventArgs;
import com.fineui.java.core.FineUIPage;
import com.fineui.java.quickstart.PageBase;
import com.fineui.java.core.FineUIPageBase;
import com.fineui.java.core.MessageBoxIcon;

/**
 * 最简示例（路由 {@code hello}）：一个按钮，点击弹出对话框。
 */
@FineUIPage("hello")
public class HelloModel extends PageBase {

    public void Page_Load(Object sender, EventArgs e) {
    }

    public void btnHello_Click(Object sender, EventArgs e) {
        showAlert("你好 FineUI！", null, MessageBoxIcon.Warning);
    }
}
