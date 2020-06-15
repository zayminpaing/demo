package com.awbagroup.awbacropai;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JsonPlaceHolderApi {

    @Multipart
    @POST("awbapredictionchannelapi/predictimage")
//    Call<List<Post>> getPosts(@PartMap HashMap<String, RequestBody> params);
    //@POST("posts")
    Call<List<Post>> getPosts(@Part MultipartBody.Part image);
}
