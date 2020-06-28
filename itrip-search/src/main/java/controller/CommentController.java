package controller;

import dto.Dto;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.ItripComment;
import service.HotelService;
import utils.DtoUtil;
import utils.ErrorCode;
import vo.comment.ItripAddCommentVO;
import vo.comment.ItripSearchCommentVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/search")
public class CommentController {
    @Resource
    private HotelService hotelService;
    @RequestMapping(value = "/searchHotelComment",method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "查询酒店评论的信息",notes = "使用json 格式返回hotel数据集合")
    public Dto searchHotelComment(ItripSearchCommentVO itripSearchCommentVO){
        if(itripSearchCommentVO.getPageNo()!=null&&itripSearchCommentVO.getPageSize()!=null){
            int pageno=itripSearchCommentVO.getPageNo();
            int pagesize=itripSearchCommentVO.getPageSize();
            itripSearchCommentVO.setPageNo((pageno-1)*pagesize);
        }
        System.out.println(itripSearchCommentVO.getPageNo()+"----"+itripSearchCommentVO.getPageSize());
        List<ItripComment> list=hotelService.selHotelCommentList(itripSearchCommentVO);
        if(list.size()>0){
            return DtoUtil.returnDataSuccess(list);
        }else{
            return DtoUtil.returnFail("查询酒店评论集合失败", ErrorCode.AUTH_UNKNOWN);
        }
    }
}

