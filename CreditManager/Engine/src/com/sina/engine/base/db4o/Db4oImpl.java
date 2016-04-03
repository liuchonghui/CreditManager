package com.sina.engine.base.db4o;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.text.TextUtils;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.utils.Utils;

/**
 * 数据库实现类
 * @author kangshaozhe
 *
 */
public class Db4oImpl {
	private String dbPath = "";

	private ObjectContainer db;

	private boolean isDebug;

	public Db4oImpl(String dbName){
		isDebug = EngineManager.DEBUG;
		String dbPath = getDbPath(dbName);
		if (Db4oManager.locks.get(dbPath) == null) {
			Db4oManager.locks.put(dbPath, new Object());
		}
	}

	/**
	 * 获取数据库路径
	 * @return
	 */
	public  String getDbPath(String dbName){
		if(Db4oManager.context == null){
			return dbPath;
		}
		try{
			File cacheDir = null;
			if(isDebug){
				cacheDir = Utils.getFileCachePath(Db4oManager.context,"/db");
			}
			else {
				cacheDir = Utils.getFileDbPath(Db4oManager.context);
			}
			if (cacheDir!=null && !cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			dbPath = cacheDir.getPath()+"/"+dbName;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return dbPath;
	}

	/**
	 *  打开数据库
	 * @param dbName
	 * @return
	 */
	public Db4oImpl open(){
		if(TextUtils.isEmpty(dbPath)){
			return this;
		}
		synchronized (Db4oManager.locks.get(dbPath)) {
			try{
				if (!isDbOpen()&&!TextUtils.isEmpty(dbPath)) {
					db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), dbPath);
				}
			}
			catch(Exception e){
				e.printStackTrace();
				try {
					deleteDbFile();
				} finally {
				}
			}
		}
		return this;
	}


	/**
	 * 关闭数据库
	 * @return
	 */
	public void close(){
		synchronized (Db4oManager.locks.get(dbPath)) {
			try{
				if (db != null) {
					db.close();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断db 是否打开
	 * @return
	 */
	public boolean isDbOpen(){
		if (db == null || db.ext().isClosed()) {
			return false;
		}
		return true;
	}

	/**
	 * 更新或者插入
	 * @param db
	 * @param saveObj
	 * @param predicate
	 */
	public  <T extends Db4oInterface<T>> void updateSave(ObjectContainer db,T saveObj,Predicate<T> predicate){
		if(db == null||predicate == null||saveObj == null){
			return;
		}
		boolean isUpdate = false;
		ObjectSet<T> result = db.query(predicate);
		while (result.hasNext()) {
			T item = result.next();
			if(item != null){
				item.objectUpdate(saveObj);
				db.store(item);
				isUpdate = true;
				break;
			}
		}
		if(!isUpdate){
			db.store(saveObj);
		}

	}

	/**
	 * 储存单个数据对象
	 * @param saveObj
	 * @param predicate
	 * @param relateClass
	 */
	public  <T extends Db4oInterface<T>> void save(T saveObj,Predicate<T> predicate,String... relateClass) {
		if(!isDbOpen()||saveObj == null||predicate == null){
			return;
		}
		synchronized (Db4oManager.locks.get(dbPath)) {
			try{
				if(relateClass != null&&relateClass.length > 0){
					for(int i=0;i<relateClass.length;i++){
						db.ext().configure().objectClass(relateClass[i]).cascadeOnUpdate(true);
					}
				}
				updateSave(db,saveObj,predicate);
				db.commit();
			}
			catch(Exception e){
				e.printStackTrace();
				deleteDbFile();
			}
		}
	}

	/**
	 * 查询对象集合
	 * @param predicate
	 * @param comparator
	 * @return
	 */
	public <T extends Db4oInterface<T>> List<T> getList(Predicate<T> predicate,Comparator<T> comparator){
		List<T> getDbList = new ArrayList<T>();
		if(!isDbOpen()||predicate == null){
			return getDbList;
		}
		synchronized (Db4oManager.locks.get(dbPath)) {
			try{
				ObjectSet<T> result = db.query(predicate,comparator);
				while (result.hasNext()) {
					T item = result.next();
					getDbList.add(item);
				}
			}
			catch(Exception e){
				e.printStackTrace();
				deleteDbFile();
			}
		}
		return getDbList;
	}

	public <T extends Db4oInterface<T>> List<T> getListNoComparator(Predicate<T> predicate){
		List<T> getDbList = new ArrayList<T>();
		synchronized (Db4oManager.locks.get(dbPath)) {
			try{
				ObjectSet<T> result = db.query(predicate);
				while (result.hasNext()) {
					T item = result.next();
					getDbList.add(item);
				}
			}
			catch(Exception e){
				e.printStackTrace();
				deleteDbFile();
			}
		}
		return getDbList;
	}

	public <T extends Db4oInterface<T>> List<T> getList(Class<T> classOfT){
		List<T> getDbList = new ArrayList<T>();
		synchronized (Db4oManager.locks.get(dbPath)) {
			try{
				ObjectSet<T> result = db.query(classOfT);
				while (result.hasNext()) {
					T item = result.next();
					getDbList.add(item);
				}
			}
			catch(Exception e){
				e.printStackTrace();
				deleteDbFile();
			}
		}
		return getDbList;
	}

	/**
	 * 删除对象
	 * @param predicate
	 * @param relateClass
	 */
	public  <T> void delete(Predicate<T> predicate,String... relateClass) {
		if (!isDbOpen()||predicate == null) {
			return;
		}
		synchronized (Db4oManager.locks.get(dbPath)) {

			try{
				if(relateClass != null&&relateClass.length > 0){
					for(int i=0;i<relateClass.length;i++){
						db.ext().configure().objectClass(relateClass[i]).cascadeOnDelete(true);
					}
				}
				ObjectSet<T> result = db.query(predicate);
				while (result.hasNext()) {
					T item = result.next();
					if(item != null){
						db.delete(item);
					}
				}
				db.commit();
			}
			catch(Exception e){
				e.printStackTrace();
				deleteDbFile();
			}

		}
	}

	/**
	 * 分页查询
	 * @param page
	 * @param count
	 * @param predicate
	 * @param comparator
	 * @return
	 */
	public  <T> List<T> getPageList(int page,int count,Predicate<T> predicate,Comparator<T> comparator){
		List<T> getPageList = new ArrayList<T>();
		if(!isDbOpen()||predicate == null){
			return getPageList;
		}
		synchronized (Db4oManager.locks.get(dbPath)) {
			try{
				ObjectSet<T> result = db.query(predicate,comparator);
				int total = result.size();
				int start = page-1>=0?(page-1)*count:0;
				int end = page*count;
				for(int i = start; i < end; i++){
					if(i >= total){
						break;
					}
					getPageList.add(result.get(i));

				}
			}
			catch(Exception e){
				e.printStackTrace();
				deleteDbFile();
			}
		}
		return getPageList;
	}

	public  <T> List<T> getPageList(int page,int count,Predicate<T> predicate){
		List<T> getPageList = new ArrayList<T>();
		if(!isDbOpen()||predicate == null){
			return getPageList;
		}
		synchronized (Db4oManager.locks.get(dbPath)) {
			try{
				ObjectSet<T> result = db.query(predicate);
				int total = result.size();
				int start = page-1>=0?(page-1)*count:0;
				int end = page*count;
				for(int i = start; i < end; i++){
					if(i >= total){
						break;
					}
					getPageList.add(result.get(i));

				}
			}
			catch(Exception e){
				e.printStackTrace();
				deleteDbFile();
			}
		}
		return getPageList;
	}

	/**
	 * 删除数据库文件
	 */
	public void deleteDbFile(){
		if(TextUtils.isEmpty(dbPath)){
			return;
		}
		Db4oUitls.deteleFile(dbPath);
	}
}
