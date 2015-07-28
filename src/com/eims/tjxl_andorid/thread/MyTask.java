package com.eims.tjxl_andorid.thread;

import android.os.AsyncTask;

/**
 * 异步任务
 * @author kuangyong
 *
 */
public class MyTask extends AsyncTask<Integer, Integer, Object> {

	private TaskCallback callback;
	public MyTask(TaskCallback callback){
		this.callback = callback;
	}
	
	
	@Override
	protected Object doInBackground(Integer... arg0) {
		if(null != callback){
			return callback.run();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if(null != callback){
			callback.dataCallback(result);
		}
	}
	
	public interface TaskCallback{
		
		public Object run();
	
		public void dataCallback(Object result);
	}
}
