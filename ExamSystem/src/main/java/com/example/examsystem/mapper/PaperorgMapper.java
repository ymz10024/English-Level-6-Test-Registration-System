package com.example.examsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.examsystem.entity.PaperView;
import com.example.examsystem.entity.Paperorg;
import com.example.examsystem.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

//在这个接口里也可以自己实现复杂的sql语句
@Mapper //MyBatis注解，Mapper指示这是一个mapper接口，映射到sql语句，BaseMapper接口是包含CRUD的通用接口
@Repository //Spring注解，Repository指示这个接口与数据库交互，spring将会自动生成一个PaperinfoMapper实例作为spring bean，可以将其注入到依赖它的其他组件中
public interface PaperorgMapper extends BaseMapper<Paperorg> {
    @Select("SELECT * FROM `question` NATURAL JOIN `paperorg` WHERE question.id = paperorg.question_id and paperorg.paperinfo_id = #{paperinfoId} ORDER BY paperorg.order_id")
    Page<PaperView> SelectPaperorgById(Integer paperinfoId, Page<PaperView> ipage);
}
