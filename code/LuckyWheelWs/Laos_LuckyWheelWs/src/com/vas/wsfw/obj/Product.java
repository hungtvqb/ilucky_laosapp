/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

/**
 *
 * @author nvta8hp0
 */
public class Product {

    private int product_id;
    private String product_name;
    private String list_group;
    private String description;
    private String path;
    private String image_path;
    private String insert_time;
    private int play_times;
    private String rate_value;
    private int rate_times;
    private String last_update;
    private String tags;
    private int status;
    private String product_group_name;
    private String product_info;
    private int display_height;
    private int ratio_percent;

    public int getRatio_percent() {
        return ratio_percent;
    }

    public void setRatio_percent(int ratio_percent) {
        this.ratio_percent = ratio_percent;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getList_group() {
        return list_group;
    }

    public void setList_group(String list_group) {
        this.list_group = list_group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public int getPlay_times() {
        return play_times;
    }

    public void setPlay_times(int play_times) {
        this.play_times = play_times;
    }

    public String getRate_value() {
        return rate_value;
    }

    public void setRate_value(String rate_value) {
        this.rate_value = rate_value;
    }

    public int getRate_times() {
        return rate_times;
    }

    public void setRate_times(int rate_times) {
        this.rate_times = rate_times;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProduct_group_name() {
        return product_group_name;
    }

    public void setProduct_group_name(String product_group_name) {
        this.product_group_name = product_group_name;
    }

    public String getProduct_info() {
        return product_info;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }

    public int getDisplay_height() {
        return display_height;
    }

    public void setDisplay_height(int display_height) {
        this.display_height = display_height;
    }
}
