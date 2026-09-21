package com.fineui.java.quickstart.pages;

import com.fineui.java.core.EventArgs;
import com.fineui.java.core.FineUIPage;
import com.fineui.java.quickstart.PageBase;
import com.fineui.java.core.FineUIPageBase;
import com.fineui.java.core.GridCommandEventArgs;
import com.fineui.java.core.controls.Grid;
import com.fineui.java.core.controls.TwinTriggerBox;
import com.fineui.java.quickstart.model.Movie;
import com.fineui.java.quickstart.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 电影列表管理页（路由 {@code movie}）：搜索 + 分页 + 排序 + 行编辑（弹窗）+ 行删除 + 新增（弹窗）。
 *
 * <p>数据访问用构造器注入的 {@link MovieRepository}（页面是 prototype Spring Bean）。列表用
 * {@code row-type-from="movies"} 从 {@link #getMovies()} 的泛型推导列头/类型/日期格式。
 */
@FineUIPage("movie")
public class MovieModel extends PageBase {

    private final MovieRepository movieRepository;

    // 控件字段（字段名 == 模板 id，框架按名注入）
    Grid Grid1;
    TwinTriggerBox ttbSearchMessage;

    // Grid 的行模型来源：row-type-from="movies" 从此 getter 的泛型 List<Movie> 反射出行类型
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

    public MovieModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void Page_Load(Object sender, EventArgs e) {
        if (!isPostBack()) {
            loadData();
        }
    }

    /** 搜索 → 计数 → 排序分页 → 绑定（Spring Data 一次查询同时给出当前页数据与总记录数）。 */
    private void loadData() {
        String kw = ttbSearchMessage.getValue().trim();   // RealTextField.getValue 永不返回 null（未设为空串）

        int pageIndex = Grid1.getPageIndex();
        int pageSize = Grid1.getPageSize();
        String sortField = Grid1.getSortField();
        Sort.Direction dir = "DESC".equalsIgnoreCase(Grid1.getSortDirection())
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = (sortField == null || sortField.isEmpty())
                ? PageRequest.of(pageIndex, pageSize)
                : PageRequest.of(pageIndex, pageSize, Sort.by(dir, sortField));

        Page<Movie> page = kw.isEmpty()
                ? movieRepository.findAll(pageable)
                : movieRepository.findByTitleContainingIgnoreCase(kw, pageable);

        Grid1.setRecordCount((int) page.getTotalElements());
        movies = page.getContent();
        Grid1.setDataSource(movies);
        Grid1.dataBind();
    }

    /** 弹窗（新增/编辑）保存后关闭 → 触发本事件刷新列表。 */
    public void Window1_Close(Object sender, EventArgs e) {
        loadData();
    }

    public void Grid1_Sort(Object sender, EventArgs e) {
        loadData();
    }

    public void Grid1_PageIndexChanged(Object sender, EventArgs e) {
        loadData();
    }

    /** 搜索框「清空」触发器：清空文本 + 隐藏清空图标 + 重查。 */
    public void ttbSearchMessage_Trigger1Click(Object sender, EventArgs e) {
        ttbSearchMessage.setValue("");
        ttbSearchMessage.setShowTrigger1(false);
        loadData();
    }

    /** 搜索框「搜索」触发器：显示清空图标 + 重查。 */
    public void ttbSearchMessage_Trigger2Click(Object sender, EventArgs e) {
        ttbSearchMessage.setShowTrigger1(true);
        loadData();
    }

    /** 行命令：删除。行 id 从数据键取（GridCommandEventArgs 只给行号）。 */
    public void Grid1_RowCommand(Object sender, GridCommandEventArgs e) {
        if ("Delete".equals(e.getCommandName())) {
            List<Object[]> keys = Grid1.getDataKeys();
            int idx = e.getRowIndex();
            if (idx < 0 || idx >= keys.size()) {
                return;
            }
            Integer id = Integer.valueOf(String.valueOf(keys.get(idx)[0]));
            if (!movieRepository.existsById(id)) {
                showAlert("指定的电影不存在！");
                return;
            }
            movieRepository.deleteById(id);
            loadData();
        }
    }
}
