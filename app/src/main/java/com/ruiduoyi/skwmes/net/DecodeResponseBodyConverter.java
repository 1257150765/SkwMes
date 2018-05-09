package com.ruiduoyi.skwmes.net;

import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Chen on 2018/4/25.
 */

/**
 * 响应数据解析
 * @param <T>
 */
public class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = DecodeResponseBodyConverter.class.getSimpleName();
    private final TypeAdapter<T> adapter;

    DecodeResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //解密字符串
        String temp = new String(value.bytes(), "utf-8");
        //LogWraper.d(TAG,"befreo"+temp);
        String s = temp.replaceAll("\\\\", "");
        //LogWraper.d(TAG,"after"+s);
       // String response = value.string();
        return adapter.fromJson(s);
    }
}
