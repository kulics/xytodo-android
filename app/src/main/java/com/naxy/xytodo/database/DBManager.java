package com.naxy.xytodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.naxy.xytodo.model.ModelTask;
import com.naxy.xytodo.model.ModelTaskSub;

import java.util.ArrayList;
import java.util.List;

//数据库管理器
public class DBManager
{
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context)
    {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        // db = helper.getWritableDatabase();
    }

    //清空数据库
    public void DBClear()
    {
        try
        {
            db = helper.getWritableDatabase();
            db.execSQL(TableTask.TableUpgrade());
            db.execSQL(TableTask.TableCreate());
            db.execSQL(TableTaskSub.TableUpgrade());
            db.execSQL(TableTaskSub.TableCreate());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

    //获取所有任务
    public List<ModelTask> TaskGetAll()
    {
        List<ModelTask> rList = new ArrayList<>();
        try
        {
            db = helper.getWritableDatabase();
            Cursor c = db.query(TableTask.TableName(), TableTask.TableColumns(), null, null, null, null, null);
            while (c.moveToNext())
            {
                ModelTask temp = new ModelTask();
                temp.setID(c.getInt(c.getColumnIndex(ModelTask.COL_ID)));
                temp.setContent(c.getString(c.getColumnIndex(ModelTask.COL_CONTENT)));
                temp.setNote(c.getString(c.getColumnIndex(ModelTask.COL_NOTE)));
                temp.setColor(c.getString(c.getColumnIndex(ModelTask.COL_COLOR)));
                temp.setTimeCreate(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_CREATE)));
                temp.setTimeTarget(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_TARGET)));
                temp.setTimeDone(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_DONE)));
                temp.setTimeSort(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_SORT)));
                temp.setStatus(c.getInt(c.getColumnIndex(ModelTask.COL_STATUS)));
                rList.add(temp);
            }
            c.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
        return rList;
    }

    //按时间倒序获取所有任务
    public List<ModelTask> TaskGetTime(String param)
    {
        List<ModelTask> rList = new ArrayList<>();
        try
        {
            db = helper.getWritableDatabase();
            String whereParams = "";
            switch (param)
            {
                case "today":
                    break;
                case "all":
                    break;
                case "todo":
                    whereParams = "status = 0";
                    break;
                case "done":
                    whereParams = "status = 1";
                    break;
            }
            Cursor c = db.query(TableTask.TableName(), TableTask.TableColumns(), whereParams, null, null, null, ModelTask.COL_TIME_SORT + " desc");
            while (c.moveToNext())
            {
                ModelTask temp = new ModelTask();
                temp.setID(c.getInt(c.getColumnIndex(ModelTask.COL_ID)));
                temp.setContent(c.getString(c.getColumnIndex(ModelTask.COL_CONTENT)));
                temp.setNote(c.getString(c.getColumnIndex(ModelTask.COL_NOTE)));
                temp.setColor(c.getString(c.getColumnIndex(ModelTask.COL_COLOR)));
                temp.setTimeCreate(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_CREATE)));
                temp.setTimeTarget(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_TARGET)));
                temp.setTimeDone(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_DONE)));
                temp.setTimeSort(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_SORT)));
                temp.setStatus(c.getInt(c.getColumnIndex(ModelTask.COL_STATUS)));
                rList.add(temp);
            }
            c.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
        return rList;
    }

    //获取任务
    public ModelTask TaskGet(int id)
    {
        ModelTask temp = new ModelTask();
        try
        {
            db = helper.getWritableDatabase();
            String[] whereParams = new String[]{id + ""};
            Cursor c = db.query(TableTask.TableName(), TableTask.TableColumns(), ModelTask.COL_ID + " = ? ", whereParams, null, null, null);
            while (c.moveToNext())
            {
                temp.setID(c.getInt(c.getColumnIndex(ModelTask.COL_ID)));
                temp.setContent(c.getString(c.getColumnIndex(ModelTask.COL_CONTENT)));
                temp.setNote(c.getString(c.getColumnIndex(ModelTask.COL_NOTE)));
                temp.setColor(c.getString(c.getColumnIndex(ModelTask.COL_COLOR)));
                temp.setTimeCreate(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_CREATE)));
                temp.setTimeTarget(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_TARGET)));
                temp.setTimeDone(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_DONE)));
                temp.setTimeSort(c.getInt(c.getColumnIndex(ModelTask.COL_TIME_SORT)));
                temp.setStatus(c.getInt(c.getColumnIndex(ModelTask.COL_STATUS)));
            }
            c.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
        return temp;
    }

    //添加任务
    public int TaskAdd(ModelTask model)
    {
        int id = 0;
        try
        {
            db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(ModelTask.COL_CONTENT, model.getContent());
            cv.put(ModelTask.COL_NOTE, model.getNote());
            cv.put(ModelTask.COL_COLOR, model.getColor());
            cv.put(ModelTask.COL_TIME_CREATE, model.getTimeCreate());
            cv.put(ModelTask.COL_TIME_TARGET, model.getTimeTarget());
            cv.put(ModelTask.COL_TIME_DONE, model.getTimeDone());
            cv.put(ModelTask.COL_TIME_SORT, model.getTimeSort());
            cv.put(ModelTask.COL_STATUS, model.getStatus());
            //使用最终插入的id来绑定
            id = (int) db.insert(TableTask.TableName(), null, cv);
            //遍历添加自定义
            for (ModelTaskSub item : model.getSub())
            {
                item.setIdTask(id);
                TaskSubAdd(item);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
        return id;
    }

    //更新任务
    public void TaskCheck(ModelTask model)
    {
        try
        {
            db = helper.getWritableDatabase();
            String[] whereParams = new String[]{model.getID() + ""};
            ContentValues cv = new ContentValues();
            cv.put(ModelTask.COL_TIME_DONE, model.getTimeDone());
            cv.put(ModelTask.COL_STATUS, model.getStatus());
            db.update(TableTask.TableName(), cv, ModelTask.COL_ID + " = ? ", whereParams);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

    //更新位置
    public void TaskPosition(ModelTask model)
    {
        try
        {
            db = helper.getWritableDatabase();
            String[] whereParams = new String[]{model.getID() + ""};
            ContentValues cv = new ContentValues();
            cv.put(ModelTask.COL_TIME_SORT, model.getTimeSort());
            db.update(TableTask.TableName(), cv, ModelTask.COL_ID + " = ? ", whereParams);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

    //更新任务
    public void TaskUpdate(ModelTask model)
    {
        try
        {
            db = helper.getWritableDatabase();
            String[] whereParams = new String[]{model.getID() + ""};
            ContentValues cv = new ContentValues();
            cv.put(ModelTask.COL_CONTENT, model.getContent());
            cv.put(ModelTask.COL_NOTE, model.getNote());
            cv.put(ModelTask.COL_COLOR, model.getColor());
            cv.put(ModelTask.COL_TIME_TARGET, model.getTimeTarget());
            cv.put(ModelTask.COL_TIME_DONE, model.getTimeDone());
            cv.put(ModelTask.COL_TIME_SORT, model.getTimeSort());
            cv.put(ModelTask.COL_STATUS, model.getStatus());
            db.update(TableTask.TableName(), cv, ModelTask.COL_ID + " = ? ", whereParams);
            //先删除后添加
            db.delete(TableTaskSub.TableName(), ModelTaskSub.COL_ID_TASK + " = ? ", whereParams);
            //遍历添加自定义
            for (ModelTaskSub item : model.getSub())
            {
                item.setIdTask(model.getID());
                TaskSubAdd(item);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

    //删除任务
    public void TaskDelete(ModelTask model)
    {
        try
        {
            db = helper.getWritableDatabase();
            String[] whereParams = new String[]{model.getID() + ""};
            db.delete(TableTask.TableName(), ModelTask.COL_ID + " = ? ", whereParams);
            db.delete(TableTaskSub.TableName(), ModelTaskSub.COL_ID_TASK + " = ? ", whereParams);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

    //获取子任务
    public List<ModelTaskSub> TaskSubGet(int id_task)
    {
        List<ModelTaskSub> rList = new ArrayList<>();
        try
        {
            db = helper.getWritableDatabase();
            String[] whereParams = new String[]{id_task + ""};
            Cursor c = db.query(TableTaskSub.TableName(), TableTaskSub.TableColumns(), ModelTaskSub.COL_ID_TASK + " = ? ", whereParams, null, null, null);
            while (c.moveToNext())
            {
                ModelTaskSub temp = new ModelTaskSub();
                temp.setID(c.getInt(c.getColumnIndex(ModelTaskSub.COL_ID)));
                temp.setIdTask(c.getInt(c.getColumnIndex(ModelTaskSub.COL_ID_TASK)));
                temp.setContent(c.getString(c.getColumnIndex(ModelTaskSub.COL_CONTENT)));
                temp.setStatus(c.getInt(c.getColumnIndex(ModelTaskSub.COL_STATUS)));
                rList.add(temp);
            }
            c.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
        return rList;
    }

    //添加子任务
    public void TaskSubAdd(ModelTaskSub model)
    {
        try
        {
            db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(ModelTaskSub.COL_ID_TASK, model.getIdTask());
            cv.put(ModelTaskSub.COL_CONTENT, model.getContent());
            cv.put(ModelTaskSub.COL_STATUS, model.getStatus());
            db.insert(TableTaskSub.TableName(), null, cv);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

}
