package com.clinical.tongxin.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class FragmentEntity {
    private String title;//title类型
    private int img_whole;//图片是否是全部
    private List<ItemFragmententity> imgs;//	图片集

    public int getImg_whole_type() {
        return img_whole;
    }

    public void setImg_whole_type(int img_whole_type) {
        this.img_whole = img_whole_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<ItemFragmententity> getImgs() {
        return imgs;
    }

    public void setImgs(List<ItemFragmententity> imgs) {
        this.imgs = imgs;
    }
}
