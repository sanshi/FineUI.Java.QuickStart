package com.fineui.java.quickstart.model;

import com.fineui.java.binding.Display;
import com.fineui.java.binding.DisplayFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 电影数据模型（JPA 实体）。
 *
 * <p>Code First：实体注解即表结构，Hibernate 据此在 H2 里建 {@code movie} 表
 * （字段名下划线化：{@code releaseDate} → 列 {@code release_date}）。
 *
 * <ul>
 *   <li>{@code @Display(name)}：字段在界面的显示名——驱动 Grid 列头文字与表单字段标题；</li>
 *   <li>{@code @NotBlank}：必填校验（Jakarta Bean Validation）；</li>
 *   <li>{@code @DisplayFormat(pattern)}：日期在 Grid 里的显示格式；</li>
 *   <li>{@code LocalDate} 类型使编辑表单渲染为日期选择控件（DatePicker）。</li>
 * </ul>
 */
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Display(name = "名称")
    @NotBlank(message = "名称不能为空！")
    private String title;

    @Display(name = "发布日期")
    @DisplayFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Display(name = "类型")
    private String genre;

    @Display(name = "价格")
    @Column(precision = 18, scale = 2)
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
