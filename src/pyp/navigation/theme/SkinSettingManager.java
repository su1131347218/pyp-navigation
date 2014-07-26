package pyp.navigation.theme;

import pyp.navigation.main.MenuActivity;
import pyp.navigation.main.R;
import android.content.SharedPreferences;

/**
 * @Title: SkinSettingManager
 * @Description: 皮肤管理类(暂未完善,暂时别动它)
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public class SkinSettingManager {
	public final static String SKIN_PREF = "skinSetting";
	public SharedPreferences skinSettingPreference;
	private MenuActivity mActivity;
	
	private int[] skinResources = { 
			R.drawable.main_menu_background,
			R.drawable.main_menu_background2};
	

	public SkinSettingManager(MenuActivity activity) {
		this.mActivity = activity;
		skinSettingPreference = mActivity.getSharedPreferences(SKIN_PREF, 3);
	}

	public int getSkin(String key) {
		return skinSettingPreference.getInt(key, 0);
	}

	public void setSkin(String key,int j) {
		SharedPreferences.Editor editor = skinSettingPreference.edit();
		editor.putInt(key, j);
		editor.commit();
	}

	public int getCurrentSkinRes() {
		return skinResources[getSkin("菜单栏背景")];
	}

	public void toggleSkins() {
		int skin = getSkin("菜单栏背景");
		if (skin == skinResources.length - 1) {
			skin = 0;
		} else {
			skin++;
		}
		setSkin("菜单栏背景",skin);
		try {
			mActivity.getResideMenu().setBackground(getCurrentSkinRes());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void initSkins() {
		mActivity.getResideMenu().setBackground(getCurrentSkinRes());
	}
}
