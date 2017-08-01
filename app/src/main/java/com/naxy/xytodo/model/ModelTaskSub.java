package com.naxy.xytodo.model;

import org.json.JSONException;
import org.json.JSONObject;

//子任务模型
public class ModelTaskSub extends ModelBase
{
    //项目字段
    public final static String COL_ID = "id";
    public final static String COL_ID_TASK = "id_task";
    public final static String COL_CONTENT = "content";
    public final static String COL_STATUS = "status";
    //属性
    private int id;//主键
    private int idTask;//索引键
    private String content;//内容
    private int status;//状态

    //构造方法
    public ModelTaskSub()
    {
        content = "";
    }

    //转化为json
    public JSONObject ToJSON()
    {
        JSONObject temp = new JSONObject();
        try
        {
            temp.put(COL_CONTENT, content);
            temp.put(COL_STATUS, status);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return temp;
    }


    public int getID()
    {
        return this.id;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public String getContent()
    {
        return this.content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getIdTask()
    {
        return idTask;
    }

    public void setIdTask(int idTask)
    {
        this.idTask = idTask;
    }
}

