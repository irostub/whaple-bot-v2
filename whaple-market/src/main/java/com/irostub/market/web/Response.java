package com.irostub.market.web;

import lombok.Data;

@Data
public class Response <T>{
    private T data;
    private Code code;

    private Response() {
    }

    public Response(T data) {
        this.data = data;
    }

    public static Response<Void> unauthorized(){
        Response<Void> response = new Response<>();
        response.setCode(Code.UNAUTHORIZED);
        return response;
    }

    public static <T> Response<T> ok(T data){
        Response<T> response = new Response<>();
        response.setData(data);
        response.setCode(Code.OK);
        return response;
    }
}
