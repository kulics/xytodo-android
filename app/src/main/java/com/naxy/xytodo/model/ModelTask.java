package com.naxy.xytodo.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//任务模型
public class ModelTask extends ModelBase {
    //项目字段
    public final static String COL_ID = "id";
    public final static String COL_CONTENT = "content";
    public final static String COL_NOTE = "note";
    public final static String COL_COLOR = "color";
    public final static String COL_TIME_CREATE = "time_create";
    public final static String COL_TIME_TARGET = "time_target";
    public final static String COL_TIME_DONE = "time_done";
    public final static String COL_TIME_SORT = "time_sort";
    public final static String COL_STATUS = "status";
    //属性
    private int id;//主键
    private String content;//内容
    private String note;//备注
    private String color;//颜色
    private int timeCreate;//创建时间
    private int timeTarget;//目标时间
    private int timeDone;//完成时间
    private int timeSort;//排序时间，用来作常规状态自定义排序用
    private int status;//状态
    private List<ModelTaskSub> sub;//子任务

    //构造方法
    public ModelTask() {
        content = "";
        note = "";
        color = "";
        sub = new ArrayList<>();
    }

    //转化为json
    public JSONObject ToJSON() {
        JSONObject temp = new JSONObject();
        try {
            temp.put(COL_ID, id);
            temp.put(COL_CONTENT, content);
            temp.put(COL_NOTE, note);
            temp.put(COL_COLOR, color);
            temp.put(COL_TIME_CREATE, timeCreate);
            temp.put(COL_TIME_TARGET, timeTarget);
            temp.put(COL_TIME_DONE, timeDone);
            temp.put(COL_TIME_SORT, timeSort);
            temp.put(COL_STATUS, status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }


    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }

    public int getTimeCreate() {
        return this.timeCreate;
    }

    public void setTimeCreate(int timeCreate) {
        this.timeCreate = timeCreate;
    }

    public int getTimeDone() {
        return timeDone;
    }

    public void setTimeDone(int timeDone) {
        this.timeDone = timeDone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTimeTarget() {
        return timeTarget;
    }

    public void setTimeTarget(int timeTarget) {
        this.timeTarget = timeTarget;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ModelTaskSub> getSub() {
        return sub;
    }

    public void setSub(List<ModelTaskSub> sub) {
        this.sub = sub;
    }

    public int getTimeSort() {
        return timeSort;
    }

    public void setTimeSort(int timeSort) {
        this.timeSort = timeSort;
    }
}

