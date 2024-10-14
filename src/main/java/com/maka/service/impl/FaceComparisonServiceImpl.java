package com.maka.service.impl;

import com.maka.service.FaceComparisonService;
import com.baidu.aip.face.AipFace;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Base64;
@Service
public class FaceComparisonServiceImpl implements FaceComparisonService {
    // 初始化百度API客户端
    private static final String APP_ID = "115865410";
    private static final String API_KEY = "VNSPEBw6uuHFjLqgV4w9WsnK";
    private static final String SECRET_KEY = "G2X1OaeqIgO6umffm5kdGTJuRIQZj5iB";
    private static final AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
    @Override
    public String compareFace(MultipartFile file) throws Exception {
        // 将图片文件转换为Base64编码
        byte[] bytes = file.getBytes();
        String imgStr = Base64.getEncoder().encodeToString(bytes);

        // 设置百度API参数
        HashMap<String, Object> options = new HashMap<>();
        options.put("image_type", "BASE64");
        options.put("liveness_control", "LOW");
        options.put("match_threshold", "50");

        // 调用百度API进行人脸比对
        JSONObject response = client.search(imgStr, "BASE64","lost_people", options);
        if (response.getInt("error_code") == 0) {
            // 处理比对结果
            JSONObject result = response.getJSONObject("result").getJSONArray("user_list").getJSONObject(0);
            String name = result.getString("user_id");
            double similarity = result.getDouble("score");

            return "{" +
                    "\"lostPerson\": \"" + name + "\"," +
                    "\"similarity\": " + similarity +
                    "}";
        } else {
            if(response.getInt("error_code") == 222207)
                return "{" +
                        "\"lostPerson\": \"" + "无" + "\"" +
                        "}";
            throw new Exception("API Error: " + response.getString("error_msg"));
        }
    }
}
