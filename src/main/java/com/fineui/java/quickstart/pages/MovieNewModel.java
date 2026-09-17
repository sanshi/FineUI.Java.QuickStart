package com.fineui.java.quickstart.pages;

import com.fineui.java.binding.BindProperty;
import com.fineui.java.core.ActiveWindow;
import com.fineui.java.core.EventArgs;
import com.fineui.java.core.FineUIPage;
import com.fineui.java.core.FineUIPageBase;
import com.fineui.java.core.MessageBoxIcon;
import com.fineui.java.quickstart.model.Movie;
import com.fineui.java.quickstart.repository.MovieRepository;

/**
 * 新增电影（路由 {@code movie-new}）：作为列表页 Window 的 IFrame 内容弹出。
 * 表单字段用 {@code for="movie.xxx"} 绑定；保存校验通过后 INSERT，弹「保存成功」并关闭窗口 + 刷新父页列表。
 */
@FineUIPage("movie-new")
public class MovieNewModel extends FineUIPageBase {

    private final MovieRepository movieRepository;

    // 表单绑定根：首屏 movie 为 null，框架兜底新建空壳承载表单值；保存时 id 为 null → INSERT
    @BindProperty
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }

    public MovieNewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void btnSaveClose_Click(Object sender, EventArgs e) {
        if (!getModelState().isValid()) {
            return;
        }
        movieRepository.save(movie);
        // 成功提示（顶部 toast）+ 关闭本窗口并回发父页（触发列表页 Window1_Close 刷新列表）
        showNotify("保存成功！", MessageBoxIcon.Success);
        ActiveWindow.hidePostBack();
    }
}
