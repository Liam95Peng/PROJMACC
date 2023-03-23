package com.example.projmacc.Comments;

import com.example.projmacc.Register.EmailRequest;
import com.example.projmacc.Register.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CommentCall {
    @POST("createCommentList/")
    Call<CommentResponse> createCommentList(@Body CommentRequest commentRequest);

    @POST("createComment/")
    Call<CommentsResponse> createComment(@Body CommentsRequest commentsRequest);
}
