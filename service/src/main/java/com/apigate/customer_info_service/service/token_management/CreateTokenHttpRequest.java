package com.apigate.customer_info_service.service.token_management;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.ContentType;
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
public class CreateTokenHttpRequest {

    @Setter(AccessLevel.NONE)
    private HttpPost request;

    public CreateTokenHttpRequest(String endpoint, String username, String password, String authKey){
        request = new HttpPost(endpoint);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + authKey);
        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());

        List<NameValuePair> formparams = new ArrayList<>();
        formparams.add(new BasicNameValuePair("grant_type", "password"));
        formparams.add(new BasicNameValuePair("username", username));
        formparams.add(new BasicNameValuePair("password", password));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, StandardCharsets.UTF_8);

        request.setEntity(entity);
    }

    private CreateTokenHttpRequest(){}
}
