package controller;

import dto.Dto;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.ItripComment;
import service.CommentService;
import utils.DtoUtil;
import utils.ErrorCode;
import vo.comment.ItripAddCommentVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @RequestMapping(value = "/addComment",method = RequestMethod.POST,produces = "application/json",headers = "token")
    @ResponseBody
    @ApiOperation(value ="发表评论",notes = "使用json 格式返回发送状态")
    public Dto addComment(HttpServletRequest request, ItripAddCommentVO itripAddCommentVO){
        ItripComment itripComment=new ItripComment();
        String useragent=request.getHeader("user-agent");
        String token=request.getHeader("token");
        try {
            if (itripAddCommentVO.getHotelId() != null) {
                itripComment.setHotelId(itripAddCommentVO.getHotelId());
            }
            if (itripAddCommentVO.getOrderId() != null) {
                itripComment.setOrderId(itripAddCommentVO.getOrderId());
            }
            if (itripAddCommentVO.getProductId() != null) {
                itripComment.setProductId(itripAddCommentVO.getProductId());
            }
            if (itripAddCommentVO.getProductType() != null) {
                itripComment.setProductType(itripAddCommentVO.getProductType());
            }
            if (itripAddCommentVO.getContent() != null) {
                String content = new String(itripAddCommentVO.getContent().getBytes("ISO-8859-1"), "UTF-8");
                itripComment.setContent(content);
            }
            if (itripAddCommentVO.getIsHavingImg() != null) {
                itripComment.setIsHavingImg(itripAddCommentVO.getIsHavingImg());
            }
            if (itripAddCommentVO.getPositionScore() != null) {
                itripComment.setPositionScore(itripAddCommentVO.getPositionScore());
            }
            if (itripAddCommentVO.getFacilitiesScore() != null) {
                itripComment.setFacilitiesScore(itripAddCommentVO.getFacilitiesScore());
            }
            if (itripAddCommentVO.getServiceScore() != null) {
                itripComment.setServiceScore(itripAddCommentVO.getServiceScore());
            }
            if (itripAddCommentVO.getHygieneScore() != null) {
                itripComment.setHygieneScore(itripAddCommentVO.getHygieneScore());
            }
            if (itripAddCommentVO.getTripMode() != null) {
                itripComment.setTripMode(itripAddCommentVO.getTripMode());
            }
            if (itripAddCommentVO.getIsOk() != null) {
                itripComment.setIsOk(itripAddCommentVO.getIsOk());
            }
            if(itripAddCommentVO.getPositionScore()!=null&&itripAddCommentVO.getFacilitiesScore()!=null&&itripAddCommentVO.getHygieneScore()!=null&&itripAddCommentVO.getServiceScore()!=null){
                int countscore=itripAddCommentVO.getPositionScore()+itripAddCommentVO.getFacilitiesScore()+itripAddCommentVO.getHygieneScore()+itripAddCommentVO.getServiceScore();
                itripComment.setScore((countscore/4));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(commentService.addComment(itripComment,useragent,token)){
            return DtoUtil.returnSuccess("发表评论成功");
        }else{
            return DtoUtil.returnFail("用户未登陆或已失效",ErrorCode.AUTH_UNKNOWN);
        }
    }
}

