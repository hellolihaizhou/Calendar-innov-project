package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;



public class MySharedPreferences  {

	private Context mContext;
	private SharedPreferences sp;
	private Editor editor;
	public MySharedPreferences(Context Context,String name)
	{
		this.mContext=Context;
		sp=mContext.getSharedPreferences(name,mContext.MODE_PRIVATE);
		editor=sp.edit();
	}
	/**设置值
	 * @param key    键名
	 * @param setValue 设置值
	 */
	public void setString(String key, String setValue)
	{
		editor.putString(key, setValue);
		editor.commit();
	}
	/**设置值
	 * @param key    键名
	 * @param setValue 设置值
	 */
	public void setBoolean(String key, Boolean setValue)
	{
		editor.putBoolean(key, setValue);
		editor.commit();
	}
	/**设置值
	 * @param key    键名
	 * @param setValue 设置值
	 */
	public void setDouble(String key, Double setValue)
	{
    /*	editor.putString(key, setValue+"");
    	editor.commit();*/
		setString(key,setValue+"");
	}

	public Double getDouble(String key, Double defValue)
	{
		return Double.parseDouble(getString(key,defValue+""));
	}

	/**取值
	 * @param key
	 * @param defValue 默认值
	 * @return
	 */
	public String getString(String key, String defValue) {
		// TODO Auto-generated method stub
		return sp.getString(key, defValue);
	}

	/**取值
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key,boolean is) {
		// TODO Auto-generated method stub
		return sp.getBoolean(key, is);
	}
	public void setInt(String key, int value)
	{
		editor.putInt(key, value);
		editor.commit();
	}
	public int getInt(String key)
	{

		return sp.getInt(key,-1);
	}
}
