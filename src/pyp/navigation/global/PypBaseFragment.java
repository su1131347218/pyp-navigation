package pyp.navigation.global;

import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * 所有Fragment的模版，要求项目中所有fragment都要继承于Fragment并实现此PypBaseFragment
 *  extends Fragment implements PypBaseFragment
 *  必须有成员变量private View parentView;
 * onCreateView()函数请重新实现，并return parentView;
 * 	 *
	 * onCreateView()函数必须包含：
    	initData();
        initViews(inflater, container);
        initListensers();
        return parentView;
	 *
 */

/**
 * @Title: PypBaseFragment
 * @Description: 项目Fragment规范接口
 * @author qsuron
 * @date 2014-7-15
 * @email admin@qiushurong.cn
 */
public interface PypBaseFragment{
	/**
	 * MVC-Model-初始化数据
	 * 抽取程序的数据逻辑，整合到函数init()中；
	 */
	public abstract void initData();
	
	/**
	 * MVC-View-初始化界面UI
	 * 抽取所有界面元件的定义和实现，整合到单独一个函数initViews()中
	 */
	public abstract void initViews(LayoutInflater inflater,ViewGroup container);
	
	/**
	 * MVC-Controller-初始化监听器
	 * 抽取程序的监听器逻辑，整合到函数initListensers()中；
	 */
	public abstract void initListensers();
	
}