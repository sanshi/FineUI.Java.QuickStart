package com.fineui.java.quickstart;

import com.fineui.java.core.PageManager;
import com.fineui.java.web.FineUIPageManagerInitializer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * 全局页面级配置初始化：每次首屏渲染前读 {@code Theme} cookie，按用户选择切换主题。
 *
 * <p>因在渲染前（head 输出之前）执行，这里设的主题才来得及影响 head 里输出的主题 CSS 标签、无闪烁。
 * 主题选择页把主题名写入 cookie 后整页刷新，下次请求本类即读到新值。作为 Spring {@code @Component}
 * 自动覆盖框架的空默认实现。
 */
@Component
public class AppPageManagerInitializer implements FineUIPageManagerInitializer {

    @Override
    public void init(PageManager pm, HttpServletRequest request) {
        String theme = cookie(request, "Theme");
        if (theme != null && !theme.isEmpty()) {
            pm.theme(theme);
        }
    }

    private static String cookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}
