package com.mycarlong.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycarlong.entity.UserEntity;
import com.mycarlong.repository.KakaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

@Service
public class KakaoServiceImpl implements KakaoService {

    @Autowired
    KakaoRepository kakaoRepository;

    @Override
    public String getToken(String code) throws Exception {
        String access_Token = "";

        final String requestUrl = "https://kauth.kakao.com/oauth/token";

        URL url = new URL(requestUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        //서버로 요청 보내기
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id=" + "${spring.security.oauth2.client.registration.kakao.client-id}");
        sb.append("&redirect_uri=http://localhost:3000/login/oauth2/code/kakao");
        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);

        access_Token = element.getAsJsonObject().get("access_token").getAsString();

        br.close();
        bw.close();

        return access_Token;
    }

    @Override
    public ArrayList<Object> getUserInfo(String access_Token) throws Exception {
        ArrayList<Object> list = new ArrayList<>();
        final String requestUrl = "https://kapi.kakao.com/v2/user/me";

        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + access_Token);

        BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = bf.readLine()) != null) {
            result += line;
        }

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);

        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
        JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

        System.out.println("----------properties"+properties);
        System.out.println("----------kakao_account"+kakao_account);

        String ninkname = properties.getAsJsonObject().get("nickname").getAsString();

        list.add(ninkname);

        UserEntity userEntity = new UserEntity();
        kakaoRepository.save(userEntity);

        return list;
    }
}
