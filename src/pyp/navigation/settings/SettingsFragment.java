package pyp.navigation.settings;

import pyp.navigation.global.PypBaseFragment;
import pyp.navigation.main.MenuActivity;
import pyp.navigation.main.R;
import pyp.navigation.main.ResideMenu;
import pyp.navigation.theme.ISkin;
import pyp.navigation.theme.ISkinManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @Title: SettingsFragment
 * @Description: 设置模块页面
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public class SettingsFragment extends Fragment implements PypBaseFragment,ISkinManager{
	private View parentView;
	private ISkin iSkin;
	private Button btn_changeSkin;
	private Button btn_main_titlebar_left_menu;						//界面做左菜单按钮
    private Button btn_main_titlebar_right_menu;					//界面做右菜单按钮
	private int i = 0;
	private MenuActivity parentActivity;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	initData();
        initViews(inflater, container);
        initListensers();
		return parentView;
    }

	@Override
	public void initData() {
	}

	@Override
	public void initViews(LayoutInflater inflater, ViewGroup container) {
		parentView = inflater.inflate(R.layout.main_settings, container, false);
		parentActivity = (MenuActivity) this.getActivity();
		btn_changeSkin = (Button) parentView.findViewById(R.id.setting_btn1);
		//菜单栏左右按钮
        btn_main_titlebar_left_menu = (Button) parentView.findViewById(R.id.main_titlebar_left_menu);
        btn_main_titlebar_right_menu = (Button) parentView.findViewById(R.id.main_titlebar_right_menu);
	}

	@Override
	public void initListensers() {
		btn_changeSkin.setOnClickListener(this);
		btn_main_titlebar_left_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				parentActivity.getResideMenu().openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		btn_main_titlebar_right_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				parentActivity.getResideMenu().openMenu(ResideMenu.DIRECTION_RIGHT);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		//Log.i("qsuron","onClick-"+iSkin);
		i = (i+1)%2;
		iSkin.changeSkin(i);
	}

	@Override
	public void setSkin(ISkin iSkin) {
		this.iSkin = iSkin;
	}
}