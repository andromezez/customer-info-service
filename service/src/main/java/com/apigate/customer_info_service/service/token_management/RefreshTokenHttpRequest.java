package com.apigate.customer_info_service.service.token_management;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bayu Utomo
 * @date 6/7/2021 5:17 PM
 */
@Data
public class RefreshTokenHttpRequest {

    @Setter(AccessLevel.NONE)
    private HttpPost request;

    public RefreshTokenHttpRequest(String endpoint, String refreshToken, String authKey){
        request = new HttpPost(endpoint);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + authKey);

        List<NameValuePair> formparams = new ArrayList<>();
        formparams.add(new BasicNameValuePair("grant_type", "refresh_token"));
        formparams.add(new BasicNameValuePair("refresh_token", refreshToken));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, StandardCharsets.UTF_8);

        request.setEntity(entity);
    }

    private RefreshTokenHttpRequest(){}
}
