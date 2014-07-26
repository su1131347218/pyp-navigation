package pyp.navigation.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.io.PrintStream;

/**
 * @Title: APPDataPrefrences
 * @Description: 用于记录各种软件信息
 * @author qsuron
 * @date 2014-7-26
 * @email admin@qiushurong.cn
 */
public class APPDataPrefrences {
	private SharedPreferences.Editor editor;
	private SharedPreferences preferences;

	public APPDataPrefrences(Context paramContext) {
		this.preferences = paramContext.getSharedPreferences("appdata", 1);
		this.editor = this.preferences.edit();
	}

	public String getStrValue(String paramString1, String paramString2) {
		return this.preferences.getString(paramString1, paramString2);
	}

	public boolean putStrValue(String paramString1, String paramString2) {
		if ((paramString1 != null) && (!paramString1.equals(""))) {
			// Log.i("qsuron","key  " + paramString1 + "  value   " +
			// paramString2);
			this.editor.putString(paramString1, paramString2);
			return this.editor.commit();
		}
		return false;
	}
}

