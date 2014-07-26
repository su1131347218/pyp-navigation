package pyp.navigation.association.two;

import pyp.navigation.global.PypBaseFragment;
import pyp.navigation.main.MenuActivity;
import pyp.navigation.main.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Title: FragmentTwo
 * @Description: 社团模块 - 子页面 - 2
 * @author qsuron
 * @date 2014-7-24
 * @email admin@qiushurong.cn
 */
public class FragmentTwo extends Fragment implements PypBaseFragment {
	private MenuActivity parentActivity;
	private View parentView;;

	public FragmentTwo() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
		parentView = inflater.inflate(R.layout.association_fragment_two,
				container, false);
		parentActivity = (MenuActivity) this.getActivity();
	}

	@Override
	public void initListensers() {
	}
}
