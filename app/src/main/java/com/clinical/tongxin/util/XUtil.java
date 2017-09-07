package com.clinical.tongxin.util;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;

public class XUtil {
    static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("yimeijing.db")
                    // 不设置dbDir时, 默认存储在app的私有目录.
                    //.setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
            .setDbVersion(1)
//            .setDbOpenListener(new DbManager.DbOpenListener() {
//                @Override
//                public void onDbOpened(DbManager db) {
//                    // 开启WAL, 对写入加速提升巨大
//                    db.getDatabase().enableWriteAheadLogging();
//                }
//            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // TODO: ...
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                }
            });
    static DbManager db = x.getDb(daoConfig);
    /**
     * 发送get请求
     * @Title: 网络请求get方式
     * @Description: TODO(网络请求get方式)
     * @param @param url 请求url
     * @param @param map 请求参数
     * @param @param callback 请求后回调
     * @throws
     */
    public static <T> Cancelable Get(String url,Map<String,String>map,CommonCallback<T>callback)
    {
        RequestParams params=new RequestParams(url);
        if(null!=map)
        {
            for(Map.Entry<String, String> entry:map.entrySet())
            {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        Cancelable cancelable=x.http().get(params, callback);
        return cancelable;
    }
    /**
     * 发送Json Post请求
     * @Title: 网络请求Json Post方式
     * @Description: TODO(网络请求Post方式)
     * @param @param url 请求url
     * @param @param jsonobject 请求参数
     * @param @param callback 请求后回调
     * @throws
     */
    public static <T> Cancelable JsonPost(String url, JSONObject jo, CommonCallback<T>callback)
    {
        RequestParams params=new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(jo.toString());
        Cancelable cancelable=x.http().post(params,callback);
        return cancelable;
    }
    /**
     * 发送post请求
     * @Title: 网络请求post方式
     * @Description: TODO(网络请求get方式)
     * @param @param url 请求url
     * @param @param map 请求参数
     * @param @param callback 请求后回调
     * @throws
     */
    public static <T> Cancelable Post(String url,Map<String,String>map,CommonCallback<T> callback)
    {
        RequestParams params=new RequestParams(url);
        if(null!=map)
        {
            for(Map.Entry<String, String> entry:map.entrySet())
            {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        Cancelable cancelable=x.http().post(params, callback);
        return cancelable;
    }
    /**
     *
     * 上传文件
     * @Title: xutils3上传文件
     * @Description: TODO(xutils3上传文件  )
     * @param @param url 上传文件url
     * @param @param map 文件map
     * @param @param callback 上传后回调
     * @throws
     */
    public static <T> Cancelable UpLoadFileAndText(String url,Map<String,Object>map,CommonCallback<T> callback)
    {
        RequestParams params=new RequestParams(url);
        if(null!=map)
        {
            for(Map.Entry<String, Object> entry:map.entrySet())
            {
                params.addBodyParameter(entry.getKey(), entry.getValue(),null);
            }
        }
        Cancelable cancelable=x.http().post(params, callback);
        return cancelable;
    }
    /**
     *
     * 上传文件
     * @Title: xutils3上传文件
     * @Description: TODO(xutils3上传文件  )
     * @param @param url 上传文件url
     * @param @param map 文件map
     * @param @param callback 上传后回调
     * @throws
     */
    public static <T> Cancelable UpLoadFile(String url,Map<String,Object>map,CommonCallback<T> callback)
    {
        RequestParams params=new RequestParams(url);
        if(null!=map)
        {
            for(Map.Entry<String, Object> entry:map.entrySet())
            {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setMultipart(true);
        Cancelable cancelable=x.http().post(params, callback);
        return cancelable;
    }
    /**
     *
     * 下载文件
     * @Title: xutils3下载文件
     * @Description: TODO(xutils3上传文件  )
     * @param @param url 下载文件url
     * @param @param filepath 下载文件保存的路径
     * @param @param callback 下载后回调
     * @throws
     */
    public static <T> Cancelable DownLoadFile(String url,String filepath,CommonCallback<T> callback)
    {
        RequestParams params=new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        //下载的文件保存的路径
        params.setSaveFilePath(filepath);
        params.setAutoRename(false);
        //params.setHeader("RANGE","bytes="+"");
        //params.addHeader("HttpHeaders.Names.CONNECTION", "keep-alive");
        Cancelable cancelable=x.http().get(params, callback);
        return cancelable;
    }
    /**
     * 插入单个对象
     *
     * @param entity 实体类的对象
     * @return true:插入成功 false:插入失败
     */
    public static synchronized boolean save(Object entity)
    {

        try {
            db.save(entity);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 插入多个对象
     *
     * @param entity 实体类的对象
     * @return true:插入成功 false:插入失败
     */
    public static synchronized boolean saveAll(List<?> entity)
    {
        //DbManager db = x.getDb(daoConfig);
        try {
            db.save(entity);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 更新这张表中的所有数据
     *
     * @param entity 实体类的对象
     * @return true:更新成功 false:更新失败
     */
    public static synchronized boolean saveOrUpdate(Object entity) {
        try {
            //DbManager db = x.getDb(daoConfig);
            db.saveOrUpdate(entity);//先去查这个条数据 根据id来判断是存储还是更新 如果存在更新
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 根据参数更新表中的数据
     *
     * @param entity 实体类的对象
     * @param value  要更新的字段
     * @return true:更新成功 false:更新失败
     */
    public static synchronized boolean update(Object entity, String... value) {
        try {
            //DbManager db = x.getDb(daoConfig);
            db.update(entity, value);
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除这个表中的所有数据
     *
     * @param entityClass 实体类的对象
     * @return true:成功 false:失败
     */
    public static synchronized <T> boolean deleteAll(Class<T> entityClass) {
        try {
            //DbManager db = x.getDb(daoConfig);
            db.delete(entityClass);
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 根据条件删除表
     *
     * @param entity 表名称
     * @param colun  列名
     * @param value  值
     * @return true:成功  false:失败
     */
    public static synchronized boolean deleteByWhere(Class<?> entity, String colun, String value) {
        try {
            //DbManager db = x.getDb(daoConfig);
            db.delete(entity, WhereBuilder.b(colun, "=", value));
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 查找 根据id
     *
     * @param cla 要查询的类
     * @param id  要查询的id
     * @return 查询到的数据
     */
    public static synchronized <T> Object searchOne(Class<T> cla, int id) {
        try {
            //DbManager db = x.getDb(daoConfig);
            return db.findById(cla, id);
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }
        return null;
    }
    /**
     * 查找 根据字段
     *
     * @param cla 要查询的类
     * @param column  要查询的字段
     * @param value  字段值
     * @return 查询到的数据
     */
    public static synchronized <T> Object searchOne(Class<T> cla, String column,String value) {
        try {
            //DbManager db = x.getDb(daoConfig);
            return db.selector(cla).where(WhereBuilder.b(column, "=", value));
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }
        return null;
    }
    /**
     * 查找 根据字段
     *
     * @param cla 要查询的类
     * @param column  要查询的字段
     * @param value  字段值
     * @return 查询到的数据
     */
    public static synchronized <T> long searchCount(Class<T> cla, String column,String value) {
        try {
            //DbManager db = x.getDb(daoConfig);
            return db.selector(cla).where(WhereBuilder.b(column, "=", value)).count();
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }
        return 0;
    }
    /**
     * 查找 根据字段
     *
     * @param cla 要查询的类
     * @param column  要查询的字段
     * @param value  字段值
     * @return 查询到的数据
     */
    public static synchronized <T> int searchCount(Class<T> cla, String column,String value, String column2,String value2) {
        try {
            //DbManager db = x.getDb(daoConfig);
            List<T> list=db.selector(cla).where(column, "=", value).and(column2,"=",value2).findAll();
            return list.size();
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }
        return 0;
    }
    /**
     * 正叙查找 没有条件的
     *
     * @param entity 要查询的类
     * @param <T>    要查询的类
     * @return 查询到的数据
     */
    public static synchronized <T> List<T> search(Class<T> entity) {
        try {
            //DbManager db = x.getDb(daoConfig);
            return db.findAll(entity);
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }
        return null;
    }
    public static synchronized <T> List<T> searchOrderBy(Class<T> entity,String column,String asc) {
        try {
            boolean b=asc.equals("asc")?false:true;
            //DbManager db = x.getDb(daoConfig);
            return db.selector(entity).orderBy(column,b).findAll();
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }
        return null;
    }
    /**
     * 倒叙查找所有数据 没有条件的
     *
     * @param entityClass
     * @return 返回数据库中所有的表数据
     */
    public static synchronized <T> List<T> searchDesc(Class<T> entityClass) {
        try {
            //DbManager db = x.getDb(daoConfig);
            return db.selector(entityClass).orderBy("id", true).findAll();
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 倒叙查找所有数据 没有条件的 分页
     *
     * @param entityClass
     * @return 返回数据库中所有的表数据
     */
    public static synchronized <T> List<T> searchDescPage(Class<T> entityClass,int limit,int pageNo) {
        try {
            //DbManager db = x.getDb(daoConfig);
            return db.selector(entityClass).orderBy("id", true).limit(limit).offset((limit + 1) * pageNo).findAll();
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 正叙查找所有数据 有条件的
     *
     * @param entityClass 实体类
     * @param column      定义的查询条件
     * @param value       定义的查询值
     * @return 返回数据库中所有的表数据
     */
    public static synchronized <T> List<T> searchSelector(Class<T> entityClass, String column, String value) {
        try {
            //DbManager db = x.getDb(daoConfig);
            return db.selector(entityClass).where(WhereBuilder.b(column, "=", value)).findAll();
        } catch (Exception e) {
            String str=e.toString();
            return null;
        }
    }
    public static synchronized <T> List<T> searchLikeSelector(Class<T> entityClass, String column, String value) {
        try {
            //DbManager db = x.getDb(daoConfig);
            return db.selector(entityClass).where(WhereBuilder.b(column, "like", "%" + value + "%")).findAll();
        } catch (Exception e) {
            String str=e.toString();
            return null;
        }
    }
    /**
     * 删除表
     *
     * @param entityClass 实体类
     * @return 返回数据库中所有的表数据
     */
    public static synchronized <T> boolean drop(Class<T> entityClass) {
        try {
            //DbManager db = x.getDb(daoConfig);
            db.dropTable(entityClass);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
