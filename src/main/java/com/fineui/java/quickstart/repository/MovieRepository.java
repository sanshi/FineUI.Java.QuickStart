package com.fineui.java.quickstart.repository;

import com.fineui.java.quickstart.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 电影数据访问接口。继承 {@link JpaRepository} 即自带
 * {@code findAll(Pageable)} / {@code findById} / {@code save} / {@code deleteById} / {@code count} 等，
 * 覆盖列表 / 分页 / 排序 / 增 / 改 / 删。
 */
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    /**
     * 按名称模糊搜索 + 分页排序。方法名即查询：{@code Containing} → SQL {@code LIKE %kw%}；
     * {@code IgnoreCase} 使搜索不区分大小写（H2 的 LIKE 默认区分大小写，加它才符合直觉）。
     */
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
