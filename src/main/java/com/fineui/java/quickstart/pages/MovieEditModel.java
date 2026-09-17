package com.fineui.java.quickstart.pages;

import com.fineui.java.binding.BindProperty;
import com.fineui.java.binding.HiddenProperty;
import com.fineui.java.core.AbortPageException;
import com.fineui.java.core.ActiveWindow;
import com.fineui.java.core.EventArgs;
import com.fineui.java.core.FineUIPage;
import com.fineui.java.core.FineUIPageBase;
import com.fineui.java.core.MessageBoxIcon;
import com.fineui.java.quickstart.model.Movie;
import com.fineui.java.quickstart.repository.MovieRepository;

/**
 * 编辑电影（路由 {@code movie-edit}）：作为列表页 Window 的 IFrame 内容弹出，{@code ?id=} 指定编辑目标。
 *
 * <p>回发时 {@code @BindProperty} 是空壳、只承载表单里 {@code for} 过的字段，与数据源无关；故保存走
 * <b>read-first</b>：按主键重新读出实体、只覆盖表单出现过的字段再 save（避免把未进表单的字段清零）。
 * 主键用 {@code @HiddenProperty} 随 __FSTATE 往返。
 */
@FineUIPage("movie-edit")
public class MovieEditModel extends FineUIPageBase {

    private final MovieRepository movieRepository;

    /** 编辑目标主键：首屏从 ?id 取，之后随 __FSTATE 往返，供保存时重新读取数据源。 */
    @HiddenProperty
    private int movieId;

    /** 表单绑定根：首屏由 Page_Get 加载供回显；回发时框架兜底新建空壳承载表单值。 */
    @BindProperty
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }

    public MovieEditModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /** 首屏渲染前执行（回发不跑）：读 ?id 加载实体供模板 for 回显；不存在则中止并提示、点确定关父窗。 */
    public void Page_Get(Object sender, EventArgs e) {
        movieId = getQueryInt("id", 0);
        movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            throw new AbortPageException("该电影不存在或已被删除！", ActiveWindow.hideReference());
        }
    }

    public void btnSaveClose_Click(Object sender, EventArgs e) {
        if (!getModelState().isValid()) {
            return;
        }
        // read-first：按主键重新读出完整实体，只覆盖表单里出现过的字段。
        // movieId 随 __FSTATE 往返、客户端可篡改——回发没有 Page_Get，真实项目须在此再做一次鉴权。
        Movie stored = movieRepository.findById(movieId).orElse(null);
        if (stored == null) {
            showNotify("该电影不存在或已被删除！", MessageBoxIcon.Error);
            return;
        }
        stored.setTitle(movie.getTitle());
        stored.setReleaseDate(movie.getReleaseDate());
        stored.setGenre(movie.getGenre());
        stored.setPrice(movie.getPrice());
        movieRepository.save(stored);

        // 成功提示（顶部 toast）+ 关闭本窗口并回发父页（触发列表页 Window1_Close 刷新列表）
        showNotify("修改成功！", MessageBoxIcon.Success);
        ActiveWindow.hidePostBack();
    }
}
