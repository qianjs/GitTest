package com.clinical.tongxin.util;

import java.lang.reflect.Type;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import com.clinical.tongxin.entity.ResultJsonP;
import com.google.gson.Gson;

/**
 * @author 石岩
 */
public class JsonResponseParser implements ResponseParser {
	//检查服务器返回的响应头信息
	@Override
	public void checkResponse(UriRequest request) throws Throwable {
	}

	/**
	 * 转换result为resultType类型的对象
	 *
	 * @param resultType  返回值类型(可能带有泛型信息)
	 * @param resultClass 返回值类型
	 * @param result      字符串数据
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {

		ResultJsonP myjson=new Gson().fromJson(result,ResultJsonP.class);
		if(myjson.getS().equals("-1"))
		{
			//Toast.makeText(MyApplication.applicationContext, "操作成功", Toast.LENGTH_SHORT).show();
			return new Gson().fromJson(myjson.getI().getList().toString(), resultClass);
		}
		else
		{
			return null;
		}
	}
}
