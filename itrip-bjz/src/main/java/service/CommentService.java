package service;

import pojo.ItripComment;


public interface CommentService {
    public boolean addComment(ItripComment itripComment, String userAgent, String token);
}
