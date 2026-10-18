package com.fineui.java.quickstart.pages;

import com.fineui.java.binding.BindProperty;
import com.fineui.java.core.PageContext;
import com.fineui.java.core.ActiveWindow;
import com.fineui.java.core.EventArgs;
import com.fineui.java.core.FineUIPage;
import com.fineui.java.quickstart.PageBase;
import com.fineui.java.core.MessageBoxIcon;
import com.fineui.java.quickstart.model.Movie;
import com.fineui.java.quickstart.repository.MovieRepository;

/**
 * 编辑电影（路由 {@code movie-edit}）：作为列表页 Window 的 IFrame 内容弹出，{@code ?id=} 指定编辑目标。
 *
 * <p>每次请求都按地址中的 id 查询并校验电影。回发的表单对象只承载用户填写的值，
 * 保存时把允许编辑的字段复制到本次查询的电影，避免隐藏字段决定编辑目标。
 */
@FineUIPage("movie-edit")
public class MovieEditModel extends PageBase {

    private final MovieRepository movieRepository;

    /** 本次按地址中的 id 查询并校验的电影；保存事件继续使用同一个目标。 */
    private Movie storedMovie;

    /** 首屏用于回显；回发时只承载用户填写的表单值。 */
    @BindProperty
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }

    public MovieEditModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /** 首屏和回发都核对地址中的电影；首次打开页面时再把它交给表单显示。 */
    public void Page_Init(Object sender, EventArgs e) {
        storedMovie = movieRepository.findById(getQueryIntValue("id")).orElse(null);
        if (storedMovie == null) {
            PageContext.abortPage("该电影不存在或已被删除！", ActiveWindow.hideCommand());
            return;
        }
        if (!isPostBack()) {
            movie = storedMovie;
        }
    }

    public void btnSaveClose_Click(Object sender, EventArgs e) {
        if (!getModelState().isValid()) {
            return;
        }
        // 只更新表单允许编辑的字段，其余数据保留本次从数据库查询的值。
        storedMovie.setTitle(movie.getTitle());
        storedMovie.setReleaseDate(movie.getReleaseDate());
        storedMovie.setGenre(movie.getGenre());
        storedMovie.setPrice(movie.getPrice());
        movieRepository.save(storedMovie);

        // 成功提示（顶部 toast）+ 关闭本窗口并回发父页（触发列表页 Window1_Close 刷新列表）
        showNotify("修改成功！", MessageBoxIcon.Success);
        ActiveWindow.hidePostBack();
    }
}
